package com.cachat.prj.echo3.chartjs;

import java.util.List;

/**
 *
 * @author scachat
 */
public class ChartJsOptions {

    private ChartJsInteraction interaction;
    private ChartJsAnimation animations;
    private ChartJsScales scales;
    private List<ChartJsPlugins> plugins;

    public ChartJsInteraction getInteraction() {
        return interaction;
    }

    public void setInteraction(ChartJsInteraction interaction) {
        this.interaction = interaction;
    }

    public ChartJsAnimation getAnimations() {
        return animations;
    }

    public void setAnimations(ChartJsAnimation animations) {
        this.animations = animations;
    }

    public ChartJsScales getScales() {
        if (scales == null) {
            scales = new ChartJsScales();
        }
        return scales;
    }

    public void setScales(ChartJsScales scales) {
        this.scales = scales;
    }

    public List<ChartJsPlugins> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<ChartJsPlugins> plugins) {
        this.plugins = plugins;
    }

}
