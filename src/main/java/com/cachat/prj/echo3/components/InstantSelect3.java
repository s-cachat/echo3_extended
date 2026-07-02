package com.cachat.prj.echo3.components;

import com.cachat.util.DateTimeUtil;
import com.cachat.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 *
 * @author scachat
 */
public class InstantSelect3 extends Component {
    // OUTPUT PROPERTIES ///////////////////////////////////////////////////////

    public static final String PROPERTY_DATE_VALUE = "value";
    public static final String PROPERTY_WITH_TIME = "withTime";
    public static final String PROPERTY_WITH_NULL = "withNull";
    public static final String PROPERTY_STEP = "step";
    /**
     * si true, permet la sélection de l'heure
     */
    private final boolean withTime;
    /**
     * si true, permet la sélection d'une valeur nulle
     */
    private boolean withNull;
    /**
     * Date en cours
     */
    private Instant date;
    /**
     * la timezone
     */
    private ZoneId zoneId = ZoneId.systemDefault();
    /**
     * Format pour l'envoi au client, date et heure
     */
    private DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(zoneId);
    /**
     * Format pour l'envoi au client, date seule
     */
    private DateTimeFormatter dFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(zoneId);

    /**
     * Donne la liste de propriétés
     */
    public static final String[] getOutputProperties() {
        return new String[]{
            PROPERTY_DATE_VALUE,
            PROPERTY_WITH_TIME,
            PROPERTY_WITH_NULL
        };
    }

    /**
     * Constructeur
     */
    public InstantSelect3() {
        this(Instant.now());
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     */
    public InstantSelect3(Instant date) {
        this(date, false);
    }

    /**
     * Constructeur
     *
     * @param withTime si true, permet la sélection de l'heure
     */
    public InstantSelect3(boolean withTime) {
        this(Instant.now(), withTime);
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     */
    public InstantSelect3(Instant date, boolean withTime) {
        this(date, withTime, false);
    }

    /**
     * Constructeur keep
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public InstantSelect3(Instant date, boolean withTime, boolean withNull) {
        super();
        this.withTime = withTime;
        this.withNull = withNull;
        set(PROPERTY_WITH_TIME, withTime);
        set(PROPERTY_WITH_NULL, withNull);
        setSelectedDate(date);
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
        dtFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(zoneId);
        dFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(zoneId);
    }

    /**
     * Donne la date sélectionnée
     *
     * @todo Supprimer le ParseException de la signature
     * @return La date sélectionnée, ou null si désactivé
     */
    public Instant getSelectedInstant() {
        return date;
    }

    /**
     * Fixe la date
     *
     * @param date la date sélectionnée en format Calendar
     */
    public void setSelectedDate(Calendar date) {
        setSelectedDate(date == null ? (Instant) null : date.toInstant());
    }

    /**
     * Fixe la date
     *
     * @param date la date sélectionnée en format Date
     */
    public void setSelectedDate(Instant date) {
        setSelectedDate(date, true);
    }

    /**
     * Fixe la date
     *
     * @param date la date sélectionnée en format Date
     * @param updateClient si true, envoi une mise à jour au client
     */
    public void setSelectedDate(Instant date, boolean updateClient) {
        this.date = date;
        if (this.date != null && !withTime) {
            DateUtil.midnight(this.date);
        }
        if (updateClient) {
            set(PROPERTY_DATE_VALUE, this.date == null ? null : (withTime ? dtFormat : dFormat).format(this.date));
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

    public boolean isWithNull() {
        return withNull;
    }

    public void setWithNull(boolean withNull) {
        this.withNull = withNull;
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        switch (inputName) {
            case PROPERTY_DATE_VALUE -> {
                try {
                    if (withTime) {
                        setSelectedDate(DateTimeUtil.parseInstant((String) inputValue));
                    } else {
                        setSelectedDate(DateUtil.parseInstant((String) inputValue));
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(InstantSelect3.class.getName()).log(Level.SEVERE, null, ex);
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
