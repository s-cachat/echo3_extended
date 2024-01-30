package com.cachat.prj.echo3.openlayer;

import com.cachat.prj.echo3.ng.peers.Extended;
import static com.cachat.prj.echo3.openlayer.OpenLayer.RELOAD_NOW_PROPERTY;
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
public class OpenLayerPeer extends AbstractComponentSynchronizePeer {

    private static final Service JS_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.openlayer.OpenLayer", "com/cachat/prj/echo3/openlayer/OpenLayer.js");
    private static final Service OL_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.openlayer.OpenLayerLib", "com/cachat/prj/echo3/openlayer/resources/OpenLayers.debug.js");
    private static final Service CLUSTER_EXPLODE_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.openlayer.ClusterExplode", "com/cachat/prj/echo3/openlayer/resources/ClusterExplode.js");
    private static final Service PROJ4JS = JavaScriptService.forResource(
            "proj4js", "com/cachat/prj/echo3/openlayer/resources/proj4.js");
    private static final Service DUMB_CONSOLE = JavaScriptService.forResource(
            "dumbConsole", "com/cachat/prj/echo3/openlayer/resources/console.js");

    static {
        WebContainerServlet.getServiceRegistry().add(DUMB_CONSOLE);
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(OL_SERVICE);
        WebContainerServlet.getServiceRegistry().add(CLUSTER_EXPLODE_SERVICE);
        WebContainerServlet.getServiceRegistry().add(PROJ4JS);
    }

    public OpenLayerPeer() {
        addOutputProperty(OpenLayer.PROPERTY_CENTER_LAT);
        addOutputProperty(OpenLayer.PROPERTY_CENTER_LON);
        addOutputProperty(OpenLayer.PROPERTY_ZOOM_LEVEL);
        addOutputProperty(OpenLayer.PROPERTY_MAX_ZOOM);
        addOutputProperty(OpenLayer.PROPERTY_GROUP_ICONS);
        addOutputProperty(OpenLayer.PROPERTY_TILE_SERVER);
        addOutputProperty(OpenLayer.BUBBLE_ID);
        addOutputProperty(OpenLayer.RELOAD_NOW_PROPERTY);
        addEvent(new EventPeer(OpenLayer.INPUT_ACTION,
                OpenLayer.ACTION_LISTENERS_CHANGED_PROPERTY, String.class) {
                    @Override
                    public boolean hasListeners(Context context, Component c) {
                        return ((OpenLayer) c).hasActionListeners();
                    }
                });
    }

    @Override
    public Object getOutputProperty(Context context, Component component,
            String propertyName, int propertyIndex) {
        switch (propertyName) {
            case OpenLayer.PROPERTY_CENTER_LAT: {
                OpenLayer comp = (OpenLayer) component;
                return comp.getCenterLat();
            }
            case OpenLayer.PROPERTY_CENTER_LON: {
                OpenLayer comp = (OpenLayer) component;
                return comp.getCenterLon();
            }
            case OpenLayer.PROPERTY_ZOOM_LEVEL: {
                OpenLayer comp = (OpenLayer) component;
                return comp.getZoomLevel();
            }
            default:
                return super.getOutputProperty(context, component, propertyName, propertyIndex);
        }
    }

    @Override
    public Class getInputPropertyClass(String propertyName) {
        switch (propertyName) {
            case OpenLayer.PROPERTY_CENTER_LAT:
                return Double.class;
            case OpenLayer.PROPERTY_CENTER_LON:
                return Double.class;
            case OpenLayer.PROPERTY_ZOOM_LEVEL:
                return Integer.class;
            case OpenLayer.PROPERTY_SELECTED_LAT:
                return Double.class;
            case OpenLayer.PROPERTY_SELECTED_LON:
                return Double.class;
            case OpenLayer.PROPERTY_SELECTED_ITEM:
                return String.class;
            case OpenLayer.BUBBLE_ID:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public void storeInputProperty(Context context, Component component,
            String propertyName, int propertyIndex, Object newValue) {
        switch (propertyName) {
            case OpenLayer.PROPERTY_CENTER_LAT:
            case OpenLayer.PROPERTY_CENTER_LON:
            case OpenLayer.PROPERTY_ZOOM_LEVEL:
            case OpenLayer.PROPERTY_SELECTED_LAT:
            case OpenLayer.PROPERTY_SELECTED_LON:
            case OpenLayer.PROPERTY_SELECTED_ITEM:
            case OpenLayer.BUBBLE_ID:
                ClientUpdateManager clientUpdateManager
                        = (ClientUpdateManager) context.get(ClientUpdateManager.class);
                clientUpdateManager.setComponentProperty(component, propertyName, newValue);
        }
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        // Return client-side component type name.
        return "OpenLayer";
    }

    @Override
    public Class getComponentClass() {
        // Return server-side Java class.
        return OpenLayer.class;
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(DUMB_CONSOLE.getId());
        serverMessage.addLibrary(PROJ4JS.getId());
        serverMessage.addLibrary(Extended.JS_SERVICE.getId());
        serverMessage.addLibrary(OL_SERVICE.getId());
        serverMessage.addLibrary(CLUSTER_EXPLODE_SERVICE.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());

    }
}
