package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.ng.RadioButtonEx;
import java.util.List;
import nextapp.echo.app.Row;
import nextapp.echo.app.button.ButtonGroup;

/**
 * un critere de type boolean
 *
 * @author scachat
 */
public class BooleanCrit extends Crit {

    /**
     * le bouton pour 'ne pas prendre en compte'
     */
    protected final RadioButtonEx rbDC;
    /**
     * le bouton pour 'doit etre vrai'
     */
    protected final RadioButtonEx rbT;
    /**
     * le bouton pour 'doit etre faux'
     */
    protected final RadioButtonEx rbF;

    /**
     * constructeur
     *
     * @param cont le conteneur de critères
     * @param prop le nom de la propriete critere
     */
    public BooleanCrit(CritContainer cont, String prop) {
        this(cont, prop, prop, null);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur de critères
     * @param prop le nom de la propriete critere
     */
    public BooleanCrit(CritContainer cont, String prop, Boolean def) {
        this(cont, prop, prop, def);
    }

    /**
     * change l'état (tri state)
     *
     * @param selected le nouvel état
     */
    public void setSelected(Boolean selected) {
        if (selected != null) {
            if (selected) {
                rbT.setSelected(true);
            } else {
                rbF.setSelected(true);
            }
        } else {
            rbDC.setSelected(true);
        }
    }

    /**
     * teste si le critère est sélectionné
     *
     * @return true si sélectionné, false si non sélectionné, null si indéci
     */
    public Boolean getSelected() {
        if (rbT.isSelected()) {
            return true;
        } else if (rbF.isSelected()) {
            return false;
        } else {
            return null;
        }
    }

    /**
     * constructeur
     *
     * @param cont le conteneur de critères
     * @param prop le nom de la propriete critere
     * @param propKey la cle pour le libelle
     * @param def le choix par defaut (true, false ou null)
     */
    public BooleanCrit(CritContainer cont, String prop, String propKey, Boolean def) {
        super(cont, prop);
        critf.add(newLabel(cont.getString(propKey), cont.getString(propKey + ".tt")));
        Row br = new Row();
        ButtonGroup bg = new ButtonGroup();
        rbDC = new RadioButtonEx(cont.getBaseString("BooleanCrit.DONTCARE"));
        rbDC.setGroup(bg);
        br.add(rbDC);
        rbT = new RadioButtonEx(cont.getBaseString("BooleanCrit.TRUE"));
        rbT.setGroup(bg);
        br.add(rbT);
        rbF = new RadioButtonEx(cont.getBaseString("BooleanCrit.FALSE"));
        rbF.setGroup(bg);
        br.add(rbF);
        critf.add(br);
        rbDC.addActionListener(cont);
        rbT.addActionListener(cont);
        rbF.addActionListener(cont);
        if (def != null) {
            if (def) {
                rbT.setSelected(true);
            } else {
                rbF.setSelected(true);
            }
        } else {
            rbDC.setSelected(true);
        }

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
        if (rbT.isSelected()) {
            return String.format("%s=true", prop);
        } else if (rbF.isSelected()) {
            return String.format("%s=false", prop);
        } else {
            //nop
            return null;
        }
    }

    @Override
    public String getSummary() {
        if (rbT.isSelected()) {
            return String.format("%s = %s", cont.getString(prop), cont.getBaseString("true"));
        } else if (rbF.isSelected()) {
            return String.format("%s = %s", cont.getString(prop), cont.getBaseString("false"));
        } else {
            return null;
        }
    }

}
