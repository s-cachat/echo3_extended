package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsPoint {

    private Double x;
    private String label;
    private Double y;
    private Double r;

    public ChartJsPoint(String label, double y) {
        this.label = label;
        this.y = y;
    }

    public ChartJsPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public ChartJsPoint(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public ChartJsPoint(double y) {
        this.y = y;
    }

    public ChartJsPoint() {
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

}
