package com.cachat.prj.echo3.chartjs;

import java.util.Objects;

/**
 *
 * @author scachat
 */
public class ChartJsLegend {

    /**
     * Is the legend shown?
     */
    private Boolean display;
    /**
     * Position of the legend. more...
     */
    private ChartJsLegendPosition position;
    /**
     * Alignment of the legend. more...
     */
    private ChartJsLegendAlign align;
    /**
     * number	Maximum width of the legend, in pixels
     */
    private Double maxHeight;
    /**
     * Marks that this box should take the full width/height of the canvas
     * (moving other boxes). This is unlikely to need to be changed in
     * day-to-day use.
     */
    private Boolean fullSize;
    /**
     * for rendering the legends from right to left.
     */
    private Boolean reverse;
    /**
     * This will force the text direction 'rtl' or 'ltr' on the canvas for
     * rendering the legend, regardless of the css specified on the canvas
     */
    private String textDirection;

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public ChartJsLegendPosition getPosition() {
        return position;
    }

    public void setPosition(ChartJsLegendPosition position) {
        this.position = position;
    }

    public ChartJsLegendAlign getAlign() {
        return align;
    }

    public void setAlign(ChartJsLegendAlign align) {
        this.align = align;
    }

    public Double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Boolean getFullSize() {
        return fullSize;
    }

    public void setFullSize(Boolean fullSize) {
        this.fullSize = fullSize;
    }

    public Boolean getReverse() {
        return reverse;
    }

    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }

    public String getTextDirection() {
        return textDirection;
    }

    public void setTextDirection(String textDirection) {
        this.textDirection = textDirection;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.display);
        hash = 31 * hash + Objects.hashCode(this.position);
        hash = 31 * hash + Objects.hashCode(this.align);
        hash = 31 * hash + Objects.hashCode(this.maxHeight);
        hash = 31 * hash + Objects.hashCode(this.fullSize);
        hash = 31 * hash + Objects.hashCode(this.reverse);
        hash = 31 * hash + Objects.hashCode(this.textDirection);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChartJsLegend other = (ChartJsLegend) obj;
        if (!Objects.equals(this.textDirection, other.textDirection)) {
            return false;
        }
        if (!Objects.equals(this.display, other.display)) {
            return false;
        }
        if (this.position != other.position) {
            return false;
        }
        if (this.align != other.align) {
            return false;
        }
        if (!Objects.equals(this.maxHeight, other.maxHeight)) {
            return false;
        }
        if (!Objects.equals(this.fullSize, other.fullSize)) {
            return false;
        }
        return Objects.equals(this.reverse, other.reverse);
    }

}
