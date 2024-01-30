package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.ListBoxEx;
import com.cachat.util.BeanTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.Validator;
import nextapp.echo.app.Column;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.GridLayoutData;
import nextapp.echo.app.list.AbstractListModel;
import nextapp.echo.app.list.ListSelectionModel;

/**
 * champs multi checkbox (champs de choix parmi n objets ou parmi une enum)
 *
 * @author scachat
 */
public class BlockMultiBiList extends BlockField<Grid> {

    protected final List< Object> allItems = new ArrayList<>();
    protected final List< Object> selectedItems = new ArrayList<>();
    protected final List< Object> unselectedItems = new ArrayList<>();
    protected final Map<Object, String> labels = new HashMap<>();
    protected ListBoxEx selectList;
    protected ListBoxEx unselectList;
    protected LabelEx selectCount;
    protected LabelEx unselectCount;
    protected ButtonEx all;
    protected ButtonEx none;
    protected ButtonEx fill;
    protected ButtonEx unselectButton;
    protected ButtonEx selectButton;
    /**
     * si supérieur à 0, le nombre maximal d'items a choisir
     */
    protected Integer maxSelectedCount = 0;
    /**
     * le nom de la propriété des items a utiliser comme libellé
     */
    protected String propLib;
    /**
     * les listeners pour une action
     */
    private final List<ActionListener> listeners = new ArrayList<>();

    public BlockMultiBiList(BlockField bf) {
        super(bf);
        this.propLib = ((BlockMultiBiList) bf).propLib;
        createGrid(bf.getLocalisedItem());
        allItems.addAll(((BlockMultiBiList) bf).allItems);
        labels.putAll(((BlockMultiBiList) bf).labels);
        update();
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param items la liste d'objet a choisir
     * @param propLib la propriété des objets a choisir a utiliser comme label
     */
    public BlockMultiBiList(LocalisedItem li, String property, List<? extends Object> items, String propLib) {
        this(li, property, items, propLib, 0);
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param items la liste d'objet a choisir
     * @param propLib la propriété des objets a choisir a utiliser comme label
     * @param maxSelectedCount le nombre maximum d'items pouvant être dans la
     * colonne sélectionnés
     */
    public BlockMultiBiList(LocalisedItem li, String property, List<? extends Object> items, String propLib, int maxSelectedCount) {
        super(li, property);
        this.maxSelectedCount = maxSelectedCount;
        this.propLib = propLib;
        createGrid(li);
        setItems(items);
    }

    /**
     * change la liste des objets a choisir
     *
     * @param items la nouvelle liste
     */
    public void setItems(List<? extends Object> items) {
        allItems.clear();
        labels.clear();
        allItems.addAll(items);
        allItems.stream().forEach((a) -> labels.put(a, BeanTools.get(a, propLib)));
        unselectedItems.clear();
        unselectedItems.addAll(allItems);
        unselectedItems.removeAll(selectedItems);
        selectedItems.clear();
        selectedItems.addAll(allItems);
        selectedItems.removeAll(unselectedItems);
        update();
    }

    /**
     * Constructeur //TODO refaire correctement
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'enum
     * @param items l'enum
     * @param cols le nombre de colonnes
     */
    public BlockMultiBiList(LocalisedItem li, String property, Class items, int cols) {
        super(li, property);
        allItems.addAll(Arrays.asList(items.getEnumConstants()));
        allItems.stream().forEach((a) -> labels.put(a, li.getString(a.toString())));
        createGrid(li);
    }

    public void checkAll() {
        unselectedItems.clear();
        selectedItems.clear();
        selectedItems.addAll(allItems);
        update();
    }

    public void uncheckAll() {
        selectedItems.clear();
        unselectedItems.clear();
        unselectedItems.addAll(allItems);
        update();
    }

    /**
     * Le boutton fill est ajouté uniquement si on utilise le "maxSelectedCount"
     */
    public void fill() {
        if (maxSelectedCount > 0) {
            Integer nbItemsToFill = maxSelectedCount - selectedItems.size();
            for (int i = 0; i < nbItemsToFill; i++) {
                selectedItems.add(unselectedItems.remove(i));
            }
            update();
        }
    }

    @Override
    public void copyObjectToUi() {
        selectedItems.clear();
        unselectedItems.clear();
        unselectedItems.addAll(allItems);
        List<Object> objects = (List< Object>) BeanTools.getRaw(getParent().getCurrent(), property);

        for (Object o : objects) {

            if (unselectedItems.remove(o)) {
                selectedItems.add(o);
            }
        }

        update();
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        try {
            List<Object> data = (List<Object>) BeanTools.getRaw(getParent().getCurrent(), property);
            data.clear();
            data.addAll(selectedItems);
            error.setText((String) null);
            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }

    private void createGrid(LocalisedItem li) {
        editor = new Grid(5);
        editor.setWidth(new Extent(100, Extent.PERCENT));
        editor.setColumnWidth(0, new Extent(47, Extent.PERCENT));
        editor.setColumnWidth(4, new Extent(47, Extent.PERCENT));
        editor.setColumnWidth(1, new Extent(2, Extent.PERCENT));
        editor.setColumnWidth(2, new Extent(2, Extent.PERCENT));
        editor.setColumnWidth(3, new Extent(2, Extent.PERCENT));

        editor.add(unselectCount = new LabelEx());

        none = new ButtonEx(li.getBaseString("none"));
        none.setStyleName("GridButton");
        editor.add(none);
        none.addActionListener((e) -> uncheckAll());

        // Si maxSelectedCount n'est pas défini on n'affiche pas le boutton fill, donc le design est différent
        if (maxSelectedCount > 0) {
            fill = new ButtonEx(li.getBaseString("fill"));
            fill.setStyleName("GridButton");
            editor.add(fill);
            fill.addActionListener((e) -> fill());
        } else {
            // On remplace fill par un label vide
            editor.add(new LabelEx(""));
        }

        all = new ButtonEx(li.getBaseString("all"));
        all.setStyleName("GridButton");
        all.addActionListener((e) -> checkAll());
        editor.add(all);

        editor.add(selectCount = new LabelEx());

        unselectList = new ListBoxEx(new IndirectModel(unselectedItems));
        unselectList.setSelectionMode(ListSelectionModel.MULTIPLE_SELECTION);
        unselectList.setWidth(new Extent(100, Extent.PERCENT));

        editor.add(unselectList);
        Column col = new Column();
        col.setLayoutData(new GridLayoutData(3, 1));
        editor.add(col);

        selectList = new ListBoxEx(new IndirectModel(selectedItems));
        selectList.setSelectionMode(ListSelectionModel.MULTIPLE_SELECTION);
        selectList.setWidth(new Extent(100, Extent.PERCENT));
        editor.add(selectList);

        selectButton = new ButtonEx("=>");
        selectButton.setStyleName("Button");
        selectButton.addActionListener((ActionEvent e) -> {
            for (Integer i : unselectList.getSelectedIndices()) {
                selectedItems.add(unselectedItems.get(i));
            }
            unselectedItems.removeAll(selectedItems);
            Collections.sort(selectedItems, (Object a, Object b) -> labels.get(a).compareTo(labels.get(b)));
            update();
        });
        col.add(selectButton);

        unselectButton = new ButtonEx("<=");
        unselectButton.setStyleName("Button");
        unselectButton.addActionListener((ActionEvent e) -> {
            for (Integer i : selectList.getSelectedIndices()) {
                unselectedItems.add(selectedItems.get(i));
            }
            selectedItems.removeAll(unselectedItems);
            Collections.sort(unselectedItems, (Object a, Object b) -> labels.get(a).compareTo(labels.get(b)));
            update();
        });
        col.add(unselectButton);

    }

    protected void update() {
        ((IndirectModel) unselectList.getModel()).update();
        ((IndirectModel) selectList.getModel()).update();

        if (maxSelectedCount > 0) {
            int missing = maxSelectedCount - selectedItems.size();
            if (missing > 0) {//il manque des items
                if (missing >= unselectedItems.size()) {//on n'a pas assez d'item pour tout remplir
                    all.setEnabled(true);
                    fill.setEnabled(false);
                } else {//on en a trop
                    fill.setEnabled(true);
                    all.setEnabled(false);
                }
            } else {
                fill.setEnabled(false);
            }

            // Si la commande n'est pas pleine et qu'il existe des items à selectionner on active "=>"
            if (maxSelectedCount > selectedItems.size() && unselectedItems.size() > 0) {
                selectButton.setEnabled(true);
            } else {
                selectButton.setEnabled(false);
            }
        } else {
            // Si la commande n'est pas pleine et qu'il existe des items à selectionner on active "=>"
            if (unselectedItems.size() > 0) {
                selectButton.setEnabled(true);
                all.setEnabled(true);
            } else {
                selectButton.setEnabled(false);
                all.setEnabled(false);
            }
        }

        // Si on a au moins un item selectionné on active none et unselect
        if (0 < selectedItems.size()) {
            none.setEnabled(true);
            unselectButton.setEnabled(true);
        } else {
            none.setEnabled(false);
            unselectButton.setEnabled(false);
        }

        selectCount.setText(String.valueOf(selectedItems.size()));
        unselectCount.setText(String.valueOf(unselectedItems.size()));
        ActionEvent e = new ActionEvent(this, "change");
        listeners.forEach((l) -> l.actionPerformed(e));
    }

    private class IndirectModel extends AbstractListModel {

        private final List<Object> data;

        public IndirectModel(List<Object> data) {
            this.data = data;
        }

        @Override
        public Object get(int index) {
            return labels.get(data.get(index));
        }

        @Override
        public int size() {
            return data.size();
        }

        public void update() {
            fireContentsChanged(0, data.size());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        unselectCount.setVisible(enabled);
        unselectList.setVisible(enabled);
        unselectButton.setVisible(enabled);
        selectButton.setVisible(enabled);
        all.setVisible(enabled);
        none.setVisible(enabled);
        if (fill != null) {
            fill.setVisible(enabled);
        }
    }

    /**
     * ajoute un action listener. Notez qu'on joue le role de proxy actif, en
     * régénérant un event plus adapté
     *
     * @param actionListener
     */
    public void addActionListener(ActionListener actionListener) {
        listeners.add(actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        listeners.remove(actionListener);
    }

    public List<Object> getSelectedItems() {
        return Collections.unmodifiableList(selectedItems);
    }

}
