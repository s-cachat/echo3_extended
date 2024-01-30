package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.components.DirectHtml;
import com.cachat.prj.echo3.ng.LabelEx;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Component;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.LayoutData;

/**
 * un label sur la largeur du formulaire
 *
 * @author scachat
 */
public class BlockLabel implements BlockInterface, BlockBase<Component> {

    /**
     * the container, for a non html content
     */
    protected LabelEx label;
    /**
     * the container, for an html content
     */
    protected DirectHtml dhtml;

    /**
     * Constructeur
     *
     * @param msg le message
     * @param html : si le message est formaté en html
     */
    public BlockLabel(String msg, boolean html) {
        if (html) {
            dhtml = new DirectHtml(msg);
        } else {
            label = new LabelEx(msg);
        }
    }
    /**
     * Constructeur, non html content
     *
     * @param msg le message
     */
    public BlockLabel(String msg) {
        this(msg, false);
    }
    /**
     * Constructeur, image
     *
     * @param img l'image
     */
    public BlockLabel(ImageReference img) {
        label = new LabelEx(img);
    }

    /**
     * fixe le layout du composant
     *
     * @param layout le layout
     */
    public void setLayoutData(LayoutData layout) {
        if (dhtml != null) {
            dhtml.setLayoutData(layout);
        } else {
            label.setLayoutData(layout);
        }
    }


    @Override
    public void copyObjectToUi() {
        //nop
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        //nop
        return false;
    }

    /**
     * ajouter un message d'erreur sur un champ
     *
     * @param pp le chemin de la propriété
     * @param msg le message
     * @return true si le message a pu être ajouté, false sinon
     */
    @Override
    public boolean appendError(String pp, String msg) {
        return false;
    }

    @Override
    public Component getComponent() {
        return label == null ? dhtml : label;
    }

    @Override
    public void setParent(BlockContainer parent) {
        //nop
    }

    @Override
    public void setVisible(boolean visible) {
        getComponent().setVisible(visible);
    }

    @Override
    public void setEnabled(boolean enabled) {
        getComponent().setEnabled(enabled);
    }

    @Override
    public Object clone() {
        if (label != null) {
            return new BlockLabel(label.getText(), false);
        } else {
            return new BlockLabel(dhtml.getText(), true);
        }
    }

    public void setText(String msg) {
        if (label != null) {
            label.setText(msg);
        } else {
            dhtml.setText(msg);
        }
    }
}
