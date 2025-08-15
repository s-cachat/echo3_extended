/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cachat.prj.echo3.base;

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
