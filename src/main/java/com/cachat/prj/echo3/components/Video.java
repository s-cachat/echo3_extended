package com.cachat.prj.echo3.components;

import static com.cachat.prj.echo3.ng.able.Heightable.PROPERTY_HEIGHT;
import com.cachat.prj.echo3.ng.able.Positionable;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_BOTTOM;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_LEFT;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_POSITION;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_RIGHT;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_TOP;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_Z_INDEX;
import com.cachat.prj.echo3.ng.able.Sizeable;
import static com.cachat.prj.echo3.ng.able.Widthable.PROPERTY_WIDTH;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;

/**
 * insère un tag permettant de voir de la vidéo, selon les paramètres et le type
 * de navigateur. Les combinaisons possibles sont : <ul>
 * <li> type jpeg et url image : image mise à jour toutes les 1/fps</li>
 * <li> type mjpeg et url video : tag image, avec pour ie une mise à jour toutes
 * les secondes (cet idiot ne sais pas gérer le mjpeg)</li>
 * <li> type mjpeg et url video et image : tag image en utilisant le mjpeg sauf
 * pour l'idiot du village qui ne connait pas le mjpeg, pour lequel on affiche
 * l'image mise à jour toutes les 1/fps</li>
 * <li> type webm et url video : tag video, et si on a aussi une url image, on
 * la met en poster </li>
 * </ul>
 *
 * @author scachat
 */
public class Video extends Component implements Positionable, Sizeable {

    public static final String PROPERTY_TYPE = "videoType";
    public static final String PROPERTY_URL_IMAGE = "videoImageUrl";
    public static final String PROPERTY_URL_VIDEO = "videoVideoUrl";
    public static final String PROPERTY_SHOW_CONTROL = "videoShowControl";

    /**
     * les types de visu
     */
    public static enum Type {
        JPEG,
        MJPEG,
        WEBM
    }

    /**
     * fixe le type de visu
     *
     * @param type le type
     */
    public void setType(Type type) {
        set(PROPERTY_TYPE, type.name());
    }

    /**
     * donne le type de visu
     *
     * @return le type
     */
    public Type getType() {
        String _type = (String) get(PROPERTY_TYPE);
        return _type == null ? null : Type.valueOf(_type);
    }

    /**
     * fixe l'url de l'image
     *
     * @param url l'url
     */
    public void setImageUrl(String url) {
        set(PROPERTY_URL_IMAGE, url);
    }

    /**
     * donne l'url de l'image
     *
     * @return l'url
     */
    public String getImageUrl() {
        return (String) get(PROPERTY_URL_IMAGE);
    }

    /**
     * fixe l'url de la video
     *
     * @param url l'url
     */
    public void setVideoUrl(String url) {
        set(PROPERTY_URL_VIDEO, url);
    }

    /**
     * donne l'url de la video
     *
     * @return l'url
     */
    public String getVideoUrl() {
        return (String) get(PROPERTY_URL_VIDEO);
    }

    /**
     * afficher les contrôles de la vidéo
     *
     * @param showControl si true, et si possible, affiche les contrôles de la
     * vidéo
     */
    public void setShowControl(boolean showControl) {
        set(PROPERTY_SHOW_CONTROL, showControl);
    }

    /**
     * afficher les contrôles de la vidéo
     *
     * @return si true, et si possible, affiche les contrôles de la vidéo
     */
    public boolean getShowControl() {
        Boolean b = (Boolean) get(PROPERTY_SHOW_CONTROL);
        return b == null || b;
    }

    @Override
    public Extent getBottom() {
        return (Extent) get(PROPERTY_BOTTOM);
    }

    @Override
    public Extent getLeft() {
        return (Extent) get(PROPERTY_LEFT);
    }

    @Override
    public int getPosition() {
        return (Integer) get(PROPERTY_POSITION);
    }

    @Override
    public Extent getRight() {
        return (Extent) get(PROPERTY_RIGHT);
    }

    @Override
    public Extent getTop() {
        return (Extent) get(PROPERTY_TOP);
    }

    @Override
    public int getZIndex() {
        return (Integer) get(PROPERTY_Z_INDEX);
    }

    @Override
    public boolean isPositioned() {
        return getPosition() != Positionable.STATIC;
    }

    @Override
    public void setPosition(int newValue) {
        set(PROPERTY_POSITION, newValue);
    }

    @Override
    public void setBottom(Extent newValue) {
        set(PROPERTY_BOTTOM, newValue);
    }

    @Override
    public void setLeft(Extent newValue) {
        set(PROPERTY_LEFT, newValue);
    }

    @Override
    public void setRight(Extent newValue) {
        set(PROPERTY_RIGHT, newValue);
    }

    @Override
    public void setTop(Extent newValue) {
        set(PROPERTY_TOP, newValue);
    }

    public final void setBottom(int newValue) {
        set(PROPERTY_BOTTOM, new Extent(newValue));
    }

    public final void setLeft(int newValue) {
        set(PROPERTY_LEFT, new Extent(newValue));
    }

    public final void setRight(int newValue) {
        set(PROPERTY_RIGHT, new Extent(newValue));
    }

    public final void setTop(int newValue) {
        set(PROPERTY_TOP, new Extent(newValue));
    }

    @Override
    public void setZIndex(int newValue) {
        set(PROPERTY_Z_INDEX, newValue);
    }

    @Override
    public Extent getHeight() {
        return (Extent) get(PROPERTY_HEIGHT);
    }

    @Override
    public void setHeight(Extent newValue) {
        set(PROPERTY_HEIGHT, newValue);
    }

    public void setHeight(int newValue) {
        set(PROPERTY_HEIGHT, new Extent(newValue));
    }

    @Override
    public Extent getWidth() {
        return (Extent) get(PROPERTY_WIDTH);
    }

    @Override
    public void setWidth(Extent newValue) {
        set(PROPERTY_WIDTH, newValue);
    }

    public void setWidth(int newValue) {
        set(PROPERTY_WIDTH, new Extent(newValue));
    }

    @Override
    public void clear() {
        //TODO
    }

    /**
     * Fixe les limites. position sera absolute
     *
     * @param left extent en pixel, ou null si indéfini
     * @param top extent en pixel, ou null si indéfini
     * @param right extent en pixel, ou null si indéfini
     * @param bottom extent en pixel, ou null si indéfini
     * @param width extent en pixel, ou null si indéfini
     * @param height extent en pixel, ou null si indéfini
     */
    public void setBounds(Integer left, Integer top, Integer right, Integer bottom, Integer width, Integer height) {
        setPosition(Positionable.ABSOLUTE);
        if (top != null) {
            setTop(new Extent(top));
        }
        if (left != null) {
            setLeft(new Extent(left));
        }
        if (right != null) {
            setRight(new Extent(right));
        }
        if (bottom != null) {
            setBottom(new Extent(bottom));
        }
        if (width != null) {
            setWidth(new Extent(width));
        }
        if (height != null) {
            setHeight(new Extent(height));
        }
    }
}
