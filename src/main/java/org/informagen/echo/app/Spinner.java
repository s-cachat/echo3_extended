package org.informagen.echo.app;

import nextapp.echo.app.Component;

public class Spinner extends Component {
    
    public static final String PROPERTY_VALUE = "value";
    

    private int value;

    public Spinner() {
        this(0);
    }

    public Spinner(int initialValue) {
        setValue(initialValue);
    }
    
    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        int oldValue = value;
        value = newValue;
        firePropertyChange(PROPERTY_VALUE, oldValue, newValue);
    }

    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
       
        if (PROPERTY_VALUE.equals(inputName))
            setValue((Integer) inputValue);
    }
}