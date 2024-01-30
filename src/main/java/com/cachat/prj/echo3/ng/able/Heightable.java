package com.cachat.prj.echo3.ng.able;

import nextapp.echo.app.Extent;

/**
 * A
 * <code>Heightable</code> is a component that can have a a fixed height in some
 * specified units. These dimensions are acheived done via setting Extents.
 * <p>
 * If the Extent properties, are set to either Integer.MAX_VALUE or
 * Integer.MIN_VALUE, then it will not be taken into effect when rendering the
 * component. Also if they are null, then then it will not be taken into effect
 * when rendering the component
 * <p>
 */
public interface Heightable extends Delegateable {

    public static final String PROPERTY_HEIGHT = "height";

    /**
     * Retutns the height extent of the
     * <code>Heightable</code>.
     *
     * @return the height extent of the <code>Heightable</code>.
     */
    public Extent getHeight();

    /**
     * Sets the height extent of the
     * <code>Heightable</code>.
     *
     * @param newValue - the new height extent of the <code>Heightable</code>
     */
    public void setHeight(Extent newValue);
}
