package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.ToggleButtonEx;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.validation.Validator;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;
import nextapp.echo.app.layout.GridLayoutData;

/**
 * champs multi checkbox (champs de choix parmi n objets ou parmi une enum)
 *
 * @param <TypeObjet> le type d'objet a sélectionner
 * @author scachat
 */
public class BlockMultiToggle<TypeObjet> extends BlockField<Grid> {

    /**
     * le style pour un bouton non pressé
     */
    private String unSelectedStyleName;
    /**
     * le style pour un bouton pressé
     */
    private String selectedStyleName;
    private ButtonEx all;
    private ButtonEx none;
    private Row buttons;
    /**
     * la map checkbox vers objet
     */
    protected Map<ToggleButtonEx, TypeObjet> items = new HashMap<>();

    /**
     * la liste a renseigner
     */
    protected Collection<TypeObjet> data;

    public BlockMultiToggle(BlockField bf) {
        super(bf);
        BlockMultiToggle bmc = (BlockMultiToggle) bf;
        this.unSelectedStyleName = ((BlockMultiToggle< TypeObjet>) bf).unSelectedStyleName;
        this.selectedStyleName = ((BlockMultiToggle< TypeObjet>) bf).selectedStyleName;
        ((BlockMultiToggle< TypeObjet>) bf).items.entrySet().forEach((x) -> items.put(new ToggleButtonEx(x.getKey().getText(), unSelectedStyleName, selectedStyleName), x.getValue()));
        createGrid(bmc.localisedItem, ((Grid) bmc.editor).getSize());
        items.keySet().forEach(a -> editor.add(a));
    }

    /**
     * Constructeur pour utiliser une liste spécifique de valeurs. Doit appeler
     * createGrid, puis remplir la map items et ajouter les checkboxex a editor
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     */
    protected BlockMultiToggle(LocalisedItem li, String property, String unSelectedStyleName, String selectedStyleName) {
        super(li, property);
        this.unSelectedStyleName = unSelectedStyleName;
        this.selectedStyleName = selectedStyleName;
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
    public BlockMultiToggle(LocalisedItem li, String property, Set<TypeObjet> items, String propLib, String unSelectedStyleName, String selectedStyleName, int cols) {
        super(li, property);
        this.unSelectedStyleName = unSelectedStyleName;
        this.selectedStyleName = selectedStyleName;
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
    public BlockMultiToggle(LocalisedItem li, String property, Class<TypeObjet> items, String unSelectedStyleName, String selectedStyleName, int cols) {
        super(li, property);
        this.unSelectedStyleName = unSelectedStyleName;
        this.selectedStyleName = selectedStyleName;
        createGrid(li, cols);
        setList(items);
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
    public BlockMultiToggle(LocalisedItem li, String property, List<TypeObjet> items, String propLib, String unSelectedStyleName, String selectedStyleName, int cols) {
        super(li, property);
        this.unSelectedStyleName = unSelectedStyleName;
        this.selectedStyleName = selectedStyleName;
        createGrid(li, cols);
        setList(items, propLib);
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param items la map d'objet/libelle a choisir
     * @param cols le nombre de colonnes
     */
    public BlockMultiToggle(LocalisedItem li, String property, Map<TypeObjet, String> items, String unSelectedStyleName, String selectedStyleName, int cols) {
        super(li, property);
        this.unSelectedStyleName = unSelectedStyleName;
        this.selectedStyleName = selectedStyleName;
        createGrid(li, cols);
        setList(items);
    }

    /**
     * change la liste d'objet, déselectionne tout. Appeler copyObjectToUi juste
     * après.
     *
     * @param items la liste d'objet a sélectionner
     * @param propLib la propriété a afficher
     */
    public void setList(Map<TypeObjet, String> list) {
        this.items.clear();
        if (editor != null) {
            editor.removeAll();
            list.forEach((o, l) -> {
                ToggleButtonEx cb = new ToggleButtonEx(l, unSelectedStyleName, selectedStyleName);
                this.items.put(cb, o);
                editor.add(cb);
            });
        }
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
        if (editor != null) {
            editor.removeAll();
            items.forEach((o) -> {
                ToggleButtonEx cb = new ToggleButtonEx(BeanTools.get(o, propLib), unSelectedStyleName, selectedStyleName);
                this.items.put(cb, o);
                editor.add(cb);
            });
        }
    }

    /**
     * change la liste d'enums, déselectionne tout. Appeler copyObjectToUi juste
     * après.
     *
     * @param items la classe pour la liste d'enums a sélectionner
     * @param propLib la propriété a afficher
     */
    public void setList(Class<TypeObjet> items) {
        this.items.clear();
        if (editor != null) {
            editor.removeAll();
            LocalisedItem li = getLocalisedItem();
            for (TypeObjet o : items.getEnumConstants()) {
                ToggleButtonEx cb = new ToggleButtonEx(li.getString(String.format("%s.%s", items.getSimpleName(), o.toString())), unSelectedStyleName, selectedStyleName);
                this.items.put(cb, o);
                editor.add(cb);
            }
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
        data = (Collection<TypeObjet>) BeanTools.getRaw(getParent().getCurrent(), property);
        if (data != null) {
            items.entrySet().forEach((x) -> {
                x.getKey().setSelected(data.contains(x.getValue()));
            });
        }
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
        editor.setInsets(new Insets(4));
        buttons = new Row();
        GridLayoutData gld = new GridLayoutData();
        buttons.setLayoutData(gld);
        editor.add(buttons);
        all = new ButtonEx(li.getBaseString("all"));
        all.setStyleName("GridButton");
        buttons.add(all);
        all.addActionListener((e) -> items.entrySet().forEach((x) -> x.getKey().setSelected(true)));
        none = new ButtonEx(li.getBaseString("none"));
        none.setStyleName("GridButton");
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
