package com.cachat.prj.echo3.base;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * une app servlet pour les fenêtre filles
 *
 * @author scachat
 */
public abstract class ChildAppServlet extends BaseAppServlet {

    /**
     * donne le nom par défaut de l'instance de BaseApp en session
     *
     * @return le nom
     */
    @Override
    public String getAppName() {
        return super.getAppName() + "_" + getServletName();
    }
    /**
     * variable TL pour l'application mère
     */
    private final static ThreadLocal<BaseApp> mainAppTL = new ThreadLocal<>();

    /**
     * donne l'instance de l'app principale
     *
     * @return l'instance de l'app principale
     */
    protected BaseApp getMainApp() {
        return mainAppTL.get();
    }

    /**
     * Crée une nouvelle instance d'application fille
     *
     * @return l'instance
     */
    @Override
    public abstract ChildApp buildApplicationInstance();

    @Override
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        mainAppTL.set((BaseApp) request.getSession().getAttribute(BaseApp.DEFAULT_INSTANCE_NAME));
        super.doProcess(request, response);
        ChildApp sapp = (ChildApp) request.getSession().getAttribute(getAppName());
        if (sapp != null) {
            sapp.setServletPath(request.getServletPath());
        }
        mainAppTL.set(null);
    }
}
