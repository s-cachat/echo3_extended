package org.informagen.echo.app;

import nextapp.echo.app.Component;
import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;

/** 
 * <code>HorizontalRule</code> is a very simple component which implements the HTML 'hr' tag.
 *  
 *  NB: There are methods to control the properties 'width', 'height', and 'border'
 *    but have been deprecated in HTML v4 and beyond. Use these properties with care.
 */

public class HorizontalRule extends Component {

    public static final String PROPERTY_HEIGHT = "height";
    public static final String PROPERTY_WIDTH  = "width";
    public static final String PROPERTY_COLOR  = "color";
    public static final String PROPERTY_BORDER = "border";


    /**
     * Sets the height of the text component.
     * This property only supports <code>Extent</code>s with
     * fixed (i.e., not percent) units.
     * 
     * @param newValue the new height
     */
    public void setHeight(Extent newValue) {
        set(PROPERTY_HEIGHT, newValue);
    }


    /**
     * Returns the height of the HR element.
     * This property only supports <code>Extent</code>s with
     * fixed (i.e., not percent) units.
     * 
     * @return the height
     */
    public Extent getHeight() {
        return (Extent) get(PROPERTY_HEIGHT);
    }
  
         
    /**
     * Sets the width of the HR element.
     * This property supports <code>Extent</code>s with
     * either fixed or percentage-based units.
     * 
     * @param newValue the new width
     */
    public void setWidth(Extent newValue) {
        set(PROPERTY_WIDTH, newValue);
    }
    
    
    /**
     * Returns the width of the HR element.
     * This property supports <code>Extent</code>s with
     * either fixed or percentage-based units.
     * 
     * @return the width
     */
    public Extent getWidth() {
        return (Extent) get(PROPERTY_WIDTH);
    }


    
    /**
     * Sets the border of the text component.
     * 
     * @param newValue the new border
     */
    public void setColor(Color newValue) {
        set(PROPERTY_COLOR, newValue);
    }

   
    /**
     * Returns the color of the HR element.
     * 
     * @return the border
     */
    public Color getColor() {
        return (Color) get(PROPERTY_COLOR);
    }


    
    /**
     * Sets the border of the HR element.
     * 
     * @param newValue the new border
     */
    public void setBorder(Border newValue) {
        set(PROPERTY_BORDER, newValue);
    }

   
    /**
     * Returns the border of the HR element.
     * 
     * @return the border
     */
    public Border getBorder() {
        return (Border) get(PROPERTY_BORDER);
    }


}