package com.cachat.prj.echo3.components;

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
 * @author user1
 */
@WebServlet(name = "GenericCssServlet", urlPatterns = "/echo3extended/css/*")
public class GenericCssServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
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
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        if (url.contains("..") || url.contains("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        URL u = getClass().getResource("css/" + url);
        if (u == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "css/" + url);
            return;
        }
        if (url.endsWith(".css")) {
            response.setContentType("text/css");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "?/" + url);
            return;
        }
        URLConnection c = u.openConnection();
        long l = c.getContentLengthLong();
        if (l >= 0) {
            response.setHeader("Content-Length", String.valueOf(l));
        }

        byte[] buffer = new byte[4096];
        try (InputStream in = c.getInputStream(); OutputStream out = response.getOutputStream();) {
            int x;
            while ((x = in.read(buffer)) >= 0) {
                out.write(buffer, 0, x);
            }
        }
    }
}
