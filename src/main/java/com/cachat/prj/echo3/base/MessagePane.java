package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.components.DirectHtml;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.Strut;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * Fenetre d'affichage de message generique
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003 SST Informatique
 */
public class MessagePane extends BasicWindow implements ActionListener {

    /**
     * les icones possibles
     */
    public static enum Icon {
        ERROR,
        ALERTE,
        INFO
    }

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
    public MessagePane(BaseApp app, String prefixe, String domaine, String msg, Icon icon, Extent w, Extent h, boolean isHtml) {
        super(app, prefixe, domaine, w, h);
        if (isHtml) {
            DirectHtml html = new DirectHtml(msg);
            init(html, icon);
        } else {
            LabelEx label = new LabelEx(msg);
            label.setIntepretNewlines(true);
            label.setLineWrap(true);
            init(label, icon);
        }
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param msg le message
     * @param prefixe le prefixe pour la localisation
     * @param domaine le domaine
     * @param icon l'icone
     * @param h la hauteur
     * @param w la largeur
     * @param isHtml si true, le contenu est du html, sinon du texte
     */
    public MessagePane(BaseApp app, String prefixe, String domaine, String msg, Icon icon, int w, int h, boolean isHtml) {
        this(app, prefixe, domaine, msg, icon, new Extent(w), new Extent(h), isHtml);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param msg le message
     * @param prefixe le prefixe pour la localisation
     * @param domaine le domaine
     * @param icon l'icone
     * @param h la hauteur
     * @param w la largeur
     */
    public MessagePane(BaseApp app, String prefixe, String domaine, String msg, Icon icon, Extent w, Extent h) {
        this(app, prefixe, domaine, msg, icon, w, h, false);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param msg le message
     * @param prefixe le préfixe
     * @param domaine le domaine
     * @param icon l'icone
     * @param h la hauteur
     * @param w la largeur
     */
    public MessagePane(BaseApp app, String prefixe, String domaine, String msg, Icon icon, int w, int h) {
        this(app, prefixe, domaine, msg, icon, w, h, false);
    }

    private void init(Component label, Icon icon) {
        setStyleName("DefaultW");
        setModal(true);
        Column col = new Column();
        add(col);
        Row row = new Row();
        col.add(row);
        if (icon != null) {
            row.add(new LabelEx(app.getStyles().getIcon(icon.toString())));
            row.add(new Strut(10, 10));
        }
        row.add(label);
        Button ok = new ButtonEx(getBaseString("fermer"));
        ok.setStyleName("Button");
        col.add(ok);
        ok.addActionListener(this);
    }

    /**
     * gere une action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        userClose();
    }
}
