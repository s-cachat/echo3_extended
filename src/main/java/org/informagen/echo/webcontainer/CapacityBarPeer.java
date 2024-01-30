package org.informagen.echo.webcontainer;

// Custom Component
import org.informagen.echo.app.CapacityBar;

// Echo Component Framework
import nextapp.echo.app.Component;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;
import nextapp.echo.app.util.Context;

// Echo WebContainer Framework
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

import java.lang.StringBuffer;
import java.util.Collections;
import java.util.List;

public class CapacityBarPeer extends AbstractComponentSynchronizePeer {

    private static final String REGISTRY_KEY = "Informagen.CapacityBar";
    private static final String JAVASCRIPT_PATH = "org/informagen/echo/resource/CapacityBar.js";
    
    static {
        Service service = JavaScriptService.forResource(REGISTRY_KEY, JAVASCRIPT_PATH);
        WebContainerServlet.getServiceRegistry().add(service);
    }

    public CapacityBarPeer() {
        super();
        
        // This essentially registers 'PropertyChange' events with the JavaScript client
        addOutputProperty(CapacityBar.PROPERTY_WIDTH);
        addOutputProperty(CapacityBar.PROPERTY_HEIGHT);
        addOutputProperty(CapacityBar.PROPERTY_SHOW_TICKS);
        addOutputProperty(CapacityBar.PROPERTY_COLORS);
        addOutputProperty(CapacityBar.PROPERTY_VALUES);
        addOutputProperty(CapacityBar.PROPERTY_REFLECTIVITY);
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
        return CapacityBar.class;
    }

    public String getClientComponentType(boolean shortType) {
        return REGISTRY_KEY;
    }


    /**
     *  The WebContainer will invoke this peer method in order to respond to 'PropertyChange'
     *      events in the Java which need to be passed to the JavaScript client.
     *
     *  NB: We are sending the string equivalent to the JS client because as of this writing
     *        a 'SerialPropertyPeer' class has not been written for the Java Double class or
     *        Java List class.
     *      On the JS client side which uses an 'eval' function to properly converts this
     *        incoming string into the approriate JS datatype.
     */
 
    public Object getOutputProperty(Context context, Component component, String propertyName, int propertyIndex) {
        
        if (propertyName.equals(CapacityBar.PROPERTY_WIDTH)) {
            Extent width = ((CapacityBar)component).getWidth();
            return width.getValue(); 
        } else if (propertyName.equals(CapacityBar.PROPERTY_HEIGHT)) {
            Extent height = ((CapacityBar)component).getHeight();
            return height.getValue(); 
        } else if (propertyName.equals(CapacityBar.PROPERTY_SHOW_TICKS)) {
            boolean showTicks = ((CapacityBar)component).isShowTicks();
            return (showTicks ? Boolean.TRUE : Boolean.FALSE); 
        } else if (propertyName.equals(CapacityBar.PROPERTY_REFLECTIVITY)) {
            double reflectivity = ((CapacityBar)component).getReflectivity();
            return reflectivity; 
        } else if (propertyName.equals(CapacityBar.PROPERTY_CORNER_RADIUS)) {
            double cornerRadius = ((CapacityBar)component).getCornerRadius();
            return Double.toString(cornerRadius); 
        } else if (propertyName.equals(CapacityBar.PROPERTY_TICK_SPACING)) {
            Number tickSpacing = ((CapacityBar)component).getTickSpacing();
            return (tickSpacing == null) ? null : tickSpacing.toString();
        } else if (propertyName.equals(CapacityBar.PROPERTY_COLORS)) {
            List<Color> colors = ((CapacityBar)component).getColors();
            return createColorsArray(colors);
        } else if (propertyName.equals(CapacityBar.PROPERTY_VALUES)) {
            List<Number> values = ((CapacityBar)component).getValues();
            return createValuesArray(values);
        } else 
            return super.getOutputProperty(context, component, propertyName, propertyIndex);
    }
    
    
    private String createColorsArray(List<Color> colors) {
    
        if(colors == null)
            return null;
    
        if(colors.equals(Collections.EMPTY_LIST))
            return null;
    
        if(colors.isEmpty())
            return null;
    
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("[");
        
        for(Color color : colors) {
            buffer.append("'#");
            buffer.append(toHex(color.getRed()));
            buffer.append(toHex(color.getGreen()));
            buffer.append(toHex(color.getBlue()));
            buffer.append("'");          
            buffer.append(",");          
        }
    
        buffer.setLength(buffer.length() - 1);
        buffer.append("]");
        
        return buffer.toString();
    }

    private String toHex(int value) {
        return ((value < 16) ? "0": "") + Integer.toHexString(value);
    }
    

    private String createValuesArray(List<Number> values) {

        if(values == null)
            return null;
    
        if(values.equals(Collections.EMPTY_LIST))
            return null;
    
        if(values.isEmpty())
            return null;

        StringBuffer buffer = new StringBuffer();
        
        buffer.append("[");
        
        for(Number value : values) {
            buffer.append(value);
            buffer.append(",");          
        }
    
        buffer.setLength(buffer.length() - 1);
        buffer.append("]");
        
        return buffer.toString();
    }

}