package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.ng.ContainerEx;

/**
 * la fenetre de connexion
 */
public class LoginBasePaneV6 extends BaseMainPaneV6 implements WithErrorStatusField {

    /**
     * la fenetre
     */
    private final LoginWindow window;

    /**
     * Constructeur
     *
     * @param app l'instance de l'application
     */
    public LoginBasePaneV6(BaseApp app) {
        super(app);
        window = new LoginWindow(app);
        window.setClosable(false);
        add(window);
        window.setParent(this);
        backBtn.setVisible(false);
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
