package com.cachat.prj.echo3.blockeditor;

import static com.cachat.prj.echo3.base.BasicWindow.FULL_WIDTH;
import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.layout.GridLayoutData;

/**
 * Un panel pour un formulaire
 *
 * @author scachat
 * @param <T> le type d'objet manipulé dans ce sous formulaire
 */
public class BlockFlexGroup<T> extends BlockPanel<T> {

    private ContainerEx container;
    private String title;
    private String basis;

    public BlockFlexGroup(BlockPanel<T> bp) throws CloneNotSupportedException {
        super(bp);
    }

    public BlockFlexGroup(LocalisedItem li, T current, Extent... columnWidth) {
        super(li, current, columnWidth);
    }

    public BlockFlexGroup(LocalisedItem li, T current, int columnCount, Extent... columnWidth) {
        super(li, current, columnCount, columnWidth);
    }

    /**
     * Constructeur
     *
     * @param li pour les traductions
     * @param current l'objet a éditer
     * @param title le titre de ce bloc
     * @param basis la base flex, en pixel
     */
    public BlockFlexGroup(LocalisedItem li, T current, String title, int basis) {
        super(li, current);
        this.title = title;
        this.basis = String.format("%dpx", basis);
    }

    /**
     * Constructeur. La base flex sera auto
     *
     * @param li pour les traductions
     * @param current l'objet a éditer
     * @param title le titre de ce bloc
     *
     */
    public BlockFlexGroup(LocalisedItem li, T current, String title) {
        super(li, current);
        this.title = title;
        this.basis = "auto";
    }

    public BlockFlexGroup(LocalisedItem li, T current) {
        super(li, current);
    }

    public BlockFlexGroup(LocalisedItem li, T current, int columnCount) {
        super(li, current, columnCount);
    }

    public BlockFlexGroup(LocalisedItem li, String property, Extent labelWidth) {
        super(li, property, labelWidth);
    }

    public BlockFlexGroup(LocalisedItem li, String property) {
        super(li, property);
    }

    @Override
    public Component getComponent() {
        if (container == null) {
            ContainerEx ca = new ContainerEx(grid);
            container = new ContainerEx(ca);
            container.setFlexBasis(basis);
            container.setFlexDirection("column");
            container.setFlexGrow(1.0);
            container.setFlexShrink(0.0);
            container.setInsets(new Insets(16, 16));
            grid.setBackground(Color.TRANSPARENT);
            ca.setInsets(new Insets(16, 16));

            ca.setHeight(FULL_WIDTH);
            ca.setRadius(new Insets(4, 4));
            ca.setShadow("0px 3px 1px -2px rgb(0 0 0 / 20%), 0px 2px 2px 0px rgb(0 0 0 / 14%), 0px 1px 5px 0px rgb(0 0 0 / 12%)");
            if (title != null) {
                LabelEx lt = new LabelEx(title);
                lt.setStyleName("flexTitle");
                grid.add(lt, 0);
                lt.setLayoutData(new GridLayoutData(grid.getSize(), 1));
            }
        }
        return container;
    }
}
