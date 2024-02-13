package com.cachat.prj.echo3.components;

import com.cachat.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 *
 * @author scachat
 */
public class DateSelect3 extends Component {
    // OUTPUT PROPERTIES ///////////////////////////////////////////////////////

    public static final String PROPERTY_START_DATE_VALUE = "startDate";
    public static final String PROPERTY_END_DATE_VALUE = "endDate";
    public static final String PROPERTY_WITH_TIME = "withTime";
    public static final String PROPERTY_WITH_NULL = "withNull";
    public static final int PROPERTY_STEP = 15;
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
    private Date date;

    /**
     * Format pour l'envoi au serveur
     */
    private final SimpleDateFormat jsFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Donne la liste de propriétés
     */
    public static final String[] getOutputProperties() {
        return new String[]{
            PROPERTY_START_DATE_VALUE,
            PROPERTY_END_DATE_VALUE,
            PROPERTY_WITH_TIME,
            PROPERTY_WITH_NULL
        };
    }

    /**
     * Constructeur
     */
    public DateSelect3() {
        this(new Date());
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     */
    public DateSelect3(Date date) {
        this(date, false);
    }

    /**
     * Constructeur
     *
     * @param withTime si true, permet la sélection de l'heure
     */
    public DateSelect3(boolean withTime) {
        this(new Date(), withTime);
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     */
    public DateSelect3(Date date, boolean withTime) {
        this(date, withTime, false);
    }

    /**
     * Constructeur keep
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public DateSelect3(Date date, boolean withTime, boolean withNull) {
        super();
        this.withTime = withTime;
        this.withNull = withNull;
        set(PROPERTY_WITH_TIME, withTime);
        set(PROPERTY_WITH_NULL, withNull);
        setSelectedDate(date);
    }

    /**
     * Donne la date sélectionnée
     *
     * @todo Supprimer le ParseException de la signature
     * @return La date sélectionnée, ou null si désactivé
     */
    public Date getSelectedDate()  {
        return date;
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
            set(PROPERTY_START_DATE_VALUE, this.date == null ? null : jsFormat.format(this.date));
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
            case PROPERTY_START_DATE_VALUE -> {
                setSelectedDate(parseDate((String) inputValue), false);
                fireAction();
            }
        }
    }

    private Date parseDate(String input) {
        try {
            String val;
            if (input == null) {
                return null;
            } else if (input.contains("+")) {
                // on supprime le fuseau horaire
                val = input.substring(0, input.indexOf("+"));
            } else {
                val = input;
            }

            return jsFormat.parse(val);
        } catch (ParseException ex) {
            return null;
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

}
