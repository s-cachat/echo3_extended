package com.cachat.prj.echo3.components;

import com.cachat.prj.echo3.ng.peers.Extended;
import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 * peer de directhtml
 *
 * @author scachat
 */
public class DirectHtmlPeer extends AbstractComponentSynchronizePeer {

    // Create a JavaScriptService containing the SpinButton JavaScript code.
    private static final Service JS_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.components.DirectHtml", "com/cachat/prj/echo3/components/js/DirectHtml.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
    }

    public DirectHtmlPeer() {
        addOutputProperty(DirectHtml.PROPERTY_TEXT);
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "DirectHtml";
    }

    @Override
    public Class getComponentClass() {
        return DirectHtml.class;
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(Extended.JS_SERVICE.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }
}
