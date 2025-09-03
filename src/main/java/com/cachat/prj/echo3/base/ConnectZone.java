/*
 * (c) 2025 Stéphane Cachat stephane@cachat.com. No reuse or distribution allowed. Réutilisation ou redistribution interdite.
 */
package com.cachat.prj.echo3.base;

import nextapp.echo.app.Button;
import nextapp.echo.app.Row;

/**
 * un composant avec les informations de connexion / de l'utilisateur connecté
 *
 * @author scachat
 */
/**
 * zone d'info sur l'utilisateur connecté
 */
public class ConnectZone extends Row {

    /**
     * bouton pour l'accès au profil
     */
    private Button profilButton;
    /**
     * bouton pour se connecter
     */
    private Button loginButton;
    /**
     * bouton pour se déconnecter
     */
    private Button logoutButton;
    /**
     * l'application
     */
    private final BaseApp app;

    /**
     * Constructeur
     *
     * @param app l'application. On utilisera entre autre les méthodes doLogin /
     * doLogout
     */
    public ConnectZone(BaseApp app) {
        this(app, null);
    }

    /**
     * Constructeur
     *
     * @param showProfilWindow une action ouvrant la fenêtre de profil. Si null,
     * le bouton profil n'est pas visible
     * @param app l'application. On utilisera entre autre les méthodes doLogin /
     * logout
     */
    public ConnectZone(BaseApp app, Runnable showProfilWindow) {
        this.app = app;
        if (showProfilWindow != null) {
            profilButton = new Button("Anonymous");
            profilButton.setLineWrap(false);
            profilButton.addActionListener(a -> showProfilWindow.run());
            profilButton.setStyleName("ConnectZone");
            add(profilButton);
        }

        loginButton = new Button(app.getBaseString("login"));
        loginButton.setLineWrap(false);
        loginButton.addActionListener(a -> app.doLogin());
        loginButton.setStyleName("ConnectZone");
        add(loginButton);

        logoutButton = new Button(app.getStyles().getIcon("logout"));
        logoutButton.addActionListener(a -> app.logout());
        logoutButton.setStyleName("ConnectZone");
        add(logoutButton);
    }

    public void update() {
        final boolean someone = app.getUser() != null;
        profilButton.setVisible(someone);
        loginButton.setVisible(!someone);
        logoutButton.setVisible(someone);
    }

}
