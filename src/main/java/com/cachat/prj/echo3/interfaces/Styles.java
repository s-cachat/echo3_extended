package com.cachat.prj.echo3.interfaces;

import nextapp.echo.app.ImageReference;
import nextapp.echo.app.StyleSheet;

/**
 * interface centrale d'accès aux styles
 *
 * @author scachat
 */
public interface Styles {

    /**
     * donne le chemin dans le classpath pour les images
     *
     * @return le chemin
     */
    public String getImagePath();

    /**
     * donne le pattern du chemin dans le classpath pour les icones
     *
     * @return le pattern du chemin
     */
    public String getIconPattern();

    /**
     * donne le chemin dans le classpath pour les styles
     *
     * @return le chemin
     */
    public String getStylePath();

    /**
     * donne la feuille de style par d�faut
     *
     * @return la feuille de style
     */
    public StyleSheet getDefaultStyleSheet();

    /**
     * donne l'icone par défaut des boutons
     * @return l'icone
     */
    public ImageReference getButtonIcon();

    /**
     * donne l'icone
     *
     * @param name le nom de l'icone
     * @return l'icone
     */
    public ImageReference getIcon(String name);

    /**
     * donne l'image demandée
     *
     * @param name le nom de l'image
     * @return l'image, ou l'image debug si elle n'existe pas
     */
    public ImageReference getImage(String name);

    /**
     * donne l'image demand�e
     *
     * @param name le nom de l'image
     * @return l'image, ou null si elle n'existe pas
     */
    public ImageReference getImage2(String name);
}
