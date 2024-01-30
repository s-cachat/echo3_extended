package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.base.BaseApp.Doc;
import com.cachat.prj.echo3.interfaces.User;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author scachat
 */
@WebServlet(name = "documents", urlPatterns = "/documents/*")
public class DocumentServlet extends AbstractAppServlet {
/**
 * notre logger
 */
    public static final Logger logger=Logger.getLogger(DocumentServlet.class.getName());
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String _id[] = request.getPathInfo().split("/");
        BaseApp app = (BaseApp) request.getAttribute("app");
        User user = (User) request.getAttribute("user");
        if (_id.length > 0 && user != null && app != null) {
            Doc d = app.findDoc(_id[1]);
            if (d == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (d.getUser().equals(user)) {
                response.setContentType(d.getContentType());
                if (d.getData() != null) {
                    response.setContentLength(d.getData().length);
                    //response.setHeader("Content-Disposition", String.format("inline, filename=%s", d.getName()));
                    response.getOutputStream().write(d.getData());
                } else if (d.getFile() != null) {
                    int todo = (int) d.getFile().length();
                    response.setContentLength(todo);
                    byte[] buffer = new byte[4096];
                    try (FileInputStream fis = new FileInputStream(d.getFile())) {
                        while (todo > 0) {
                            int r = fis.read(buffer);
                            if (r < 0) {
                                break;
                            }
                            response.getOutputStream().write(buffer);
                            todo -= r;
                        }
                    }
                }
                response.getOutputStream().close();
                logger.severe("Send document "+d.getName());
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, String.format("Utilisateur %s au lieu de %s", user, d.getUser()));
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, _id[0]);
        }
    }

}
