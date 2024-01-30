package com.cachat.prj.echo3.models;

import com.cachat.prj.echo3.base.LocalisedItem;

/**
 * un list model prenant ses valeurs dans une enum
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003-2019 SST Informatique
 * @param <T> Type de l'enumeration
 */
public class EnumListModel<T extends Enum> extends AbstractRawListModel {

    /**
     * contructeur pour surcharge, ne fait aucune initialisation
     */
    protected EnumListModel() {
    }

    /**
     * Constructeur
     *
     * @param values la class de l'enum
     */
    public EnumListModel(Class values) {
        this(values, false);
    }

    /**
     * Constructeur
     *
     * @param values la class de l'enum
     * @param neutre si true, ajoute en tete une valeur neutre (indefinie)
     */
    public EnumListModel(Class values, boolean neutre) {
        this(values, null, neutre);
    }

    /**
     * Constructeur
     *
     * @param values la class de l'enum
     * @param neutre si true, ajoute en tete une valeur neutre (indefinie)
     * @param locItem la fenetre, utilisee pour avoir acces aux ressources texte
     */
    public EnumListModel(Class<T> values, LocalisedItem locItem, boolean neutre) {
        init(values, locItem, neutre, false, "");
    }

    /**
     * Constructeur
     *
     * @param values la class de l'enum
     * @param neutre si true, ajoute en tete une valeur neutre (indefinie)
     * @param locItem la fenetre, utilisee pour avoir acces aux ressources texte
     */
    public EnumListModel(Class<T> enumValue,LocalisedItem locItem, boolean neutre, T... values) {
        init(locItem, neutre, false, "", values);
    }
    /**
     * Constructeur
     *
     * @param values la class de l'enum
     * @param neutre si true, ajoute en tete une valeur neutre (indefinie)
     * @param locItem la fenetre, utilisee pour avoir acces aux ressources texte
     */
    public EnumListModel(LocalisedItem locItem, boolean neutre, T... values) {
        init(locItem, neutre, false, "", values);
    }

    /**
     * Constructeur
     *
     * @param values la class de l'enum
     * @param neutre si true, ajoute en tete une valeur neutre (indefinie)
     * @param locItem la fenetre, utilisee pour avoir acces aux ressources texte
     * @param prefix prefixe a ajouter aux clés pour obtenir les libellés (via
     * LocalisedItem.getBaseString())
     */
    public EnumListModel(Class<T> values, LocalisedItem locItem, boolean neutre, String prefix) {
        init(values, locItem, neutre, true, prefix);
    }

    /**
     * Constructeur
     *
     * @param values la class de l'enum
     * @param neutre si true, ajoute en tete une valeur neutre (indefinie)
     * @param locItem la fenetre, utilisee pour avoir acces aux ressources texte
     * @param useBaseLoc si true, utilise getBaseString de LocalisedItem, sinon
     * utilise getString
     */
    public EnumListModel(Class<T> values, LocalisedItem locItem, boolean neutre, boolean useBaseLoc) {
        init(values, locItem, neutre, useBaseLoc, "");
    }

    private void init(Class<T> values, LocalisedItem locItem, boolean neutre, boolean useBaseLoc, String prefix) {
        init(locItem, neutre, useBaseLoc, prefix, values.getEnumConstants());

    }

    private void init(LocalisedItem locItem, boolean neutre, boolean useBaseLoc, String prefix, T... v) {
        libs = new String[v.length + (neutre ? 1 : 0)];
        if (locItem == null || v.length == 0) {
            for (int i = 0; i < v.length; i++) {
                libs[i] = v[i].toString();
            }
        } else {
            String sn = v.length == 0 ? "" : v[0].getClass().getSimpleName();
            for (int i = 0; i < v.length; i++) {
                libs[i] = v[i] == null ? "" : (useBaseLoc ? locItem.getBaseString(String.format("%s%s.%s", prefix, sn, v[i].toString())) : locItem.getString(String.format("%s.%s", sn, v[i].toString())));
            }
        }
        if (neutre) {
            this.values = new Object[v.length + 1];
            this.values[0] = null;

            for (int i = v.length; i > 0; i--) {
                this.values[i] = v[i - 1];
                libs[i] = libs[i - 1];
            }
            libs[0] = "-";
        } else {
            this.values = v;
        }
    }
    /**
     * les libelles
     */
    protected String[] libs;
    /**
     * les valeurs
     */
    protected Object[] values;

    /**
     * donne une valeur
     *
     * @param i l'index
     * @return la valeur
     */
    @Override
    public Object get(int i) {
        return i < 0 ? "" : libs[i];
    }

    /**
     * donne une valeur brute
     *
     * @param i l'index
     * @return la valeur
     */
    @Override
    public Object getRaw(int i) {
        return i < 0 ? null : values[i];
    }

    /**
     * donne l'index d'une valeur
     *
     * @param o la valeur
     * @return l'index
     */
    public int indexOf(Object o) {
        if (o == null) {
            if (values[0] == null) {
                return 0;
            } else {
                return -1;
            }
        }
        for (int i = 0; i < values.length; i++) {
            if (o.equals(values[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * donne le nombre de valeurs
     *
     * @return le nombre
     */
    @Override
    public int size() {
        return values.length;
    }

    /**
     * donne les valeurs
     *
     * @return les valeurs
     */
    public Object[] values() {
        return values;
    }
}
