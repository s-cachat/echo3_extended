package com.cachat.prj.echo3.base;

import nextapp.echo.app.Extent;

/**
 *
 * @author scachat
 */
public class HomeWindow extends BasicWindow {

    public HomeWindow(BaseApp app, String prefixe, String domaine, int w, int h) {
        super(app, prefixe, domaine, w, h);
    }

    public HomeWindow(BaseApp app, String prefixe, String domaine, Extent w, Extent h) {
        super(app, prefixe, domaine, w, h);
    }

    public HomeWindow(BaseApp app) {
        super(app, "app", "basic", new Extent(500), new Extent(500));
    }
}
