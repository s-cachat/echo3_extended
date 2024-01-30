package com.cachat.prj.echo3.ng.peers;

import com.cachat.prj.echo3.ng.GroupBox;
import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 *
 * @author scachat
 */
public class GroupBoxPeer extends ContainerExPeer {
    // Create a JavaScriptService containing the SpinButton JavaScript code.

    private static final Service JS_SERVICE = JavaScriptService.forResource(
            "com.cachat.prj.echo3.ng.GroupBox", "com/cachat/prj/echo3/ng/js/GroupBox.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
    }

    public GroupBoxPeer() {
        addOutputProperty(GroupBox.TITLE_CHANGED_PROPERTY);
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        // Return client-side component type name.
        return "GroupBox";
    }

    @Override
    public Class getComponentClass() {
        // Return server-side Java class.
        return GroupBox.class;
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        serverMessage.addLibrary(Extended.JS_SERVICE.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }

    @Override
    public Object getOutputProperty(Context context, Component component,
            String propertyName, int propertyIndex) {
        if (propertyName.equals(GroupBox.TITLE_CHANGED_PROPERTY)) {
            GroupBox comp = (GroupBox) component;
            return comp.getTitle();
        } else {
            return super.getOutputProperty(context, component, propertyName, propertyIndex);
        }
    }
}
