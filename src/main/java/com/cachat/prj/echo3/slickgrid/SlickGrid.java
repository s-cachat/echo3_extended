package com.cachat.prj.echo3.slickgrid;

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
import com.cachat.util.SimpleJsonWriter;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Position;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 *
 * @author scachat
 */
public class SlickGrid extends Component implements Positionable, Sizeable {

    public static Logger logger = Logger.getLogger("SG");

    public static final String CELL_UPDATED_LISTENERS_CHANGED_PROPERTY = "actionListeners";
    public static final String ACTION_COMMAND_CHANGED_PROPERTY = "actionCommand";
    public static final String CELL_UPDATED = "cellUpdate";
    public static final String PROPERTY_CELL_CHANGE = "cellChange";
    public static final String PROPERTY_MODEL = "model";
    public static final String PROPERTY_DATA = "data";
    private String actionCommand;
    private List<? extends SlickGridData> data;
    private String cellChange;

    /**
     * service d'exécution différé. Utilisé si async est true; est asynchrone.
     */
    private ExecutorService updateExecutorService;
    /**
     * service d'exécution différé. Si true, le traitement de valueChanged est
     * asynchrone.
     */
    private final boolean async;

    /**
     * constructeur. (async=false)
     */
    public SlickGrid() {
        this(false);
    }

    /**
     * Constructeur
     *
     * @param async si true, le traitement des modifications dans valueChanged
     * est fait de façon asynchrone (et donc en dehors du thread ui)
     */
    public SlickGrid(boolean async) {
        this.async = async;
    }

    @Override
    public boolean isValidChild(Component child) {
        return false;
    }

    @Override
    public void clear() {
        //TODO
    }

    public void addCellUpdateListener(ActionListener l) {
        getEventListenerList().addListener(ActionListener.class, l);
        firePropertyChange(CELL_UPDATED_LISTENERS_CHANGED_PROPERTY, null, l);
    }

    public void removeCellUpdateListener(ActionListener l) {
        getEventListenerList().removeListener(ActionListener.class, l);
        firePropertyChange(CELL_UPDATED_LISTENERS_CHANGED_PROPERTY, l, null);
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
        return getPosition() != Position.STATIC;
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

    /*package protected*/ void setCellChange(int row, String field, Object value) {
        String oldData = cellChange;
        String newValue = SimpleJsonWriter.toString(new CellChange(row, field, value));
        set(PROPERTY_CELL_CHANGE, newValue);
        firePropertyChange(PROPERTY_CELL_CHANGE, oldData, data);
        cellChange = newValue;
    }

    public String getCellChange() {
        return cellChange;
    }

    public void setCellChange(String change) {
        this.cellChange = change;
        set(PROPERTY_CELL_CHANGE, change);

    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);

        switch (inputName) {
            case CELL_UPDATED:
                logger.log(Level.SEVERE, "cell update {0} = {1}", new Object[]{inputName, inputValue});
                CellUpdateData cu = (CellUpdateData) inputValue;

                if (async) {
                    updateExecutorService.submit(() -> getModel().getData().get(cu.getRow())._put(cu.getField(), cu.getValue()));
                } else {
                    getModel().getData().get(cu.getRow())._put(cu.getField(), cu.getValue());
                }
                break;
            default:
                logger.log(Level.SEVERE, "process input {0} = {1}", new Object[]{inputName, inputValue});
        }
    }

    /**
     * donne le modèle de donnée
     *
     * @return
     */
    public SlickGridModel getModel() {
        return (SlickGridModel) get(PROPERTY_MODEL);
    }

    /**
     * fixe le modèle de données
     *
     * @param model
     */
    public void setModel(SlickGridModel model) {
        model.setGrid(this);
        set(PROPERTY_DATA, data = model.getData());
        set(PROPERTY_MODEL, model);
    }

    @Override
    public void init() {
        updateExecutorService = async ? new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000)) : null;
        super.init();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (updateExecutorService != null) {
            updateExecutorService.shutdown();
            updateExecutorService = null;
        }
        SlickGridModel model = (SlickGridModel) get(PROPERTY_MODEL);
        if (model != null) {
            model.setGrid(null);
        }
    }

    /**
     * fixe les données seulement.
     *
     * @param data les nouvelles données
     */
    public void setData(List<? extends SlickGridData> data) {
        List<? extends SlickGridData> oldData = data;
        set(PROPERTY_DATA, data);
        firePropertyChange(PROPERTY_DATA, oldData, data);
    }

    /**
     * donne les données seulement
     *
     * @return les données
     */
    public List<? extends SlickGridData> getData() {
        return (List<? extends SlickGridData>) get(PROPERTY_DATA);
    }

}
