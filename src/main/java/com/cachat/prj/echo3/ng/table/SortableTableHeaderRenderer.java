package com.cachat.prj.echo3.ng.table;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.Table;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.table.DefaultTableCellRenderer;

/**
 *
 * @author scachat
 */
public class SortableTableHeaderRenderer extends DefaultTableCellRenderer {

    public static final ImageReference DEFAULT_UP_ARROW_IMAGE = new ResourceImageReference("/com/cachat/prj/echo3/openlayer/resources/img/ArrowUp.gif");
    public static final ImageReference DEFAULT_DOWN_ARROW_IMAGE = new ResourceImageReference("/com/cachat/prj/echo3/openlayer/resources/img/ArrowDown.gif");
    private final ImageReference upArrowImage = DEFAULT_UP_ARROW_IMAGE;
    private final ImageReference downArrowImage = DEFAULT_DOWN_ARROW_IMAGE;

    public ImageReference getUpArrowImage() {
        return upArrowImage;
    }

    public ImageReference getDownArrowImage() {
        return downArrowImage;
    }

    protected ActionListener getSortButtonListener(final int column, final SortableTableModel model) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int sortDirective = model.getSortDirective(column);
                int newSortDirective;
                if (sortDirective == SortableTableModel.NOT_SORTED) {
                    newSortDirective = SortableTableModel.ASCENDING;
                } else if (sortDirective == SortableTableModel.ASCENDING) {
                    newSortDirective = SortableTableModel.DESCENDING;
                } else {
                    newSortDirective = SortableTableModel.NOT_SORTED;
                }
                model.sortByColumn(column, newSortDirective);
            }
        };
    }

    protected Button getSortButton(String label, int column, SortableTableModel model) {
        Button button = new Button(label);
        button.addActionListener(getSortButtonListener(column, model));
        button.setTextPosition(new Alignment(Alignment.LEFT, Alignment.DEFAULT));
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

    @Override
    public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
        Button b = getSortButton(String.valueOf(value), column, (SortableTableModel) table.getModel());
        if (b.getText() != null && b.getText().trim().length() > 0) {
            b.addActionListener(getSortButtonListener(column, (SortableTableModel) table.getModel()));
        }
        return b;
    }

}
