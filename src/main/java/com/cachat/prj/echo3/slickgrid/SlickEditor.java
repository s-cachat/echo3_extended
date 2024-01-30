package com.cachat.prj.echo3.slickgrid;

/**
 * les éditeurs
 *
 * @author scachat
 */
public enum SlickEditor {
    TEXT("Slick.Editors.Text"),
    INTEGER("Slick.Editors.Integer"),
    DATE("Slick.Editors.Date"),
    YESNOSELECT("Slick.Editors.YesNoSelect"),
    CHECKBOX("Slick.Editors.Checkbox"),
    PERCENTCOMPLETE("Slick.Editors.PercentComplete"),
    LONGTEXT("Slick.Editors.LongText");
    private final String editorName;

    SlickEditor(String editorName) {
        this.editorName = editorName;
    }

    public String getEditorName() {
        return editorName;
    }

}
