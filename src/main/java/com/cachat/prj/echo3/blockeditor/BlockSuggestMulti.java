package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.list.ActionButton;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.util.BeanTools;
import com.fasterxml.jackson.databind.PropertyName;
import de.exxcellent.echolot.app.SuggestField;
import de.exxcellent.echolot.event.SuggestItemSelectEvent;
import de.exxcellent.echolot.listener.SuggestItemSelectListener;
import de.exxcellent.echolot.model.SuggestItem;
import de.exxcellent.echolot.model.SuggestModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import jakarta.validation.Validator;
import java.util.function.Consumer;
import nextapp.echo.app.Column;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * Champs de selection pour une liste, avec auto complete
 *
 * @param <T> le type d'objet à sélectionner
 * @author scachat
 */
public class BlockSuggestMulti<T> extends BlockField<Column> implements SuggestItemSelectListener {

    /**
     * Fourni une liste d'objets pouvant correspondre à la suggestion
     */
    private final Requester<T> requester;
    /**
     * Fourni une liste d'objets pouvant correspondre à la suggestion
     */
    private final Requester2<T> requester2;
    /**
     * mode lecture/écriture
     */
    protected SuggestField addField;

    /**
     * grille pour les éléments sélectionnés
     */
    protected Grid selectedItems;
    /**
     * mode actuel
     */
    private boolean canWrite = true;

    /**
     * Objets sélectionnés
     */
    protected List<T> selected = null;

    /**
     * Propriété des objets pour la description (optionnel)
     */
    private final String propertyDescription;

    /**
     * Propriété des objets pour le label
     */
    private final String propertyLabel;

    /**
     * Constructeur
     *
     * @param bf la référence
     */
    public BlockSuggestMulti(BlockField bf) {
        super(bf.getLocalisedItem(), bf.getProperty());
        this.requester = ((BlockSuggestMulti) bf).requester;
        this.requester2 = ((BlockSuggestMulti) bf).requester2;
        if (bf instanceof BlockSuggestMulti blockSuggestMulti) {
            this.propertyLabel = blockSuggestMulti.getPropertyLabel();
            this.propertyDescription = blockSuggestMulti.getPropertyDescription();
        } else {
            this.propertyLabel = bf.getProperty();
            this.propertyDescription = bf.getProperty();
        }
        buildEditor();
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param requester chargé de fournir les suggestions
     * @param propertyLabel le libellé du propriété
     * @param propertyDescription la description du propriété
     */
    public BlockSuggestMulti(LocalisedItem li, String property, Requester<T> requester, String propertyLabel, String propertyDescription) {
        super(li, property);
        this.requester = requester;
        this.requester2 = null;
        this.propertyLabel = propertyLabel;
        this.propertyDescription = propertyDescription;
        buildEditor();
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property la propriété de type liste d'objet
     * @param requester2 chargé de fournir les suggestions
     * @param propertyLabel le libellé du propriété
     * @param propertyDescription la description du propriété
     */
    public BlockSuggestMulti(LocalisedItem li, String property, Requester2<T> requester2, String propertyLabel, String propertyDescription) {
        super(li, property);
        this.requester = null;
        this.requester2 = requester2;
        this.propertyLabel = propertyLabel;
        this.propertyDescription = propertyDescription;
        buildEditor();
    }

    @Override
    public void copyObjectToUi() {
        Object bean = getParent().getCurrent();
        selected = (List<T>) BeanTools.getRaw(bean, property);
        if (selected == null) {
            selected = new ArrayList<>();
        }
        updateUi();
    }

    private void updateUi() {
        selectedItems.removeAll();
        for (T i : selected) {
            SuggestItem s = suggest(i);
            selectedItems.add(new Label(s.getLabel()));
            selectedItems.add(new ActionButton("X", () -> {
                selected.remove(i);
                updateUi();
            }));
        }
    }

    /**
     * copy ui components (edited) values to object. for the fields, if a error
     * is encountered (bad values, ...), it should be displayed in the field's
     * error label. If the error is really not field specific, it can be
     * displayed at the top of the form, by adding the error text to
     * genericErrors.
     *
     * @param genericErrors a list for generic error
     * @param validator le validateur utilisé
     * @return true if there were errors
     */
    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        if (!canWrite) {
            return false;
        }
        try {
            BeanTools.setRaw(getParent().getCurrent(), property, selected);
            error.setText((String) null);
            return validateProperty(validator, getParent().getCurrent(), property);
        } catch (Throwable e) {
            handleError(e, genericErrors);
            return true;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.canWrite = enabled;
        addField.setVisible(enabled);
    }

    protected final void buildEditor() {
        editor = new Column();
        
        addField = new SuggestField();
        addField.setStyleName("BlockEditor");
        addField.addSuggestItemSelectListener(this);
        addField.addServerFilterListener((e) -> {
            String str = e.getInputData();
            logger.log(Level.INFO, "serverFilterListener : {0}", str);
            if (requester != null) {
                List<T> sl = requester.getSuggestion(str);
                updateData(sl);
            } else {
                requester2.getSuggestion(str, sl -> updateData(sl));
            }

        });
        addField.setDoServerFilter(true);
        addField.setShowDescription(propertyDescription!=null && !propertyDescription.equals(propertyLabel));
        if (propertyDescription != null) {
            addField.setDescriptionFont(new Font(Font.ARIAL, Font.ITALIC, new Extent(8, Extent.PT)));
        }
        editor.add(addField);
        selectedItems =new Grid(2);
        addField.setVisible(canWrite);
    }

    private void updateData(List<T> sl) {
        SuggestItem res[];
        if (sl != null) {
            res = new SuggestItem[sl.size()];
            for (int i = 0; i < sl.size(); i++) {
                res[i] = suggest(sl.get(i));
            }
        } else {
            res = new SuggestItem[0];
        }
        addField.setSuggestModel(new SuggestModel(res));
        error.setText(localisedItem.getBaseString("invalid"));
    }

    /**
     * Transforme un objet en suggestion
     *
     * @param o l'objet
     * @return la suggestion
     */
    protected SuggestItem suggest(T o) {
        if (o == null) {
            return null;
        }
        SuggestItem res;
        if (propertyLabel == null) {
            res = new SuggestItem(Objects.toString(o));
        } else {
            res = new SuggestItem(BeanTools.get(o, propertyLabel));
        }
        res.setUserObject(o);
        if (propertyDescription != null) {
            res.setDescription(BeanTools.get(o, propertyDescription));
        }
        return res;
    }

    @Override
    public void suggestItemSelected(SuggestItemSelectEvent e) {
        final SuggestItem si = e.getSuggestItem();
        T item = si == null ? null : (T) si.getUserObject();
        if (item != null) {
            selected.add(item);
        } else {
            return;
        }
        addField.setSuggestModel(null);
        error.setText(null);
        ActionEvent ee = new ActionEvent(this, si.getLabel());
        listeners.forEach((al) -> al.actionPerformed(ee));
    }

    /**
     * Fourni une liste d'objets pouvant correspondre à la suggestion
     *
     * @param <T> le type d'objet
     */
    public static interface Requester<T> {

        /**
         * Fourni une liste d'objets pouvant correspondre à la suggestion
         *
         * @param crit la suggestion
         * @return le résultat
         */
        public List<T> getSuggestion(String crit);
    }

    /**
     * Fourni une liste d'objets pouvant correspondre à la suggestion. Le
     * résultat est fourni via un callback, ce qui permet de générer l'ui dans
     * le cadre d'une session de persistence, et donc de ne pas avoir de
     * problème de lazy loading.
     *
     * @param <T> le type d'objet
     */
    public static interface Requester2<T> {

        /**
         * Fourni une liste d'objets pouvant correspondre à la suggestion
         *
         * @param crit la suggestion
         * @param callback le callback (appelé dans le contexte d'une session de
         * persistence si nécessaire)
         */
        public void getSuggestion(String crit, Consumer<List<T>> callback);
    }
    //<editor-fold desc="Listeners" defaultstate="collapsed">
    // Les listeners pour une action
    List<ActionListener> listeners = new ArrayList<>();

    /**
     * Ajoute un action listener. Notez qu'on joue le role de proxy actif, en
     * régénérant un event plus adapté
     *
     * @param actionListener le listener
     */
    public void addActionListener(ActionListener actionListener) {
        listeners.add(actionListener);
    }

    /**
     * Enleve un action listener.
     *
     * @param actionListener le listener
     */
    public void removeActionListener(ActionListener actionListener) {
        listeners.remove(actionListener);
    }

    //</editor-fold>
    //<editor-fold desc="Getters" defaultstate="collapsed">
    public List<T> getSelectedValues() {
        return selected;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public String getPropertyLabel() {
        return propertyLabel;
    }
    //</editor-fold>
}
