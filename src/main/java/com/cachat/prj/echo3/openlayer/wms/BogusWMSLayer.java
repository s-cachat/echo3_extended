package com.cachat.prj.echo3.openlayer.wms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;

/**
 * un pseudo calque "license invalide"
 * @author scachat
 */
public class BogusWMSLayer extends LayerWMS {

   /**
    * fabrique l'image
    * @param srs le srs
    * @param minLat latitude du coin NW
    * @param minLon longitude du coin NW
    * @param maxLat latitude du coin SE
    * @param maxLon longitude du coint SE
    * @param width largeur en pixel
    * @param height longueur en pixel
    * @return l'image png
    */
   @Override
   public byte[] makeImage(String srs, double minLat, double minLon, double maxLat, double maxLon, int width, int height,Date dateRef) throws IOException {
      System.err.println(String.format("WMS WMS WMS bbox=%1.5f,%1.5f %1.5f,%1.5f, size=%dx%d, srs=%s", minLat, minLon, maxLat,
         maxLon, width, height, srs));
      BufferedImage bu = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
      Graphics g = bu.getGraphics();
      g.setColor(Color.RED);
      g.drawRect(2, 2, width - 4, height - 4);
      g.drawLine(0, 0, width, height);
      g.drawLine(0, height, width, 0);
      g.setColor(Color.BLACK);
      g.drawString(String.format("Licence invalide"), 5, 5);
      // send back the data
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ImageIO.write(bu, "png", bos);
      return bos.toByteArray();
   }
}
