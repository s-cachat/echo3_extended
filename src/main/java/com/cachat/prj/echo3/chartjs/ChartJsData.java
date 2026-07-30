package com.cachat.prj.echo3.chartjs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author scachat
 */
public class ChartJsData {

    private ChartJsType type;
    private List<ChartJsDataset> datasets;
    private List<String> labels;

    public ChartJsType getType() {
        return type;
    }

    public void setType(ChartJsType type) {
        this.type = type;
    }

    public List<ChartJsDataset> getDatasets() {
        if (datasets == null) {
            datasets = new ArrayList<>();
        }
        return datasets;
    }

    public void setDatasets(List<ChartJsDataset> datasets) {
        this.datasets = datasets;
    }

    public List<String> getLabels() {
        if (labels == null) {
            labels = new ArrayList<>();
        }
        return labels;
    }

    public void addLabel(String label) {
        getLabels().add(label);
    }

    public void setLabels(String... labels) {
        getLabels().clear();
        getLabels().addAll(Arrays.asList(labels));
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    /**
     * ajoute un dataset bar
     *
     * @return le dataset ajouté
     */
    public ChartJsBarDataset addBarDataset() {
        ChartJsBarDataset ds = new ChartJsBarDataset();
        getDatasets().add(ds);
        return ds;
    }
    /**
     * ajoute un dataset line
     *
     * @return le dataset ajouté
     */
    public ChartJsLineDataset addLineDataset() {
        ChartJsLineDataset ds = new ChartJsLineDataset();
        getDatasets().add(ds);
        return ds;
    }
    /**
     * ajoute un dataset pie
     *
     * @return le dataset ajouté
     */
    public ChartJsPieDataset addPieDataset() {
        ChartJsPieDataset ds = new ChartJsPieDataset();
        getDatasets().add(ds);
        return ds;
    }
    /**
     * ajoute un dataset line
     *
     * @return le dataset ajouté
     */
    public ChartJsBubbleDataset addBubbleDataset() {
        ChartJsBubbleDataset ds = new ChartJsBubbleDataset();
        getDatasets().add(ds);
        return ds;
    }
}
