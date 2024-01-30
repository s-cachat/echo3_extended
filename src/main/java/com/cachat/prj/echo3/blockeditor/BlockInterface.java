package com.cachat.prj.echo3.blockeditor;

import java.util.List;
import jakarta.validation.Validator;

/**
 *
 * @author scachat
 */
public interface BlockInterface extends Cloneable {

    /**
     * copy object values to ui components
     */
    public abstract void copyObjectToUi();

    /**
     * copy ui components (edited) values to object. for the fields, if a error
     * is encountered (bad values, ...), it should be displayed in the field's
     * error label. If the error is really not field specific, it can be
     * displayed at the top of the form, by adding the error text to
     * genericErrors.
     *
     * @param genericErrors a list for generic error
     * @param validator le validateur utilisé
     * @return true if there were errors
     */
    public abstract boolean copyUiToObject(Validator validator, List<String> genericErrors);

    /**
     * set the parent (the container)
     *
     * @param parent the BlockContainer
     */
    public void setParent(BlockContainer parent);

    /**
     * ajouter un message d'erreur sur un champ
     *
     * @param pp le chemin de la propriété
     * @param msg le message
     * @return true si le message a pu être ajouté, false sinon
     */
    public boolean appendError(String pp, String msg);

    /**
     * set the ui visibility
     *
     * @param visible if true the ui for this item is visible
     */
    public void setVisible(boolean visible);

    /**
     * set the ui editability
     *
     * @param enabled if true the ui for this item is enabled
     */
    public void setEnabled(boolean enabled);

    /**
     * cloneable
     */
    public Object clone() throws CloneNotSupportedException;
}
