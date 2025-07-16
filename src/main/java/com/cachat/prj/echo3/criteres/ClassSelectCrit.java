package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.models.AbstractRawListModel;
import com.cachat.prj.echo3.models.StringListModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextapp.echo.app.SelectField;

/**
 * un critere de type choix de sous type
 *
 * @author scachat
 */
public class ClassSelectCrit extends Crit {

    /**
     * le choix
     */
    private SelectField tf;
    /**
     * map nom de valeur vers valeur
     */
    private Map<String, Class> rawValues = new HashMap<>();

    /**
     * constructeur
     *
     * @param cont le conteneur réfèrant
     * @param prop l'alias de la table (qui sera utilisé dans la fonction lpql type()) 
     * @param values les valeurs
     * @param neutre si true, ajoute une valeur neutre
     */
    public ClassSelectCrit(CritContainer cont, String prop, boolean neutre, Class... values) {
        super(cont, prop);
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        List<String> vals = new ArrayList<>();
        List<String> libs = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            rawValues.put(values[i].getSimpleName(), values[i]);
            vals.add(values[i].getSimpleName());
            libs.add(cont.getString(values[i].getSimpleName()));
        }
        tf = new SelectField(new StringListModel(vals, libs, neutre));
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
        if (v != null) {
            if (prop.contains("DTYPE")) {//old style, depends on hibernate implementation
                return String.format("%s=%s", prop, v);
            } else {
                return String.format("type(%s)=%s", prop, v);
            }
        } else {
            return null;
        }
    }

    public Class getSelectedValue() {
        AbstractRawListModel arl = (AbstractRawListModel) tf.getModel();
        return rawValues.get((String) arl.getRaw(tf.getSelectedIndex()));
    }

    @Override
    public String getSummary() {
        AbstractRawListModel arl = (AbstractRawListModel) tf.getModel();
        Object v = arl.getRaw(tf.getSelectedIndex());
        if (v != null) {
            return String.format("%s = %s", cont.getString(prop), v);
        } else {
            return null;
        }
    }
}
