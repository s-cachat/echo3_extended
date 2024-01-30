package com.cachat.prj.echo3.ng.peers;

import com.cachat.prj.echo3.ng.BackButton;
import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;
import nextapp.echo.app.update.ClientUpdateManager;

/**
 *
 * @author scachat
 */
public class BackButtonPeer extends AbstractComponentSynchronizePeer {

    private static final Service JS_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.ng.js.BackButton", "com/cachat/prj/echo3/ng/js/BackButton.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
    }

    public BackButtonPeer() {
    }

    /**
     * @see
     * nextapp.echo.webcontainer.ComponentSynchronizePeer#getComponentClass()
     */
    @Override
    public Class getComponentClass() {
        return BackButton.class;
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "BackButton";
    }

    /**
     * @see
     * nextapp.echo.webcontainer.AbstractComponentSynchronizePeer#getInputPropertyClass(java.lang.String)
     */
    @Override
    public Class getInputPropertyClass(String propertyName) {
        if (BackButton.ACTION_EVENT.equals(propertyName)) {
            return String.class;
        }
        return null;
    }

    /**
     * @see
     * nextapp.echo.webcontainer.ComponentSynchronizePeer#storeInputProperty(Context,
     * Component, String, int, Object)
     */
    @Override
    public void storeInputProperty(Context context, Component component, String propertyName, int propertyIndex, Object newValue) {
        if (propertyName.equals(BackButton.ACTION_EVENT)) {
            if (newValue == null) {
                newValue = "";
            }
            ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
            clientUpdateManager.setComponentProperty(component, BackButton.ACTION_EVENT, newValue);
        }
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        serverMessage.addLibrary(JS_SERVICE.getId());
    }
}
