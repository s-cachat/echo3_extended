package org.informagen.echo.app;

/**
 * 
 */

public class NumericTextField extends ActiveTextField {

    // For now limit to the integer range but as doubles
    private static final double MIN_VALUE = Integer.valueOf(Integer.MIN_VALUE).doubleValue();
    private static final double MAX_VALUE = Integer.valueOf(Integer.MAX_VALUE).doubleValue();
    

    public static final String PROPERTY_MINIMUM_VALUE = IntegerTextField.PROPERTY_MINIMUM_VALUE;
    public static final String PROPERTY_MAXIMUM_VALUE = IntegerTextField.PROPERTY_MAXIMUM_VALUE;
    // public static final String PROPERTY_MINIMUM_VALUE = "minDouble";
    // public static final String PROPERTY_MAXIMUM_VALUE = "maxDouble";

    private double minimumValue = NumericTextField.MIN_VALUE;
    private double maximumValue = NumericTextField.MAX_VALUE;


    public NumericTextField() {
        this(NumericTextField.MIN_VALUE, NumericTextField.MAX_VALUE);
    }

    public NumericTextField(double minimumValue, double maximumValue) {
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
        
        double value = getValue();
        double min = getMinimumValue();
        double max = getMaximumValue();
    
        return (min <= value) && (value <= max);
    }


    /**
     * Set the integer value as an int
     * 
     * @param value the int value
    */
       
    public void setValue(double value) {
        setText(Double.toString(value));
    }


    /**
     * Get the value as a double
     * 
     * @returns int the int value
    */
       
    public double getValue() {
        double value;
        try {
            value = Double.parseDouble(getText());
        } catch (NumberFormatException nfe) {
            value = 0.0;
        }
        
        return value;
    }

    
    /**
     * Set the minimum allowed value 
     * 
     * @param minimumValue the minimum value
    */
       
    public void setMinimumValue(double minimumValue) {
        double oldValue = this.minimumValue;
        this.minimumValue = minimumValue;
        firePropertyChange(PROPERTY_MINIMUM_VALUE, oldValue, minimumValue);
    }


    /**
     * Get the minimum allowed value
     * 
     * @returns int the minimum value
    */
       
    public double getMinimumValue() {
        return minimumValue;
    }

    
    /**
     * Set the maximum allowed value
     * 
     * @param maximumValue the maximum value
    */
       
    public void setMaximumValue(double maximumValue) {
        double oldValue = this.maximumValue;
        this.maximumValue = maximumValue;
        firePropertyChange(PROPERTY_MAXIMUM_VALUE, oldValue, maximumValue);
    }


    /**
     * Get the maximum allowed value
     * 
     * @returns int the maximum value
    */
       
    public double getMaximumValue() {
        return maximumValue;
    }


}

