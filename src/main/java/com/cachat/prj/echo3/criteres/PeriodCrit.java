package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.base.DateSelect;
import com.cachat.prj.echo3.components.DateSelect3;
import java.text.SimpleDateFormat;
import java.util.*;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * un critere de type choix de date
 *
 * @author scachat
 */
public class PeriodCrit extends Crit implements ActionListener {

    /**
     * le format
     */
    private static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    
    /**
     * la date de debut
     */
    private DateSelect3 dfDebut;
    /**
     * la date de fin
     */
    private DateSelect3 dfFin;
    /**
     * longueur maxi de la période, en jours (-1 = illimité)
     */
    private final int maxLen;
    /**
     * les actionListener
     */
    private final List<ActionListener> listeners = new ArrayList<>();

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     * @param maxLen longueur maxi de la période, en jours (-1 = illimité)
     */
    public PeriodCrit(CritContainer cont, String prop, int maxLen) {
        this(cont, prop, prop, maxLen);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     * @param libKey la cle pour le libelle
     * @param maxLen longueur maxi de la période, en jours (-1 = illimité)
     */
    public PeriodCrit(CritContainer cont, String prop, String libKey, int maxLen) {
        this(cont, prop, prop, libKey, maxLen);
    }
    /**
     * la propriete de fin
     */
    private String propFin;

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param propDeb le nom de la propriete critere "debut"
     * @param propFin le nom de la propriete critere "fin"
     * @param libKey la cle pour le libelle
     * @param maxLen longueur maxi de la période, en jours (-1 = illimité)
     */
    public PeriodCrit(CritContainer cont, String propDeb, String propFin, String libKey, int maxLen) {
        super(cont, propDeb);
        this.propFin = propFin;
        this.maxLen = maxLen;
        critf.add(newLabel(cont.getString(libKey), cont.getString(libKey + ".tt")));
        Row row = new Row();
        dfDebut = new DateSelect3(false);
        dfDebut.setStyleName("Button");
        dfDebut.addActionListener(this);
        row.add(dfDebut);
        row.add(new Label("=>"));
        dfFin = new DateSelect3(false);
        dfFin.setStyleName("Button");
        dfFin.addActionListener(this);
        row.add(dfFin);
        Calendar now = new GregorianCalendar();
        now.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        dfDebut.setSelectedDate(now.getTime());
        now.add(Calendar.DATE, 1);
        dfFin.setSelectedDate(now.getTime());
        critf.add(row);
        cont.addCrit(this);
        addActionListener(cont);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
    }

    /**
     * donne la date de début sélectionnée
     *
     * @return la date de début
     */
    public Date getDebut() {
        return dfDebut.getSelectedDate();
    }

    /**
     * donne la date de fin sélectionnée
     *
     * @return la date de fin
     */
    public Date getFin() {
        return dfFin.getSelectedDate();
    }

    public void setDebut(Date d) {
        dfDebut.setSelectedDate(d);
    }

    public void setFin(Date d) {
        dfFin.setSelectedDate(d);
    }

    /**
     * traite l'action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (dfDebut.getSelectedDate() != null && dfFin.getSelectedDate() != null) {
            long debut = dfDebut.getSelectedDate().getTime();
            long fin = dfFin.getSelectedDate().getTime();
            if (fin - debut < 86400000l) {//min 1 jour entre debut et fin
                dfFin.setSelectedDate(new Date(debut + 86400000l));
            } else if (maxLen > 0 && (fin - debut) > 86400000l * maxLen) {
                dfFin.setSelectedDate(new Date(debut + 86400000l * maxLen));
            }
        }
        ActionEvent ae = new ActionEvent(this, "DATE");
        for (ActionListener a : listeners) {
            a.actionPerformed(ae);
        }
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
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        java.util.Date min = null;
        if (dfDebut.getSelectedDate() != null) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dfDebut.getSelectedDate());
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            min = cal.getTime();
        }
        java.util.Date max = null;
        if (dfFin.getSelectedDate() != null) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dfFin.getSelectedDate());
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            max = cal.getTime();
        }
        if (min != null) {
            if (max != null) {
                arg.add(max);
                arg.add(min);
                return String.format("%1$s<? and %2$s>?", prop, propFin);
            } else {
                arg.add(min);
                return String.format("%1$s>?", propFin);
            }
        } else if (max != null) {
            arg.add(max);
            return String.format("%1$s<?", prop);
        } else {
            return null;
        }
    }

    @Override
    public String getSummary() {
        java.util.Date min = null;
        if (dfDebut.getSelectedDate() != null) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dfDebut.getSelectedDate());
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            min = cal.getTime();
        }
        java.util.Date max = null;
        if (dfFin.getSelectedDate() != null) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dfFin.getSelectedDate());
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            max = cal.getTime();
        }
        String p = cont.getString(prop);
        String pFin = cont.getString(propFin);
        if (min != null) {
            if (max != null) {
                return String.format("%1$s %2$s %3$s => %4$s", p, cont.getBaseString("entre"), format.format(min), format.format(max));
            } else {
                return String.format("%1$s > %2$s", pFin, format.format(min));
            }
        } else if (max != null) {
            return String.format("%1$s < %2$s", p, format.format(max));
        } else {
            return null;
        }
    }
}
