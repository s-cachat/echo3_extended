package com.cachat.prj.echo3.chartjs;

import java.util.ArrayList;
import java.util.List;
import nextapp.echo.app.Color;

/**
 *
 * @author scachat
 */
public class ChartJsDataset {

    private List<ChartJsPoint> data;
    private List<Color> backgroundColor;
    private List<Color> borderColor;
    private Double borderWidth;
    private Double clip;
    private List<Color> hoverBackgroundColor;
    private List<Color> hoverBorderColor;
    private String label;

    public List<ChartJsPoint> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<ChartJsPoint> data) {

        this.data = data;
    }

    public void addPoint(double x, double y) {
        getData().add(new ChartJsPoint(x, y));
    }

    public void addPoint(double x) {
        getData().add(new ChartJsPoint(x));
    }

    public void addPoint(String cat, double y) {
        getData().add(new ChartJsPoint(cat, y));
    }

    public List<Color> getBackgroundColor() {
        if (backgroundColor == null) {
            backgroundColor = new ArrayList<>();
        }
        return backgroundColor;
    }

    public void setBackgroundColor(List<Color> backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void addBackgroundColor(Color borderColor) {
        getBackgroundColor().add(borderColor);
    }

    public void setBackgroundColor(Color borderColor) {
        getBackgroundColor().clear();
        getBackgroundColor().add(borderColor);
    }

    public List<Color> getBorderColor() {
        if (borderColor == null) {
            borderColor = new ArrayList<>();
        }
        return borderColor;
    }

    public void setBorderColor(List<Color> borderColor) {
        this.borderColor = borderColor;
    }

    public void addBorderColor(Color borderColor) {
        getBorderColor().add(borderColor);
    }

    public void setBorderColor(Color borderColor) {
        getBorderColor().clear();
        getBorderColor().add(borderColor);
    }

    public Double getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(Double borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Double getClip() {
        return clip;
    }

    public void setClip(Double clip) {
        this.clip = clip;
    }

    public List<Color> getHoverBackgroundColor() {
        if (hoverBackgroundColor == null) {
            hoverBackgroundColor = new ArrayList<>();
        }
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(List<Color> hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public void addHoverBackgroundColor(Color borderColor) {
        getHoverBackgroundColor().add(borderColor);
    }

    public void setHoverBackgroundColor(Color borderColor) {
        getHoverBackgroundColor().clear();
        getHoverBackgroundColor().add(borderColor);
    }

    public List<Color> getHoverBorderColor() {
        if (hoverBorderColor == null) {
            hoverBorderColor = new ArrayList<>();
        }
        return hoverBorderColor;
    }

    public void setHoverBorderColor(List<Color> hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor;
    }

    public void addHoverBorderColor(Color borderColor) {
        getHoverBorderColor().add(borderColor);
    }

    public void setHoverBorderColor(Color borderColor) {
        getHoverBorderColor().clear();
        getHoverBorderColor().add(borderColor);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
