package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.ng.Strut;
import java.util.List;
import jakarta.validation.Validator;

/**
 * un espacement
 *
 * @author scachat
 */
public class BlockStrut implements BlockInterface, BlockBase<Strut> {

    /**
     * the container, for a non html content
     */
    protected Strut strut;

    /**
     * Constructeur
     *
     * @param space l'espace en pixels
     */
    public BlockStrut(int space) {
        strut = new Strut(1, space);
    }

    @Override
    public void copyObjectToUi() {
        //nop
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        //nop
        return false;
    }

    /**
     * ajouter un message d'erreur sur un champ
     *
     * @param pp le chemin de la propriété
     * @param msg le message
     * @return true si le message a pu être ajouté, false sinon
     */
    @Override
    public boolean appendError(String pp, String msg) {
        return false;
    }

    @Override
    public Strut getComponent() {
        return strut;
    }

    @Override
    public void setParent(BlockContainer parent) {
        //nop
    }

    @Override
    public void setVisible(boolean visible) {
        getComponent().setVisible(visible);
    }

    @Override
    public void setEnabled(boolean enabled) {
        getComponent().setEnabled(enabled);
    }

    @Override
    public Object clone() {
        return new BlockStrut(strut.getHeight().getValue());
    }
}
