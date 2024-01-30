package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Extent;
import com.cachat.prj.echo3.quill.QuillEditor;

/**
 * champs texte riche
 *
 * @author scachat
 */
public class BlockQuill extends BlockField<QuillEditor> {

    public BlockQuill(BlockField x) {
        super(x);
        editor = new QuillEditor();

        editor.setWidth(new Extent(100, Extent.PERCENT));
        editor.setHeight(new Extent(200, Extent.PX));
    }

    public BlockQuill(LocalisedItem li, String property) {
        super(li, property);
        editor = new QuillEditor();
        editor.setWidth(new Extent(100, Extent.PERCENT));
        editor.setHeight(new Extent(200, Extent.PX));
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

    @Override
    public void setEnabled(boolean enabled) {
        //     editor.setEditable(enabled);
    }

    /**
     * get the text
     *
     * @return the field text
     */
    public String getText() {
        return editor.getText();
    }
}
