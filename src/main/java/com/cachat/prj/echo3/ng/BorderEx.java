/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cachat.prj.echo3.ng;

import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;

/**
 *
 * @author scachat
 */
public class BorderEx extends Border {

    public BorderEx(int sizePx, Color color, int style) {
        super(sizePx, color, style);
    }

    public BorderEx(Extent size, Color color, int style) {
        super(size, color, style);
    }

    public BorderEx(Side[] sides) {
        super(sides);
    }
}
