package com.cachat.prj.echo3.blockeditor;

import nextapp.echo.app.Component;

/**
 * un block de base est un composant qui prend toute la largeur de l'editeur,
 * comme par exemple le BlockPanel.
 *
 * @author scachat
 * @param <T> le type de composant echo2 utilisé comme base
 */
public interface BlockBase<T extends Component> {

    /**
     * donne le composant ui
     *
     * @return le composant
     */
    public T getComponent();
}
