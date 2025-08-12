package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.interfaces.Styles;
import com.cachat.prj.echo3.interfaces.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.TaskQueueHandle;
import nextapp.echo.app.Window;
import nextapp.echo.app.WindowPane;

/**
 * une application avec tous les services de base
 *
 * @author scachat
 */
public abstract class ChildApp extends BaseApp {

    /**
     * notre application maitre
     */
    protected final BaseApp masterApp;
    /**
     * le nom de l'entityManager de référence
     */
    private final String entityManagerName;
    /**
     * la locale par défaut
     */
    private final Locale defaultLocale;

    /**
     *
     * @param masterApp l'application maitre
     * @param entityManagerName le nom de l'entityManager de référence
     * @param defaultLocale la locale par défaut
     */
    public ChildApp(BaseApp masterApp, String entityManagerName, Locale defaultLocale) {
        this.masterApp = masterApp;
        masterApp.addChildApp(this);
        this.entityManagerName = entityManagerName;
        this.defaultLocale = defaultLocale;
        sessionCleaner.schedule(sessionCleanTask, 10000, 10000);
    }

    /**
     * le chemin relatif de cette servlet
     */
    private String pathInfo;

    /**
     * fixe le chemin de cette servlet (et donc son contenu)
     *
     * @param pathInfo le chemin relatif de cette servlet
     */
    /*package protected*/ void setServletPath(String pathInfo) {
        if (pathInfo != null && (this.pathInfo == null || !this.pathInfo.equals(pathInfo))) {
            this.pathInfo = pathInfo;
            initContent(pathInfo);
        }

    }

    /**
     * génère un id de composant
     *
     * @return
     */
    @Override
    public String generateId() {
        return this.pathInfo + "_" + super.generateId();
    }
       /**
     * change la locale
     *
     * @param loc la nouvelle
     */
    @Override
    public final void setMyLocale(Locale loc) {
        super.setLocale(loc);
    }
    /**
     * donne l'application de base
     *
     * @return
     */
    public BaseApp getMainApp() {
        return masterApp;
    }

    @Override
    public void setSuperAdmin(boolean superAdmin) {
        getMainApp().setSuperAdmin(superAdmin);
    }

    @Override
    public void doLogin() {
        getMainApp().doLogin();
    }

    @Override
    public User validLogin(String name, String pass) {
        return getMainApp().validLogin(name, pass);
    }
    @Override
    public User getUser() {
        return masterApp.getUser();
    }

    @Override
    public String getString(String key) {
        return masterApp.getString(key);
    }

    @Override
    public ResourceBundle getResource() {
        return masterApp.getResource();
    }

    @Override
    public BaseApp.Doc findDoc(String name) {
        return masterApp.findDoc(name);
    }

    @Override
    public void sendDoc(String format, String name, byte[] byteArray) {
        masterApp.sendDoc(format, name, byteArray);
    }

    @Override
    public boolean isSuperAdmin() {
        return masterApp.isSuperAdmin();
    }

    @Override
    public Window init() {
        window = new Window();
        window.setTitle(getResource().getString("app.title"));
        setStyleSheet(getStyles().getDefaultStyleSheet());
        setMainPane(getMainPane());
        return window;
    }

    /**
     * initialise le contenu en fonction du chemin
     *
     * @param pathInfo le chemin
     */
    public abstract void initContent(String pathInfo);
    /**
     * la queue des evenements push
     */
    private TaskQueueHandle taskQueue = null;

    /**
     * timer pour la purge des updates
     */
    private final Timer sessionCleaner = new Timer("echo session cleaner", true);
    /**
     * tache pour la purge des updates
     */
    private final TimerTask sessionCleanTask = new TimerTask() {

        @Override
        public void run() {
            cleanTaskQueue();
        }
    };
    /**
     * timestamp de dernier accès aux taches
     */
    private long lastTaskAccess = System.currentTimeMillis();

    /**
     * Processes all queued tasks. This method may only be invoked from within a
     * UI thread by the <code>UpdateManager</code>. Tasks are removed from
     * queues once they have been processed.
     */
    @Override
    public void processQueuedTasks() {
        lastTaskAccess = System.currentTimeMillis();
        super.processQueuedTasks();
    }

    /**
     * destroy task queue if it has not been accessed recently
     */
    public void cleanTaskQueue() {
        if (System.currentTimeMillis() - lastTaskAccess > 70000) {
            if (taskQueue != null) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Destroying taskqueue for {0} / {1}", new Object[]{hashCode(), getUser().getLibelle()});
                removeTaskQueue(taskQueue);
                taskQueue = null;
            }
        }
    }

    /**
     * enregistre un evenement push
     */
    @Override
    public synchronized void enqueueTask(Runnable run) {
        if (taskQueue == null) {
            taskQueue = createTaskQueue();
        }
        if (run != null) {
            enqueueTask(taskQueue, run);
        }
    }

    /**
     * Invoked when the application is disposed and will not be used again.
     * Implementations must invoke <code>super.dispose()</code>.
     */
    @Override
    public void dispose() {
        sessionCleanTask.cancel();
        if (taskQueue != null) {
            removeTaskQueue(taskQueue);
        }
        super.dispose();
    }

    /**
     * logout the user
     */
    @Override
    public abstract void logout();

    /**
     * ajoute une fenetre, quand on n'est pas dans le thread echo2
     *
     * @param parent la fenêtre parente
     * @param pane la nouvelle fenêtre
     */
    @Override
    public void delayedAddWindow(WindowPane pane, WindowPane parent) {
        enqueueTask(new DelayedAddWindow(pane, parent));
    }

    /**
     * action d'ajout de fenêtre après délai
     */
    private class DelayedAddWindow implements Runnable {

        /**
         * la fenêtre parente
         */
        private final WindowPane parent;
        /**
         * la nouvelle fenêtre
         */
        private final WindowPane pane;

        /**
         * constructeur
         *
         * @param parent la fenêtre parente
         * @param pane la nouvelle fenêtre
         */
        public DelayedAddWindow(WindowPane pane, WindowPane parent) {
            this.parent = parent;
            this.pane = pane;
        }

        @Override
        public void run() {
            addWindow(pane, parent);
        }
    }


    @Override
    public String getCopyright() {
        return "";
    }

    @Override
    public String getUrlLogoProprietaire() {
        return "logo.png";
    }

    @Override
    public String getLoginName() {
        return getUser().getLibelle();
    }

    @Override
    public String getEntityManagerName() {
        return entityManagerName;
    }

    @Override
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    @Override
    public String getBaseString(String n) {
        return getString(n);
    }
    protected int taskRateLimite = 3;

    /**
     * Get the value of taskRateLimite
     *
     * @return the value of taskRateLimite
     */
    @Override
    public int getTaskRateLimite() {
        return taskRateLimite;
    }

    /**
     * Set the value of taskRateLimite
     *
     * @param taskRateLimite new value of taskRateLimite
     */
    @Override
    public void setTaskRateLimite(int taskRateLimite) {
        this.taskRateLimite = taskRateLimite;
    }
    /**
     * timer pour l'envoi des taches avec une limite
     */
    private Timer taskLimitTimer;
    ;
   /**
    * liste des taches
    */
   private final List<Runnable> taskWithLimits = new ArrayList<>();

    /**
     * enregistre un evenement push, avec une limite pour éviter la saturation
     * du client. Les taches seront exécutée seulement une fois toutes les
     * taskRateLimit secondes
     */
    @Override
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
}
