package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.interfaces.User;

/**
 * conteneur pour les elements d'identification sso type openConnectId
 */
public class LoginExterne {

    /**
     * l'utilisateur connecté
     */
    private User user;
    /**
     * le message provenant de la connexion sso (succès ou échec)
     */
    private String message;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
