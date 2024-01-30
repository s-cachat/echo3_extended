package com.cachat.prj.echo3.components;

import java.util.EventListener;
import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;

/**
 * Un composant UI qui couplé avec un élement <input type="range">
 *
 * @author scachat
 */
public class Slider extends Component {

    public static final String PROPERTY_MIN_VALUE = "min";
    public static final String PROPERTY_MAX_VALUE = "max";
    public static final String PROPERTY_STEP_VALUE = "step";
    public static final String PROPERTY_VALUE = "value";
    public static final String PROPERTY_ENABLE_INPUT_EVENT = "enableInputEvent";
    public static final String CHANGE_LISTENERS_CHANGED_PROPERTY = "changeListeners";
    public static final String INPUT_LISTENERS_CHANGED_PROPERTY = "inputListeners";
    public static final String ON_CHANGE_ACTION = "change";
    public static final String ON_INPUT_ACTION = "input";
    /**
     * valeur courante
     */
    private Integer value;
    /**
     * si true, on recoit les evenement au fur et a mesure du déplacement
     */
    private boolean enableInputEvent;

    /**
     * Constructeur
     */
    public Slider() {
        this(false);
    }

    /**
     * Constructeur
     *
     * @param enableInputEvent si true, on recoit les evenement au fur et a
     * mesure du déplacement
     */
    public Slider(boolean enableInputEvent) {
        this.enableInputEvent = enableInputEvent;
        set(PROPERTY_ENABLE_INPUT_EVENT, enableInputEvent);
    }

    public final void setMinValue(Integer newValue) {
        set(PROPERTY_MIN_VALUE, newValue);
    }

    public Integer getMinValue() {
        return (Integer) get(PROPERTY_MIN_VALUE);
    }

    public final void setMaxValue(Integer newValue) {
        set(PROPERTY_MAX_VALUE, newValue);
    }

    public Integer getMaxValue() {
        return (Integer) get(PROPERTY_MAX_VALUE);
    }

    public final void setStepValue(Integer newValue) {
        set(PROPERTY_STEP_VALUE, newValue);
    }

    public Integer getStepValue() {
        return (Integer) get(PROPERTY_STEP_VALUE);
    }

    public boolean isEnableInputEvent() {
        return enableInputEvent;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
        set(PROPERTY_VALUE, value);
    }

    public void addChangeListener(SliderChangeListener l) {
        getEventListenerList().addListener(SliderChangeListener.class, l);
        firePropertyChange(CHANGE_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeChangeListener(SliderChangeListener l) {
        getEventListenerList().removeListener(SliderChangeListener.class, l);
        firePropertyChange(CHANGE_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasChangeListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(SliderChangeListener.class) > 0;
    }

    private void fireOnChange() {
        EventListener[] changeListeners = getEventListenerList().getListeners(SliderChangeListener.class);
        ActionEvent e = new ActionEvent(this, null);
        for (int i = 0; i < changeListeners.length; ++i) {
            ((SliderChangeListener) changeListeners[i]).onChange(e);
        }
    }

    public void addInputListener(SliderInputListener l) {
        getEventListenerList().addListener(SliderInputListener.class, l);
        firePropertyChange(INPUT_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeInputListener(SliderInputListener l) {
        getEventListenerList().removeListener(SliderInputListener.class, l);
        firePropertyChange(INPUT_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasInputListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(SliderInputListener.class) > 0;
    }

    private void fireOnInput() {
        EventListener[] inputListeners = getEventListenerList().getListeners(SliderInputListener.class);
        ActionEvent e = new ActionEvent(this, null);
        for (int i = 0; i < inputListeners.length; ++i) {
            ((SliderInputListener) inputListeners[i]).onInput(e);
        }
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        switch (inputName) {
            case PROPERTY_VALUE:
                this.value = (((Number) inputValue).intValue());
                break;
            case ON_CHANGE_ACTION:
                fireOnChange();
                break;
            case ON_INPUT_ACTION:
                fireOnInput();
                break;
            default:
        }
    }

}
