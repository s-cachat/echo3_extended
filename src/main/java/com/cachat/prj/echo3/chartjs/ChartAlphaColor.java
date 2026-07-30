package com.cachat.prj.echo3.chartjs;

import nextapp.echo.app.Color;

/**
 * une couleur echo3 surchargée avec de l'alpha
 * @author scachat
 */
public class ChartAlphaColor extends Color {

    private double alpha;

    public ChartAlphaColor(int red, int green, int blue, double alpha) {
        super(red, green, blue);
        this.alpha = alpha;
    }

    public ChartAlphaColor(int red, int green, int blue) {
        super(red, green, blue);
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

}
