/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cachat.prj.echo3.base;

/**
 * un conteneur de portlet
 *
 * @author scachat
 */
public interface PortletContainer {

    /**
     * ajoute une portlet
     *
     * @param p la portlet
     */
    public void addPortlet(Portlet p);

    /**
     * supprime une portlet
     *
     * @param p la portlet
     */
    public void removePortlet(Portlet p);

    /**
     * met à jour la position des portlets en fonction de leur taille
     */
    public void updatePortletSize();
}
