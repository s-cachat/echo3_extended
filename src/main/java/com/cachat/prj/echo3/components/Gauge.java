package com.cachat.prj.echo3.components;

import nextapp.echo.app.Component;

/**
 * un cadran d'indication
 * @author scachat
 */
public class Gauge extends Component {
    
    public static final String PROPERTY_MIN_VALUE = "minValue";
    public static final String PROPERTY_MAX_VALUE = "maxValue";
    public static final String PROPERTY_VALUE = "value";

    /**
     * Donne la liste de propriétés
     *
     * @return les propriétés de sortie
     */
    public static final String[] getOutputProperties() {
        return new String[]{
            PROPERTY_MIN_VALUE,
            PROPERTY_MAX_VALUE,
            PROPERTY_VALUE
        };
    }

    /**
     * Constructeur
     */
    public Gauge() {
        this(0, 0, 100);
    }

    /**
     * Constructeur
     *
     * @param minValue la valeur minimale
     * @param value la valeur courante
     * @param maxValue la valeur maximale
     */
    public Gauge(double minValue, double value, double maxValue) {
        setMinValue(minValue);
        setMaxValue(maxValue);
        setValue(value);
    }
    
    public void setMinValue(double newValue) {
        set(PROPERTY_MIN_VALUE, newValue);
    }

    public double getMinValue(double newValue) {
        return (Double) get(PROPERTY_MIN_VALUE);
    }
    
    public void setMaxValue(double newValue) {
        set(PROPERTY_MAX_VALUE, newValue);
    }

    public double getMaxValue(double newValue) {
        return (Double) get(PROPERTY_MAX_VALUE);
    }
    
    public void setValue(double newValue) {
        set(PROPERTY_VALUE, newValue);
    }

    public double getValue(double newValue) {
        return (Double) get(PROPERTY_VALUE);
    }
}
