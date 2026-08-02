package com.cachat.prj.echo3.chartjs;

import java.util.Objects;

/**
 *
 * @author scachat
 */
public class ChartJSConfig {

    private ChartJsType type;
    private ChartJsData data;
    private ChartJsOptions options;

    public ChartJsType getType() {
        return type;
    }

    public void setType(ChartJsType type) {
        this.type = type;
    }

    public ChartJsData getData() {
        if (data == null) {
            data = new ChartJsData();
        }
        return data;
    }

    public void setData(ChartJsData data) {
        this.data = data;
    }

    public ChartJsOptions getOptions() {
        if (options == null) {
            options = new ChartJsOptions();
        }
        return options;
    }

    public void setOptions(ChartJsOptions options) {
        this.options = options;
    }

}
