package com.cachat.prj.echo3.synoptique;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author scachat
 */
@WebServlet(urlPatterns = "/synView/*")
public class SynViewServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path[] = req.getPathInfo().split("/");
        Synoptique s = SynManager.getInstance().getSynoptique(req.getSession(),path[1]);
        if (s == null) {
            resp.sendError(404, path[1]);
            return;
        }
        SynView v = s.getView(path[2]);
        if (v == null) {
            resp.sendError(404, path[2]);
            return;
        }
        byte[] c = v.getContent();
        if (c == null) {
            resp.sendError(500, "bad content type " + v.getContentType());
            return;
        }
        resp.setContentType(v.getContentType());
        resp.setContentLength(c.length);
        resp.getOutputStream().write(c);
    }
}
