package com.cachat.prj.echo3.slickgrid;

/**
 * notifie un changement de cellule dont l'origine est le serveur
 * @author scachat
 */
public class CellChange {

    private int row;
    private String field;
    private Object value;

    public CellChange() {
    }
 
    public CellChange(int row, String field, Object value) {
        this.row = row;
        this.field = field;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

}
