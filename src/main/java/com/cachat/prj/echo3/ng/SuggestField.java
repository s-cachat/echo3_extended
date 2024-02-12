package com.cachat.prj.echo3.ng;

import com.cachat.util.BeanTools;
import de.exxcellent.echolot.event.SuggestItemSelectEvent;
import de.exxcellent.echolot.listener.SuggestItemSelectListener;
import de.exxcellent.echolot.model.SuggestItem;
import de.exxcellent.echolot.model.SuggestModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * Un champ de suggestion (intégration Echo3 de la classe
 * de.exxcellent.echolot.app.SuggestField)
 *
 * @author user1
 * @param <T> le type d'objet
 */
public class SuggestField<T> extends de.exxcellent.echolot.app.SuggestField implements SuggestItemSelectListener {

    /**
     * Fourni une liste d'objets pouvant correspondre à la suggestion. Il faut
     * fournir soit le requester, soit le requester2
     */
    private final Requester<T> requester;
    /**
     * Fourni une liste d'objets pouvant correspondre à la suggestion. Il faut
     * fournir soit le requester, soit le requester2
     */
    private final Requester2<T> requester2;

    /**
     * Objet sélectionné
     */
    private T selected = null;

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
     * @param requester la fonction de recherche
     * @param propertyLabel le ou les libellés principaux, séparés par des
     * virgules
     * @param propertyDescription le ou les libellés secondaires, séparés par
     * des virgules. Peux être null.
     */
    public SuggestField(Requester requester, String propertyLabel, String propertyDescription) {
        super();
        this.requester = requester;
        this.requester2 = null;
        this.propertyLabel = propertyLabel;
        this.propertyDescription = propertyDescription;
        init();
    }

    /**
     * Constructeur
     *
     * @param requester2 la fonction de recherche avec callback
     * @param propertyLabel le ou les libellés principaux, séparés par des
     * virgules
     * @param propertyDescription le ou les libellés secondaires, séparés par
     * des virgules. Peux être null.
     */
    public SuggestField(Requester2 requester2, String propertyLabel, String propertyDescription) {
        super();
        this.requester = null;
        this.requester2 = requester2;
        this.propertyLabel = propertyLabel;
        this.propertyDescription = propertyDescription;
        init();
    }

    @Override
    public void init() {
        super.init();
        setStyleName("Grid");
        addSuggestItemSelectListener(this);
        addServerFilterListener((e) -> {
            String str = e.getInputData();
            if (requester != null) {
                List<T> sl = requester.getSuggestion(str);
                updateData(sl);
            } else {
                requester2.getSuggestion(str, sl -> {
                    updateData(sl);
                });
            }
        });
        setDoServerFilter(true);
        setShowDescription(true);
        if (propertyDescription != null) {
            setDescriptionFont(new Font(Font.ARIAL, Font.ITALIC, new Extent(8, Extent.PT)));
        }
    }

    private void updateData(List<T> sl) {
        SuggestItem res[] = new SuggestItem[sl.size()];
        for (int i = 0; i < sl.size(); i++) {
            res[i] = suggest(sl.get(i));
        }
        setSuggestModel(new SuggestModel(res));
    }

    /**
     * Transforme un objet en suggestion
     *
     * @param o l'objet
     * @return la suggestion
     */
    private SuggestItem suggest(T o) {
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
        selected = si == null ? null : (T) si.getUserObject();
        setSuggestModel(new SuggestModel(new SuggestItem[]{si}));
        ActionEvent ee = new ActionEvent(this, si == null ? "" : si.getLabel());
        listeners.forEach((al) -> al.actionPerformed(ee));
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
    @Override
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
    public T getSelectedValue() {
        return selected;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public String getPropertyLabel() {
        return propertyLabel;
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
}
