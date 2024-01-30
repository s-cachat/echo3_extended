package com.cachat.prj.echo3.ng.peers;

import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

public class Extended {

    public static final Service JS_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.ng.Extended", "com/cachat/prj/echo3/ng/js/Extended.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
    }
}
