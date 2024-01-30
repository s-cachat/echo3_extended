package com.cachat.prj.echo3.ng;

import nextapp.echo.app.Component;
import java.util.List;
import java.util.ArrayList;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import java.util.logging.Logger;

/**
 * un composant gérant l'historique du navigateur
 *
 * @author scachat
 */
public class BackButton extends Component {

    public static final String ACTION_EVENT = "action";
    /**
     * les listeners pour le bouton back
     */
    private List<ActionListener> backListener = new ArrayList<>();
    /**
     * les listeners pour le bouton forward
     */
    private List<ActionListener> forwardListener = new ArrayList<>();

    public BackButton() {
    }

    /**
     * Ajoute un listener pour le bouton back du navigateur
     *
     * @param al le listener
     */
    public void addBackListener(ActionListener al) {
        backListener.add(al);
    }

    /**
     * Supprime un listener pour le bouton back du navigateur
     *
     * @param al le listener
     */
    public void removeBackListener(ActionListener al) {
        backListener.remove(al);
    }

    /**
     * Ajoute un listener pour le bouton forward du navigateur
     *
     * @param al le listener
     */
    public void addForwardListener(ActionListener al) {
        forwardListener.add(al);
    }

    /**
     * Supprime un listener pour le bouton forward du navigateur
     *
     * @param al le listener
     */
    public void removeForwardListener(ActionListener al) {
        forwardListener.remove(al);
    }

    public void setAction(String action) {
        Logger.getLogger(getClass().getSimpleName()).severe("BackButton Action " + action);
        switch (action) {
            case "forward": {
                ActionEvent ae = new ActionEvent(this, action);
                forwardListener.forEach(a -> a.actionPerformed(ae));
            }
            case "back": {
                ActionEvent ae = new ActionEvent(this, action);
                backListener.forEach(a -> a.actionPerformed(ae));
            }
        }
    }
}
