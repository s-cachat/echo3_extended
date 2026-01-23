package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import static com.cachat.prj.echo3.editor.BasicEditor.buildLib;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.SelectFieldEx;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.validation.Validator;
import java.util.function.Function;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.ChangeEvent;
import nextapp.echo.app.event.ChangeListener;
import nextapp.echo.app.list.AbstractListModel;

/**
 * champs multi checkbox (champs de choix parmi n objets ou parmi une enum)
 *
 * @param <T> le type d'objet à sélectionner
 * @author scachat
 */
public class BlockSelect<T> extends BlockField<SelectFieldEx> implements ActionListener, ChangeListener {

    /**
     * la liste des labels
     */
    protected List<String> labels;
    /**
     * la liste des objets a sélectionner
     */
    protected List<T> items;
    /**
     * la liste contient la valeur null
     */
    protected boolean gotNull;
    /**
     * le libellé a extraire (nécessaire sauf si propFunc est défini)
     */
    private String propLib;
    /**
     * l'extracteur de libellé (nécessaire sauf si propLib est défini)
     */
    private Function<T, String> propFunc;
    private MyModel model;

    public BlockSelect(BlockField bf) {
        super(bf.localisedItem, bf.property);
        labels = new ArrayList<>(((BlockSelect) bf).labels);
        this.items = new ArrayList<>(((BlockSelect) bf).items);
        this.gotNull = ((BlockSelect) bf).gotNull;
        this.propLib = ((BlockSelect) bf).propLib;//nécessaire si on veut refaire un setList
        this.propFunc = ((BlockSelect) bf).propFunc;//nécessaire si on veut refaire un setList
        buildEditor();
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param items la liste d'objet a choisir, label vers objet
     */
    public BlockSelect(LocalisedItem li, String property, Map<String, T> items) {
        super(li, property);
        labels = new ArrayList<>();
        this.items = new ArrayList<>();
        setMap(items);
        buildEditor();
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param items la liste d'objet a choisir
     * @param propLib la propriété des objets a choisir a utiliser comme label
     */
    public BlockSelect(LocalisedItem li, String property, List<T> items, String propLib) {
        super(li, property);
        this.propLib = propLib;
        labels = new ArrayList<>();
        this.items = new ArrayList<>();
        items.stream().forEach((o) -> {
            labels.add(o == null ? "-" : buildLib(o, propLib));
            this.items.add(o);
        });
        buildEditor();
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param items la liste d'objet a choisir
     * @param propFunc l'extracteur de libellé
     */
    public BlockSelect(LocalisedItem li, String property, List<T> items, Function<T, String> propFunc) {
        super(li, property);
        this.propFunc = propFunc;
        labels = new ArrayList<>();
        this.items = new ArrayList<>();
        items.stream().forEach((o) -> {
            labels.add(o == null ? "-" : propFunc.apply(o));
            this.items.add(o);
        });
        buildEditor();
    }

    /**
     * Met à jour la liste de choix en modifiant propLib
     *
     * @param items la liste de choix
     */
    public void setList(List<T> items) {
        if (propLib == null && propFunc == null) {
            throw new RuntimeException("Invalid state");
        }
        labels = new ArrayList<>();
        this.items = new ArrayList<>();
        if (propFunc != null) {
            items.stream().forEach((o) -> {
                labels.add(o == null ? "-" : propFunc.apply(o));
                this.items.add(o);
            });
        } else {
            items.stream().forEach((o) -> {
                labels.add(o == null ? "-" : buildLib(o, propLib));
                this.items.add(o);
            });
        }
        model.update();
    }

    /**
     * Met à jour la liste de choix
     *
     * @param propLib la propriété des objets a choisir a utiliser comme label
     * @param items la liste de choix
     */
    public void setList(List<T> items, String propLib) {
        if (propLib == null) {
            throw new RuntimeException("Invalid state");
        }
        this.propLib = propLib;
        labels = new ArrayList<>();
        this.items = new ArrayList<>();
        items.stream().forEach((o) -> {
            labels.add(o == null ? "-" : buildLib(o, propLib));
            this.items.add(o);
        });
        model.update();
    }

    /**
     * Met à jour la liste de choix
     *
     * @param items la map label vers choix
     */
    public void setMap(Map<String, T> items) {
        labels = new ArrayList<>();
        this.items = new ArrayList<>();
        items.forEach((k, v) -> {
            labels.add(k);
            this.items.add(v);
        });
        if (model != null) {
            model.update();
        } else {
            model = new MyModel();
        }
    }

    /**
     * Constructeur pour une fabrication spécifique de la liste par une
     * surcharge de cette classe. L'appelant est chargé d'initialiser labels et
     * items, puis d'appeler buildEditor().
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'enum
     */
    protected BlockSelect(LocalisedItem li, String property, boolean gotNull) {
        super(li, property);
        this.gotNull = gotNull;
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'enum
     * @param items l'enum
     */
    public BlockSelect(LocalisedItem li, String property, Class items) {
        this(li, property, items, false);
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'enum
     * @param items l'enum
     * @param gotNull permettre un choix "null"
     */
    public BlockSelect(LocalisedItem li, String property, Class<T> items, boolean gotNull) {
        super(li, property);
        labels = new ArrayList<>();
        this.items = new ArrayList<>();

        if (gotNull) {
            this.labels.add("-");
            this.items.add(null);
        }

        for (T o : items.getEnumConstants()) {
            labels.add(li.getString(String.format("%s.%s", items.getSimpleName(), o.toString())));
            this.items.add(o);
        }
        buildEditor();
    }

    @Override
    public void copyObjectToUi() {
        Object bean = getParent().getCurrent();
        T o = (T) BeanTools.getRaw(bean, property);
        if (o != null && !items.contains(o) && (propLib != null || propFunc != null)) {
            if (propFunc != null) {
                labels.add(addExtraItem(propFunc.apply(o)));
            } else {
                labels.add(addExtraItem(buildLib(o, propLib)));
            }
            this.items.add(o);
        }
        editor.setSelectedIndex(items.indexOf(o));
    }

    /**
     * défini le libellé d'un item initialement non présent dans la liste. Cet
     * item n'est pas dans la liste, mais c'est lui qui est sélectionné dans
     * l'objet en cours d'édition.
     *
     * @param label le libellé brut de l'item
     * @return le libellé modifié (avec une mention qui dit qu'il n'est plus
     * sélectionnable pour un nouvel objet par exemple)
     */
    public String addExtraItem(String label) {
        return label;
    }

    /**
     * donne la valeur sélectionnée
     *
     * @return la valeur
     */
    public T getSelectedValue() {
        int i = editor.getSelectedIndex();
        return i < 0 ? null : items.get(i);
    }

    /**
     * change la valeur sélectionnée
     *
     * @param value la nouvelle valeur
     */
    public void setSelectedValue(T value) {
        editor.setSelectedIndex(items.indexOf(value));
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {

        try {
            BeanTools.setRaw(getParent().getCurrent(), property, getSelectedValue());
            error.setText((String) null);
            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }
    /**
     * les listeners pour une action
     */
    List<ActionListener> listeners = new ArrayList<>();

    /**
     * ajoute un action listener. Notez qu'on joue le role de proxy actif, en
     * régénérant un event plus adapté
     *
     * @param actionListener
     */
    public void addActionListener(ActionListener actionListener) {
        if (listeners.isEmpty()) {
            editor.addActionListener(this);
            editor.getSelectionModel().addChangeListener(this);
        }
        listeners.add(actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        listeners.remove(actionListener);
    }

    /**
     * Invoked when a state change occurs.
     *
     * @param e an event describing the change
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        actionPerformed(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionEvent ee = new ActionEvent(this, String.valueOf(this.items.get(editor.getSelectedIndex())));
        for (ActionListener al : listeners) {
            al.actionPerformed(ee);
        }
    }

    protected final void buildEditor() {
        if (model == null) {
            model = new MyModel();
        }
        editor = new SelectFieldEx(model);
    }

    /**
     * ajoute un null au début de la liste
     *
     * @param l la liste d'origine
     * @return la copie avec le null
     * @param T le type d'élément
     */
    public static final <T> List<T> addNull(List<T> l) {
        List d = new ArrayList();
        d.add(null);
        d.addAll(l);
        return d;
    }

    private class MyModel extends AbstractListModel {

        @Override
        public Object get(int index) {
            return labels.get(index);
        }

        @Override
        public int size() {
            return labels.size();
        }

        public MyModel() {
        }

        public void update() {
            fireContentsChanged(0, labels.size());
        }
    }
}
