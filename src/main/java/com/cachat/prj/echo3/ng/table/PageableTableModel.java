package com.cachat.prj.echo3.ng.table;

import nextapp.echo.app.table.TableModel;

/**
 * An interface for paging through rows of data in a
 * <code>TableModel</code>. The idea behind a
 * <code>PageableTableModel</code> is that the number of rows reports by it is
 * simple a page worth and that there may be more rows waiting in the wings.
 *
 * @author Jason Dalton
 */
public interface PageableTableModel extends TableModel {

    /**
     * Returns the currently displayed page in the range
     * <code>(page &gt;= 0 && page &lt getTotalPages())</code>
     *
     *
     * @return the currently displayed page
     */
    public int getCurrentPage();

    /**
     * Returns the number of viewable rows per each displayed page
     *
     * @return the number of viewable rows per each displayed page
     */
    public int getRowsPerPage();

    /**
     * Returns the total number of pages this model contains based on total rows
     * / rows per page. Note it will always be a number greater than or equal to
     * 1 since a underlying row count less than the rows per page, still
     * constitutes a at least one page from a UI perspective. Also
     * <code>TableModel</code> wih zero rows is still considered to have 1 page
     * from a UI perspective.
     *
     * @return the total number of pages for the underlying row count.
     */
    public int getTotalPages();

    /**
     * Returns the total number of rows in the underlying
     * <code>TableModel</code>
     *
     * @return the total number of rows in the underlying <code>TableModel
     */
    public int getTotalRows();

    /**
     * Sets the current page this model should reflect. Implementations that
     * sublcass
     * <code>DefaultTableModel</code> should call fireTableDataChanged() to
     * indicate that the table structure has changed and hence a redraw is
     * necessary.
     *
     * @param page the page to display. This should in the range
     * <code>(page &gt;= 0 && page &lt getTotalPages())</code>
     *
     * @throws IllegalArgumentException if the page is not in the range
     * <code>(page &gt;= 0 && page &lt getTotalPages())</code>
     */
    public void setCurrentPage(int page);

    /**
     * Sets the number of viewable rows per each displayed page. Implementations
     * that sublcass
     * <code>DefaultTableModel</code> should call fireTableDataChanged() to
     * indicate that the table structure has changed and hence a redraw is
     * necessary.
     *
     * @param rows the number of rows to display
     */
    public void setRowsPerPage(int rows);

    /**
     * This converts the unpaged model row index into the equivalent paged view
     * row index. When the underlying TableModel is being paged, you can use
     * this method to map from the unpaged Model set of row indexes into the
     * paged View row indexes.
     *
     * @param modelRowIndex - a row index in terms of the unpaged model that you
     * want to convert to a paged view row index
     * @return a paged view row index
     */
    public int toPagedViewRowIndex(int modelRowIndex);

    /**
     * This converts the paged view row index into the equivalent unpaged model
     * row index. When the underlying TableModel is being paged, you can use
     * this method to map from the paged View set of row indexes into the
     * underlying unpaged
     * <code>TableModel</code> row indexes.
     *
     * @param viewRowIndex - a row index in terms of the page view that you want
     * to convert to a unpaged model row index
     * @return a unpaged model row index
     */
    public int toUnpagedModelRowIndex(int viewRowIndex);
}
