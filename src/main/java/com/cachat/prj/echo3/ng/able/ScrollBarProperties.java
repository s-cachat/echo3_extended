package com.cachat.prj.echo3.ng.able;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import nextapp.echo.app.Color;

/**
 * <code>ScrollBarProperties</code> is used to contain colors for scroll bars.
 * Not that setting the scrollbar colors may not work an all client agents.
 * <p>
 * The scroll box is the square box within a scroll bar that can be moved either
 * up and down or left and right on a track to change the position of the
 * content on the screen. The scroll arrows, located at each end of a scroll
 * bar, are the square buttons containing the arrows that move the content on
 * the screen in small increments, either up and down or left and right.
 * <p>
 * The gutter is the space between the track and the bottom and right edges of
 * the scroll box and scroll arrows of the scroll bar. The scrollbar
 * darkShadowColor appears outside the scrollbar shadowColor. The track is the
 * element of a scroll bar on which the scroll box can slide either up and down
 * or left and right.
 * <p>
 * The scroll arrows, located at each end of a scroll bar, are the square
 * buttons containing the arrows that move the content on the screen in small
 * increments, either up and down or left and right.
 *
 */
public class ScrollBarProperties implements Serializable {

    private Color threeDLightColor;
    private Color arrowColor;
    private Color baseColor;
    private Color darkShadowColor;
    private Color faceColor;
    private Color hilightColor;
    private Color shadowColor;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Constructs a
     * <code>ScrollBarProperties</code>
     */
    public ScrollBarProperties() {
        super();
    }

    /**
     * Constructs a
     * <code>ScrollBarProperties</code> with the specified base color.
     *
     * @param baseColor - the base color to use
     */
    public ScrollBarProperties(Color baseColor) {
        super();
        setBaseColor(baseColor);
    }

    /**
     * Adds a property change listener.
     *
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * Removes a property change listener.
     *
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /**
     * @return the color of the arrow elements of a scroll arrow.
     */
    public Color getArrowColor() {
        return arrowColor;
    }

    /**
     * Sets the color of the arrow elements of a scroll arrow.
     *
     * @param newValue - The newValue to set.
     */
    public void setArrowColor(Color newValue) {
        Object oldValue = this.arrowColor;
        this.arrowColor = newValue;
        pcs.firePropertyChange("arrowColor", oldValue, newValue);
    }

    /**
     * @return the color of the main elements of a scroll bar, which include the
     * scroll box, track, and scroll arrows.
     */
    public Color getBaseColor() {
        return baseColor;
    }

    /**
     * Sets the color of the main elements of a scroll bar, which include the
     * scroll box, track, and scroll arrows. This can be set in isolation to
     * quickly set the main colors of the scroll bar.
     *
     * @param newValue - The newValue to set.
     */
    public void setBaseColor(Color newValue) {
        Object oldValue = this.baseColor;
        this.baseColor = newValue;
        pcs.firePropertyChange("baseColor", oldValue, newValue);
    }

    /**
     * @return the color of the gutter of a scroll bar.
     */
    public Color getDarkShadowColor() {
        return darkShadowColor;
    }

    /**
     * Sets the color of the gutter of a scroll bar.
     *
     * @param newValue - The newValue to set.
     */
    public void setDarkShadowColor(Color newValue) {
        Object oldValue = this.darkShadowColor;
        this.darkShadowColor = newValue;
        pcs.firePropertyChange("darkShadow", oldValue, newValue);
    }

    /**
     * @return the color of the scroll box and scroll arrows of a scroll bar.
     */
    public Color getFaceColor() {
        return faceColor;
    }

    /**
     * Sets the color of the scroll box and scroll arrows of a scroll bar.
     *
     * @param newValue - The newValue to set.
     */
    public void setFaceColor(Color newValue) {
        Object oldValue = this.faceColor;
        this.faceColor = newValue;
        pcs.firePropertyChange("faceColor", oldValue, newValue);
    }

    /**
     * @return the color of the top and left edges of the scroll box and scroll
     * arrows of a scroll bar.
     */
    public Color getHilightColor() {
        return hilightColor;
    }

    /**
     * Sets the color of the top and left edges of the scroll box and scroll
     * arrows of a scroll bar.
     *
     * @param newValue - The newValue to set.
     */
    public void setHilightColor(Color newValue) {
        Object oldValue = this.hilightColor;
        this.hilightColor = newValue;
        pcs.firePropertyChange("hilightColor", oldValue, newValue);
    }

    /**
     * @return the color of the bottom and right edges of the scroll box and
     * scroll arrows of a scroll bar.
     */
    public Color getShadowColor() {
        return shadowColor;
    }

    /**
     * Sets the color of the bottom and right edges of the scroll box and scroll
     * arrows of a scroll bar.
     *
     * @param newValue - The newValue to set.
     */
    public void setShadowColor(Color newValue) {
        Object oldValue = this.shadowColor;
        this.shadowColor = newValue;
        pcs.firePropertyChange("shadowColor", oldValue, newValue);
    }

    /**
     * @return the color of the top and left edges of the scroll box and scroll
     * arrows of a scroll bar.
     */
    public Color getThreeDLightColor() {
        return threeDLightColor;
    }

    /**
     * Sets the color of the top and left edges of the scroll box and scroll
     * arrows of a scroll bar.
     *
     * @param newValue - The newValue to set.
     */
    public void setThreeDLightColor(Color newValue) {
        Object oldValue = this.threeDLightColor;
        this.threeDLightColor = newValue;
        pcs.firePropertyChange("threeDLightColor", oldValue, newValue);
    }
}
