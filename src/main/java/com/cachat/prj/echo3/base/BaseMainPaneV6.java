package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.components.ButtonEx2;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.Strut;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Command;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.WindowPaneEvent;
import nextapp.echo.webcontainer.command.BrowserOpenWindowCommand;

/**
 * l'écran principal
 */
public abstract class BaseMainPaneV6 extends MainPane {

    /**
     * le logger
     */
    protected static final Logger logger = Logger.getLogger(BaseMainPaneV6.class.getName());

    /**
     * l'instance de l'application
     */
    protected final BaseApp app;

    /**
     * La zone de titre
     */
    protected LabelEx title;

    /**
     * la fenetre principale
     */
    protected WindowPane activeWindow;

    /**
     * la fenetre home
     */
    protected BasicWindow homeWindow;

    /**
     * le ContainerEx de la fenetre principale
     */
    protected Component activeCE;

    /**
     * le conteneur du header
     */
    protected ContainerEx headerCE;

    /**
     * le conteneur du logo
     */
    protected ContainerEx logoCE;

    /**
     * le conteneur du menu vérticale à gauche
     */
    protected ContainerEx menu1CE;

    /**
     * le conteneur du menu horizontale sous le header
     */
    protected ContainerEx menu2CE;
    /**
     * drapeau : le menu 2 doit être visible
     */
    protected boolean menu2visible = true;

    /**
     * le conteneur pour le titre
     */
    protected ContainerEx titleCE;

    /**
     * le conteneur principal
     */
    protected ContainerEx mainCE;

    /**
     * le conteneur du pied de page
     */
    protected ContainerEx footerCE;

    /**
     * le conteneur des boutons qui gères les actions sur le connexion
     */
    protected ContainerEx connectCE;

    /**
     * le conteneur des boutons contextuels dans le header (eg. "X", "?")
     */
    protected ContainerEx contextButtonsCE;

    /**
     * bouton pour fermer une fenêtre plein écran
     */
    protected ButtonEx2 backBtn;

    /**
     * action listener sur le bouton close
     */
    protected ActionListener closeBtnListener;

    /**
     * bouton pour ouvrir la fenêtre d'aide
     */
    protected ButtonEx2 helpBtn;

    /**
     * timer pour les toast
     */
    private static final Timer toastTimer = new Timer(true);

    /**
     * container pour les toast (message fugitif)
     */
    private final ContainerEx toastCE;

    /**
     * label pour les toast
     */
    private final Label toast;

    /**
     * fin d'affichage d'un toast
     */
    private TimerTask endToast;
    /**
     * container pour les toast (message fugitif)
     */
    private final ContainerEx toastErrorCE;

    /**
     * label pour les toast
     */
    private final Label toastError;

    /**
     * fin d'affichage d'un toast
     */
    private TimerTask endToastError;
    /**
     * liste de fenetres affich?es, dans l'ordre
     */
    protected List<WindowPane> windows = new ArrayList<>();

    /**
     * fenêtre modale
     */
    protected WindowPane modalWindow = null;

    /**
     * Constructeur
     *
     * @param app l'application
     */
    public BaseMainPaneV6(BaseApp app) {
        this.app = app;
        logger.severe("Constructeur BaseMainPaneV6");
        try {
            // Logo container
            logoCE = new ContainerEx();
            logoCE.add(new Label(app.getStyles().getImage("pageLogoV6.png")));
            logoCE.setStyleName("LogoCE");
            add(logoCE);

            // Container for connection buttons
            connectCE = new ContainerEx();
            connectCE.setStyleName("ConnectCE");
            add(connectCE);

            contextButtonsCE = new ContainerEx();
            contextButtonsCE.setStyleName("ContextButtonsCE");
            Row r = new Row();
            String url = app.getHelpUrl();
            if (url != null) {
                helpBtn = new ButtonEx2("?");
                helpBtn.setStyleName("ContextButton");
                helpBtn.addActionListener(e -> {
                    Command command = new BrowserOpenWindowCommand(url, "extra", new Extent(800), new Extent(600), BrowserOpenWindowCommand.FLAG_REPLACE | BrowserOpenWindowCommand.FLAG_RESIZABLE);
                    app.enqueueCommand(command);
                });
                r.add(helpBtn);
                r.add(new Strut(6, 6));
            }
            backBtn = new ButtonEx2("⇦");
            backBtn.setStyleName("ContextButton");
            backBtn.setVisible(false);
            r.add(backBtn);
            backBtn.addActionListener(closeBtnListener = e -> {
                if (activeWindow != null) {
                    try {
                        activeWindow.userClose();
                    } catch (Throwable t) {
                        logger.log(Level.SEVERE, "Erreur", t);
                    }
                }
            });
            contextButtonsCE.add(r);
            add(contextButtonsCE);
            // Title label
            title = new LabelEx();
            title.setStyleName("Title");
            title.setLineWrap(false);

            // Title container
            titleCE = new ContainerEx();
            titleCE.setStyleName("TitleCE");
            titleCE.add(title);
            titleCE.setZIndex(9999);
            add(titleCE);

            // header
            headerCE = new ContainerEx();
            headerCE.setStyleName("HeaderCE");
            headerCE.add(new LabelEx(app.getBaseString("app.title")));
            add(headerCE);

            // Vertical menu (main menu)
            menu1CE = new ContainerEx();
            menu1CE.setStyleName("Menu1CE");
            add(menu1CE);

            // Horizontal sub menu (below header)
            menu2CE = new ContainerEx();
            menu2CE.setStyleName("Menu2CE");
            menu2CE.setVisible(false);
            add(menu2CE);

            // Footer
            footerCE = new ContainerEx();
            footerCE.setStyleName("FooterCE");
            r = new Row();
            r.setInsets(new Insets(0, 8, 0, 0));
            r.setAlignment(Alignment.ALIGN_CENTER);
            LabelEx legal = new LabelEx(app.getBaseString("copyright"));
            legal.setStyleName("Copyright");
            r.add(legal);
            footerCE.add(r);
            add(footerCE);

            // Main content container
            mainCE = new ContainerEx();
            mainCE.setStyleName("MainCE");
            mainCE.setScrollBarPolicy(ContainerEx.CLIPHIDE);
            add(mainCE);

            homeWindow = app.newHomeWindow();
            addWindow(homeWindow, null);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "EE", t);
        }

        // Toast container
        toastCE = new ContainerEx(10, null, 10, 10, null, 22);
        toastCE.setStyleName("Toast");
        toastCE.add(toast = new Label());
        toastCE.setVisible(false);
        add(toastCE);
        toastErrorCE = new ContainerEx(10, null, 10, 10, null, 22);
        toastErrorCE.setStyleName("ToastError");
        toastErrorCE.add(toastError = new Label());
        toastErrorCE.setVisible(false);
        add(toastErrorCE);
    }

    /**
     * Met à jour la mise en page (V6)
     *
     * @param window la fenêtre
     */
    public void updateLayout(WindowPane window) {
        if (window != null && window instanceof FullScreen) {
            headerCE.setVisible(false);
            titleCE.setStyleName("TitleCEFS");
            mainCE.setStyleName("MainCEFS");
            logoCE.setVisible(false);
            menu1CE.setVisible(false);
            menu2CE.setVisible(false);
            connectCE.setVisible(false);
            footerCE.setVisible(false);
        } else {
            headerCE.setVisible(true);
            logoCE.setVisible(true);
            menu1CE.setVisible(true);
            connectCE.setVisible(true);
            footerCE.setVisible(true);
            if (window == null) {
                backBtn.setVisible(false);
            } else {
                backBtn.setVisible(true);
            }
            if (menu2visible) {
                titleCE.setStyleName("TitleCE");
                mainCE.setStyleName("MainCE");
                menu2CE.setVisible(true);
            } else {
                titleCE.setStyleName("TitleCEN2");
                mainCE.setStyleName("MainCEN2");
                menu2CE.setVisible(false);
            }
        }

    }

    /**
     * Changer l'etat de visibilité du sous menu horizontale
     *
     * @param visible visible si true, invisible sinon
     */
    protected void setSubMenuVisible(boolean visible) {
        this.menu2visible = visible;
        updateLayout(activeWindow);
        menu2CE.setVisible(visible);
    }

    private String getDefaultTitle() {
        return app.getString("app.accueil");
    }

    @Override
    public void windowsUpdated() {
    }

    /**
     * supprime une fenetre
     *
     * @param w la fenêtre a faire disparaitre
     */
    @Override
    public void removeWindow(WindowPane w) {

    }

    public void modalClosing() {
        if (modalWindow != null) {
            remove(modalWindow);
            modalWindow = null;
        }
    }

    @Override
    public synchronized void addWindow(WindowPane w, WindowPane parent) {
        if (w != null && w.isModal()) {
            add(w);
            modalWindow = w;
            w.addWindowPaneListener((WindowPaneEvent e) -> modalClosing());
            return;
        }
        if (activeCE != null) {
            mainCE.remove(activeCE);
            activeCE = null;
        }
        if (activeWindow != null) {
            activeWindow.dispose();
            activeWindow = null;
        }

        if (w == null) {
            title.setText(getDefaultTitle());
            if (app.getUser() != null && homeWindow != null) {
                activeCE = homeWindow.getContentPane();
                mainCE.add(activeCE);
                homeWindow.setParent(this);
            }
            if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6) {
                updateLayout(null);
            }
        } else {
            if (w instanceof BasicWindow) {
                if (!windows.contains(w)) {
                    windows.add(0, w);
                }
                BasicWindow bw = (BasicWindow) w;
                title.setText(bw.getTitre2());
                activeWindow = bw;
                activeCE = bw.getContentPane();
                mainCE.add(activeCE);

                bw.setParent(this);
                bw.init();
                if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6) {
                    updateLayout(bw);
                }
            } else {
                throw new RuntimeException("Fenêtre incompatible : " + w);
            }
        }
    }

    @Override
    public void clearWindows() {
        mainCE.removeAll();
        windows.clear();
        addWindow(null, null);
    }
    private List<Portlet> portlets = new ArrayList<>();

    /**
     * affiche un message fugitif en bas de page. peut ne pas être implémenté
     * (dans ce cas ne fait rien)
     *
     * @param message le message
     */
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

    /**
     * affiche un message fugitif en bas de page. peut ne pas être implémenté
     * (dans ce cas ne fait rien)
     *
     * @param message le message
     */
    @Override
    public void toastError(String message) {
        synchronized (toastError) {
            if (endToastError != null) {
                endToastError.cancel();
                toastError.setText(message);
            } else {
                toastError.setText(message);
                toastErrorCE.setVisible(true);
            }
            endToastError = new TimerTask() {
                @Override
                public void run() {
                    app.enqueueTask(() -> {
                        synchronized (toastError) {
                            toastError.setText("");
                            toastErrorCE.setVisible(false);
                            endToastError = null;
                        }
                    });
                }
            };
            toastTimer.schedule(endToastError, 5000);
        }
    }

    @Override
    public void dispose() {
    }

    public enum Layout {
        NORMAL, // mise en page normale avec le titre dans le contenu
        FULLSCREEN // mise en page plein écran avec le titre et les boutons supplementaires dans le header
    }

}
