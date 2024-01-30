package org.informagen.echo.app;

import org.informagen.echo.app.ActiveTextField;

import nextapp.echo.app.TextField;

import java.lang.NumberFormatException;

/**
 * 
 */

public class IntegerTextField extends ActiveTextField {

    public static final String PROPERTY_MINIMUM_VALUE = "minimumValue";
    public static final String PROPERTY_MAXIMUM_VALUE = "maximumValue";


    private int minimumValue = Integer.MIN_VALUE;
    private int maximumValue = Integer.MAX_VALUE;


    public IntegerTextField() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerTextField(int minimumValue, int maximumValue) {
        super();
        
        // Ensure min is less than max, otherwise swap them. 
        //  Maybe better to throw illegalArgumentException?
        
        if(minimumValue < maximumValue) {
            setMinimumValue(minimumValue);
            setMaximumValue(maximumValue);
        } else {
            setMinimumValue(maximumValue);
            setMaximumValue(minimumValue);
        }
    }

    public boolean isValid() {
        return !isEmpty(getText()) && isInRange();
    }

    public boolean isInRange() {
        
        int value = getValue();
        int min = getMinimumValue();
        int max = getMaximumValue();
    
        return (min <= value) && (value <= max);
    }


    /**
     * Set the integer value as an int
     * 
     * @param value the int value
    */
       
    public void setValue(int value) {
        setText(Integer.toString(value));
    }


    /**
     * Get the value as an int
     * 
     * @returns int the int value
    */
       
    public int getValue() {
        int value;
        try {
            value = Integer.parseInt(getText());
        } catch (NumberFormatException nfe) {
            value = 0;
        }
        
        return value;
    }

    
    /**
     * Set the minimum allowed value 
     * 
     * @param minValue the minimum value
    */
       
    public void setMinimumValue(int minimumValue) {
        int oldValue = this.minimumValue;
        this.minimumValue = minimumValue;
        firePropertyChange(PROPERTY_MINIMUM_VALUE, oldValue, minimumValue);
    }


    /**
     * Get the minimum value for this integer field
     * 
     * @returns int the minimum value
    */
       
    public int getMinimumValue() {
        return minimumValue;
    }

    
    /**
     * Set the maximum allowed value
     * 
     * @param maxValue the maximum value
    */
       
    public void setMaximumValue(int maximumValue) {
        int oldValue = this.maximumValue;
        this.maximumValue = maximumValue;
        firePropertyChange(PROPERTY_MAXIMUM_VALUE, oldValue, maximumValue);
    }


    /**
     * Get the maximum value for this integer field
     * 
     * @returns int the maximum value
    */
       
    public int getMaximumValue() {
        return maximumValue;
    }


}

