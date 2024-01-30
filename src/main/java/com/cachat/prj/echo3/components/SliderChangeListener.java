package com.cachat.prj.echo3.components;

import java.io.Serializable;
import java.util.EventListener;
import nextapp.echo.app.event.ActionEvent;

/**
 * Interface pour un evenement de type 'onchange' d'un Slider
 * 
 * @author user1
 */
public interface SliderChangeListener extends EventListener, Serializable {

    /**
     * Action lorsque l'utilisateur a relaché le curseur
     * 
     * @param event l'évenement qui déclenché l'action
     */
    public void onChange(ActionEvent event);
}
