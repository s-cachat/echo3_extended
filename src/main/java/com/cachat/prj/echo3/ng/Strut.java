package com.cachat.prj.echo3.ng;

import static nextapp.echo.app.Position.RELATIVE;

/**
 * un bloc de taille fixe utilisé pour la mise en page
 *
 * @author scachat
 */
public class Strut extends ContainerEx {

    public Strut(int width, int height) {
        setWidth(width);
        setHeight(height);
        setPosition(RELATIVE);
    }

}
