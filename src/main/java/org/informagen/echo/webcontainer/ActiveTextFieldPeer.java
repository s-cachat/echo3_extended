package org.informagen.echo.webcontainer;

import org.informagen.echo.app.ActiveTextField;


import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;

import nextapp.echo.webcontainer.ContentType;
import nextapp.echo.webcontainer.ResourceRegistry;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.WebContainerServlet;

import nextapp.echo.webcontainer.service.JavaScriptService;

import nextapp.echo.webcontainer.sync.component.TextComponentPeer;


public class ActiveTextFieldPeer extends TextComponentPeer {

    private static final String REGISTRY_KEY = "Informagen.ActiveTextField";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/ActiveTextField.js";

    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
        
        // Add the 'ActiveTextField' icons to the resource registry
        ResourceRegistry resources = WebContainerServlet.getResourceRegistry();        
        resources.addPackage("ActiveTextField", "org/informagen/echo/resource/");
        
        // PNG images for IE7 and all other browsers
        resources.add("ActiveTextField", "image/good.icon.png", ContentType.IMAGE_PNG);
        resources.add("ActiveTextField", "image/error.icon.png", ContentType.IMAGE_PNG);
        resources.add("ActiveTextField", "image/warning.icon.png", ContentType.IMAGE_PNG);
        resources.add("ActiveTextField", "image/empty.icon.png", ContentType.IMAGE_PNG);
        resources.add("ActiveTextField", "image/rightArrow.icon.png", ContentType.IMAGE_PNG);
        resources.add("ActiveTextField", "image/leftArrow.icon.png", ContentType.IMAGE_PNG);
        
        // GIF images for IE 6
        resources.add("ActiveTextField", "image/good.icon.gif", ContentType.IMAGE_GIF);
        resources.add("ActiveTextField", "image/error.icon.gif", ContentType.IMAGE_GIF);
        resources.add("ActiveTextField", "image/warning.icon.gif", ContentType.IMAGE_GIF);
        resources.add("ActiveTextField", "image/empty.icon.gif", ContentType.IMAGE_GIF);
        resources.add("ActiveTextField", "image/rightArrow.icon.gif", ContentType.IMAGE_GIF);
        resources.add("ActiveTextField", "image/leftArrow.icon.gif", ContentType.IMAGE_GIF);
    }

    public ActiveTextFieldPeer() {
        super();
        
        // This essentially registers 'PropertyChange' events with the JavaScript client
        addOutputProperty(ActiveTextField.PROPERTY_MESSAGE);
        addOutputProperty(ActiveTextField.PROPERTY_VALID_MESSAGE);
        addOutputProperty(ActiveTextField.PROPERTY_INVALID_MESSAGE);
    }

    // Intialize and invoke superclass initialization -------------------------------------

    public void init(Context context, Component component) {
        super.init(context, component);
        ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
        serverMessage.addLibrary(REGISTRY_KEY);
    }

     // Abstract methods from 'ComponentSynchronizePeer' -----------------------------------
    //  Return application class and registry unique name
    
   public Class getComponentClass() {
        return ActiveTextField.class;
    }

    public String getClientComponentType(boolean shortType) {
        return REGISTRY_KEY;
    }

    /**
     *  The WebContainer will invoke this peer method in order to respond to 'PropertyChange'
     *      events in the Java which need to be passed to the JavaScript client
     */
 
    public Object getOutputProperty(Context context, Component component, String propertyName, int propertyIndex) {
        
        if (propertyName.equals(ActiveTextField.PROPERTY_MESSAGE)) 
            return ((ActiveTextField)component).getMessage();
        else if (propertyName.equals(ActiveTextField.PROPERTY_VALID_MESSAGE)) 
            return ((ActiveTextField)component).getValidMessage();
        else if (propertyName.equals(ActiveTextField.PROPERTY_INVALID_MESSAGE)) 
            return ((ActiveTextField)component).getInvalidMessage();
        else 
            return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }

}