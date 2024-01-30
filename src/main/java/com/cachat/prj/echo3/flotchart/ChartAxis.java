package com.cachat.prj.echo3.flotchart;

/**
 *
 * @author scachat
 */
public class ChartAxis {

    public static final String MODE_TIME = "time";
    public static final String MODE_CATEGORIES = "categories";
    private String mode;
    private Number min;
    private Number max;
    private Number autoScaleMargin;
    private Boolean show;
    private String color;
    private String tickColor;
    private String font;
    private Number tickDecimals;
    private Number minTickSize;
    private String timeformat;
    private String axisLabel;
    private String position;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Number getMin() {
        return min;
    }

    public void setMin(Number min) {
        this.min = min;
    }

    public Number getMax() {
        return max;
    }

    public void setMax(Number max) {
        this.max = max;
    }

    public Number getAutoScaleMargin() {
        return autoScaleMargin;
    }

    public void setAutoScaleMargin(Number autoScaleMargin) {
        this.autoScaleMargin = autoScaleMargin;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTickColor() {
        return tickColor;
    }

    public void setTickColor(String tickColor) {
        this.tickColor = tickColor;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public Number getTickDecimals() {
        return tickDecimals;
    }

    public void setTickDecimals(Number tickDecimals) {
        this.tickDecimals = tickDecimals;
    }

    public Number getMinTickSize() {
        return minTickSize;
    }

    public void setMinTickSize(Number minTickSize) {
        this.minTickSize = minTickSize;
    }

    public String getTimeformat() {
        return timeformat;
    }

    public void setTimeformat(String timeformat) {
        this.timeformat = timeformat;
    }

    public String getAxisLabel() {
        return axisLabel;
    }

    public void setAxisLabel(String axisLabel) {
        this.axisLabel = axisLabel;
    }

    public String getPosition() {
        return position;
    }

    /**
     * fixe la position (left ou right)
     *
     * @param position
     */
    public void setPosition(String position) {
        this.position = position;
    }

}
