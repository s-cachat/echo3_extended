package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsPlugins {

    private ChartJsPluginCustomCanvasBackgroundColor customCanvasBackgroundColor;
    private ChartJsLegend legend;
    private ChartJsZoom zoom;
    private ChartJsTooltip tooltip;

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

    public ChartJsZoom getZoom() {if (zoom==null)zoom=new ChartJsZoom();
        return zoom;
    }

    public void setZoom(ChartJsZoom zoom) {
        this.zoom = zoom;
    }

    public ChartJsTooltip getTooltip() {
        return tooltip;
    }

    public void setTooltip(ChartJsTooltip tooltip) {
        this.tooltip = tooltip;
    }

}
