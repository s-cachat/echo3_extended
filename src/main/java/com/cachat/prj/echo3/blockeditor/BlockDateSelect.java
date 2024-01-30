package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.components.DateSelect2;
import com.cachat.util.BeanTools;
import java.util.Date;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.event.ChangeListener;

/**
 * champs date
 *
 * @author scachat
 */
public class BlockDateSelect extends BlockField<DateSelect2> {

    public BlockDateSelect(BlockField bf) {
        super(bf);
        if (bf.editor instanceof DateSelect2) {
            DateSelect2 x = (DateSelect2) bf.editor;

            editor = new DateSelect2(x.isWithTime());
            editor.setEnableButton(x.isEnableButton());
        }
    }

    public BlockDateSelect(LocalisedItem li, String property, boolean withTime, boolean withNull) {
        super(li, property);
        editor = new DateSelect2(withTime);
        editor.setButtonStyleName("Button");
        editor.setEnableButton(withNull);
    }

    public BlockDateSelect(LocalisedItem li, String property, boolean withTime) {
        super(li, property);
        editor = new DateSelect2(withTime);
    }

    public BlockDateSelect(LocalisedItem li, String property) {
        super(li, property);
        editor = new DateSelect2();
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
            if (editor.isEnableButton() && !editor.isEnabled2()) {
                BeanTools.setRaw(getParent().getCurrent(), property, null);
            } else {
                BeanTools.setRaw(getParent().getCurrent(), property, d);
            }

            error.setText((String) null);
            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }

    /**
     * ajoute un changeListener
     *
     * @param a le listener
     */
    public void addChangeListener(ChangeListener a) {
        editor.addChangeListener(a);
    }

    /**
     * supprime un changeListener
     *
     * @param a le listener
     */
    public void removeChangeListener(ChangeListener a) {
        editor.removeChangeListener(a);
    }
}
