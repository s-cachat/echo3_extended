package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;

/**
 * un champs en lecture seule, permettant d'afficher le nom de la classe. Elle
 * recherche dans le localized item des valeurs correspondant au getSimpleName
 * de la classe.
 *
 * @author scachat
 */
public class BlockClassReadOnly extends BlockReadOnly {

    public BlockClassReadOnly(BlockField x) {
        super(x);
    }

    public BlockClassReadOnly(LocalisedItem li) {
        super(li, "dtype");
    }

    @Override
    public void copyObjectToUi() {
        Object o = getParent().getCurrent();
        if (o == null) {
            editor.setText("");
        } else {
            editor.setText(localisedItem.getString("dtype." + o.getClass().getSimpleName()));
        }
    }
}
