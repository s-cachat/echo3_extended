package com.cachat.prj.echo3.openlayer;

import nextapp.echo.app.Component;

/**
 *
 * @author scachat
 */
public class Layer extends Component {

    public static final String TYPE_PROPERTY = "layerType";
    public static final String LABEL_PROPERTY = "layerLabel";
    public static final String NAME_PROPERTY = "layerName";
    public static final String KEY_PROPERTY = "layerKey";
    public static final String VISIBILITY_PROPERTY = "layerVisibility";

    public Layer(Type type, String label, String name, String key) {
        setLayerType(type);
        setLayerLabel(label);
        setLayerName(name);
        setLayerKey(key);
    }

    public Layer() {

    }

    public static enum Type {

        KML,
        WMS,
        WFS,
        OSM,
        BING
    }

    public Type getLayerType() {
        return Type.valueOf((String) get(TYPE_PROPERTY));
    }

    public final void setLayerType(Type layerType) {
        set(TYPE_PROPERTY, layerType.name());
    }

    public String getLayerLabel() {
        return (String) get(LABEL_PROPERTY);
    }

    public final void setLayerLabel(String layerLabel) {
        set(LABEL_PROPERTY, layerLabel);
    }

    public String getLayerName() {
        return (String) get(NAME_PROPERTY);
    }

    public final void setLayerName(String layerName) {
        set(NAME_PROPERTY, layerName);
    }

    public String getLayerKey() {
        return (String) get(KEY_PROPERTY);
    }

    public final void setLayerKey(String layerKey) {
        set(KEY_PROPERTY, layerKey);
    }

    public boolean getVisibility() {
        String ret = (String) get(VISIBILITY_PROPERTY);
        return Boolean.parseBoolean(ret);
    }

    public void setVisibility(boolean visibility) {
        set(VISIBILITY_PROPERTY, Boolean.toString(visibility));
    }
}
