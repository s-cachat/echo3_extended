package com.cachat.prj.echo3.criteres;

import com.cachat.util.BeanTools;
import de.exxcellent.echolot.app.SuggestField;
import de.exxcellent.echolot.model.SuggestItem;
import de.exxcellent.echolot.model.SuggestModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Font;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * Critère de recherche par texte qui propose des suggestions à l'utilisateur
 * après au'il a saisit 3 trois caractères
 *
 * @author user1
 * @param <T> Le type d'entité
 */
public class SuggestCrit<T> extends Crit {

    /**
     * Champ de suggestion
     */
    private final SuggestField sf;

    /**
     * Le requester
     */
    private final Requester<T> requester;
    /**
     * Le requester
     */
    private final Requester2<T> requester2;

    /**
     * Nom de la propriete libellé
     */
    private final String propLib;

    /**
     * Limite de suggestions à afficher
     */
    private int sizeLimit = 21;

    /**
     * Mode de sélection
     */
    private ModeSelect mode = ModeSelect.EQUALS;

    /**
     * Entité séléctionné
     */
    private T selected;

    /**
     * Constructeur.
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critère de l'entité parente
     * @param requester le requester
     * @param propLib le nom de la propriete libellé
     * @param mode le mode de sélection
     * @param sizeLimit le nombre de suggestions à afficher
     */
    public SuggestCrit(CritContainer cont, String prop, Requester<T> requester, String propLib, ModeSelect mode, int sizeLimit) {
        this(cont, prop, requester, propLib);
        this.mode = mode;
        this.sizeLimit = sizeLimit;
    }

    /**
     * Constructeur.
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critère de l'entité parente
     * @param requester le requester
     * @param propLib le nom de la propriete libellé
     * @param sizeLimit le nombre de suggestions à afficher
     */
    public SuggestCrit(CritContainer cont, String prop, Requester<T> requester, String propLib, int sizeLimit) {
        this(cont, prop, requester, propLib);
        this.sizeLimit = Math.max(0, sizeLimit);
    }

    /**
     * Constructeur.
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critère de l'entité parente
     * @param requester le requester
     * @param propLib le nom de la propriete libellé
     * @param mode le mode de sélection
     */
    public SuggestCrit(CritContainer cont, String prop, Requester<T> requester, String propLib, ModeSelect mode) {
        this(cont, prop, requester, propLib);
        this.mode = mode;
    }

    /**
     * Constructeur.
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critère de l'entité parente
     * @param requester le requester
     * @param propLib le nom de la propriete libellé
     */
    public SuggestCrit(CritContainer cont, String prop, Requester<T> requester, String propLib) {
        this(cont,prop,requester,null,propLib);
    }
    

    /**
     * Constructeur.
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critère de l'entité parente
     * @param requester2 le requester
     * @param propLib le nom de la propriete libellé
     * @param mode le mode de sélection
     * @param sizeLimit le nombre de suggestions à afficher
     */
    public SuggestCrit(CritContainer cont, String prop, Requester2<T> requester2, String propLib, ModeSelect mode, int sizeLimit) {
        this(cont, prop, requester2, propLib);
        this.mode = mode;
        this.sizeLimit = sizeLimit;
    }

    /**
     * Constructeur.
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critère de l'entité parente
     * @param requester2 le requester
     * @param propLib le nom de la propriete libellé
     * @param sizeLimit le nombre de suggestions à afficher
     */
    public SuggestCrit(CritContainer cont, String prop, Requester2<T> requester2, String propLib, int sizeLimit) {
        this(cont, prop, requester2, propLib);
        this.sizeLimit = Math.max(0, sizeLimit);
    }

    /**
     * Constructeur.
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critère de l'entité parente
     * @param requester2 le requester
     * @param propLib le nom de la propriete libellé
     * @param mode le mode de sélection
     */
    public SuggestCrit(CritContainer cont, String prop, Requester2<T> requester2, String propLib, ModeSelect mode) {
        this(cont, prop, requester2, propLib);
        this.mode = mode;
    }

    /**
     * Constructeur.
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critère de l'entité parente
     * @param requester2 le requester
     * @param propLib le nom de la propriete libellé
     */
    public SuggestCrit(CritContainer cont, String prop, Requester2<T> requester2, String propLib) {
        this(cont,prop,null,requester2,propLib);
    }
    
    /**
     * Constructeur.
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critère de l'entité parente
     * @param requester le requester (si défini, alors requester2 doit être null)
     * @param requester2 le requester2 (si défini, alors requester doit être null)
     * @param propLib le nom de la propriete libellé
     */
    private SuggestCrit(CritContainer cont, String prop, Requester<T> requester,Requester2<T> requester2, String propLib) {        
        super(cont, prop);
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        sf = new SuggestField();
        sf.setStyleName("Grid");
        sf.setWidth(new Extent(99, Extent.PERCENT));
        this.requester = requester;
        this.requester2 = requester2;
        this.propLib = propLib;
        sf.addSuggestItemSelectListener((e) -> {
            final SuggestItem si = e.getSuggestItem();
            if (si != null) {
                selected = (T) si.getUserObject();
                sf.setSuggestModel(new SuggestModel(new SuggestItem[]{si}));
                ActionEvent ee = new ActionEvent(this, si.getLabel());
                listeners.forEach((al) -> al.actionPerformed(ee));
            }

        });
        sf.addServerFilterListener((e) -> {
            String str = e.getInputData();
            List<T> sl = this.requester.getSuggestion(str);
            if (sl != null && sl.size() <= sizeLimit) {
                SuggestItem res[] = new SuggestItem[sl.size()];
                for (int i = 0; i < sl.size(); i++) {
                    res[i] = suggest(sl.get(i));
                }
                sf.setSuggestModel(new SuggestModel(res));
            }
        });
        sf.setDoServerFilter(true);
        sf.setShowDescription(true);
        if (propLib != null) {
            sf.setDescriptionFont(new Font(Font.ARIAL, Font.ITALIC, new Extent(8, Extent.PT)));
        }
        sf.getDocument().addDocumentListener((e) -> {
            String str = sf.getDocument().getText();
            if (str == null || str.trim().length() == 0) {
                selected = null;
                sf.setSuggestModel(new SuggestModel(new SuggestItem[]{}));
                ActionEvent ee = new ActionEvent(this, null);
                listeners.forEach((al) -> al.actionPerformed(ee));
            }
        });
        sf.addActionListener(cont);
        listeners.add(cont);
        critf.add(sf);
        cont.addCrit(this);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
    }

    @Override
    public String updateWhere(List<Object> arg) {
        T s = getSelectedValue();
        if (s != null) {
            arg.add(s);
            return String.format(mode.getQuery(), prop);
        } else {
            return null;
        }
    }

    @Override
    public String getSummary() {
        T s = getSelectedValue();
        if (s != null) {
            return String.format(mode.getQuery(), cont.getString(prop), BeanTools.get(s, propLib));
        } else {
            return null;
        }
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
        if (propLib != null) {
            res = new SuggestItem(BeanTools.get(o, propLib));
        } else {
            res = new SuggestItem(Objects.toString(o));
        }
        res.setUserObject(o);
        return res;
    }

    /**
     * change le fond du champs
     */
    public void setBackground(Color color) {
        sf.setBackground(color);
    }

    /**
     * change le premier plan du champs
     */
    public void setForeground(Color color) {
        sf.setForeground(color);
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
    public T getSelectedValue() {
        return selected;
    }

    /**
     * fixe une valeur par défaut. N'appelle pas les listeners (c'est
     * généralement trop tot)
     *
     * @param value la valeur
     */
    public void setSelectedValue(T value) {
        final SuggestItem si = suggest(value);
        if (si != null) {
            selected = (T) si.getUserObject();
            ActionEvent ee = new ActionEvent(this, si.getLabel());
            sf.setText(si.getLabel());
        }
    }

    public String getPropLib() {
        return propLib;
    }
    //</editor-fold>
}
