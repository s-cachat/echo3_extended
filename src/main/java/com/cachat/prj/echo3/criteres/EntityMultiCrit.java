package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.models.AbstractRawListModel;
import com.cachat.prj.echo3.models.EntityListModel;
import com.cachat.prj.echo3.ng.ListBoxEx;
import com.cachat.util.BeanTools;
import java.util.ArrayList;
import java.util.List;
import nextapp.echo.app.list.ListSelectionModel;

/**
 * un critere de type choix n/m
 */
public class EntityMultiCrit extends Crit {

    /**
     * le choix
     */
    private final ListBoxEx tf;

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     * @param values les valeurs
     * @param propLib la propriete des valeurs a utiliser comme libelle
     * @param neutre si true, ajoute une valeur neutre
     */
    public EntityMultiCrit(CritContainer cont, String prop, List<? extends Object> values, String propLib, boolean neutre) {
        super(cont, prop);
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        tf = new ListBoxEx(new EntityListModel(values, propLib, neutre));
        tf.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_SELECTION);
        tf.setStyleName("Grid");
        critf.add(tf);
        tf.addActionListener(cont);
        tf.setSelectedIndex(0);
        tf.setToolTipText(cont.getString(prop + ".tt"));
        cont.addCrit(this);
        cont.extendCritAreaHeight(cont.CRIT_HEIGHT * (values.size() + 1) / 4);
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        List<Object> v = getSelected();
        if (v.isEmpty() || v.get(0) == null) {
            return null;
        } else {
            arg.addAll(v);
            StringBuilder sb = new StringBuilder();
            String sep = "(";
            for (int i = 0; i < v.size(); i++) {
                sb.append(sep).append("(").append(prop).append("= ?)");
                sep = " or ";
            }
            sb.append(")");
            return sb.toString();
        }
    }

    /**
     * donne l'element selectionné
     *
     * @return l'objet, ou null si pas de selection
     */
    public List<Object> getSelected() {
        AbstractRawListModel arl = (AbstractRawListModel) tf.getModel();
        ListSelectionModel lsm = tf.getSelectionModel();
        List<Object> res = new ArrayList<>();
        for (int i = 0; i < arl.size(); i++) {
            if (lsm.isSelectedIndex(i)) {
                res.add(arl.getRaw(i));
            }
        }
        return res;
    }

    @Override
    public String getSummary() {
        List<Object> v = getSelected();
        if (v.isEmpty() || v.get(0) == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            String sep = "(";
            String p = cont.getString(prop);
            for (Object v1 : v) {
                String val = BeanTools.get(v1, prop);
                sb.append(sep).append("(").append(p).append(" = ").append(val).append("?)");
                sep = " or ";
            }
            sb.append(")");
            return sb.toString();
        }
    }
}
