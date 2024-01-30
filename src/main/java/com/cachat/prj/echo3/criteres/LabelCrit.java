package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.components.DirectHtml;
import com.cachat.prj.echo3.ng.LabelEx;
import java.util.List;
import nextapp.echo.app.Color;
import nextapp.echo.app.layout.GridLayoutData;

/**
 * un pseudo critere permettant d'afficher un label dans la liste des critères
 */
public class LabelCrit extends Crit {

    LabelEx l;
    DirectHtml h;

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param text le texte du label (plain text)
     */
    public LabelCrit(CritContainer cont, String text) {
        this(cont, false, text);
    }

    /**
     * constructeur
     *
     * @param isHtml si true, le contenu est en html, sinon en plain text
     * @param cont le conteneur (la liste)
     * @param text le texte du label
     */
    public LabelCrit(CritContainer cont, boolean isHtml, String text) {
        super(cont, "null-");
        GridLayoutData gld = new GridLayoutData();
        gld.setColumnSpan(2);
        if (isHtml) {
            h = new DirectHtml(text);
            critf.add(h);
            h.setLayoutData(gld);
        } else {
            l = new LabelEx(text);
            critf.add(l);
            l.setLayoutData(gld);
        }

        cont.addCrit(this);
        cont.extendCritAreaHeight(cont.CRIT_HEIGHT);
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        return null;
    }

    /**
     * change le texte affich�
     *
     * @param s le nouveau texte
     */
    public void setText(String s) {
        if (l != null) {
            l.setText(s);
        } else {
            h.setText(s);
        }
    }

    /**
     * change le fond
     *
     * @param col la couleur
     */
    public void setBackground(Color col) {
        if (l != null) {
            l.setBackground(col);
        } else {
            h.setBackground(col);
        }
    }

    /**
     * change la couleur
     *
     * @param col la couleur
     */
    public void setForeground(Color col) {
        if (l != null) {
            l.setForeground(col);
        } else {
            h.setForeground(col);
        }
    }

    /**
     * change le style du label
     *
     * @param string le nom du style
     */
    public void setStyleName(String string) {
        if (l != null) {
            l.setStyleName(string);
        } else {
            h.setStyleName(string);
        }
    }

    @Override
    public String getSummary() {
        if (l != null) {
            return l.getText();
        } else {
            return h.getText();
        }

    }
}
