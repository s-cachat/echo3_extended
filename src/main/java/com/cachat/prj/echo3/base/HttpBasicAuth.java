/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.interfaces.User;
import java.io.IOException;
import java.text.ParseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author scachat
 */
public class HttpBasicAuth {

    /**
     * valide l'authentification de l'utilisateur
     *
     * @param req la requete http
     * @param res la reponse http
     * @return le user si il est valide, null sinon
     * @throws ServletException en cas d'erreur
     * @throws IOException en cas d'erreur
     */
    public static User validAuthentification(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        BaseApp app = (BaseApp) req.getSession().getAttribute(BaseApp.DEFAULT_INSTANCE_NAME);
        req.setAttribute("app", app);
        User user = app == null ? null : app.getUser();
        if (user == null) {
            String name = req.getParameter("username");
            String pass = req.getParameter("pass");
            if (name != null && pass != null) {
                user = validLogin(name, pass);
            } else {
                String auth = req.getHeader("Authorization");
                if (auth != null) {
                    if (auth.toUpperCase().startsWith("BASIC ")) {
                        try {
                            String userpassEncoded = auth.substring(6);
                            com.cachat.util.Base64.decode(userpassEncoded);
                            String userpassDecoded = new String(com.cachat.util.Base64.decode(userpassEncoded));
                            int ti = userpassDecoded.indexOf(":");
                            if (ti > 0) {
                                name = userpassDecoded.substring(0, ti);
                                pass = userpassDecoded.substring(ti + 1);
                                user = validLogin(name, pass);
                            }
                        } catch (ParseException ex) {
                            throw new ServletException(ex);
                        }
                    }
                } else {
                    res.setHeader("WWW-Authenticate", "BASIC realm=\"trafx\"");
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return null;
                }
            }
        }
        req.getSession().setAttribute("user", user);
        req.setAttribute("user", user);
        if (user == null) {
            res.sendError(401, "Non connecté");
            return null;
        }

        return user;
    }

    /**
     * valide l'authentification de l'utilisateur
     *
     * @param user son nom
     * @param pass son mot de passe
     * @return le user si il est valide, null sinon
     */
    protected static User validLogin(String user, String pass) {
        return com.cachat.prj.echo3.security.SecurityManager.getInstance().validLogin(user, pass);
    }
}
