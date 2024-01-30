package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.prj.echo3.ng.ButtonEx;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import jakarta.validation.Validator;
import nextapp.echo.app.ImageReference;

/**
 * un bloc affichant un bouton. Les button listeners est appelé quand on clique
 * sur le bouton.
 *
 * @author scachat
 */
public class AbstractBlockButton<T> extends BlockField<ButtonEx> {

    /**
     * l'action a réaliser
     */
    private Consumer<T> consumer;

    public static interface ButtonListener<T> {

        public void buttonPressed(T o);
    }
    private String buttonLabel;
    private ImageReference icon;
    private List<ButtonListener> listeners = new ArrayList<>();

    public AbstractBlockButton(BlockField x) {
        super(x);

        buttonLabel = ((AbstractBlockButton) x).buttonLabel;
        icon = ((AbstractBlockButton) x).icon;
        listeners = ((AbstractBlockButton) x).listeners;
        consumer = ((AbstractBlockButton) x).consumer;
        editor = new ButtonEx(buttonLabel, icon);
        editor.addActionListener((a) -> buttonPressed());
        editor.setStyleName("Button");
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param buttonLabel le nom du bouton
     * @param icon l'icone du bouton
     * @param consumer l'action a réaliser
     */
    public AbstractBlockButton(LocalisedItem li, String buttonLabel, ImageReference icon, Consumer<T> consumer) {
        this(li, buttonLabel, icon);
        this.consumer = consumer;
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param buttonLabel le nom du bouton
     * @param icon l'icone du bouton
     */
    public AbstractBlockButton(LocalisedItem li, String buttonLabel, ImageReference icon) {
        super(li, "");
        this.buttonLabel = buttonLabel;
        this.icon = icon;
        editor = new ButtonEx(buttonLabel, icon);
        editor.addActionListener((a) -> buttonPressed());
        editor.setStyleName("Button");
    }

    @Override
    public void copyObjectToUi() {
        //nop
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        return false;
    }

    private void buttonPressed() {
        listeners.stream().forEach((a) -> a.buttonPressed(getParent().getCurrent()));
        if (consumer != null) {
            consumer.accept((T) getParent().getCurrent());
        }
    }

    /**
     * ajoute un listener pour l'appui sur le bouton correspondant
     *
     * @param bl le listener
     */
    public final void addListener(ButtonListener<T> bl) {
        listeners.add(bl);
    }

    /**
     * enlève un listener pour l'appui sur le bouton correspondant
     *
     * @param bl le listener
     */
    public void removeListener(ButtonListener<T> bl) {
        listeners.remove(bl);
    }

    /**
     * donne le bouton
     *
     * @return le bouton
     */
    public ButtonEx getButton() {
        return editor;
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
