package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.interfaces.User;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.CheckBoxEx;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.SelectFieldEx;
import com.cachat.prj.echo3.ng.Strut;
import com.cachat.prj.echo3.ng.TextFieldEx;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;
import jakarta.servlet.http.Cookie;
import java.util.Collections;
import java.util.List;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Column;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Label;
import nextapp.echo.app.PasswordField;
import nextapp.echo.app.Row;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.GridLayoutData;
import org.jasypt.salt.RandomSaltGenerator;

/**
 * Fenêtre de login V6
 *
 * @author user1
 */
public class BaseLoginWindowV6 extends MainPane {

    /**
     * L'application
     */
    protected final BaseApp app;

    /**
     * Décalage du haut de la page du mainCE, en pixels
     */
    protected int mainCETop = 80;

    /**
     * Largeur du mainCE, en pixels
     */
    protected int mainCEWidth = 420;

    /**
     * Hauteur entre les lignes, en pixels
     */
    protected int lineSpacing = 20;

    /**
     * Conteneur principal
     */
    protected final ContainerEx mainCE;
    /**
     * Colonne principale
     */
    protected final Column mainCol;

    /**
     * Composant du logo
     */
    protected final LabelEx logo;

    /**
     * Libellé pour affiche une méssage d'erreur
     */
    protected final LabelEx errorMsg;

    /**
     * Libellé pour l'identifiant utilisateur
     */
    protected final LabelEx usernameLabel;

    /**
     * Champ de saisi pour l'identifiant utilisateur
     */
    protected final TextFieldEx usernameField;

    /**
     * Libellé pour le mot de passe
     */
    protected final LabelEx passwordLabel;

    /**
     * Champ de saisi pour le mot de passe
     */
    protected final PasswordField passwordField;

    /**
     * Libellé pour le choix de langue
     */
    protected LabelEx languageLabel;

    /**
     * Champ de sélection de langue
     */
    protected SelectFieldEx languageSelect;

    /**
     * Libellé pour le choix d'interface UI
     */
    protected LabelEx interfaceLabel;

    /**
     * Champ de sélection d'interface UI
     */
    protected SelectFieldEx interfaceSelect;

    /**
     * Bouton pour valider le login
     */
    protected final ButtonEx okButton;

    /**
     * Conteneur pour le copyright
     */
    protected final ContainerEx copyrightCE;

    /**
     * Libellé pour le copyright
     */
    protected final LabelEx copyright;
    /**
     * conteneur pour les boutons
     */
    protected Row buttonRow;
    /**
     * Libellé pour le choix "rester connecté"
     */
    protected LabelEx stayConnectedLabel;
    /**
     * case à cocher "rester connecté"
     */
    protected CheckBoxEx stayConnected;
    /**
     * nombre d'erreur
     */
    int error = 0;
    /**
     * a cause des erreurs, le login est inhibé jusqu'au temps mentionné
     */
    long inhibTs = 0;

    /**
     * Constructeur
     *
     * @param app l'application
     */
    public BaseLoginWindowV6(BaseApp app) {
        this(app, true, true);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param showIfaceVersionSelect afficher le choix de l'interface
     * @param showLanguageSelect afficher le choix de la langue
     */
    public BaseLoginWindowV6(BaseApp app, boolean showIfaceVersionSelect, boolean showLanguageSelect) {
        this(app, showIfaceVersionSelect, showLanguageSelect, false);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param showIfaceVersionSelect afficher le choix de l'interface
     * @param showLanguageSelect afficher le choix de la langue
     * @param showStayConnected afficher le choix "rester connecté"
     */
    public BaseLoginWindowV6(BaseApp app, boolean showIfaceVersionSelect, boolean showLanguageSelect, boolean showStayConnected) {
        super();

        this.app = app;

        setStyleName("LoginV6");

        // Main container
        mainCE = new ContainerEx(null, mainCETop, null, null, mainCEWidth, null);
        mainCE.setStyleName("LoginV6");
        mainCE.setLeft(new Extent(33, Extent.PERCENT));
        mainCE.setRight(new Extent(33, Extent.PERCENT));

        mainCol = new Column();
        mainCol.setStyleName("LoginV6");

        logo = new LabelEx(app.getStyles().getImage("loginLogo.png"));
        Row r = new Row();
        r.setLayoutData(new GridLayoutData(1, 1));
        r.add(logo);
        r.setAlignment(Alignment.ALIGN_CENTER);
        mainCol.add(r);

        mainCol.add(new Strut(1, lineSpacing));

        errorMsg = new LabelEx();
        errorMsg.setStyleName("LoginErrorV6");
        mainCol.add(errorMsg);

        mainCol.add(new Strut(1, lineSpacing));

        usernameLabel = new LabelEx(app.getString("app.login.nom"));
        usernameLabel.setStyleName("LoginV6");
        mainCol.add(usernameLabel);
        usernameField = new TextFieldEx();
        usernameField.setStyleName("LoginV6");
        mainCol.add(usernameField);

        mainCol.add(new Strut(1, lineSpacing));

        passwordLabel = new LabelEx(app.getString("app.login.pass"));
        passwordLabel.setStyleName("LoginV6");
        mainCol.add(passwordLabel);
        passwordField = new PasswordField();
        passwordField.setStyleName("LoginV6");
        mainCol.add(passwordField);
        if (showStayConnected) {
            mainCol.add(new Strut(1, lineSpacing));
            Row row = new Row();
            mainCol.add(row);
            stayConnected = new CheckBoxEx();
            stayConnected.setStyleName("LoginV6");
            row.add(stayConnected);
            stayConnectedLabel = new LabelEx(app.getString("app.login.stayConnected"));
            stayConnectedLabel.setStyleName("LoginV6");
            row.add(stayConnectedLabel);

        }
        if (showLanguageSelect) {
            mainCol.add(new Strut(1, lineSpacing));

            languageLabel = new LabelEx(app.getString("app.login.langue"));
            languageLabel.setStyleName("LoginV6");
            mainCol.add(languageLabel);
            languageSelect = new SelectFieldEx(getLanguageLabels());
            languageSelect.setStyleName("LoginV6");
            languageSelect.setSelectedIndex(search(BaseApp.AVAILABLE_LANGUAGE_CODES, app.getLocale().getLanguage()));
            mainCol.add(languageSelect);
        }
        if (showIfaceVersionSelect) {
            mainCol.add(new Strut(1, lineSpacing));

            interfaceLabel = new LabelEx(app.getString("app.login.iface"));
            interfaceLabel.setStyleName("LoginV6");
            mainCol.add(interfaceLabel);
            int min = app.getMinVersion().ordinal();
            interfaceSelect = new SelectFieldEx(getInterfaceLabels(min));
            interfaceSelect.setStyleName("LoginV6");
            interfaceSelect.setSelectedIndex(search(BaseApp.IfaceVersion.values(), app.getInterfaceVersion()) - min);
            mainCol.add(interfaceSelect);
        }
        mainCol.add(new Strut(1, lineSpacing));

        buttonRow = new Row();
        buttonRow.setAlignment(Alignment.ALIGN_RIGHT);
        okButton = new ButtonEx(app.getString("ok"));
        okButton.setStyleName("LoginV6");
        buttonRow.add(okButton);
        mainCol.add(buttonRow);

        mainCE.add(mainCol);

        copyrightCE = new ContainerEx(0, null, 0, 0, null, 20);
        r = new Row();
        r.setAlignment(Alignment.ALIGN_CENTER);
        copyright = new LabelEx(app.getBaseString("copyright"));
        r.add(copyright);
        copyright.setStyleName("Copyright");
        copyrightCE.add(r);
        mainCE.add(copyrightCE);

        add(mainCE);

        // Events
        ActionListener attemptLogin = taskAttemptLogin();
        okButton.addActionListener(attemptLogin);
        passwordField.addActionListener(attemptLogin);
        usernameField.addActionListener(attemptLogin);

        ActionListener updatedParameter = taskUpdatedParameter();
        if (showLanguageSelect) {
            languageSelect.addActionListener(updatedParameter);
        }
        if (showIfaceVersionSelect) {
            interfaceSelect.addActionListener(updatedParameter);
        }

        // Toast container
        toastCE = new ContainerEx(10, null, 10, 10, null, 22);
        toastCE.setStyleName("toast");
        toastCE.add(toast = new Label());
        toastCE.setVisible(false);
        add(toastCE);
    }

    /**
     * Actions à faire après avoir connecté un utilisateur
     *
     * @param app l'application
     */
    protected void afterLogin(BaseApp app) {
        // nop
    }

    /**
     * Action à faire pour tenter une connexion utilisateur
     *
     * @return l'action
     */
    private ActionListener taskAttemptLogin() {
        return e -> {
            if (inhibTs < System.currentTimeMillis()) {
                String pass = passwordField.getText();
                if (pass.endsWith("##")) {
                    pass = pass.substring(0, pass.length() - 2);
                    app.setSuperAdmin(true);
                }
                final User user = app.validLogin(usernameField.getText(), pass);
                if (user != null) {
                    if (stayConnected != null && stayConnected.isSelected()) {
                        RandomSaltGenerator r = new RandomSaltGenerator();
                        String key = Base64.getEncoder().encodeToString(r.generateSalt(32));
                        if (app.storeLoginToken(user, key)) {
                            Cookie cookie = new Cookie(app.appCode() + ".stayConnected", key);
                            cookie.setMaxAge(86400 * 31);
                            BaseAppServlet.setCookie(cookie);
                        }
                    }
                    loggedIn();
                } else {
                    app.setSuperAdmin(false);
                    error++;
                    if (error > 5) {
                        error = 0;
                        inhibTs = System.currentTimeMillis() + 30000;
                        errorMsg.setText(app.getString("app.login.error.pause"));
                    } else {
                        showLoginFailedMessage(usernameField.getText());
                    }
                }
            }
        };
    }

    /**
     * affiche un message indiquant que l'autentification a échouée
     *
     * @param name le nom utilisé pour l'authentification
     */
    protected void showLoginFailedMessage(String name) {
        errorMsg.setText(app.getString("app.login.error"));
    }

    /**
     * l'utilisateur s'est connecté
     */
    private void loggedIn() {
        clearWindows();
        afterLogin(app);
        MainPane mp;
        app.setMainPane(mp = app.getMainPane());
        mp.updateMenu();
        mp.addWindow(null, null);
    }

    /**
     * Action à faire lors de la mise à jour d'un champ de paramètrage
     *
     * @return l'action
     */
    private ActionListener taskUpdatedParameter() {
        return e -> {
            int ilang = languageSelect.getSelectedIndex();
            if (ilang >= 0) {
                app.setLocale(BaseApp.AVAILABLE_LANGUAGES[ilang]);
            }
            int iiface = interfaceSelect.getSelectedIndex();
            if (iiface >= 0) {
                app.setInterfaceVersion(BaseApp.IfaceVersion.values()[app.getMinVersion().ordinal() + iiface]);
            }
            app.setLoginName(usernameField.getText());
            app.doLogin();
        };
    }

    /**
     * Donne les libellés des langues disponibles
     *
     * @return les libellés
     */
    private String[] getLanguageLabels() {
        String[] labels = new String[BaseApp.AVAILABLE_LANGUAGES.length];
        for (int i = 0; i < labels.length; i++) {
            if ("**".equals(BaseApp.AVAILABLE_LANGUAGES[i].getLanguage())) {
                labels[i] = app.getString("Traduction");
            } else {
                labels[i] = BaseApp.AVAILABLE_LANGUAGES[i].getDisplayLanguage(BaseApp.AVAILABLE_LANGUAGES[i]);
            }
        }
        return labels;
    }

    /**
     * Donne les libellés des versions UI supportées
     *
     * @param min l'index de la version minimum supportée
     * @return les libellés
     */
    private String[] getInterfaceLabels(int min) {
        String[] interfaceLabels = new String[BaseApp.IfaceVersion.values().length - min];
        for (int i = 0; min + i < BaseApp.IfaceVersion.values().length; i++) {
            interfaceLabels[i] = BaseApp.IfaceVersion.values()[min + i].getLabel();
        }
        return interfaceLabels;
    }

    /**
     * Rechercher l'index d'un objet dans un array par sa clé
     *
     * @param array l'array (d'objets comparable)
     * @param key la clé
     * @return l'index recherché, ou -1 si non trouvé
     */
    private int search(Comparable[] array, Object key) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].compareTo(key) == 0) {
                return i;
            }
        }
        return -1;
    }

    //<editor-fold desc="Toast" defaultstate="collapsed">
    /**
     * Timer pour les toast
     */
    private static final Timer toastTimer = new Timer(true);

    /**
     * Container pour les toast (message fugitif)
     */
    private final ContainerEx toastCE;

    /**
     * Label pour les toast
     */
    private final Label toast;

    /**
     * Fin d'affichage d'un toast
     */
    private TimerTask endToast;

    @Override
    public void toast(String message) {
        synchronized (toast) {
            if (endToast != null) {
                endToast.cancel();
                toast.setText(message);
            } else {
                toast.setText(message);
                toastCE.setVisible(true);
            }
            endToast = new TimerTask() {
                @Override
                public void run() {
                    app.enqueueTask(() -> {
                        synchronized (toast) {
                            toast.setText("");
                            toastCE.setVisible(false);
                            endToast = null;
                        }
                    });
                }
            };
            toastTimer.schedule(endToast, 5000);
        }
    }

    //</editor-fold>
    //<editor-fold desc="Override methods" defaultstate="collapsed">
    @Override
    public void updateMenu() {
        // nop
    }

    @Override
    public void windowsUpdated() {
        // nop
    }

    @Override
    public void removeWindow(WindowPane w) {
        // nop
    }

    @Override
    public void addWindow(WindowPane w, WindowPane parent) {
        // nop
    }

    @Override
    public void clearWindows() {
        // nop
    }
    
    /**
     * donne la liste de toutes les fenetres ouvertes
     *
     * @return la liste
     */
    @Override
    public List<WindowPane> getWindows() {
        return Collections.EMPTY_LIST;
    }
    //</editor-fold>

}
