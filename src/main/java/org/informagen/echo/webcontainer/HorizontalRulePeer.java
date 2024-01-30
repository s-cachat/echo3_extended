package org.informagen.echo.webcontainer;

// This peer's Echo component
import org.informagen.echo.app.HorizontalRule;

import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;

import nextapp.echo.app.util.Context;


// Echo - WebContainer
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;


/**
 * <code>HorizontalRulePeer</code>
 */

public class HorizontalRulePeer extends AbstractComponentSynchronizePeer {

    private static final String REGISTRY_KEY = "Informagen.HorizontalRule";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/HorizontalRule.js";

    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
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
        return HorizontalRule.class;
    }

    public String getClientComponentType(boolean shortType) {
        return REGISTRY_KEY;
    }
        
}