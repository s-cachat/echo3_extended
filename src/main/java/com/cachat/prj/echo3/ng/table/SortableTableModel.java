package com.cachat.prj.echo3.ng.table;

import nextapp.echo.app.table.TableModel;

/**
 * A
 * <code>TableModel</code> representing sortable data.
 *
 * @author Jason Dalton
 */
public interface SortableTableModel extends TableModel {

    /**
     * A sort directive to sort in ascending order
     */
    public static final int ASCENDING = 1;
    /**
     * A sort directive to sort in descending order
     */
    public static final int DESCENDING = -1;
    /**
     * A sort directive to not sort at all
     */
    public static final int NOT_SORTED = 0;

    /**
     * Returns the primarily sorted column number.
     *
     * @return the column number.
     */
    public int getCurrentSortColumn();

    /**
     * Returns the sort directive for the specified column
     *
     * @return retutrns on of these values
     * <ul>
     * <li>SortableTableModel.DESCENDING</li>
     * <li>SortableTableModel.NOT_SORTED</li>
     * <li>SortableTableModel.ASCEDING</li>
     * </ul>
     */
    public int getSortDirective(int column);

    /**
     * Sets th sort directive for the given TableModel column.
     *
     * @param column the column in question
     * @param sortDirective must be one of :
     * <ul>
     * <li>SortableTableModel.DESCENDING</li>
     * <li>SortableTableModel.NOT_SORTED</li>
     * <li>SortableTableModel.ASCEDING</li>
     * </ul>
     */
    public void setSortDirective(int column, int sortDirective);

    /**
     * Sorts the data backing this model based on the given column and sort
     * directive
     *
     * @param column the column to sort by
     * @param sortDirective must be one of :
     * <ul>
     * <li>SortableTableModel.DESCENDING</li>
     * <li>SortableTableModel.NOT_SORTED</li>
     * <li>SortableTableModel.ASCEDING</li>
     * </ul>
     */
    public void sortByColumn(int column, int sortDirective);

    /**
     * This converts the sorted view row index into the equivalent unsorted
     * model row index. When the underlying TableModel is being sorted, you can
     * use this method to map from the sorted View set of row indexes into the
     * underlying unsorted
     * <code>TableModel</code> row indexes.
     *
     * @param viewRowIndex - a row index in terms of the sorted view that you
     * want to convert to a unsorted model row index
     * @return a unsorted model row index
     */
    public int toUnsortedModelRowIndex(int viewRowIndex);

    /**
     * This converts the unsorted model row index into the equivalent sorted
     * view row index. When the underlying TableModel is being sorted, you can
     * use this method to map from the underlying TableModel set of row indexes
     * into sorted View row indexes.
     *
     * @param modelRowIndex - a row index in terms of the unsorted model that
     * you want to convert to a sorted view row index
     * @return a sorted view row index
     */
    public int toSortedViewRowIndex(int modelRowIndex);
}
