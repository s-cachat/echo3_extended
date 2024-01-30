package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.able.Positionable;
import com.cachat.prj.echo3.ng.able.Scrollable;
import java.util.ArrayList;
import java.util.List;
import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 *
 * @author scachat
 */
public class Module extends ContainerEx {

    ContainerEx backgroundPane;
    ContainerEx contentPane;
    protected final BaseApp app;
    LabelEx titleLabel;

    public Module(BaseApp app, String title) {
        this(app, title, false);
    }

    public Module(BaseApp app, String title, boolean closeButton) {
        this.app = app;
        setPosition(Positionable.ABSOLUTE);
        backgroundPane = new ContainerEx();
        backgroundPane.setStyleName("ModuleBackground");

        backgroundPane.setTop(new Extent(0));
        backgroundPane.setRight(new Extent(0));
        backgroundPane.setBottom(new Extent(0));
        backgroundPane.setLeft(new Extent(0));
        backgroundPane.setPosition(Positionable.ABSOLUTE);
        add(backgroundPane);
        contentPane = new ContainerEx();
        contentPane.setRight(new Extent(13));
        contentPane.setBottom(new Extent(11));
        contentPane.setLeft(new Extent(13));
        contentPane.setPosition(Positionable.ABSOLUTE);
        contentPane.setScrollBarPolicy(Scrollable.AUTO);
        if (title != null && title.length() > 0) {
            ContainerEx titleCont = new ContainerEx();
            titleCont.setStyleName("ModuleTitle");
            titleCont.setTop(new Extent(10));
            titleCont.setRight(new Extent(13));
            titleCont.setHeight(new Extent(29));
            titleCont.setLeft(new Extent(12));
            titleCont.setPosition(Positionable.ABSOLUTE);

            titleLabel = new LabelEx(title);
            if (!closeButton) {
                titleCont.add(titleLabel);
            } else {
                ContainerEx c1 = new ContainerEx();
                c1.setLeft(new Extent(0));
                c1.setTop(new Extent(0));
                c1.setRight(new Extent(0));
                c1.setInsets(new Insets(4));
                c1.add(titleLabel);
                c1.setPosition(Positionable.ABSOLUTE);
                titleCont.add(c1);

                ContainerEx c2 = new ContainerEx();
                c2.setRight(new Extent(8));
                c2.setTop(new Extent(0));
                c2.setWidth(new Extent(30));
                c2.setHeight(new Extent(24));
                ButtonEx button = new ButtonEx(app.getStyles().getIcon("home"));
                button.setBackground(Color.TRANSPARENT);
                button.setRolloverBackground(Color.TRANSPARENT);
                button.setBorder(null);
                c2.setBackground(Color.TRANSPARENT);
                c2.add(button);
                c2.setPosition(Positionable.ABSOLUTE);
                titleCont.add(c2);
                button.addActionListener(this::dispatchCloseAction);
            }
            titleLabel.setStyleName("ModuleTitle");

            add(titleCont);
            contentPane.setTop(new Extent(11 + 35));
        } else {
            contentPane.setTop(new Extent(11));
        }
        add(contentPane);
    }
/**
* change le titre
* @param title le titre
 */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    private List<ActionListener> listeners = new ArrayList<>();

    /**
     * ajoute un listener pour le bouton de fermeture
     *
     * @param listener
     */
    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    /**
     * enleve un listener pour le bouton de fermeture
     *
     * @param listener
     */
    public void removeActionListener(ActionListener listener) {
        listeners.remove(listener);
    }

    /**
     * dispatche l'action du bouton de fermeture
     */
    private void dispatchCloseAction(ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    public ContainerEx getContentPane() {
        return contentPane;
    }

    public void setContent(Component item) {
        this.contentPane.removeAll();
        this.contentPane.add(item);
    }
}
