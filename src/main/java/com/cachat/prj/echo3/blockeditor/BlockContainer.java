package com.cachat.prj.echo3.blockeditor;

import nextapp.echo.app.Component;

/**
 * un container pour un editeur des propriétés directes d'un objet. pour éditer
 * un sous objet on ajoute un BlockContainer à ce BlockContainer
 *
 * @author scachat
 */
public interface BlockContainer<T> extends BlockInterface {

    /**
     * ajoute un composant d'edition. Est également chargé de faire le
     * setParent(this)
     *
     * @param bf le champs
     * @return le champs
     */
    public BlockInterface add(BlockInterface bf);

    /**
     * supprime un composant d'edition. Est également chargé de faire le
     * setParent(null)
     *
     * @param bf
     */
    public void remove(BlockInterface bf);

    /**
     * donne l'objet concerne par cet editeur
     *
     * @return l'objet
     */
    public T getCurrent();

    /**
     * donne le composant echo2 (container)
     *
     * @return le composant
     */
    public Component getComponent();
}
