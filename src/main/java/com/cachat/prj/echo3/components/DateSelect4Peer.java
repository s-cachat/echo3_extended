package com.cachat.prj.echo3.components;

import static com.cachat.prj.echo3.components.GenericServices.JQUERY_SERVICE;
import static com.cachat.prj.echo3.components.GenericServices.JQUERY_UI_SERVICE;
import static com.cachat.prj.echo3.components.TimeSelectPeer.JS_TIME_PICKER_SERVICE;
import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 * Peer pour un composant DateSelect4
 *
 * @author user1
 */
public class DateSelect4Peer extends AbstractComponentSynchronizePeer {

    private static final Service JS_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.DateSelect4", "com/cachat/prj/echo3/components/js/DateSelect4.js");
    private static final Service JS_FLAT_PICKER_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.FlatPicker", "com/cachat/prj/echo3/components/js/flatpicker.min.js");
    private static final Service JS_FLAT_PICKER_FR_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.FlatPicker.fr", "com/cachat/prj/echo3/components/js/flatpicker_fr.js");
    private static final Service JS_FLAT_PICKER_EN_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.FlatPicker.en", "com/cachat/prj/echo3/components/js/flatpicker_en.js");
    private static final Service JS_FLAT_PICKER_ES_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.FlatPicker.es", "com/cachat/prj/echo3/components/js/flatpicker_es.js");
    private static final Service JS_FLAT_PICKER_DE_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.FlatPicker.de", "com/cachat/prj/echo3/components/js/flatpicker_de.js");
    private static final Service JS_FLAT_PICKER_IT_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.FlatPicker.it", "com/cachat/prj/echo3/components/js/flatpicker_it.js");

    public static final String CHANGE_LISTENERS_CHANGED_PROPERTY = "changeListeners";
    public static final String ON_CHANGE_ACTION = "change";

    static {
        WebContainerServlet.getServiceRegistry().add(JS_FLAT_PICKER_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_FLAT_PICKER_DE_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_FLAT_PICKER_EN_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_FLAT_PICKER_ES_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_FLAT_PICKER_FR_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_FLAT_PICKER_IT_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
    }

    public DateSelect4Peer() {
        for (String prop : DateSelect4.getOutputProperties()) {
            addOutputProperty(prop);
        }
        addEvent(new EventPeer(DateSelect4Peer.ON_CHANGE_ACTION, DateSelect4Peer.CHANGE_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((DateSelect4) c).hasActionListeners();
            }
        });
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "DateSelect4";
    }

    @Override
    public Class getComponentClass() {
        return DateSelect4.class;
    }

    @Override
    public Class getInputPropertyClass(String propertyName) {
        return switch (propertyName) {
            case DateSelect4.PROPERTY_DATE_VALUE ->
                String.class;
            default ->
                null;
        };
    }

    @Override
    public void storeInputProperty(Context context, Component component, String propertyName, int propertyIndex, Object newValue) {
        switch (propertyName) {
            case DateSelect4.PROPERTY_DATE_VALUE:
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
        serverMessage.addLibrary(JS_FLAT_PICKER_SERVICE.getId());
        serverMessage.addLibrary(JS_FLAT_PICKER_DE_SERVICE.getId());
        serverMessage.addLibrary(JS_FLAT_PICKER_EN_SERVICE.getId());
        serverMessage.addLibrary(JS_FLAT_PICKER_ES_SERVICE.getId());
        serverMessage.addLibrary(JS_FLAT_PICKER_FR_SERVICE.getId());
        serverMessage.addLibrary(JS_FLAT_PICKER_IT_SERVICE.getId());
        serverMessage.addLibrary(JQUERY_SERVICE.getId());
        serverMessage.addLibrary(JS_TIME_PICKER_SERVICE.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }

}
