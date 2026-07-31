package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsScale {

    private Double min;
    private Double max;
    private Double suggestedMin;
    private Double suggestedMax;
    private Boolean beginAtZero;
    private ChartJsScaleType type;
    private ChartJsScaleTime time;

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Boolean getBeginAtZero() {
        return beginAtZero;
    }

    public void setBeginAtZero(Boolean beginAtZero) {
        this.beginAtZero = beginAtZero;
    }

    public Double getSuggestedMin() {
        return suggestedMin;
    }

    public void setSuggestedMin(Double suggestedMin) {
        this.suggestedMin = suggestedMin;
    }

    public Double getSuggestedMax() {
        return suggestedMax;
    }

    public void setSuggestedMax(Double suggestedMax) {
        this.suggestedMax = suggestedMax;
    }

    public ChartJsScaleType getType() {
        return type;
    }

    public void setType(ChartJsScaleType type) {
        this.type = type;
    }

    public ChartJsScaleTime getTime() {
        if (time == null) {
            time = new ChartJsScaleTime();
        }
        return time;
    }

    public void setTime(ChartJsScaleTime time) {
        this.time = time;
    }

}
