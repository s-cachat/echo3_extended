package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.menu.BaseMenuManager;

/**
 * la fenetre de connexion
 */
public class LoginBasePaneV6 extends BaseMainPaneV6 implements WithErrorStatusField {

    /**
     * la fenetre
     */
    private final LoginWindow window;

    /**
     * Constructeur a utiliser dans le cas particulier ou on a un menu au niveau
     * login
     *
     * @param app
     * @param menuManager
     */
    protected LoginBasePaneV6(BaseApp app, BaseMenuManager menuManager) {
        super(app, menuManager);
        window = new LoginWindow(app);
        window.setClosable(false);
        add(window);
        window.setParent(this);
        backBtn.setVisible(false);
    }

    /**
     * Constructeur
     *
     * @param app l'instance de l'application
     */
    public LoginBasePaneV6(BaseApp app) {
        this(app, null);//dans ce cas particulier, pas de menu

    }

    @Override
    public void updateMenu() {
        //nop
    }

    @Override
    public void setErrorMsg(String msg) {
        window.setErrorMsg(msg);
    }
}
