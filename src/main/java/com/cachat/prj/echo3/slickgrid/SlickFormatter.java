package com.cachat.prj.echo3.slickgrid;

/**
 * les éditeurs
 *
 * @author scachat
 */
public enum SlickFormatter {
    YESNO("Slick.Formatters.YesNo"),
    CHECKMARK("Slick.Formatters.Checkmark"),
    PERCENTCOMPLETE("Slick.Formatters.PercentComplete"),
    PERCENTCOMPLETEBAR("Slick.Formatters.PercentCompleteBar");
    private final String editorName;

    SlickFormatter(String editorName) {
        this.editorName = editorName;
    }

    public String getFormatterName() {
        return editorName;
    }

}
