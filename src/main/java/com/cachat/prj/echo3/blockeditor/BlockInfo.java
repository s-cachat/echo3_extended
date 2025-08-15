package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.components.DirectHtml;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import static nextapp.echo.app.Position.FIXED;

/**
 * un bloc d'information
 *
 * @author scachat
 */
public class BlockInfo implements BlockBase<Component>, BlockInterface {

    /**
     * Contenu html
     */
    protected DirectHtml html;
    /**
     * Contenu texte
     */
    protected LabelEx text;
    /**
     * le contenu
     */
    private String content;
    /**
     * si true, le contenu est du html, sinon du texte
     */
    private boolean isHtml;

    /**
     * le conteneur de boutons
     */
    protected final ContainerEx butContainer;
    /**
     * la grille
     */
    private final Grid grid;

    /**
     * ajoute une zone de bouton
     *
     * @param foreground la couleur de texte
     * @param background la couleur de fond
     * @param content le contenu
     * @param isHtml si true, le contenu est du html, sinon du texte
     */
    public BlockInfo(String content, boolean isHtml, Color foreground, Color background) {
        this(content, isHtml);
        setBackground(background);
        setForeground(foreground);
    }

    /**
     * ajoute une zone de bouton
     *
     * @param content le contenu
     * @param isHtml si true, le contenu est du html, sinon du texte
     */
    public BlockInfo(String content, boolean isHtml) {
        this.content = content;
        this.isHtml = isHtml;
        butContainer = new ContainerEx(0, null, 0, 0, null, null);
        butContainer.setPosition(FIXED);
        butContainer.setStyleName("GridButton");
        grid = new Grid(1);
        grid.setStyleName("GridButton");
        butContainer.add(grid);
        if (isHtml) {
            grid.add(html = new DirectHtml(content));
        } else {
            grid.add(text = new LabelEx(content));
        }
    }

    /**
     * change le contenu
     */
    public void setContent(String content) {
        this.content = content;
        if (isHtml) {
            html.setText(content);
        } else {
            text.setText(content);
        }
    }

    @Override
    public Component getComponent() {
        return butContainer;

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
    public void setParent(BlockContainer parent) {
        //nop
    }

    @Override
    public void setEnabled(boolean enabled) {
        //nop
    }

    @Override
    public void setVisible(boolean visible) {
        butContainer.setVisible(visible);
    }

    @Override
    public Object clone() {
        return new BlockInfo(content, html != null);
    }

    /**
     * change la couleur de texte
     *
     * @param color la couleur
     */
    public void setForeground(Color color) {
        if (html != null) {
            html.setForeground(color);
        }
        if (text != null) {
            text.setForeground(color);
        }
    }

    /**
     * change la couleur de fond
     *
     * @param color la couleur
     */
    public void setBackground(Color color) {
        grid.setBackground(color);
    }

}
