package com.cachat.prj.echo3.base;

import java.util.function.Function;
import nextapp.echo.app.Button;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.event.ActionEvent;

/**
 * une action qui sera proposée sur chacune des lignes d'une basicList
 *
 * @author user1
 */
public class LineAction extends Button {

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
    protected LineAction() {
        super();
        setStyleName("BasicListItemButton");
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param iconImage l'icone
     * @param label le libellé
     * @param action l'action a exécuter
     */
    public LineAction(BaseApp app, ImageReference iconImage, String label, Function<ActionEvent, Boolean> action) {
        super();
        this.app = app;
        this.iconImage = iconImage;
        this.label = label;
        if (action != null) {
            addActionListener((e) -> action.apply(e));
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
        if (iconImage != null) {
            switch (app.getInterfaceVersion()) {
                case TAB_V4:
                case WEB_V5:

                    setText(null);
                    setIcon(iconImage);
                    break;
                case WEB_V6:
                    setText(label);
                    setIcon(iconImage);
                    break;
            }
        } else {
            setText(label);
        }
    }

    public BaseApp getApp() {
        return app;
    }
}
