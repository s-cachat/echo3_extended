package com.cachat.prj.echo3.components;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.ChangeEvent;
import nextapp.echo.app.event.ChangeListener;

/**
 * Un composant de sélection de date/heure
 *
 * @author scachat
 */
public class DateSelect2 extends Component {

    // OUTPUT PROPERTIES ///////////////////////////////////////////////////////
    public static final String PROPERTY_START_DATE_VALUE = "startDate";
    public static final String PROPERTY_END_DATE_VALUE = "endDate";
    public static final String PROPERTY_MIN_DATE_VALUE = "minDate";
    public static final String PROPERTY_MAX_DATE_VALUE = "maxDate";
    public static final String PROPERTY_MIN_YEAR_VALUE = "minYear";
    public static final String PROPERTY_MAX_YEAR_VALUE = "maxYear";
    public static final String PROPERTY_DATE_LIMIT_VALUE = "dateLimit";
    public static final String PROPERTY_AUTO_APPLY_VALUE = "autoApply";
    public static final String PROPERTY_SINGLE_DATE_VALUE = "singleDatePicker";
    public static final String PROPERTY_SHOW_DROPDOWNS_VALUE = "showDropdowns";
    public static final String PROPERTY_SHOW_WEEK_NUMBERS_VALUE = "showWeekNumbers";
    public static final String PROPERTY_SHOW_ISO_WEEK_NUMBERS_VALUE = "showISOWeekNumbers";
    public static final String PROPERTY_SHOW_CUSTOM_RANGE_LABEL_VALUE = "showCustomRangeLabel";
    public static final String PROPERTY_TIME_PICKER_VALUE = "timePicker";
    public static final String PROPERTY_TIME_PICKER_24H_VALUE = "timePicker24Hour";
    public static final String PROPERTY_TIME_PICKER_INCREMENT_VALUE = "timePickerIncrement";
    public static final String PROPERTY_TIME_PICKER_SECONDS_VALUE = "timePickerSeconds";
    public static final String PROPERTY_LINKED_CALENDARS_VALUE = "linkedCalendars";
    public static final String PROPERTY_AUTO_UPDATE_INPUT_VALUE = "autoUpdateInput";
    public static final String PROPERTY_ALWAYS_SHOW_CALENDARS_VALUE = "alwaysShowCalendars";
    public static final String PROPERTY_RANGES_VALUE = "ranges";
    public static final String PROPERTY_OPENS_VALUE = "opens";
    public static final String PROPERTY_DROPS_VALUE = "drops";
    public static final String PROPERTY_BUTTON_CLASSES_VALUE = "buttonClasses";
    public static final String PROPERTY_APPLY_BUTTON_CLASSES_VALUE = "applyButtonClasses";
    public static final String PROPERTY_CANCEL_BUTTON_CLASSES_VALUE = "cancelButtonClasses";
    public static final String PROPERTY_LOCALE_VALUE = "locale";

    /**
     * Donne la liste de propriétés
     */
    public static final String[] getOutputProperties() {
        return new String[]{
            PROPERTY_START_DATE_VALUE,
            PROPERTY_END_DATE_VALUE,
            PROPERTY_MIN_DATE_VALUE,
            PROPERTY_MAX_DATE_VALUE,
            PROPERTY_MIN_YEAR_VALUE,
            PROPERTY_MAX_YEAR_VALUE,
            PROPERTY_DATE_LIMIT_VALUE,
            PROPERTY_AUTO_APPLY_VALUE,
            PROPERTY_SINGLE_DATE_VALUE,
            PROPERTY_SHOW_DROPDOWNS_VALUE,
            PROPERTY_SHOW_WEEK_NUMBERS_VALUE,
            PROPERTY_SHOW_ISO_WEEK_NUMBERS_VALUE,
            PROPERTY_SHOW_CUSTOM_RANGE_LABEL_VALUE,
            PROPERTY_TIME_PICKER_VALUE,
            PROPERTY_TIME_PICKER_24H_VALUE,
            PROPERTY_TIME_PICKER_INCREMENT_VALUE,
            PROPERTY_TIME_PICKER_SECONDS_VALUE,
            PROPERTY_LINKED_CALENDARS_VALUE,
            PROPERTY_AUTO_UPDATE_INPUT_VALUE,
            PROPERTY_ALWAYS_SHOW_CALENDARS_VALUE,
            PROPERTY_RANGES_VALUE,
            PROPERTY_OPENS_VALUE,
            PROPERTY_DROPS_VALUE,
            PROPERTY_BUTTON_CLASSES_VALUE,
            PROPERTY_APPLY_BUTTON_CLASSES_VALUE,
            PROPERTY_LOCALE_VALUE
        };
    }

    // EVENT LISTENERS & PROPERTIES ////////////////////////////////////////////
    public static final String APPLY_LISTENERS_CHANGED_PROPERTY = "applyListeners";
    public static final String CANCEL_LISTENERS_CHANGED_PROPERTY = "cancelListeners";
    public static final String CLEAR_LISTENERS_CHANGED_PROPERTY = "clearListeners";
    public static final String SHOW_LISTENERS_CHANGED_PROPERTY = "showListeners";
    public static final String HIDE_LISTENERS_CHANGED_PROPERTY = "hideListeners";
    public static final String SHOW_CALENDAR_LISTENERS_CHANGED_PROPERTY = "showCalendarListeners";
    public static final String HIDE_CALENDAR_LISTENERS_CHANGED_PROPERTY = "hideCalendarListeners";
    public static final String ON_APPLY_ACTION = "apply";
    public static final String ON_CANCEL_ACTION = "cancel";
    public static final String ON_CLEAR_ACTION = "clear";
    public static final String ON_SHOW_ACTION = "show";
    public static final String ON_HIDE_ACTION = "hide";
    public static final String ON_SHOW_CALENDAR_ACTION = "showCalendar";
    public static final String ON_HIDE_CALENDAR_ACTION = "hideCalendar";

    /**
     * Nombre de minutes entre chaque step pour le choix de l'heure
     */
    public static final int STEP = 15;

    /**
     * Format pour l'envoi au serveur
     */
    private final SimpleDateFormat jsFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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
     * Constructeur
     */
    public DateSelect2() {
        this(new Date());
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     */
    public DateSelect2(Date date) {
        this(date, false);
    }

    /**
     * Constructeur
     *
     * @param withTime si true, permet la sélection de l'heure
     */
    public DateSelect2(boolean withTime) {
        this(new Date(), withTime);
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     */
    public DateSelect2(Date date, boolean withTime) {
        this(date, withTime, false);
    }

    /**
     * Constructeur keep
     *
     * @param date la date initiale
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public DateSelect2(Date date, boolean withTime, boolean withNull) {
        super();
        this.withTime = withTime;
        this.withNull = withNull;
        set(PROPERTY_TIME_PICKER_VALUE, withTime);
        set(PROPERTY_TIME_PICKER_24H_VALUE, true);
        set(PROPERTY_TIME_PICKER_INCREMENT_VALUE, STEP);
        set(PROPERTY_SINGLE_DATE_VALUE, true);
        set(PROPERTY_AUTO_UPDATE_INPUT_VALUE, true);
        set(PROPERTY_LOCALE_VALUE, buildLocale());
        setSelectedDate(date);
    }

    /**
     * Constructeur keep
     *
     * @param withTime si true, permet la sélection de l'heure
     * @param withNull si true, permet la sélection d'une valeur nulle
     */
    public DateSelect2(boolean withTime, boolean withNull) {
        this(new Date(), withTime, withNull);
    }

    /**
     * Fix le nom de style des boutons
     *
     * @param name Le nom de style des boutons
     */
    public void setButtonStyleName(String name) {
        // TODO
    }

    /**
     * Donne la date sélectionnée
     *
     * @todo Supprimer le ParseException de la signature
     * @return La date sélectionnée, ou null si désactivé
     */
    public Date getSelectedDate() throws ParseException {
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

    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        switch (inputName) {
            case PROPERTY_START_DATE_VALUE ->
                setSelectedDate(parseDate((String) inputValue), false);
            case ON_APPLY_ACTION ->
                fireOnApply();
            case ON_SHOW_ACTION ->
                fireOnShow();
            case ON_HIDE_ACTION ->
                fireOnHide();
            case ON_SHOW_CALENDAR_ACTION ->
                fireOnShowCalendar();
            case ON_HIDE_CALENDAR_ACTION ->
                fireOnHideCalendar();
            case ON_CANCEL_ACTION ->
                fireOnCancel();
            case ON_CLEAR_ACTION ->
                fireOnClear();
            default -> {
                //nop
            }
        }
    }

    /**
     * Construit le JSON pour la configuration du locale
     *
     * @return Le JSON de la configuration du locale
     */
    private String buildLocale() {
        ApplicationInstance app = ApplicationInstance.getActive();
        Locale loc;
        if (app == null) {
            loc = Locale.getDefault();
        } else {
            loc = app.getLocale();
        }

        StringBuilder sb = new StringBuilder();
        // "lang" : code ISO 2-char pour le locale de moment.js
        sb.append("{\"lang\":\"").append(loc.getLanguage()).append("\"");

        // "format" : 
        // voir doc pour moment.js :
        // https://momentjs.com/docs/#/i18n/changing-locale/
        String format;
        if (withTime) {
            format = "L LT";
        } else {
            format = "L";
        }
        sb.append(",\"format\":\"").append(format).append("\"");

        if (app instanceof BaseApp ba) {
            sb.append(",\"applyLabel\":\"").append(ba.getBaseString("ok")).append("\"");
            sb.append(",\"cancelLabel\":\"").append(ba.getBaseString("cancel")).append("\"");
            sb.append(",\"clearLabel\":\"").append(ba.getBaseString("delete")).append("\"");
        }

        sb.append("}");
        return sb.toString();
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

    //<editor-fold desc="EventListeners" defaultstate="collapsed">
    // ChangeListener //////////////////////////////////////////////////////////
    private final List<ChangeListener> changeListeners = new ArrayList<>();

    public void addChangeListener(ChangeListener a) {
        changeListeners.add(a);
    }

    public void removeChangeListener(ChangeListener a) {
        changeListeners.remove(a);
    }

    public boolean hasChangeListeners() {
        return !changeListeners.isEmpty();
    }

    private void fireOnChange() {
        ChangeEvent e = new ChangeEvent(this);
        changeListeners.stream().forEach((a) -> a.stateChanged(e));
    }

    // ActionListener //////////////////////////////////////////////////////////
    public void addActionListener(ActionListener l) {
        addApplyListener(l);
        addCancelListener(l);
        addClearListener(l);
        addShowListener(l);
        addHideListener(l);
        addShowCalendarListener(l);
        addHideCalendarListener(l);
    }

    public void removeActionListener(ActionListener l) {
        removeApplyListener(l);
        removeCancelListener(l);
        removeClearListener(l);
        removeShowListener(l);
        removeHideListener(l);
        removeShowCalendarListener(l);
        removeHideCalendarListener(l);
    }

    // ApplyListener ///////////////////////////////////////////////////////////
    public static interface ApplyListener extends ActionListener {
    }

    public void addApplyListener(ActionListener l) {
        getEventListenerList().addListener(ApplyListener.class, l);
        firePropertyChange(APPLY_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeApplyListener(ActionListener l) {
        getEventListenerList().removeListener(ApplyListener.class, l);
        firePropertyChange(APPLY_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasApplyListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(ApplyListener.class) > 0;
    }

    private void fireOnApply() {
        EventListener[] applyListeners = getEventListenerList().getListeners(ApplyListener.class);
        ActionEvent e = new ActionEvent(this, "APPLY");
        for (int i = 0; i < applyListeners.length; ++i) {
            ((ActionListener) applyListeners[i]).actionPerformed(e);
        }
    }

    // CancelListener //////////////////////////////////////////////////////////
    public static interface CancelListener extends ActionListener {
    }

    public void addCancelListener(ActionListener l) {
        getEventListenerList().addListener(CancelListener.class, l);
        firePropertyChange(CANCEL_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeCancelListener(ActionListener l) {
        getEventListenerList().removeListener(CancelListener.class, l);
        firePropertyChange(CANCEL_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasCancelListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(CancelListener.class) > 0;
    }

    private void fireOnCancel() {
        EventListener[] cancelListeners = getEventListenerList().getListeners(CancelListener.class);
        ActionEvent e = new ActionEvent(this, "CANCEL");
        for (int i = 0; i < cancelListeners.length; ++i) {
            ((ActionListener) cancelListeners[i]).actionPerformed(e);
        }
    }

    // ClearListener ///////////////////////////////////////////////////////////
    public static interface ClearListener extends ActionListener {
    }

    public void addClearListener(ActionListener l) {
        getEventListenerList().addListener(CancelListener.class, l);
        firePropertyChange(CLEAR_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeClearListener(ActionListener l) {
        getEventListenerList().removeListener(ClearListener.class, l);
        firePropertyChange(CLEAR_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasClearListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(ClearListener.class) > 0;
    }

    private void fireOnClear() {
        EventListener[] clearListeners = getEventListenerList().getListeners(ClearListener.class);
        ActionEvent e = new ActionEvent(this, "CLEAR");
        for (int i = 0; i < clearListeners.length; ++i) {
            ((ActionListener) clearListeners[i]).actionPerformed(e);
        }
    }

    // ShowListener ////////////////////////////////////////////////////////////
    public static interface ShowListener extends ActionListener {
    }

    public void addShowListener(ActionListener l) {
        getEventListenerList().addListener(ShowListener.class, l);
        firePropertyChange(SHOW_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeShowListener(ActionListener l) {
        getEventListenerList().removeListener(ShowListener.class, l);
        firePropertyChange(SHOW_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasShowListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(ShowListener.class) > 0;
    }

    private void fireOnShow() {
        EventListener[] showListeners = getEventListenerList().getListeners(ShowListener.class);
        ActionEvent e = new ActionEvent(this, "SHOW");
        for (int i = 0; i < showListeners.length; ++i) {
            ((ActionListener) showListeners[i]).actionPerformed(e);
        }
    }

    // HideListener ////////////////////////////////////////////////////////////
    public static interface HideListener extends ActionListener {
    }

    public void addHideListener(ActionListener l) {
        getEventListenerList().addListener(HideListener.class, l);
        firePropertyChange(HIDE_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeHideListener(ActionListener l) {
        getEventListenerList().removeListener(HideListener.class, l);
        firePropertyChange(HIDE_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasHideListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(HideListener.class) > 0;
    }

    private void fireOnHide() {
        System.err.println("DateSelect2 onHide");
        EventListener[] hideListeners = getEventListenerList().getListeners(HideListener.class);
        ActionEvent e = new ActionEvent(this, "HIDE");
        for (int i = 0; i < hideListeners.length; ++i) {
            ((ActionListener) hideListeners[i]).actionPerformed(e);
        }
    }

    // ShowCalendarListener ////////////////////////////////////////////////////
    public static interface ShowCalendarListener extends ActionListener {
    }

    public void addShowCalendarListener(ActionListener l) {
        getEventListenerList().addListener(ShowCalendarListener.class, l);
        firePropertyChange(SHOW_CALENDAR_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeShowCalendarListener(ActionListener l) {
        getEventListenerList().removeListener(ShowCalendarListener.class, l);
        firePropertyChange(SHOW_CALENDAR_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasShowCalendarListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(ShowCalendarListener.class) > 0;
    }

    private void fireOnShowCalendar() {
        System.err.println("DateSelect2 onShowCalendar");
        EventListener[] showCalendarListeners = getEventListenerList().getListeners(ShowCalendarListener.class);
        ActionEvent e = new ActionEvent(this, "SHOW_CALENDAR");
        for (int i = 0; i < showCalendarListeners.length; ++i) {
            ((ActionListener) showCalendarListeners[i]).actionPerformed(e);
        }
    }

    // HideCalendarListener ////////////////////////////////////////////////////
    public static interface HideCalendarListener extends ActionListener {
    }

    public void addHideCalendarListener(ActionListener l) {
        getEventListenerList().addListener(HideCalendarListener.class, l);
        firePropertyChange(HIDE_CALENDAR_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeHideCalendarListener(ActionListener l) {
        getEventListenerList().removeListener(HideCalendarListener.class, l);
        firePropertyChange(HIDE_CALENDAR_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasHideCalendarListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(HideCalendarListener.class) > 0;
    }

    private void fireOnHideCalendar() {
        System.err.println("DateSelect2 onHideCalendar");
        EventListener[] hideCalendarListeners = getEventListenerList().getListeners(HideCalendarListener.class);
        ActionEvent e = new ActionEvent(this, "HIDE_CALENDAR");
        for (int i = 0; i < hideCalendarListeners.length; ++i) {
            ((ActionListener) hideCalendarListeners[i]).actionPerformed(e);
        }
    }

    //</editor-fold>
    /**
     * affiche un bouton permettant de désactiver la saisie de date (la date
     * sélectionnée est alors null). avec le nouveau widget, cela revient à
     * permettre ou non la saisie de valeur nulle, et a afficher ou faire
     * disparaitre le bouton clean
     *
     * @TODO : changer la visibilité du bouton clear
     * @param visible
     */
    public void setEnableButton(boolean visible) {
        this.withNull = visible;
    }

    /**
     * indique si le bouton clear est visible
     *
     * @TODO : implémenter
     * @return true si il est visible
     */
    public boolean isEnableButton() {
        return true;
    }

    /**
     * déprécié, même retour que isEnableButton
     */
    public final boolean isEnabled2() {
        return isEnableButton();
    }

    /**
     * affiche la popup
     *
     * @TODO implémenter
     */
    public final void popupDate() {

    }

    /**
     * end action
     *
     * @TODO implémenter
     */
    public final void endPopupDate() {

    }

}
