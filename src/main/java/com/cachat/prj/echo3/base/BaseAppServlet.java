package com.cachat.prj.echo3.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextapp.echo.webcontainer.WebContainerServlet;
import com.cachat.net.BrowserCapability;
import com.cachat.prj.echo3.interfaces.User;
import java.util.Enumeration;
import jakarta.servlet.http.HttpSession;
import nextapp.echo.app.ApplicationInstance;

/**
 * servlet de base, intégrant quelques services supplémentaires à ceux d'écho3.
 * Cherche entre autre les capacités du navigateur, rend accessible les cookies.
 *
 * @author scachat
 */
public abstract class BaseAppServlet extends WebContainerServlet {

    /**
     * Logger local
     */
    private static final transient Logger logger = Logger.getLogger(BaseAppServlet.class.getName());
    /**
     * variable TL pour l'acces aux cookies
     */
    private static final ThreadLocal<Cookies> cookiesTL = new ThreadLocal<>();
    /**
     * variable TL pour l'acces aux cookies
     */
    private static final ThreadLocal<HttpServletRequest> requestTL = new ThreadLocal<>();
    /**
     * variable TL pour l'acces aux cookies
     */
    private static final ThreadLocal<HttpServletResponse> responseTL = new ThreadLocal<>();
    /**
     * variable TL pour l'adresse IP du client
     */
    private static final ThreadLocal<String> clientAddress = new ThreadLocal<>();
    /**
     * variable TL pour les caracteristiques navigateur
     */
    private static final ThreadLocal<BrowserCapability> browserCapTL = new ThreadLocal<>();
    /**
     * variable TL pour l'instance
     */
    private static final ThreadLocal<BaseApp> appTL = new ThreadLocal<>();

    /**
     * variable TL pour les elements d'identification sso echo3
     */
    private static final ThreadLocal<LoginSsoSlave> loginSSO = new ThreadLocal<>();

    /**
     * variable TL pour les elements d'identification externe
     */
    private static final ThreadLocal<LoginExterne> loginExterne = new ThreadLocal<>();

    /**
     * donne les information de login sso (ou null si non applicable). Ne peut
     * être appelé qu'une fois, l'appel détruisant l'information.
     *
     * @return le jeton d'authentification sso, a validé avec la clé avant usage
     */
    public static LoginExterne getLoginExterne() {
        LoginExterne loginExt = loginExterne.get();
        if (loginExt != null) {
            loginExterne.set(null);
        }
        return loginExt;
    }

    /**
     * stocke les informations de login dans la session.
     *
     * @param message le mssage de succès ou d'erreur
     * @param session la session http
     * @param user l'utilisateur ou null
     *
     */
    public static void setLoginExterne(HttpSession session, User user, String message) {
        LoginExterne loginExt = new LoginExterne();
        loginExt.setMessage(message);
        loginExt.setUser(user);
        session.setAttribute("loginSso", loginExt);
    }

    /**
     * donne les information de login sso echo3 (ou null si non applicable)
     *
     * @return le jeton d'authentification sso, a validé avec la clé avant usage
     */
    public static LoginSsoSlave getLoginSSO() {
        LoginSsoSlave lsso = loginSSO.get();
        if (lsso != null) {
            lsso.done();
        }
        return lsso;
    }

    /**
     * supprime le login sso
     */
    public static void clearLoginSSO() {
        loginSSO.set(null);
    }

    /**
     * donne l'application liée au thread. Utilisable dans doProcess uniquement
     *
     * @return l'instance
     */
    public BaseApp getApp() {
        return appTL.get();
    }

    /**
     * donne les information de cookies pour le thread de cette requete
     *
     * @return les cookies de la requete
     */
    public static Cookies getCookies() {
        return cookiesTL.get();
    }

    /**
     * Ajoute un cookie
     *
     * @param cookie le cookie
     */
    public static void setCookie(Cookie cookie) {
        HttpServletResponse res = responseTL.get();
        if (res != null) {
            res.addCookie(cookie);
        } else {
            logger.severe("Not setting cookie " + cookie.getName() + ", response is null");
        }
    }

    /**
     * donne l'adresse du client pour le thread de cette requete
     *
     * @return l'adresse ip
     */
    public static String getRemoteAddr() {
        return clientAddress.get();
    }

    /**
     * Creates a new <code>ApplicationInstance</code> for visitor to an
     * application. Calls buildApplicationInstance which can be overridden
     *
     * @return a new <code>ApplicationInstance</code>
     */
    @Override
    public final ApplicationInstance newApplicationInstance() {
        BaseApp app = buildApplicationInstance();
        appTL.set(app);
        return app;
    }

    /**
     * Crée une nouvelle instance d'application
     *
     * @return l'instance
     */
    public abstract BaseApp buildApplicationInstance();

    /**
     * donne les possibilités du navigateur
     *
     * @return l'adresse ip du client
     */
    public static BrowserCapability getBrowserCapability() {
        return browserCapTL.get();
    }

    /**
     * traite une requete. Stock un éventuel login sso, puis passe la main
     *
     * @param request la requete http
     * @param response la réponse http
     * @throws IOException en cas d'exception
     * @throws ServletException en cas d'exception
     */
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String sso = request.getParameter("sso");
        LoginSsoSlave lsso = (LoginSsoSlave) request.getSession().getAttribute("lsso");
        if (sso != null) {
            lsso = new LoginSsoSlave(sso);
            request.getSession().setAttribute("lsso", lsso);
        }
        loginSSO.set(lsso);
        LoginExterne loginExt = (LoginExterne) request.getSession().getAttribute("loginSso");
        loginExterne.set(loginExt);
        try {
            super.process(request, response);
        } finally {
            loginSSO.set(null);
            loginExterne.set(null);
        }
    }

    /**
     * conteneur pour les cookies
     */
    public static class Cookies {

        /**
         * les cookies et leur valeur
         */
        private final Map<String, String> cookies = new HashMap<>();
        /**
         * la reponse http
         */
        private final HttpServletResponse res;
        /**
         * ip du propriétaire
         */
        private final String ip;

        /**
         * donne un cookie
         *
         * @param name le nom du cookie  
         * @return sa valeur
         */
        public String getCookie(String name) {
            return cookies.get(name);
        }

        /**
         * donne l'ip du proprietaire
         *
         * @return l'ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * fixe la valeur d'un cookies
         *
         * @param name le nom du cookie  
         * @param value sa valeur
         */
        public void setCookie(String name, String value) {
            cookies.put(name, value);
            Cookie c = new Cookie(name, cookies.get(name));
            c.setPath("/");
            c.setMaxAge(60 * 60 * 24 * 365);
            c.setComment("persistent data for WebInforoute");
            res.addCookie(c);
        }

        public Cookies(HttpServletRequest req, HttpServletResponse res) {
            if (req.getCookies() != null) {
                for (Cookie c : req.getCookies()) {
                    cookies.put(c.getName(), c.getValue());
                }
            }
            ip = req.getRemoteAddr();
            this.res = res;
        }
    }

    /**
     * Processes a HTTP request and generates a response. subclass must override
     * doProcess(request,response)
     *
     * @param request the incoming <code>HttpServletRequest</code>
     * @param response the outgoing <code>HttpServletResponse</code>
     */
    @Override
    protected final void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            clientAddress.set(request.getRemoteAddr());
            BrowserCapability bc = (BrowserCapability) request.getSession().getAttribute("browserCapabilities");
            if (bc == null) {
                bc = new BrowserCapability();
                bc.parseUserAgent(request.getHeader("User-Agent"), request.getRemoteAddr());
            }
            browserCapTL.set(bc);
            cookiesTL.set(new Cookies(request, response));
            requestTL.set(request);
            responseTL.set(response);
            doProcess(request, response);
            requestTL.set(null);
            responseTL.set(null);
            BaseApp app = appTL.get();
            if (app != null) {
                request.getSession().setAttribute(getAppName(), app);
            }
        } catch (IOException | ServletException | RuntimeException e) {
            logger.log(Level.SEVERE, "Erreur", e);
            throw e;
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Erreur", e);
            throw new ServletException(e);
        } finally {
            appTL.set(null);
            clientAddress.set(null);
            cookiesTL.set(null);
            browserCapTL.set(null);
        }
    }

    /**
     * donne le nom par défaut de l'instance de BaseApp en session
     *
     * @return le nom
     */
    public String getAppName() {
        return BaseApp.DEFAULT_INSTANCE_NAME;
    }

    /**
     * déconnecte la session lié au thread
     */
    public static void logout() throws java.lang.IllegalStateException {
        HttpServletRequest req = requestTL.get();
        HttpServletResponse res = responseTL.get();
        HttpSession s = req.getSession();
        if (s == null) {
            logger.severe("No session : called from the right display thread ?");
        } else {
            for (Enumeration<String> en = s.getAttributeNames(); en.hasMoreElements();) {
                String n = en.nextElement();
                s.setAttribute(n, null);
            }
        }
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "loginIface", "loginName", "persistentLogin" -> {/*nop*/
                    }
                    default -> {
                        cookie.setMaxAge(0);
                        cookie.setValue("");
                        res.addCookie(cookie);
                    }
                }
            }
        }
    }

    /**
     * donne accès à la session http
     *
     * @return la session http si existante
     */
    public static HttpSession getSession() {
        HttpServletRequest req = requestTL.get();
        if (req == null) {
            return null;
        }
        return req.getSession();
    }
}
