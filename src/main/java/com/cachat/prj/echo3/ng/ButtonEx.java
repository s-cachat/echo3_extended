package com.cachat.prj.echo3.ng;

import nextapp.echo.app.Button;
import nextapp.echo.app.ImageReference;

/**
 *
 * @author scachat
 */
public class ButtonEx extends Button {

    public ButtonEx() {
    }

    public ButtonEx(String text) {
        super(text);
    }

    public ButtonEx(ImageReference icon) {
        super(icon);
    }

    public ButtonEx(String text, ImageReference icon) {
        super(text, icon);
    }
}
