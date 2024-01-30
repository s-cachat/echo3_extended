package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.components.ButtonEx2;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.Strut;
import com.cachat.prj.echo3.ng.able.Positionable;
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
     * le conteneur des boutons sur la bande horizontale
     */
    protected ContainerEx headerButtonCE;

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
     * liste de fenetres affich?es, dans l'ordre
     */
    protected List<WindowPane> windows = new ArrayList<>();

    /**
     * fenêtre modale
     */
    protected WindowPane modalWindow = null;

    /**
     * hauteur du header, en pixel
     */
    public static final int HEIGHT_HEADER = 48;

    /**
     * largeur de connectCE, en pixel
     */
    public static final int WIDTH_CONNECT_CE = 207;

    /**
     * hauteur du barre de titre, en pixel
     */
    public static final int HEIGHT_TITLE = 44;

    /**
     * hauteur du sous menu horizontale, en pixel
     */
    public static final int INNER_HEIGHT_MENU_2 = 48;

    /**
     * hauteur du sous menu horizontale, en pixel
     */
    public static final int OUTER_HEIGHT_MENU_2 = 48;

    /**
     * largeur extetieur du menu vérticale à gauche, en pixel
     */
    public static final int WIDTH_MENU_1 = 165;

    /**
     * hauteur du pied de page en pixel
     */
    public static final int HEIGHT_FOOTER = 30;

    /**
     * Décalage du titre du haut de la page avec le sous menu visible
     */
    protected Extent offsetTitleTopSubmenu = new Extent(HEIGHT_HEADER + OUTER_HEIGHT_MENU_2, Extent.PX);

    /**
     * Décalage du titre du haut de la page avec le sous menu invisible
     */
    protected Extent offsetTitleTopNoSubmenu = new Extent(HEIGHT_HEADER, Extent.PX);

    /**
     * Décalage du contenu du haut de la page avec le sous menu visible
     */
    protected Extent offsetMainTopSubmenu = new Extent(HEIGHT_HEADER + OUTER_HEIGHT_MENU_2 + HEIGHT_TITLE, Extent.PX);

    /**
     * Décalage du haut de la page avec le sous menu invisible
     */
    protected Extent offsetMainTopNoSubmenu = new Extent(HEIGHT_HEADER + HEIGHT_TITLE, Extent.PX);

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
            logoCE = new ContainerEx(0, 0, null, null, WIDTH_MENU_1, HEIGHT_HEADER);
            logoCE.add(new Label(app.getStyles().getImage("pageLogoV6.png")));
            logoCE.setStyleName("LogoCE");
            add(logoCE);

            // Container for connection buttons
            connectCE = new ContainerEx(0, 0, 0, null, WIDTH_CONNECT_CE, HEIGHT_HEADER);
            connectCE.setStyleName("ConnectZone");

            contextButtonsCE = new ContainerEx(WIDTH_CONNECT_CE, 0, 0, 0, null, null);
            contextButtonsCE.setStyleName("ContextButtons");
            Row r = new Row();
            String url = app.getHelpUrl();
            if (url != null) {
            helpBtn = new ButtonEx2("?");
            helpBtn.setStyleName("HeaderButton");
            helpBtn.setAlignment(Alignment.ALIGN_CENTER);
                helpBtn.addActionListener(e -> {                   
                    Command command = new BrowserOpenWindowCommand(url, "extra", new Extent(800), new Extent(600), BrowserOpenWindowCommand.FLAG_REPLACE | BrowserOpenWindowCommand.FLAG_RESIZABLE);
                    app.enqueueCommand(command);
            });
            r.add(helpBtn);
            r.add(new Strut(6, 6));
            }
            backBtn = new ButtonEx2("⇦");
            backBtn.setStyleName("HeaderButton");
            backBtn.setAlignment(Alignment.ALIGN_CENTER);
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
            // Title label
            title = new LabelEx();
            title.setStyleName("Title");
            title.setLineWrap(false);

            // Title container
            titleCE = new ContainerEx(WIDTH_MENU_1, HEIGHT_HEADER + OUTER_HEIGHT_MENU_2, 0, null, null, null);
            titleCE.setStyleName("TitleCE");
            titleCE.add(title);
            titleCE.setZIndex(9999);
            titleCE.setVisible(false);
            add(titleCE);
            // Container for buttons on the horizontal band
            headerButtonCE = new ContainerEx(null, 0, 0, 0, null, HEIGHT_HEADER);
            headerButtonCE.setStyleName("HeaderButtonCE");
            headerButtonCE.add(connectCE);
            headerButtonCE.add(contextButtonsCE);

            // header
            headerCE = new ContainerEx(null, 0, 0, null, null, HEIGHT_HEADER);
            headerCE.setStyleName("HeaderCE");
            headerCE.add(headerButtonCE);
            add(headerCE);

            // Vertical menu (main menu)
            menu1CE = new ContainerEx(0, HEIGHT_HEADER, null, 0, WIDTH_MENU_1, null);
            menu1CE.setStyleName("Menu");
            add(menu1CE);

            // Horizontal sub menu (below header)
            menu2CE = new ContainerEx(WIDTH_MENU_1, HEIGHT_HEADER, 0, null, null, INNER_HEIGHT_MENU_2);
            menu2CE.setStyleName("SubMenu");
            menu2CE.setVisible(false);
            add(menu2CE);

            // Footer
            footerCE = new ContainerEx(WIDTH_MENU_1, null, 0, 0, null, HEIGHT_FOOTER);
            footerCE.setPosition(Positionable.ABSOLUTE);
            r = new Row();
            r.setInsets(new Insets(0, 8, 0, 0));
            r.setAlignment(Alignment.ALIGN_CENTER);
            LabelEx legal = new LabelEx(app.getBaseString("copyright"));
            legal.setStyleName("Copyright");
            r.add(legal);
            footerCE.add(r);
            add(footerCE);

            // Main content container
            mainCE = new ContainerEx(WIDTH_MENU_1, HEIGHT_HEADER + HEIGHT_TITLE, 0, HEIGHT_FOOTER + 6, null, null);
            mainCE.setStyleName("MainCE");
            mainCE.setScrollBarPolicy(ContainerEx.CLIPHIDE);
            add(mainCE);

            homeWindow = getHomeWindow();
            addWindow(homeWindow, null);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "EE", t);
        }

        // Toast container
        toastCE = new ContainerEx(10, null, 10, 10, null, 22);
        toastCE.setStyleName("toast");
        toastCE.add(toast = new Label());
        toastCE.setVisible(false);
        add(toastCE);
    }

    /**
     * Met à jour la mise en page (V6)
     *
     * @param window la fenêtre
     */
    public void updateLayout(BasicWindow window) {
        if (window != null && window instanceof FullScreen) {
            titleCE.setTop(0);
            titleCE.setLeft(0);
            titleCE.setRight(null);
            titleCE.setStyleName("TitleCEHeader");
            mainCE.setTop(offsetTitleTopNoSubmenu);
            mainCE.setLeft(0);
            mainCE.setBottom(0);
            logoCE.setVisible(false);
            menu1CE.setVisible(false);
        } else {
            if (menu2CE.isVisible()) {
                titleCE.setTop(offsetTitleTopSubmenu);
                mainCE.setTop(offsetMainTopSubmenu);
            } else {
                titleCE.setTop(offsetTitleTopNoSubmenu);
                mainCE.setTop(offsetMainTopNoSubmenu);
            }
            mainCE.setLeft(WIDTH_MENU_1);
            mainCE.setBottom(HEIGHT_FOOTER);
            titleCE.setLeft(WIDTH_MENU_1);
            titleCE.setRight(0);
            titleCE.setStyleName("TitleCE");
            logoCE.setVisible(true);
            menu1CE.setVisible(true);
        }

        if (window == null) {
            backBtn.setVisible(false);
            headerButtonCE.setWidth(new Extent(WIDTH_CONNECT_CE + HEIGHT_HEADER, Extent.PX));
        } else {
            backBtn.setVisible(true);
            headerButtonCE.setWidth(new Extent(WIDTH_CONNECT_CE + (HEIGHT_HEADER * 2), Extent.PX));
        }
    }

    /**
     * Changer l'etat de visibilité du sous menu horizontale
     *
     * @param visible visible si true, invisible sinon
     */
    protected void setSubMenuVisible(boolean visible) {
        menu2CE.setVisible(visible);
        if (visible) {
            if (!backBtn.isVisible()) {
                titleCE.setTop(offsetTitleTopSubmenu);
                mainCE.setTop(offsetMainTopSubmenu);
            } else {
                mainCE.setTop(offsetTitleTopSubmenu);
            }
        } else {
            if (!backBtn.isVisible()) {
                titleCE.setTop(offsetTitleTopNoSubmenu);
                mainCE.setTop(offsetMainTopNoSubmenu);
            } else {
                mainCE.setTop(offsetTitleTopNoSubmenu);
            }
        }
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

    public abstract HomeWindow getHomeWindow();

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
            titleCE.setVisible(true);
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
                titleCE.setVisible(true);
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

    @Override
    public void dispose() {
    }

    public enum Layout {
        NORMAL, // mise en page normale avec le titre dans le contenu
        FULLSCREEN // mise en page plein écran avec le titre et les boutons supplementaires dans le header
    }

}
