package com.cachat.prj.echo3.flotchart;

/**
 *
 * @author scachat
 */
public class ChartTypeSpec {

    private Boolean show = true;
    private Boolean fill;
    private Boolean steps;
    private Float barWidth;
    ChartGradient fillColor;
    private Float lineWidth;
    private Boolean zero;
    private Float radius;
    private Boolean horizontal;
    private Integer order;

    public boolean isShow() {
        return show == null ? false : show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isFill() {
        return fill == null ? false : fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public boolean isSteps() {
        return steps == null ? false : steps;
    }

    public void setSteps(boolean steps) {
        this.steps = steps;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public void setFill(Boolean fill) {
        this.fill = fill;
    }

    public void setSteps(Boolean steps) {
        this.steps = steps;
    }

    public Boolean getZero() {
        return zero;
    }

    public void setZero(Boolean zero) {
        this.zero = zero;
    }

    public Float getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(Float barWidth) {
        this.barWidth = barWidth;
    }

    public Float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(Float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Boolean horizontal) {
        this.horizontal = horizontal;
    }

    public ChartGradient getFillColor() {
        return fillColor;
    }

    public void setFillColor(ChartGradient fillColor) {
        this.fillColor = fillColor;
    }
}
