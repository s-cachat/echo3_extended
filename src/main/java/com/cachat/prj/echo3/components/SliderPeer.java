package com.cachat.prj.echo3.components;

import com.cachat.prj.echo3.ng.peers.Extended;
import nextapp.echo.app.Component;
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
public class SliderPeer extends AbstractComponentSynchronizePeer {

    private static final Service JS_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.components.Slider", "com/cachat/prj/echo3/components/js/Slider.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
    }

    public SliderPeer() {
        addOutputProperty(Slider.PROPERTY_MAX_VALUE);
        addOutputProperty(Slider.PROPERTY_MIN_VALUE);
        addOutputProperty(Slider.PROPERTY_STEP_VALUE);
        addOutputProperty(Slider.PROPERTY_VALUE);
        addEvent(new EventPeer(Slider.ON_CHANGE_ACTION, Slider.CHANGE_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((Slider) c).hasChangeListeners();
            }
        });
        addEvent(new EventPeer(Slider.ON_INPUT_ACTION, Slider.INPUT_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((Slider) c).hasInputListeners();
            }
        });
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "Slider";
    }

    @Override
    public Class getComponentClass() {
        return Slider.class;
    }

    @Override
    public Class getInputPropertyClass(String propertyName) {
        switch (propertyName) {
            case Slider.PROPERTY_VALUE:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public void storeInputProperty(Context context, Component component,
            String propertyName, int propertyIndex, Object newValue) {
        switch (propertyName) {
            case Slider.PROPERTY_VALUE:
                ClientUpdateManager clientUpdateManager
                        = (ClientUpdateManager) context.get(ClientUpdateManager.class);
                clientUpdateManager.setComponentProperty(component, propertyName, newValue);
        }
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
