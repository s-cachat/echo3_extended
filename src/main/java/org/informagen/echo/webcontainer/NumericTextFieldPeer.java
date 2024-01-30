package org.informagen.echo.webcontainer;

// Custom Component
import org.informagen.echo.app.NumericTextField;

// Echo Component Framework
import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;

// Echo WebContainer Framework
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

// Custom component's superclass
import org.informagen.echo.webcontainer.IntegerTextFieldPeer;


public class NumericTextFieldPeer extends IntegerTextFieldPeer {

    private static final String REGISTRY_KEY = "Informagen.NumericTextField";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/NumericTextField.js";
    
    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
    }

    public NumericTextFieldPeer() {
        super();
        
        // This essentially registers 'PropertyChange' events with the JavaScript client
        addOutputProperty(NumericTextField.PROPERTY_MINIMUM_VALUE);
        addOutputProperty(NumericTextField.PROPERTY_MAXIMUM_VALUE);
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
        return NumericTextField.class;
    }

    public String getClientComponentType(boolean shortType) {
        return REGISTRY_KEY;
    }


    /**
     *  The WebContainer will invoke this peer method in order to respond to 'PropertyChange'
     *      events in the Java which need to be passed to the JavaScript client.
     *
     *  NB: We are sending the string equivalent to the JS client because as of this writing
     *      a 'SerialPropertyPeer' class has not been written for the Java Double class.
     *      There is a hack on the JS client side which uses an 'eval' function to properly
     *      converts the incoming string into the approriate JS datatype, in this case a
     *      JavaScript 'float' object.
     */
 
    public Object getOutputProperty(Context context, Component component, String propertyName, int propertyIndex) {
        
        if (propertyName.equals(NumericTextField.PROPERTY_MINIMUM_VALUE)) {
            double value = ((NumericTextField)component).getMinimumValue();
            return Double.valueOf(value);
        } else if (propertyName.equals(NumericTextField.PROPERTY_MAXIMUM_VALUE)) {
            double value = ((NumericTextField)component).getMaximumValue();
            return Double.valueOf(value);
        } else 
            return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }

}