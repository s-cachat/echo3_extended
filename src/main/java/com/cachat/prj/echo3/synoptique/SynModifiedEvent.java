package com.cachat.prj.echo3.synoptique;

/**
 *
 * @author scachat
 */
public class SynModifiedEvent {

    /**
     * l'identifiant unique de l'objet modifié
     */
    private String uid;
    /**
     * position x
     */
    protected double left;
    /**
     * position y
     */
    protected double top;
    /**
     * largeur
     */
    protected double width;
    /**
     * hauteur
     */
    protected double height;
    /**
     * angle
     */
    protected double angle;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "SynModifiedEvent{" + "uid=" + uid + ", left=" + left + ", top=" + top + ", width=" + width + ", height=" + height + ", angle=" + angle + '}';
    }

}
