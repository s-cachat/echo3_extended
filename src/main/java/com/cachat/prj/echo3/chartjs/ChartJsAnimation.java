package com.cachat.prj.echo3.chartjs;

/**
 *
 * @author scachat
 */
public class ChartJsAnimation {

    private Integer duration;
    private ChartJsEasing easing;
    private Integer delay;
    private Boolean loop;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ChartJsEasing getEasing() {
        return easing;
    }

    public void setEasing(ChartJsEasing easing) {
        this.easing = easing;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Boolean getLoop() {
        return loop;
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

}
