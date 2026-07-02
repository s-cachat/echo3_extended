package com.cachat.prj.echo3.components;

import com.cachat.util.DateTimeUtil;
import com.cachat.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 *
 * @author scachat
 */
public class InstantSelect4 extends Component {

    // OUTPUT PROPERTIES ///////////////////////////////////////////////////////
    public static final String PROPERTY_DATE_VALUE = "value";
    public static final String PROPERTY_DATE2_VALUE = "value2";
    public static final String PROPERTY_WITH_TIME = "withTime";
    public static final String PROPERTY_WITH_NULL = "withNull";
    public static final String PROPERTY_RANGE = "range";
    public static final String PROPERTY_LOCALE = "locale";
    public static final String PROPERTY_WIDTH = "width";

    /**
     * Si true, permet la sélection de l'heure
     */
    private final boolean withTime;

    /**
     * Si true, permet la sélection d'une valeur nulle
     */
    private boolean withNull;

    /**
     * Range si true, passe en mode range (2 dates)
     */
    private boolean range;

    /**
     * Date en cours
     */
    private Instant date;

    /**
     * Seconde date en cours (mode range)
     */
    private Instant date2;
    /**
     * le fuseau horaire
     */
    private ZoneId zoneId = ZoneId.systemDefault();
    /**
     * Format pour l'envoi au client, date et heure
     */
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(zoneId);

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
            PROPERTY_LOCALE,
            PROPERTY_WIDTH
        };
    }

    /**
     * Constructeur
     */
    public InstantSelect4() {
        this(Instant.now());
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     */
    public InstantSelect4(Instant date) {
        this(date, false);
    }

    /**
     * Constructeur
     *
     * @param withTime si true, permet la sélection de l'heure
     */
    public InstantSelect4(boolean withTime) {
        this(withTime, false);
    }

    /**
     * Constructeur
     *
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public InstantSelect4(boolean withTime, boolean withNull) {
        this(withTime, withNull, null);
    }

    /**
     * Constructeur
     *
     * @param locale change la langue
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public InstantSelect4(boolean withTime, boolean withNull, Locale locale) {
        this(Instant.now(), withTime, withNull, locale);
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     */
    public InstantSelect4(Instant date, boolean withTime) {
        this(date, withTime, false);
    }

    /**
     * Constructeur keep
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public InstantSelect4(Instant date, boolean withTime, boolean withNull) {
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
    public InstantSelect4(Instant date, boolean withTime, boolean withNull, Locale locale) {
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
        InstantSelect4.this.setSelectedInstant(date);
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
    public InstantSelect4(Instant date, Instant date2, boolean withTime, boolean withNull, boolean range, Locale locale) {
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
        InstantSelect4.this.setSelectedInstant(date);
        setSelectedInstant2(date2);
    }

    /**
     * Constructeur par recopie
     *
     * @param dateSelect le modèle
     */
    public InstantSelect4(InstantSelect4 dateSelect) {
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
        InstantSelect4.this.setSelectedInstant(dateSelect.date);
        if (dateSelect.date2 != null) {
            setSelectedInstant2(dateSelect.date2);
        }
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(zoneId);
    }

    /**
     * Donne la date sélectionnée
     *
     * @return La date sélectionnée, ou null si désactivé
     */
    public Instant getSelectedInstant() {
        return date;
    }

    /**
     * Donne la deuxième date sélectionnée (seulement en mode range)
     *
     * @return La date sélectionnée, ou null si désactivé
     */
    public Instant getSelectedInstant2() {
        return date2;
    }

    /**
     * Fixe la date
     *
     * @param date la date sélectionnée en format Calendar
     */
    public void setSelectedInstant(Calendar date) {
        InstantSelect4.this.setSelectedInstant(date == null ? (Instant) null : date.toInstant());
    }

    /**
     * Fixe la date
     *
     * @param date la date sélectionnée en format Date
     */
    public final void setSelectedInstant(Instant date) {
        setSelectedInstant(date, true);
    }

    /**
     * Fixe la date
     *
     * @param date la date sélectionnée en format Date
     * @param updateClient si true, envoi une mise à jour au client
     */
    public void setSelectedInstant(Instant date, boolean updateClient) {
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
    public final void setSelectedInstant2(Instant date) {
        setSelectedDate2(date, true);
    }

    /**
     * Fixe la seconde date (en mode range)
     *
     * @param date la date sélectionnée en format Date
     * @param updateClient si true, envoi une mise à jour au client
     */
    public void setSelectedDate2(Instant date, boolean updateClient) {
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

    /**
     * Est-ce qu'on est en mode range (sélèctionne de deux dates) ?
     *
     * @return True si on est en mode range
     */
    public boolean isRange() {
        return range;
    }

    /**
     * Peut-on avoir une valeur null ?
     *
     * @return True si on permet une valeur null
     */
    public boolean isWithNull() {
        return withNull;
    }

    /**
     * Fixe la propriété withNull
     *
     * @param withNull True si on permet le saisi d'une valeur null
     */
    public void setWithNull(boolean withNull) {
        this.withNull = withNull;
        set(PROPERTY_WITH_NULL, withNull);
    }

    /**
     * Returns the width of the component. This property supports
     * <code>Extent</code>s with fixed or percentile units.
     *
     * @return the width
     */
    public Extent getWidth() {
        return (Extent) get(PROPERTY_WIDTH);
    }

    /**
     * Sets the width of the component. This property supports
     * <code>Extent</code>s with fixed or percentile units.
     *
     * @param newValue the new width
     */
    public void setWidth(Extent newValue) {
        set(PROPERTY_WIDTH, newValue);
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        switch (inputName) {
            case PROPERTY_DATE_VALUE -> {
                try {
                    if (withNull && (inputValue == null || (inputValue instanceof String str && str.isBlank()))) {
                        setSelectedInstant((Instant) null, false);
                    }
                    if (withTime) {
                        setSelectedInstant(DateTimeUtil.parseInstant((String) inputValue), false);
                    } else {
                        setSelectedInstant(DateUtil.parseInstant((String) inputValue), false);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(InstantSelect4.class.getName()).log(Level.SEVERE, null, ex);
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
