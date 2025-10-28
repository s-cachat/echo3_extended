package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.TextFieldEx;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Extent;
import nextapp.echo.app.event.DocumentEvent;
import nextapp.echo.app.event.DocumentListener;

/**
 * champs texte
 *
 * @author scachat
 */
public class BlockTextField extends BlockField<TextFieldEx> implements DocumentListener {

    /**
     * if true, we added a document listener
     */
    private boolean isListeningDocument = false;

    public BlockTextField(BlockField x) {
        super(x);
        editor = new MyTextfield();
        editor.setStyleName("BlockEditor");
    }

    public BlockTextField(LocalisedItem li, String property) {
        super(li, property);
        editor = new MyTextfield();
        editor.setStyleName("BlockEditor");
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
        editor.setEditable(enabled);
    }
    /**
     * change listeners
     */
    private List<DocumentListener> changeListener = new ArrayList<>();

    /**
     * add a change listener
     *
     * @param c the listener called when the text changes
     */
    public void addListener(DocumentListener c) {
        if (!isListeningDocument) {
            editor.getDocument().addDocumentListener(this);
        }
        changeListener.add(c);
    }

    /**
     * remove a change listener
     *
     * @param c the listener called when the text changes
     */
    public void removeListener(DocumentListener c) {
        changeListener.remove(c);
        if (changeListener.isEmpty()) {
            editor.getDocument().removeDocumentListener(this);
        }
    }

    /**
     * document listener implementation
     *
     * @param e the event
     */
    @Override
    public void documentUpdate(DocumentEvent e) {
        changeListener.forEach((a) -> a.documentUpdate(e));
    }

    /**
     * a textfield with automatic listener removal
     */
    private class MyTextfield extends TextFieldEx {

        public MyTextfield() {
        }

        public MyTextfield(String s) {
            super(s);
        }

        @Override
        public void dispose() {
            if (isListeningDocument) {
                getDocument().removeDocumentListener(BlockTextField.this);
            }
        }
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
