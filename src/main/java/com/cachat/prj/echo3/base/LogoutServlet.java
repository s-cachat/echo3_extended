package com.cachat.prj.echo3.base;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * servlet qui gère la déconnexion d'une utilisateur
 * 
 * @author user1
 */
@WebServlet(name = "logout", urlPatterns = {"/logout", "/logout/*"})
public class LogoutServlet extends HttpServlet {
    
    @Override
    public final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        process(request, response);
    }

    @Override
    public final void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        process(request, response);
    }
    
    /**
     * Processes an HTTP request and generates a response.
     *
     * @param request the incoming <code>HttpServletRequest</code>
     * @param response the outgoing <code>HttpServletResponse</code>
     * @throws java.io.IOException
     * @throws jakarta.servlet.ServletException
     */
    protected void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        session.getAttributeNames().asIterator().forEachRemaining(attr -> session.setAttribute(attr, null));
        response.sendRedirect("#");
    }
    
}
