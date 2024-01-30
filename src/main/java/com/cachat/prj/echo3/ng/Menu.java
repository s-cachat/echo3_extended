package com.cachat.prj.echo3.ng;

import nextapp.echo.extras.app.menu.DefaultMenuModel;
import nextapp.echo.app.ImageReference;
import nextapp.echo.extras.app.menu.AbstractMenuComponent;

/**
 *
 * @author scachat
 */
public class Menu extends DefaultMenuModel implements MenuContainer {

    protected AbstractMenuComponent root;

    public Menu() {
        super(null, "");
    }

    public Menu(String text) {
        super(null, text);
    }

    public Menu(ImageReference icon) {
        super(null, null, icon);
    }

    public Menu(String text, ImageReference icon) {
        super(null, text, icon);
    }

    protected void setRoot(AbstractMenuComponent root) {
        this.root = root;
    }

    @Override
    public void add(MenuItem mi) {
        mi.setRoot(root);
        super.addItem(mi);
    }

    @Override
    public void add(Menu mi) {
        mi.setRoot(root);
        super.addItem(mi);
    }

    @Override
    public String toString() {
        return getText() == null ? getId() : getText();
    }

}
