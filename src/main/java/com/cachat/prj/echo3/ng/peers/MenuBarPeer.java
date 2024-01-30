package com.cachat.prj.echo3.ng.peers;

import com.cachat.prj.echo3.ng.MenuBar;
import nextapp.echo.app.Component;
import nextapp.echo.app.util.Context;
import nextapp.echo.extras.webcontainer.sync.component.MenuBarPanePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 *
 * @author scachat
 */
public class MenuBarPeer extends MenuBarPanePeer {

    private static final Service MENUBAR_SERVICE = JavaScriptService.forResource("Extended.MenuBar", "com/cachat/prj/echo3/ng/js/MenuBar.js");

    static {
        WebContainerServlet.getServiceRegistry().add(MENUBAR_SERVICE);
    }

    /**
     * Returns the remote client component type name.
     *
     * @param mode a boolean flag indicating whether a component name (true) or
     * style name (false) is being rendered
     * @return the client component type name
     */
    @Override
    public String getClientComponentType(boolean mode) {
        return "Extended.MenuBar";
    }

    /**
     * Returns the <code>Class</code> of <code>Component</code> supported by
     * this peer.
     *
     * @return the <code>Class</code>
     */
    @Override
    public Class getComponentClass() {
        return MenuBar.class;
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
        serverMessage.addLibrary(MENUBAR_SERVICE.getId());
    }
}
