package com.cachat.prj.echo3.ng;

import com.cachat.prj.echo3.ng.able.Positionable;
import com.cachat.prj.echo3.ng.able.Scrollable;
import nextapp.echo.app.Component;
import nextapp.echo.app.IllegalChildException;
import nextapp.echo.app.PaneContainer;

/**
 *
 * @author scachat
 */
public class GroupBox extends ContainerEx implements PaneContainer, Positionable, Scrollable {

    private String title;
    public static final String TITLE_CHANGED_PROPERTY = "title";

    public GroupBox() {
    }

    public GroupBox(Component c) {
        super.add(c);
    }

    public GroupBox(Component c, String title) {
        super.add(c);
        setTitle(title);
    }

    @Override
    public void add(Component c) {
        removeAll();
        super.add(c, -1);
    }

    @Override
    public void add(Component c, int n) throws IllegalChildException {
        removeAll();
        super.add(c, n);
    }

    @Override
    public void clear() {
        //TODO
    }

    public String getTitle() {
        return title;
    }

    public final void setTitle(String newTitle) {
        String oldValue = title;
        title = newTitle;
        firePropertyChange(TITLE_CHANGED_PROPERTY, oldValue, newTitle);
    }
}
