package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.editor.BasicEditor;
import com.cachat.prj.echo3.editor.BasicEditor.SelectButton;
import nextapp.echo.app.Extent;

/**
 * fenetre de sélection de valeur
 *
 * @author scachat
 */
public abstract class SelectPopup extends BasicWindow {

    private final SelectButton selectButton;

    public SelectPopup(BaseApp app, BasicEditor.SelectButton selectButton, String prefixe, String domaine, Extent w, Extent h) {
        super(app, prefixe, domaine, w, h);
        this.selectButton = selectButton;
    }

    public void select(Object o) {
        selectButton.setValue(o);
        app.removeWindow(this);
    }
}
