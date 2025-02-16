package com.cachat.prj.echo3.components;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.ng.ButtonGroupEx;
import com.cachat.prj.echo3.ng.ToggleButtonEx;
import com.cachat.util.ACEntityManager;
import com.cachat.util.BeanTools;
import com.cachat.util.EntityManagerUtil;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolationException;
import nextapp.echo.app.Row;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * Change à la volée l'état d'un objet (défini par une enum)
 *
 * @param <T> le type de l'objet
 * @param <V> le type de la valeur
 * @author scachat
 */
public class EnumLiveSelect<T, V extends Enum> extends Row {

    private V selectedValue;

    /**
     * Constructeur
     *
     * @param app l'application
     * @param o l'objet principal
     * @param enumClass le type de la propriété
     * @param property le nom de la propriété
     * @param unselectedStyle le style désélectionné
     * @param selectedStyle le style sélectionné
     */
    public EnumLiveSelect(BaseApp app, T o, Class<V> enumClass, String property, String unselectedStyle, String selectedStyle) {
        this(app, o, enumClass, property, null, unselectedStyle, selectedStyle);
    }

    /**
     * Constructeur (si on ne veut pas proposer toutes les valeurs de l'enum)
     *
     * @param app l'application
     * @param o l'objet principal
     * @param enumClass le type de la propriété
     * @param property le nom de la propriété
     * @param saveAction l'action pour l'enregistrement
     * @param unselectedStyle le style désélectionné
     * @param selectedStyle le style sélectionné
     */
    public EnumLiveSelect(BaseApp app, T o, Class<V> enumClass, String property, SaveAction<T> saveAction, String unselectedStyle, String selectedStyle) {
        this(app, o, enumClass, enumClass.getEnumConstants(), property, saveAction, unselectedStyle, selectedStyle);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param o l'objet principal
     * @param enumClass le type de la propriété
     * @param enumConstants les valeurs de l'enum a présenter
     * @param property le nom de la propriété
     * @param saveAction l'action pour l'enregistrement 
     * @param unselectedStyle le style désélectionné
     * @param selectedStyle le style sélectionné
     */
    public EnumLiveSelect(BaseApp app, T o, Class<V> enumClass, V[] enumConstants, String property, SaveAction<T> saveAction, String unselectedStyle, String selectedStyle) {
        ButtonGroupEx<V> bge = new ButtonGroupEx<>();
        ActionListener l = (ActionEvent e) -> {
            final ToggleButtonEx tbe = (ToggleButtonEx) e.getSource();
            bge.updateSelection(tbe);
            selectedValue = bge.getSelected();
            try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())) {
                T oo = (T) em.find(o.getClass(), EntityManagerUtil.getId(o));
                BeanTools.setRaw(oo, property, selectedValue);
                if (saveAction != null) {
                    saveAction.save(em, oo);
                } else {
                    em.getTransaction().begin();
                    em.merge(oo);
                    em.getTransaction().commit();
                }
            } catch (RollbackException ex) {
                Throwable ex2 = ex.getCause();
                if (ex2 != null && ex2 instanceof ConstraintViolationException cve) {
                    app.toastError(cve.getConstraintViolations().iterator().next().getMessageTemplate());
                    tbe.setSelected(false);
                } else {
                    throw ex;
                }
            } catch (ConstraintViolationException ex) {
                app.toastError(ex.getMessage());
            } catch (NoSuchElementException | ParseException ex) {
                Logger.getLogger(EnumLiveSelect.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())) {
            T oo = (T) em.find(o.getClass(), EntityManagerUtil.getId(o));
            selectedValue = (V) BeanTools.getRaw(oo, property);
            for (V c : enumConstants) {
                ToggleButtonEx tbe = new ToggleButtonEx(app.getString(enumClass.getSimpleName() + "." + c.name()), unselectedStyle, selectedStyle);
                add(tbe);
                tbe.addActionListener(l);
                tbe.setSelected(c == selectedValue);
                bge.addButton(tbe, c);
            }
        }
    }

    /**
     * donne la valeur sélectionnée
     *
     * @return la valeur
     */
    public V getSelectedValue() {
        return selectedValue;
    }

    /**
     * Action d'enregistrement. Si non précisée, un enregistrement simple est
     * fait
     */
    public static interface SaveAction<T> {

        public void save(EntityManager em, T object);
    }
}
