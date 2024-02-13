package com.cachat.prj.echo3.components;

import static com.cachat.prj.echo3.components.GenericServices.JQUERY_SERVICE;
import java.util.Date;
import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 * Peer pour un composant DateSelect3
 *
 * @author user1
 */
public class DateSelect3Peer extends AbstractComponentSynchronizePeer {

    private static final Service BOOTSTRAP_DATEPICKER = JavaScriptService.forResource("com.cachat.prj.echo3.components.bootstrap_datepicker", "com/cachat/prj/echo3/components/js/bootstrap-datepicker.js");
    private static final Service JQUERY_TIMEPICKER = JavaScriptService.forResource("com.cachat.prj.echo3.components.jquery_timepicker", "com/cachat/prj/echo3/components/js/jquery.timepicker.js");
    private static final Service DATEPAIR = JavaScriptService.forResource("com.cachat.prj.echo3.components.datepair", "com/cachat/prj/echo3/components/js/datepair.js");
    private static final Service DATEPAIR_JQ = JavaScriptService.forResource("com.cachat.prj.echo3.components.datepair.jq", "com/cachat/prj/echo3/components/js/jquery.datepair.js");
    private static final Service JS_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.DateSelect3", "com/cachat/prj/echo3/components/js/DateSelect3.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JQUERY_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JQUERY_TIMEPICKER);
        WebContainerServlet.getServiceRegistry().add(BOOTSTRAP_DATEPICKER);
        WebContainerServlet.getServiceRegistry().add(DATEPAIR);
        WebContainerServlet.getServiceRegistry().add(DATEPAIR_JQ);
    }

    public DateSelect3Peer() {
        for (String prop : DateSelect3.getOutputProperties()) {
            addOutputProperty(prop);
        }
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "DateSelect3";
    }

    @Override
    public Class getComponentClass() {
        return DateSelect3.class;
    }

    @Override
    public Class getInputPropertyClass(String propertyName) {
        return switch (propertyName) {
            case DateSelect3.PROPERTY_START_DATE_VALUE ->
                String.class;
            case DateSelect3.PROPERTY_END_DATE_VALUE ->
                String.class;
            default ->
                null;
        };
    }

    @Override
    public void storeInputProperty(Context context, Component component, String propertyName, int propertyIndex, Object newValue) {
        ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
        clientUpdateManager.setComponentProperty(component, propertyName, newValue);
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(JQUERY_SERVICE.getId());
        serverMessage.addLibrary(JQUERY_TIMEPICKER.getId());
        serverMessage.addLibrary(BOOTSTRAP_DATEPICKER.getId());
        serverMessage.addLibrary(DATEPAIR.getId());
        serverMessage.addLibrary(DATEPAIR_JQ.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }
}
