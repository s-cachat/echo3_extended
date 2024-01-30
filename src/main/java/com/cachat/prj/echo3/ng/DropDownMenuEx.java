package com.cachat.prj.echo3.ng;

import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.extras.app.DropDownMenu;
import nextapp.echo.extras.app.menu.DefaultMenuModel;
import nextapp.echo.extras.app.menu.ItemModel;
import nextapp.echo.extras.app.menu.MenuModel;
import nextapp.echo.extras.app.menu.OptionModel;

/**
 *
 * @author scachat
 */
public class DropDownMenuEx extends DropDownMenu implements MenuContainer {

    DefaultMenuModel model;

    public DropDownMenuEx() {
        setModel(model = new DefaultMenuModel());
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                propagate(e, model);
            }
        });
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
    }

    @Override
    public void add(Menu mi) {
        mi.setRoot(this);
        model.addItem(mi);
    }
}
