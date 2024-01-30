/*
 */

package org.informagen.echo.app;

import org.informagen.echo.app.ButtonWheel;

public class ScrollWheel extends ButtonWheel {


    /** Default constructor. */

    public ScrollWheel() {
        this(150);
    }
    
    /**
    * Create a new instance using the specified image reference.
    *
    * @param int The outer diameter of the scroll wheel
    */

    public ScrollWheel(final int diameter ) {
        super();
        setNoScroll(false);
        setDiameter(diameter);
    }

    
}
