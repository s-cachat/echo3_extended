package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.EntityModifiedListener;
import com.cachat.prj.echo3.components.ButtonEx2;
import com.cachat.prj.echo3.ng.ButtonEx;
import java.util.function.Consumer;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.event.ActionEvent;

/**
 * Surcharge de l'éditeur pour avoir un bouton d'enregistrement sans fermeture
 * de la fenêtre
 *
 * @param <T> le type d'objet à modifier
 */
public abstract class BlockEditor2<T> extends BlockEditor<T> {

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le domaine de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param list la liste (a mettre a jour en fin d'edition)
     */
    public BlockEditor2(BaseApp app, String prefixe, String domaine, Extent w, Extent h, EntityModifiedListener list) {
        super(app, prefixe, domaine, w, h, list);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le domaine de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     */
    public BlockEditor2(BaseApp app, String prefixe, String domaine, Extent w, Extent h) {
        super(app, prefixe, domaine, w, h);
    }
    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le domaine de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     */
    public BlockEditor2(BaseApp app, String prefixe, String domaine, int w, int h) {
        super(app, prefixe, domaine, w, h);
    }
    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le domaine de la fenetre
     */
    public BlockEditor2(BaseApp app, String prefixe, String domaine) {
        super(app, prefixe, domaine);
    }

    protected class BlockButton extends BlockEditor.BlockButton {

        /**
         * bouton enregistrer simple
         */
        protected final ButtonEx2 save;

        /**
         * ajoute une zone de bouton
         */
        public BlockButton() {
            if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6) {
                save = new ButtonEx2(getBaseString("save"));
                save.setStyleName("BlockEditorButtons");
                save.addActionListener(e -> {
                    T current = (T) getCurrent();
                    if (save(false)) {
                        edite(current);
                        if (list != null) {
                            list.update(current);
                        }
                        app.toast(getBaseString("saveOkMessage"));
                    }
                });
                addButtonRight(save);
                cancel.setIcon(null);
                ok.setIcon(null);
            } else {
                save = new ButtonEx2(getBaseString("save"));
                save.setStyleName("Button");
                save.addActionListener(e -> {
                    T current = (T) getCurrent();
                    if (save(false)) {
                        edite(current);
                        if (list != null) {
                            list.update(current);
                        }
                        app.toast(getBaseString("saveOkMessage"));
                    }
                });
                butRow.remove(cancel);
                addButton(save);
                addButton(cancel);
                cancel.setIcon(null);
                ok.setIcon(null);
            }
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            save.setEnabled(enabled);
            cancel.setEnabled(true);

        }

        /**
         * change uniquement la possibilité d'appuyer sur ok
         */
        public void setOkEnabled(boolean enabled) {
            ok.setEnabled(enabled);
            save.setEnabled(enabled);
        }

        @Override
        public Object clone() {
            return new BlockButton();
        }

        public void cancelOnly() {
            ok.setVisible(false);
            save.setVisible(false);
        }
    }

    /**
     * un bouton d'action
     */
    public class BasicButton extends ButtonEx {

        /**
         * Constructeur
         *
         * @param labelKey la clé pour le libellé
         * @param a l'action a réaliser
         */
        public BasicButton(String labelKey, Consumer<ActionEvent> a) {
            super(getString(labelKey));
            setStyleName("Button");
            addActionListener((x) -> a.accept(x));
        }

        /**
         * Constructeur
         *
         * @param icon le nom de l'icone
         * @param a l'action a réaliser
         */
        public BasicButton(ImageReference icon, Consumer<ActionEvent> a) {
            super(icon);
            addActionListener((x) -> a.accept(x));
        }
    }
}
