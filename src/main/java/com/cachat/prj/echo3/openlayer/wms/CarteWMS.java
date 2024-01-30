package com.cachat.prj.echo3.openlayer.wms;

import com.cachat.prj.echo3.base.AbstractAppServlet;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * sert un calque wms. L'objet étendant LayerWMS doit être présent en session
 *
 * @author scachat
 */
@WebServlet(name = "CarteWMS", urlPatterns = "/wms")
public class CarteWMS extends AbstractAppServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String layers = request.getParameter("LAYERS");
            if (layers == null) {
                layers = request.getParameter("layers");
            }
            LayerWMS layer = WmsLayerRegistry.getInstance().getLayerWMS(layers, request.getSession());
            if (layer == null) {
                throw new NoSuchElementException(layers);
            }

            response.setContentType("image/png");
            layer.makeImage(request, response);
        } catch (Exception ex) {
            Logger.getLogger(CarteWMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    static LayerWMS l;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        handleRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
