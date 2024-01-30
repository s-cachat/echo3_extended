package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.blockeditor.BlockSelect;
import com.cachat.prj.echo3.criteres.Crit;
import com.cachat.prj.echo3.criteres.CritContainer;
import com.cachat.prj.echo3.ng.ButtonGroupEx;
import com.cachat.prj.echo3.ng.Strut;
import com.cachat.prj.echo3.ng.ToggleButtonEx;
import java.util.Arrays;
import java.util.List;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.GridLayoutData;

/**
 * un critere de type choix 1/n pour une enum, sous la forme de boutons
 *
 * @param <T> le type de l'enum
 * @author scachat
 */
public class EnumRadioSelectCrit<T extends Enum> extends Crit {

    /**
     * le groupe classement sélectionné
     */
    private final ButtonGroupEx<T> bgc = new ButtonGroupEx<>();
    /**
     * nombre de colonnes
     */
    private int colSpan = 2;
    /**
     * Conteneur
     */
    private Row va;
    /**
     * préfixe pour les labels
     */
    private String prefix = "";

    /**
     * constructeur
     *
     * @param cont le conteneur des critères
     * @param prop le nom de la propriete critere
     * @param values les valeurs (la classe de base de l'enum
     * @param neutre si true, ajoute une valeur neutre
     */
    public EnumRadioSelectCrit(CritContainer cont, String prop, Class<T> values, boolean neutre) {
        super(cont, prop);
        this.prefix = values.getSimpleName();
        init(cont, prop, neutre ? BlockSelect.addNull(Arrays.asList(values.getEnumConstants())) : Arrays.asList(values.getEnumConstants()));
    }

    /**
     * constructeur
     *
     * @param cont le conteneur des critères
     * @param prop le nom de la propriete critere
     * @param values les valeurs (la classe de base de l'enum
     * @param neutre si true, ajoute une valeur neutre
     */
    public EnumRadioSelectCrit(CritContainer cont, String prop, boolean neutre, T... values) {
        super(cont, prop);
        init(cont, prop, neutre ? BlockSelect.addNull(Arrays.asList(values)) : Arrays.asList(values));
    }

    /**
     * fixe la valeur
     *
     * @param val la valeur
     */
    public void setValue(T val) {
        this.bgc.setSelected(val);
    }

    /**
     * donne la valeur sélectionnée
     *
     * @return la valeur
     */
    public T getValue() {
        return this.bgc.getSelected();
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        T v = this.bgc.getSelected();
        if (v != null) {
            arg.add(v);
            return String.format("%s=?", prop);
        } else {
            return null;
        }
    }

    @Override
    public String getSummary() {
        T v = this.bgc.getSelected();
        if (v != null) {
            return String.format("%s=%s", cont.getString(prop), cont.getString(prefix + "." + v.toString()));
        } else {
            return null;
        }
    }

    /**
     * initialiseur
     *
     * @param cont le conteneur des critères
     * @param prop le nom de la propriete
     * @param values la liste des valeurs à prendre en compte
     */
    private void init(CritContainer cont, String prop, List<T> values) {
        this.prop = prop;
        va  = new Row();
        va.setLayoutData(new GridLayoutData(colSpan - 1, 1));
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        critf.add(va);
        cont.addCrit(this);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
        _setValues(values);

    }

    private void _setValues(List<T> values) {
        va.removeAll();
        ActionListener al = e -> cont.actionPerformed(new ActionEvent(this, String.valueOf(this.bgc.getSelected())));
        values.forEach(gc -> {
            ToggleButtonEx b = null;
            if (gc == null) {
                b = new ToggleButtonEx(cont.getString("all"));

            } else {
                b = new ToggleButtonEx(cont.getString(prefix + "." + gc.toString()));
            }
            bgc.addButton(b, gc);
            va.add(b);
            b.addActionListener(al);
            va.add(new Strut(10, 10));
            b.setSelected(gc == null);
        });
    }
}
