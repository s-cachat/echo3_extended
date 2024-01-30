package com.cachat.prj.echo3.ng;

import static com.cachat.prj.echo3.ng.able.Heightable.PROPERTY_HEIGHT;
import com.cachat.prj.echo3.ng.able.Sizeable;
import static com.cachat.prj.echo3.ng.able.Widthable.PROPERTY_WIDTH;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Extent;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.Label;

/**
 *
 * @author scachat
 */
public class LabelEx extends Label implements Sizeable, Comparable<LabelEx> {

    /**
     * our logger
     */
    private static final Logger logger = Logger.getLogger(LabelEx.class.getName());

    public LabelEx() {
    }

    public LabelEx(String text) {
        super(text);
    }
    public LabelEx(Alignment alignment,String text) {
        super(text);
        setTextAlignment(alignment);
    }

    public LabelEx(String text, Object... arg) {
        super();
        try {
            setText(String.format(text, arg));
        } catch (Throwable e) {
            setText(text + " (TE!)");
            logger.log(Level.SEVERE, "Error on format \"" + text + "\"", e);
        }
        }
    public LabelEx(Alignment alignment,String text, Object... arg) {
        this(text,arg);
        setTextAlignment(alignment);
    }

    public LabelEx(ImageReference icon) {
        super(icon);
    }

    public LabelEx(String text, ImageReference icon) {
        super(text, icon);
    }

    @Override
    public Extent getWidth() {
        return (Extent) get(PROPERTY_WIDTH);
    }

    @Override
    public void setWidth(Extent newValue) {
        set(PROPERTY_WIDTH, newValue);
    }

    @Override
    public Extent getHeight() {
        return (Extent) get(PROPERTY_HEIGHT);
    }

    @Override
    public void setHeight(Extent newValue) {
        set(PROPERTY_HEIGHT, newValue);
    }

    public void setIntepretNewlines(boolean b) {
        //TODO
    }

    @Override
    public int compareTo(LabelEx o) {
        String me = getText();
        String mo = o == null ? null : o.getText();
        if (me == null) {
            if (mo == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (mo == null) {
            return 1;
        } else {
            return me.compareTo(mo);
        }
    }
}
