package org.informagen.echo.webcontainer;

// Custom Component
import org.informagen.echo.app.RegExTextField;

// Echo Component Framework
import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;

// Echo WebContainer Framework
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

// Custom component's superclass
import org.informagen.echo.webcontainer.ActiveTextFieldPeer;


public class RegExTextFieldPeer extends ActiveTextFieldPeer {

    private static final String REGISTRY_KEY = "Informagen.RegExTextField";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/RegExTextField.js";

    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
    }

    public RegExTextFieldPeer() {
        super();
        
        // This essentially registers 'PropertyChange' events with the JavaScript client
        addOutputProperty(RegExTextField.PROPERTY_FILTER);
        addOutputProperty(RegExTextField.PROPERTY_REGEX);
    }


    public void init(Context context, Component component) {
        super.init(context, component);
        ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
        serverMessage.addLibrary(REGISTRY_KEY);
    }

    // Abstract methods from 'ComponentSynchronizePeer' -----------------------------------
    //  Return application class and registry unique name
    
   public Class getComponentClass() {
        return RegExTextField.class;
    }

    public String getClientComponentType(boolean shortType) {
        return REGISTRY_KEY;
    }
 
    /**
     *  The WebContainer will invoke this peer method in order to respond to 'PropertyChange'
     *      events in the Java which need to be passed to the JavaScript client
     */
 
    public Object getOutputProperty(Context context, Component component, String propertyName, int propertyIndex) {
        
        if (propertyName.equals(RegExTextField.PROPERTY_FILTER)) 
            return ((RegExTextField)component).getFilter();
        else if (propertyName.equals(RegExTextField.PROPERTY_REGEX))
            return ((RegExTextField)component).getRegEx();
        else 
            return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }
}