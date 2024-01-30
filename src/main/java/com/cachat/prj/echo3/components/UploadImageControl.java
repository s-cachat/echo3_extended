package com.cachat.prj.echo3.components;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import com.cachat.prj.echo3.editor.BasicEditor;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.UploadSelectEx;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import nextapp.echo.app.AwtImageReference;
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
public class UploadImageControl extends Row implements UploadListener {

    UploadSelectEx us;
    Label view;
    BufferedImage content;
    private final LabelEx err;
    BasicEditor parent;
    int maxWidth;
    int maxHeight;

    public UploadImageControl(BasicEditor parent, LabelEx err, int maxWidth, int maxHeight) {
        us = new UploadSelectEx();
        us.setEnabledSendButtonText(parent.getBaseString("upload"));
        us.setDisabledSendButtonText(parent.getBaseString("noupload"));
        us.setWidth(new Extent(75, Extent.PERCENT));
        add(us);
        view = new LabelEx();
        add(view);
        this.err = err;
        this.parent = parent;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        us.setHeight(new Extent(maxHeight));
        us.setWidth(new Extent(200));

        us.addUploadListener(this);

    }

    @Override
    public void dispose() {
        try {
            us.removeUploadListener(this);
        } catch (TooManyListenersException ex) {
            Logger.getLogger(UploadImageControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void uploadComplete(UploadEvent uploadEvent) {
        try {
            if (uploadEvent.getUpload().getStatus() == Upload.STATUS_COMPLETE) {
                Upload ul = uploadEvent.getUpload();
                byte[] rawContent = new byte[(int) ul.getSize()];
                int r = ul.getInputStream().read(rawContent);
                System.err.println("Read " + r + "/" + rawContent.length + " byte");
                ByteArrayInputStream in = new ByteArrayInputStream(rawContent);
                setContent(ImageIO.read(in));
                for (UploadListener listener : listeners) {
                    listener.uploadComplete(uploadEvent);
                }
            }
        } catch (IOException ex) {
            setContent(null);
            Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private List<UploadListener> listeners = new ArrayList<>();

    public void addUploadListener(UploadListener listener) {
        listeners.add(listener);
    }

    public void removeUploadListener(UploadListener listener) {
        listeners.remove(listener);
    }

    public BufferedImage getContent() {
        return content;
    }

    public void setContent(BufferedImage content) {
        this.content = content;
        if (err != null) {
            if (content == null) {
                view.setIcon(null);
                err.setText(parent.getBaseString("noimage"));
            } else {
                AffineTransform tx = new AffineTransform();
                double sx = 32d / content.getWidth();
                double sy = 32d / content.getHeight();
                double s = Math.max(sx, sy);
                tx.scale(s, s);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                BufferedImage icon = op.filter(content, null);
                if (maxWidth > 0 && maxHeight > 0) {
                    tx = new AffineTransform();
                    sx = ((double) maxWidth) / content.getWidth();
                    sy = ((double) maxHeight) / content.getHeight();
                    s = Math.max(sx, sy);
                    tx.scale(s, s);
                    op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                    this.content = op.filter(content, null);
                }
                view.setIcon(new AwtImageReference(icon));
                err.setText("");
            }
        }
    }

    @Override
    public void uploadSend() {
    }
}
