package com.cachat.prj.echo3.ng;

import nextapp.echo.app.ListBox;
import nextapp.echo.app.list.ListModel;
import nextapp.echo.app.list.ListSelectionModel;

/**
 *
 * @author scachat
 */
public class ListBoxEx extends ListBox {

    public ListBoxEx() {
    }

    public ListBoxEx(ListModel model) {
        super(model);
    }

    public ListBoxEx(ListModel model, ListSelectionModel selectionModel) {
        super(model, selectionModel);
    }

    public ListBoxEx(Object[] itemArray) {
        super(itemArray);
    }
}
