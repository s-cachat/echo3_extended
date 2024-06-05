package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.flotchart.FlotChartPeer;
import com.cachat.prj.echo3.interfaces.Styles;
import com.cachat.prj.echo3.interfaces.User;
import com.cachat.util.CacheMap;
import com.cachat.util.StringUtil;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.*;
import nextapp.echo.app.event.WindowPaneEvent;
import nextapp.echo.app.event.WindowPaneListener;
import nextapp.echo.webcontainer.command.BrowserOpenWindowCommand;
import org.jasypt.util.digest.Digester;

/**
 *
 * @author scachat
 */
public abstract class BaseApp extends ApplicationInstance implements LocalisedItem {

    /**
     * Logger local
     */
    protected static final transient Logger logger = Logger.getLogger("com.cachat.prj.wir.gui");

    /**
     * nom de l'instance par défaut dans la session http
     */
    public static final String DEFAULT_INSTANCE_NAME = "echo3sstApp";

    /**
     * langues disponibles
     */
    public static Locale[] AVAILABLE_LANGUAGES;

    /**
     * codes des langues disponibles
     */
    public static String[] AVAILABLE_LANGUAGE_CODES;

    /**
     * reference explicite
     */
    protected BaseApp app = this;

    /**
     * la fenetre
     */
    protected Window window;

    /**
     * la version d'interface minimum supportée
     */
    protected IfaceVersion minVersion = IfaceVersion.WEB_V5;

    /**
     * version de l'interface
     */
    protected IfaceVersion ifaceVersion = IfaceVersion.WEB_V5;

    /**
     * l'instance de fenêtre de connexion (null en dehors des phases de
     * connexion)
     */
    protected MainPane loginBasePane;

    /**
     * mode superAdmin
     */
    private boolean superAdmin = false;
    /**
     * mode superAdmin pour l'utilisateur initial
     */
    private Boolean initialSuperAdmin = null;

    /**
     * identifiant pour la fenêtre de connexion
     */
    private String loginName;

    /**
     * l'identite de l'utilisateur connecte initialement (ne change pas lors
     * d'un impersonate)
     */
    private User initialUser = null;
    /**
     * l'identite de l'utilisateur connecte
     */
    private User user = null;

    /**
     * la cache des visioneurs d'images
     */
    private Map<String, ImageViewer> imageViewers = new HashMap<>();

    /**
     * le nettoyeur de fenetres
     */
    private final WindowPaneListener wpl = (WindowPaneEvent e) -> removeWindow((WindowPane) e.getSource());

    /**
     * la liste des lien entre fenetre (liste/detail)
     */
    private final HashMap<WindowPane, WindowPane> windowsLinks = new HashMap<>();

    /**
     * Ressources
     */
    private transient ResourceBundle res;

    /**
     * Cache des ressources
     */
    private static final Map<Locale, ResourceBundle> resCache = new HashMap<>();

    /**
     * le cache des documents
     */
    private CacheMap<String, Doc> cm = new CacheMap<>(600000l);

    /**
     * la queue des evenements push
     */
    private TaskQueueHandle taskQueue = createTaskQueue();

    /**
     * la limite de la queue des evenements push
     */
    protected int taskRateLimite = 3;

    /**
     * timer pour l'envoi des taches avec une limite
     */
    private Timer taskLimitTimer;

    /**
     * liste des taches
     */
    private final List<Runnable> taskWithLimits = new ArrayList<>();

    /**
     * Constructeur
     */
    protected BaseApp() {
        new FlotChartPeer();//initialisation statique des ressource à charger
    }

    /**
     * Initialise l'application
     *
     * @return la fenêtre principale
     * @see nextapp.echo2.app.ApplicationInstance#init()
     */
    @Override
    public Window init() {
        logger.info("Nouvelle session");
        setLocale(Locale.FRENCH);
        window = new Window();
        window.setTitle(getResource().getString("app.title"));
        setStyleSheet(getStyles().getDefaultStyleSheet());
        doLogin();
        return window;
    }

    /**
     * Crée une fenêtre et l'affiche. La fenêtre n'a pas de parent.
     *
     * @param newPane le nom de la classe de la fenêtre
     * @return false si une classe n'est pas trouvée (ce qui implique un
     * problème de license)
     */
    public boolean addOrActivatePane(String newPane) {
        WindowPane wp = null;
        if (newPane != null) {
            try {
                Class<? extends WindowPane> clazz = (Class<? extends WindowPane>) Class.forName(newPane);
                Constructor<? extends WindowPane> cons = clazz.getConstructor(BaseApp.class);
                wp = cons.newInstance(BaseApp.this);
            } catch (ClassNotFoundException ex) {
                logger.log(Level.SEVERE, "Classe non trouvée " + newPane, ex);
                return false;
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Impossible d'instancier la classe " + newPane, ex);
                wp = null;
            }

        }
        if (wp != null) {
            BaseApp.this.addWindow(wp, null);
        }
        return true;
    }

    /**
     * Saute l'étape de login
     */
    public void skipLogin() {
        User u = () -> "Anonymous";
        setUser(u);
        clearWindows();
        setMainPane(app.getMainPane());
        getMainPane().updateMenu();
    }

    /**
     * Demande l'authentification d'un utilisateur
     */
    public void doLogin() {
        boolean ok;
        String msg = null;
        try {
            ok = checkSSOLogin();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Erreur", e);
            msg = e.getMessage();
            ok = false;
        }
        String c = BaseAppServlet.getCookies().getCookie(appCode() + ".stayConnected");
        if (c != null) {
            logger.log(Level.INFO, "stay connected login : {0}", user);
            user = app.validTokenLogin(c);
            ok = user != null;
        }
        if (!ok) {
            user = null;
            logger.log(Level.INFO, "logout : {0}", user);
            window.setContent(loginBasePane = newLoginPane());
            if (loginBasePane instanceof WithErrorStatusField withErrorStatusField) {
                withErrorStatusField.setErrorMsg(msg);
            }
        } else {
            clearWindows();
            MainPane mainPane = getMainPane();
            if (mainPane != null) {
                setMainPane(mainPane);
                mainPane.updateMenu();
                app.toast(getString("app.autoLogin"));
            }
        }
    }

    /**
     * Avant d'afficher la fenêtre de login, vérifie si un login SSO est
     * possible. Doit renseigner les info utilisateurs si le login SSO est
     * présent et valide.
     *
     * @return true si un login SSO était présent et valide, false sinon.
     * @throws Exception en cas d'erreur
     */
    public boolean checkSSOLogin() throws Exception {
        return false;
    }

    /**
     * Déconnecte l'utilisateur
     */
    public void logout() {
        if (initialUser != null && user != null && !user.equals(initialUser)) {
            setUser(initialUser);
            setSuperAdmin(initialSuperAdmin);
            app.toast("Vous êtes maintenant " + user.getLibelle() + (isSuperAdmin() ? "(*)" : ""));
        } else {
            storeLoginToken(user, null);
            clearWindows();
            this.user = null;
            this.initialUser = null;
            BaseAppServlet.logout();
        }
    }

    /**
     * Donne une fenêtre de connexion
     *
     * @return la fenêtre
     */
    protected MainPane newLoginPane() {
        return new LoginBasePane(this);
    }

    /**
     * Ajoute une fenêtre
     *
     * @param w la fenêtre
     * @param parent si il existe une relation type liste-détail, la fenêtre
     * parente
     */
    public void addWindow(WindowPane w, WindowPane parent) {
        MainPane mainPane = getMainPane();
        if (w != null) {
            if (parent != null) {
                synchronized (windowsLinks) {
                    windowsLinks.put(w, parent);
                }
            }
            mainPane.addWindow(w, parent);
            w.addWindowPaneListener(wpl);
            w.setDefaultCloseOperation(WindowPane.DO_NOTHING_ON_CLOSE);
        }
        mainPane.windowsUpdated();
    }

    /**
     * Ajoute une fenêtre, quand on n'est pas dans le thread echo2
     *
     * @param parent la fenêtre parente (au sens liste/detail) ou null
     * @param pane la fenetre
     */
    public void delayedAddWindow(WindowPane pane, WindowPane parent) {
        enqueueTask(() -> addWindow(pane, parent));
    }

    /**
     * Supprime toutes les fenetres
     */
    public void clearWindows() {
        MainPane mainPane = getMainPane();
        synchronized (windowsLinks) {
            windowsLinks.clear();
            if (mainPane != null) {
                mainPane.clearWindows();
            }
        }
        if (mainPane != null) {
            mainPane.windowsUpdated();
        }
    }

    /**
     * Supprime une fenetre
     */
    public void removeWindow(WindowPane w) {
        MainPane mainPane = getMainPane();
        synchronized (windowsLinks) {
            WindowPane parent = windowsLinks.remove(w);
            if (w instanceof WindowPaneListener) {
                ((WindowPaneListener) w).windowPaneClosing(new WindowPaneEvent(""));
            }
            mainPane.removeWindow(w);
            mainPane.addWindow(parent, windowsLinks.get(parent));
            logger.log(Level.FINEST, "Supression de la fenetre {0}", w.getTitle());
        }
        mainPane.windowsUpdated();
    }

    /**
     * Supprime toutes les fenetres
     */
    public void removeAllWindow() {
        MainPane mainPane = getMainPane();
        synchronized (windowsLinks) {
            for (WindowPane w : windowsLinks.keySet()) {
                if (w instanceof WindowPaneListener) {
                    ((WindowPaneListener) w).windowPaneClosing(new WindowPaneEvent(""));
                }
                mainPane.removeWindow(w);
            }

            mainPane.addWindow(null, null);
        }
        mainPane.windowsUpdated();
    }

    /**
     * Affiche une popup avec une exception
     *
     * @param ex l'exception
     */
    public void showException(Exception ex) {
        logger.log(Level.SEVERE, "Exception displayed to user", ex);
        addWindow(new MessagePane(app, ex.getMessage(), "error", "<pre>" + StringUtil.valueOf(ex) + "</pre>", MessagePane.Icon.ERROR, 800, 600, true), null);
    }

    /**
     * Affiche un message fugitif en bas de l'écran
     *
     * @param message le message
     */
    public void toast(String message) {
        MainPane mainPane = getMainPane();
        if (mainPane != null) {
            mainPane.toast(message);
        }
    }

    /**
     * Affiche un message fugitif en bas de l'écran
     *
     * @param message le message
     */
    public void toastError(String message) {
        MainPane mainPane = getMainPane();
        if (mainPane != null) {
            mainPane.toastError(message);
        }
    }

    /**
     * Enregistre un evenement push
     *
     * @param run l'éxécutable
     */
    public synchronized void enqueueTask(Runnable run) {
        if (taskQueue == null) {
            taskQueue = app.createTaskQueue();
        }
        enqueueTask(taskQueue, run);
    }

    /**
     * Enregistre un evenement push, avec une limite pour éviter la saturation
     * du client. Les taches seront exécutée seulement une fois toutes les
     * taskRateLimit secondes
     *
     * @param run l'éxécutable
     */
    public void enqueueTaskWithRateLimit(Runnable run) {
        synchronized (taskWithLimits) {
            taskWithLimits.add(run);
            if (taskLimitTimer == null) {
                taskLimitTimer = new Timer();
                taskLimitTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (taskWithLimits) {
                            while (!taskWithLimits.isEmpty()) {
                                enqueueTask(taskWithLimits.remove(0));
                            }
                        }
                    }
                }, 100, taskRateLimite * 1000);
            }
        }
    }

    /**
     * Invoked when the application is disposed and will not be used again.
     * Implementations must invoke <code>super.dispose()</code>.
     */
    @Override
    public void dispose() {
        this.user = null;
        if (taskQueue != null) {
            removeTaskQueue(taskQueue);
        }
        if (taskLimitTimer != null) {
            taskLimitTimer.cancel();
        }
        taskLimitTimer = null;
        clearWindows();
        super.dispose();
    }

    /**
     * Donne un document
     *
     * @param id l'identifiant unique du document
     * @return le document
     */
    public Doc findDoc(String id) {
        return cm.get(id);
    }

    /**
     * Envoi un document au navigateur
     *
     * @param contentType le type de contenu
     * @param name le nom du document
     * @param data le contenu
     */
    public void sendDoc(String contentType, String name, byte[] data) {
        Doc d = new Doc(contentType, name, data, getUser());
        cm.put(d.getUid(), d);
        Command command = new BrowserOpenWindowCommand(String.format("documents/%s/%s", d.getUid(), d.getName()), "extra", new Extent(800), new Extent(600), BrowserOpenWindowCommand.FLAG_REPLACE | BrowserOpenWindowCommand.FLAG_RESIZABLE);
        enqueueCommand(command);
    }

    /**
     * Envoi un document au navigateur
     *
     * @param contentType le type de contenu
     * @param name le nom du document
     * @param file le contenu
     */
    public void sendDoc(String contentType, String name, File file) {
        logger.info("Sending doc " + file.getAbsolutePath());
        Doc d = new Doc(contentType, name, file, getUser());
        cm.put(d.getUid(), d);
        Command command = new BrowserOpenWindowCommand(String.format("documents/%s/%s", d.getUid(), d.getName()), "extra", new Extent(800), new Extent(600), BrowserOpenWindowCommand.FLAG_REPLACE | BrowserOpenWindowCommand.FLAG_RESIZABLE);
        enqueueCommand(command);
    }

    /**
     * Inscrire un visioneur d'image
     *
     * @param iv le visioneur d'image
     */
    void registerImageViewer(ImageViewer iv) {
        if (iv.getId() != null) {
            imageViewers.put(iv.getId(), iv);
        }
    }

    /**
     * Désinscrire un visioneur d'image
     *
     * @param iv le visioneur d'image
     */
    void unregisterImageViewer(ImageViewer iv) {
        imageViewers.remove(iv.getId());
    }

    //<editor-fold desc="Abstract methods" defaultstate="collapsed">
    /**
     * Donne le style actif
     *
     * @return le style actif
     */
    public abstract Styles getStyles();

    /**
     * Donne le conteneur principal correspondant a la version de l'interface
     *
     * @return le conteneur principal
     */
    protected abstract MainPane getMainPane();

    /**
     * Donne l'url du logo du propriétaire
     *
     * @return l'url du logo du propriétaire
     */
    public abstract String getUrlLogoProprietaire();

    /**
     * Instancie une nouvelle home window (celle qui s'affiche par defaut)
     *
     * @return la nouvelle home window
     */
    public abstract HomeWindow newHomeWindow();

    /**
     * Valide l'identification. Ne doit pas interagir avec app, seulement
     * retourner true ou false.
     *
     * @param name l'utilisateur
     * @param pass le mot de passe
     * @return le user si l'authentification reussi, sinon null
     */
    public abstract User validLogin(String name, String pass);

    /**
     * enregistre un jeton d'authentification automatique pour l'utilisateur.
     * L'implémentation par défaut ne fait rien.
     *
     * @param user l'utilisateur
     * @param token le jeton (null pour l'annuler)
     * @return true si le token a pu être stocké, false si il n'est pas valide
     * (non unique par exemple)
     */
    public boolean storeLoginToken(User user, String token) {
        return false;
    }

    /**
     * Valide l'identification par un jeton d'authentification. Ne doit pas
     * interagir avec app, seulement retourner true ou false. L'implémentation
     * par défaut retourne toujours null.
     *
     * @param token le token
     * @return le user si l'authentification reussi, sinon null
     */
    public User validTokenLogin(String token) {
        return null;
    }

    /**
     * Donne le nom de l'EntityManager
     *
     * @return le nom de l'netity manager
     */
    public abstract String getEntityManagerName();
    /**
     * donne le nom de l'application. Par défaut dérive ce nom de l'entity
     * manager name
     */
    /**
     * code application
     */
    private static String appCode;

    /**
     * donne le code application
     *
     * @return le code
     */
    public String appCode() {
        if (appCode == null) {
            Digester d = new Digester();
            appCode = new String(org.apache.commons.codec.binary.Hex.encodeHex(d.digest(getEntityManagerName().getBytes()))).replace('=', '0');
        }
        return appCode;
    }

    /**
     * Donne la locale par défaut
     *
     * @return la locale par défaut
     */
    public abstract Locale getDefaultLocale();

    /**
     * change la locale
     *
     * @param loc la nouvelle
     */
    public abstract void setMyLocale(Locale loc);

    /**
     * Donne l'URL de la page d'aide
     *
     * @return l'URL
     */
    public String getHelpUrl() {
        return null;
    }

    //</editor-fold>
    //<editor-fold desc="Getters/Setters" defaultstate="collapsed">
    /**
     * change le mode superAdmin
     *
     * @param superAdmin si true, superadmin
     */
    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
        if (initialSuperAdmin = null) {
            initialSuperAdmin = superAdmin;
        }
    }

    /**
     * teste si on est en superAdmin
     *
     * @return true si superAdmin
     */
    public boolean isSuperAdmin() {
        return superAdmin;
    }

    /**
     * fixe l'identité de l'utilisateur
     *
     * @param loginName l'objet utilisateur
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * donne le nom de connexion par d�faut
     *
     * @return l'identifiant
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * fixe l'utilisateur connecté
     *
     * @param user l'utilisateur
     */
    public void setUser(User user) {
        if (this.initialUser == null) {
            initialUser = user;
        }
        this.user = user;
    }

    /**
     * donne l'identite de l'utilisateur connecte
     *
     * @return l'identité
     */
    public User getUser() {
        return user;
    }

    /**
     * donne l'identite de l'utilisateur connecte initialement (on l'enregistre
     * sur l'appel de setUser, si a ce moment user est null)
     *
     * @return l'identité
     */
    public User getInitialUser() {
        return initialUser;
    }

    /**
     * Change la locale
     *
     * @param loc la locale
     */
    @Override
    public void setLocale(Locale loc) {
        super.setLocale(loc);
        this.res = getResource(loc);
    }

    /**
     * Donne les resources de cette instance
     *
     * @return la ressource
     */
    public ResourceBundle getResource() {
        if (res == null) {
            setLocale(getDefaultLocale());
            assert res != null;
        } else {
            logger.log(Level.FINEST, "LOCALE : App.getResource() for locale {0} app locale is {1}",
                    new Object[]{res.getLocale(), getLocale()});
        }
        return res;
    }

    /**
     * Donne une ressource pour une locale
     *
     * @param loc la locale
     * @return la ressource
     */
    public static ResourceBundle getResource(Locale loc) {
        ResourceBundle rb = resCache.get(loc);
        if (rb != null) {
            return resCache.get(loc);
        } else {

            synchronized (resCache) {
                rb = resCache.get(loc);//on le refait de fa�on synchrone
                if (rb == null) {
                    rb = new MyResourceBundle(loc);
                    resCache.put(loc, rb);
                }
                return rb;
            }

        }
    }

    /**
     * Donne une ressource. Synonyme de getString pour BaseApp (car pas de
     * préfixe ici)
     *
     * @param n la clé
     * @return le string
     */
    @Override
    public String getBaseString(String n) {
        return getString(n);
    }

    /**
     * Donne une ressource
     *
     * @param n la clé
     * @return le string
     */
    @Override
    public String getString(String n) {
        try {
            return getResource().getString(n);
        } catch (MissingResourceException e) {
            int i = n.lastIndexOf('.');
            if (i > 0) {
                final String cloneName = n.substring(0, i) + ".clone";
                String cloneBase = getResource().getString(cloneName);
                if (cloneBase != null) {
                    final String cloneTarget = cloneBase + "." + n.substring(i + 1);
                    String v = getResource().getString(cloneTarget);
                    if (v != null) {
                        return v;
                    }
                }
            }
            logger.log(Level.SEVERE, "Missing ressource {0}", n);
            return n;
        }
    }

    /**
     * Donne une ressource
     *
     * @param n le nom sans le prefixe qui sera ajoute
     * @return le string
     */
    public String getString2(String n) {
        try {
            return getResource().getString(n);
        } catch (MissingResourceException e) {
            logger.log(Level.SEVERE, "Missing ressource {0}", n);
            return null;
        }
    }

    /**
     * donne les clés des ressources
     *
     * @return les clés
     */
    public Enumeration<String> getKeys() {
        return getResource().getKeys();
    }

    /**
     * Get the value of taskRateLimite
     *
     * @return the value of taskRateLimite
     */
    public int getTaskRateLimite() {
        return taskRateLimite;
    }

    /**
     * Set the value of taskRateLimite
     *
     * @param taskRateLimite new value of taskRateLimite
     */
    public void setTaskRateLimite(int taskRateLimite) {
        this.taskRateLimite = taskRateLimite;
    }

    /**
     * Défini le conteneur principal.
     *
     * @param mp le conteneur principal
     */
    public void setMainPane(MainPane mp) {
        window.setContent(mp);
    }

    /**
     * Donne le texte du copyright
     *
     * @return le texte du copyright
     */
    public String getCopyright() {
        return "© 2019 SST Informatique";
    }

    /**
     * Donne le visioneur d'image pour un identifiant
     *
     * @param id l'identifiant
     * @return le visioneur d'image
     */
    public ImageViewer getImageViewer(String id) {
        return imageViewers.get(id);
    }

    /**
     * Donne la version de l'interface UI
     *
     * @return la version de l'interface
     */
    public IfaceVersion getInterfaceVersion() {
        return ifaceVersion;
    }

    /**
     * change la version de l'interface
     *
     * @param ifaceVersion la version de l'interface
     */
    public void setInterfaceVersion(IfaceVersion ifaceVersion) {
        this.ifaceVersion = ifaceVersion;
        setStyleSheet(getStyles().getDefaultStyleSheet());
    }

    /**
     * Donne la version de l'interface UI minimum
     *
     * @return la version minimum supportée
     */
    public IfaceVersion getMinVersion() {
        return minVersion;
    }

    /**
     * rassembler les actions pour une liste dans un menu drop down par ligne
     *
     * @return true si il faut utiliser un drop down
     */
    public boolean useDropDownForList() {
        return true;
    }

    //</editor-fold>
    //<editor-fold desc="Classes/Enums" defaultstate="collapsed">
    /**
     * ResourceBundle permissif
     */
    protected static class MyResourceBundle extends ResourceBundle {

        private Locale loc;

        /**
         * Constructeur
         */
        public MyResourceBundle(Locale loc) {
            this("com.cachat.prj.echo3.editor", loc);
            logger.log(Level.SEVERE, "LOCALE : App.MyResourceBundle({0})", loc);
            this.loc = loc;
        }

        /**
         * Constructeur
         */
        public MyResourceBundle(String path, Locale loc) {
            logger.log(Level.SEVERE, "LOCALE : App.MyResourceBundle({0})", loc);
            myRes = ResourceBundle.getBundle(path, loc);
            this.loc = loc;
        }
        /**
         * les resources sous jacentes
         */
        private ResourceBundle myRes;

        /**
         * gere l'obtention d'une cle
         */
        @Override
        public String handleGetObject(String s) {
            try {
                return myRes.getString(s);
            } catch (MissingResourceException e) {
                return s;
            }
        }

        /**
         * donne la locale de cette ressource
         */
        @Override
        public Locale getLocale() {
            return myRes.getLocale();//loc;//don't use underlying locale (error on default)
        }

        /**
         * donne toutes les clés
         *
         * @return
         */
        @Override
        public Enumeration<String> getKeys() {
            return myRes.getKeys();
        }
    };

    /**
     * Document en cache pour envoyer au navigateur
     */
    public class Doc {

        /**
         * l'identifiant unique du document
         */
        private String uid = UUID.randomUUID().toString();

        /**
         * Constructeur
         *
         * @param contentType type de contenu
         * @param name nom
         * @param data donn�es
         * @param user l'utilisateur
         */
        public Doc(String contentType, String name, byte[] data, User user) {
            this.contentType = contentType;
            this.name = name;
            this.data = data;
            this.user = user;
        }

        /**
         * Constructeur
         *
         * @param contentType type de contenu
         * @param name nom
         * @param file le fichier de données (il doit exister, et ne sera pas
         * supprimé automatiquement après le téléchargement
         * @param user l'utilisateur
         */
        public Doc(String contentType, String name, File file, User user) {
            this.contentType = contentType;
            this.name = name;
            this.file = file;
            this.user = user;
        }
        protected String contentType;

        /**
         * Get the value of contentType
         *
         * @return the value of contentType
         */
        public String getContentType() {
            return contentType;
        }
        protected String name;

        /**
         * Get the value of name
         *
         * @return the value of name
         */
        public String getName() {
            return name;
        }
        protected byte[] data;

        /**
         * Get the value of data
         *
         * @return the value of data
         */
        public byte[] getData() {
            return data;
        }
        protected User user;

        /**
         * Get the value of user
         *
         * @return the value of user
         */
        public User getUser() {
            return user;
        }
        protected File file;

        public File getFile() {
            return file;
        }

        public String getUid() {
            return uid;
        }

    }

    /**
     * Version d'interface graphique
     */
    public static enum IfaceVersion {
        TAB_V4("V4"),
        WEB_V5("V5"),
        WEB_V6("V6");

        private final String label;

        private IfaceVersion(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
    //</editor-fold>
}
