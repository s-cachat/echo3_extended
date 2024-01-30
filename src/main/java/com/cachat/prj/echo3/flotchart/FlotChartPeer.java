package com.cachat.prj.echo3.flotchart;

import com.cachat.prj.echo3.base.BaseAppServlet;
import com.cachat.prj.echo3.ng.peers.Extended;
import com.cachat.prj.echo3.outils.LocalResource;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;

/**
 *
 * @author scachat
 */
public class FlotChartPeer extends AbstractComponentSynchronizePeer {
    // Create a JavaScriptService containing the SpinButton JavaScript code.

    private static final Service JS_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.FlotChart", "/com/cachat/prj/echo3/flotchart/FlotChart.js");
    private static final Service JQUERY_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.jquery", "/com/cachat/prj/echo3/flotchart/js/jquery-1.11.2.min.js");
    private static final Service FLOT_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.min.js");
    private static final Service FLOT_TIME_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.time", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.time.min.js");
    private static final Service FLOT_STACK_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.stack", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.stack.min.js");
    private static final Service FLOT_PIE_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.pie", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.pie.min.js");
    private static final Service FLOT_SELECTION_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.selection", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.selection.min.js");
    private static final Service FLOT_NAVIGATE_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.navigate", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.navigate.min.js");
    private static final Service FLOT_CROSSHAIR_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.crosshair", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.crosshair.min.js");
    private static final Service FLOT_AXISLABEL_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.axisLabel", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.axislabel.js");
    private static final Service FLOT_SAVE_AS_IMAGE_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.saveAsImage", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.saveAsImage.js");
    private static final Service FLOT_ORDER_BARS_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.orderBars", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.orderBars.js");
    private static final Service FLOT_TOOLTIP_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.tooltip", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.tooltip.min.js");
    private static final Service FLOT_CATEGORIES_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.categories", "/com/cachat/prj/echo3/flotchart/js/jquery.flot.categories.min.js");
    private static final Service EXCANVAS_SERVICE = LocalResource.forJsResource(FlotChartPeer.class,"com.cachat.prj.echo3.flotchart.flot.excanvas", "/com/cachat/prj/echo3/flotchart/js/excanvas.min.js");
    private static final ObjectMapper json;

    static {
        WebContainerServlet.getServiceRegistry().add(EXCANVAS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JQUERY_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_TIME_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_STACK_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_PIE_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_SELECTION_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_NAVIGATE_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_CROSSHAIR_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_AXISLABEL_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_SAVE_AS_IMAGE_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_ORDER_BARS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_TOOLTIP_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FLOT_CATEGORIES_SERVICE);
        json = new ObjectMapper();
        json.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public FlotChartPeer() {
        addOutputProperty(FlotChart.PROPERTY_MODEL);
        addOutputProperty(FlotChart.PROPERTY_DATA);
    }
    /*
     * Over-ridden to handle request of tag instances are that are serialised
     * as a JSON stucture.
     *
     * @see nextapp.echo.webcontainer.ComponentSynchronizePeer#getOutputProperty(Context, Component, String, int)
     */

    @Override
    public Object getOutputProperty(final Context context,
            final Component component, final String propertyName,
            final int propertyIndex) {
        try {
            switch (propertyName) {
                case FlotChart.PROPERTY_MODEL: {
                    ChartModel data = ((FlotChart) component).getModel();
                    String res;
                    if (data != null) {
                        data.process();
                        res = "{\"flotModel\":" + json.writeValueAsString(data) + "}";
                    } else {
                        res = "{\"flotModel\":null}";
                    }
                    Logger.getLogger(getClass().getName()).finest("MODEL RES: " + res);
                    return res;
                }
                case FlotChart.PROPERTY_DATA: {
                    ChartData data = ((FlotChart) component).getData();
                    String res;
                    if (data != null) {
                        data.process();
                        res = "{\"flotData\":" + json.writeValueAsString(data) + "}";
                    } else {
                        res = "{\"flotData\":null}";
                    }
                    Logger.getLogger(getClass().getName()).finest("DATA RES : " + res);
                    return res;
                }
                default:
                    return super.getOutputProperty(context, component, propertyName, propertyIndex);
            }
        } catch (IOException ex) {
            Logger.getLogger(FlotChartPeer.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "FlotChart";
    }

    @Override
    public Class getComponentClass() {
        return FlotChart.class;
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(Extended.JS_SERVICE.getId());
        boolean isIe8OrLess = BaseAppServlet.getBrowserCapability() != null && BaseAppServlet.getBrowserCapability().isIE8orLess();
        if (isIe8OrLess) {
            serverMessage.addLibrary(EXCANVAS_SERVICE.getId());
        }
        serverMessage.addLibrary(JQUERY_SERVICE.getId());
        serverMessage.addLibrary(FLOT_SERVICE.getId());
        serverMessage.addLibrary(FLOT_TIME_SERVICE.getId());
        serverMessage.addLibrary(FLOT_STACK_SERVICE.getId());
        serverMessage.addLibrary(FLOT_PIE_SERVICE.getId());
        serverMessage.addLibrary(FLOT_SELECTION_SERVICE.getId());
        serverMessage.addLibrary(FLOT_NAVIGATE_SERVICE.getId());
        serverMessage.addLibrary(FLOT_CROSSHAIR_SERVICE.getId());
        if (!isIe8OrLess) {
            serverMessage.addLibrary(FLOT_AXISLABEL_SERVICE.getId());
            serverMessage.addLibrary(FLOT_SAVE_AS_IMAGE_SERVICE.getId());
        }
        serverMessage.addLibrary(FLOT_ORDER_BARS_SERVICE.getId());
        serverMessage.addLibrary(FLOT_TOOLTIP_SERVICE.getId());
        serverMessage.addLibrary(FLOT_CATEGORIES_SERVICE.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }
}
