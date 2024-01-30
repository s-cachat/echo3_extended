package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.validation.Validator;
import nextapp.echo.app.Component;
import nextapp.echo.app.WindowPane;

/**
 * un bloc affichant un composant
 *
 * @param <T> le composant. Si il a une méthode setCurrent(o), elle sera appelé
 * avec l'objet en cours d'édition. Si il a une méthode setParentPane, et que le
 * localizedItem est une windowPane, elle est appelée avec ce li. Le composant
 * doit avoir un constructeur sans arguments composant
 * @author scachat
 */
public class BlockComponent<T extends Component> extends BlockField<T> {

    private Class<T> clazz;

    public BlockComponent(BlockField x) {
        super(x);
        clazz = ((BlockComponent) x).clazz;
        try {
            editor = clazz.getConstructor().newInstance();
            LocalisedItem li = getLocalisedItem();
            if (li instanceof WindowPane windowPane) {
                BeanTools.setRaw(editor, "parentPane", windowPane);
            }
        } catch (IllegalArgumentException|SecurityException |InvocationTargetException| NoSuchMethodException | InstantiationException | IllegalAccessException | NoSuchElementException | ParseException ex) {
            Logger.getLogger(BlockComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property pour le nom du champ
     * @param clazz le composant
     */
    public BlockComponent(String property, LocalisedItem li, Class<T> clazz) {
        super(li, property);
        this.clazz = clazz;
        try {
            editor = clazz.getConstructor().newInstance();
            if (li instanceof WindowPane) {
                BeanTools.setRaw(editor, "parentPane", (WindowPane) li);
            }
        } catch (NoSuchMethodException|InvocationTargetException|InstantiationException | IllegalAccessException | NoSuchElementException | ParseException ex) {
            Logger.getLogger(BlockComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void copyObjectToUi() {
        if (BeanTools.canWrite(editor, "current")) {
            try {
                final Object current = getParent().getCurrent();
                BeanTools.setRaw(editor, "current", current);
            } catch (NoSuchElementException | ParseException ex) {
                Logger.getLogger(BlockComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        return false;
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
