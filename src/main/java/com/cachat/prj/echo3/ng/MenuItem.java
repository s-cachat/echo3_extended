package com.cachat.prj.echo3.ng;

import java.util.ArrayList;
import java.util.List;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.extras.app.menu.AbstractMenuComponent;
import nextapp.echo.extras.app.menu.DefaultOptionModel;

/**
 *
 * @author scachat
 */
public class MenuItem extends DefaultOptionModel {

    public static final String ACTION_LISTENERS_CHANGED_PROPERTY = "actionListeners";
    protected AbstractMenuComponent root;

    public MenuItem() {
        super(null, "", null);
    }

    public MenuItem(String text) {
        super(null, text, null);
    }

    public MenuItem(ImageReference icon) {
        super(null, null, icon);
    }

    public MenuItem(String text, ImageReference icon) {
        super(null, text, icon);
    }
    public MenuItem(String text, ImageReference icon,ActionListener al) {
        super(null, text, icon);
        addActionListener(al);
    }
    private List<ActionListener> listeners = new ArrayList<>();

    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        listeners.remove(l);
    }

    void fireActionEvent(ActionEvent e) {
        for (ActionListener l : listeners) {
            l.actionPerformed(e);
        }
    }

    protected void setRoot(AbstractMenuComponent root) {
        this.root = root;
    }

    @Override
    public String getId() {
        return "Menu_" + hashCode();
    }

    @Override
    public String toString() {
        return getText() == null ? getId() : getText();
    }
}
