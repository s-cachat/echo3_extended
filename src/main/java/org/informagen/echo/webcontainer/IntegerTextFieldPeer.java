package org.informagen.echo.webcontainer;

// Custom Component
import org.informagen.echo.app.IntegerTextField;

// Echo Component Framework
import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;

// Echo WebContainer Framework
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

// Custom component's superclass
import org.informagen.echo.webcontainer.ActiveTextFieldPeer;


public class IntegerTextFieldPeer extends ActiveTextFieldPeer {

    private static final String REGISTRY_KEY = "Informagen.IntegerTextField";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/IntegerTextField.js";

    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
    }

    public IntegerTextFieldPeer() {
        super();
        
        // This essentially registers 'PropertyChange' events with the JavaScript client
        addOutputProperty(IntegerTextField.PROPERTY_MINIMUM_VALUE);
        addOutputProperty(IntegerTextField.PROPERTY_MAXIMUM_VALUE);
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
        return IntegerTextField.class;
    }

    public String getClientComponentType(boolean shortType) {
        return REGISTRY_KEY;
    }

    /**
     *  The WebContainer will invoke this peer method in order to respond to 'PropertyChange'
     *      events in the Java which need to be passed to the JavaScript client.
     */
 
    public Object getOutputProperty(Context context, Component component, String propertyName, int propertyIndex) {
       
        if (propertyName.equals(IntegerTextField.PROPERTY_MINIMUM_VALUE)) {
            int value = ((IntegerTextField)component).getMinimumValue();
            return Integer.valueOf(value);
        } else if (propertyName.equals(IntegerTextField.PROPERTY_MAXIMUM_VALUE)) {
            int value = ((IntegerTextField)component).getMaximumValue();
            return Integer.valueOf(value);
        } else 
            return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }

}