package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Extent;
import nextapp.echo.app.TextArea;

/**
 * un bloc dont le label et la valeur sont définie à la création
 *
 * @author scachat
 */
public class BlockFreeArea extends BlockField<TextArea> {

    public BlockFreeArea(BlockField x) {
        super(x);
        editor = new TextArea();
        editor.setWidth(new Extent(100, Extent.PERCENT));
        editor.setEditable(false);
    }

    public BlockFreeArea(LocalisedItem li, String labelCode, String value) {
        super(li, labelCode);
        editor = new TextArea();
        editor.setWidth(new Extent(100, Extent.PERCENT));
        editor.setText(value);
        editor.setHeight(new Extent(100));
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
