package com.cachat.prj.echo3.criteres;

import com.cachat.util.BeanTools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextapp.echo.app.Button;
import nextapp.echo.app.CheckBox;
import nextapp.echo.app.Grid;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.ChangeEvent;
import nextapp.echo.app.event.ChangeListener;

/**
 * un critere de type choix n/m
 * @param <T> le type d'entité
 */
public class EntityMultiBoxCrit<T> extends Crit {

    /**
     * les choix
     */
    private final Map<CheckBox, T> tfs = new HashMap<>();
    /**
     * le conteneur des choix
     */
    private final Grid tf;
    /**
     * le conteneur
     */
    private final CritContainer cont;

    /**
     * drapeau pour éviter un appel multiple lors du click sur all/none
     */
    private boolean allNoneInProgress = false;
    private final List<T> values;
    private final String propLib;
    private final ChangeListener relay;
    private final Button none;
    private final Button all;

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     * @param values les valeurs
     * @param propLib la propriete des valeurs a utiliser comme libelle
     */
    public EntityMultiBoxCrit(CritContainer cont, String prop, List<T> values, String propLib) {
        this(cont, prop, values, propLib, Collections.EMPTY_LIST);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     * @param values les valeurs
     * @param propLib la propriete des valeurs a utiliser comme libelle
     * @param preselected les objets qui doivent être sélectionnés initialement.
     */
    public EntityMultiBoxCrit(CritContainer cont, String prop, List<T> values, String propLib, List<Object> preselected) {
        super(cont, prop);
        this.cont = cont;
        this.values = new ArrayList<>(values);
        this.propLib = propLib;
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        tf = new Grid(8);
        critf.add(tf);
        relay = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!allNoneInProgress) {
                    EntityMultiBoxCrit.this.cont.actionPerformed(new ActionEvent(e.getSource(), "select"));
                }
            }
        };

        EntityMultiBoxCrit.this.cont.actionPerformed(new ActionEvent(this, "select"));
        all = new Button(cont.getBaseString("all"));
        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (CheckBox cb : tfs.keySet()) {
                    allNoneInProgress = true;
                    cb.setSelected(true);
                    allNoneInProgress = false;
                    EntityMultiBoxCrit.this.cont.actionPerformed(new ActionEvent(e.getSource(), "select"));
                }
            }
        });
        all.setStyleName("Button");

        none = new Button(cont.getBaseString("none"));
        none.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (CheckBox cb : tfs.keySet()) {
                    allNoneInProgress = true;
                    cb.setSelected(false);
                    allNoneInProgress = false;
                    EntityMultiBoxCrit.this.cont.actionPerformed(new ActionEvent(e.getSource(), "select"));
                }
            }
        });
        none.setStyleName("Button");
        cont.addCrit(this);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT + 27 * ((2 + values.size()) / tf.getSize()));
        updateCheckBoxes();
        for (Map.Entry<CheckBox, T> x : tfs.entrySet()) {
            if (preselected.contains(x.getValue())) {
                x.getKey().setSelected(true);
            }
        }
    }

    public void updateValues(List<T> values) {
        this.values.clear();
        this.values.addAll(values);
        updateCheckBoxes();
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        List<T> v = getSelected();
        if (v.isEmpty() || v.get(0) == null) {
            return null;
        } else {
            arg.addAll(v);
            StringBuilder sb = new StringBuilder();
            String sep = "(";
            for (Object v1 : v) {
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
    public List<T> getSelected() {
        List<T> res = new ArrayList<>();
        for (Map.Entry<CheckBox, T> x : tfs.entrySet()) {
            if (x.getKey().isSelected()) {
                res.add(x.getValue());
            }
        }
        return res;
    }

    private void updateCheckBoxes() {
        allNoneInProgress = true;
        tfs.clear();
        tf.removeAll();
        for (T o : values) {
            CheckBox tfe;
            tfe = new CheckBox(BeanTools.get(o, propLib));
            tfe.setStyleName("Grid");
            tf.add(tfe);
            tfe.addChangeListener(relay);
            tfe.setToolTipText(cont.getString(prop + ".tt"));
            tfs.put(tfe, o);
        }
        tf.add(all);
        tf.add(none);

        allNoneInProgress = false;
    }

    @Override
    public String getSummary() {
        List<T> v = getSelected();
        if (v.isEmpty() || v.get(0) == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            String sep = "(";
            String p = cont.getString(prop);
            for (Object v1 : v) {
                String val = BeanTools.get(v1, propLib);
                sb.append(sep).append("(").append(p).append(" = ").append(val).append(")");
                sep = " or ";
            }
            sb.append(")");
            return sb.toString();
        }
    }
}
