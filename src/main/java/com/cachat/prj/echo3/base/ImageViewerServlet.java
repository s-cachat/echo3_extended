package com.cachat.prj.echo3.base;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextapp.echo.app.AwtImageReference;
import nextapp.echo.app.HttpImageReference;
import nextapp.echo.app.ImageReference;

/**
 *
 * @author scachat
 */
@WebServlet(name = "imageViewer", urlPatterns = "/imageViewer/*")
public class ImageViewerServlet extends AbstractAppServlet {

    private static final Logger logger = Logger.getLogger("imageViewer");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "request from {0} for {1}", new Object[]{req.getRemoteAddr(), req.getContextPath()});
        String id = req.getContextPath().split("/")[1];
        BaseApp app = (BaseApp) req.getAttribute("app");
        if (app == null) {
            resp.sendError(403);
            return;
        }
        ImageViewer iv = app.getImageViewer(id);
        if (iv == null) {
            resp.sendError(404);
            return;
        }
        ImageReference ir = iv.getImage();
        if (ir instanceof HttpImageReference) {
            resp.sendRedirect(((HttpImageReference) ir).getUri());
        } else if (ir instanceof AwtImageReference) {
            AwtImageReference air = (AwtImageReference) ir;
            Image img = air.getImage();
            if (img instanceof RenderedImage) {
                resp.setContentType("img/jpeg");
                ImageIO.write((RenderedImage) air.getImage(), "jpeg", resp.getOutputStream());
            } else {
                resp.sendError(500, "Image is a " + img.getClass().getName());
            }
        }
    }

}
