package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.interfaces.Activable;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.Strut;
import com.cachat.prj.echo3.ng.TableEx;
import com.cachat.util.BeanTools;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.persistence.OneToMany;
import jakarta.validation.Validator;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Border;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.Row;
import nextapp.echo.app.Table;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.ColumnLayoutData;
import nextapp.echo.app.table.AbstractTableModel;
import nextapp.echo.app.table.DefaultTableCellRenderer;

/**
 * ce bloc permet d'éditer une liste d'objets identiques sous la forme d'un
 * tableau. Les block enfants décrivent chacune des colonnes. peut être utilisé
 * comme localizedItem, en ajoutant le titleKey aux clés
 *
 * @author scachat
 */
public class BlockTable<T> implements BlockContainer, BlockBase<Column>, LocalisedItem {

    protected static final transient Logger logger = Logger.getLogger("BlockEditor");
    protected final LocalisedItem li;
    private final Column column;
    private final List<T> current;
    //la liste des enfants théoriques
    private final List<BlockInterface> childs = new ArrayList<>();
    //La liste des lignes
    private final List<BlockLine> rows = new ArrayList<>();
    protected TableEx table;
    private BlockContainer parent;
    private final EditTableModel model;
    private final String property;
    private final List<T> removed;
    private final List<T> added;
    private final Class[] clazz;
    private final boolean canAdd;
    private final DeleteMode deleteMode;
    private boolean enabled = true;
    private Color c1 = new Color(220, 220, 220);
    private Color c2 = new Color(255, 255, 255);
    protected LabelEx error;

    /**
     * mode d'effacement
     */
    public static enum DeleteMode {
        NONE,
        DELETE,
        DEACTIVATE
    }
    /**
     * les boutons d'éditions
     *
     */
    private final List<Component> editButtons = new ArrayList<>();

    protected BlockTable(BlockTable<T> bp) throws CloneNotSupportedException {
        this(bp.li, bp.property, bp.current, bp.canAdd, bp.deleteMode, bp.clazz);
        for (BlockInterface bi : childs) {
            add((BlockInterface) bi.clone());
        }
    }

    /**
     * Construit un panel pour contenir l'editeur de current
     *
     * @param li pour la generation des label
     * @param titleKey le nom de la propriété pour le titre
     * @param clazz le type
     * @param current l'objet a editer
     */
    public BlockTable(LocalisedItem li, String titleKey, List<T> current, Class<T> clazz) {
        this(li, titleKey, current, clazz, true);
    }

    /**
     * Construit un panel pour contenir l'editeur de current
     *
     * @param li pour la generation des label
     * @param current l'objet a editer
     * @param titleKey le nom de la propriété pour le titre
     * @param clazz le type
     * @param canAddRemove si true, l'ajout et la suppression de lignes sont
     * possibles
     */
    public BlockTable(LocalisedItem li, String titleKey, List<T> current, Class<T> clazz, boolean canAddRemove) {
        this(li, titleKey, current, clazz, canAddRemove, canAddRemove);
    }

    /**
     * Construit un panel pour contenir l'editeur de current
     *
     * @param li pour la generation des label
     * @param current l'objet a editer
     * @param property le nom de la propriété pour le titre et pour le set
     * réciproque (père d'un nouvel objet)
     * @param clazz le type
     * @param canAdd si true, l'ajout de lignes est possible
     * @param canRemove si true, la suppression de lignes est possibles
     * @deprecated
     */
    @Deprecated
    public BlockTable(LocalisedItem li, String property, List<T> current, Class<T> clazz, boolean canAdd, boolean canRemove) {
        this(li, property, current, canAdd, canRemove, clazz);
    }

    /**
     * Construit un panel pour contenir l'editeur de current
     *
     * @param li pour la generation des label
     * @param current l'objet a editer
     * @param property le nom de la propriété pour le titre et pour le set
     * réciproque (père d'un nouvel objet)
     * @param clazz le type
     * @param canAdd si true, l'ajout de lignes est possible
     * @param canRemove si true, la suppression de lignes est possibles
     */
    public BlockTable(LocalisedItem li, String property, List<T> current, boolean canAdd, boolean canRemove, Class<T>... clazz) {
        this(li, property, current, canAdd, canRemove ? DeleteMode.DELETE : DeleteMode.NONE, clazz);
    }

    /**
     * Construit un panel pour contenir l'editeur de current
     *
     * @param li pour la generation des label
     * @param current l'objet a editer
     * @param property le nom de la propriété pour le titre et pour le set
     * réciproque (père d'un nouvel objet)
     * @param clazz le type
     * @param canAdd si true, l'ajout de lignes est possible
     * @param deleteMode si true, la suppression de lignes est possibles
     */
    public BlockTable(LocalisedItem li, String property, List<T> current, boolean canAdd, DeleteMode deleteMode, Class<T>... clazz) {
        this.clazz = clazz;
        this.deleteMode = deleteMode;
        this.canAdd = canAdd;
        assert clazz != null;
        model = new EditTableModel();
        this.property = property;
        column = new Column();
        column.add(new Strut(5, 5));
        ColumnLayoutData cld = new ColumnLayoutData();
        cld.setAlignment(Alignment.ALIGN_TOP);
        LabelEx label = new LabelEx(li.getString(property));

        removed = new ArrayList<>();
        added = new ArrayList<>();
        Row r = new Row();
        column.add(r);
        r.add(label);

        if (canAdd) {
            for (Class cl : clazz) {
                ButtonEx but = new ButtonEx(li.getString((clazz.length < 2 ? property : (property + "." + cl.getSimpleName())) + ".add"));
                but.setStyleName("Button");

                r.add(new Strut(20, 1));
                r.add(but);
                editButtons.add(but);
                but.addActionListener((e) -> addRow(cl, property));
            }
        } else {
            column.add(label);
        }
        error = new LabelEx();
        error.setStyleName("ErrorMsg");
        r.add(error);
        column.add(new Strut(3, 3));

        this.li = li;
        this.current = current;
    }

    /**
     * Ajoute une ligne
     */
    protected void addRow(Class cl, String property) {
        try {
            T x = (T) cl.getConstructor().newInstance();
            initInstance(x);
            BlockLine bl;
            rows.add(bl = new BlockLine(x));
            bl.copyObjectToUi();
            added.add(x);
            setReciproque(getParent().getCurrent(), property, x);
            model.update();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * supprime une ligne
     *
     * @param line la ligne
     */
    public void removeRow(BlockLine line) throws RuntimeException {
        removed.add(line.getCurrent());
        if (!rows.remove(line)) {
            throw new RuntimeException("Can't remove row !");
        }
        column.remove(table);
        column.add(table);
//            int lno = BlockTable.this.current.indexOf(this.line.getCurrent());
//            model.fireTableRowsUpdated(lno, lno);
        model.fireTableStructureChanged();
        model.update();
    }

    public Color getC1() {
        return c1;
    }

    public void setC1(Color c1) {
        this.c1 = c1;
    }

    public Color getC2() {
        return c2;
    }

    public void setC2(Color c2) {
        this.c2 = c2;
    }

    /**
     * initialise la nouvelle instance
     *
     * @param instance
     */
    protected void initInstance(T instance) {
    }

    /**
     * Permet d'utiliser la table comme localizedItem, en ajoutant le titleKey
     * aux clés
     *
     * @param key
     * @return
     */
    @Override
    public String getString(String key) {
        return li.getString(property + "." + key);
    }

    @Override
    public String getBaseString(String key) {
        return li.getBaseString(key);
    }

    @Override
    public Locale getLocale() {
        return li.getLocale();
    }

    /**
     * lors de l'ajout d'une ligne, si la collection a une relation onetomany
     * avec un mapped by, reseigne la propriété de la nouvelle ligne
     *
     * @param current l'objet contenant
     * @param property la propriété du contenant
     * @param x l'objet de la nouvelle ligne
     */
    private void setReciproque(Object current, String property, T x) {
        OneToMany otm = null;
        if (current != null) {
            try {
                Field f = current.getClass().getDeclaredField(property);
                otm = f.getAnnotation(OneToMany.class);
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(BlockTable.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (otm == null) {
                try {
                    Method m = current.getClass().getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1));
                    otm = m.getAnnotation(OneToMany.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(BlockTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (otm != null) {
            String mb = otm.mappedBy();
            if (mb != null) {
                try {
                    BeanTools.setRaw(x, mb, current);
                } catch (NoSuchElementException | ParseException ex) {
                    Logger.getLogger(BlockTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    class ButtonDel extends ButtonEx implements ActionListener, BlockInterface {

        BlockLine line;

        public ButtonDel(BlockLine line) {
            super(new ResourceImageReference("/com/cachat/prj/echo3/blockeditor/delete.png"));
            this.line = line;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setEnabled(false);
            removeRow(line);
        }

        @Override
        public void copyObjectToUi() {
            //nop
        }

        @Override
        public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
            return false;
        }

        @Override
        public void setParent(BlockContainer parent) {
            //nop
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

        @Override
        public Object clone() {
            return new ButtonDel(line);
        }
    }

    public class ButtonActivable extends ButtonEx implements ActionListener, BlockInterface {

        BlockLine line;

        public ButtonActivable(BlockLine line) {
            super(new ResourceImageReference("/com/cachat/prj/echo3/blockeditor/delete.png"));
            this.line = line;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object x = line.getCurrent();
            if (x instanceof Activable) {
                final boolean actif = !((Activable) x).getActif();
                ((Activable) x).setActif(actif);
                setIcon(new ResourceImageReference(actif ? "/com/cachat/prj/echo3/blockeditor/add.png" : "/com/cachat/prj/echo3/blockeditor/delete.png"));
            }
        }

        @Override
        public void copyObjectToUi() {
            Object x = line.getCurrent();
            if (x instanceof Activable) {
                final boolean actif = ((Activable) x).getActif();
                setIcon(new ResourceImageReference(actif ? "/com/cachat/prj/echo3/blockeditor/add.png" : "/com/cachat/prj/echo3/blockeditor/delete.png"));
            }
        }

        @Override
        public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
            return false;
        }

        @Override
        public void setParent(BlockContainer parent) {
            //nop
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

        @Override
        public Object clone() {
            return new ButtonActivable(line);
        }
    }

    /**
     * ajoute un bloc. Cas particulier de la table, le bloc défin une colone, et
     * sera cloné pour chaque ligne.
     *
     * @param bf le blokc
     */
    @Override
    public final BlockInterface add(BlockInterface bf) {
        childs.add(bf);

        model.update();
        if (bf instanceof BlockBase) {//impossible
            throw new RuntimeException("BlockBase not valid as a child of BlockTable");
        } else if (bf instanceof BlockField) {
            Logger.getLogger("x").log(Level.FINER, "{0} add column {1}", new Object[]{BlockTable.this.hashCode(), ((BlockField) bf).property});
            for (Component c : ((List<Component>) ((BlockField) bf).getComponents())) {
                assert c != null : "Composant null pour " + bf.getClass().getSimpleName() + " / " + ((BlockField) bf).property;
            }
        }
        return bf;
    }

    /**
     * fabrique le tableau (est appelé à la demande, au premier getComponent)
     *
     * @return la table
     */
    protected TableEx buildTable() {
        TableEx tbl = new TableEx(model);
        tbl.setStyleName("BlockTable");
        tbl.setBorder(new Border(1, Color.BLACK, Border.STYLE_SOLID));
        tbl.setWidth(new Extent(100, Extent.PERCENT));
        tbl.setDefaultRenderer(Component.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
                if (value instanceof Component) {
                    Component c = (Component) value;
                    if (c1 != null && c2 != null) {
                        c.setBackground((row % 2 == 0) ? c1 : c2);
                    }
                    return c;
                } else {
                    return super.getTableCellRendererComponent(table, value, column, row);
                }
            }
        });
        return tbl;
    }

    /**
     * contient les données d'une ligne de tableau
     */
    public class BlockLine implements BlockContainer<T> {

        private final List<BlockInterface> lineChilds = new ArrayList<>();
        private final T current;
        private BlockContainer parent;

        public BlockLine(T current) {
            this.current = current;
            for (BlockInterface bf : childs) {
                BlockField x = (BlockField) ((BlockField) bf).clone();
                add(x);
            }
            switch (deleteMode) {
                case DEACTIVATE: {
                    ButtonActivable bb = new ButtonActivable(this);
                    add(bb);
                    editButtons.add(bb);
                }
                break;
                case DELETE: {
                    ButtonDel bb = new ButtonDel(this);
                    add(bb);
                    editButtons.add(bb);
                }
            }
        }

        @Override
        public final BlockInterface add(BlockInterface bf) {
            lineChilds.add(bf);
            bf.setParent(this);
            return bf;
        }

        @Override
        public void remove(BlockInterface bf) {
            lineChilds.remove(bf);
            bf.setParent(null);
        }

        @Override
        public T getCurrent() {
            return current;
        }

        @Override
        public Component getComponent() {
            return null;
        }

        public Component getComponent(int column) {
            BlockInterface x = lineChilds.get(column);
            if (x instanceof Component) {
                return (Component) x;
            }
            BlockField bf = (BlockField) x;
            Column col = new Column();
            col.add(bf.editor);
            col.add(bf.error);
            return col;
        }

        public BlockField getBlockInterface(String property) {
            if (property == null) {
                return null;
            }
            for (BlockInterface b : lineChilds) {
                if (b instanceof BlockField) {
                    BlockField bf = (BlockField) b;
                    if (property.equals(bf.getProperty())) {
                        return bf;
                    }
                }
            }
            return null;
        }

        @Override
        public void copyObjectToUi() {
            lineChilds.forEach((bi) -> bi.copyObjectToUi());
        }

        @Override
        public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
            boolean errors = false;
            for (BlockInterface bi : lineChilds) {
                errors |= bi.copyUiToObject(validator, genericErrors);
            }
            return errors;
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

        @Override
        public void setParent(BlockContainer parent) {
            this.parent = parent;
        }

        @Override
        public void setVisible(boolean visible) {
            //nop
        }

        @Override
        public void setEnabled(boolean enabled) {
            lineChilds.forEach((x) -> x.setEnabled(enabled));
        }

        @Override
        public Object clone() {
            return new BlockLine(current);
        }
    }

    @Override
    public void remove(BlockInterface bf) {
        childs.remove(bf);
    }

    @Override
    public List<T> getCurrent() {
        return current;
    }

    @Override
    public void copyObjectToUi() {
        rows.clear();
        if (current != null) {
            for (T t : current) {
                BlockLine bl;
                rows.add(bl = new BlockLine(t));
                bl.copyObjectToUi();
            }
        }
        model.update();
        rows.forEach((r) -> r.setEnabled(enabled));
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        boolean errors = false;
        error.setText("");

        for (BlockLine b : rows) {
            errors |= b.copyUiToObject(validator, genericErrors);
        }
        for (T t : removed) {
            current.remove(t);
        }
        removed.clear();
        for (T t : added) {
            current.add(t);
        }
        added.clear();
        return errors;
    }

    @Override
    public Column getComponent() {
        if (table == null) {
            column.add(table = buildTable());
        }
        return column;
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
        rows.forEach((r) -> setVisible(visible));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        rows.forEach((r) -> r.setEnabled(enabled));
        editButtons.forEach((a) -> a.setVisible(enabled));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new BlockTable(this);
    }

    private class EditTableModel extends AbstractTableModel {

        @Override
        public Class getColumnClass(int column) {
            return Component.class;
        }

        @Override
        public String getColumnName(int column) {
            return column < childs.size() ? ((BlockField) childs.get(column)).label.getText() : "";
        }

        @Override
        public int getColumnCount() {
            return childs.size() + (deleteMode != DeleteMode.NONE ? 1 : 0);
        }

        @Override
        public int getRowCount() {
            int res = (getCurrent() == null) ? 0 : rows.size();
            return res;
        }

        @Override
        public Object getValueAt(int column, int row) {
            Component x = rows.get(row).getComponent(column);
            if (x instanceof Component) {
                if (row < current.size() && removed.contains(current.get(row))) {
                    x.setBackground(Color.RED);
                    x.setEnabled(false);
                }
            }
            return x;
        }

        public void update() {
            fireTableDataChanged();
        }
    };

    /**
     * donnel le nombre de lignes
     *
     * @return le nombre de lignes
     */
    public int getRowCount() {
        return model.getRowCount();
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
        if (pp.equals(property)) {
            error.setText(msg);
            return true;
        }
        return rows.stream().anyMatch((c) -> c.appendError(pp, msg));
    }
}
