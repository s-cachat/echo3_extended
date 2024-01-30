package com.cachat.prj.echo3.ng.table;

import com.cachat.prj.echo3.models.ListTableModel;
import java.util.List;
import nextapp.echo.app.event.TableModelEvent;
import nextapp.echo.app.event.TableModelListener;

/**
 *
 * @author scachat
 */
public class DefaultPageableTableModel implements PageableTableModel {

    private final ListTableModel listModel;

    public ListTableModel getUnderlyingTableModel() {
        return listModel;
    }

    public DefaultPageableTableModel(ListTableModel listModel) {
        this.listModel = listModel;
    }
    int currentPage = 0;
    int rowPerPage = 25;

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public int getRowsPerPage() {
        return rowPerPage;
    }

    @Override
    public int getTotalPages() {
        int v1 = (int) (rowPerPage == 0 ? 0 : Math.ceil(((double) listModel.getRowCount()) / ((double) rowPerPage)));
        int v2 = rowPerPage == 0 ? 0 : listModel.getRowCount() / rowPerPage;
        System.err.printf("ROW count %d rows, %d per page, 1=%d / 2=%d\r\n", listModel.getRowCount(), rowPerPage, v1, v2);
        return v1;
    }

    @Override
    public int getTotalRows() {
        return listModel.getRowCount();
    }

    @Override
    public int getRowCount() {
        int first = currentPage * rowPerPage;
        int afterLast = Math.min(listModel.getRowCount(), first + rowPerPage);
        return afterLast - first;
    }

    @Override
    public Object getValueAt(int column, int row) {
        int first = currentPage * rowPerPage;
        return listModel.getValueAt(column, row + first); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        fireTableDataChanged();
    }

    @Override
    public void setRowsPerPage(int rowPerPage) {
        this.rowPerPage = rowPerPage;
    }

    @Override
    public int toPagedViewRowIndex(int modelRowIndex) {
        int first = currentPage * rowPerPage;
        return modelRowIndex - first;
    }

    @Override
    public int toUnpagedModelRowIndex(int viewRowIndex) {
        int first = currentPage * rowPerPage;
        return viewRowIndex + first;
    }

    public void update(String where, List arg) {
        listModel.update(where, arg);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listModel.addTableModelListener(l);
    }

    public void fireTableCellUpdated(int column, int row) {
        listModel.fireTableCellUpdated(column, row);
    }

    public void fireTableDataChanged() {
        listModel.fireTableDataChanged();
    }

    public void fireTableRowsDeleted(int firstRow, int lastRow) {
        listModel.fireTableRowsDeleted(firstRow, lastRow);
    }

    public void fireTableRowsInserted(int firstRow, int lastRow) {
        listModel.fireTableRowsInserted(firstRow, lastRow);
    }

    public void fireTableRowsUpdated(int firstRow, int lastRow) {
        listModel.fireTableRowsUpdated(firstRow, lastRow);
    }

    public void fireTableStructureChanged() {
        listModel.fireTableStructureChanged();
    }

    public void fireTableChanged(TableModelEvent e) {
        listModel.fireTableChanged(e);
    }

    @Override
    public Class getColumnClass(int column) {
        return listModel.getColumnClass(column);
    }

    @Override
    public String getColumnName(int column) {
        return listModel.getColumnName(column);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listModel.removeTableModelListener(l);
    }

    @Override
    public int getColumnCount() {
        return listModel.getColumnCount();
    }
}
