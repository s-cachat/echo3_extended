package com.cachat.prj.echo3.slickgrid;

import nextapp.echo.app.Color;

/**
 * Les options
 *
 * @author scachat
 */
public class SlickGridOptions {

    private Integer defaultColumnWidth;
    private Integer headerRowHeight;
    private Integer rowHeight;
    private Integer topPanelHeight;
    private Integer frozenColumn;
    private Integer frozenRow;
    private Boolean editable;
    private Boolean enableAddRow;
    private Boolean enableCellNavigation;
    private Boolean asyncEditorLoading;
    private Boolean autoEdit;
    private Boolean autoHeight;
    private Boolean enableColumnReorder;
    private Boolean enableTextSelectionOnCells;
    private Boolean forceFitColumns;
    private Boolean fullWidthRows;
    private Boolean leaveSpaceForNewRows;
    private Boolean multiColumnSort;
    private Boolean multiSelect;
    private Boolean showHeaderRow;
    private Boolean showFooterRow;
    private Boolean showTopPanel;
    private Boolean syncColumnCellResize;
    private String headerBackgroundColor;
    private String backgroundColor;
    private String oddBackgroundColor;
    private String evenBackgroundColor;
    private String selectHighLightColor;

    public Integer getDefaultColumnWidth() {
        return defaultColumnWidth;
    }

    public void setDefaultColumnWidth(Integer defaultColumnWidth) {
        this.defaultColumnWidth = defaultColumnWidth;
    }

    public Integer getHeaderRowHeight() {
        return headerRowHeight;
    }

    public void setHeaderRowHeight(Integer headerRowHeight) {
        this.headerRowHeight = headerRowHeight;
    }

    public Integer getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(Integer rowHeight) {
        this.rowHeight = rowHeight;
    }

    public Integer getTopPanelHeight() {
        return topPanelHeight;
    }

    public void setTopPanelHeight(Integer topPanelHeight) {
        this.topPanelHeight = topPanelHeight;
    }

    public Boolean isEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean isEnableAddRow() {
        return enableAddRow;
    }

    public void setEnableAddRow(Boolean enableAddRow) {
        this.enableAddRow = enableAddRow;
    }

    public Boolean isEnableCellNavigation() {
        return enableCellNavigation;
    }

    public void setEnableCellNavigation(Boolean enableCellNavigation) {
        this.enableCellNavigation = enableCellNavigation;
    }

    public Boolean isAsyncEditorLoading() {
        return asyncEditorLoading;
    }

    public void setAsyncEditorLoading(Boolean asyncEditorLoading) {
        this.asyncEditorLoading = asyncEditorLoading;
    }

    public Boolean isAutoEdit() {
        return autoEdit;
    }

    public void setAutoEdit(Boolean autoEdit) {
        this.autoEdit = autoEdit;
    }

    public Boolean isAutoHeight() {
        return autoHeight;
    }

    public void setAutoHeight(Boolean autoHeight) {
        this.autoHeight = autoHeight;
    }

    public Boolean isEnableColumnReorder() {
        return enableColumnReorder;
    }

    public void setEnableColumnReorder(Boolean enableColumnReorder) {
        this.enableColumnReorder = enableColumnReorder;
    }

    public Boolean isEnableTextSelectionOnCells() {
        return enableTextSelectionOnCells;
    }

    public void setEnableTextSelectionOnCells(Boolean enableTextSelectionOnCells) {
        this.enableTextSelectionOnCells = enableTextSelectionOnCells;
    }

    public Boolean isForceFitColumns() {
        return forceFitColumns;
    }

    public void setForceFitColumns(Boolean forceFitColumns) {
        this.forceFitColumns = forceFitColumns;
    }

    public Boolean isFullWidthRows() {
        return fullWidthRows;
    }

    public void setFullWidthRows(Boolean fullWidthRows) {
        this.fullWidthRows = fullWidthRows;
    }

    public Boolean isLeaveSpaceForNewRows() {
        return leaveSpaceForNewRows;
    }

    public void setLeaveSpaceForNewRows(Boolean leaveSpaceForNewRows) {
        this.leaveSpaceForNewRows = leaveSpaceForNewRows;
    }

    public Boolean isMultiColumnSort() {
        return multiColumnSort;
    }

    public void setMultiColumnSort(Boolean multiColumnSort) {
        this.multiColumnSort = multiColumnSort;
    }

    public Boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public Boolean isShowHeaderRow() {
        return showHeaderRow;
    }

    public void setShowHeaderRow(Boolean showHeaderRow) {
        this.showHeaderRow = showHeaderRow;
    }

    public Boolean getShowFooterRow() {
        return showFooterRow;
    }

    public void setShowFooterRow(Boolean showFooterRow) {
        this.showFooterRow = showFooterRow;
    }

    public Boolean getShowTopPanel() {
        return showTopPanel;
    }

    public void setShowTopPanel(Boolean showTopPanel) {
        this.showTopPanel = showTopPanel;
    }

    public Boolean isSyncColumnCellResize() {
        return syncColumnCellResize;
    }

    public void setSyncColumnCellResize(Boolean syncColumnCellResize) {
        this.syncColumnCellResize = syncColumnCellResize;
    }

    public Integer getFrozenColumn() {
        return frozenColumn;
    }

    public void setFrozenColumn(Integer frozenColumn) {
        this.frozenColumn = frozenColumn;
    }

    public Integer getFrozenRow() {
        return frozenRow;
    }

    public void setFrozenRow(Integer frozenRow) {
        this.frozenRow = frozenRow;
    }

    public String getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(Color headerBackgroundColor) {
        this.headerBackgroundColor = String.format("#%06x", headerBackgroundColor.getRgb());
    }

    public void setHeaderBackgroundColor(String headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = String.format("#%06x", backgroundColor.getRgb());
    }

    public String getOddBackgroundColor() {
        return oddBackgroundColor;
    }

    public void setOddBackgroundColor(String oddBackgroundColor) {
        this.oddBackgroundColor = oddBackgroundColor;
    }

    public void setOddBackgroundColor(Color oddBackgroundColor) {
        this.oddBackgroundColor = String.format("#%06x", oddBackgroundColor.getRgb());
    }

    public String getEvenBackgroundColor() {
        return evenBackgroundColor;
    }

    public void setEvenBackgroundColor(String evenBackgroundColor) {
        this.evenBackgroundColor = evenBackgroundColor;
    }

    public void setEvenBackgroundColor(Color evenBackgroundColor) {
        this.evenBackgroundColor = String.format("#%06x", evenBackgroundColor.getRgb());
    }

    public String getSelectHighLightColor() {
        return selectHighLightColor;
    }

    public void setSelectHighLightColor(String selectHighLightColor) {
        this.selectHighLightColor = selectHighLightColor;
    }

    public void setSelectHighLightColor(Color selectHighLightColor) {
        this.selectHighLightColor = String.format("#%06x", selectHighLightColor.getRgb());
    }

}
