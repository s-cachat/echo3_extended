package com.cachat.prj.echo3.openlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author scachat
 */
@WebServlet(name = "OpenLayerProxy", urlPatterns = "/ol/*")
public class OpenLayerStaticRessources extends /*TODO AbstractAppServlet*/ HttpServlet {

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getPathInfo();
        if (url.indexOf("..") >= 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        URL u = getClass().getResource("resources/" + url);
        if (u == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "ol " + url);
            return;
        }
        URLConnection c = u.openConnection();
        long l = c.getContentLengthLong();
        if (l >= 0) {
            response.setHeader("Content-Length", String.valueOf(l));
        }
        if (url.endsWith(".js")) {
            response.setContentType("text/javascript");
        } else if (url.endsWith(".gif")) {
            response.setContentType("image/gif");
        } else if (url.endsWith(".png")) {
            response.setContentType("image/png");
        } else if (url.endsWith(".jpg")) {
            response.setContentType("image/jpg");
        } else if (url.endsWith(".css")) {
            response.setContentType("text/css");
        } else if (url.endsWith(".js")) {
            response.setContentType("text/javascript");
        } else {
            response.setContentType(c.getContentType());
        }
        byte[] buffer = new byte[4096];
        InputStream in = c.getInputStream();
        OutputStream out = response.getOutputStream();
        int x;
        while ((x = in.read(buffer)) >= 0) {
            out.write(buffer, 0, x);
        }
        in.close();
        out.close();
    }
}
