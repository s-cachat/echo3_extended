/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.components.ButtonEx2;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.Strut;
import jakarta.validation.Validator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import static nextapp.echo.app.Position.STATIC;
import nextapp.echo.app.Row;
import nextapp.echo.app.layout.GridLayoutData;

/**
 *
 * @author scachat
 */
public class BlockComponentContainerFullwidth implements BlockBase<Component>, BlockInterface {

    /**
     * le conteneur de composant
     */
    protected final ContainerEx compContainer;
    /**
     * le composant
     */
    protected Component comp;

    /**
     * ajout un container de composant
     *
     * @param comp le composant
     */
    public BlockComponentContainerFullwidth(Component comp) {
        compContainer = new ContainerEx(0, null, 0, 0, null, null);
        compContainer.setPosition(STATIC);
        compContainer.add(comp);
        this.comp = comp;
    }

    /**
     * change le composant
     *
     * @param comp le composant
     */
    public void setComponent(Component comp) {
        compContainer.clear();
        compContainer.add(comp);
        this.comp = comp;
    }

    @Override
    public Component getComponent() {
        return compContainer;
    }

    @Override
    public void copyObjectToUi() {
        //nop
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors) {
        //nop
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

    @Override
    public void setParent(BlockContainer parent) {
        //nop
    }

    @Override
    public void setVisible(boolean visible) {
        compContainer.setVisible(visible);
    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    /**
     * change uniquement la possibilité d'appuyer sur ok
     */
    public void setOkEnabled(boolean enabled) {
    }

    @Override
    public Object clone() {
        try {
            Method cloneMethod = comp.getClass().getMethod("clone");
            return new BlockComponentContainerFullwidth((Component) cloneMethod.invoke(comp));
        } catch (NoSuchMethodException ex) {
            return new BlockComponentContainerFullwidth(comp);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
