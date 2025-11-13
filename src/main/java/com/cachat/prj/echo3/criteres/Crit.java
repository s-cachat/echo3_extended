package com.cachat.prj.echo3.criteres;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import nextapp.echo.app.Component;
import nextapp.echo.app.Label;

/**
 * un critere de sélection
 *
 * @author scachat
 */
public abstract class Crit {

    /**
     * notre logger
     */
    protected static final Logger logger = Logger.getLogger(Crit.class.getName());
    /**
     * les composants graphiques qui me composent, dans leur ordre d'apparition
     */
    protected List<Component> critf = new ArrayList<>();

    /**
     * donne les composants graphiques qui me composent, dans leur ordre
     * d'apparition
     * @return les composants
     */
    public List<Component> getComponents() {
        return critf;
    }
    /**
     * la propriete concernee
     */
    protected String prop;

    /**
     * le conteneur (la liste)
     */
    protected CritContainer cont;
    
    /**
     * construit un critere et l'ajoute a l'ui
     *
     * @param cont le conteneur (la liste)
     * @param prop la propriete concernee
     */
    public Crit(CritContainer cont, String prop) {
        this.cont = cont;
        this.prop = prop;
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where, ou null si pas de changement a la
     * chaine
     * @param arg la liste des arguments a completer
     */
    public abstract String updateWhere(List<Object> arg);

    /**
     * donne le nom de la propriete associee
     */
    public String getProp() {
        return prop;
    }
    
    /**
     * Donne un résumé du critère recherché
     * 
     * @return le résumé
     */
    public abstract String getSummary();
    
    /**
     * crée un nouveau label pour un champs
     *
     * @param label le texte
     * @param tooltip le tooltip
     * @return le label
     */
    protected Label newLabel(String label, String tooltip) {
        Label l = new Label(label);
        l.setToolTipText(tooltip);
        l.setLineWrap(false);
        l.setStyleName("CritLabel");
        return l;
    }
}
