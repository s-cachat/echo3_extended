package com.cachat.prj.echo3.ng;

import nextapp.echo.app.Color;
import nextapp.echo.app.Table;
import nextapp.echo.app.table.TableColumnModel;
import nextapp.echo.app.table.TableModel;

/**
 *
 * @author scachat
 */
public class TableEx extends Table {

    public static final String PROPERTY_HEADER_BACKGROUND = "headerBackground";

    public TableEx() {
    }

    public TableEx(int columns, int rows) {
        super(columns, rows);
    }

    public TableEx(TableModel model) {
        super(model);
    }

    public TableEx(TableModel model, TableColumnModel columnModel) {
        super(model, columnModel);
    }

    public void setHeaderBackground(Color newValue) {
        set(PROPERTY_HEADER_BACKGROUND, newValue);
    }

    public Color getHeaderBackground() {
        return (Color) get(PROPERTY_HEADER_BACKGROUND);
    }
}
