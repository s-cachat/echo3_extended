package com.cachat.prj.echo3.slickgrid;

import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe doit être surchargée, et avoir des propriétés dont les noms
 * correspondent aux "field" des colonnes.
 *
 * @author scachat
 */
public class SlickGridData {

    /**
     * identifiant de la ligne
     */
    private long id;
    /**
     * les données de la ligne (la clé doit correspondre aux "field" des
     * colonnes).
     */
    private Map<String, Object> data = new HashMap<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        data.put("id", id);
        this.id = id;
    }

    /**
     * ajoute ou modifie une valeur
     *
     * @param field nom du champs
     * @param value valeur
     */
    public final void put(String field, Object value) {
        data.put(field, value);

    }

    /**
     * modification d'une valeur par le client
     *
     * @param field nom du champs
     * @param value valeur
     */
    protected final void _put(String field, Object value) {
        data.put(field, value);
        valueChanged(field, value);
    }

    /**
     * donne une valeur
     *
     * @param field nom du champs
     * @return valeur
     */
    public final Object get(String field) {
        return data.get(field);
    }

    /**
     * callback a surcharger : signale un changement de valeur
     *
     * @param field nom du champs
     * @param value valeur
     */
    public void valueChanged(String field, Object value) {
    }

    /**
     * ne pas appeler directement cette méthode !
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * ne pas appeler directement cette méthode !
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
