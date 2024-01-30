package com.cachat.prj.echo3.components;

import com.cachat.prj.echo3.ng.ButtonEx;
import nextapp.echo.app.FillImage;
import nextapp.echo.app.ImageReference;

/**
 *
 * @author scachat
 */
public class ButtonEx2 extends ButtonEx {

    public static final String PROPERTY_BORDER_IMAGE = "borderImage";
    public static final String PROPERTY_BORDER_TOP = "borderImageTop";
    public static final String PROPERTY_BORDER_RIGHT = "borderImageRight";
    public static final String PROPERTY_BORDER_BOTTOM = "borderImageBottom";
    public static final String PROPERTY_BORDER_LEFT = "borderImageLeft";

    public ButtonEx2() {
    }

    public ButtonEx2(String label) {
        super(label);
    }

    public ButtonEx2(ImageReference icon) {
        super(icon);
    }

    public ButtonEx2(String text, ImageReference icon) {
        super(text, icon);
    }

    /**
     * Returns the border image.
     *
     * @return the border image
     */
    public FillImage getBorderImage() {
        return (FillImage) get(PROPERTY_BORDER_IMAGE);
    }

    public int getBorderTop() {
        return toInt(get(PROPERTY_BORDER_TOP));
    }

    public int getBorderRight() {
        return toInt(get(PROPERTY_BORDER_RIGHT));
    }

    public int getBorderBottom() {
        return toInt(get(PROPERTY_BORDER_BOTTOM));
    }

    public int getBorderLeft() {
        return toInt(get(PROPERTY_BORDER_LEFT));
    }

    /**
     * Sets the border image.
     *
     * @param newValue the new background image
     */
    public void setBorderImage(FillImage newValue) {
        set(PROPERTY_BORDER_IMAGE, newValue);
    }

    /**
     * Sets the border image top.
     *
     * @param newValue the new background image
     */
    public void setBorderImageTop(int top) {
        set(PROPERTY_BORDER_TOP, top);
    }

    /**
     * Sets the border image top.
     *
     * @param newValue the new background image
     */
    public void setBorderImageRight(int right) {
        set(PROPERTY_BORDER_RIGHT, right);
    }

    /**
     * Sets the border image top.
     *
     * @param newValue the new background image
     */
    public void setBorderImageBottom(int bottom) {
        set(PROPERTY_BORDER_BOTTOM, bottom);
    }

    /**
     * Sets the border image top.
     *
     * @param newValue the new background image
     */
    public void setBorderImageLeft(int left) {
        set(PROPERTY_BORDER_LEFT, left);
    }

    private int toInt(Object property) {
        return property == null ? 0 : ((Number) property).intValue();
    }
}
