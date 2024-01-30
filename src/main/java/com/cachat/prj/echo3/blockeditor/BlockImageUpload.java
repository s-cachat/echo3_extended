package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.editor.BasicEditor;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.UploadSelectEx;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jakarta.validation.Validator;
import nextapp.echo.app.AwtImageReference;
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
public class BlockImageUpload extends BlockField<Row> implements UploadListener {

    protected UploadSelectEx us;
    protected Label view;
    protected BufferedImage content;
    protected int maxWidth;
    protected int maxHeight;
    protected String contentType;

    public BlockImageUpload(BlockField x) {
        super(x);
        maxHeight = ((BlockImageUpload) x).maxHeight;
        maxWidth = ((BlockImageUpload) x).maxWidth;
        buildEditor();
    }

    public BlockImageUpload(LocalisedItem li, String property, int maxWidth, int maxHeight) {
        super(li, property);
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        buildEditor();
    }

    private void buildEditor() {
        editor = new Row() {
            @Override
            public void dispose() {
                super.dispose();
                try {
                    us.removeUploadListener(BlockImageUpload.this);
                } catch (TooManyListenersException ex) {
                    Logger.getLogger(BlockImageUpload.class.getName()).log(Level.SEVERE, null, ex);
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
            ByteArrayInputStream in = new ByteArrayInputStream(rawContent);
            setContent(ImageIO.read(in));
            contentType = ul.getContentType();
        } catch (IOException ex) {
            setContent(null);
            Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void uploadSend() {
        content = null;
    }

    protected void setContent(BufferedImage content) {
        this.content = content;
        if (content == null) {
            view.setIcon(null);
            error.setText(localisedItem.getBaseString("noimage"));
        } else {
            AffineTransform tx = new AffineTransform();
            double sx = 32d / content.getWidth();
            double sy = 32d / content.getHeight();
            double s = Math.max(sx, sy);
            tx.scale(s, s);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage icon = op.filter(content, null);
            if (maxWidth > 0 && maxHeight > 0 && (content.getWidth() > maxWidth || content.getHeight() > maxHeight)) {
                tx = new AffineTransform();
                sx = ((double) maxWidth) / content.getWidth();
                sy = ((double) maxHeight) / content.getHeight();
                s = Math.max(sx, sy);
                tx.scale(s, s);
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                this.content = op.filter(content, null);
            } else {
                this.content = content;
            }
            view.setIcon(new AwtImageReference(icon));
            error.setText((String) null);
        }
    }

    @Override
    public void copyObjectToUi() {
        setContent((BufferedImage) BeanTools.getRaw(getParent().getCurrent(), property));
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            final Object current = getParent().getCurrent();
            if (current == null) {
                return false;
            }
            BeanTools.setRaw(current, property, content);
            error.setText((String) null);
            return validateProperty(validator, current, property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }
}
