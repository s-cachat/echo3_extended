package com.cachat.prj.echo3.blockeditor;

/**
 * une exception signalant un contenu invalide
 *
 * @author scachat
 */
public class InvalidException extends Exception {

    /**
     * le code pour trouver le message dans les ressources localisées
     */
    private String code = null;

    public InvalidException() {
    }

    /**
     * Constructeur
     *
     * @param message le message par défaut
     * @param code le code pour trouver le message dans les ressources
     * localisées
     */
    public InvalidException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * Constructeur
     *
     * @param message le message par défaut
     */
    public InvalidException(String message) {
        super(message);
    }

    /**
     * Constructeur
     *
     * @param message le message par défaut
     * @param cause l'exception d'origine
     */
    public InvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur
     *
     * @param message le message par défaut
     * @param cause l'exception d'origine
     * @param code le code pour trouver le message dans les ressources
     * localisées
     */
    public InvalidException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Constructeur
     *
     * @param cause l'exception d'origine
     */
    public InvalidException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

}
