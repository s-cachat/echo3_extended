package com.cachat.prj.echo3.base;

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
@WebServlet(urlPatterns = "/")
public class RedirectServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Strict-Transport-Security", "max-age=31536000");
        response.addHeader("Permissions-Policy", "geolocation=*");
        response.addHeader("Permissions-Policy", "camera=*");
        response.addHeader("Content-Security-Policy", "frame-ancestors 'self'");
            response.addHeader("Content-Security-Policy", "default-src 'self'");
            response.addHeader("Content-Security-Policy", "script-src 'self'");
        response.setHeader("Referrer-Policy", "origin");
        response.sendRedirect("./app");
    }

}
