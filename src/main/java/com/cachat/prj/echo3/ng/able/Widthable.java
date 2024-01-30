package com.cachat.prj.echo3.ng.able;

import nextapp.echo.app.Extent;

/**
 * A
 * <code>Sizeable</code> is a component that can have a a fixed width in some
 * specified units. These dimensions are acheived done via setting Extents.
 */
public interface Widthable extends Delegateable {

    public static final String PROPERTY_WIDTH = "width";

    /**
     * Returns the width extent of the
     * <code>Widthable</code>.
     *
     * @return the width extent of the <code>Widthable</code>.
     */
    public Extent getWidth();

    /**
     * Sets the width extent of the
     * <code>Widthable</code>.
     *
     * @param newValue - the new width extent of the <code>Widthable</code>
     */
    public void setWidth(Extent newValue);
}
