/*
 * (c) 2025 Stéphane Cachat stephane@cachat.com. No reuse or distribution allowed. Réutilisation ou redistribution interdite.
 */
package com.cachat.prj.echo3.ng.menu;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.BasicWindow;
import com.cachat.prj.echo3.list.ActionButton;
import com.cachat.prj.echo3.ng.ContainerEx;
import nextapp.echo.app.Button;
import nextapp.echo.app.event.ActionListener;

/**
 * Page intermédiaire pour naviguer un sous menu
 * 
 * @author scachat
 */
public class MenuPane extends BasicWindow {

    /**
     * Conteneur principal
     */
    private final ContainerEx base;

    /**
     * Constructeur
     * 
     * @param app l'instance de application
     */
    public MenuPane(BaseApp app) {
        super(app, "Menu", "Menu", 800, 600);
        setTitle("");
        add(base = new ContainerEx());
        base.setStyleName("MenuPane");
    }

    /**
     * Ajoute un élément sur le menu à partir d'un composant existant.
     * 
     * @param b le bouton de référence
     */
    public void addMenuItem(Button b) {
        addMenuItem(b.getText(), e -> b.fireActionPerformed(e));
    }
    
    /**
     * Ajoute un élément sur le menu
     * 
     * @param text le text
     * @param actionListener le handler pour l'action
     */
    public void addMenuItem(String text, ActionListener actionListener) {
        Button button = new ActionButton(text, actionListener);
        button.setStyleName("MenuPaneButton");

        ContainerEx flexV = new ContainerEx(button);
        flexV.setStyleName("MenuPaneFlexCol");

        ContainerEx flexH = new ContainerEx(flexV);
        flexH.setStyleName("MenuPaneFlexRow");

        base.add(flexH);
    }

}
