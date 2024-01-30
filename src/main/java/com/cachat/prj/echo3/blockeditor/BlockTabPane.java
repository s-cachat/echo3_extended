package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.ng.ContainerEx;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.extras.app.TabPane;
import nextapp.echo.extras.app.event.TabSelectionEvent;
import nextapp.echo.extras.app.layout.TabPaneLayoutData;

/**
 * un container pour un formulaire à onglet. Les données sont enregistrées dans
 * l'objet (pas en base) à chaque changement d'onglet.
 *
 * @author scachat
 * @param <T> le type d'objet à éditer
 */
public class BlockTabPane<T> implements BlockContainer, BlockBase<ContainerEx>, LocalisedItem {

    /**
     * notre logger
     */
    protected static final transient Logger logger = Logger.getLogger("BlockEditor");
    /**
     * objet à éditer
     */
    private final T current;
    /**
     * Les onglets
     */
    protected TabPane tabs;
    /**
     * La liste des panneaux
     */
    private final List<TabSubPane> subPanes = new ArrayList<>();
    /**
     * le bloc parent
     */
    private BlockContainer parent;

    /**
     * Ajoute un onglet
     *
     * @param title le titre de l'onglet
     * @param panel le panel
     */
    public void add(String title, BlockPanel panel) {
        TabPaneLayoutData layout = new TabPaneLayoutData();
        layout.setTitle(title);
        final Component comp = panel.getComponent();
        comp.setLayoutData(layout);
        tabs.add(comp);
        subPanes.add(new TabSubPane(title, panel, layout));
    }

    /**
     * tab on change listener. trigger copyUiToObject which mark tab with error,
     * then perform a validation of current, and mark additionnal errors
     */
    private void tabChanged() {
        List<String> genericErrors = new ArrayList<>();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        boolean gotError = copyUiToObject(validator, genericErrors);
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(current);
        if (constraintViolations.isEmpty() && !gotError) {
            subPanes.forEach(s -> s.setError(false));
        } else if (!subPanes.isEmpty()) {
            subPanes.forEach(s -> s.setError(!genericErrors.isEmpty()));
            constraintViolations.forEach(cv -> {
                subPanes.forEach(sp -> sp.checkForError(cv.getPropertyPath()));
            });
        }
    }

    /**
     * définie l'onglet actif
     *
     * @param tabIndex l'index de l'onglet actif
     */
    public void setSelectedTab(int tabIndex) {
        tabs.setActiveTabIndex(tabIndex);
    }

    /**
     * donne l'index de l'onglet actif
     *
     * @return l'index de l'onglet actif
     */
    public int getSelectedTab() {
        return tabs.getActiveTabIndex();
    }

    /**
     * Un sous panneau
     */
    private class TabSubPane {

        /**
         * le titre
         */
        private final String title;
        /**
         * le sous formulaire
         */
        private final BlockPanel panel;
        /**
         * les informations de layout
         */
        private TabPaneLayoutData tld;

        /**
         * Constructeur
         *
         * @param title le titre
         * @param panel le sous formulaire
         * @param tld les informations de layout
         */
        public TabSubPane(String title, BlockPanel panel, TabPaneLayoutData tld) {
            this.title = title;
            this.panel = panel;
            this.tld = tld;
        }

        public String getTitle() {
            return title;
        }

        public BlockPanel getPanel() {
            return panel;
        }

        /**
         * affiche l'onglet avec les couleurs normale ou en erreur
         *
         * @param error si true, une erreur est présente sur l'onglet
         */
        public void setError(boolean error) {
            if (error) {
                tld.setActiveBackground(Color.RED);
                tld.setInactiveBackground(new Color(128, 0, 0));
                tld.setActiveForeground(Color.WHITE);
                tld.setInactiveForeground(Color.WHITE);
            } else {
                tld.setActiveBackground(null);
                tld.setInactiveBackground(null);
                tld.setActiveForeground(null);
                tld.setInactiveForeground(null);
            }
        }

        /**
         * error marking second pass. if the path match one of our component,
         * mark the tab in error, else leave it unchanged
         *
         * @param propertyPath the pasth of the property in error
         */
        private void checkForError(Path propertyPath) {
            //TODO
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
    public BlockTabPane(LocalisedItem li, T current) {
        this.li = li;
        this.current = current;
        tabs = new TabPane();
        tabs.setStyleName("Tab");

        container.add(tabs);
        tabs.addTabSelectionListener((TabSelectionEvent e) -> tabChanged());

    }

    /**
     * constructeur par recopie
     *
     * @param o l'original
     * @throws CloneNotSupportedException si le clonage n'est pas supporté
     */
    private BlockTabPane(BlockTabPane<T> o) throws CloneNotSupportedException {
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
        return new BlockTabPane(this);
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
        for (Iterator<TabSubPane> it = subPanes.iterator(); it.hasNext();) {
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
     * genericErrors. First part of tab error marking : tab is red if there were
     * an error on e field during copyUiToObject, else it is normal.
     *
     * @param genericErrors a list for generic error
     * @param validator le validateur utilisé
     * @return true if there were errors
     */
    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        boolean res = true;
        for (TabSubPane s : subPanes) {
            boolean r = s.getPanel().copyUiToObject(validator, genericErrors);
            res &= r;
            s.setError(r);
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
        tabs.setVisible(visible);
    }

    /**
     * définie l'état du composant
     *
     * @param enabled true si actif
     */
    @Override
    public void setEnabled(boolean enabled) {
        tabs.setEnabled(enabled);
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
