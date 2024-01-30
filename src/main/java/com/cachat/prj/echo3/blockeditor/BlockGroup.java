package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.ng.GroupBox;
import com.cachat.prj.echo3.ng.LabelEx;
import nextapp.echo.app.Component;
import nextapp.echo.app.ImageReference;

/**
 * défini un sous formulaire pour un sous objet
 *
 * @param <T> le type d'objet manipulé dans ce sous formulaire
 * @author scachat
 */
public class BlockGroup<T> extends BlockPanel<T> {

    GroupBox es;

    public BlockGroup(LocalisedItem li, String property) {
        super(li, property);
        es = new GroupBox(super.getComponent(), li.getString(property));
    }

    public BlockGroup(LocalisedItem li, T current, String title, int columnmCount) {
        super(li, current, columnmCount);
        es = new GroupBox(super.getComponent(), title);
    }

    public BlockGroup(LocalisedItem li, T current, String title) {
        super(li, current);
        es = new GroupBox(super.getComponent(), title);
    }

    public BlockGroup(LocalisedItem li, T current, String title, ImageReference icon) {
        super(li, current);
        LabelEx tb = new LabelEx(title, icon);
        es = new GroupBox(tb);
        es.add(super.getComponent());
    }

    @Override
    public Component getComponent() {
        return es;
    }

    @Override
    public void setVisible(boolean visible) {
        es.setVisible(visible);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
