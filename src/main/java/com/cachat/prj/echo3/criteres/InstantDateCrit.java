package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.components.DateSelect3;
import com.cachat.prj.echo3.components.InstantSelect3;
import com.cachat.util.DateUtil;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * un critere de type choix de date
 *
 * @author scachat
 * @deprecated
 */
@Deprecated(since = "5.0")
public class InstantDateCrit extends Crit {

    /**
     * la timezone
     */
    private ZoneId zoneId = ZoneId.systemDefault();
    /**
     * la propriete de fin
     */
    private String propFin;
    /**
     * le format
     */
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(zoneId);

    /**
     * la date
     */
    private InstantSelect3 df;
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
    public InstantDateCrit(CritContainer cont, String prop) {
        this(cont, prop, prop);
    }

    /**
     * constructeur
     *
     * @param cont le container des critères
     * @param prop le nom de la propriete critere
     * @param libKey la cle pour le libelle
     */
    public InstantDateCrit(CritContainer cont, String prop, String libKey) {
        this(cont, prop, prop, libKey);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur réfèrant
     * @param propDeb le nom de la propriete critere "debut"
     * @param propFin le nom de la propriete critere "fin"
     * @param libKey la cle pour le libelle
     */
    public InstantDateCrit(CritContainer cont, String propDeb, String propFin, String libKey) {
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
    public InstantDateCrit(CritContainer cont, String propDeb, String propFin, String libKey, boolean canBeDisabled) {
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

        df = new InstantSelect3();
        df.setStyleName("Button");
        critf.add(df);
        cont.addCrit(this);
        df.addActionListener(cont);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
        df.addActionListener(e -> {
            ActionEvent ae = new ActionEvent(InstantDateCrit.this, "DATE");
            for (ActionListener a : listeners) {
                a.actionPerformed(ae);
            }
        });
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(zoneId);
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
        Instant date = df.getSelectedInstant();
        if (date == null) {
            return null;
        }
        
        Instant min=DateUtil.midnight(date);
        Instant max=min.plus(Duration.ofHours(24));
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
    public Instant getDate() throws ParseException {
        return df.getSelectedInstant();
    }

    @Override
    public String getSummary() {
        Instant date = df.getSelectedInstant();
        if (date == null) {
            return null;
        }
        Instant min = DateUtil.midnight(date);
        Instant max = min.plus(Duration.ofHours(24));
        return String.format("%1$s < %3$s %5$s %2$s >= %4$s", cont.getString(prop), cont.getString(propFin),
                format.format(max),
                format.format(min),
                cont.getBaseString("and"));

    }
}
