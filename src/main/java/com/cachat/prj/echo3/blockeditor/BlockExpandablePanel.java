package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import de.exxcellent.echolot.app.Expander;
import com.cachat.prj.echo3.ng.LabelEx;
import java.util.List;
import jakarta.validation.Validator;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 *
 * @author scachat
 */
public class BlockExpandablePanel<T> extends BlockPanel<T> {

    private final Column col;
    Expander es;
    private LabelEx error;
    private Button info;
    private Button main;

    public BlockExpandablePanel(LocalisedItem li, T current, String title) {
        this(li, current, title, null);
    }

    public BlockExpandablePanel(LocalisedItem li, T current, String title, ImageReference icon) {
        super(li, current);
        col = new Column();
        col.add(buildTitle(title, icon));
        es = new Expander();
        col.add(es);
        es.setShowText("");
        es.setHideText("");
        es.setHeaderHide(true);
        es.setContent(super.getComponent());
    }

    @Override
    public Component getComponent() {
        return col;
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        boolean res = super.copyUiToObject(validator, genericErrors);
        if (res) {
            error.setText(li.getBaseString("gotError"));
        } else {
            error.setText("");
        }
        return res;
    }

    public void setIcon(ImageReference icon) {
        main.setIcon(icon);
    }

    @Override
    public void setVisible(boolean visible) {
        es.setVisible(visible);
    }

    private Row buildTitle(String title, ImageReference icon) {
        Row r = new Row();
        r.setStyleName("ExpandTitle");
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                es.setShow(!es.isShow());
            }
        };
        if (icon == null) {
            r.add(main = new Button(title));
        } else {
            r.add(main = new Button(title, icon));
        }
        main.addActionListener(al);
//TODO HTML5 et enlever ci dessous r.add(new Strut(5, 5));
        r.add(new Label(" "));
        r.add(error = new LabelEx());
        error.setStyleName("ErrorMsg");
        r.add(info = new Button());
        info.setStyleName("bigComment");
        info.addActionListener(al);
        return r;
    }

    public void setInfo(String s) {
        info.setText(s);
    }

    public void setExpanded(boolean expanded) {
        es.setShow(expanded);
    }
}
