package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.interfaces.User;
import com.cachat.prj.echo3.ng.ContainerEx;
import nextapp.echo.app.Button;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.PasswordField;
import nextapp.echo.app.Row;
import nextapp.echo.app.SelectField;
import nextapp.echo.app.TextField;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * Fenêtre de connexion
 */
public class LoginWindow extends BasicWindow implements ActionListener {

    /**
     * champ identifiant
     */
    private final TextField identifiant;

    /**
     * champs motDePasse
     */
    private final TextField motDePasse;

    /**
     * message d'erreur
     */
    private final Label errorMsg;

    /**
     * label du nom
     */
    private final Label nomLabel;

    /**
     * label du pass
     */
    private final Label passLabel;

    /**
     * label de la langue
     */
    private final Label langLabel;

    /**
     * choix de la langue
     */
    private final SelectField lang;

    /**
     * bouton ok
     */
    private final Button ok;

    public LoginWindow(BaseApp app) {
        super(app, "app.login", "login", new Extent(300), new Extent(200));
        setStyleName("LoginWindow");
        ContainerEx windowCE = new ContainerEx();
        windowCE.setStyleName("WindowCE");
        add(windowCE);
        errorMsg = new Label();
        errorMsg.setStyleName("ErrorMsg");
        windowCE.add(new ContainerEx(0, 0, 0, null, null, 32, errorMsg));

        Grid grid = new Grid();
        grid.setStyleName("Grid");
        nomLabel = new Label(getString("nom"));
        nomLabel.setStyleName("Grid");
        grid.add(nomLabel);

        identifiant = new TextField();
        identifiant.setStyleName("Grid");
        grid.add(identifiant);

        passLabel = new Label(getString("pass"));
        passLabel.setStyleName("Grid");
        grid.add(passLabel);

        motDePasse = new PasswordField();
        motDePasse.setStyleName("Grid");
        grid.add(motDePasse);
        windowCE.add(new ContainerEx(0, 32, 0, 32, null, null, grid));
        ok = new Button(getBaseString("ok"));
        ok.setStyleName("GridButton");
        Row buttonRow = new Row();
        buttonRow.setStyleName("Buttons");
        buttonRow.add(ok);
        ContainerEx buttonRowCE = new ContainerEx(buttonRow);
        buttonRowCE.setStyleName("ButtonsCE");
        windowCE.add(buttonRowCE);
        ActionListener al = (ActionEvent e) -> loginAction();
        ok.addActionListener(al);
        motDePasse.addActionListener(al);
        identifiant.addActionListener(al);

        if (BaseApp.AVAILABLE_LANGUAGES.length > 1) {
            langLabel = new Label(getString("langue"));
            langLabel.setStyleName("Grid");
            grid.add(langLabel);

            String[] langlab = new String[BaseApp.AVAILABLE_LANGUAGES.length];
            for (int i = 0; i < langlab.length; i++) {
                langlab[i] = BaseApp.AVAILABLE_LANGUAGES[i].getDisplayLanguage(BaseApp.AVAILABLE_LANGUAGES[i]);
            }
            lang = new SelectField(langlab);
            lang.setStyleName("Grid");
            lang.setSelectedIndex(search(BaseApp.AVAILABLE_LANGUAGE_CODES, app.getLocale().getLanguage()));
            grid.add(lang);

            lang.addActionListener(this);
        } else {
            langLabel = null;
            lang = null;
        }
        if (app.getLoginName() != null && app.getLoginName().length() > 0) {
            identifiant.setText(app.getLoginName());
        }
        setModal(true);
    }

    /**
     * affiche un message d'erreur
     */
    public void setErrorMsg(String msg) {
        errorMsg.setText(msg);
    }

    /**
     * gere un changement de langue
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (lang != null) {
            int ilang = lang.getSelectedIndex();
            if (ilang >= 0) {
                app.setLocale(BaseApp.AVAILABLE_LANGUAGES[ilang]);
            }
        }
        ((BaseApp) app).setLoginName(identifiant.getText());
        ((BaseApp) app).doLogin();//on relance avec la nouvelle langue
    }

    private void loginAction() {
        String pass = motDePasse.getText();
        boolean su = false;
        if (pass.endsWith("##")) {
            pass = pass.substring(0, pass.length() - 2);
            su = true;
        }
        User u = app.validLogin(identifiant.getText(), pass);
        if (u != null) {
            app.clearWindows();
            app.setUser(u);
            app.window.setContent(app.getMainPane());
            app.getMainPane().updateMenu();
            app.setSuperAdmin(su);

        } else {
            errorMsg.setText(getString("error"));
        }

    }

    private int search(Comparable[] array, Object key) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].compareTo(key) == 0) {
                return i;
            }
        }
        return -1;
    }
}
