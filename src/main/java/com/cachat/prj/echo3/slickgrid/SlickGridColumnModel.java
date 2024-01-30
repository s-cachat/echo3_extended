package com.cachat.prj.echo3.slickgrid;

/**
 * le modèle de colonne
 *
 * @author scachat
 */
public class SlickGridColumnModel {

    public SlickGridColumnModel() {
    }

    public SlickGridColumnModel(long id, String name, String field, SlickFormatter formatter, SlickEditor editor) {
        setId(field);//String.format("c%d",id));
        setName(name);
        setField(field);
        setFormatter(formatter);
        setEditor(editor);
    }

    /**
     * A unique identifier for the column within the grid.
     */
    String id;
    /**
     * The text to display on the column heading.
     */
    String name;
    String toolTip;
    Integer width;
    Integer minWidth;
    Integer maxWidth;
    String cssClass;
    String field;
    SlickFormatter formatter;
    String formatterName;
    SlickEditor editor;
    String editorName;
    Boolean focusable;
    Boolean resizable = true;
    Boolean selectable = true;
    Boolean sortable = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setFormatter(SlickFormatter formatter) {
        this.formatter = formatter;
        this.formatterName = formatter == null ? null : formatter.getFormatterName();
    }

    public String getFormatterName() {
        return formatterName;
    }

    public void setFormatterName(String formatterName) {
        this.formatterName = formatterName;
    }

    public void setEditor(SlickEditor editor) {
        this.editor = editor;
        this.editorName = editor == null ? null : editor.getEditorName();
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public Boolean getFocusable() {
        return focusable;
    }

    public void setFocusable(Boolean focusable) {
        this.focusable = focusable;
    }

    public Boolean getResizable() {
        return resizable;
    }

    public void setResizable(Boolean resizable) {
        this.resizable = resizable;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

}
