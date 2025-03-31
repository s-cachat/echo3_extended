package com.cachat.prj.echo3.synoptique;

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
import static com.cachat.prj.echo3.openlayer.OpenLayer.ACTION_LISTENERS_CHANGED_PROPERTY;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;

/**
 *
 * @author scachat
 */
public class Synoptique extends Component implements Positionable, Sizeable {

    /*package protected*/ static final Logger logger = Logger.getLogger(Synoptique.class.getName());

    public static final String OBJECT_EDIT = "objectEdit";
    public static final String OBJECT_CLIC = "objectClic";
    public static final String ACTION = "objectClic";
    private Map<String, SynObject> objects = new HashMap<>();
    private static int nextId = 1;
    private SynAction action = null;
    private ReentrantLock actionLock = new ReentrantLock();
    private Map<String, SynObject> objectWithClicListener = new HashMap<>();
    private Map<String, SynObject> objectWithEditListener = new HashMap<>();

    public Synoptique() {
    }

    /**
     * ajoute un objet au synoptique
     *
     * @param obj l'objet
     */
    public void add(SynObject obj) {
        obj.setUid(String.format("S_%d", nextId++));
        actionLock.lock();
        try {
            objects.put(obj.getUid(), obj);
            if (action == null) {
                action = new SynAction();
            }
            action.getAdd().add(obj);
            if (obj.hasClicListener()) {
                objectWithClicListener.put(obj.getUid(), obj);
            }
            if (obj.hasEditListener()) {
                objectWithEditListener.put(obj.getUid(), obj);
            }
        } finally {
            actionLock.unlock();
        }
        firePropertyChange(ACTION, null, action);
    }

    /**
     * modifie un objet
     *
     * @param obj l'objet
     */
    public void update(SynObject obj) {
        actionLock.lock();
        try {
            if (action == null) {
                action = new SynAction();
            }
            action.getUpdate().add(obj);
            if (obj.hasClicListener()) {
                objectWithClicListener.put(obj.getUid(), obj);
            }
            if (obj.hasEditListener()) {
                objectWithEditListener.put(obj.getUid(), obj);
            }
        } finally {
            actionLock.unlock();
        }
        firePropertyChange(ACTION, action, action);
    }

    /**
     * supprime un objet
     *
     * @param obj l'objet
     */
    public void del(SynObject obj) {
        actionLock.lock();
        try {
            objects.remove(obj.getUid());
            objectWithClicListener.remove(obj.getUid());
            objectWithEditListener.remove(obj.getUid());
            if (action == null) {
                action = new SynAction();
            }
            action.getDel().add(obj.getUid());
        } finally {
            actionLock.unlock();
        }
        firePropertyChange(ACTION, action, null);
    }

    /**
     * donne l'action en cours et la remet à zéro
     *
     * @return l'action
     */
    /*package protected */ SynAction getActionAndClear() {
        actionLock.lock();
        try {
            if (action == null) {
                logger.info("Send empty Action");
                return new SynAction();
            } else {
                logger.info("Send and clear Action");
                SynAction a = action;
                action = null;
                return a;
            }

        } finally {
            actionLock.unlock();
        }
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

    @Override
    public void processInput(String inputName, Object inputValue) {
        super.processInput(inputName, inputValue);
        switch (inputName) {
            case OBJECT_EDIT:
                //TODO;
                break;
            case OBJECT_CLIC:
                //TODO;
                break;
            default:
                logger.severe("unhandled input value " + inputName + " = " + inputValue);
        }
    }

    /*package protected*/ boolean hasClicListeners() {
        return !objectWithClicListener.isEmpty();
    }

    /*package protected*/ boolean hasEditListeners() {
        return !objectWithEditListener.isEmpty();
    }

}
