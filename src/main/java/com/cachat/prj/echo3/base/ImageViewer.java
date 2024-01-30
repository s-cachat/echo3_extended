package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.components.DirectHtml;
import nextapp.echo.app.Extent;
import nextapp.echo.app.ImageReference;

/**
 * affiche une image
 *
 * @author scachat
 */
public class ImageViewer extends DirectHtml {

    private static long lastId = System.currentTimeMillis();
    private Extent width;
    private Extent height;
    private ImageReference image;
    private final BaseApp app;

    public ImageViewer(BaseApp app) {
        this.app = app;
        setId(String.valueOf(lastId++));
    }

    @Override
    public void dispose() {
        super.dispose();
        app.unregisterImageViewer(this);
    }

    public ImageReference getImage() {
        return image;
    }

    public final void setImage(ImageReference image) {
        this.image = image;
        update();
    }

    private void update() {
        app.registerImageViewer(this);
        StringBuilder sb = new StringBuilder();
        boolean ok = image != null;
        if (ok) {
            sb.append("<img src=\"imageViewer/").append(this.getId()).append("/img.jpg&noCache=").append(System.currentTimeMillis()).append(
                    "\" ");
        } else {
            sb.append("<div");
        }
        if (width != null) {
            sb.append("width=\"").append(width.getValue());
            switch (width.getUnits()) {
                case Extent.PERCENT:
                    sb.append("%");
                    break;
                case Extent.PX:
                default:
                    sb.append("px");
                    break;
            }
            sb.append("\" ");
        }
        if (height != null) {
            sb.append("height=\"").append(height.getValue());
            switch (height.getUnits()) {
                case Extent.PERCENT:
                    sb.append("%");
                    break;
                case Extent.PX:
                default:
                    sb.append("px");
                    break;
            }
            sb.append("\" ");
        }
        if (ok) {
            sb.append("/>");
        } else {
            sb.append(">-</div>");
        }

        setText(sb.toString());
    }

    public Extent getHeight() {
        return height;
    }

    public void setHeight(Extent height) {
        this.height = height;
    }

    public Extent getWidth() {
        return width;
    }

    public void setWidth(Extent width) {
        this.width = width;
    }
}
