package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.CheckBoxEx;
import java.util.List;
import jakarta.validation.Validator;

/**
 * champs case a cocher (propriété boolean ou Boolean)
 *
 * @author scachat
 */
public class BlockCheckBoxRO extends BlockField<CheckBoxEx> {

    public BlockCheckBoxRO(BlockField x) {
        super(x);
        editor = new CheckBoxEx();
        editor.setEnabled(false);
    }

    public BlockCheckBoxRO(LocalisedItem li, String property) {
        super(li, property);
        editor = new CheckBoxEx();
        editor.setEnabled(false);
    }

    @Override
    public void copyObjectToUi() {
        Boolean x = (Boolean) BeanTools.getRaw(getParent().getCurrent(), property);
        editor.setSelected(x == null ? false : x);
    }

    /**
     * inhibe le setEnabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(false);
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        //nop
        return false;
    }
}
