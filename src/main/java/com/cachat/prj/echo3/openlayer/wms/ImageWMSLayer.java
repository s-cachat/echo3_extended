package com.cachat.prj.echo3.openlayer.wms;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import javax.imageio.ImageIO;

/**
 * un calque WMS avec cache disque
 *
 * @author scachat
 */
public class ImageWMSLayer extends LayerWMS {

    /**
     * l'url de l'image
     */
    private final String url;
    /**
     * l'image
     */
    private final BufferedImage buffer;
    private final double origLat;
    private final double origLon;
    private final double maxLat;
    private final double maxLon;
    private final double hScale;
    private final double vScale;

    /**
     * constructeur
     */
    public ImageWMSLayer(String url, double minLat, double minLon, double maxLat, double maxLon) throws IOException {
        this.url = url;
        URL u = new URL(url);
        buffer = ImageIO.read(u.openStream());
        this.origLat = Math.min(minLat, maxLat);
        this.origLon = Math.min(minLon, maxLon);
        this.maxLat = Math.max(minLat, maxLat);
        this.maxLon = Math.max(minLon, maxLon);
        this.hScale = buffer.getWidth() / (this.maxLon - this.origLon);
        this.vScale = buffer.getHeight() / (this.maxLat - this.origLat);
    }

    @Override
    public byte[] makeImage(String srs, double minLat, double minLon, double maxLat, double maxLon, int width, int height,
            Date dateRef) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        int x = 0;
        int y = 0;
        if (maxLat < this.origLat
                || maxLon < this.origLon
                || minLat > this.maxLat
                || minLon > this.maxLon) {
            g.setColor(Color.RED);
        } else {
            double xs = (width / (maxLon - minLon)) / hScale;
            double ys = (height / (maxLat - minLat)) / vScale;
            x = (int) ((minLon - origLon) * (width / (maxLon - minLon)));
            y = (int) (buffer.getHeight() * ys - ((minLat - origLat) * (height / (maxLat - minLat))) - height);
            g.setTransform(new AffineTransform(
                    xs, 0d,
                    0d, ys,
                    -x, -y));

            g.drawImage(buffer, 0, 0, null);
            g.setTransform(new AffineTransform());

            g.setColor(Color.BLUE);
        }

//      g.drawRect(5, 5, width - 10, height - 10);
//      g.drawLine(5, 5, width - 10, height - 10);
//      g.drawLine(5, height - 10, width - 10, 5);
//
//      String msg;
//      int th = g.getFont().getSize();
//
//      msg = String.format(Locale.ENGLISH, "%s\n%1.5f , %1.5f\n%1.5f , %1.5f\n%d x %d\nscale=%1.5fx%1.5f\norig=%dx%d", srs, minLat,
//         minLon, maxLat, maxLon, width,
//         height, hScale, vScale, x, y);
//      y = 0;
//      for (String s : msg.split("\n")) {
//         int tw = g.getFontMetrics().stringWidth(s);
//         g.setColor(new Color(255, 255, 255, 128));
//         g.fillRect(30, y * th + 30 - th, tw, th);
//         g.setColor(Color.RED);
//         g.drawString(s, 30, y * th + 30);
//         y++;
//      }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", bos);
        bos.flush();
        return bos.toByteArray();
    }
}
