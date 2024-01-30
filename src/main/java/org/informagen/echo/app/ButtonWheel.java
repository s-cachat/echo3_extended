/*
 */

package org.informagen.echo.app;

// Echo - Components
import nextapp.echo.app.Component;

// Echo - Events
import nextapp.echo.app.event.ActionListener;

// Echo - Button Model
import nextapp.echo.app.button.ButtonModel;
import nextapp.echo.app.button.DefaultButtonModel;


public class ButtonWheel extends Component {

    public static final String INPUT_ACTION = "action";
        
    public static final String PROPERTY_ACTION_LISTENERS_CHANGED = "actionListeners";
    public static final String PROPERTY_ACTION_COMMAND_CHANGED   = "actionCommand";

    public static final String PROPERTY_DIAMETER = "diameter";
    public static final String PROPERTY_MINIMUM  = "minimum";
    public static final String PROPERTY_MAXIMUM  = "maximum";
    public static final String PROPERTY_VALUE    = "value";
    public static final String PROPERTY_NOSCROLL = "noScroll";
    public static final String PROPERTY_SENSITIVITY = "sensitivity";

    private int diameter = 150;
    private String actionCommand;
    private int minimum = 0;
    private int maximum = 100;
    private int value = 0;
    private int sensitivity = 15;
     
    private final ButtonModel buttonModel = new DefaultButtonModel();
     
    /** Default constructor. */

    public ButtonWheel() {
        this(150);
    }
    
    /**
    * Create a new instance using the specified image reference.
    *
    * @param int The outer diameter of the scroll wheel
    */

    public ButtonWheel(final int diameter ) {
        super();
        setNoScroll(true);
        setDiameter(diameter);
    }
 
    public ButtonModel getModel() {
        return buttonModel;
    }
    
    
    /**
    * Return the value of {@link #PROPERTY_DIAMETER} property.
    *
    * @return The image reference currently in use
    */

    public int getDiameter() {
        return diameter;
    }
    
    /**
    * Set the value of {@link #PROPERTY_DIAMETER} property.
    *
    * @param diameter The outer diameter of the scroll wheel
    */

    public void setDiameter(final int diameter ) {
        Object oldValue =  Integer.valueOf(this.diameter);
        this.diameter = diameter;
        firePropertyChange(PROPERTY_DIAMETER, oldValue, diameter);
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(final int maximum) {
        Object oldValue = Integer.valueOf(this.maximum);
        this.maximum = maximum;
        firePropertyChange(PROPERTY_MAXIMUM, oldValue, maximum);
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(final int minimum ) {
        Object oldValue = Integer.valueOf(this.minimum);
        this.minimum = minimum;
        firePropertyChange(PROPERTY_MINIMUM, oldValue, minimum);
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value ) {
        Object oldValue = Integer.valueOf(this.value);
        this.value = value;
        firePropertyChange(PROPERTY_VALUE, oldValue, value);
    }


    public int getSensitivity() {
        return sensitivity;
    }

    /**
    ** Control the sensitivity of the scroll wheel, set large
    **  for small ranges, small for large ranges
    */

    public void setSensitivity(final int sensitivity ) {
        Object oldValue = Integer.valueOf(this.sensitivity);
        this.sensitivity = sensitivity;
        firePropertyChange(PROPERTY_SENSITIVITY, oldValue, sensitivity);
    }


    // public boolean isNoScroll() {
    //     return noScroll;
    // }

    protected void setNoScroll(final boolean noScroll ) {
        firePropertyChange(PROPERTY_NOSCROLL, null, (noScroll ? Boolean.TRUE : Boolean.FALSE));
    }
 
    public void processInput(String inputName, Object inputValue) {
        
        if (PROPERTY_ACTION_COMMAND_CHANGED.equals(inputName)) {
            String value = (String) inputValue;
            getModel().setActionCommand(value);
       } else  if (PROPERTY_VALUE.equals(inputName)) {
            setValue(((Integer)inputValue));
       } else if (INPUT_ACTION.equals(inputName)) {
            getModel().doAction();
        }
    }

    public void addActionListener(ActionListener l) {
        getModel().addActionListener(l);
    }

    public void removeActionListener(ActionListener l) {
        getModel().removeActionListener(l);
    }

    
}
