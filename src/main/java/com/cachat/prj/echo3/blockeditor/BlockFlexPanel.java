package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.able.Scrollable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import jakarta.validation.Validator;
import nextapp.echo.app.Component;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Row;

/**
 * Un panel pour un formulaire composé de block flex
 *
 * @author scachat
 * @param <T> le type d'objet manipulé dans ce sous formulaire
 */
public class BlockFlexPanel<T> implements BlockContainer, BlockBase<Component> {

    /**
     * Logger
     */
    protected static final transient Logger logger = Logger.getLogger("BlockEditor");
    
    /**
     * L'instance de BlockEditor pour la generation des label, et l'ajustement
     * de ses paramètres aux besoins du flex
     */
    protected final BlockEditor editor;
    
    protected T current;
    
    protected List<BlockFlexGroup> childs = new ArrayList<>();
    
    /**
     * div principal
     */
    protected ContainerEx base;
    
    /**
     * div pour les boutons
     */
    protected ContainerEx butContainer;
    
    /**
     * row pour les boutons
     */
    protected Row butRow;
    
    /**
     * div pour les éléments flex
     */
    protected ContainerEx flexContainer;
    
    private BlockContainer parent;

    /**
     * Constructeur par recopie
     *
     * @param bp la référence
     * @throws CloneNotSupportedException
     */
    protected BlockFlexPanel(BlockFlexPanel<T> bp) throws CloneNotSupportedException {
        this(bp.editor, bp.current);
        for (BlockFlexGroup bi : childs) {
            add((BlockFlexGroup) bi.clone());
        }
    }

    /**
     * Construit un panel pour contenir l'editeur de property
     *
     * @param editor pour la generation des label, et l'ajustement de ses
     * paramètres aux besoins du flex
     */
    public BlockFlexPanel(BlockEditor editor) {
        this(editor, null);
    }

    /**
     * Construit un panel pour contenir l'editeur de current
     *
     * @param editor pour la generation des label, et l'ajustement de ses
     * paramètres aux besoins du flex
     * @param current l'objet a editer
     */
    public BlockFlexPanel(BlockEditor editor, T current) {
        editor.setFullWidth(true);
        base = new ContainerEx(0, 0, 0, 0, null, null);
        butContainer = new ContainerEx(0, null, 0, 0, null, 64);
        flexContainer = new ContainerEx(0, 0, 0, 64, null, null);
        flexContainer.setFlexBasis(ContainerEx.FLEX_BASIS_AUTO);
        flexContainer.setFlexWrap(ContainerEx.FLEX_WRAP_WRAP);
        flexContainer.setFlexGrow(0.0);
        flexContainer.setFlexShrink(0.0);
        flexContainer.setFlexDirection(ContainerEx.FLEX_DIRECTION_ROW);
        flexContainer.setResponsiveWidth(800.0);
        flexContainer.setInsets(new Insets(12, 15));
        flexContainer.setScrollBarPolicy(Scrollable.AUTO);
        base.add(flexContainer);
        base.add(butContainer);
        this.editor = editor;
        this.current = null;
    }

    @Override
    public BlockInterface add(BlockInterface bf) {

        if (bf instanceof BlockFlexGroup blockFlexGroup) {
            childs.add(blockFlexGroup);
            Component c = blockFlexGroup.getComponent();
            assert c != null : "Composant null pour " + bf.getClass().getSimpleName() + " / " + ((BlockField) bf).property;
            flexContainer.add(c);
        } else if (bf instanceof BlockEditor2.BlockButton) {
            butContainer.add(((BlockEditor2.BlockButton) bf).getComponent());
        }
        bf.setParent(this);
        return bf;
    }

    @Override
    public void remove(BlockInterface bf) {
        childs.remove(bf);
        bf.setParent(null);
    }

    @Override
    public T getCurrent() {
        return current;
    }

    @Override
    public void copyObjectToUi() {
        for (BlockInterface bi : childs) {
            bi.copyObjectToUi();
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
        boolean errors = false;

        for (BlockInterface bi : childs) {
            errors |= bi.copyUiToObject(validator, genericErrors);
        }
        return errors;
    }

    @Override
    public Component getComponent() {
        return base;
    }

    @Override
    public void setParent(BlockContainer parent) {
        this.parent = parent;
    }

    public BlockContainer getParent() {
        return parent;
    }

    @Override
    public void setVisible(boolean visible) {
        for (BlockInterface bi : childs) {
            bi.setVisible(visible);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (BlockInterface bi : childs) {
            bi.setEnabled(enabled);
        }
    }

    /**
     * donne tous les block contenus
     *
     * @return la liste des blocs
     */
    public List<BlockFlexGroup> getChilds() {
        return childs;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            return getClass().getConstructor(BlockFlexPanel.class).newInstance(this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * fixe le style de la flexContainer
     *
     * @param style le nom du style
     */
    public void setStyleName(String style) {
        flexContainer.setStyleName(style);
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
        return childs.stream().anyMatch((c) -> c.appendError(pp, msg));
    }
}
