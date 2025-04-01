package com.cachat.prj.echo3.synoptique;

import com.cachat.prj.echo3.ng.peers.Extended;
import com.google.gson.Gson;
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
public class SynoptiquePeer extends AbstractComponentSynchronizePeer {

    private static final Service JS_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.synoptique.Synoptique", "com/cachat/prj/echo3/synoptique/synoptique.js");
    private static final Service FABRIC_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.openlayer.SynoptiqueLib", "com/cachat/prj/echo3/synoptique/fabric.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(FABRIC_SERVICE);
    }

    public SynoptiquePeer() {
        addOutputProperty(Synoptique.ACTION);
        addEvent(new AbstractComponentSynchronizePeer.EventPeer(Synoptique.OBJECT_CLIC, Synoptique.OBJECT_CLIC, SynClicEvent.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return true;//((Synoptique) c).hasClicListeners();
            }
        });
        addEvent(new AbstractComponentSynchronizePeer.EventPeer(Synoptique.OBJECT_EDIT, Synoptique.OBJECT_EDIT, SynModifiedEvent.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return true;//((Synoptique) c).hasEditListeners();
            }
        });
    }

    @Override
    public Object getOutputProperty(Context context, Component component,
            String propertyName, int propertyIndex) {
        switch (propertyName) {
            case Synoptique.ACTION: {
                Synoptique comp = (Synoptique) component;
                return comp.getActionAndClear();
            }
            default:
                return super.getOutputProperty(context, component, propertyName, propertyIndex);
        }
    }

    @Override
    public Class getInputPropertyClass(String propertyName) {
        switch (propertyName) {
            case Synoptique.OBJECT_CLIC:
                return String.class;
            case Synoptique.OBJECT_EDIT:
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public void storeInputProperty(Context context, Component component,
            String propertyName, int propertyIndex, Object newValue) {
        switch (propertyName) {
            case Synoptique.ACTION:
                ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
                Gson gson = new Gson();
                final String newJsonValue = gson.toJson(newValue);
                Synoptique.logger.info("Sending update " + newJsonValue);
                clientUpdateManager.setComponentProperty(component, propertyName, newJsonValue);
        }
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "Synoptique";
    }

    @Override
    public Class getComponentClass() {
        return Synoptique.class;
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        serverMessage.addLibrary(Extended.JS_SERVICE.getId());
        serverMessage.addLibrary(FABRIC_SERVICE.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());

    }
}
