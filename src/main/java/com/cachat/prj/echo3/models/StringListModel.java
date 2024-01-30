package com.cachat.prj.echo3.models;

import java.util.*;

/**
 * un list model prenant ses valeurs dans une liste d'objets
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003 SST Informatique
 */
public class StringListModel extends AbstractRawListModel {

    /**
     * Constructeur
     *
     * @param values les valeurs
     * @param propName le nom de la propriete a utiliser comme libelle
     */
    public StringListModel(List<String> values) {
        this(values, false);
    }

    /**
     * Constructeur
     *
     * @param values les valeurs (et libelles)
     * @param propName le nom de la propriete a utiliser comme libelle
     * @param neutre si true, ajoute une valeur neutre en tete
     */
    public StringListModel(List<String> values, boolean neutre) {
        this(values, values, neutre);
    }

    /**
     * Constructeur
     *
     * @param values les valeurs
     * @param libs les libelles
     * @param propName le nom de la propriete a utiliser comme libelle
     * @param neutre si true, ajoute une valeur neutre en tete
     */
    public StringListModel(List<String> values, List<String> libs, boolean neutre) {
        if (neutre) {
            this.values = new ArrayList<>();
            this.libs = new ArrayList<>();
            this.values.add(null);
            this.libs.add("");
            for (String o : values) {
                this.values.add(o);
            }
            for (String o : libs) {
                this.libs.add(o);
            }
        } else {
            this.values = values;
            this.libs = libs;
        }
        this.propName = propName;
    }
    /**
     * les libelles
     */
    private List<String> libs;
    /**
     * les valeurs
     */
    private List<String> values;
    /**
     * le nom de la propriete
     */
    private String propName;

    /**
     * donne une valeur
     */
    public Object get(int i) {
        Object v = i >= 0 && i < libs.size() ? libs.get(i) : "";
        return v == null ? "" : v;
    }

    /**
     * donne une valeur
     */
    @Override
    public Object getRaw(int i) {
        Object v = i >= 0 && i < values.size() ? values.get(i) : null;
        return v;
    }

    /**
     * donne le nombre de valeurs
     */
    public int size() {
        return values.size();
    }
}
