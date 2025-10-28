package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.CheckBoxEx;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.Validator;
import nextapp.echo.app.Color;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Row;
import nextapp.echo.app.layout.GridLayoutData;

/**
 * champs multi checkbox (champs de choix parmi une enum)
 *
 * @author scachat
 */
public class BlockMultiCheckBoxEnum extends BlockField<Grid> {

    private ButtonEx all;
    private ButtonEx none;
    private Row buttons;

    public BlockMultiCheckBoxEnum(BlockField bf) {
        super(bf);
        ((BlockMultiCheckBoxEnum) bf).items.entrySet().forEach((x) -> items.put(new CheckBoxEx(x.getKey().getText()), x.getValue()));
    }
    /**
     * la map checkbox vers objet
     */
    protected Map<CheckBoxEx, Object> items = new HashMap<>();
    /**
     * la liste a renseigner
     */
    protected Collection<Object> data;

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'enum
     * @param items l'enum (TypeObjet doit être le type de l'enum)
     * @param cols le nombre de colonnes
     */
    public BlockMultiCheckBoxEnum(LocalisedItem li, String property, Class items, int cols) {
        super(li, property);
        createGrid(li, cols);
        for (Object o : items.getEnumConstants()) {
            CheckBoxEx cb = new CheckBoxEx(li.getString(o.toString()));
            this.items.put(cb, o);
            editor.add(cb);
        }
    }

    public void checkAll() {
        items.entrySet().forEach((x) -> {
            if (!data.contains(x.getValue())) {
                data.add(x.getValue());
            }
            x.getKey().setSelected(true);
        });
    }

    public void uncheckAll() {
        items.entrySet().forEach((x) -> {
            data.remove(x.getValue());
            x.getKey().setSelected(false);
        });
    }

    @Override
    public void copyObjectToUi() {
        data = (Collection<Object>) BeanTools.getRaw(getParent().getCurrent(), property);
        items.entrySet().forEach((x) -> {
            x.getKey().setSelected(data.contains(x.getValue()));
            if (data.contains(x.getValue())) {
                if (!x.getKey().isVisible()) {
                    x.getKey().setBackground(Color.LIGHTGRAY);
                }
                x.getKey().setVisible(true);
            }
        });
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            data = (Collection<Object>) BeanTools.getRaw(getParent().getCurrent(), property);
            items.entrySet().forEach((x) -> {
                data.remove(x.getValue());
                if (x.getKey().isSelected()) {
                    data.add(x.getValue());
                }
            });
            error.setText((String) null);
            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }

    protected void createGrid(LocalisedItem li, int cols) {
        editor = new Grid(cols);
        buttons = new Row();
        GridLayoutData gld = new GridLayoutData();
        buttons.setLayoutData(gld);
        editor.add(buttons);
        all = new ButtonEx(li.getBaseString("all"));
        all.setStyleName("BlockEditorButtons");
        buttons.add(all);
        all.addActionListener((e) -> items.entrySet().forEach((x) -> x.getKey().setSelected(true)));
        none = new ButtonEx(li.getBaseString("none"));
        none.setStyleName("BlockEditorButtons");
        buttons.add(none);
        none.addActionListener((e) -> items.entrySet().forEach((x) -> x.getKey().setSelected(false)));
    }

    /**
     * affiche ou cache les boutons
     *
     * @param visible si true affiche
     */
    public void setButtonVisible(boolean visible) {
        buttons.setVisible(visible);
    }
}
