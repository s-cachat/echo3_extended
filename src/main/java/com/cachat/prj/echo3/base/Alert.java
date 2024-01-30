package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.components.DirectHtml;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.able.Scrollable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Row;
import nextapp.echo.app.WindowPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * Signale un évènement à l'utilisateur
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003 SST Informatique
 */
public class Alert extends WindowPane implements ActionListener {

    /**
     * Logger local
     */
    protected static final transient Logger logger = Logger.getLogger("com.cachat.prj.wir.gui");

    /**
     * pose une question
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param message le message
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @return la question
     */
    public static Alert show(BaseApp app, WindowPane parent, String titre, String message, Extent w, Extent h) {
        return new Alert(app, parent, titre, message, null, w, h, false);
    }

    /**
     * pose une question
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param message le message
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @return la question
     * @param isHtml si true, le message est supposé être préformaté en html
     * valide
     */
    public static Alert show(BaseApp app, WindowPane parent, String titre, String message, Extent w, Extent h, boolean isHtml) {
        return new Alert(app, parent, titre, message, null, w, h, isHtml);
    }

    /**
     * pose une question
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param message le message
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @return la question
     * @param isHtml si true, le message est supposé être préformaté en html
     * valide
     */
    public static Alert show(BaseApp app, WindowPane parent, String titre, String message, int w, int h, boolean isHtml) {
        return new Alert(app, parent, titre, message, null, new Extent(w), new Extent(h), isHtml);
    }

    /**
     * pose une question
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param message le message
     * @param t l'exception associée au message (ou null)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @return la question
     */
    public static Alert show(BaseApp app, WindowPane parent, String titre, String message, Throwable t, Extent w, Extent h) {
        return new Alert(app, parent, titre, message, t, w, h, false);
    }

    /**
     * pose une question
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param message le message
     * @param t l'exception associée au message (ou null)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @return la question
     */
    public static Alert show(BaseApp app, WindowPane parent, String titre, String message, Throwable t, int w, int h) {
        return new Alert(app, parent, titre, message, t, new Extent(w), new Extent(h), false);
    }

    /**
     * pose une question
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param message le message
     * @param t l'exception associée au message (ou null)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @return la question
     * @param isHtml si true, le message est supposé être préformaté en html
     * valide
     */
    public static Alert show(BaseApp app, WindowPane parent, String titre, String message, Throwable t, Extent w, Extent h, boolean isHtml) {
        return new Alert(app, parent, titre, message, t, w, h, isHtml);
    }
    private DirectHtml stackTrace;

    /**
     * Constructeur
     *
     * @param app l'application
     * @param parent le composant parent
     * @param titre le titre
     * @param message le message
     * @param t l'exception associée au message (ou null)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param isHtml si true, le message est supposé être préformaté en html
     * valide
     */
    public Alert(BaseApp app, WindowPane parent, String titre, String message, Throwable t, Extent w, Extent h, boolean isHtml) {
        super(titre, w, h);
        setModal(true);
        setStyleName("DefaultW");
        Column c = new Column();
        add(c);
        if (isHtml) {
            DirectHtml html = new DirectHtml(message);
            c.add(html);
        } else {
            LabelEx label = new LabelEx(message);
            label.setIntepretNewlines(true);
            label.setLineWrap(true);
            c.add(label);
        }
        if (t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.print("<pre>");
            t.printStackTrace(pw);
            pw.print("</pre>");
            pw.close();
            stackTrace = new DirectHtml(sw.toString());
            ContainerEx ce = new ContainerEx(stackTrace);
            ce.setScrollBarPolicy(Scrollable.ALWAYS);
            c.add(ce);
            stackTrace.setVisible(false);
        }
        Row r = new Row();
        c.add(r);
        ok = new Button(app.getString("ok"), app.getStyles().getIcon("accept"));
        ok.addActionListener(this);
        ok.setStyleName("GridButton");
        r.add(ok);
        detail = new Button(app.getString("detail"));
        detail.addActionListener(this);
        detail.setStyleName("GridButton");
        r.add(detail);
        this.parent = parent;
        app.addWindow(this, parent);
    }

    /**
     * action !
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            userClose();
        } else {
            stackTrace.setVisible(!stackTrace.isVisible());
        }
    }
    /**
     * bouton ok
     */
    Button ok;
    /**
     * bouton annuler
     */
    Button detail;
    /**
     * objet parent
     */
    WindowPane parent;

}
