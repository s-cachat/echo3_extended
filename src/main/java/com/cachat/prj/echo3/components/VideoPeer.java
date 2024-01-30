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
 * peer de video
 *
 * @author scachat
 */
public class VideoPeer extends AbstractComponentSynchronizePeer {

    private static final Service JS_DETECTIE_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.components.DetectIE", "com/cachat/prj/echo3/components/js/detectIE.js");
    private static final Service JS_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.components.Video", "com/cachat/prj/echo3/components/js/Video.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_DETECTIE_SERVICE);
    }

    public VideoPeer() {
        addOutputProperty(Video.PROPERTY_SHOW_CONTROL);
        addOutputProperty(Video.PROPERTY_TYPE);
        addOutputProperty(Video.PROPERTY_URL_IMAGE);
        addOutputProperty(Video.PROPERTY_URL_VIDEO);
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "Video";
    }

    @Override
    public Class getComponentClass() {
        return Video.class;
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(JS_DETECTIE_SERVICE.getId());
        serverMessage.addLibrary(Extended.JS_SERVICE.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }
}
