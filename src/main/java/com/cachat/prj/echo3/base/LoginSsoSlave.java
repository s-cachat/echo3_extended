package com.cachat.prj.echo3.base;

import java.util.Base64;

/**
 * conteneur pour les elements d'identification sso type echo3 SST
 */
public class LoginSsoSlave {

    /**
     * Constructeur. Utilisé par un esclave sso pour récupérer les données d'une
     * requête
     *
     * @param param le code recu du maitre sso
     */
    public LoginSsoSlave(String param) {
       if (param != null) {
            String sx[] = new String(Base64.getDecoder().decode(param)).split("\\|");
            if (sx.length == 3 && sx[1].matches("\\d+")) {
                 nom = sx[0];
                 ts = Long.parseLong(sx[1]);
                 sig = sx[2];
            }
        }
    }

    /**
     * valide le login sso
     *
     * @return true si le login est valide
     * @param key la clé
     * @param timeoutMs l'age maximal de la signature
     */
    public boolean validate(String key,long timeoutMs) {
        String base=nom+'|'+ts+'|'+key;
        System.err.printf("sign %s with %s : %s\r\n",base,key,sig);
        return com.cachat.util.CryptoUtil.checkPass(base, sig) && (Math.abs(System.currentTimeMillis()-ts)<timeoutMs);
    }
    /**
     * le nom
     */
    private  String nom;

    /**
     * donne le nom
     *
     * @return le nom
     */
    public String getNom() {
        return nom;
    }
    /**
     * la signature
     *
     */
    private  String sig;

    /**
     * donne la signature
     *
     * @return la signature
     */
    public String getSig() {
        return sig;
    }
    /**
     * le timestamp
     */
    private  Long ts;

    /**
     * donne le timestamp
     *
     * @return le timestamp
     */
    public Long getTs() {
        return ts;
    }
    /**
     * drapeau : sso utilise, a detruire
     */
    private boolean done = false;

    /**
     * indique que le sso a ete utilise
     */
    public void done() {
        done = true;
    }

    /**
     * teste si il a ete utilise
     *
     * @return true si il a été utilisé
     */
    public boolean isDone() {
        return done;
    }


}
