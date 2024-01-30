package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.models.AbstractRawListModel;
import com.cachat.prj.echo3.models.MapListModel;
import java.util.LinkedHashMap;
import java.util.List;
import nextapp.echo.app.SelectField;

/**
 * un critere de type choix 1/n
 *
 * @author scachat
 */
public class MapSelectCrit extends Crit {

    /**
     * le choix
     */
    private final SelectField tf;
    /**
     * le mode
     */
    private final ModeSelect mode;
    /**
     * la map cle valeur
     */
    private LinkedHashMap data;

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     * @param values les valeurs sous la forme d'une map cle libelle
     * @param propLib la propriete des valeurs a utiliser comme libelle
     * @param mode, en fonction du type du champs
     * @param neutre si true, ajoute une valeur neutre
     */
    public MapSelectCrit(CritContainer cont, String prop, LinkedHashMap<? extends Object, ? extends Object> values, String propLib,
            ModeSelect mode,
            boolean neutre) {
        super(cont, prop);
        this.mode = mode;
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        tf = new SelectField(new MapListModel(values, neutre));
        tf.setStyleName("Grid");
        critf.add(tf);
        tf.addActionListener(cont);
        tf.setSelectedIndex(0);
        tf.setToolTipText(cont.getString(prop + ".tt"));
        cont.addCrit(this);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
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
        String q = mode.getQuery();
        if (v != null && q != null) {
            arg.add(v);
            return String.format(q, prop);
        } else {
            return null;
        }
    }

    @Override
    public String getSummary() {
        String v = (String) ((MapListModel) tf.getModel()).get(tf.getSelectedIndex());
        String q = mode.getQuery();
        if (v != null && q != null) {
            q = q.replace("?", v);
            return String.format(q, cont.getString(prop));
        } else {
            return null;
        }
    }
}
