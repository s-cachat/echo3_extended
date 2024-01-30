package com.cachat.prj.echo3.flotchart;

/**
 *
 * @author scachat
 */
public class ChartGrid {

    private boolean show = true;
    private boolean aboveData = false;
    private String color;
    private ChartGradient backgroundColor;
    private boolean clickable;
    private boolean hoverable;
    private boolean autoHighlight;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isAboveData() {
        return aboveData;
    }

    public void setAboveData(boolean aboveData) {
        this.aboveData = aboveData;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ChartGradient getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(ChartGradient backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isHoverable() {
        return hoverable;
    }

    public void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
    }

    public boolean isAutoHighlight() {
        return autoHighlight;
    }

    public void setAutoHighlight(boolean autoHighlight) {
        this.autoHighlight = autoHighlight;
    }
}
