package com.cachat.prj.echo3.flotchart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.ArrayList;
import java.util.List;

/**
 * le modèle de donnée, pour la création initiale. Contient les données à tracer
 * et les paramètres du graphe.
 *
 * @author scachat
 */
@JsonRootName(value = "flotModel")
public class ChartModel {

    @JsonIgnore
    private final List<ChartSerie> series = new ArrayList<>();
    ChartAxis xaxis;
    @JsonIgnore
    ChartAxis yaxis1;
    @JsonIgnore
    ChartAxis yaxis2;
    ChartAxis yaxes[] = {yaxis1};
    boolean clickable = false;
    boolean hoverable = true;
    Integer shadowSize;
    Integer highlightColor;
    Boolean stack;
    ChartGrid grid;
    ChartLegend legend;
    boolean tooltip;
    ChartTooltip tooltipOpts;
    private ChartTypeSpec pie;
    private String title;

    public ChartTooltip getTooltipOpts() {
        if (tooltipOpts == null) {
            tooltipOpts = new ChartTooltip();
        }
        tooltip = true;
        return tooltipOpts;
    }

    public ChartLegend getLegend() {
        if (legend == null) {
            legend = new ChartLegend();
        }
        return legend;
    }

    public ChartGrid getGrid() {
        if (grid == null) {
            grid = new ChartGrid();
        }
        return grid;
    }

    public ChartAxis getXaxis() {
        if (xaxis == null) {
            xaxis = new ChartAxis();
        }
        return xaxis;
    }

    public void setXaxis(ChartAxis xaxis) {
        this.xaxis = xaxis;
    }

    public ChartAxis getYaxis() {
        if (yaxis1 == null) {
            yaxis1 = new ChartAxis();
            yaxis1.setPosition("left");
        }
        return yaxis1;
    }

    public void setYaxis(ChartAxis yaxis1) {
        this.yaxis1 = yaxis1;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isHoverable() {
        return hoverable;
    }

    public void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
    }

    public Integer getShadowSize() {
        return shadowSize;
    }

    public void setShadowSize(Integer shadowSize) {
        this.shadowSize = shadowSize;
    }

    public Integer getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(Integer highlightColor) {
        this.highlightColor = highlightColor;
    }

    public List<ChartSerie> getSeries() {
        return series;
    }

    public void process() {
        int i = 0;
        for (ChartSerie cs : series) {
            if (cs.getType() == ChartSerie.Type.PIE) {
                this.pie = cs.getSpec();
            }
            cs.process();
            switch (cs.getType()) {
                case BAR:
                    if (cs.getSpec().getOrder() == null) {
                        cs.getSpec().setOrder(i++);
                    }
                    break;
                case LINE:
                    break;
                case PIE:
                    break;
                case POINTS:
                    break;

            }
        }
    }

    public Boolean getStack() {
        return stack;
    }

    public void setStack(Boolean stack) {
        this.stack = stack;
    }

    public boolean isTooltip() {
        return tooltip;
    }

    public void setTooltip(boolean tooltip) {
        this.tooltip = tooltip;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYAxis2Enabled(boolean b) {
        if (b) {
            yaxes = new ChartAxis[]{yaxis1, yaxis2};
        } else {
            yaxes = new ChartAxis[]{yaxis1};
        }
    }

    public ChartAxis getYaxis2() {
        if (yaxis2 == null) {
            yaxis2 = new ChartAxis();
            yaxis2.setPosition("right");
        }
        return yaxis2;
    }

    public void setYaxis2(ChartAxis yaxis2) {
        this.yaxis2 = yaxis2;
    }
}
