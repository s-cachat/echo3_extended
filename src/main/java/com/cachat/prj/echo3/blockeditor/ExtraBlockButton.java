package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.Strut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Row;

/**
 * un bloc de bouton complémentaires
 */
public class ExtraBlockButton implements BlockBase<Column>, BlockInterface {

    /**
     * les boutons
     */
    private final List<Button> buttons = new ArrayList<>();
    /**
     * la ligne de boutons
     */
    protected final Row butRow;
    /**
     * la colonne de boutons
     */
    protected final Column butCol;

    /**
     * ajoute une zone de bouton supplémentaires
     *
     * @param buttons les boutons
     */
    public ExtraBlockButton(Button... buttons) {
        this.buttons.addAll(Arrays.asList(buttons));
        butCol = new Column();
        butRow = new Row();
        butCol.add(new Strut(5, 5));
        butCol.add(butRow);
        butRow.setStyleName("Buttons");
        this.buttons.forEach((a) -> addButton(a));
    }

    @Override
    public Column getComponent() {
        return butCol;
    }

    @Override
    public void copyObjectToUi() {
        //nop
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
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        //nop
        return false;
    }

    @Override
    public void setParent(BlockContainer parent) {
        //nop
    }

    @Override
    public void setVisible(boolean visible) {
        butRow.setVisible(visible);
    }

    @Override
    public void setEnabled(boolean enabled) {
        buttons.forEach((a) -> a.setEnabled(enabled));
    }

    @Override
    public Object clone() {
        return new ExtraBlockButton((Button[]) buttons.toArray());
    }

    /**
     * add a label
     *
     * @param s the label
     */
    public void addLabel(String s) {
        if (butRow.getComponentCount() > 0) {
            butRow.add(new Strut(5, 5));
        }
        butRow.add(new LabelEx(s));
    }

    /**
     * add another button
     *
     * @param b the button
     */
    public void addButton(Button b) {
        if (butRow.getComponentCount() > 0) {
            butRow.add(new Strut(5, 5));
        }
        butRow.add(b);
    }

}
