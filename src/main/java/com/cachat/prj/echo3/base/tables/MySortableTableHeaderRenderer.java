package com.cachat.prj.echo3.base.tables;

import com.cachat.prj.echo3.ng.table.SortableTableHeaderRenderer;
import nextapp.echo.app.Button;
import com.cachat.prj.echo3.ng.table.SortableTableModel;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Extent;
import nextapp.echo.app.ImageReference;

/**
 *
 * @author scachat
 */
class MySortableTableHeaderRenderer extends SortableTableHeaderRenderer {

    public MySortableTableHeaderRenderer() {
    }

    @Override
    protected Button getSortButton(String label, int column, SortableTableModel model) {
        if (label == null || label.trim().length() == 0) {
            Button b = new Button();
            b.setStyleName("TableHeaderBlank");
            b.setHeight(new Extent(100, Extent.PERCENT));
            b.setWidth(new Extent(100, Extent.PERCENT));
            b.setTextPosition(new Alignment(Alignment.CENTER, Alignment.DEFAULT));
            return b;
        }
        Button button = new Button(label);
        button.addActionListener(getSortButtonListener(column, model));
        button.setStyleName("TableHeader");
        button.setHeight(new Extent(100, Extent.PERCENT));
        button.setWidth(new Extent(100, Extent.PERCENT));
        button.setTextPosition(new Alignment(Alignment.CENTER, Alignment.DEFAULT));
        ImageReference icon = null;
        if (model.getCurrentSortColumn() == column) {
            int sortDirective = model.getSortDirective(column);
            if (sortDirective == SortableTableModel.ASCENDING) {
                icon = getUpArrowImage();
            } else if (sortDirective == SortableTableModel.DESCENDING) {
                icon = getDownArrowImage();
            } else {
                icon = null;
            }
            button.setIcon(icon);
        }
        return button;
    }
}
