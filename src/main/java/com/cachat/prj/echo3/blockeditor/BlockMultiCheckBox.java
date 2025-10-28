package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.CheckBoxEx;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.validation.Validator;
import nextapp.echo.app.Color;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Row;
import nextapp.echo.app.layout.GridLayoutData;

/**
 * champs multi checkbox (champs de choix parmi n objets ou parmi une enum)
 *
 * @param <TypeObjet> le type d'objet a sélectionner
 * @author scachat
 */
public class BlockMultiCheckBox<TypeObjet> extends BlockField<Grid> {

    private ButtonEx all;
    private ButtonEx none;
    private Row buttons;

    public BlockMultiCheckBox(BlockField bf) {
        super(bf);
        ((BlockMultiCheckBox<TypeObjet>) bf).items.entrySet().forEach((x) -> items.put(new CheckBoxEx(x.getKey().getText()), x.getValue()));
    }
    /**
     * la map checkbox vers objet
     */
    protected Map<CheckBoxEx, TypeObjet> items = new HashMap<>();
    /**
     * la liste a renseigner
     */
    protected Collection<TypeObjet> data;

    /**
     * Constructeur pour utiliser une liste spécifique de valeurs. Doit appeler
     * createGrid, puis remplir la map items et ajouter les checkboxex a editor
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     */
    protected BlockMultiCheckBox(LocalisedItem li, String property) {
        super(li, property);
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param items la liste d'objet a choisir
     * @param propLib la propriété des objets a choisir a utiliser comme label
     * @param cols le nombre de colonnes
     */
    public BlockMultiCheckBox(LocalisedItem li, String property, Set<TypeObjet> items, String propLib, int cols) {
        super(li, property);
        createGrid(li, cols);
        setList(items, propLib);
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param items la liste d'objet a choisir
     * @param propLib la propriété des objets a choisir a utiliser comme label
     * @param cols le nombre de colonnes
     */
    public BlockMultiCheckBox(LocalisedItem li, String property, List<TypeObjet> items, String propLib, int cols) {
        super(li, property);
        createGrid(li, cols);
        setList(items, propLib);
    }

    /**
     * change la liste d'objet, déselectionne tout. Appeler copyObjectToUi juste
     * après.
     *
     * @param items la liste d'objet a sélectionner
     * @param propLib la propriété a afficher
     */
    public void setList(Collection<TypeObjet> items, String propLib) {
        this.items.clear();
        editor.removeAll();
        items.forEach((o) -> {
            CheckBoxEx cb = new CheckBoxEx(BeanTools.get(o, propLib));
            this.items.put(cb, o);
            editor.add(cb);
        });
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
        data = (Collection<TypeObjet>) BeanTools.getRaw(getParent().getCurrent(), property);
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
            data = (Collection<TypeObjet>) BeanTools.getRaw(getParent().getCurrent(), property);
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
