package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.interfaces.User;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;
import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet, avec récupération de l'identité de l'utilisateur. Cette identité
 * sera stockée dans l'attribut user de l'objet requete. L'application sera dans
 * l'attribut app de la requete. Si aucune identité n'est trouvée, une erreur
 * 401 est renvoyée. L'authentification se fait soit sur des paramètres d'url
 * (username et pass), soit en authentification http basic.
 *
 * @author scachat
 */
public abstract class AbstractAppServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AbstractAppServlet.class.getName());
    /**
     * Prefix to use for <code>UserInstanceContainer</code>
     * <code>HttpSession</code> keys.
     */
    private static final String USER_INSTANCE_CONTAINER_SESSION_KEY_PREFIX = "EchoUserInstanceContainer";

    static String getUserInstanceContainerSessionKey(GenericServlet servlet) {
        return USER_INSTANCE_CONTAINER_SESSION_KEY_PREFIX + ":" + servlet.getServletName();
    }
    /**
     * La session
     */
    private final static ThreadLocal<HttpSession> session = new ThreadLocal<>();
    /**
     * La session
     */
    private final static ThreadLocal<BaseApp> app = new ThreadLocal<>();

    /**
     * donne l'application courante
     *
     * @return l'application
     */
    public static BaseApp getCurrentApp() {
        return app.get();

    }

    /**
     * fonction d'authentification annexe
     *
     * @param req la requete http
     * @param res la reponse http
     * @return un utilisateur si des paramètres spécifiques ont permis l'accès à
     * cette ressource, null si l'accès n'est pas autorisé via cette fonction
     * @throws IOException en cas d'erreur d'entrée/sortie
     * @throws ServletException en cas d'erreur autre
     */
    protected User auxAuthenticate(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        return null;
    }

    /**
     * intercepte la requete pour extraire l'utilisateur
     *
     * @param req la requete http
     * @param res la reponse http
     * @throws IOException en cas d'erreur io
     * @throws ServletException en cas d'erreur autre, encapsulée dans une
     * ServletException
     */
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            BaseApp app = (BaseApp) req.getSession().getAttribute(getAppName());
            req.setAttribute("app", app);
            User user = app == null ? null : app.getUser();
            session.set(req.getSession());
            this.app.set(app);
            if (user == null) {
                user = auxAuthenticate(req, res);
            }
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
                        return;
                    }
                }
            }
            req.getSession().setAttribute("user", user);
            req.setAttribute("user", user);
            if (user == null) {
                res.sendError(401, "Non connecté");
                return;
            }

            super.service(req, res);
        } finally {
            session.set(null);
            app.set(null);
        }
    }

    /**
     * valide l'authentification de l'utilisateur
     *
     * @param user son nom
     * @param pass son mot de passe
     * @return le user si il est valide, null sinon
     */
    public User validLogin(String user, String pass) {
        return com.cachat.prj.echo3.security.SecurityManager.getInstance().validLogin(user, pass);
    }

    /**
     * donne le nom par défaut de l'instance de BaseApp en session
     *
     * @return le nom
     */
    public String getAppName() {
        return BaseApp.DEFAULT_INSTANCE_NAME;
    }
}
