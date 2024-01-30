package org.informagen.echo.app;

import nextapp.echo.app.Component;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;

import java.lang.Number;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CapacityBar extends Component {

    // Properties
    public static final String PROPERTY_WIDTH = "width";
    public static final String PROPERTY_HEIGHT = "height";
    public static final String PROPERTY_SHOW_TICKS = "showTicks";
    public static final String PROPERTY_REFLECTIVITY = "reflectivity";
    public static final String PROPERTY_CORNER_RADIUS = "cornerRadius";

    // Values
    public static final String PROPERTY_TICK_SPACING = "tickSpacing";
    public static final String PROPERTY_COLORS = "colors";
    public static final String PROPERTY_VALUES = "values";

    private Extent width = new Extent(460, Extent.PX);
    private Extent height = new Extent(18, Extent.PX);
    private boolean showTicks = true;
    private double reflectivity = 0.75;
    private double cornerRadius = 1.0;

    private Number tickSpacing = null;

    private List<Color> colors = Collections.EMPTY_LIST;
    private List<Number> values = Collections.EMPTY_LIST;

    public CapacityBar() {
        super();
    }

    public CapacityBar(int height, int width) {
        super();

        setHeight(height);
        setWidth(width);
    }

    //-----------------------------------------------------------------------------------------
    /**
     * Set the width
     *
     * @param width the width in pixels
     */
    public void setWidth(int width) {
        setWidth(new Extent(width, Extent.PX));
    }

    public void setWidth(Extent width) {
        Extent oldWidth = this.width;
        this.width = width;
        firePropertyChange(PROPERTY_WIDTH, oldWidth, width);
    }

    /**
     * Get the width
     *
     * @returns Extent the width
     */
    public Extent getWidth() {
        return width;
    }

    //-----------------------------------------------------------------------------------------
    /**
     * Set the height
     *
     * @param height the height in pixels
     */
    public void setHeight(int height) {
        setHeight(new Extent(height, Extent.PX));
    }

    public void setHeight(Extent height) {
        Extent oldHeight = this.height;
        this.height = height;
        firePropertyChange(PROPERTY_HEIGHT, oldHeight, height);
    }

    /**
     * Get the height
     *
     * @returns Extent the height
     */
    public Extent getHeight() {
        return height;
    }

    //-----------------------------------------------------------------------------------------
    /**
     * Set showTicks
     *
     * @param showTicks show/hide the tick marks along the capacity bar
     */
    public void setShowTicks(boolean showTicks) {
        Boolean oldShowTicks = this.showTicks ? Boolean.TRUE : Boolean.FALSE;
        this.showTicks = showTicks;
        firePropertyChange(PROPERTY_SHOW_TICKS, oldShowTicks, showTicks ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Are the ticks shown or hidden
     *
     * @returns boolean the value of showTicks
     */
    public boolean isShowTicks() {
        return showTicks;
    }

    //-----------------------------------------------------------------------------------------
    /**
     * Set reflectivity of the relfected image
     *
     * @param reflectivity the level of the reflectivity, .001 is solid, 1.0 is
     * transparent
     */
    public void setReflectivity(double reflectivity) {
        Double oldValue = Double.valueOf(this.reflectivity);

        // Keep in range 0.001 to 1.0
        reflectivity = Math.max(reflectivity, 0.001);
        reflectivity = Math.min(reflectivity, 1.00);

        this.reflectivity = reflectivity;
        firePropertyChange(PROPERTY_REFLECTIVITY, oldValue, reflectivity);
    }

    /**
     * Are the ticks shown or hidden
     *
     * @returns double the reflectivity of the reflected image, .001 is
     * transparent, 1.0 is solid
     */
    public double getReflectivity() {
        return reflectivity;
    }

    //-----------------------------------------------------------------------------------------
    /**
     * Set the corner radius of the capacity bar
     *
     * @param corner radius, .001 is square, 1.0 is round
     */
    public void setCornerRadius(double cornerRadius) {
        Double oldCornerRadius = Double.valueOf(this.cornerRadius);

        // Keep in range 0.001 to 1.0
        cornerRadius = Math.max(cornerRadius, 0.001);
        cornerRadius = Math.min(cornerRadius, 1.00);

        this.cornerRadius = cornerRadius;
        firePropertyChange(PROPERTY_CORNER_RADIUS, oldCornerRadius, Double.valueOf(cornerRadius));
    }

    /**
     * Are the ticks shown or hidden
     *
     * @returns double the opacity of the reflected image, .001 is solid, 1.0 is
     * transparent
     */
    public double getCornerRadius() {
        return cornerRadius;
    }

    //-----------------------------------------------------------------------------------------
    /**
     * Set the tick spacing of the capacity bar
     *
     * @param tickSpacing
     */
    public void setTickSpacing(Number tickSpacing) {
        Number oldTickSpacing = this.tickSpacing;

        this.tickSpacing = tickSpacing;
        firePropertyChange(PROPERTY_TICK_SPACING, oldTickSpacing, tickSpacing);
    }

    /**
     * Spacing of the ticks
     *
     * @returns Number
     */
    public Number getTickSpacing() {
        return tickSpacing;
    }

    //-----------------------------------------------------------------------------------------
    /**
     * Set the list of colors
     *
     * @param colors the list of colors to use and reuse across the bar
     */
    public void setColors(List<Color> colors) {
        List oldColors = this.colors;

        this.colors = colors;
        firePropertyChange(PROPERTY_COLORS, oldColors, colors);
    }

    /**
     * The list of colors to use and reuse across the bar
     *
     * @returns List<colors> the list of colors to use and reuse across the bar
     */
    public List<Color> getColors() {
        return colors;
    }

    //-----------------------------------------------------------------------------------------
    /**
     * Set the list of values
     *
     * @param values the list of values to use across the bar
     */
    public void setValues(List<Number> values) {
        List oldValues = this.values;

        this.values = values;
        firePropertyChange(PROPERTY_VALUES, oldValues, values);
    }

    /**
     * The list of values to use across the bar
     *
     * @returns List<Number> the list of values to use across the bar
     */
    public List<Number> getValues() {
        return values;
    }

}
