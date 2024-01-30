package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.ng.TextFieldEx;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Extent;

/**
 * un bloc dont le label et la valeur sont définie à la création
 *
 * @author scachat
 */
public class BlockFreeField extends BlockField<TextFieldEx> {

    public BlockFreeField(BlockField x) {
        super(x);
        editor = new TextFieldEx();
        editor.setWidth(new Extent(100, Extent.PERCENT));
        editor.setEditable(false);
    }

    public BlockFreeField(LocalisedItem li, String labelCode, String value) {
        super(li, labelCode);
        editor = new TextFieldEx();
        editor.setWidth(new Extent(100, Extent.PERCENT));
        editor.setText(value);
        editor.setEditable(false);
    }

    @Override
    public void copyObjectToUi() {
        //nop
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
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        //nop
        return false;
    }
}
