package com.cachat.prj.echo3.synoptique;

/**
 * un visuel géométrique simple
 *
 * @author scachat
 */
public class SynViewBasic extends SynView {

    /**
     * les formes connues
     */
    public enum SubType {
        CIRCLE,
        ELLIPSE,
        LINE,
        POLYGON,
        POLYLINE,
        RECT,
        TRIANGLE
    }
    /**
     * Type de forme
     */
    private SubType subType;
    /**
     * couleur du remplissage
     */
    private int fill;
    /**
     * opacité
     */
    private int opacity;
    /**
     * couleur du trait
     */
    private int stroke;
    /**
     * largeur du trait
     */
    private int strokeWidth;

    public SynViewBasic() {
    }

    public SynViewBasic(SubType type, int fill) {
        this.subType = type;
        this.fill = fill;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public int getFill() {
        return fill;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }


}
