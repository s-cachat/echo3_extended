package org.informagen.echo.webcontainer;

import org.informagen.echo.app.SyncTextField;

import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;

import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

import nextapp.echo.webcontainer.sync.component.TextFieldPeer;

public class SyncTextFieldPeer extends TextFieldPeer {

    private static final String REGISTRY_KEY = "Informagen.SyncTextField";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/SyncTextField.js";

    static {
        WebContainerServlet.getServiceRegistry().add(JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH));
    }

    public SyncTextFieldPeer() {
        super();
        
        // This essentially registers 'PropertyChange' events with the JavaScript client
        addOutputProperty(SyncTextField.PROPERTY_SYNC_DELAY);
    }

    public void init(Context context, Component component) {
            super.init(context, component);
            
            ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
            serverMessage.addLibrary(REGISTRY_KEY);
    }

    public Class getComponentClass() {
            return SyncTextField.class;
    }

    public String getClientComponentType(boolean shortType) {
            return REGISTRY_KEY;
    }

    /**
     *  The WebContainer will invoke this peer method in order to respond to 'PropertyChange'
     *      events in the Java which need to be passed to the JavaScript client
     */
 
    public Object getOutputProperty(Context context, Component component, String propertyName, int propertyIndex) {
        
        if (propertyName.equals(SyncTextField.PROPERTY_SYNC_DELAY)) 
            return Integer.valueOf(((SyncTextField)component).getSyncDelay());
        else 
            return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }

}