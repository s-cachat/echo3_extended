package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.components.DirectHtml;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.TextFieldEx;
import java.util.logging.Logger;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * Pose une question simple
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003-2020 SST Informatique
 */
public class QuestionTexte extends WindowPane {

    /**
     * Logger local
     */
    protected static final transient Logger logger = Logger.getLogger("com.cachat.prj.wir.gui");
    /**
     * le champs de saisie
     */
    private TextField tf;

    /**
     * pose une question
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param question la question
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param resp la reponse
     * @return la question
     */
    public static QuestionTexte ask(BaseApp app, WindowPane parent, String titre, String question, int w, int h, Reponse resp) {
        return ask(app, parent, titre, question, new Extent(w), new Extent(h), resp);
    }

    /**
     * pose une question
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param question la question
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param resp la reponse
     * @return la question
     */
    public static QuestionTexte ask(BaseApp app, WindowPane parent, String titre, String question, Extent w, Extent h, Reponse resp) {
        return new QuestionTexte(app, parent, titre, question, w, h, resp);
    }

    /**
     * pose une question
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param question la question
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param resp la reponse
     * @return la question
     * @param isHtml si true, la question est supposée être préformatée en html
     * valide
     */
    public static QuestionTexte ask(BaseApp app, WindowPane parent, String titre, String question, Extent w, Extent h, Reponse resp, boolean isHtml) {
        return new QuestionTexte(app, parent, titre, question, w, h, resp, isHtml);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param question la question
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param resp la reponse
     */
    public QuestionTexte(BaseApp app, WindowPane parent, String titre, String question, Extent w, Extent h, Reponse resp) {
        this(app, parent, titre, question, w, h, resp, false);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param question la question
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param resp la reponse
     * @param isHtml si true, la question est supposée être préformatée en html
     * valide
     */
    public QuestionTexte(BaseApp app, WindowPane parent, String titre, String question, Extent w, Extent h, Reponse resp, boolean isHtml) {
        this(app, parent, titre, question, w, h, "", resp, isHtml);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param question la question
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param value la valeur avant édition
     * @param resp la reponse
     */
    public QuestionTexte(BaseApp app, WindowPane parent, String titre, String question, Extent w, Extent h, String value, Reponse resp) {
        this(app, parent, titre, question, w, h, value, resp, false);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param question la question
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param value la valeur avant édition
     * @param resp la reponse
     * @param isHtml si true, la question est supposée être préformatée en html
     * valide
     */
    public QuestionTexte(BaseApp app, WindowPane parent, String titre, String question, Extent w, Extent h, String value, Reponse resp, boolean isHtml) {
        super(titre, w, h);
        setModal(true);
        setStyleName("DefaultW");
        Column c = new Column();
        add(c);
        if (isHtml) {
            DirectHtml html = new DirectHtml(question);
            c.add(html);
        } else {
            LabelEx label = new LabelEx(question);
            label.setIntepretNewlines(true);
            label.setLineWrap(true);
            c.add(label);
        }
        c.add(tf = new TextFieldEx(value));
        Row r = new Row();
        c.add(r);
        ok = new Button(app.getString("ok"), app.getStyles().getIcon("accept"));
        ok.addActionListener(e -> reponse(tf.getText()));
        ok.setStyleName("Button");
        r.add(ok);
        cancel = new Button(app.getString("cancel"), app.getStyles().getIcon("cross"));
        cancel.addActionListener(e -> reponse(null));
        cancel.setStyleName("Button");
        r.add(cancel);
        this.resp = resp;
        this.parent = parent;
        app.addWindow(this, parent);
    }

    /**
     * action !
     * @param s la réponse
     */
    public void reponse(String s) {
        userClose();
        resp.reponse(s);
    }
    /**
     * bouton ok
     */
    Button ok;
    /**
     * bouton annuler
     */
    Button cancel;
    /**
     * objet parent
     */
    WindowPane parent;
    /**
     * objet reponse
     */
    Reponse resp;

    /**
     * interface pour l'objet recevant la reponse
     */
    public static interface Reponse {

        public void reponse(String res);
    }
}
