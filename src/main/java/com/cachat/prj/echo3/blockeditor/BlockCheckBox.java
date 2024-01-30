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
public class BlockCheckBox extends BlockField<CheckBoxEx> {

    public BlockCheckBox(BlockField x) {
        super(x);
        editor = new CheckBoxEx();
    }

    public BlockCheckBox(LocalisedItem li, String property) {
        super(li, property);
        editor = new CheckBoxEx();
    }

    @Override
    public void copyObjectToUi() {
        Boolean x = (Boolean) BeanTools.getRaw(getParent().getCurrent(), property);
        editor.setSelected(x == null ? false : x);
    }

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
    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            BeanTools.setRaw(getParent().getCurrent(), property, editor.isSelected());

            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }
}
