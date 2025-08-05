package com.cachat.prj.echo3.ng.able;

import nextapp.echo.app.Color;

/**
 * A
 * <code>Scrollable</code> is a component that can have a a fixed width and/or
 * height, and can present scrollbars when the content of the component is too
 * large to fit inside.
 * <p>
 * <h3>SCROLLBARS</h3>
 *
 * Since a <code>Scrollable</code> can be made a fixed size, by setting its
 * width and/or height properties, it has support for a scroll bar policy which
 * controls how scroll bars are used if the content of the component will not
 * fit into the fixed size.
 * <p>
 * If the scroll bar policy is NEVER, then no scroll bars will be shown and the
 * content inside the component will be clipped to the bounding rectangle.
 * <p>
 * If the scroll bar policy is ALWAYS, then scroll bars will always be shown,
 * regardless of whether the content is too big for the bounding rectangle,
 * which allows the user to view all the content.
 * <p>
 * If the scroll bar policy is AUTO, then scroll bars will be shown when
 * appropriate, ie when the content is too big for the bounding rectangle.
 */
public interface Scrollable extends Sizeable {

    /**
     * A scroll bar policy that will cause not cause any scroll bar policy to be
     * applied at all. Its as if no scroll bar policy is in place.
     */
    public static final int UNDEFINED = 0;
    /**
     * A scroll bar policy that will cause scroll bars to never appear, without
     * regard for whether they are required. Content is never clipped even if
     * its to large for the components dimensions.
     */
    public static final int NEVER = 1;
    /**
     * A scroll bar policy that will cause scroll bars to always appear, without
     * regard for whether they are required.
     */
    public static final int ALWAYS = 2;
    /**
     * A scroll bar policy that will cause scroll bars to be visible if they are
     * necessary, and invisible if they are not.
     */
    public static final int AUTO = 4;
    /**
     * A scroll bar policy that will cause scroll bars to never appear, without
     * regard for whether they are required. Content will always be clipped to
     * the components dimensions and the scollbars are hidden.
     */
    public static final int CLIPHIDE = 8;
    /**
     * ALWAYS for Y, NEVER for X
     */
    public static final int ALWAYS_Y = 16;
    /**
     * ALWAYS for X, NEVER for Y
     */
    public static final int ALWAYS_X = 32;
    public static final String PROPERTY_SCROLL_BAR_POLICY = "scrollBarPolicy";
    public static final String PROPERTY_SCROLL_BAR_BASE_COLOR = "scrollBarBaseColor";
    public static final String PROPERTY_SCROLL_BAR_PROPERTIES = "scrollBarProperties";

    /**
     * Returns the ScrollBarPolicy in place
     *
     * This can be one of :
     * <ul>
     * <li>NONE</li>
     * <li>ALWAYS</li>
     * <li>AUTO</li>
     * <li>CLIPHIDE</li>
     * </ul>
     */
    public int getScrollBarPolicy();

    /**
     * Returns the base color of the ScrollBarProperties associated with this
     * <code>Scrollable</code>
     *
     * @return the base color of the ScrollBarProperties associated with * this
     * <code>Scrollable</code>
     */
    public Color getScrollBarBaseColor();

    /**
     * Returns the ScrollBarProperties associated with this
     * <code>Scrollable</code>
     *
     * @return the ScrollBarProperties associated with * this
     * <code>Scrollable</code>
     */
    public ScrollBarProperties getScrollBarProperties();

    /**
     * Sets the scroll bar policy of the component
     *
     * This can be one of :
     * <ul>
     * <li>SCOLLBARS_NONE</li>
     * <li>SCOLLBARS_ALWAYS</li>
     * <li>SCOLLBARS_AUTO</li>
     * <li>CLIPHIDE</li>
     * </ul>
     */
    public void setScrollBarPolicy(int newScrollBarPolicy);

    /**
     * Sets the base color of the ScrollBarProperties associated with this
     * <code>Scrollable</code>. If no ScrollBarProperties is available, then a
     * new one should be created.
     *
     * @param newValue - the new base color of ScrollBarProperties to use
     */
    public void setScrollBarBaseColor(Color newValue);

    /**
     * Sets the ScrollBarProperties associated with this <code>Scrollable</code>
     *
     * @param newValue - the new ScrollBarProperties to use
     */
    public void setScrollBarProperties(ScrollBarProperties newValue);
}
