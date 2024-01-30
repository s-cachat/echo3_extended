package com.cachat.prj.echo3.editor;

import com.cachat.util.BeanTools;
import java.util.Map;
import nextapp.echo.app.Label;
import nextapp.echo.app.text.TextComponent;

public class BasicEditorMLItem {

    /**
     * le composant texte
     */
    private TextComponent comp;
    /**
     * le label pour le texte de reference
     */
    private Label label;
    /**
     * le type d'objet
     */
    private Class type;
    /**
     * la propriete du pere, de type List
     */
    private String prop;
    /**
     * la propriete du fils, contenant le texte
     */
    private String propText;
    /**
     * la map
     */
    private Map map;
    BasicEditor editor;

    public BasicEditorMLItem(TextComponent comp, Label label, Class type, String prop, String propText,
            BasicEditor editor) {
        this.editor = editor;
        this.comp = comp;
        this.label = label;
        this.type = type;
        this.prop = prop;
        this.propText = propText;
    }

    public Map getMap() {
        if (map == null) {
            map = (Map) BeanTools.getRaw(editor.cur, prop);
        }
        return map;
    }

    public TextComponent getComp() {
        return comp;
    }

    public String getProp() {
        return prop;
    }

    public String getPropText() {
        return propText;
    }

    public Class getType() {
        return type;
    }

    public Label getLabel() {
        return label;
    }
}
