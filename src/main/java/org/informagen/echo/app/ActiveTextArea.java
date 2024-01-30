package org.informagen.echo.app;

import nextapp.echo.app.Color;
import nextapp.echo.app.TextArea;


public class ActiveTextArea extends TextArea {

    public static final String PROPERTY_OVERDRAFT = "overdraft";

    public static final String PROPERTY_OVERDRAFT_FOREGROUND_COLOR = "overdraftForegroundColor";
    public static final String PROPERTY_OVERDRAFT_BACKGROUND_COLOR = "overdraftBackgroundColor";

    /**
     *  A TextArea which displays the current character count.  If
     *   an 'overdraft' value is specified this component will also
     *   display the number of remaining characters.  If this limit
     *   is exceeded, the overdrawn nubmer of character is displayed
     *   in bold red text.
     *
     *  NB: This component will not truncate the String returned with
     *      with the superclass method 'getText'. This decision must
     *      be implemented with application level business logic.
     */



    public ActiveTextArea() {
    }

    /**
     *  return Suggested maximum number of characters
     */

    public int getOverdraft() {
        return ((Integer)get(PROPERTY_OVERDRAFT)).intValue();
    }

    /**
     *  overdraft Suggested maximum number of characters
     */

    public void setOverdraft(int overdraft) {
        set(PROPERTY_OVERDRAFT, overdraft);
    }



    /** ----------------------------------------------------------------------------------------
    */
       
    public void setOverdraftForegroundColor(Color color) {
        set(PROPERTY_OVERDRAFT_FOREGROUND_COLOR, color);
    }


    /**
    */
       
    public Color getOverdraftForegroundColor() {
        return (Color)get(PROPERTY_OVERDRAFT_FOREGROUND_COLOR);
    }

    /** ----------------------------------------------------------------------------------------
    */
       
    public void setOverdraftBackgroundColor(Color color) {
        set(PROPERTY_OVERDRAFT_BACKGROUND_COLOR, color);
    }


    /**
    */
       
    public Color getOverdraftBackgroundColor() {
        return (Color)get(PROPERTY_OVERDRAFT_BACKGROUND_COLOR);
    }

}