package org.informagen.echo.webcontainer;

import org.informagen.echo.app.ButtonWheel;
import org.informagen.echo.app.ScrollWheel;

import nextapp.echo.app.Component;
import nextapp.echo.app.FillImage;

import nextapp.echo.webcontainer.sync.component.AbstractButtonPeer;


import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;

import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ContentType;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.ResourceRegistry;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

public class ButtonWheelPeer extends AbstractComponentSynchronizePeer {

    private static final String REGISTRY_KEY = "Informagen.ButtonWheel";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/ButtonWheel.js";
    private static final String SCROLLER_MODEL_JAVASCRIPT_PATH = "org/informagen/echo/resource/ScrollerModel.js";

    static {
        String[] javaScriptPaths = new String[] { JAVASCRIPT_PATH, SCROLLER_MODEL_JAVASCRIPT_PATH };
        Service service = JavaScriptService.forResources(REGISTRY_KEY, javaScriptPaths);
        WebContainerServlet.getServiceRegistry().add(service);
        
        // Add the 'ScrollWheel' background image to the resource registry
        ResourceRegistry resources = WebContainerServlet.getResourceRegistry();        
        resources.addPackage("Informagen", "org/informagen/echo/resource/");
        resources.add("Informagen", "image/buttonWheel.gif", ContentType.IMAGE_GIF);
    }

    public ButtonWheelPeer() {
        super();
        
        // This registers a 'PropertyChange' event with the JavaScript client
        addOutputProperty(ButtonWheel.PROPERTY_DIAMETER);
        addOutputProperty(ButtonWheel.PROPERTY_MINIMUM);
        addOutputProperty(ButtonWheel.PROPERTY_MAXIMUM);
        addOutputProperty(ButtonWheel.PROPERTY_VALUE);
        // addOutputProperty(ButtonWheel.PROPERTY_NOSCROLL);
        addOutputProperty(ScrollWheel.PROPERTY_SENSITIVITY);

        // Handle JavaScript client events of type 'action'
        addEvent(new AbstractComponentSynchronizePeer.EventPeer("action", ButtonWheel.PROPERTY_ACTION_LISTENERS_CHANGED) {
            public boolean hasListeners(Context context, Component component) {
                return true;
            }
        });

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
        return ButtonWheel.class;
    }

    //-------------------------------------------------------------------------------------
    //
    //  - getOutputProperty - used when sending property changes to the JavaScript client
    //

    public Object getOutputProperty(Context context, Component component, String propertyName, int propertyIndex) {
                        
        if (ButtonWheel.PROPERTY_DIAMETER.equals(propertyName)) 
            return Integer.valueOf(((ButtonWheel)component).getDiameter());
        else if (ButtonWheel.PROPERTY_MAXIMUM.equals(propertyName)) 
            return Integer.valueOf(((ButtonWheel)component).getMaximum());
        else if (ButtonWheel.PROPERTY_MINIMUM.equals(propertyName)) 
            return Integer.valueOf(((ButtonWheel)component).getMinimum());
        else if (ButtonWheel.PROPERTY_VALUE.equals(propertyName)) 
            return Integer.valueOf(((ButtonWheel)component).getValue());
        // else if (ButtonWheel.PROPERTY_NOSCROLL.equals(propertyName)) 
        //     return new Boolean(true);
        else if (ScrollWheel.PROPERTY_SENSITIVITY.equals(propertyName)) 
            return Integer.valueOf(((ButtonWheel)component).getSensitivity());
  
        
        return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }


    //-------------------------------------------------------------------------------------
    //
    //  - getInputPropertyClass - used when receiving property changes from the JavaScript client
    //                              to determine how to treat the incoming object
    //
    //  - storeInputProperty - used when sending property changes to the JavaScript client to
    //                              forward changes to the target property
    //

    public Class getInputPropertyClass(String propertyName) {
       
        if (ScrollWheel.PROPERTY_ACTION_COMMAND_CHANGED.equals(propertyName)) 
            return String.class;
        else if (ScrollWheel.PROPERTY_VALUE.equals(propertyName)) 
            return Integer.class;
       
        return super.getInputPropertyClass(propertyName);
    }


    public void storeInputProperty(Context context, Component component,
                                   String propertyName, int propertyIndex, Object newValue) {

        ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
                              
        if (ScrollWheel.PROPERTY_ACTION_COMMAND_CHANGED.equals(propertyName)) 
            clientUpdateManager.setComponentProperty(component, ScrollWheel.PROPERTY_ACTION_COMMAND_CHANGED, newValue);
        else if (ScrollWheel.PROPERTY_VALUE.equals(propertyName)) 
            clientUpdateManager.setComponentProperty(component, ScrollWheel.PROPERTY_VALUE, newValue);
  }



}