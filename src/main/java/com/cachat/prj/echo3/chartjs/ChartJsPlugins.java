package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsPlugins {

    private ChartJsPluginCustomCanvasBackgroundColor customCanvasBackgroundColor;
    private ChartJsLegend legend;

    public ChartJsPluginCustomCanvasBackgroundColor getCustomCanvasBackgroundColor() {
        if (customCanvasBackgroundColor == null) {
            customCanvasBackgroundColor = new ChartJsPluginCustomCanvasBackgroundColor();
        }
        return customCanvasBackgroundColor;
    }

    public void setCustomCanvasBackgroundColor(ChartJsPluginCustomCanvasBackgroundColor customCanvasBackgroundColor) {
        this.customCanvasBackgroundColor = customCanvasBackgroundColor;
    }

    public ChartJsLegend getLegend() {
        if (legend == null) {
            legend = new ChartJsLegend();
        }
        return legend;
    }

    public void setLegend(ChartJsLegend legend) {
        this.legend = legend;
    }

}
