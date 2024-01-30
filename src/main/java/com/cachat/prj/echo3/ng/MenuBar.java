package com.cachat.prj.echo3.ng;

import static com.cachat.prj.echo3.ng.ContainerEx.PROPERTY_BORDER_IMAGE;
import nextapp.echo.app.FillImage;
import nextapp.echo.app.FillImageBorder;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.extras.app.MenuBarPane;
import nextapp.echo.extras.app.menu.DefaultMenuModel;
import nextapp.echo.extras.app.menu.ItemModel;
import nextapp.echo.extras.app.menu.MenuModel;
import nextapp.echo.extras.app.menu.OptionModel;

/**
 *
 * @author scachat
 */
public class MenuBar extends MenuBarPane implements MenuContainer {

    public static final String PROPERTY_MENU_BORDER_IMAGE = "menuBorderImage";
    public static final String PROPERTY_MENU_BAR_BACKGROUND_IMAGE = "menuBarBackgroundImage";

    DefaultMenuModel model;

    public MenuBar() {
        setModel(model = new DefaultMenuModel());
        addActionListener((e) -> propagate(e, model));
    }

    private void propagate(ActionEvent e, MenuModel item) {
        for (int i = 0; i < item.getItemCount(); i++) {
            ItemModel x = item.getItem(i);
            if (x instanceof MenuModel) {
                propagate(e, (MenuModel) x);
            } else if (x instanceof OptionModel) {
                if (e.getActionCommand().equals(x.getId())) {
                    ((MenuItem) x).fireActionEvent(e);
                }
            }
        }
    }

    @Override
    public void add(MenuItem mi) {
        mi.setRoot(this);
        model.addItem(mi);
        firePropertyChange(MODEL_CHANGED_PROPERTY, null, model);
    }

    @Override
    public void add(Menu mi) {
        mi.setRoot(this);
        model.addItem(mi);
        firePropertyChange(MODEL_CHANGED_PROPERTY, null, model);
    }

    public void remove(MenuItem mi) {
        mi.setRoot(null);
        model.removeItem(mi);
        firePropertyChange(MODEL_CHANGED_PROPERTY, null, model);
    }

    public void remove(Menu mi) {
        mi.setRoot(null);
        model.removeItem(mi);
        firePropertyChange(MODEL_CHANGED_PROPERTY, null, model);
    }

    public void setMenuBarBackgroundImage(FillImage newValue) {
        set(PROPERTY_MENU_BAR_BACKGROUND_IMAGE, newValue);
    }

    public FillImageBorder getBorderImage() {
        return (FillImageBorder) get(PROPERTY_BORDER_IMAGE);
    }

    public void setBorderImage(FillImageBorder newValue) {
        set(PROPERTY_BORDER_IMAGE, newValue);
    }
}
