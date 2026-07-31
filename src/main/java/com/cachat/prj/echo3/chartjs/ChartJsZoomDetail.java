package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsZoomDetail {

    private ChartJsZoomWheel wheel;
    private ChartJsZoomPinch pinch;
    private String mode;

    public ChartJsZoomWheel getWheel() {
        if (wheel == null) {
            wheel = new ChartJsZoomWheel();
        }
        return wheel;
    }

    public void setWheel(ChartJsZoomWheel wheel) {
        this.wheel = wheel;
    }

    public ChartJsZoomPinch getPinch() {
        if (pinch == null) {
            pinch = new ChartJsZoomPinch();
        }
        return pinch;
    }

    public void setPinch(ChartJsZoomPinch pinch) {
        this.pinch = pinch;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}
