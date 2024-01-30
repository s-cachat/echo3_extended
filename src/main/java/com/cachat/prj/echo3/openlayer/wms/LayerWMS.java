package com.cachat.prj.echo3.openlayer.wms;

import com.cachat.util.LruCache;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author scachat
 */
public abstract class LayerWMS {

    public LayerWMS() {

    }

    /**
     * methode appellee quand le cache n'est plus utile
     */
    public void dispose() {
    }
    protected LruCache<String, byte[]> cache = new LruCache<>(10);

    protected void makeImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get parameters
        String version = request.getParameter("VERSION");
        if (version == null) {
            version = request.getParameter("version");
        }

        String prequest = request.getParameter("REQUEST");
        if (prequest == null) {
            prequest = request.getParameter("request");
        }

        String srs = request.getParameter("SRS");
        if (srs == null) {
            srs = request.getParameter("srs");
        }

        String bbox = request.getParameter("BBOX");
        if (bbox == null) {
            bbox = request.getParameter("bbox");
        }

        String tmp = request.getParameter("WIDTH");
        if (tmp == null) {
            tmp = request.getParameter("width");
        }
        int width =  Double.valueOf(tmp).intValue();

        tmp = request.getParameter("HEIGHT");
        if (tmp == null) {
            tmp = request.getParameter("height");
        }
        int height = Double.valueOf(tmp).intValue();

        String styles = request.getParameter("STYLES");
        if (styles == null) {
            styles = request.getParameter("styles");
        }

        String format = request.getParameter("FORMAT");
        if (format == null) {
            format = request.getParameter("format");
        }

        String bgcolor = request.getParameter("BGCOLOR");
        if (bgcolor == null) {
            bgcolor = request.getParameter("bgcolor");
        }

        boolean useTransparent = false;
        String transparent = request.getParameter("TRANSPARENT");
        if (transparent == null) {
            transparent = request.getParameter("transparent");
        }
        if (transparent != null) {
            if (transparent.equalsIgnoreCase("true")) {
                useTransparent = true;
            }
        }
        String exceptions = request.getParameter("EXCEPTIONS");
        if (exceptions == null) {
            exceptions = request.getParameter("exception");
        }
        Date dateRef = null;
        String qs = request.getQueryString();
        String _dateRef = request.getParameter("dateRef");
        if (_dateRef != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy_HH:mm");
            try {
                dateRef = sdf.parse(_dateRef);
            } catch (ParseException pe) {
                //nop
            }
        }
        StringTokenizer st = new StringTokenizer(bbox, ",");
        double minY = Double.valueOf(st.nextToken()).doubleValue();
        double minX = Double.valueOf(st.nextToken()).doubleValue();
        double maxY = Double.valueOf(st.nextToken()).doubleValue();
        double maxX = Double.valueOf(st.nextToken()).doubleValue();
        String key = String.format("%s,%1.6f,%1.6f,%1.6f,%1.6f,%d,%d,%d", srs, minX, minY, maxX, maxY, width, height,
                dateRef == null ? 0 : dateRef.getTime());
        byte[] data = getFromCache(key);
        data = null;//TODO remove
        if (data == null) {
            double oShift = 2 * Math.PI * 6378137 / 2.0;
            double minLon = minX / oShift * 180.0;
            double minLat = minY / oShift * 180.0;
            //minLat = 180 / Math.PI * (2 * Math.atan(Math.exp(minLat * Math.PI / 180.0)) - Math.PI / 2.0);
            minLon = 180 / Math.PI * (2 * Math.atan(Math.exp(minLon * Math.PI / 180.0)) - Math.PI / 2.0);
            double maxLon = maxX / oShift * 180.0;
            double maxLat = maxY / oShift * 180.0;
            //maxLat = 180 / Math.PI * (2 * Math.atan(Math.exp(maxLat * Math.PI / 180.0)) - Math.PI / 2.0);
            maxLon = 180 / Math.PI * (2 * Math.atan(Math.exp(maxLon * Math.PI / 180.0)) - Math.PI / 2.0);
            data = makeImage(srs, minLon, minLat, maxLon, maxLat, width, height, dateRef);
            addToCache(key, data);
        }
        response.setContentType("image/png");
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
    }

    /**
     * donne l'image contenue dans le cache
     *
     * @param key la clé
     * @return l'image
     */
    protected byte[] getFromCache(String key) {
        return cache.get(key);
    }

    /**
     * met dans le cache une image
     *
     * @param key la clé
     * @param data l'image
     */
    protected void addToCache(String key, byte data[]) {
        cache.put(key, data);
    }

    /**
     * reinitialise le cache d'image
     */
    public void reset() {
        cache.clear();
    }

    /**
     * fabrique l'image
     *
     * @param srs le srs
     * @param minLat latitude du coin NW
     * @param minLon longitude du coin NW
     * @param maxLat latitude du coin SE
     * @param maxLon longitude du coint SE
     * @param width largeur en pixel
     * @param height longueur en pixel
     * @return l'image png
     */
    public abstract byte[] makeImage(String srs, double minLat, double minLon, double maxLat, double maxLon, int width, int height,
            Date dateRef)
            throws IOException;
}
