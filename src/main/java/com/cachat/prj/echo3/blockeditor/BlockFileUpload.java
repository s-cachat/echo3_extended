package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.editor.BasicEditor;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.UploadSelectEx;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.validation.Validator;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.filetransfer.app.event.UploadEvent;
import nextapp.echo.filetransfer.app.event.UploadListener;
import nextapp.echo.filetransfer.model.Upload;

/**
 * editeur pour une image a uploader
 *
 * @author scachat
 */
public class BlockFileUpload extends BlockField<Row> implements UploadListener {

    protected UploadSelectEx us;
    protected Label view;
    protected byte[] content;
    protected String contentType;

    public BlockFileUpload(BlockField x) {
        super(x);
        buildEditor();
    }

    public BlockFileUpload(LocalisedItem li, String property) {
        super(li, property);
        buildEditor();
    }

    private void buildEditor() {
        editor = new Row() {
            @Override
            public void dispose() {
                super.dispose();
                try {
                    us.removeUploadListener(BlockFileUpload.this);
                } catch (TooManyListenersException ex) {
                    Logger.getLogger(BlockFileUpload.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        us = new UploadSelectEx();
        us.setEnabledSendButtonText(getLocalisedItem().getBaseString("upload"));
        us.setDisabledSendButtonText(getLocalisedItem().getBaseString("noupload"));
//        us.setHeight(new Extent(1024));
//        us.setWidth(new Extent(1024));

        editor.add(us);
        view = new LabelEx();
        editor.add(view);
        us.addUploadListener(this);

    }

    @Override
    public void uploadComplete(UploadEvent uploadEvent) {
        try {
            Upload ul = uploadEvent.getUpload();
            byte[] rawContent = new byte[(int) ul.getSize()];
            ul.getInputStream().read(rawContent, 0, (int) ul.getSize());
            setContent(rawContent);
            contentType=ul.getContentType();
        } catch (IOException ex) {
            setContent(null);
            contentType=null;
            Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void uploadSend() {
        content = null;
    }

    protected void setContent(byte[] content) {
        this.content = content;
        if (content == null) {
            view.setIcon(null);
            view.setText(null);
            error.setText(localisedItem.getBaseString("noimage"));
        } else {
            this.content = content;
            view.setText(content.length + " bytes");
            error.setText((String) null);
        }
    }

    @Override
    public void copyObjectToUi() {
        setContent((byte[]) BeanTools.getRaw(getParent().getCurrent(), property));
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            BeanTools.setRaw(getParent().getCurrent(), property, content);
            error.setText((String) null);
            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }
}
