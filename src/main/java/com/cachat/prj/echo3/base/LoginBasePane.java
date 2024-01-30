/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cachat.prj.echo3.base;

import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;
import nextapp.echo.app.FillImage;
import nextapp.echo.app.FillImageBorder;
import nextapp.echo.app.Insets;

/**
 * la fenetre de connexion
 */
public class LoginBasePane extends BaseMainPane implements WithErrorStatusField {

    /**
     * la fenetre
     */
    private final LoginWindow window;

    /**
     * Constructeur
     *
     * @param app l'instance de l'application
     */
    public LoginBasePane(BaseApp app) {
        super(app);
        window = new LoginWindow(app);
        window.setClosable(false);
        window.setTitleBackgroundImage(new FillImage(app.getStyles().getImage("titleWindowBck.png")));
        window.setTitleHeight(new Extent(35));
        window.setTitleInsets(new Insets(5, 6, 0, 0));
        window.setBorder(new FillImageBorder(Color.BLACK, new Insets(2), new Insets(2)));
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
