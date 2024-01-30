package com.cachat.prj.echo3.flotchart;

/**
 *
 * @author scachat
 */
public class ChartPoint {

    protected String x;
    protected Number y;

    public ChartPoint(String x, Number y) {
        this.x = x;
        this.y = y;
    }

    public ChartPoint(Number x, Number y) {
        this.x = String.valueOf(x);
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public void setX(Number x) {
        this.x = String.valueOf(x);
    }

    public Number getY() {
        if (y instanceof Double && ((Double) y).isNaN()) {
            return 0;
        }
        return y;
    }

    public void setY(Number y) {
        this.y = y;
    }
}
