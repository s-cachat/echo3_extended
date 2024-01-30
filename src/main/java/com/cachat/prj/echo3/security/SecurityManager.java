package com.cachat.prj.echo3.security;

import com.cachat.prj.echo3.interfaces.User;
import java.lang.reflect.InvocationTargetException;

/**
 * Le gestionnaire de sécurité, pour valider les logins
 *
 * @author scachat
 */
public class SecurityManager {

    /**
     * le fournisseur de sécurité
     */
    private SecurityProvider provider = null;
    /**
     * l'instance unique
     */
    private static final SecurityManager instance = new SecurityManager();

    public static SecurityManager getInstance() {
        return instance;
    }

    /**
     * Valide un login
     *
     * @param user nom d'utilisateur
     * @param pass mot de passe
     * @return l'utilisateur, ou null si les informations sont non valides
     */
    public User validLogin(String user, String pass) {
        return provider == null ? null : provider.validLogin(user, pass);
    }

    /**
     * initialise (si ce n'est déjà fait) le fournisseur
     *
     * @param clazz le type du fournisseur, qui doit avoir un constructeur sans
     * arguments
     */
    public void setProviderClass(Class<? extends SecurityProvider> clazz) {
        if (provider == null) {
            try {
                provider = clazz.getConstructor().newInstance();
            } catch (NoSuchMethodException|InvocationTargetException|InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
