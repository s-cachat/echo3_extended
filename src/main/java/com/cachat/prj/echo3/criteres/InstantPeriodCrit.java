package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.base.DateSelect;
import com.cachat.prj.echo3.components.DateSelect3;
import com.cachat.prj.echo3.components.InstantSelect4;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
public class InstantPeriodCrit extends Crit implements ActionListener {

    /**
     * le format
     */
    private static final DateTimeFormatter format
            = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    .withZone(ZoneId.systemDefault());

    /**
     * la date de debut
     */
    private InstantSelect4 dfField;
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
    public InstantPeriodCrit(CritContainer cont, String prop, int maxLen) {
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
    public InstantPeriodCrit(CritContainer cont, String prop, String libKey, int maxLen) {
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
    public InstantPeriodCrit(CritContainer cont, String propDeb, String propFin, String libKey, int maxLen) {
        super(cont, propDeb);
        this.propFin = propFin;
        this.maxLen = maxLen;
        critf.add(newLabel(cont.getString(libKey), cont.getString(libKey + ".tt")));
        dfField = new InstantSelect4(Instant.now().minus(Duration.ofDays(Math.min(-1, -maxLen))), Instant.now(), false, false, true, Locale.getDefault());
        dfField.addActionListener(this);
        critf.add(dfField);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
    }

    /**
     * donne la date de début sélectionnée
     *
     * @return la date de début
     */
    public Instant getDebut() {
        return dfField.getSelectedInstant();
    }

    /**
     * donne la date de fin sélectionnée
     *
     * @return la date de fin
     */
    public Instant getFin() {
        return dfField.getSelectedInstant2();
    }

    public void setDebut(Instant d) {
        dfField.setSelectedInstant(d);
    }

    public void setFin(Instant d) {
        dfField.setSelectedInstant2(d);
    }

    /**
     * traite l'action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (dfField.getSelectedInstant() != null && dfField.getSelectedInstant2() != null) {
            Instant debut = dfField.getSelectedInstant();
            Instant fin = dfField.getSelectedInstant2();
            final long days = debut.until(fin).toDays();
            if (days < 1) {
                fin = debut.plus(Duration.ofDays(1));
                dfField.setSelectedDate2(fin, true);
            } else if (days > maxLen) {
                fin = debut.plus(Duration.ofDays(maxLen));
                dfField.setSelectedDate2(fin, true);
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
        Instant min = dfField.getSelectedInstant();
        Instant max = dfField.getSelectedInstant2();
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
        Instant min = dfField.getSelectedInstant();
        Instant max = dfField.getSelectedInstant2();

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
