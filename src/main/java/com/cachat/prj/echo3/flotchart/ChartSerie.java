package com.cachat.prj.echo3.flotchart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author scachat
 */
public class ChartSerie {

    private String color;
    private String label;
    private ChartTypeSpec lines;
    private ChartTypeSpec bars;
    private ChartTypeSpec points;
    private ChartTypeSpec pie;
    private Integer yaxis;
    private Boolean stack;
    private Boolean step;

    List<List<Object>> data = new ArrayList<>();
    @JsonIgnore
    private ChartTypeSpec spec;
    @JsonIgnore
    protected List<ChartPoint> dataPoints = new ArrayList<>();
    @JsonIgnore
    private final Type type;

    public static enum Type {

        BAR,
        LINE,
        POINTS,
        PIE
    }

    public ChartSerie(Type type) {
        this.type = type;
        spec = new ChartTypeSpec();
        switch (type) {
            case BAR:
                bars = spec;
                break;
            case LINE:
                lines = spec;
                break;
            case POINTS:
                points = spec;
                break;
            case PIE:
                pie = spec;
                break;
        }
    }

    public List<ChartPoint> getDataPoints() {
        return dataPoints;
    }

    public void addPoint(String x, Number y) {
        dataPoints.add(new ChartPoint(x, y));
    }

    public void addPoint(Number x, Number y) {
        dataPoints.add(new ChartPoint(x, y));
    }

    /**
     * les données, formatées pour flotchart (pour les avoir, il faut d'abord
     * appeler process)
     *
     * @return les données
     */
    public List<List<Object>> getData() {
        return data;
    }

    /**
     * fixe les données. Elles doivent être formatée sous la forme d'un tableau
     * de serie. Une serie est un tableau de valeur. Une valeur est un tableau
     * de deux nombres x,y
     *
     * @param data les données formatées
     */
    public void setData(List<List<Object>> data) {
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ChartTypeSpec getSpec() {
        return spec;
    }

    public String getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    void process() {
        data.clear();
        for (ChartPoint p : getDataPoints()) {
            String x = p.getX();
            Number y = p.getY();
            data.add(Arrays.asList((Object) x, y == null ? 0 : y.doubleValue()));
        }
    }

    public Integer getYaxis() {
        return yaxis;
    }

    public void setYaxis(Integer yaxis) {
        this.yaxis = yaxis;
    }

    public ChartTypeSpec getLines() {
        return lines;
    }

    public ChartTypeSpec getBars() {
        return bars;
    }

    public ChartTypeSpec getPoints() {
        return points;
    }

    public ChartTypeSpec getPie() {
        return pie;
    }

    public Boolean getStack() {
        return stack;
    }

    public void setStack(Boolean stack) {
        this.stack = stack;
    }

    public Boolean getStep() {
        return step;
    }

    public void setStep(Boolean step) {
        this.step = step;
    }

}
