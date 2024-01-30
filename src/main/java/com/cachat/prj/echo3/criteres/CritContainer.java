package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.base.LocalisedItem;
import nextapp.echo.app.event.ActionListener;

/**
 * une classe pouvant contenir des criteres
 *
 * @author scachat
 */
public interface CritContainer extends ActionListener, LocalisedItem {

    /**
     * hauteur par defaut d'un critere
     */
    public static final int CRIT_HEIGHT = 32;

    /**
     * agrandie la zone de criteres
     *
     * @param height
     */
    public void extendCritAreaHeight(int height);

    /**
     * ajoute un critere accessible a l'utilisateur
     */
    public void addCrit(Crit crit);

    /**
     * donne un critere
     */
    public Crit getCrit(String prop);
}
