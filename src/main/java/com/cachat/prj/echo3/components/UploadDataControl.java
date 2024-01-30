package com.cachat.prj.echo3.components;

import com.cachat.prj.echo3.editor.BasicEditor;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.UploadSelectEx;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.filetransfer.app.event.UploadEvent;
import nextapp.echo.filetransfer.app.event.UploadListener;
import nextapp.echo.filetransfer.model.Upload;

/**
 * utilitaire pour l'upload d'images
 *
 * @author scachat
 */
public class UploadDataControl extends Row implements UploadListener {

    UploadSelectEx us;
    Label size;
    byte[] content;
    private final LabelEx err;
    BasicEditor parent;

    public UploadDataControl(BasicEditor parent, LabelEx err) {
        us = new UploadSelectEx();
        us.setEnabledSendButtonText(parent.getBaseString("upload"));
        us.setDisabledSendButtonText(parent.getBaseString("noupload"));
        us.setWidth(new Extent(75, Extent.PERCENT));
        add(us);
        size = new LabelEx();
        add(size);
        this.err = err;
        this.parent = parent;
        us.setWidth(new Extent(200));
        us.addUploadListener(this);

    }

    @Override
    public void dispose() {
        try {
            us.removeUploadListener(this);
        } catch (TooManyListenersException ex) {
            Logger.getLogger(UploadDataControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
        if (err != null) {
            if (content == null) {
                err.setText(parent.getBaseString(""));
                size.setText("");
            } else {
                size.setText("Fichier de " + content.length + " octets");
            }
        }
    }
    private List<UploadListener> listeners = new ArrayList<>();

    public void addUploadListener(UploadListener listener) {
        listeners.add(listener);
    }

    public void removeUploadListener(UploadListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void uploadComplete(UploadEvent uploadEvent) {
        try {
            if (uploadEvent.getUpload().getStatus() == Upload.STATUS_COMPLETE) {
                Upload ul = uploadEvent.getUpload();
                byte[] rawContent = new byte[(int) ul.getSize()];
                ul.getInputStream().read(rawContent, 0, (int) ul.getSize());
                setContent(rawContent);
                for (UploadListener listener : listeners) {
                    listener.uploadComplete(uploadEvent);
                }
            }
        } catch (IOException ex) {
            setContent(null);
            Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void uploadSend() {
//nop
    }
}
