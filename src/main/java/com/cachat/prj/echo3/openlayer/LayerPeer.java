package com.cachat.prj.echo3.openlayer;

import com.cachat.prj.echo3.ng.peers.Extended;
import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 *
 * @author scachat
 */
public class LayerPeer extends AbstractComponentSynchronizePeer {

    // Create a JavaScriptService containing the SpinButton JavaScript code.
    private static final Service JS_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.openlayer.Layer", "com/cachat/prj/echo3/openlayer/Layer.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
    }

    public LayerPeer() {
        addOutputProperty(Layer.VISIBILITY_PROPERTY);
        addOutputProperty(Layer.TYPE_PROPERTY);
        addOutputProperty(Layer.LABEL_PROPERTY);
        addOutputProperty(Layer.NAME_PROPERTY);
        addOutputProperty(Layer.KEY_PROPERTY);
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        // Return client-side component type name.
        return "Layer";
    }

    @Override
    public Class getComponentClass() {
        // Return server-side Java class.
        return Layer.class;
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
