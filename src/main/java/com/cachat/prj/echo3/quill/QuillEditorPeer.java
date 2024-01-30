package com.cachat.prj.echo3.quill;

import com.cachat.prj.echo3.ng.peers.Extended;
import nextapp.echo.app.Component;
import nextapp.echo.app.text.TextComponent;
import nextapp.echo.app.update.ClientUpdateManager;
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
public class QuillEditorPeer extends AbstractComponentSynchronizePeer {

    private static final Service JS_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.quill.QuillEditor", "com/cachat/prj/echo3/quill/QuillEditor.js");
    private static final Service QUILL_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.quill.quill.min", "com/cachat/prj/echo3/quill/quill.min.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(QUILL_SERVICE);
    }

    public QuillEditorPeer() {
        addOutputProperty(TextComponent.TEXT_CHANGED_PROPERTY);
    }

    /**
     * @see
     * nextapp.echo.webcontainer.ComponentSynchronizePeer#getComponentClass()
     */
    @Override
    public Class getComponentClass() {
        return QuillEditor.class;
    }

    /**
     * @see
     * nextapp.echo.webcontainer.AbstractComponentSynchronizePeer#getInputPropertyClass(java.lang.String)
     */
    @Override
    public Class getInputPropertyClass(String propertyName) {
        if (QuillEditor.TEXT_CHANGED_PROPERTY.equals(propertyName)) {
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
        if (propertyName.equals(TextComponent.TEXT_CHANGED_PROPERTY)) {
            if (newValue == null) {
                newValue = "";
            }
            ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
                clientUpdateManager.setComponentProperty(component, TextComponent.TEXT_CHANGED_PROPERTY, newValue);
        }
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "QuillEditor";
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        serverMessage.addLibrary(JS_SERVICE.getId());

// Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(QUILL_SERVICE.getId());
    }

}
