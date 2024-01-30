package org.informagen.echo.webcontainer;

import org.informagen.echo.app.Spinner;

import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

public class SpinnerPeer extends AbstractComponentSynchronizePeer {


    private static final String REGISTRY_KEY = "Informagen.Spinner";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/Spinner.js";

    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
    }

    public SpinnerPeer() {
        super();
        addOutputProperty(Spinner.PROPERTY_VALUE);
    }

    // Intialize and invoke superclass initialization -------------------------------------

    public void init(Context context, Component component) {
        super.init(context, component);
        ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
        serverMessage.addLibrary(REGISTRY_KEY);
    }

    // Abstract methods from 'ComponentSynchronizePeer' -----------------------------------
    //  Return application class and registry unique name
    
    public String getClientComponentType(boolean shortType) {
        return REGISTRY_KEY;
    }

    public Class getComponentClass() {
        return Spinner.class;
    }

    //-------------------------------------------------------------------------------------
    // Override the following three methods in order to work with local component properties
    //
    //  - getOutputProperty - used when sending property changes to the JavaScript client
    //  - getInputPropertyClass
    //  - storeInputProperty

    public Object getOutputProperty(Context context, Component component,
                                    String propertyName, int propertyIndex) {
                        
        if (Spinner.PROPERTY_VALUE.equals(propertyName)) 
            return ((Spinner)component).getValue();
        
        return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }


    public Class getInputPropertyClass(String propertyName) {
        
        if (Spinner.PROPERTY_VALUE.equals(propertyName)) 
            return Integer.class;
        
        return super.getInputPropertyClass(propertyName);
    }


    public void storeInputProperty(Context context, Component component,
                                   String propertyName, int propertyIndex, Object newValue) {

        ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
                              
        if (Spinner.PROPERTY_VALUE.equals(propertyName)) 
            clientUpdateManager.setComponentProperty(component, Spinner.PROPERTY_VALUE, newValue);
    }
}