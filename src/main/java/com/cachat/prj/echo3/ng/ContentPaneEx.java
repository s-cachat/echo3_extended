package com.cachat.prj.echo3.ng;

import com.cachat.prj.echo3.ng.able.Positionable;
import nextapp.echo.app.Component;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.IllegalChildException;

/**
 *
 * @author scachat
 */
public class ContentPaneEx extends ContentPane {

    ContainerEx nonPaneContainer;

    public ContentPaneEx() {
        super.add(nonPaneContainer = new ContainerEx(0,0,0,0,null,null), -1);
    }

    @Override
    public boolean isValidChild(Component child) {
        return true;
    }

    @Override
    public void add(Component c) {
        if (c instanceof ContainerEx) {
            nonPaneContainer.add(c);
        } else {
            super.add(c, -1);
        }
    }

    @Override
    public void add(Component c, int n) throws IllegalChildException {
        if (c instanceof ContainerEx) {
            nonPaneContainer.add(c, n);
        } else {
            super.add(c, n);
        }
    }

    /**
     * teste si ce composant est vide
     *
     * @return true si il est vide (si il ne contient que son nonPaneContaner
     * interne)
     */
    public boolean isEmpty() {
        int a = getComponentCount();
        int b = nonPaneContainer.getComponentCount();
        boolean res = (a <= 1 && b == 0);
        return res;
    }
}
