package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.components.InstantSelect4;
import com.cachat.util.BeanTools;
import java.util.List;
import jakarta.validation.Validator;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.ChangeEvent;
import nextapp.echo.app.event.ChangeListener;

/**
 * Champs date
 *
 * @author scachat
 */
public class BlockInstantDateSelect extends BlockField<InstantSelect4> {

    public BlockInstantDateSelect(BlockField bf) {
        super(bf);
        if (bf.editor instanceof InstantSelect4 dateSelect4) {
            editor = new InstantSelect4(dateSelect4);
            editor.setStyleName("BlockEditor");
        }
    }

    public BlockInstantDateSelect(LocalisedItem li, String property) {
        this(li, property, false);
    }

    public BlockInstantDateSelect(LocalisedItem li, String property, boolean withTime) {
        this(li, property, withTime, false);
    }

    public BlockInstantDateSelect(LocalisedItem li, String property, boolean withTime, boolean withNull) {
        super(li, property);
        editor = new InstantSelect4(withTime, withNull, li == null ? null : li.getLocale());
        editor.setStyleName("BlockEditor");
    }

    @Override
    public void copyObjectToUi() {
        Object o = BeanTools.getRaw(getParent().getCurrent(), property);
        if (o == null) {
            editor.setSelectedInstant((Instant) null);
        } else if (o instanceof Instant d) {
            editor.setSelectedInstant(d);
        } else {
            throw new ClassCastException(String.format("%s.%s is not an Instant but a %s", getParent().getCurrent().getClass().getSimpleName(), property, o.getClass().getName()));
        }
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            Instant d = editor.getSelectedInstant();
            BeanTools.setRaw(getParent().getCurrent(), property, d);

            error.setText((String) null);
            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }
    private List<ChangeListener> listeners = new ArrayList<>();
    private ActionListener al = null;

    /**
     * ajoute un changeListener
     *
     * @param cl le listener
     */
    public synchronized void addChangeListener(ChangeListener cl) {
        if (al == null) {
            al = e -> listeners.forEach(a -> a.stateChanged(new ChangeEvent(e.getSource())));
            editor.addActionListener(al);
        }
        listeners.add(cl);
    }

    /**
     * supprime un changeListener
     *
     * @param cl le listener
     */
    public synchronized void removeChangeListener(ChangeListener cl) {
        listeners.remove(cl);
        if (listeners.isEmpty()) {
            editor.removeActionListener(al);
            al = null;
        }

    }

    /**
     * donne la date sélectionnée
     *
     * @return la date/heure
     */
    public Instant getSelectedInstant() throws ParseException {
        return editor.getSelectedInstant();
    }

    /**
     * force la date sélectionnée
     *
     * @param instant la date/heure
     */
    public void setSelectedInstant(Instant instant) {
        editor.setSelectedInstant(instant);
        BeanTools.setRaw(getParent().getCurrent(), property, instant);
    }
}
