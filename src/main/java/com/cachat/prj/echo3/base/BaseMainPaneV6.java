package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.able.Scrollable;
import com.cachat.prj.echo3.ng.menu.MenuElement;
import com.cachat.prj.echo3.ng.menu.MenuItem;
import com.cachat.prj.echo3.ng.menu.MenuPane;
import com.cachat.prj.echo3.ng.menu.SubMenu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Command;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.WindowPaneEvent;
import nextapp.echo.webcontainer.command.BrowserOpenWindowCommand;

/**
 * l'écran principal
 */
public abstract class BaseMainPaneV6 extends MainPane {

    /**
     * la liste de boutons pour le menu vérticale
     */
    protected final List<Button> menu1Items = new ArrayList<>();

    /**
     * la liste de boutons pour le menu horizontale
     */
    protected final List<Button> menu2Items = new ArrayList<>();
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
    protected Button backBtn;

    /**
     * action listener sur le bouton close
     */
    protected ActionListener closeBtnListener;

    /**
     * bouton pour ouvrir la fenêtre d'aide
     */
    protected Button helpBtn;

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

            // Title label
            title = new LabelEx();
            title.setStyleName("Title");
            title.setLineWrap(false);

            // Title container
            titleCE = new ContainerEx();
            titleCE.setStyleName("TitleCE");
            titleCE.add(title);
            add(titleCE);

            // header
            headerCE = new ContainerEx();
            headerCE.setStyleName("HeaderCE");
            headerCE.add(new LabelEx(app.getBaseString("app.title")));
            add(headerCE);

            // Container for connection buttons
            connectCE = new ContainerEx();
            connectCE.setStyleName("ConnectCE");
            add(connectCE);

            contextButtonsCE = new ContainerEx();
            contextButtonsCE.setStyleName("ContextButtonsCE");
            add(contextButtonsCE);
            Row r = new Row();
            contextButtonsCE.add(r);
            String url = app.getHelpUrl();
            if (url != null) {
                helpBtn = new Button(app.getStyles().getIcon("help"));
                helpBtn.setStyleName("ContextButton");
                helpBtn.addActionListener(e -> {
                    Command command = new BrowserOpenWindowCommand(url, "extra", new Extent(800), new Extent(600), BrowserOpenWindowCommand.FLAG_REPLACE | BrowserOpenWindowCommand.FLAG_RESIZABLE);
                    app.enqueueCommand(command);
                });
                r.add(helpBtn);
            }
            backBtn = new Button(app.getStyles().getIcon("arrowLeft"));
            backBtn.setStyleName("TopRightButton");
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
            connectCE.setVisible(true);
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
            updateLayout(null);
        } else {
            if (w instanceof BasicWindow basicWindow) {
                if (!windows.contains(w)) {
                    windows.add(0, w);
                }
                BasicWindow bw = basicWindow;
                title.setText(bw.getTitre2());
                activeWindow = bw;
                activeCE = bw.getContentPane();
                mainCE.add(activeCE);

                bw.setParent(this);
                bw.init();
                updateLayout(bw);
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

    /**
     * Méttre à jour le style des boutons sur le menu vérticale
     *
     * @param button le bouton actuellement séléctionné
     */
    private void setMenu1Selected(Button button) {
        menu1Items.forEach(m -> {
            m.setStyleName("Menu1Unselected");
        });
        if (button == null) {
            return;
        }
        button.setStyleName("Menu1Selected");
    }

    /**
     * Méttre à jour le style des boutons sur le menu horizontal
     *
     * @param button le bouton actuellement séléctionné
     */
    private void setMenu2Selected(Button button) {
        menu2Items.forEach(m -> {
            m.setStyleName("Menu2Unselected");
        });
        if (button == null) {
            return;
        }
        button.setStyleName("Menu2Selected");
    }

    /**
     * Méttre à jour le contenu du mainCE à partir d'un MenuItem
     *
     * @param mi le MenuItem
     */
    private void updateMainCE(MenuItem mi) {
        if (mi.getNewPane() != null) {
            if (!app.addOrActivatePane(mi.getNewPane())) {
                new MessagePane(app, app.getString("license.invalide.titre"), "outils", app.getString("license.invalide.message"), MessagePane.Icon.ERROR, new Extent(500), new Extent(200));
            }
        } else if (mi.getNewWindow() != null) {
            mainCE.removeAll();
            title.setText("");
            Command command = new BrowserOpenWindowCommand(mi.getNewWindow(), "extra", new Extent(800), new Extent(600), BrowserOpenWindowCommand.FLAG_REPLACE | BrowserOpenWindowCommand.FLAG_RESIZABLE);
            app.enqueueCommand(command);
        } else {
            if ("logout".equals(mi.getId())) {
                app.logout();
            } else {
                logger.log(Level.SEVERE, "No action for menu item {0}", mi.getId());
            }
        }
    }

    /**
     * construit le menu
     *
     * @param root la racine de la reference
     */
    protected void buildMainMenu(SubMenu root) {
        if (app.getUser() == null) {
            return;
        }
        Column col = new Column();
        for (MenuElement mel : root.getChilds()) {
            final ButtonEx button = new ButtonEx(app.getString(mel.getLabel()), app.getStyles().getIcon(mel.getIcon()));
            button.setStyleName("Menu1Unselected");
            col.add(button);
            menu1Items.add(button);
            button.addActionListener(e -> {
                activateMenu(mel, e, button);
            });
        }

        menu1CE.setScrollBarPolicy(Scrollable.CLIPHIDE);
        menu1CE.add(new ContainerEx(0, 0, 0, null, null, null, col));
    }

    /**
     * active un menu
     *
     * @param button le bouton qui a été utilisé
     * @param e l'évènement
     * @param mel le menu
     */
    public void activateMenu(MenuElement mel, ActionEvent e, final ButtonEx button) {
        if (mel instanceof MenuItem menuItem) {
            logger.log(Level.FINE, "menu : {0}", e.getActionCommand());
            setSubMenuVisible(false);
            setMenu1Selected(button);
            updateMainCE(menuItem);
        } else if (mel instanceof SubMenu sm) {
            setSubMenuVisible(false);
            setMenu1Selected(button);
            logger.log(Level.FINE, "menu : {0}", e.getActionCommand());
            if (sm.getChilds() != null && !sm.getChilds().isEmpty()) {
                menu2Items.clear();
                menu2CE.removeAll();
                BasicWindow mp = buildSubMenu(sm);

                setSubMenuVisible(true);
                if (mp != null) {
                    app.addWindow(mp, null);
                }
            } else {
                logger.log(Level.SEVERE, "No action for menu item {0}", app.getString(sm.getLabel()));
            }

        }
    }

    /**
     * Construit un sous menu
     *
     * @param root la racine du sous menu
     */
    protected BasicWindow buildSubMenu(SubMenu root) {
        if (app.getUser() == null) {
            return null;
        }
        List<ActionListener> actionsPossibles = new ArrayList<>();
        MenuPane mp = new MenuPane(app);
        Row row = new Row();
        for (MenuElement mel : root.getChilds()) {
            ButtonEx button = new ButtonEx(app.getString(mel.getLabel()), app.getStyles().getIcon(mel.getIcon()));
            button.setStyleName("Menu2Unselected");
            row.add(button);
            menu2Items.add(button);
            if (mel instanceof MenuItem menuItem) {
                button.setVisible(menuItem.getMenu());
                final ActionListener al = e -> {
                    logger.log(Level.FINE, "menu : {0}", e.getActionCommand());
                    setMenu2Selected(button);
                    updateMainCE(menuItem);
                };
                actionsPossibles.add(al);
                button.addActionListener(al);
                mp.addMenuItem2(button);
            } else if (mel instanceof SubMenu) {
                final ActionListener al = e -> {
                    setMenu2Selected(button);
                    mainCE.removeAll();
                    title.setText("");
                    app.toast("Sous menus à deux niveaux non implementés");
                };
                button.addActionListener(al);
                mp.addMenuItem2(button);
            }
        }
        menu2CE.add(row);
        if (actionsPossibles.size() == 1) {
            actionsPossibles.get(0).actionPerformed(new ActionEvent(this, null));
            return null;
        }
        return mp;
    }

    public enum Layout {
        NORMAL, // mise en page normale avec le titre dans le contenu
        FULLSCREEN // mise en page plein écran avec le titre et les boutons supplementaires dans le header
    }

    /**
     * donne la liste de toutes les fenetres ouvertes
     *
     * @return la liste
     */
    @Override
    public List<WindowPane> getWindows() {
        return Collections.unmodifiableList(windows);
    }
}
