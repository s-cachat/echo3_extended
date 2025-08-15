package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.components.ButtonEx2;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.able.Scrollable;
import java.util.logging.Level;
import nextapp.echo.app.Button;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.FillImage;
import nextapp.echo.app.FillImageBorder;
import nextapp.echo.app.HttpImageReference;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.Insets;
import static nextapp.echo.app.Position.ABSOLUTE;
import nextapp.echo.app.Row;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.WindowPaneEvent;
import nextapp.echo.app.event.WindowPaneListener;

/**
 * l'ecran principal
 */
public abstract class BaseMainPane extends MainPane {

    /**
     * l'application
     */
    protected final BaseApp app;
    /**
     * la fenetre principale
     */
    protected WindowPane activeWindow;
    /**
     * la fenetre home
     */
    protected HomeWindow homeWindow;
    /**
     * le ContainerEx de la fenetre principale
     */
    protected Component activeCE;
    /**
     * le conteneur de l'image du logo partenaire
     */
    protected ContainerEx logoPartCE;
    /**
     * le conteneur principal
     */
    protected ContainerEx mainCE;
    /**
     * le fond du conteneur principal
     */
    protected ContainerEx homeCE;

    /**
     * fenêtre modale en cours
     */
    protected WindowPane modalWindow = null;
    /**
     * zone principale
     */
    protected ContainerEx zoneCE;
    /**
     * zone d'entête
     */
    protected ContainerEx headCE;
    /**
     * zone de mention légale
     */
    protected ContainerEx legalCE;
    /**
     * zone de titre
     */
    protected ContainerEx titleCE;
    /**
     * module (pour les BasicWindow)
     */
    private Module module;

    @Override
    public void windowsUpdated() {
    }

    /**
     * supprime une fenetre
     *
     * @param w
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

    /**
     * demande l'affichage d'une nouvelle fen�tre
     *
     * @param w la nouvelle fen�tre
     * @param parent la fen�tre parente, si il existe un lien de parent� type
     * liste-d�tail, ou null
     */
    @Override
    public synchronized void addWindow(WindowPane w, WindowPane parent) {
        if (w != null && w.isModal()) {
            w.setCloseIcon(app.getStyles().getIcon("close"));
            w.setTitleBackgroundImage(new FillImage(app.getStyles().getImage("titleWindowBck.png")));
            w.setTitleHeight(new Extent(35));
            w.setTitleInsets(new Insets(5, 6, 0, 0));
            w.setBorder(new FillImageBorder(Color.BLACK, new Insets(2), new Insets(2)));
            add(w);
            modalWindow = w;
            w.addWindowPaneListener(new WindowPaneListener() {
                @Override
                public void windowPaneClosing(WindowPaneEvent e) {
                    modalClosing();
                }
            });
            return;
        }
        //         if (activeWindow != null && activeWindow instanceof BasicEditor) {
        //            app.addWindow("alertWindow", new WindowImplicitCloseAlert(app, this, activeWindow, w));
        //            return;
        //         }
        if (activeWindow != null) {
            activeWindow.dispose();
            activeWindow = null;
        }
        if (activeCE != null) {
            mainCE.remove(activeCE);
            activeCE = null;
            activeWindow = null;
        }
        if (w == null) {
            if (app.getUser() != null && homeWindow != null) {
                activeCE = homeWindow.getContentPane();
                mainCE.add(activeCE);
                homeWindow.setParent(this);
            }
        } else {
            if (w instanceof BasicWindow) {
                BasicWindow bw = (BasicWindow) w;
                activeWindow = bw;
                Component windowContent = bw.getContentPane();
                module = new Module(app, bw.getTitre1(), true);
                module.setTop(new Extent(0));
                module.setRight(new Extent(0));
                module.setBottom(new Extent(0));
                module.setLeft(new Extent(0));
                module.setContent(windowContent);
                activeCE = module;
                module.addActionListener(new CloseActionListener(w));
                mainCE.add(activeCE);
                bw.setParent(this);
            } else {
                throw new RuntimeException("Fenêtre incompatible : " + w);
            }
        }
    }

    @Override
    public void clearWindows() {
        addWindow(null, null);
    }

    /**
     * Constructeur
     *
     * @param app l'instance de l'application
     */
    public BaseMainPane(BaseApp app) {
        this.app = app;
        BaseApp.logger.severe("Constructeur BaseMainPane");
        try {
            updateMenu();
            setStyleName("MainWindow");
            zoneCE = new ContainerEx();
            zoneCE.setTop(new Extent(80));
            zoneCE.setBottom(new Extent(26));
            zoneCE.setRight(new Extent(10));
            zoneCE.setLeft(new Extent(10));
            zoneCE.setPosition(ABSOLUTE);
            zoneCE.setStyleName("ZoneCE");
            add(zoneCE);
            headCE = new ContainerEx();
            headCE.setTop(new Extent(0));
            headCE.setHeight(new Extent(97));
            headCE.setRight(new Extent(0));
            headCE.setLeft(new Extent(0));
            headCE.setPosition(ABSOLUTE);
            headCE.setStyleName("HeadCE");
            add(headCE);
            mainCE = new ContainerEx();
            mainCE.setBottom(new Extent(47));
            mainCE.setTop(new Extent(141));
            mainCE.setLeft(new Extent(4));
            mainCE.setRight(new Extent(4));
            mainCE.setPosition(ABSOLUTE);
            mainCE.setStyleName("MainCE");
            mainCE.setScrollBarPolicy(Scrollable.CLIPHIDE);
            add(mainCE);
            titleCE = new ContainerEx();
            titleCE.setTop(new Extent(97));
            titleCE.setLeft(new Extent(30));
            titleCE.setPosition(ABSOLUTE);
            titleCE.setScrollBarPolicy(Scrollable.CLIPHIDE);
            add(titleCE);
            LabelEx titleLA = new LabelEx(app.getString("app.title"));
            titleLA.setStyleName("title");
            titleCE.add(titleLA);
            legalCE = new ContainerEx();
            legalCE.setHeight(new Extent(20));
            legalCE.setBottom(new Extent(0));
            legalCE.setLeft(new Extent(0));
            legalCE.setRight(new Extent(0));
            legalCE.setPosition(ABSOLUTE);
            LabelEx legal = new LabelEx(app.getCopyright());
            legal.setStyleName("Copyright");
            legalCE.add(legal);
            add(legalCE);
            String logoPart = app.getUrlLogoProprietaire();
            if (logoPart != null && logoPart.trim().length() > 0) {
                ImageReference logoImg = new HttpImageReference(logoPart);
                logoPartCE = new ContainerEx();
                logoPartCE.setTop(new Extent(0));
                logoPartCE.setRight(new Extent(24));
                logoPartCE.setWidth(new Extent(70));
                logoPartCE.setHeight(new Extent(70));
                logoPartCE.setPosition(ABSOLUTE);
                add(logoPartCE);
                LabelEx le = new LabelEx(logoImg);
                le.setWidth(logoPartCE.getWidth());
                le.setHeight(logoPartCE.getHeight());
                logoPartCE.add(le);
            } else {
                logoPartCE = null;
            }
            if (app.getUser() != null) {
                homeWindow = app.newHomeWindow();
            }
        } catch (Throwable t) {
            BaseApp.logger.log(Level.SEVERE, "EE", t);
        }
    }

    private class CloseActionListener implements ActionListener {

        private final WindowPane w;

        private CloseActionListener(WindowPane w) {
            this.w = w;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            app.removeWindow(w);
        }
    }

    private class WindowImplicitCloseAlert extends BasicWindow {

        private final WindowPane newPane;
        private final WindowPane oldPane;
        private final WindowPane newPaneParent;
        private final BaseMainPane bmp;

        public WindowImplicitCloseAlert(BaseApp app, BaseMainPane bmp, WindowPane oldPane, WindowPane newPane, WindowPane newPaneParent) {
            super(app, app.getString("attentionFermeture"), "outils", new Extent(400), new Extent(150));
            setModal(true);
            setStyleName("DefaultW");
            LabelEx label = new LabelEx(String.format(app.getString("attentionFermetureMsg"), oldPane.getTitle()));
            label.setIntepretNewlines(true);
            label.setLineWrap(true);
            this.newPane = newPane;
            this.newPaneParent = newPaneParent;
            this.oldPane = oldPane;
            this.bmp = bmp;
            Column col = new Column();
            add(col);
            Row row = new Row();
            col.add(row);
            row.add(new LabelEx(app.getStyles().getIcon("alerte")));
            row.add(label);
            row = new Row();
            row.setStyleName("Buttons");
            col.add(row);
            Button ok = new ButtonEx2(getBaseString("ok"));
            ok.setStyleName("Button");
            row.add(ok);
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ok();
                }
            });
            Button cancel = new ButtonEx2(getBaseString("cancel"));
            cancel.setStyleName("Button");
            row.add(cancel);
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            });
        }

        public void cancel() {
            userClose();
        }

        public void ok() {
            userClose();
            app.removeWindow(this);
            app.removeWindow(oldPane);
            app.addWindow(newPane, newPaneParent);
        }
    }

    /**
     * affiche un message fugitif en bas de page. peut ne pas être implémenté
     * (dans ce cas ne fait rien)
     *
     * @param message le message
     */
    @Override
    public void toast(String message) {

    }
}
