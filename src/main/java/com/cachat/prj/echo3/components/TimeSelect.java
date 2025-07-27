package com.cachat.prj.echo3.components;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class TimeSelect extends Component {

    public static final String PROPERTY_TIME_VALUE = "value";

    /**
     * Heure en cours (secondes depuis minuit)
     */
    private int time;

    /**
     * Donne la liste de propriétés
     *
     * @return les propriétés de sortie
     */
    public static final String[] getOutputProperties() {
        return new String[]{
            PROPERTY_TIME_VALUE
        };
    }

    /**
     * Constructeur
     */
    public TimeSelect() {
        this(new Date());
    }

    /**
     * Constructeur
     *
     * @param time l'heure initiale (secondes depuis minuit)
     */
    public TimeSelect(int time) {
        this.time = time;
    }

    /**
     * Constructeur
     *
     * @param date la date dont on va extraire l'heure
     */
    public TimeSelect(Date date) {
        setSelectedTime(date);
    }

    /**
     * fixe l'heure sélectionnée
     *
     * @param time l'heure sélectionnée (secondes depuis minuit)
     */
    public void setSelectedTime(int time) {
        this.time = time;
        int hour = time;
        int sec = hour % 60;
        hour = (hour - sec) / 60;
        int min = hour % 60;
        hour = (hour - min) / 60;
        set(PROPERTY_TIME_VALUE, String.format("%d:%02d", hour, min));
    }

    /**
     * fixe l'heure sélectionnée
     *
     * @param date la date dont on va extraire l'heure
     */
    public final void setSelectedTime(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        setSelectedTime(cal.get(Calendar.HOUR_OF_DAY) * 3600 + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND));
    }

    public int getSelectedTime() {
        return time;
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        switch (inputName) {
            case PROPERTY_TIME_VALUE -> {
                try {
                    String x[] = ((String) inputValue).split(":");
                    time = Integer.parseInt(x[0]) * 3600 + Integer.parseInt(x[1]) * 60;
                } catch (Exception ex) {
                    Logger.getLogger(DateSelect4.class.getName()).log(Level.SEVERE, null, ex);
                }
                fireAction();
            }
        }
    }  // ActionListener //////////////////////////////////////////////////////////
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
