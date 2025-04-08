package com.cachat.prj.echo3.synoptique;

import nextapp.echo.app.serial.SerialException;
import nextapp.echo.app.serial.SerialPropertyPeer;
import nextapp.echo.app.util.Context;
import org.w3c.dom.Element;

/**
 *
 * @author scachat
 */
public class SynModifiedEventPeer implements SerialPropertyPeer {

    @Override
    public Object toProperty(Context context, Class objectClass, Element propertyElement) throws SerialException {
        SynModifiedEvent x = new SynModifiedEvent();
        x.setAngle(parseDouble(propertyElement, "angle"));
        x.setHeight(parseDouble(propertyElement, ("height")));
        x.setWidth(parseDouble(propertyElement, ("width")));
        x.setLeft(parseDouble(propertyElement, ("left")));
        x.setTop(parseDouble(propertyElement, ("top")));
        x.setUid(propertyElement.getAttribute("uid"));
        return x;
    }

    private static Double parseDouble(Element propertyElement, String name) throws NumberFormatException {
        final String attribute = propertyElement.getAttribute(name);
        if (attribute == null || !attribute.matches("\\d+(\\.\\d*)?")) {
            return null;
        } else {
            return Double.valueOf(attribute);
        }
    }

    @Override
    public void toXml(Context context, Class objectClass, Element propertyElement, Object propertyValue) throws SerialException {
        //nop
    }
}
