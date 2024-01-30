package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.models.AbstractRawListModel;
import com.cachat.prj.echo3.models.EnumListModel;
import java.util.List;
import nextapp.echo.app.SelectField;

/**
 * un critere de type choix 1/n
 *
 * @author scachat
 * @param <T> Type de l'enumeration
 */
public class EnumSelectCrit<T extends Enum> extends Crit {

    /**
     * le choix
     */
    private SelectField tf;
    /**
     * le mode
     */
    private ModeSelect mode;

    /**
     * constructeur
     *
     * @param cont le conteneur des critères
     * @param prop le nom de la propriete critere
     * @param values les valeurs (la classe de base de l'enum
     * @param mode, en fonction du type du champs
     * @param neutre si true, ajoute une valeur neutre
     */
    public EnumSelectCrit(CritContainer cont, String prop, Class<T> values, ModeSelect mode, boolean neutre) {
        super(cont, prop);
        init(mode, prop, new EnumListModel(values, cont, neutre));
    }

    /**
     * constructeur
     *
     * @param cont le conteneur des critères
     * @param prop le nom de la propriete critere
     * @param values les valeurs (la classe de base de l'enum
     * @param mode, en fonction du type du champs
     * @param neutre si true, ajoute une valeur neutre
     */
    public EnumSelectCrit(CritContainer cont, String prop, ModeSelect mode, boolean neutre, T... values) {
        super(cont, prop);
        init(mode, prop, new EnumListModel(cont, neutre, values));
    }

    private void init(ModeSelect mode, String prop, EnumListModel enumListModel) {
        this.mode = mode;
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        tf = new SelectField(enumListModel);
        tf.setStyleName("Grid");
        tf.setSelectedIndex(0);
        critf.add(tf);
        tf.addActionListener(cont);
        tf.setToolTipText(cont.getString(prop + ".tt"));
        cont.addCrit(this);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);

    }

    /**
     * fixe la valeur
     *
     * @param val la valeur
     */
    public void setValue(T val) {
        Object[] values = ((EnumListModel) tf.getModel()).values();
        for (int i = 0; i < values.length; i++) {
            if (val.equals(values[i])) {
                tf.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * donne la valeur sélectionnée
     *
     * @return la valeur
     */
    public T getValue() {
        Object[] values = ((EnumListModel) tf.getModel()).values();
        int s = tf.getSelectedIndex();
        return (T) (s < 0 ? null : values[s]);
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        AbstractRawListModel arl = (AbstractRawListModel) tf.getModel();
        Object v = arl.getRaw(tf.getSelectedIndex());
        if (v != null) {
            arg.add(v);
            return String.format(mode.getQuery(), prop);
        } else {
            return null;
        }
    }

    @Override
    public String getSummary() {
        T v = getValue();
        String q = mode.getQuery();
        if (v != null && q != null) {
            q = q.replace("?", v.toString());
            return String.format(q, cont.getString(prop));
        } else {
            return null;
        }
    }
}
