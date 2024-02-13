package com.cachat.prj.echo3.slickgrid;

import static com.cachat.prj.echo3.components.GenericServices.JQUERY_SERVICE;
import com.cachat.prj.echo3.ng.peers.Extended;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 *
 * @author scachat
 */
public class SlickGridPeer extends AbstractComponentSynchronizePeer {

    private static final Service JS_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.slickgrid.SlickGrid", "com/cachat/prj/echo3/slickgrid/SlickGrid.js");
    private static final Service JQUERY_EVENT_DRAG = JavaScriptService.forResource("com.cachat.prj.slickgrid.jquery.event.drag", "com/cachat/prj/echo3/slickgrid/js/jquery.event.drag-2.3.0.js");
    private static final Service SLICK_CELLRANGEDECORATOR = JavaScriptService.forResource("com.cachat.prj.slickgrid.slick.cellrangedecorator", "com/cachat/prj/echo3/slickgrid/js/slick.cellrangedecorator.js");
    private static final Service SLICK_CELLSELECTIONMODEL = JavaScriptService.forResource("com.cachat.prj.slickgrid.slick.cellselectionmodel", "com/cachat/prj/echo3/slickgrid/js/slick.cellselectionmodel.js");
    private static final Service SLICK_EDITORS = JavaScriptService.forResource("com.cachat.prj.slickgrid.slick.editors", "com/cachat/prj/echo3/slickgrid/js/slick.editors.js");
    private static final Service SLICK_GRID = JavaScriptService.forResource("com.cachat.prj.slickgrid.slick.grid", "com/cachat/prj/echo3/slickgrid/js/slick.grid.js");
    private static final Service JQUERY_UI = JavaScriptService.forResource("com.cachat.prj.slickgrid.jquery-ui", "com/cachat/prj/echo3/slickgrid/js/jquery-ui-1.11.3.min.js");
    private static final Service SLICK_CELLRANGESELECTOR = JavaScriptService.forResource("com.cachat.prj.slickgrid.slick.cellrangeselector", "com/cachat/prj/echo3/slickgrid/js/slick.cellrangeselector.js");
    private static final Service SLICK_CORE = JavaScriptService.forResource("com.cachat.prj.slickgrid.slick.core", "com/cachat/prj/echo3/slickgrid/js/slick.core.js");
    private static final Service SLICK_FORMATTERS = JavaScriptService.forResource("com.cachat.prj.slickgrid.slick.formatters", "com/cachat/prj/echo3/slickgrid/js/slick.formatters.js");

    private static final ObjectMapper json;

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JQUERY_SERVICE);
        WebContainerServlet.getServiceRegistry().add(JQUERY_EVENT_DRAG);
        WebContainerServlet.getServiceRegistry().add(SLICK_CELLRANGEDECORATOR);
        WebContainerServlet.getServiceRegistry().add(SLICK_CELLSELECTIONMODEL);
        WebContainerServlet.getServiceRegistry().add(SLICK_EDITORS);
        WebContainerServlet.getServiceRegistry().add(SLICK_GRID);
        WebContainerServlet.getServiceRegistry().add(JQUERY_UI);
        WebContainerServlet.getServiceRegistry().add(SLICK_CELLRANGESELECTOR);
        WebContainerServlet.getServiceRegistry().add(SLICK_CORE);
        WebContainerServlet.getServiceRegistry().add(SLICK_FORMATTERS);
        json = new ObjectMapper();
        json.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public SlickGridPeer() {
        addOutputProperty(SlickGrid.PROPERTY_MODEL);
        addOutputProperty(SlickGrid.PROPERTY_DATA);
        addEvent(new EventPeer(SlickGrid.CELL_UPDATED,
                SlickGrid.CELL_UPDATED_LISTENERS_CHANGED_PROPERTY, CellUpdateData.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return true;//at least we are a listener, to update the model
            }
        });
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
                case SlickGrid.PROPERTY_MODEL: {
                    SlickGridModel data = ((SlickGrid) component).getModel();
                    String res;
                    if (data != null) {
                        res = "{\"slickgridModel\":" + json.writeValueAsString(data) + "}";
                    } else {
                        res = "{\"slickgridModel\":null}";
                    }
                    Logger.getLogger(getClass().getName()).finest("MODEL RES: " + res);
                    return res;
                }
                case SlickGrid.PROPERTY_DATA: {
                    List<? extends SlickGridData> data = ((SlickGrid) component).getData();
                    String res;
                    if (data != null) {
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
            Logger.getLogger(SlickGridPeer.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "SlickGrid";
    }

    @Override
    public Class getComponentClass() {
        return SlickGrid.class;
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        serverMessage.addLibrary(Extended.JS_SERVICE.getId());

// Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(JQUERY_SERVICE.getId());
        serverMessage.addLibrary(JQUERY_EVENT_DRAG.getId());
        serverMessage.addLibrary(SLICK_CELLRANGEDECORATOR.getId());
        serverMessage.addLibrary(SLICK_CELLSELECTIONMODEL.getId());
        serverMessage.addLibrary(SLICK_EDITORS.getId());
        serverMessage.addLibrary(SLICK_GRID.getId());
        serverMessage.addLibrary(JQUERY_UI.getId());
        serverMessage.addLibrary(SLICK_CELLRANGESELECTOR.getId());
        serverMessage.addLibrary(SLICK_CORE.getId());
        serverMessage.addLibrary(SLICK_FORMATTERS.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }

    @Override
    public Class getInputPropertyClass(String propertyName) {
        if (SlickGrid.PROPERTY_CELL_CHANGE.equals(propertyName)) {
            return String.class;
        }
        return null;
    }

    @Override
    public void storeInputProperty(Context context, Component component,
            String propertyName, int propertyIndex, Object newValue) {
        if (propertyName.equals(SlickGrid.PROPERTY_CELL_CHANGE)) {
            try {
                CellUpdateData objectValue = json.readValue((String) newValue, CellUpdateData.class);
                ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
                clientUpdateManager.setComponentProperty(component, SlickGrid.PROPERTY_CELL_CHANGE, objectValue);
            } catch (IOException ex) {
                Logger.getLogger(SlickGridPeer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
