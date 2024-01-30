package com.cachat.prj.echo3.list;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.BasicWindow;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.Strut;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Extent;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.Row;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * bouton d'action, avec popup de confirmation
 *
 * @author scachat
 */
public class ActionConfirmButton extends ButtonEx {

    private final BaseApp app;
    private final String popupTitle;
    private final String popupContent;
    private final WindowPane parentWindow;
    private final Runnable action;

    /**
     * Constructeur
     *
     * @param app l'application
     * @param buttonText le texte du bouton
     * @param popupTitle le titre de la popup
     * @param popupContent le contenu de la popup
     * @param parentWindow la fenêtre parente
     * @param action l'action a effectuer
     */
    public ActionConfirmButton(BaseApp app, String buttonText, String popupTitle, String popupContent, WindowPane parentWindow, Runnable action) {
        this(app, buttonText, null, "Button", popupTitle, popupContent, parentWindow, action);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param buttonText le texte du bouton
     * @param buttonImage l'image du bouton
     * @param buttonStyleName le style du bouton
     * @param popupTitle le titre de la popup
     * @param popupContent le contenu de la popup
     * @param parentWindow la fenêtre parente
     * @param action l'action a effectuer
     */
    public ActionConfirmButton(BaseApp app, String buttonText, ImageReference buttonImage, String buttonStyleName, String popupTitle, String popupContent, WindowPane parentWindow, Runnable action) {
        super(buttonText, buttonImage);
        this.app = app;
        this.popupTitle = popupTitle;
        this.popupContent = popupContent;
        this.parentWindow = parentWindow;
        this.action = action;
        if (buttonStyleName == null) {
            setStyleName("Button");
        } else {
            setStyleName(buttonStyleName);
        }
        addActionListener(new ConfirmActionListener());
    }

    private class ConfirmActionListener implements ActionListener {

        public ConfirmActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            app.addWindow(new ConfirmPane(app, popupTitle, popupContent),  parentWindow);
        }
    }

    private class ConfirmPane extends BasicWindow {

        /**
         * Constructeur
         *
         * @param app l'application
         * @param msg le message
         * @param domaine le domaine
         * @param prefixe le prefixe pour la localisation
         * @param icon l'icone
         * @param h la hauteur
         * @param w la largeur
         * @param isHtml si true, le contenu est du html, sinon du texte
         */
        public ConfirmPane(BaseApp app, String title, String msg) {
            super(app, "", "", 640, 226);
            setTitle(title);
            setStyleName("DefaultW");
            setModal(true);
            
            Column col = new Column();
            add(col);
            Row row = new Row();
            col.add(row);
            final ImageReference icon = app.getStyles().getIcon("ALERTE");
            row.add(new LabelEx(icon));
            row.add(new Strut(10, 10));

            LabelEx label = new LabelEx(msg);
            label.setIntepretNewlines(true);
            label.setLineWrap(true);
            row.add(label);
            
            row = new Row();
            col.add(row);
            Button ok = new ButtonEx(getBaseString("ok"));
            ok.setStyleName("Button");
            row.add(ok);
            ok.addActionListener(e -> {
                userClose();
                action.run();
            });
            Button cancel = new ButtonEx(getBaseString("cancel"));
            cancel.setStyleName("Button");
            row.add(cancel);
            row.setAlignment(Alignment.ALIGN_RIGHT);
            cancel.addActionListener(e -> userClose());
        }

    }

}
