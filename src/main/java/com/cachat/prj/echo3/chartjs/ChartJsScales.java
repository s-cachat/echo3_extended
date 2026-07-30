package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsScales {

    private ChartJsScale x;
    private ChartJsScale y;

    public ChartJsScale getX() {
        if (x == null) {
            x = new ChartJsScale();
        }
        return x;
    }

    public void setX(ChartJsScale x) {
        this.x = x;
    }

    public ChartJsScale getY() {
        if (y == null) {
            y = new ChartJsScale();
        }
        return y;
    }

    public void setY(ChartJsScale y) {
        this.y = y;
    }

}
