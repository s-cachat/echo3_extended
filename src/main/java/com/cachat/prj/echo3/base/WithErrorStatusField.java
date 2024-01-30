package com.cachat.prj.echo3.base;

/**
 * objet avec un champs pour l'affichage d'un message d'erreur (typiquement
 * blanc sur rouge)
 *
 * @author scachat
 */
public interface WithErrorStatusField {

    /**
     * met à jour le message d'erreur
     *
     * @param msg le message ou null pour faire disparaitre le champs
     */
    public void setErrorMsg(String msg);
}
