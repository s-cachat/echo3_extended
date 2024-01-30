package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.components.DateSelect2;
import com.cachat.prj.echo3.ng.Strut;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import nextapp.echo.app.CheckBox;
import nextapp.echo.app.Row;
import nextapp.echo.app.SelectField;

/**
 * un critere de type choix de date
 */
public class DateHourCrit extends Crit {

    /**
     * le format
     */
    private static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    /**
     * la date
     */
    private DateSelect2 df;
    /**
     * le champs heure
     */
    private SelectField hf;
    private CheckBox hEnableButton;

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     */
    public DateHourCrit(CritContainer cont, String prop) {
        this(cont, prop, prop);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     * @param libKey la cle pour le libelle
     */
    public DateHourCrit(CritContainer cont, String prop, String libKey) {
        this(cont, prop, prop, libKey);
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
     */
    public DateHourCrit(CritContainer cont, String propDeb, String propFin, String libKey) {
        this(cont, propDeb, propFin, libKey, false);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param propDeb le nom de la propriete critere "debut"
     * @param propFin le nom de la propriete critere "fin"
     * @param libKey la cle pour le libelle
     * @param canBeDisabled si le critère peut être désactivé
     */
    public DateHourCrit(CritContainer cont, String propDeb, String propFin, String libKey, boolean canBeDisabled) {
        this(cont, propDeb, propFin, libKey, canBeDisabled, false);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param propDeb le nom de la propriete critere "debut"
     * @param propFin le nom de la propriete critere "fin"
     * @param libKey la cle pour le libelle
     * @param canBeDisabled si le critère peut être désactivé
     * @param canDisableTime si l'heure peut être désactivée
     */
    public DateHourCrit(CritContainer cont, String propDeb, String propFin, String libKey, boolean canBeDisabled, boolean canDisableTime) {
        super(cont, propDeb);
        this.propFin = propFin;
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));

        Row x = new Row();
        df = new DateSelect2();
        df.setStyleName("Button");
        df.setEnableButton(canBeDisabled);
        x.add(df);
        if (canDisableTime) {
            hEnableButton = new CheckBox();
            hEnableButton.setSelected(true);
            x.add(hEnableButton);
            hEnableButton.addActionListener(e -> cont.actionPerformed(e));
        }
        String[] items = new String[24];
        for (int i = 0; i < items.length; i++) {
            items[i] = String.format("%02d:00-%02d:00", i, i + 1);
        }
        hf = new SelectField(items);
        x.add(new Strut(5, 5));
        x.add(hf);
        GregorianCalendar cal = new GregorianCalendar();
        df.setSelectedDate(cal);
        hf.setSelectedIndex(cal.get(Calendar.HOUR_OF_DAY));

        critf.add(x);

        cont.addCrit(this);
        df.addActionListener(cont);
        hf.addActionListener(cont);
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
        try {
            Date date = df.getSelectedDate();
            if (date == null) {
                return null;
            }
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            java.util.Date min;
            java.util.Date max;
            if (hEnableButton != null && !hEnableButton.isSelected()) {
                cal.set(Calendar.HOUR_OF_DAY, 0);
                min = cal.getTime();
                cal.add(Calendar.DATE, 1);
                max = cal.getTime();
            } else {
                cal.set(Calendar.HOUR_OF_DAY, hf.getSelectedIndex());
                min = cal.getTime();
                cal.add(Calendar.HOUR_OF_DAY, 1);
                max = cal.getTime();
            }
            arg.add(max);
            arg.add(min);
            return String.format("%1$s<? and %2$s>?", prop, propFin);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    @Override
    public String getSummary() {
        try {
            Date date = df.getSelectedDate();
            if (date == null) {
                return null;
            }
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, hf.getSelectedIndex());
            java.util.Date min = cal.getTime();
            cal.add(Calendar.HOUR_OF_DAY, 1);
            java.util.Date max = cal.getTime();
            return String.format("%1$s < %3$s %5$s %2$s >= %4$s", cont.getString(prop), cont.getString(propFin), format.format(max), format.format(min), cont.getBaseString("and"));
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }
}
