package com.cachat.prj.echo3.slickgrid;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contient les données pour la mise à jour d'une cellule
 *
 * @author scachat
 */
@XmlRootElement
public class SlickDataUpdate implements Serializable{

    /**
     * identifiant de la ligne
     */
    private long rowId;
    /**
     * identifiant de la colonne
     */
    private String colName;
    /**
     * nouvelle valeur
     */
    private Object value;

    public SlickDataUpdate() {
    }

    public SlickDataUpdate(long rowId, String colName, Object value) {
        this.rowId = rowId;
        this.colName = colName;
        this.value = value;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.rowId ^ (this.rowId >>> 32));
        hash = 41 * hash + Objects.hashCode(this.colName);
        hash = 41 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SlickDataUpdate other = (SlickDataUpdate) obj;
        if (this.rowId != other.rowId) {
            return false;
        }
        if (!Objects.equals(this.colName, other.colName)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

}
