package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.components.DateSelect3;
import com.cachat.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * un critere de type choix de date
 *
 * @author scachat
 */
public class DateCrit extends Crit {

    /**
     * le format
     */
    private static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * la date
     */
    private DateSelect3 df;
    /**
     * les actionListener
     */
    private final List<ActionListener> listeners = new ArrayList<>();

    /**
     * constructeur
     *
     * @param cont le container des critères
     * @param prop le nom de la propriete critere
     */
    public DateCrit(CritContainer cont, String prop) {
        this(cont, prop, prop);
    }

    /**
     * constructeur
     *
     * @param cont le container des critères
     * @param prop le nom de la propriete critere
     * @param libKey la cle pour le libelle
     */
    public DateCrit(CritContainer cont, String prop, String libKey) {
        this(cont, prop, prop, libKey);
    }
    /**
     * la propriete de fin
     */
    private String propFin;

    /**
     * constructeur
     *
     * @param cont le conteneur réfèrant
     * @param propDeb le nom de la propriete critere "debut"
     * @param propFin le nom de la propriete critere "fin"
     * @param libKey la cle pour le libelle
     */
    public DateCrit(CritContainer cont, String propDeb, String propFin, String libKey) {
        this(cont, propDeb, propFin, libKey, false);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur réfèrant
     * @param propDeb le nom de la propriete critere "debut"
     * @param propFin le nom de la propriete critere "fin"
     * @param libKey la cle pour le libelle
     * @param canBeDisabled si true, une case a cocher permet d'inhiber le
     * critere
     */
    public DateCrit(CritContainer cont, String propDeb, String propFin, String libKey, boolean canBeDisabled) {
        super(cont, propDeb);
        this.propFin = propFin;
        Label l = newLabel(cont.getString(libKey), cont.getString(prop + ".tt"));
        if (canBeDisabled) {
            Row r = new Row();
            r.add(l);
            critf.add(r);
        } else {
            critf.add(l);
        }

        df = new DateSelect3();
        df.setStyleName("Button");
        critf.add(df);
        cont.addCrit(this);
        df.addActionListener(cont);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
        df.addActionListener(e -> {
            ActionEvent ae = new ActionEvent(DateCrit.this, "DATE");
            for (ActionListener a : listeners) {
                a.actionPerformed(ae);
            }
        });
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
            Date date = df.getSelectedDate();
            if (date == null) {
                return null;
            }
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            DateUtil.midnight(cal);
            java.util.Date min = cal.getTime();
            cal.add(Calendar.DATE, 1);
            java.util.Date max = cal.getTime();
            arg.add(max);
            arg.add(min);
            return String.format("%1$s<? and %2$s>=?", prop, propFin);


    }

    /**
     * donne la date sélectionnée
     *
     * @return la date sélectionnée
     * @throws java.text.ParseException en cas d'erreur de formatage
     */
    public Date getDate() throws ParseException {
        return df.getSelectedDate();
    }

    @Override
    public String getSummary() {
            Date date = df.getSelectedDate();
            if (date == null) {
                return null;
            }
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            DateUtil.midnight(cal);
            java.util.Date min = cal.getTime();
            cal.add(Calendar.DATE, 1);
            java.util.Date max = cal.getTime();
            return String.format("%1$s < %3$s %5$s %2$s >= %4$s", cont.getString(prop), cont.getString(propFin), format.format(max), format.format(min), cont.getBaseString("and"));

    }
}
