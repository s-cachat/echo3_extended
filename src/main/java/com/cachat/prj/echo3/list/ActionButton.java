package com.cachat.prj.echo3.list;

import com.cachat.prj.echo3.ng.ButtonEx;
import java.util.function.Consumer;
import java.util.function.Supplier;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.event.ActionListener;

/**
 * un bouton configurable par une lambda
 *
 * @author scachat
 */
public class ActionButton extends ButtonEx {

    /**
     * constructeur
     *
     * @param text le texte du bouton
     * @param consumer l'action a lancer quand on sollicite ce bouton
     */
    public ActionButton(String text, ActionListener consumer) {
        super(text);
        addActionListener(consumer);
        setStyleName("Button");
    }

    /**
     * constructeur
     *
     * @param text le texte du bouton
     * @param consumer l'action a lancer quand on sollicite ce bouton
     */
    public ActionButton(String text, Supplier consumer) {
        super(text);
        addActionListener((e) -> consumer.get());
        setStyleName("Button");
    }

    /**
     * constructeur
     *
     * @param text le texte du bouton
     * @param consumer l'action a lancer quand on sollicite ce bouton
     */
    public ActionButton(String text, Runnable consumer) {
        this(text, consumer, "Button");
    }

    /**
     * constructeur
     *
     * @param text le texte du bouton
     * @param consumer l'action a lancer quand on sollicite ce bouton
     * @param styleName le nom du style du bouton
     */
    public ActionButton(String text, Runnable consumer, String styleName) {
        this(text, null, consumer, styleName);
    }

    /**
     * constructeur
     *
     * @param img l'image
     * @param text le texte du bouton
     * @param consumer l'action a lancer quand on sollicite ce bouton
     */
    public ActionButton(String text, ImageReference img, Runnable consumer) {
        this(text,img,consumer,"Button");
    }
    /**
     * constructeur
     *
     * @param img l'image
     * @param text le texte du bouton
     * @param consumer l'action a lancer quand on sollicite ce bouton
     * @param styleName le nom du style du bouton
     */
    public ActionButton(String text, ImageReference img, Runnable consumer, String styleName) {
        super(text, img);
        addActionListener((e) -> consumer.run());
        setStyleName(styleName);
    }

    public ActionButton(ImageReference img, Runnable consumer) {
        super(img);
        addActionListener((e) -> consumer.run());
    }

    public ActionButton(ImageReference img, ActionListener consumer) {
        super(img);
        addActionListener(consumer);
    }

    public ActionButton(ImageReference img, Supplier consumer) {
        super(img);
        addActionListener((e) -> consumer.get());
    }

}
