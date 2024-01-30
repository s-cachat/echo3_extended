package com.cachat.prj.echo3.base.tables;

import nextapp.echo.app.Color;
import com.cachat.prj.echo3.ng.table.PageableSortableTable;
import nextapp.echo.app.table.TableModel;

/**
 * une table avec le style webinforoute
 *
 * @author scachat
 */
public class MyPageableSortableTable extends PageableSortableTable {

    public MyPageableSortableTable(TableModel tm) {
        super(tm);
        setHeaderBackground(new Color(0xed, 0xed, 0xed));
        setDefaultHeaderRenderer(new MySortableTableHeaderRenderer());
    }
}
