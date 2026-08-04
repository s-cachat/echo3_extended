package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsOptions {

    private ChartJsInteraction interaction;
    private ChartJsAnimation animations;
    private ChartJsScales scales;
    private ChartJsPlugins plugins;
    private Boolean maintainAspectRatio;
    private Boolean responsive;
    private Double aspectRatio;
    private Double resizeDelay;
    private String locale;
    private double circumference;
    private double cutout;
    private double rotation;

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

    public ChartJsPlugins getPlugins() {
        if (plugins == null) {
            plugins = new ChartJsPlugins();
        }
        return plugins;
    }

    public void setPlugins(ChartJsPlugins plugins) {
        this.plugins = plugins;
    }

    public Boolean getMaintainAspectRatio() {
        return maintainAspectRatio;
    }

    public void setMaintainAspectRatio(Boolean maintainAspectRatio) {
        this.maintainAspectRatio = maintainAspectRatio;
    }

    public Boolean getResponsive() {
        return responsive;
    }

    public void setResponsive(Boolean responsive) {
        this.responsive = responsive;
    }

    public Double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Double getResizeDelay() {
        return resizeDelay;
    }

    public void setResizeDelay(Double resizeDelay) {
        this.resizeDelay = resizeDelay;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public double getCircumference() {
        return circumference;
    }

    public void setCircumference(double circumference) {
        this.circumference = circumference;
    }

    public double getCutout() {
        return cutout;
    }

    public void setCutout(double cutout) {
        this.cutout = cutout;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

}
