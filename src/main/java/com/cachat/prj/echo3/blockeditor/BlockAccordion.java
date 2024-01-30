package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.ng.ContainerEx;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import jakarta.validation.Validator;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.extras.app.AccordionPane;
import nextapp.echo.extras.app.layout.AccordionPaneLayoutData;

/**
 * un container pour un formulaire à onglet.
 *
 * @author scachat
 * @param <T> le type d'objet à éditer
 */
public class BlockAccordion<T> implements BlockContainer, BlockBase<ContainerEx>, LocalisedItem {

    /**
     * notre logger
     */
    protected static final transient Logger logger = Logger.getLogger("BlockEditor");
    /**
     * objet à éditer
     */
    private final T current;
    /**
     * L'accordéon
     */
    protected AccordionPane accordion;
    /**
     * La liste des panneaux
     */
    private final List<AccordionSubPane> subPanes = new ArrayList<>();
    private BlockContainer parent;

    /**
     * Ajoute un élément à l'accordéon
     *
     * @param title le titre de l'accordéon
     * @param panel le panel
     */
    public void add(String title, BlockPanel panel) {
        AccordionPaneLayoutData ld = new AccordionPaneLayoutData();
        ld.setTitle(title);
        final Component comp = panel.getComponent();
        comp.setLayoutData(ld);
        accordion.add(comp);
        subPanes.add(new AccordionSubPane(title, panel));
    }

    /**
     * Un sous panneau
     */
    private class AccordionSubPane {

        /**
         * le titre
         */
        private final String title;
        /**
         * le sous formulaire
         */
        private final BlockPanel panel;

        public AccordionSubPane(String title, BlockPanel panel) {
            this.title = title;
            this.panel = panel;
        }

        public String getTitle() {
            return title;
        }

        public BlockPanel getPanel() {
            return panel;
        }

    }

    /**
     * objet pour la localisation
     */
    private LocalisedItem li;
    /**
     * container global
     */
    private ContainerEx container = new ContainerEx();
    /**
     * Formulaire
     */
    private Grid form;

    /**
     * Constructeur
     *
     * @param li objet pour la localisation
     * @param current objet à éditer
     */
    public BlockAccordion(LocalisedItem li, T current) {
        this.li = li;
        this.current = current;

        accordion = new AccordionPane();
        container.add(accordion);

    }

    /**
     * constructeur par recopie
     *
     * @param o l'original
     * @throws CloneNotSupportedException si le clonage n'est pas supporté
     */
    private BlockAccordion(BlockAccordion<T> o) throws CloneNotSupportedException {
        this(o.li, o.current);
        subPanes.forEach((bi) -> add(bi.getTitle(), bi.getPanel()));
    }

    /**
     * clone l'objet
     *
     * @return un clone
     * @throws CloneNotSupportedException si le clonage n'est pas supporté
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new BlockAccordion(this);
    }

    /**
     * ajoute un sous bloc (uniquement de type bloc panel)
     *
     * @param bf le bloc
     * @return le bloc
     */
    @Override
    public BlockInterface add(BlockInterface bf) {
        if (!(bf instanceof BlockPanel)) {
            throw new RuntimeException("Invalid class " + bf.getClass().getName() + ", only BlockPanel are accepted here");
        }
        add(null, (BlockPanel) bf);
        return bf;
    }

    /**
     * supprime un sous bloc
     *
     * @param bf le bloc
     */
    @Override
    public void remove(BlockInterface bf) {
        for (Iterator<AccordionSubPane> it = subPanes.iterator(); it.hasNext();) {
            if (it.next().getPanel().equals(bf)) {
                it.remove();
                break;
            }
        }
    }

    /**
     * donne l'objet en cours d'édition
     *
     * @return l'objet
     */
    @Override
    public T getCurrent() {
        return current;
    }

    /**
     * Donne le composant principal
     *
     * @return
     */
    @Override
    public ContainerEx getComponent() {
        return container;
    }

    /**
     * copy object values to ui components
     */
    @Override
    public void copyObjectToUi() {
        subPanes.stream().forEach((a) -> a.getPanel().copyObjectToUi());
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
        boolean res = false;
        for (AccordionSubPane s : subPanes) {
            res |= s.getPanel().copyUiToObject(validator, genericErrors);
        }
        return res;
    }

    /**
     * Définie l'objet parent
     */
    @Override
    public void setParent(BlockContainer parent) {
        this.parent = parent;
    }

    /**
     * Définie la visibilité du composant
     *
     * @param visible la visibilité
     */
    @Override
    public void setVisible(boolean visible) {
        accordion.setVisible(visible);
    }

    /**
     * définie l'état du composant
     *
     * @param enabled true si actif
     */
    @Override
    public void setEnabled(boolean enabled) {
        accordion.setEnabled(enabled);
        subPanes.stream().forEach((a) -> a.getPanel().setEnabled(enabled));
    }

    /**
     * donne une chaine de caractere dans la bonne langue, avec le
     * ressourcebundle du container.
     *
     * @param key la clé
     * @return la chaine de caractère
     */
    @Override
    public String getString(String key) {
        return li.getString(key);
    }

    /**
     * donne une chaine de caractere dans la bonne langue, avec le
     * ressourcebundle global
     *
     * @param key la clé
     * @return la chaine de caractère
     */
    @Override
    public String getBaseString(String key) {
        return li.getBaseString(key);
    }

    /**
     * donne la langue
     *
     * @return la langue
     */
    @Override
    public Locale getLocale() {
        return li.getLocale();
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
}
