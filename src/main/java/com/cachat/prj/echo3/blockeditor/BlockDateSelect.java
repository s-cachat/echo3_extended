package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.components.DateSelect4;
import com.cachat.util.BeanTools;
import java.util.Date;
import java.util.List;
import jakarta.validation.Validator;
import java.text.ParseException;
import java.util.ArrayList;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.ChangeEvent;
import nextapp.echo.app.event.ChangeListener;

/**
 * Champs date
 *
 * @author scachat
 */
public class BlockDateSelect extends BlockField<DateSelect4> {

    public BlockDateSelect(BlockField bf) {
        super(bf);
        if (bf.editor instanceof DateSelect4 dateSelect4) {
            editor = new DateSelect4(dateSelect4);
            editor.setStyleName("BlockEditor");
        }
    }

    public BlockDateSelect(LocalisedItem li, String property) {
        this(li, property, false);
    }

    public BlockDateSelect(LocalisedItem li, String property, boolean withTime) {
        this(li, property, false, false);
    }

    public BlockDateSelect(LocalisedItem li, String property, boolean withTime, boolean withNull) {
        super(li, property);
        editor = new DateSelect4(withTime, withNull, li == null ? null : li.getLocale());
        editor.setStyleName("BlockEditor");
    }

    @Override
    public void copyObjectToUi() {
        Date d = (Date) BeanTools.getRaw(getParent().getCurrent(), property);
        editor.setSelectedDate(d);
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            Date d = editor.getSelectedDate();
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
     */
    public Date getSelectedDate() throws ParseException {
        return editor.getSelectedDate();
    }
}
