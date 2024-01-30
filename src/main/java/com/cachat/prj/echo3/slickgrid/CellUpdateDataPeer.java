package com.cachat.prj.echo3.slickgrid;

import nextapp.echo.app.serial.SerialException;
import nextapp.echo.app.serial.SerialPropertyPeer;
import nextapp.echo.app.util.Context;

import org.w3c.dom.Element;

/**
 * <code>SerialPropertyPeer</code> for <code>BoxShadow</code> properties.
 *
 * @author sieskei (XSoft Ltd.)
 */
public class CellUpdateDataPeer implements SerialPropertyPeer {

    public Object toProperty(Context context, Class objectClass, Element propertyElement) throws SerialException {
        CellUpdateData x = new CellUpdateData();
        x.setRow(Integer.parseInt(propertyElement.getAttribute("row")));
        x.setCol(Integer.parseInt(propertyElement.getAttribute("col")));
        x.setField(propertyElement.getAttribute("field"));
        x.setValue(propertyElement.getAttribute("value"));
        return x;
    }

    @Override
    public void toXml(Context context, Class objectClass, Element propertyElement, Object propertyValue) throws SerialException {
        //nop
    }
}
