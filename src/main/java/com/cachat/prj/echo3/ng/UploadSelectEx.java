package com.cachat.prj.echo3.ng;

import static com.cachat.prj.echo3.ng.able.Heightable.PROPERTY_HEIGHT;
import com.cachat.prj.echo3.ng.able.Sizeable;
import static com.cachat.prj.echo3.ng.able.Widthable.PROPERTY_WIDTH;
import nextapp.echo.app.Extent;
import nextapp.echo.filetransfer.app.UploadSelect;

/**
 *
 * @author scachat
 */
public class UploadSelectEx extends UploadSelect implements Sizeable {

    public UploadSelectEx() {
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

    public void setEnabledSendButtonText(String baseString) {
        //TODO
    }

    public void setDisabledSendButtonText(String baseString) {
        //TODO
    }
}
