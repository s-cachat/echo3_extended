package com.cachat.prj.echo3.flotchart;

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
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Position;
import static nextapp.echo.app.Position.STATIC;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 *
 * @author scachat
 */
public class FlotChart extends Component implements Positionable, Sizeable {

    public static final String ACTION_LISTENERS_CHANGED_PROPERTY = "actionListeners";
    public static final String ACTION_COMMAND_CHANGED_PROPERTY = "actionCommand";
    public static final String INPUT_ACTION = "action";
    public static final String PROPERTY_MODEL = "model";
    public static final String PROPERTY_DATA = "data";
    private String actionCommand;

    public FlotChart() {
    }

    @Override
    public boolean isValidChild(Component child) {
        return false;
    }

    @Override
    public void clear() {
        //TODO
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
        EventListener[] actionListeners = getEventListenerList().getListeners(ActionListener.class);
        ActionEvent e = new ActionEvent(this, getActionCommand());
        for (int i = 0; i < actionListeners.length; ++i) {
            ((ActionListener) actionListeners[i]).actionPerformed(e);
        }
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
    public Position getPosition() {
        return (Position) get(PROPERTY_POSITION);
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
        return getPosition() != STATIC;
    }

    @Override
    public void setPosition(Position newValue) {
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
    public void setZIndex(int newValue) {
        set(PROPERTY_Z_INDEX, newValue);
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
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);

        switch (inputName) {
            case INPUT_ACTION:
                fireAction();
                break;
        }
    }

    /**
     * donne le modèle de donnée
     *
     * @return
     */
    public ChartModel getModel() {
        return (ChartModel) get(PROPERTY_MODEL);
    }

    /**
     * fixe le modeèle de données
     *
     * @param model
     */
    public void setModel(ChartModel model) {
        set(PROPERTY_DATA, new ChartData(model));
        set(PROPERTY_MODEL, model);
    }

    /**
     * fixe les données seulement
     */
    public void setData(ChartData data) {
        ChartData oldData = data;
        set(PROPERTY_DATA, data);
        firePropertyChange(PROPERTY_DATA, oldData, data);
        Logger.getLogger("FC").severe("setData");
    }

    /**
     * donne les données seulement
     */
    public ChartData getData() {
        return (ChartData) get(PROPERTY_DATA);
    }
}
