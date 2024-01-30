package com.cachat.prj.echo3.models;

import com.cachat.prj.echo3.editor.BasicEditor;
import java.util.ArrayList;
import java.util.List;

/**
 * un list model prenant ses valeurs dans une liste d'objets
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003 SST Informatique
 */
public class EntityListModel<T> extends AbstractRawListModel {

    /**
     * Constructeur
     *
     * @param values les valeurs
     * @param propName le nom de la propriete a utiliser comme libelle
     */
    public EntityListModel(List<T> values, String propName) {
        this(values, propName, false);
    }
    /**
     * avec valeur neutre
     */
    private boolean neutre;

    /**
     * Constructeur
     *
     * @param values les valeurs
     * @param propName le nom de la propriete a utiliser comme libelle
     * @param neutre si true, ajoute une valeur neutre en tete
     */
    public EntityListModel(List<T> values, String propName, boolean neutre) {
        this.neutre = neutre;
        setData(values);
        this.propName = propName;
    }

    /**
     * change la liste de donnee
     */
    public void setData(List<T> values) {
        int s1 = this.values == null ? 0 : this.values.size();
        if (neutre) {
            this.values = new ArrayList<>();
            this.values.add(null);
            values.stream().forEach((o) -> this.values.add(o));
        } else {
            this.values = values;
        }
        fireContentsChanged(0, Math.max(s1, values.size()));
    }
    /**
     * les valeurs
     */
    private List<T> values;
    /**
     * le nom de la propriete
     */
    private String propName;

    /**
     * donne une valeur
     */
    @Override
    public Object get(int i) {
        Object v = i < values.size() ? values.get(i) : null;
        return v == null ? "" : (propName == null ? v.toString() : BasicEditor.buildLib(v, propName));
    }

    /**
     * donne une valeur
     */
    @Override
    public T getRaw(int i) {
        T v = i >= 0 && i < values.size() ? values.get(i) : null;
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
     */
    public int indexOf(T value) {
        return values.indexOf(value);
    }
}
