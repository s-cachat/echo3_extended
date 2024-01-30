package org.informagen.echo.webcontainer;

import org.informagen.echo.app.StaticImage;

import nextapp.echo.app.Component;
import nextapp.echo.app.FillImage;


import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

public class StaticImagePeer extends AbstractComponentSynchronizePeer {


    private static final String REGISTRY_KEY = "Informagen.StaticImage";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/StaticImage.js";

    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
    }

    public StaticImagePeer() {
        super();
        
        // This essentially registers 'PropertyChange' events with the JavaScript client
        addOutputProperty(StaticImage.PROPERTY_IMAGEREFERENCE);
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
        return StaticImage.class;
    }

    //-------------------------------------------------------------------------------------
    //
    //  - getOutputProperty - used when sending property changes to the JavaScript client
    //

    public Object getOutputProperty(Context context, Component component, String propertyName, int propertyIndex) {
                        
        if (StaticImage.PROPERTY_IMAGEREFERENCE.equals(propertyName)) 
            return ((StaticImage)component).getImageReference();
        
        return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }

}