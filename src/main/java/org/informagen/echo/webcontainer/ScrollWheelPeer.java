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

public class ScrollWheelPeer extends ButtonWheelPeer {

    private static final String REGISTRY_KEY = "Informagen.ScrollWheel";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/ScrollWheel.js";

    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
        
        // Add the 'ScrollWheel' background image to the resource registry
        ResourceRegistry resources = WebContainerServlet.getResourceRegistry();        
        resources.addPackage("Informagen", "org/informagen/echo/resource/");
        resources.add("Informagen", "image/scrollWheel.gif", ContentType.IMAGE_GIF);
    }


    public ScrollWheelPeer() {
        super();
    }

    // Intialize and invoke superclass initialization -------------------------------------

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
        return ScrollWheel.class;
    }

}