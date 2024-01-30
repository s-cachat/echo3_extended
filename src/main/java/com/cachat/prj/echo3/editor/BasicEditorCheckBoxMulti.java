package com.cachat.prj.echo3.editor;

import com.cachat.prj.echo3.ng.CheckBoxEx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.CheckBox;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * une liste de choix multiple basee sur un ensemble de checkbox
 */
public class BasicEditorCheckBoxMulti extends Grid implements ActionListener {

    /**
     * Logger local
     */
    protected static final Logger logger = Logger.getLogger(BasicEditor.class.getName());
    /**
     * les composants de base
     */
    private List<Component> lst = new ArrayList<>();
    /**
     * la map checkbox vers objet
     */
    private Map<CheckBox, Object> items = new HashMap<>();
    /**
     * la liste a renseigner
     */
    private List<Object> data;
    BasicEditor editor;

    public BasicEditorCheckBoxMulti(List<? extends Object> items, String propLib,
            BasicEditor editor, int cols) {
        super(cols);
        this.editor = editor;
        for (Object o : items) {
            CheckBox cb = new CheckBoxEx(BasicEditor.buildLib(o, propLib));
            this.items.put(cb, o);
            cb.addActionListener(this);
            add(cb);
        }
    }

    public BasicEditorCheckBoxMulti(Class items,
            BasicEditor editor) {
        this.editor = editor;
        for (Object o : items.getEnumConstants()) {
            CheckBox cb = new CheckBoxEx(editor.getString(o.toString()));
            this.items.put(cb, o);
            cb.addActionListener(this);
            add(cb);
        }
    }

    /**
     * fixe la liste des valeurs
     */
    public void setData(List<Object> data) {
        this.data = data;
        logger.log(Level.FINE, "CheckBoxMulti.setData {0}", (data == null ? null : data.size()));
        if (data != null) {
            for (Map.Entry<CheckBox, Object> x : items.entrySet()) {
                x.getKey().setSelected(data.contains(x.getValue()));
            }
            for (Object x : data) {
                logger.log(Level.FINE, "setData : {0}", x);
            }
        }
    }

    /**
     * donne la liste des valeurs
     */
    public List<Object> getData() {
        return data;
    }

    /**
     * active/desactive
     */
    @Override
    public void setEnabled(boolean enabled) {
        Component[] c = getComponents();
        logger.log(Level.FINE, "CheckBoxMulti.setEnabled {0} / {1}", new Object[]{enabled, c.length});
        for (int i = 0; i < c.length; i++) {
            c[i].setEnabled(enabled);
        }
    }

    /**
     * changement
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        logger.log(Level.SEVERE, "CheckBoxMulti.actionPerformed {0}", e);
        CheckBox cb = (CheckBox) e.getSource();
        Object x = items.get(cb);
        if (x == null) {
            logger.log(Level.SEVERE, "Pas d''objet pour :{0}", e);
        } else if (data != null) {
            if (cb.isSelected()) {
                logger.log(Level.SEVERE, "add {0}", x);
                data.add(x);
            } else {
                logger.log(Level.SEVERE, "remove {0}", x);
                data.remove(x);
            }
        } else {
            logger.severe("Data is null");
        }
    }

    /**
     * sélectionne toutes les entités
     */
    public void checkAll() {
        for (Map.Entry<CheckBox, Object> x : items.entrySet()) {
            if (!data.contains(x.getValue())) {
                data.add(x.getValue());
            }
            x.getKey().setSelected(true);
        }
    }

    /**
     * désélectionne toutes les entités
     */
    public void uncheckAll() {
        for (Map.Entry<CheckBox, Object> x : items.entrySet()) {
            data.remove(x.getValue());
            x.getKey().setSelected(false);
        }
    }

    /**
     * selectione les entité demandées
     *
     * @param selected les entités à sélectionner
     * @param append si true, ajoute à la sélection en cours, sinon
     * déselectionne les entités absentes de selected.
     */
    public void checkTheses(List<? extends Object> selected, boolean append) {
        for (Map.Entry<CheckBox, Object> x : items.entrySet()) {
            if (selected.contains(x.getValue())) {
                if (!data.contains(x.getValue())) {
                    data.add(x.getValue());
                }
                x.getKey().setSelected(true);
            } else if (!append) {
                data.remove(x.getValue());
                x.getKey().setSelected(false);
            }
        }
    }
}
