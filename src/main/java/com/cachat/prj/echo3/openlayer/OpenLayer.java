package com.cachat.prj.echo3.openlayer;

import com.cachat.prj.echo3.ng.ContainerEx;
import static com.cachat.prj.echo3.ng.able.Heightable.PROPERTY_HEIGHT;
import com.cachat.prj.echo3.ng.able.Positionable;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_BOTTOM;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_LEFT;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_POSITION;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_RIGHT;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_TOP;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_Z_INDEX;
import com.cachat.prj.echo3.ng.able.Sizeable;
import static com.cachat.prj.echo3.ng.able.Widthable.PROPERTY_WIDTH;
import java.util.EventListener;
import java.util.logging.Logger;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Label;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 *
 * @author scachat
 */
public class OpenLayer extends Component implements Positionable, Sizeable {

    public static final String PROPERTY_CENTER_LAT = "centerLat";
    public static final String PROPERTY_CENTER_LON = "centerLon";
    public static final String PROPERTY_ZOOM_LEVEL = "zoomLevel";
    private double centerLat;
    private double centerLon;
    private int zoomLevel;
    private int maxZoom;
    private boolean groupIcons = true;
    private String tileServer;
    public static final String PROPERTY_SELECTED_LAT = "selectedLat";
    public static final String PROPERTY_SELECTED_LON = "selectedLon";
    public static final String PROPERTY_SELECTED_ITEM = "selectedItem";
    private Double selectedLat;
    private Double selectedLon;
    private String selectedItem;
    public static final String ACTION_LISTENERS_CHANGED_PROPERTY = "actionListeners";
    public static final String ACTION_COMMAND_CHANGED_PROPERTY = "actionCommand";
    public static final String RELOAD_NOW_PROPERTY = "reloadNow";
    public static final String BUBBLE_ID = "bubble_id";
    public static final String INPUT_ACTION = "action";
    public static final String PROPERTY_MAX_ZOOM = "maxZoom";
    public static final String PROPERTY_GROUP_ICONS = "groupIcons";
    public static final String PROPERTY_TILE_SERVER = "tileServer";
    private String actionCommand;

    private OpenLayerBubbleGenerator bubbleGenerator;
    private final ContainerEx bubbleContainer;
    private final ContainerEx bubbleIsolator;

    private static final Logger logger = Logger.getLogger(OpenLayer.class.getName());

    public OpenLayer() {
        bubbleIsolator = new ContainerEx();
        add(bubbleIsolator);
        bubbleContainer = new ContainerEx();
        bubbleContainer.setBackground(Color.WHITE);
        bubbleContainer.add(new Label(""));
        bubbleIsolator.add(bubbleContainer);

    }

    @Override
    public boolean isValidChild(Component child) {
        return (child instanceof Layer) || child == bubbleIsolator;
    }

    @Override
    public void clear() {
        //TODO
    }

    /**
     * Fixe les limites. position sera absolute
     *
     * @param left extent en pixel, ou null si indéfini
     * @param top extent en pixel, ou null si indéfini
     * @param right extent en pixel, ou null si indéfini
     * @param bottom extent en pixel, ou null si indéfini
     * @param width extent en pixel, ou null si indéfini
     * @param height extent en pixel, ou null si indéfini
     */
    public void setBounds(Integer left, Integer top, Integer right, Integer bottom, Integer width, Integer height) {
        setPosition(Positionable.ABSOLUTE);
        if (top != null) {
            setTop(new Extent(top));
        }
        if (left != null) {
            setLeft(new Extent(left));
        }
        if (right != null) {
            setRight(new Extent(right));
        }
        if (bottom != null) {
            setBottom(new Extent(bottom));
        }
        if (width != null) {
            setWidth(new Extent(width));
        }
        if (height != null) {
            setHeight(new Extent(height));
        }
    }

    public void addActionListener(ActionListener l) {
        getEventListenerList().addListener(ActionListener.class, l);
        firePropertyChange(ACTION_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeActionListener(ActionListener l) {
        getEventListenerList().removeListener(ActionListener.class, l);
        firePropertyChange(ACTION_LISTENERS_CHANGED_PROPERTY, l, null);
    }

    public boolean hasActionListeners() {
        return hasEventListenerList()
                && getEventListenerList().getListenerCount(ActionListener.class) > 0;
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public void setActionCommand(String newValue) {
        String oldValue = actionCommand;
        actionCommand = newValue;
        firePropertyChange(ACTION_COMMAND_CHANGED_PROPERTY, oldValue, newValue);
    }

    private void fireAction() {
        System.err.println("ACTION : " + getSelectedItem());
        EventListener[] actionListeners = getEventListenerList().getListeners(ActionListener.class);
        ActionEvent e = new ActionEvent(this, getSelectedItem());
        for (int i = 0; i < actionListeners.length; ++i) {
            ((ActionListener) actionListeners[i]).actionPerformed(e);
        }
    }

    public Double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(Double newValue) {
        Double oldValue = this.centerLat;
        this.centerLat = newValue;
        firePropertyChange(PROPERTY_CENTER_LAT, oldValue, newValue);
    }

    public Double getCenterLon() {
        return centerLon;
    }

    public void setCenterLon(Double newValue) {
        Double oldValue = this.centerLon;
        this.centerLon = newValue;
        firePropertyChange(PROPERTY_CENTER_LON, oldValue, newValue);
    }

    public Integer getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(Integer newValue) {
        Integer oldValue = this.zoomLevel;
        this.zoomLevel = newValue;
        firePropertyChange(PROPERTY_ZOOM_LEVEL, oldValue, newValue);
    }

    public Boolean getGroupIcons() {
        return groupIcons;
    }

    public void setGroupIcons(Boolean newValue) {
        Integer oldValue = this.zoomLevel;
        this.groupIcons = newValue;
        firePropertyChange(PROPERTY_GROUP_ICONS, oldValue, newValue);
    }

    public Integer getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(Integer newValue) {
        Integer oldValue = this.zoomLevel;
        this.maxZoom = newValue;
        firePropertyChange(PROPERTY_MAX_ZOOM, oldValue, newValue);
    }

    public Double getSelectedLat() {
        return selectedLat;
    }

    public void setSelectedLat(Double newValue) {
        Double oldValue = this.selectedLat;
        this.selectedLat = newValue;
        firePropertyChange(PROPERTY_SELECTED_LAT, oldValue, newValue);
    }

    public Double getSelectedLon() {
        return selectedLon;
    }

    public void setSelectedLon(Double newValue) {
        Double oldValue = this.selectedLon;
        this.selectedLon = newValue;
        firePropertyChange(PROPERTY_SELECTED_LON, oldValue, newValue);
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String newValue) {
        String oldValue = this.selectedItem;
        this.selectedItem = newValue;
        firePropertyChange(PROPERTY_SELECTED_ITEM, oldValue, newValue);
    }

    public String getTileServer() {
        return tileServer;
    }

    public void setTileServer(String newValue) {
        String oldValue = this.selectedItem;
        this.tileServer = newValue;
        firePropertyChange(PROPERTY_TILE_SERVER, oldValue, newValue);
    }

    @Override
    public Extent getBottom() {
        return (Extent) get(PROPERTY_BOTTOM);
    }

    @Override
    public Extent getLeft() {
        return (Extent) get(PROPERTY_LEFT);
    }

    @Override
    public int getPosition() {
        return (Integer) get(PROPERTY_POSITION);
    }

    @Override
    public Extent getRight() {
        return (Extent) get(PROPERTY_RIGHT);
    }

    @Override
    public Extent getTop() {
        return (Extent) get(PROPERTY_TOP);
    }

    @Override
    public int getZIndex() {
        return (Integer) get(PROPERTY_Z_INDEX);
    }

    @Override
    public boolean isPositioned() {
        return getPosition() != Positionable.STATIC;
    }

    @Override
    public void setPosition(int newValue) {
        set(PROPERTY_POSITION, newValue);
    }

    @Override
    public void setBottom(Extent newValue) {
        set(PROPERTY_BOTTOM, newValue);
    }

    @Override
    public void setLeft(Extent newValue) {
        set(PROPERTY_LEFT, newValue);
    }

    @Override
    public void setRight(Extent newValue) {
        set(PROPERTY_RIGHT, newValue);
    }

    @Override
    public void setTop(Extent newValue) {
        set(PROPERTY_TOP, newValue);
    }

    public void setBottom(int newValue) {
        set(PROPERTY_BOTTOM, new Extent(newValue));
    }

    public void setLeft(int newValue) {
        set(PROPERTY_LEFT, new Extent(newValue));
    }

    public void setRight(int newValue) {
        set(PROPERTY_RIGHT, new Extent(newValue));
    }

    public void setTop(int newValue) {
        set(PROPERTY_TOP, new Extent(newValue));
    }

    @Override
    public Extent getWidth() {
        return (Extent) get(PROPERTY_WIDTH);
    }

    @Override
    public void setWidth(Extent newValue) {
        set(PROPERTY_WIDTH, newValue);
    }

    public void setWidth(int newValue) {
        set(PROPERTY_WIDTH, new Extent(newValue));
    }

    @Override
    public Extent getHeight() {
        return (Extent) get(PROPERTY_HEIGHT);
    }

    @Override
    public void setHeight(Extent newValue) {
        set(PROPERTY_HEIGHT, newValue);
    }

    public void setHeight(int newValue) {
        set(PROPERTY_HEIGHT, new Extent(newValue));
    }

    @Override
    public void setZIndex(int newValue) {
        set(PROPERTY_Z_INDEX, newValue);
    }

    public OpenLayerBubbleGenerator getBubbleGenerator() {
        return bubbleGenerator;
    }

    public void setBubbleGenerator(OpenLayerBubbleGenerator bubbleGenerator) {
        this.bubbleGenerator = bubbleGenerator;
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        switch (inputName) {
            case OpenLayer.PROPERTY_CENTER_LAT:
                setCenterLat(((Number) inputValue).doubleValue());
                break;
            case OpenLayer.PROPERTY_CENTER_LON:
                setCenterLon(((Number) inputValue).doubleValue());
                break;
            case OpenLayer.PROPERTY_ZOOM_LEVEL:
                setZoomLevel((Integer) inputValue);
                break;
            case OpenLayer.PROPERTY_SELECTED_LAT:
                setSelectedLat((Double) inputValue);
                break;
            case OpenLayer.PROPERTY_SELECTED_LON:
                setSelectedLon((Double) inputValue);
                break;
            case OpenLayer.PROPERTY_SELECTED_ITEM:
                setSelectedItem((String) inputValue);
                break;
            case OpenLayer.BUBBLE_ID:
                if (bubbleGenerator != null) {
                    logger.severe("building bubble for " + inputValue);
                    bubbleContainer.removeAll();
                    Component comp = bubbleGenerator.getComponentFor((String) inputValue);
                    if (comp != null) {
                        bubbleContainer.add(comp);
                    }
                } else {
                    logger.severe("no bubble generator for " + inputValue);
                }

                break;
            case INPUT_ACTION:
                fireAction();
                this.selectedItem = null;
                break;
            default:
                logger.severe("unhandled input value " + inputName + " = " + inputValue);
        }
    }

    public void update() {
//        long oldValue=get(RELOAD_NOW_PROPERTY);
//        long newValue=System.currentTimeMillis();
//        set(RELOAD_NOW_PROPERTY, System.currentTimeMillis());
//         firePropertyChange(ACTION_COMMAND_CHANGED_PROPERTY, oldValue, newValue);
    }

}
