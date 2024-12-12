package com.cachat.prj.echo3.models;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * un list model prenant ses valeurs dans une liste d'objets
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003 SST Informatique
 */
public class MapListModel<T> extends AbstractRawListModel {

    /**
     * Constructeur
     *
     * @param values les valeurs
     */
    public MapListModel(LinkedHashMap<T, String> values) {
        this(values, false);
    }
    /**
     * avec valeur neutre
     */
    private boolean neutre;

    /**
     * Constructeur
     *
     * @param values les valeurs
     * @param neutre si true, ajoute une valeur neutre en tete
     */
    public MapListModel(LinkedHashMap<T, String> values, boolean neutre) {
        this.neutre = neutre;
        setData(values);
    }

    /**
     * change la liste de donnee
     *
     * @param values la nouvelle liste d'objets vers leur libellés
     */
    public void setData(LinkedHashMap<T, String> values) {
        int s1 = this.values == null ? 0 : this.values.size();
        if (neutre) {
            this.keys.add(null);
            this.values.add("");
        }
        for (Map.Entry<T, String> o : values.entrySet()) {
            this.values.add(o.getValue());
            this.keys.add(o.getKey());
        }

        fireContentsChanged(0, Math.max(s1, values.size()));
    }

    /**
     * change la liste de donnee
     *
     * @param values la nouvelle liste de libellés vers l'objet correspondant
     */
    public void setDataReverse(LinkedHashMap<String, T> values) {
        int s1 = this.values == null ? 0 : this.values.size();
        if (neutre) {
            this.keys.add(null);
            this.values.add("");
        }
        for (Map.Entry<String, T> o : values.entrySet()) {
            this.values.add(o.getKey());
            this.keys.add(o.getValue());
        }

        fireContentsChanged(0, Math.max(s1, values.size()));
    }
    /**
     * les valeurs
     */
    private List<String> values = new ArrayList<>();
    /**
     * les clés
     */
    private List<T> keys = new ArrayList<>();

    /**
     * donne une valeur (libelle)
     *
     * @param i l'index
     */
    @Override
    public Object get(int i) {
        Object v = i < values.size() ? values.get(i) : null;
        return v == null ? "" : v;
    }

    /**
     * donne une valeur (valeur brute)
     *
     * @param i l'index
     */
    @Override
    public T getRaw(int i) {
        T v = i >= 0 && i < keys.size() ? keys.get(i) : null;
        return v;
    }

    /**
     * donne le nombre de valeurs
     */
    @Override
    public int size() {
        return values.size();
    }

    /**
     * donne l'index d'une valeur
     *
     * @param value la valeur
     */
    public int indexOf(T value) {
        return values.indexOf(value);
    }
}
