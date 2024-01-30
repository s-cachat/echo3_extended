package org.informagen.echo.webcontainer;

import org.informagen.echo.app.ReflectedImage;

import nextapp.echo.app.Component;

import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

public class ReflectedImagePeer extends StaticImagePeer {


    private static final String REGISTRY_KEY = "Informagen.ReflectedImage";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/ReflectedImage.js";

    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
    }

    public ReflectedImagePeer() {
        super();
         // This essentially registers 'PropertyChange' events with the JavaScript client
        addOutputProperty(ReflectedImage.PROPERTY_GAP);
        addOutputProperty(ReflectedImage.PROPERTY_REFECTIVITY);
        addOutputProperty(ReflectedImage.PROPERTY_REFLECTED_HEIGHT);
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
        return ReflectedImage.class;
    }

    //-------------------------------------------------------------------------------------
    //
    //  - getOutputProperty - used when sending property changes to the JavaScript client
    //

    public Object getOutputProperty(Context context, Component component, String propertyName, int propertyIndex) {
 
         if (ReflectedImage.PROPERTY_GAP.equals(propertyName)) {
            return Integer.valueOf(((ReflectedImage)component).getGap());
         } else if (propertyName.equals(ReflectedImage.PROPERTY_REFECTIVITY)) {
            double value = ((ReflectedImage)component).getReflectivity();
            return value;
        } else if (propertyName.equals(ReflectedImage.PROPERTY_REFLECTED_HEIGHT)) {
            double value = ((ReflectedImage)component).getReflectedHeight();
            return value;
        }
                              
        return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }

}