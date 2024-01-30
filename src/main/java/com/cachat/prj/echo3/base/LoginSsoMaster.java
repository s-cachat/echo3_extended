package com.cachat.prj.echo3.base;

import java.util.Base64;

/**
 * conteneur pour les elements d'identification sso
 */
public class LoginSsoMaster {


    /**
     * Constructeur. Utilisé par le maitre sso pour générer un login
     *
     * @param nom Le nom recu par le maitre sso
     */
    public LoginSsoMaster(String nom) {
        this.nom = nom;
    }

    /**
     * génère un login sso
     *
     * @param key la clé
     * @return le paramètre à transmettre à l'esclave
     */
    public String generate(String key) {
        long ts=System.currentTimeMillis();
        String base=nom+'|'+ts+'|'+key;
        String sig=com.cachat.util.CryptoUtil.hashPass(base);
        System.err.printf("sign %s with %s : %s\r\n",base,key,sig);
        return Base64.getEncoder().encodeToString((nom+'|'+ts+"|"+sig).getBytes());
    }

    /**
     * le nom
     */
    private final String nom;

    /**
     * donne le nom
     *
     * @return le nom
     */
    public String getNom() {
        return nom;
    }
    
}
