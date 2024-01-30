package com.cachat.prj.echo3.ng.table;

import com.cachat.prj.echo3.ng.TableEx;
import nextapp.echo.app.table.TableColumnModel;
import nextapp.echo.app.table.TableModel;

/**
 *
 * @author scachat
 * @deprecated
 */
@Deprecated
public class PageableSortableTable extends TableEx {

    public PageableSortableTable() {
    }

    public PageableSortableTable(int columns, int rows) {
        super(columns, rows);
    }

    public PageableSortableTable(TableModel model) {
        super(model);
    }

    public PageableSortableTable(TableModel model, TableColumnModel columnModel) {
        super(model, columnModel);
    }
}
