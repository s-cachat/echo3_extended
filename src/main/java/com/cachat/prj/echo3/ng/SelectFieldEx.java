package com.cachat.prj.echo3.ng;

import nextapp.echo.app.SelectField;
import nextapp.echo.app.list.ListModel;

/**
 *
 * @author scachat
 */
public class SelectFieldEx extends SelectField {

    public SelectFieldEx() {
    }

    public SelectFieldEx(ListModel model) {
        super(model);
    }

    public SelectFieldEx(Object[] items) {
        super(items);
    }
}
