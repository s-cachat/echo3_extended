package com.cachat.prj.echo3.components;

import com.cachat.util.DateTimeUtil;
import com.cachat.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 *
 * @author scachat
 */
public class DateSelect4 extends Component {
    // OUTPUT PROPERTIES ///////////////////////////////////////////////////////

    public static final String PROPERTY_DATE_VALUE = "value";
    public static final String PROPERTY_DATE2_VALUE = "value2";
    public static final String PROPERTY_WITH_TIME = "withTime";
    public static final String PROPERTY_WITH_NULL = "withNull";
    public static final String PROPERTY_RANGE = "range";
    public static final String PROPERTY_LOCALE = "locale";
    /**
     * si true, permet la sélection de l'heure
     */
    private final boolean withTime;
    /**
     * si true, permet la sélection d'une valeur nulle
     */
    private boolean withNull;
    /**
     * range si true, passe en mode range (2 dates)
     */
    private boolean range;
    /**
     * Date en cours
     */
    private Date date;
    /**
     * Seconde date en cours (mode range)
     */
    private Date date2;

    /**
     * Format pour l'envoi au client, date et heure
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Donne la liste de propriétés
     *
     * @return les propriétés
     */
    public static final String[] getOutputProperties() {
        return new String[]{
            PROPERTY_DATE_VALUE,
            PROPERTY_DATE2_VALUE,
            PROPERTY_WITH_TIME,
            PROPERTY_WITH_NULL,
            PROPERTY_RANGE,
            PROPERTY_LOCALE
        };
    }

    /**
     * Constructeur
     */
    public DateSelect4() {
        this(new Date());
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     */
    public DateSelect4(Date date) {
        this(date, false);
    }

    /**
     * Constructeur
     *
     * @param withTime si true, permet la sélection de l'heure
     */
    public DateSelect4(boolean withTime) {
        this(new Date(), withTime);
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     */
    public DateSelect4(Date date, boolean withTime) {
        this(date, withTime, false);
    }

    /**
     * Constructeur keep
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public DateSelect4(Date date, boolean withTime, boolean withNull) {
        this(date, withTime, withNull, null);
    }

    /**
     * Constructeur keep
     *
     * @param locale change la langue
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public DateSelect4(Date date, boolean withTime, boolean withNull, Locale locale) {
        super();
        this.withTime = withTime;
        this.withNull = withNull;
        this.range = false;
        setLocale(locale);
        set(PROPERTY_WITH_TIME, withTime);
        set(PROPERTY_WITH_NULL, withNull);
        set(PROPERTY_RANGE, range);
        if (locale != null) {
            set(PROPERTY_LOCALE, locale.getLanguage());
        }
        setSelectedDate(date);
    }

    /**
     * Constructeur en mode range
     *
     * @param range si true, passe en mode range (2 dates)
     * @param locale change la langue
     * @param date la date de début
     * @param date2 la date de fin
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public DateSelect4(Date date, Date date2, boolean withTime, boolean withNull, boolean range, Locale locale) {
        super();
        this.withTime = withTime;
        this.withNull = withNull;
        this.range = true;
        setLocale(locale);
        set(PROPERTY_WITH_TIME, withTime);
        set(PROPERTY_WITH_NULL, withNull);
        set(PROPERTY_RANGE, range);
        if (locale != null) {
            set(PROPERTY_LOCALE, locale.getLanguage());
        }
        setSelectedDate(date);
        setSelectedDate2(date2);
    }

    /**
     * Constructeur par recopie
     *
     * @param dateSelect le modèle
     */
    public DateSelect4(DateSelect4 dateSelect) {
        super();
        this.withTime = dateSelect.withTime;
        this.withNull = dateSelect.withNull;
        this.range = dateSelect.range;
        Locale locale = dateSelect.getLocale();

        set(PROPERTY_WITH_TIME, dateSelect.withTime);
        set(PROPERTY_WITH_NULL, dateSelect.withNull);
        set(PROPERTY_RANGE, dateSelect.range);
        if (locale != null) {
            set(PROPERTY_LOCALE, locale.getLanguage());
            setLocale(locale);
        }
        setSelectedDate(dateSelect.date);
        if (dateSelect.date2 != null) {
            setSelectedDate2(dateSelect.date2);
        }
    }

    /**
     * Donne la date sélectionnée
     *
     * @return La date sélectionnée, ou null si désactivé
     */
    public Date getSelectedDate() {
        return date;
    }

    /**
     * Donne la deuxième date sélectionnée (seulement en mode range)
     *
     * @return La date sélectionnée, ou null si désactivé
     */
    public Date getSelectedDate2() {
        return date2;
    }

    /**
     * Fixe la date
     *
     * @param date la date sélectionnée en format Calendar
     */
    public void setSelectedDate(Calendar date) {
        setSelectedDate(date == null ? (Date) null : date.getTime());
    }

    /**
     * Fixe la date
     *
     * @param date la date sélectionnée en format Date
     */
    public void setSelectedDate(Date date) {
        setSelectedDate(date, true);
    }

    /**
     * Fixe la date
     *
     * @param date la date sélectionnée en format Date
     * @param updateClient si true, envoi une mise à jour au client
     */
    public void setSelectedDate(Date date, boolean updateClient) {
        this.date = date;
        if (this.date != null && !withTime) {
            DateUtil.midnight(this.date);
        }
        if (updateClient) {
            set(PROPERTY_DATE_VALUE, this.date == null ? null : dateFormat.format(this.date));
        }
    }

    /**
     * Fixe la seconde date (en mode range)
     *
     * @param date la date sélectionnée en format Date
     */
    public void setSelectedDate2(Date date) {
        setSelectedDate2(date, true);
    }

    /**
     * Fixe la seconde date (en mode range)
     *
     * @param date la date sélectionnée en format Date
     * @param updateClient si true, envoi une mise à jour au client
     */
    public void setSelectedDate2(Date date, boolean updateClient) {
        this.date = date;
        if (this.date != null && !withTime) {
            DateUtil.midnight(this.date);
        }
        if (updateClient) {
            set(PROPERTY_DATE2_VALUE, this.date == null ? null : dateFormat.format(this.date));
        }
    }

    /**
     * Peut on sélectionner une heure ?
     *
     * @return True si on permet la sélection de l'heure
     */
    public boolean isWithTime() {
        return withTime;
    }

    public boolean isRange() {
        return range;
    }

    public boolean isWithNull() {
        return withNull;
    }

    public void setWithNull(boolean withNull) {
        this.withNull = withNull;
        set(PROPERTY_WITH_NULL, withNull);
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        switch (inputName) {
            case PROPERTY_DATE_VALUE -> {
                try {
                    if (withTime) {
                        setSelectedDate(DateTimeUtil.parse((String) inputValue), false);
                    } else {
                        setSelectedDate(DateUtil.parse((String) inputValue), false);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(DateSelect4.class.getName()).log(Level.SEVERE, null, ex);
                }
                fireAction();
            }

        }
    }

    // ActionListener //////////////////////////////////////////////////////////
    private final List<ActionListener> actionListeners = new ArrayList<>();

    public void addActionListener(ActionListener a) {
        actionListeners.add(a);
    }

    public void removeActionListener(ActionListener a) {
        actionListeners.remove(a);
    }

    private void fireAction() {
        ActionEvent e = new ActionEvent(this, "change");
        actionListeners.stream().forEach((a) -> a.actionPerformed(e));
    }

    public boolean hasActionListeners() {
        return !actionListeners.isEmpty();
    }

}
