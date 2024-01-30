package com.cachat.prj.echo3.ng;

import java.util.ArrayList;
import java.util.List;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.extras.app.menu.DefaultMenuModel;

/**
 *
 * @author scachat
 */
public class MenuBase extends DefaultMenuModel {

    public static final String ACTION_LISTENERS_CHANGED_PROPERTY = "actionListeners";
    protected MenuBar root;

    public MenuBase() {
    }

    public MenuBase(String text) {
        super();
        setText(text);
    }

    public MenuBase(ImageReference icon) {
        super();
        setIcon(icon);
    }

    public MenuBase(String text, ImageReference icon) {
        super();
        setText(text);
        setIcon(icon);
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

    protected void setRoot(MenuBar root) {
        this.root = root;
    }

    @Override
    public String getId() {
        return "Menu_" + hashCode();
    }
}
