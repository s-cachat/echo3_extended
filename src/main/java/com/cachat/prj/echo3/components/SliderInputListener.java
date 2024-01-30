package com.cachat.prj.echo3.components;

import java.io.Serializable;
import java.util.EventListener;
import nextapp.echo.app.event.ActionEvent;

/**
 * Interface pour un evenement de type 'onchange' d'un Slider
 * 
 * @author user1
 */
public interface SliderInputListener extends EventListener, Serializable {

    /**
     * Action lorsque l'utilisateur bouge le curseur du Slider
     * 
     * @param event l'évenement qui déclenché l'action
     */
    public void onInput(ActionEvent event);
}