package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.models.AbstractRawListModel;
import com.cachat.prj.echo3.models.EntityListModel;
import com.cachat.util.BeanTools;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextapp.echo.app.SelectField;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * un critere de type choix 1/n
 *
 * @author scachat
 * @param <T> le type d'entité
 */
public class EntitySelectCrit<T> extends Crit {

    /**
     * le choix
     */
    private final SelectField tf;
    /**
     * le mode
     */
    private final ModeSelect mode;
    private final EntityListModel<T> elm;
    private final String propLib;
    /**
     * les actionListener
     */
    private final List<ActionListener> listeners = new ArrayList<>();

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     * @param values les valeurs
     * @param propLib la propriete des valeurs a utiliser comme libelle
     * @param mode, en fonction du type du champs
     * @param neutre si true, ajoute une valeur neutre
     */
    public EntitySelectCrit(CritContainer cont, String prop, List<T> values, String propLib, ModeSelect mode,
            boolean neutre) {
        super(cont, prop);
        this.mode = mode;
        this.propLib = propLib;
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        tf = new SelectField(elm = new EntityListModel<>(values, propLib, neutre));
        if (neutre || !values.isEmpty()) {
            tf.setSelectedIndex(0);
        }
        tf.setStyleName("Grid");
        critf.add(tf);
        tf.addActionListener(e -> {
            ActionEvent ae = new ActionEvent(EntitySelectCrit.this, "ENTITY");
            for (ActionListener a : listeners) {
                a.actionPerformed(ae);
            }
        });
        tf.addActionListener(cont);
        tf.setSelectedIndex(0);
        tf.setToolTipText(cont.getString(prop + ".tt"));
        cont.addCrit(this);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);

    }

    public void updateValues(List<T> values) {
        elm.setData(values);
    }

    /**
     * ajoute un actionListener
     *
     * @param a le listener
     */
    public void addActionListener(ActionListener a) {
        listeners.add(a);
    }

    /**
     * ajoute un actionListener
     *
     * @param a le listener
     */
    public void removeActionListener(ActionListener a) {
        listeners.remove(a);
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        Object v = getSelected();
        String q = mode.getQuery();
        if (v != null && q != null) {
            arg.add(v);
            return String.format(q, prop);
        } else {
            return null;
        }
    }

    /**
     * donne l'element selectionné
     *
     * @return l'objet, ou null si pas de selection
     */
    public T getSelected() {
        AbstractRawListModel<T> arl = (AbstractRawListModel<T>) tf.getModel();
        return arl.getRaw(tf.getSelectedIndex());
    }

    /**
     * sélectionne l'élément
     *
     * @param sel l'entité sélectionné
     */
    public void setSelected(T sel) {
        AbstractRawListModel<T> arl = (AbstractRawListModel<T>) tf.getModel();
        for (int i = 0; i < arl.size(); i++) {
            if (Objects.equals(arl.getRaw(i), sel)) {
                tf.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public String getSummary() {
        T v = getSelected();
        String q = mode.getQuery();
        if (v != null && q != null) {
            String val = BeanTools.get(v, propLib);
            q = q.replace("?", val);
            return String.format(q, cont.getString(prop));
        } else {
            return null;
        }
    }

}
