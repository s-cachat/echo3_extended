package com.cachat.prj.echo3.slickgrid;

/**
 * données pour la mise à jour d'une cellule notifie un changement de cellule
 * dont l'origine est l'utilisateur
 *
 * @author scachat
 */
public class CellUpdateData {

    /**
     * ligne
     */
    private int row;
    /**
     * colonne
     */
    private int col;
    /**
     * nom du champs
     */
    private String field;
    /**
     * valeur
     */
    private String value;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
