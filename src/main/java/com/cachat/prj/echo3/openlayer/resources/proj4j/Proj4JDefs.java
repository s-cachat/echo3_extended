package com.cachat.prj.echo3.openlayer.resources.proj4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author scachat
 */
@WebServlet(urlPatterns = {"/defs/*", "/projCode/*"})
public class Proj4JDefs extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = req.getPathInfo();
        int i = s.lastIndexOf('/');
        if (i >= 0) {
            s = s.substring(i + 1);
        }
        try (InputStream in = getClass().getResourceAsStream(s)) {
            OutputStream out = resp.getOutputStream();
            byte[] buffer = new byte[4096];
            while ((i = in.read(buffer)) > 0) {
                out.write(buffer, 0, i);
            }
        }
    }

}
