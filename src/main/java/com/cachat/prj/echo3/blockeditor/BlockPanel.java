package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import jakarta.validation.Validator;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Label;
import nextapp.echo.app.layout.GridLayoutData;

/**
 * Un panel pour un formulaire
 *
 * @author scachat
 * @param <T> le type d'objet manipulé dans ce sous formulaire
 */
public class BlockPanel<T> implements BlockContainer, BlockBase<Component> {

    protected static final transient Logger logger = Logger.getLogger("BlockEditor");
    protected final LocalisedItem li;
    protected T current;
    protected List<BlockInterface> childs = new ArrayList<>();
    protected Grid grid;
    private int gridPos;
    private BlockContainer parent;
    private final String property;

    protected BlockPanel(BlockPanel<T> bp) throws CloneNotSupportedException {
        this(bp.li, bp.current);
        for (BlockInterface bi : childs) {
            add((BlockInterface) bi.clone());
        }
    }

    public BlockPanel(LocalisedItem li, T current, Extent... columnWidth) {
        this(li, current, 1, columnWidth);
    }

    public BlockPanel(LocalisedItem li, T current, int columnCount, Extent... columnWidth) {
        this(li, current, columnCount);
        if (columnWidth != null) {
            for (int i = 0; i < columnWidth.length; i++) {
                grid.setColumnWidth(i, columnWidth[i]);
            }
        }
    }

    /**
     * Construit un panel pour contenir l'editeur de current
     *
     * @param li pour la generation des label
     * @param current l'objet a editer
     */
    public BlockPanel(LocalisedItem li, T current) {
        this(li, current, 1);
    }

    /**
     * Construit un panel pour contenir l'editeur de current
     *
     * @param li pour la generation des label
     * @param current l'objet a editer
     */
    public BlockPanel(LocalisedItem li, T current, int columnCount) {
        assert columnCount > 0;
        this.grid = new Grid(3 * columnCount);
        this.grid.setWidth(new Extent(100, Extent.PERCENT));
        this.li = li;
        this.current = current;
        this.property = null;
    }

    public BlockPanel(LocalisedItem li, String property, Extent labelWidth) {
        this(li, property);
        grid.setColumnWidth(0, labelWidth);
    }

    /**
     * Construit un panel pour contenir l'editeur de property
     *
     * @param li pour la generation des label
     * @param property la propriété
     */
    public BlockPanel(LocalisedItem li, String property) {
        this.grid = new Grid(3);
        this.grid.setWidth(new Extent(100, Extent.PERCENT));
        this.li = li;
        this.property = property;
        this.current = null;
    }

    /**
     * finit de remplir la ligne en cours (si plusieurs colonnes). Le prochain
     * add sera alors en début de ligne
     */
    public void endLine() {
        while (gridPos % grid.getSize() != 0) {
            //TODO HTML5 et enlever ci dessous grid.add(new Strut(5, 5));
            grid.add(new Label(" "));
            gridPos++;
        }
    }

    @Override
    public BlockInterface add(BlockInterface bf) {

        if (bf instanceof BlockBase) {
            add((BlockBase) bf, true);
        } else if (bf instanceof BlockField) {
            childs.add(bf);
            for (Component c : ((List<Component>) ((BlockField) bf).getComponents())) {
                assert c != null : "Composant null pour " + bf.getClass().getSimpleName() + " / " + ((BlockField) bf).property;
                grid.add(c);
                gridPos++;
            }
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

        if (property != null) {
            current = (T) BeanTools.get(getParent().getCurrent(), property);
        }
        for (BlockInterface bi : childs) {
            boolean err = bi.copyUiToObject(validator, genericErrors);
            if (err) {
                logger.severe("Error on " + bi.getClass().getSimpleName());
            }
            errors |= err;
        }
        return errors;
    }

    @Override
    public Component getComponent() {
        return grid;
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
    public List<BlockInterface> getChilds() {
        return childs;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            return getClass().getConstructor(BlockPanel.class).newInstance(this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * ajoute un bloc base
     *
     * @param bf le block a ajouter
     * @param span si true, le bloc s'etalera sur toutes les colonnes (rappel 1
     * colonne du panel = 3 colonnes de la grid sous jacente). Si false, il ne
     * couvrira qu'une colonne
     */
    public void add(BlockBase bf, boolean span) {
        if (bf instanceof BlockInterface) {
            childs.add((BlockInterface) bf);
            ((BlockInterface) bf).setParent(this);
        }
        if (span) {
            endLine();
        }
        Component c = ((BlockBase) bf).getComponent();
        assert c != null : "Composant null pour " + bf.getClass().getSimpleName();
        GridLayoutData gld = new GridLayoutData();
        gld.setColumnSpan(span ? grid.getSize() : 3);
        c.setLayoutData(gld);
        grid.add(c);
        gridPos += gld.getColumnSpan();
    }

    /**
     * fixe le style de la grid
     *
     * @param style le nom du style
     */
    public void setStyleName(String style) {
        grid.setStyleName(style);
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
