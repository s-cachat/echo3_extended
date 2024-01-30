package org.informagen.echo.app;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Color;
import nextapp.echo.app.TextField;

import java.lang.IllegalArgumentException;


public abstract class ActiveTextField extends TextField {
    

    public static final String PROPERTY_MESSAGE = "message";
    public static final String PROPERTY_VALID_MESSAGE = "validMessage";
    public static final String PROPERTY_INVALID_MESSAGE = "invalidMessage";
    public static final String PROPERTY_MESSAGE_POSITION = "messagePosition";

    public static final String PROPERTY_EMPTY_ICON = "emptyIcon";
    public static final String PROPERTY_VALID_ICON = "validIcon";
    public static final String PROPERTY_INVALID_ICON = "invalidIcon";
    public static final String PROPERTY_ICON_POSITION = "iconPosition";

    public static final String PROPERTY_INVALID_FOREGROUND_COLOR = "invalidForegroundColor";
    public static final String PROPERTY_INVALID_BACKGROUND_COLOR = "invalidBackgroundColor";

    private boolean required = false;
    private String message = null;
    private String validMessage = null;
    private String invalidMessage = null;

    /**
    */
       
    public void setRequired(boolean required) {
        this.required = required;
    }


    /**
    */
       
    public boolean isRequired() {
        return required;
    }


    abstract boolean isValid();

    public void setEmptyIcon(ActiveTextFieldIcon icon) {
        set(PROPERTY_EMPTY_ICON, (icon != null) ? icon.getName() : ActiveTextFieldIcon.EMPTY.getName());
    }

    public void setValidIcon(ActiveTextFieldIcon icon) {
        set(PROPERTY_VALID_ICON, (icon != null) ? icon.getName() : ActiveTextFieldIcon.GOOD.getName());
    }

    public void setInvalidIcon(ActiveTextFieldIcon icon) {
        set(PROPERTY_INVALID_ICON, (icon != null) ? icon.getName() : ActiveTextFieldIcon.ERROR.getName());
    }


    /**
     * Set the position of the icon 
     * 
     * @param iconPosition the status icon position
    */
       
    public void setIconPosition(int iconPosition) {
        
        switch (iconPosition) {
            
            // Allowed positions and default
            case Alignment.LEFT:
            case Alignment.RIGHT:
            case Alignment.TRAILING:
            case Alignment.LEADING:
                break;
                
            case Alignment.DEFAULT:
                iconPosition = Alignment.TRAILING;
                break;
                
            default:
                throw new IllegalArgumentException("Icon must be positioned on the RIGHT, LEFT, LEADING, TRAILING or DEFAULT");
        }
        
        // Alignment(horizontal, vertical)
        set(PROPERTY_ICON_POSITION, new Alignment(iconPosition, Alignment.DEFAULT));
    }


    /**
     * Get the icon position
     * 
     * @returns Alignment the icon position relative to the input field
    */
       
    public int getIconPosition() {
        Alignment iconPosition = (Alignment)get(PROPERTY_ICON_POSITION);
        return (iconPosition != null) ? iconPosition.getHorizontal() : Alignment.TRAILING;
    }


    /** ----------------------------------------------------------------------------------------
    */
       
    public void setMessage(String message) {
        String oldValue = this.message;
        this.message = message;
        firePropertyChange(PROPERTY_MESSAGE, oldValue, message);
    }


    /**
    */
       
    public String getMessage() {
        return message;
    }

    /** ----------------------------------------------------------------------------------------
    */
       
    public void setValidMessage(String validMessage) {
        String oldValue = this.validMessage;
        this.validMessage = validMessage;
        firePropertyChange(PROPERTY_VALID_MESSAGE, oldValue, validMessage);
    }


    /**
    */
       
    public String getValidMessage() {
        return validMessage;
    }


    /** ----------------------------------------------------------------------------------------
    */
       
    public void setInvalidMessage(String invalidMessage) {
        String oldValue = this.invalidMessage;
        this.invalidMessage = invalidMessage;
        firePropertyChange(PROPERTY_INVALID_MESSAGE, oldValue, invalidMessage);
    }


    /**
    */
       
    public String getInvalidMessage() {
        return invalidMessage;
    }

    /**
     * Set the position of the message 
     * 
     * @param messagePosition the status icon position
    */
       
    public void setMessagePosition(int messagePosition) {
        
        switch (messagePosition) {
            
            // Allowed positions and default
            case Alignment.TOP:
            case Alignment.BOTTOM:
                break;
                
            case Alignment.DEFAULT:
                messagePosition = Alignment.BOTTOM;
                break;
                
            default:
                throw new IllegalArgumentException("Message must be positioned on the TOP, BOTTOM or DEFAULT");
        }
        
        // Alignment(horizontal, vertical)
        set(PROPERTY_MESSAGE_POSITION, new Alignment(Alignment.DEFAULT, messagePosition));
    }


    /**
     * Get the message position
     * 
     * @returns Alignment the message position relative to the input field
    */
       
    public int getMessagePosition() {
        Alignment messagePosition = (Alignment)get(PROPERTY_MESSAGE_POSITION);
        return (messagePosition != null) ? messagePosition.getVertical() : Alignment.BOTTOM;
    }


    /** ----------------------------------------------------------------------------------------
    */
       
    public void setInvalidForegroundColor(Color color) {
        set(PROPERTY_INVALID_FOREGROUND_COLOR, color);
    }


    /**
    */
       
    public Color getInvalidForegroundColor() {
        return (Color)get(PROPERTY_INVALID_FOREGROUND_COLOR);
    }

    /** ----------------------------------------------------------------------------------------
    */
       
    public void setInvalidBackgroundColor(Color color) {
        set(PROPERTY_INVALID_BACKGROUND_COLOR, color);
    }


    /**
    */
       
    public Color getInvalidBackgroundColor() {
        return (Color)get(PROPERTY_INVALID_BACKGROUND_COLOR);
    }


    //-----------------------------------------------------------------------------------------

    protected static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

}


