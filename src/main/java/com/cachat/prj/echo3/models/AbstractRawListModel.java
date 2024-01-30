package com.cachat.prj.echo3.models;

import nextapp.echo.app.list.AbstractListModel;

/**
 * un list model donnant pour chaque valeur un libelle et une valeur
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>Copyright 2003 SST Informatique
 */
public abstract class AbstractRawListModel<T> extends AbstractListModel {

    /**
     * donne la valeur brute
     */
    public abstract T getRaw(int i);
}
