package com.cachat.prj.echo3.ng.able;

import nextapp.echo.app.Extent;
import nextapp.echo.app.Position;

/**
 * A
 * <code>Positionable</code> is one that can be postioned anywhere on the
 * screen, regardless of the flow layout of other components.
 * <p>
 * By default the it acts like a normal component, and will be rendered with the
 * flow of its parent and siblings. The component has its Positioning property
 * set to POSITIONING_FLOW.
 * <p>
 * <h3>POSITIONING</h3>
 *
 * However if the Positioning property is POSITIONING_ABSOLUTE or
 * POSITIONING_RELATIVE then the component will break out of the normal flow
 * layout and position themselves directly on the screen.
 * <p>
 * If the Positioning is POSITIONING_RELATIVE, then the component is positioned
 * at an at a point on the screen relative to its first positioned parent
 * component. If it has no parents that are positioned, then it will be
 * positioned relative to the origins of the client window.
 * <p>
 * If the Positioning is POSITIONING_ABSOLUTE, then the component is positioned
 * at an absolute point outside the normal flow of layout. The left, top, right
 * and bottom properties can be used to position the component.
 * <p>
 * If the Positioning is POSITIONING_FIXED, then the component is positioned at
 * an absolute point from the origin of the client window. The left, top, right
 * and bottom properties can be used to position the component.
 * <p>
 * <h3>Left, Top, Right, Bottom</h3>
 * Typically you would set the Left and Top properties in order to get a
 * Positionable to a specified location. However you can also use the Right and
 * Bottom properties.
 * <p>
 * For example you could position a component to 100 pixels in from the bottom
 * and 10 pixels if from the right by only settting the bottom and right
 * properties to 100 and 10 respectively. The width of the component will be
 * determined by the content.
 * <p>
 * A convenience method called <i>clearPositioning() </i> is provided to clear
 * all positioning and have the component acts like a normal flow component.
 * <p>
 * <h3>Z-INDEX</h3>
 *
 * A
 * <code>Positionable</code> also supports a z-idex, which controls how it is
 * layered over other components, especially other <code>Positionable</code> 's.
 * <p>
 * If no zIndex is to apply then the Integer.MIN_VALUE can be used in which case
 * no zIndex will be set.
 */
public interface Positionable extends Delegateable {

    public static final String PROPERTY_BOTTOM = "bottom";
    public static final String PROPERTY_LEFT = "left";
    public static final String PROPERTY_POSITION = "position";
    public static final String PROPERTY_RIGHT = "right";
    public static final String PROPERTY_TOP = "top";
    public static final String PROPERTY_Z_INDEX = "zIndex";

    /**
     * This sets all the positioning attributes (left,top,right,bottom,z-index)
     * to null or zero.
     */
    public void clear();

    /**
     * Returns the bottom Y position of the component
     */
    public Extent getBottom();

    /**
     * Returns the left X position of the component
     */
    public Extent getLeft();

    /**
     * The positionning mode
     *
     * @return the mode
     */
    public Position getPosition();

    /**
     * Returns the right X position of the component
     */
    public Extent getRight();

    /**
     * Returns the top Y position of the component
     */
    public Extent getTop();

    /**
     * Returns the z-index of the component
     */
    public int getZIndex();

    /**
     * This returns true if any positioning is in place other than normal flow
     * ie. STATIC.
     *
     */
    public boolean isPositioned();

    /**
     * Sets the bottom Y position of the component
     */
    public void setBottom(Extent newValue);

    /**
     * Set the left X position of the component
     */
    public void setLeft(Extent newValue);

    /**
     * Sets the position of the component
     * @param newPositioning the new position
     */
    public void setPosition(Position newPositioning);

    /**
     * Sets the right X position of the component
     */
    public void setRight(Extent newValue);

    /**
     * Sets the top Y position of the component
     */
    public void setTop(Extent newValue);

    /**
     * Sets the z-index of the component
     */
    public void setZIndex(int newValue);
}
