package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsPlugins {

    public ChartJsPluginCustomCanvasBackgroundColor customCanvasBackgroundColor;

    public ChartJsPluginCustomCanvasBackgroundColor getCustomCanvasBackgroundColor() {
        if (customCanvasBackgroundColor == null) {
            customCanvasBackgroundColor = new ChartJsPluginCustomCanvasBackgroundColor();
        }
        return customCanvasBackgroundColor;
    }

    public void setCustomCanvasBackgroundColor(ChartJsPluginCustomCanvasBackgroundColor customCanvasBackgroundColor) {
        this.customCanvasBackgroundColor = customCanvasBackgroundColor;
    }

}
