package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import static com.cachat.prj.echo3.blockeditor.BlockField.logger;
import com.cachat.prj.echo3.editor.BasicEditor;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.LabelEx;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.validation.Validator;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.filetransfer.app.UploadSelect;
import nextapp.echo.filetransfer.app.event.UploadEvent;
import nextapp.echo.filetransfer.app.event.UploadListener;
import nextapp.echo.filetransfer.model.Upload;

/**
 * editeur pour un fichier brut a uploader
 *
 * @author scachat
 */
public class BlockDataUpload extends BlockField<Row> implements UploadListener {

    UploadSelect us;
    Label view;
    byte[] content;
    int maxSize;

    public BlockDataUpload(BlockField x) {
        super(x);
        maxSize = ((BlockDataUpload) x).maxSize;
        buildEditor();
    }

    public BlockDataUpload(LocalisedItem li, String property, int maxSize) {
        super(li, property);
        this.maxSize = maxSize;
        buildEditor();
    }

    private void buildEditor() {
        editor = new Row() {
            @Override
            public void dispose() {
                super.dispose();
                try {
                    us.removeUploadListener(BlockDataUpload.this);
                } catch (TooManyListenersException ex) {
                    Logger.getLogger(BlockDataUpload.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        us = new UploadSelect();
//TODO ECHO3//        us.setWidth(new Extent(75, Extent.PERCENT));
        editor.add(us);
        view = new LabelEx();
        editor.add(view);
//TODO ECHO3//        us.setWidth(new Extent(200));
        us.addUploadListener(this);
    }

    @Override
    public void uploadComplete(UploadEvent uploadEvent) {
        try {
            Upload ul = uploadEvent.getUpload();
            if (ul.getSize() < maxSize || maxSize <= 0) {
                content = new byte[(int) ul.getSize()];
                ul.getInputStream().read(content, 0, (int) ul.getSize());
                error.setText((String) null);
            } else {
                error.setText(localisedItem.getString("too_big"));
            }
        } catch (IOException ex) {
            content = null;
            Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void copyObjectToUi() {
        content = (byte[]) BeanTools.getRaw(getParent().getCurrent(), property);
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            BeanTools.setBin(getParent().getCurrent(), property, content);
            error.setText((String) null);
            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }

    @Override
    public void uploadSend() {
        content = null;
    }
}
