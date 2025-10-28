package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.ng.ContentPaneEx;
import nextapp.echo.app.WindowPane;

/**
 * le panneau principal
 */
public abstract class MainPane extends ContentPaneEx {

    public abstract void updateMenu();

    public abstract void windowsUpdated();

    public abstract void removeWindow(WindowPane w);

    /**
     * demande l'affichage d'une nouvelle fen�tre
     *
     * @param w la nouvelle fenêtre
     * @param parent la fenêtre parente, si il existe un lien de parenté type
     * liste-détail, ou null
     */
    public abstract void addWindow(WindowPane w, WindowPane parent);

    public abstract void clearWindows();

    /**
     * affiche un message fugitif en bas de page. peut ne pas être implémenté
     * (dans ce cas ne fait rien)
     *
     * @param message le message
     */
    public abstract void toast(String message);

    /**
     * affiche un message d'erreur fugitif en bas de page. peut ne pas être
     * implémenté (dans ce cas ne fait rien)
     *
     * @param message le message
     */
    public void toastError(String message) {
        toast(message);
    }

}
