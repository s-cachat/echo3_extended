package com.cachat.prj.echo3.components;

import static com.cachat.prj.echo3.components.GenericServices.JQUERY_SERVICE;
import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 * Peer pour un composant TimeSelect
 *
 * @author user1
 */
public class TimeSelectPeer extends AbstractComponentSynchronizePeer {

    private static final Service JS_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.TimeSelect", "com/cachat/prj/echo3/components/js/TimeSelect.js");
    public static final Service JS_TIME_PICKER_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.TimePicker", "com/cachat/prj/echo3/components/js/jquery-clock-timepicker.js");
    public static final String CHANGE_LISTENERS_CHANGED_PROPERTY = "changeListeners";
    public static final String ON_CHANGE_ACTION = "change";

    static {
        WebContainerServlet.getServiceRegistry().add(JS_TIME_PICKER_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
    }

    public TimeSelectPeer() {
        for (String prop : TimeSelect.getOutputProperties()) {
            addOutputProperty(prop);
        }
        addEvent(new EventPeer(TimeSelectPeer.ON_CHANGE_ACTION, TimeSelectPeer.CHANGE_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((TimeSelect) c).hasActionListeners();
            }
        });
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "TimeSelect";
    }

    @Override
    public Class getComponentClass() {
        return TimeSelect.class;
    }

    @Override
    public Class getInputPropertyClass(String propertyName) {
        return switch (propertyName) {
            case TimeSelect.PROPERTY_TIME_VALUE ->
                String.class;
            default ->
                null;
        };
    }

    @Override
    public void storeInputProperty(Context context, Component component, String propertyName, int propertyIndex, Object newValue) {
        switch (propertyName) {
            case TimeSelect.PROPERTY_TIME_VALUE -> {
                ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
                clientUpdateManager.setComponentProperty(component, propertyName, newValue);
            }
        }
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(JQUERY_SERVICE.getId());
        serverMessage.addLibrary(JS_TIME_PICKER_SERVICE.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }

}
