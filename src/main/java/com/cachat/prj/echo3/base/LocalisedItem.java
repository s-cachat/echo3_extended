package com.cachat.prj.echo3.base;

import java.util.Locale;

/**
 * Cette interface décrit un objet pouvant fournir des textes localisés. La
 * locale choisie dépend du contexte de l'objet
 *
 * @author scachat
 */
public interface LocalisedItem {

    /**
     * donne une chaine de caractere dans la bonne langue, avec le
     * ressourcebundle du container.
     *
     * @param key la clé
     * @return la chaine de caractère
     */
    public String getString(String key);

    /**
     * donne une chaine de caractere dans la bonne langue, avec le
     * ressourcebundle global
     *
     * @param key la clé
     * @return la chaine de caractère
     */
    public String getBaseString(String key);

    /**
     * donne la langue
     * @return la langue
     */
    public Locale getLocale();
}
