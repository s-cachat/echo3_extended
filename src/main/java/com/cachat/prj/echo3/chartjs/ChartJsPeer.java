package com.cachat.prj.echo3.chartjs;

import com.cachat.prj.echo3.flotchart.ChartData;
import com.cachat.prj.echo3.ng.peers.Extended;
import com.cachat.prj.echo3.outils.LocalResource;
import com.cachat.prj.echo3.outils.LowercaseEnumSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nextapp.echo.app.Color;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
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
public class ChartJsPeer extends AbstractComponentSynchronizePeer {
    // Create a JavaScriptService containing the SpinButton JavaScript code.

    public static final Service JS_SERVICE = LocalResource.forJsResource(ChartJsPeer.class, "com.cachat.prj.echo3.chartjs.ChartJs", "/com/cachat/prj/echo3/chartjs/js/ChartJS.js");
    private static final Service CHART = LocalResource.forJsResource(ChartJsPeer.class, "com.cachat.prj.echo3.chartjs.chart", "/com/cachat/prj/echo3/chartjs/js/chart.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(CHART);
    }

    public ChartJsPeer() {
        addOutputProperty(ChartJs.PROPERTY_CONFIG);
        addOutputProperty(ChartJs.PROPERTY_DATA);
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
        switch (propertyName) {
            case ChartJs.PROPERTY_CONFIG -> {
                ChartJSConfig data = ((ChartJs) component).getConfig();
                String res;
                if (data != null) {
                    res = "{\"chartModel\":" + buildGson().toJson(data) + "}";
                } else {
                    res = "{\"chartModel\":null}";
                }
                Logger.getLogger(getClass().getName()).finest("MODEL RES: " + res);
                return res;
            }
            case ChartJs.PROPERTY_DATA -> {
                ChartJsData data = ((ChartJs) component).getData();
                String res;
                if (data != null) {
                    res = "{\"chartData\":" + buildGson().toJson(data) + "}";
                } else {
                    res = "{\"chartData\":null}";
                }
                Logger.getLogger(getClass().getName()).finest("DATA RES : " + res);
                return res;
            }
            default -> {
                return super.getOutputProperty(context, component, propertyName, propertyIndex);
            }
        }
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "ChartJs";
    }

    @Override
    public Class getComponentClass() {
        return ChartJs.class;
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context
                .get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(Extended.JS_SERVICE.getId());
        serverMessage.addLibrary(CHART.getId());
        serverMessage.addLibrary(JS_SERVICE.getId());
    }

    public static Gson buildGson() {
        GsonBuilder b = new GsonBuilder();
        Type typeListColor = new TypeToken<List<Color>>() {
        }.getType();
        Gson g = b
                .registerTypeAdapter(ChartJsBorderAlign.class, new LowercaseEnumSerializer())
                .registerTypeAdapter(ChartJsBorderJoinStyle.class, new LowercaseEnumSerializer())
                .registerTypeAdapter(ChartJsEasing.class, new LowercaseEnumSerializer())
                .registerTypeAdapter(ChartJsInteractionMode.class, new LowercaseEnumSerializer())
                .registerTypeAdapter(ChartJsPointStyle.class, new LowercaseEnumSerializer())
                .registerTypeAdapter(ChartJsType.class, new LowercaseEnumSerializer())
                .registerTypeAdapter(ChartJsPoint.class, new ChartJsPointSerializer())
                .registerTypeAdapter(Color.class, new ChartJsColorSerializer())
                .registerTypeAdapter(typeListColor, new ChartJsColorArraySerializer())
                .setPrettyPrinting()
                .create();
        return g;
    }
}
