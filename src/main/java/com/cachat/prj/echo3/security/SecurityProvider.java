package com.cachat.prj.echo3.security;

import com.cachat.prj.echo3.interfaces.User;

/**
 * Cette classe fournie un service d'identification
 *
 * @author scachat
 */
public interface SecurityProvider {

    /**
     * Valide un login
     *
     * @param username nom d'utilisateur
     * @param password mot de passe
     * @return l'utilisateur, ou null si les informations sont non valides
     */
    public User validLogin(String username, String password);
}
