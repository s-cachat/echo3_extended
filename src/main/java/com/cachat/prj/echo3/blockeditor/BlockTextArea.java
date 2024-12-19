package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Extent;
import nextapp.echo.app.TextArea;

/**
 * champs texte
 *
 * @author scachat
 */
public class BlockTextArea extends BlockField<TextArea> {

    public BlockTextArea(BlockField x) {
        super(x);
        editor = new TextArea();
        editor.setWidth(new Extent(100, Extent.PERCENT));
    }

    public BlockTextArea(LocalisedItem li, String property) {
        super(li, property);
        editor = new TextArea();
        editor.setWidth(new Extent(100, Extent.PERCENT));
    }

    public void setHeight(Extent height) {
        editor.setHeight(height);
    }

    @Override
    public void copyObjectToUi() {
        editor.setText(BeanTools.get(getParent().getCurrent(), property));
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
            final Object current = getParent().getCurrent();
            if (current != null) {
                BeanTools.set(current, property, editor.getText());
                error.setText((String) null);
                return validateProperty(validator, current, property);
            } else {
                error.setText("!");
                return true;
            }
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }

    /**
     * donne le texte actuellement affiché
     *
     * @return le texte
     */
    public String getText() {
        return editor.getText();
    }

    /**
     * fixe le texte affiché (mais ne change pas le bean)
     *
     * @param texte le texte
     */
    public void setText(String texte) {
        editor.setText(texte);
    }

    @Override
    public void setEnabled(boolean enabled) {
        editor.setEditable(enabled);
    }
}
