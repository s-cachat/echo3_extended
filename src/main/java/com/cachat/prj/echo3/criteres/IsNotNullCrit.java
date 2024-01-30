package com.cachat.prj.echo3.criteres;

import java.util.List;

/**
 * un critere de type est non null
 *
 * @author scachat
 */
public class IsNotNullCrit extends BooleanCrit {

    /**
     * constructeur
     *
     * @param cont le conteneur de critères
     * @param prop le nom de la propriete critere
     */
    public IsNotNullCrit(CritContainer cont, String prop) {
        super(cont, prop);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur de critères
     * @param prop le nom de la propriete critere
     * @param def le choix par defaut (true, false ou null)
     */
    public IsNotNullCrit(CritContainer cont, String prop, Boolean def) {
        super(cont, prop, def);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur de critères
     * @param prop le nom de la propriete critere
     * @param propKey la cle pour le libelle
     * @param def le choix par defaut (true, false ou null)
     */
    public IsNotNullCrit(CritContainer cont, String prop, String propKey, Boolean def) {
        super(cont, prop, propKey, def);
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        if (rbT.isSelected()) {
            return String.format("%s is not null", prop);
        } else if (rbF.isSelected()) {
            return String.format("%s is null", prop);
        } else {
            //nop
            return null;
        }
    }
}
