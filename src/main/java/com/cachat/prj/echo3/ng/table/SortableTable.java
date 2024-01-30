package com.cachat.prj.echo3.ng.table;

import com.cachat.prj.echo3.ng.TableEx;
import nextapp.echo.app.event.TableModelListener;
import nextapp.echo.app.table.TableColumnModel;
import nextapp.echo.app.table.TableModel;

/**
 *
 * @author scachat
 * @deprecated
 */
@Deprecated
public class SortableTable extends TableEx {

    public SortableTable() {
    }

    public SortableTable(int columns, int rows) {
        super(columns, rows);
    }

    public SortableTable(SortableTableModel model) {
        super(model);
        super.setModel(model);
    }

    public SortableTable(SortableTableModel model, TableColumnModel columnModel) {
        super(model, columnModel);
        super.setModel(model);
    }

    @Override
    public void setModel(TableModel newValue) {
        SortableTableModel m = (SortableTableModel) newValue;
        super.setModel(m);
    }

    private static class IndirectModel implements SortableTableModel {

        private final SortableTableModel model;

        public IndirectModel(SortableTableModel model) {
            this.model = model;
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            model.addTableModelListener(l);
        }

        @Override
        public Class getColumnClass(int column) {
            return model.getColumnClass(column);
        }

        @Override
        public int getColumnCount() {
            return model.getColumnCount();
        }

        @Override
        public String getColumnName(int column) {
            return model.getColumnName(column);
        }

        @Override
        public int getRowCount() {
            return model.getRowCount();
        }

        @Override
        public Object getValueAt(int column, int row) {
            return model.getValueAt(column, model.toUnsortedModelRowIndex(row));
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            model.removeTableModelListener(l);
        }

        @Override
        public int getCurrentSortColumn() {
            return model.getCurrentSortColumn();
        }

        @Override
        public int getSortDirective(int column) {
            return model.getSortDirective(column);
        }

        @Override
        public void setSortDirective(int column, int sortDirective) {
            model.setSortDirective(column, sortDirective);
        }

        @Override
        public void sortByColumn(int column, int sortDirective) {
            model.sortByColumn(column, sortDirective);
        }

        @Override
        public int toUnsortedModelRowIndex(int viewRowIndex) {
            return model.toUnsortedModelRowIndex(viewRowIndex);
        }

        @Override
        public int toSortedViewRowIndex(int modelRowIndex) {
            return model.toSortedViewRowIndex(modelRowIndex);
        }

    }

}
