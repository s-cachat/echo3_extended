package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.blockeditor.BlockField;
import com.cachat.prj.echo3.ng.TextFieldEx;
import com.cachat.util.BeanTools;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import jakarta.validation.Validator;
import nextapp.echo.app.Extent;
import nextapp.echo.app.event.DocumentListener;

/**
 * un champs texte pour une valeur contenue dans une map
 *
 * @author scachat
 */
public class BlockMapTextField extends BlockField<TextFieldEx> {

    /**
     * if true, we added a document listener
     */
    private boolean isListeningDocument = false;
    /**
     * la valeur par défaut si le champs est null. peut être null
     */
    private Object defaultValue;
    /**
     * le libelle du champs
     */
    private String description;
    /**
     * le nom de la variable map
     */
    private String mapName;
    /**
     * le nom de la variable (dans la map)
     */
    private String mapKey;

    public BlockMapTextField(BlockField x) {
        super(x);
        this.mapKey = ((BlockMapTextField) x).mapKey;
        this.mapName = ((BlockMapTextField) x).mapName;
        this.description = ((BlockMapTextField) x).description;
        this.defaultValue = ((BlockMapTextField) x).defaultValue;
        editor = new TextFieldEx();
        label.setText(description);
        editor.setWidth(new Extent(100, Extent.PERCENT));
    }

    /**
     *
     * @param li le contexte
     * @param property le nom de la propriété de type Map&gt;String,Object&lt;
     * @param mapKey le nom de la variable (dans la map)
     * @param description le libelle du champs
     * @param defaultValue la valeur par défaut si le champs est null. peut être
     * null
     */
    public BlockMapTextField(LocalisedItem li, String property, String description, Object defaultValue) {
        super(li, property);
        this.description = description;
        this.defaultValue = defaultValue;
        editor = new TextFieldEx();
        editor.setWidth(new Extent(100, Extent.PERCENT));
        int i = property.lastIndexOf(".");
        label.setText(description);

        if (i < 0) {
            mapKey = null;
            mapName = null;
        } else {
            mapKey = property.substring(i + 1);
            mapName = property.substring(0, i);
        }
    }

    @Override
    public void copyObjectToUi() {
        Map m = (Map) BeanTools.getRaw(getParent().getCurrent(), mapName);
        if (m != null) {
            editor.setText(String.valueOf(m.get(mapKey)));
        }
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
            Map m = (Map) BeanTools.getRaw(getParent().getCurrent(), mapName);
            if (m == null) {
                return false;
            }
            Class valueType = (Class) ((ParameterizedType) BeanTools.getGenericType(getParent().getCurrent(), mapName)).getActualTypeArguments()[1];
            String t = editor.getText();
            Object v;
            if (t == null) {
                v = null;
            } else if (valueType.isAssignableFrom(Double.class)) {
                v = Double.valueOf(t.replace(',', '.'));
            } else if (valueType.isAssignableFrom(Float.class)) {
                v = Float.valueOf(t.replace(',', '.'));
            } else if (valueType.isAssignableFrom(Integer.class)) {
                v = Integer.valueOf(t);
            } else if (valueType.isAssignableFrom(Short.class)) {
                v = Short.valueOf(t);
            } else if (valueType.isAssignableFrom(Character.class)) {
                v = t.length() > 0 ? t.charAt(0) : null;
            } else if (valueType.isAssignableFrom(Boolean.class)) {
                v = Boolean.valueOf(t);
            } else if (valueType.isAssignableFrom(Byte.class)) {
                v = Byte.valueOf(t);
            } else if (valueType.isAssignableFrom(String.class)) {
                v = t;
            } else {
                logger.log(Level.SEVERE, "Don''t know how to handle {0}", valueType.getName());
                v = null;
            }
            m.put(mapKey, v);
            BeanTools.setRaw(getParent().getCurrent(), mapName, m);
            error.setText((String) null);
            return false;
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
     * get the text
     *
     * @return the field text
     */
    public String getText() {
        return editor.getText();
    }
}
