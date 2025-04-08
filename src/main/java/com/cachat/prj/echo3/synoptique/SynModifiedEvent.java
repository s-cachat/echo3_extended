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
    protected Double left;
    /**
     * position y
     */
    protected Double top;
    /**
     * largeur
     */
    protected Double width;
    /**
     * hauteur
     */
    protected Double height;
    /**
     * angle
     */
    protected Double angle;
    /**
     * evenement
     */
    private String objectEdit;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    public Double getTop() {
        return top;
    }

    public void setTop(Double top) {
        this.top = top;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public String getObjectEdit() {
        return objectEdit;
    }

    public void setObjectEdit(String objectEdit) {
        this.objectEdit = objectEdit;
    }

    @Override
    public String toString() {
        return "SynModifiedEvent{" + "uid=" + uid + ", left=" + left + ", top=" + top + ", width=" + width + ", height=" + height + ", angle=" + angle + '}';
    }

}
