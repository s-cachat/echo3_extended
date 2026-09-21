package com.cachat.prj.echo3.components;

import static com.cachat.prj.echo3.components.GenericServices.JQUERY_SERVICE;
import static com.cachat.prj.echo3.components.GenericServices.JQUERY_UI_SERVICE;
import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 * Peer pour un composant InstantSelect3
 *
 * @author user1
 */
public class InstantSelect3Peer extends AbstractComponentSynchronizePeer {

    private static final Service JQUERY_TIMEPICKER = JavaScriptService.forResource("com.cachat.prj.echo3.components.jquery_ui_timepicker_addon", "com/cachat/prj/echo3/components/js/jquery-ui-timepicker-addon.js");
    private static final Service JQUERY_DATEPICKER = JavaScriptService.forResource("com.cachat.prj.echo3.components.jquery_ui_datepicker", "com/cachat/prj/echo3/components/js/jquery-ui-datepicker.js");
    private static final Service JS_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.InstantSelect3", "com/cachat/prj/echo3/components/js/InstantSelect3.js");
    public static final String CHANGE_LISTENERS_CHANGED_PROPERTY = "changeListeners";
    public static final String ON_CHANGE_ACTION = "change";

    static {
        WebContainerServlet.getServiceRegistry().add(JQUERY_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JQUERY_UI_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JQUERY_DATEPICKER);
        WebContainerServlet.getServiceRegistry().add(JQUERY_TIMEPICKER);
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);        
    }

    public InstantSelect3Peer() {
        for (String prop : InstantSelect3.getOutputProperties()) {
            addOutputProperty(prop);
        }
        addEvent(new EventPeer(InstantSelect3Peer.ON_CHANGE_ACTION, InstantSelect3Peer.CHANGE_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((InstantSelect3) c).hasActionListeners();
            }
        });
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "InstantSelect3";
    }

    @Override
    public Class getComponentClass() {
        return InstantSelect3.class;
    }

    @Override
    public Class getInputPropertyClass(String propertyName) {
        return switch (propertyName) {
            case InstantSelect3.PROPERTY_DATE_VALUE ->
                String.class;
            default ->
                null;
        };
    }

    @Override
    public void storeInputProperty(Context context, Component component, String propertyName, int propertyIndex, Object newValue) {
        switch (propertyName) {
            case InstantSelect3.PROPERTY_DATE_VALUE:
                ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
                clientUpdateManager.setComponentProperty(component, propertyName, newValue);
        }
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(JQUERY_SERVICE.getId());
        serverMessage.addLibrary(JQUERY_UI_SERVICE.getId());
        serverMessage.addLibrary(JQUERY_DATEPICKER.getId());
        serverMessage.addLibrary(JQUERY_TIMEPICKER.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }

}
