package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsBubbleDataset extends ChartJsDataset {

    private String borderSkipped;
    private Boolean drawActiveElementsOnTop;
    private Double hoverBorderWidth;
    private Double hoverRadius;
    private Double hitRadius;
    private Integer order;
    private ChartJsPointStyle pointStyle;
    private Double rotation;
    private Double radius;

    public void addPoint(double x, double y, double r) {
        getData().add(new ChartJsPoint(x, y, r));
    }

    public String getBorderSkipped() {
        return borderSkipped;
    }

    public void setBorderSkipped(String borderSkipped) {
        this.borderSkipped = borderSkipped;
    }

    public Boolean getDrawActiveElementsOnTop() {
        return drawActiveElementsOnTop;
    }

    public void setDrawActiveElementsOnTop(Boolean drawActiveElementsOnTop) {
        this.drawActiveElementsOnTop = drawActiveElementsOnTop;
    }

    public Double getHoverBorderWidth() {
        return hoverBorderWidth;
    }

    public void setHoverBorderWidth(Double hoverBorderWidth) {
        this.hoverBorderWidth = hoverBorderWidth;
    }

    public Double getHoverRadius() {
        return hoverRadius;
    }

    public void setHoverRadius(Double hoverRadius) {
        this.hoverRadius = hoverRadius;
    }

    public Double getHitRadius() {
        return hitRadius;
    }

    public void setHitRadius(Double hitRadius) {
        this.hitRadius = hitRadius;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public ChartJsPointStyle getPointStyle() {
        return pointStyle;
    }

    public void setPointStyle(ChartJsPointStyle pointStyle) {
        this.pointStyle = pointStyle;
    }

    public Double getRotation() {
        return rotation;
    }

    public void setRotation(Double rotation) {
        this.rotation = rotation;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

}
