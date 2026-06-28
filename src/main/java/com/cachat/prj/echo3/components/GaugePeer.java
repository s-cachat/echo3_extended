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
 * Peer pour un composant Gauge
 *
 * @author user1
 */
public class GaugePeer extends AbstractComponentSynchronizePeer {

    private static final Service JS_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.GaugeComp", "com/cachat/prj/echo3/components/js/Gauge.js");
    public static final Service JS_GAUGE_LIB_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.GaugeLib", "com/cachat/prj/echo3/components/js/gauge.min.js");
   
    static {
        WebContainerServlet.getServiceRegistry().add(JS_GAUGE_LIB_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
    }

    public GaugePeer() {
        for (String prop : Gauge.getOutputProperties()) {
            addOutputProperty(prop);
        }
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "Gauge";
    }

    @Override
    public Class getComponentClass() {
        return Gauge.class;
    }
//
//    @Override
//    public Class getInputPropertyClass(String propertyName) {
//        return switch (propertyName) {
//            case Gauge.PROPERTY_MAX_VALUE,Gauge.PROPERTY_MIN_VALUE,Gauge.PROPERTY_VALUE ->
//                Double.class;
//            default ->
//                null;
//        };
//    }
//
//    @Override
//    public void storeInputProperty(Context context, Component component, String propertyName, int propertyIndex, Object newValue) {
//        switch (propertyName) {
//            case Gauge.PROPERTY_TIME_VALUE -> {
//                ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
//                clientUpdateManager.setComponentProperty(component, propertyName, newValue);
//            }
//        }
//    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(JS_GAUGE_LIB_SERVICE.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }

}
