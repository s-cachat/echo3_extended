package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsScaleTime {

    private Boolean isoWeekday;
    private ChartJsTimeUnit round;
    private ChartJsTimeUnit unit;
    private String tooltipFormat;
    private ChartJsTimeUnit minUnit;

    public Boolean getIsoWeekday() {
        return isoWeekday;
    }

    public void setIsoWeekday(Boolean isoWeekday) {
        this.isoWeekday = isoWeekday;
    }

    public ChartJsTimeUnit getRound() {
        return round;
    }

    public void setRound(ChartJsTimeUnit round) {
        this.round = round;
    }

    public ChartJsTimeUnit getUnit() {
        return unit;
    }

    public void setUnit(ChartJsTimeUnit unit) {
        this.unit = unit;
    }

    public String getTooltipFormat() {
        return tooltipFormat;
    }

    public void setTooltipFormat(String tooltipFormat) {
        this.tooltipFormat = tooltipFormat;
    }

    public ChartJsTimeUnit getMinUnit() {
        return minUnit;
    }

    public void setMinUnit(ChartJsTimeUnit minUnit) {
        this.minUnit = minUnit;
    }

}
