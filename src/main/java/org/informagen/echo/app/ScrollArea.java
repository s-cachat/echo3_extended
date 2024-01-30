package org.informagen.echo.app;

import nextapp.echo.app.Border;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;

public class ScrollArea extends Component {

    public static final String PROPERTY_WIDTH  = "width";
    public static final String PROPERTY_HEIGHT = "height";
    public static final String PROPERTY_BORDER = "border";

    // Constructors ---------------------------------------------------------------------------
    
    public ScrollArea() {
        this(new Extent(100, Extent.PERCENT), new Extent(100, Extent.PERCENT));
    }

    public ScrollArea(Extent width, Extent height) {
        setWidth(width);
        setHeight(height);
    }

    public ScrollArea(int width, int height) {
        this(new Extent(width), new Extent(height));
    }

    public ScrollArea(Component component, Extent width, Extent height) {
        this(width, height);
        add(component);
    }

    // Width Property -------------------------------------------------------------------------
    
    public void setWidth(Extent newValue) {
        set(PROPERTY_WIDTH, newValue);
    }

    public Extent getWidth() {
        return (Extent) get(PROPERTY_WIDTH);
    }
    
    
    // Height Property ------------------------------------------------------------------------
    
    public void setHeight(Extent newValue) {
        set(PROPERTY_HEIGHT, newValue);
    }

    public Extent getHeight() {
        return (Extent) get(PROPERTY_HEIGHT);
    }


    // Border Property ------------------------------------------------------------------------
    public void setBorder(Border border) {
        set(PROPERTY_BORDER, border);
    }
    
    public Border getBorder() {
        return (Border)get(PROPERTY_BORDER);
    }


    public boolean isValidChild(Component child) {
        return getComponentCount() == 0 || indexOf(child) != -1;
    }

}