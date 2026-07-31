package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsZoom {

    private ChartJsPan pan;
    private ChartJsZoomDetail zoom;

    public ChartJsPan getPan() {
        if (pan == null) {
            pan = new ChartJsPan();
        }
        return pan;
    }

    public void setPan(ChartJsPan pan) {
        this.pan = pan;
    }

    public ChartJsZoomDetail getZoom() {
        if (zoom == null) {
            zoom = new ChartJsZoomDetail();
        }
        return zoom;
    }

    public void setZoom(ChartJsZoomDetail zoom) {
        this.zoom = zoom;
    }

}
