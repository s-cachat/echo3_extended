/*
 * (c) 2025 Stéphane Cachat stephane@cachat.com. No reuse or distribution allowed. Réutilisation ou redistribution interdite.
 */
package com.cachat.prj.echo3.ng.menu;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.BasicWindow;
import static com.cachat.prj.echo3.base.BasicWindow.FULL_WIDTH;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.ContainerEx;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;

/**
 *
 * @author scachat
 */
public class MenuPane extends BasicWindow {

    ContainerEx base;

    public MenuPane(BaseApp app) {
        super(app, "Menu", "Menu", 800, 600);
        add(base = new ContainerEx(0, 0, 0, 0, null, null));
        base.setFlexWrap(ContainerEx.FLEX_WRAP_WRAP);
        base.setFlexDirection(ContainerEx.FLEX_DIRECTION_RESPONSIVE);
        base.setInsets(new Insets(12, 15));

        setTitle("");
    }

    public void addMenuItem2(ButtonEx b) {

        MenuButton mb;
        ContainerEx ca = new ContainerEx(mb = new MenuButton(b));

        ContainerEx cb = new ContainerEx(ca);//container flex 
        cb.setFlexBasis("33%");
        cb.setFlexGrow(0.0);
        cb.setFlexShrink(0.0);
        cb.setInsets(new Insets(16, 16));
        
        ca.setJustifyContent("center");//container pour imposer une marge entre boutons
        ca.setFlexDirection("column");
        ca.setWidth(FULL_WIDTH);
        ca.setHeight(FULL_WIDTH);
        ca.setRadius(new Insets(4, 4));
        ca.setShadow("0px 3px 1px -2px rgb(0 0 0 / 20%), 0px 2px 2px 0px rgb(0 0 0 / 14%), 0px 1px 5px 0px rgb(0 0 0 / 12%)");
        base.add(cb);
        mb.setStyleName("MenuPaneButton");
    }

    /**
     * un bouton pour la page intermédiaire avec les menu 2
     */
    public static class MenuButton extends ButtonEx {

        /**
         * Constructeur
         *
         * @param b le bouton de référence
         */
        private MenuButton(ButtonEx b) {
            super(b.getText().toUpperCase());
            setStyleName("MenuPaneButton");
//            setHeight(FULL_WIDTH);
//            setWidth(FULL_WIDTH);
//            setAlignment(Alignment.ALIGN_CENTER);
            addActionListener(e -> {
                b.fireActionPerformed(e);
            });
        }
    }
}
