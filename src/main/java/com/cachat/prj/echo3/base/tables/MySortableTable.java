package com.cachat.prj.echo3.base.tables;

import com.cachat.prj.echo3.ng.table.SortableTable;
import com.cachat.prj.echo3.ng.table.SortableTableModel;
import nextapp.echo.app.Color;
import nextapp.echo.app.table.TableModel;

/**
 * une table avec le style webinforoute
 *
 * @author scachat
 */
public class MySortableTable extends SortableTable {

    public MySortableTable(TableModel tm) {
        super((SortableTableModel) tm);
        setHeaderBackground(new Color(0xed, 0xed, 0xed));
        setDefaultHeaderRenderer(new MySortableTableHeaderRenderer());
    }
}
