package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Column;
import nextapp.echo.app.PasswordField;

/**
 * champs texte
 *
 * @author scachat
 */
public class BlockPassword extends BlockField<Column> {

    PasswordField pf1;
    PasswordField pf2;
    private static final String NOPASS = "*___*__NOPASS__*___*";

    public BlockPassword(BlockField bf) {
        super(bf.localisedItem, bf.property);
        buildEditor();
    }

    public BlockPassword(LocalisedItem li, String property) {
        super(li, property);
        buildEditor();
    }

    protected void buildEditor() {
        editor = new Column();
        pf1 = new PasswordField();
        pf2 = new PasswordField();
        editor.add(pf1);
        editor.add(pf2);
    }

    @Override
    public void copyObjectToUi() {
        if (BeanTools.canRead(getParent().getCurrent(), property)) {
            String x = BeanTools.get(getParent().getCurrent(), property);
            if (x != null && x.trim().length() > 0) {
                pf1.setText(NOPASS);
                pf2.setText("");
            }
        } else {
            pf1.setText(NOPASS);
            pf2.setText("");
        }
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            String s1 = pf1.getText();
            String s2 = pf2.getText();
            if (NOPASS.equals(s1)) {
                //no change
                error.setText((String) null);
                return false;
            }
            if (!s1.equals(s2)) {
                error.setText(localisedItem.getBaseString("passesDoesntMatch"));
                return true;
            }
            BeanTools.set(getParent().getCurrent(), property, s1);
            error.setText((String) null);
            if (BeanTools.canRead(getParent().getCurrent(), property)) {
                return validateProperty(validator, getParent().getCurrent(), property);
            } else {
                return false;
            }
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }
}
