package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.util.StringUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.validation.Validator;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;

/**
 * un bloc affichant une propriete en lecture seule
 *
 * @author scachat
 */
public class BlockComponentContainer<T extends Component> extends BlockField<T> {

    Class<T> clazz;

    public BlockComponentContainer(BlockField x) {
        super(x);
        try {
            editor = clazz.getConstructor().newInstance();
        } catch (InvocationTargetException|NoSuchMethodException|InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BlockComponentContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Constructeur
     *
     * @param c le composant
     */
    public BlockComponentContainer(LocalisedItem localisedItem, String property, T c) {
        super(localisedItem, property);
        editor = c;
    }

    @Override
    public void copyObjectToUi() {
      
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        return false;
    }

}
