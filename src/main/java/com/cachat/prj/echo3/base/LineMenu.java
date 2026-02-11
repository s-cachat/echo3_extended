package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.ng.MenuItem;
import java.util.function.Function;
import nextapp.echo.app.Button;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * une action qui sera proposée sur chacune des lignes d'une basicList
 *
 * @author user1
 */
public class LineMenu extends MenuItem {

    /**
     * l'application
     */
    protected BaseApp app;
    /**
     * l'icone demandé initialement
     */
    protected ImageReference iconImage;
    /**
     * le label demandé initialement
     */
    protected String label;

    /**
     * Constructeur sans argument : lors de la surcharge, il faut absolument
     * renseigner list, iconImage, label et action
     */
    protected LineMenu() {
        super();
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param iconImage l'icone
     * @param label le libellé
     * @param al l'action a exécuter
     */
    public LineMenu(BaseApp app, ImageReference iconImage, String label, ActionListener al) {
        super();
        this.app = app;
        this.iconImage = iconImage;
        this.label = label;
        if (al != null) {
            addActionListener(al);
        }
        update();
    }

    public void addAction(Function<ActionEvent, Boolean> action) {
        if (action != null) {
            addActionListener((e) -> action.apply(e));
        }
    }

    protected void update() {
        checkVersion();
    }

    @Override
    public void setIcon(ImageReference icon) {
        super.setIcon(icon);
        this.iconImage = icon;
    }

    protected void checkVersion() {
        if (app == null) {
            return;
        }
        setText(label);
        setIcon(iconImage);
    }

    public BaseApp getApp() {
        return app;
    }
}
