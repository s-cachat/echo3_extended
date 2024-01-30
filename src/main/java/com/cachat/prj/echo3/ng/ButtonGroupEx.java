package com.cachat.prj.echo3.ng;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import nextapp.echo.app.ApplicationInstance;

/**
 * un groupe de bouton plus générique. Attention, la méthode getButton ne
 * renvoie rien ! il faut utiliser getToggleButton
 *
 * @param <T> le type d'obejt associé à chaque bouton (optionnel)
 * @author scachat
 */
public class ButtonGroupEx<T> {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 20070121L;

    private static final ToggleButtonEx[] EMPTY = new ToggleButtonEx[0];

    private String id = ApplicationInstance.generateSystemId();
    private Set buttons;
    /**
     * la map bouton vers valeur
     */
    private Map<ToggleButtonEx, T> values = new HashMap<>();
    /**
     * la valeur sélectionnée
     */
    private T selected = null;

    /**
     * Adds a <code>ToggleButton</code> to the group. Applications should use
     * <code>ToggleButton.setGroup()</code> to add buttons from a group rather
     * than invoking this method.
     *
     * @param toggleButton the <code>ToggleButton</code> to add
     * @see ToggleButton#setGroup(ButtonGroup)
     * @return the button
     */
    public ToggleButtonEx addButton(ToggleButtonEx toggleButton) {
        return addButton(toggleButton, null);
    }

    /**
     * Adds a <code>ToggleButton</code> to the group. Applications should use
     * <code>ToggleButton.setGroup()</code> to add buttons from a group rather
     * than invoking this method.
     *
     * @param toggleButton the <code>ToggleButton</code> to add
     * @param value the value associated to this button
     * @see ToggleButton#setGroup(ButtonGroup)
     * @return the button
     */
    public ToggleButtonEx addButton(ToggleButtonEx toggleButton, T value) {
        if (buttons == null) {
            buttons = new HashSet();
        }
        if (toggleButton.getGroup() != this) {
            toggleButton.setGroup(this);
        }
        buttons.add(toggleButton);
        values.put(toggleButton, value);
        updateSelection(toggleButton);
        toggleButton.addActionListener(e -> updateSelection(toggleButton));
        return toggleButton;
    }

    /**
     * Returns all <code>ToggleButton</code>s in the group.
     *
     * @return the <code>ToggleButton</code>
     */
    public ToggleButtonEx[] getToggleButtons() {
        if (buttons == null) {
            return EMPTY;
        } else {
            return (ToggleButtonEx[]) buttons.toArray(new ToggleButtonEx[buttons.size()]);
        }
    }

    /**
     * @see nextapp.echo.app.RenderIdSupport#getRenderId()
     */
    public String getRenderId() {
        return id;
    }

    /**
     * Removes a <code>ToggleButton</code> from the group. Applications should
     * use <code>ToggleButton.setGroup()</code> to remove buttons from a group
     * rather than invoking this method.
     *
     * @param toggleButton the <code>ToggleButton</code> to remove
     * @see ToggleButton#setGroup(ButtonGroup)
     */
    public void removeButton(ToggleButtonEx toggleButton) {
        if (buttons != null) {
            buttons.remove(toggleButton);
        }
    }

    /**
     * Notifies the <code>ButtonGroup</code> that a <code>ToggleButton</code>
     * within its domain may have changed state.
     *
     * @param changedButton the changed <code>ToggleButton</code>
     */
    public void updateSelection(ToggleButtonEx changedButton) {
        if (buttons == null || !changedButton.isSelected()) {
            return;
        }

        Iterator buttonIt = buttons.iterator();
        while (buttonIt.hasNext()) {
            ToggleButtonEx button = (ToggleButtonEx) buttonIt.next();
            if (!button.equals(changedButton)) {
                button.setSelected(false);
            } else {
                selected = values.get(button);
            }
        }
    }

    /**
     * la valeur sélectionnée
     *
     * @return la valeur
     */
    public T getSelected() {
        return selected;
    }

    /**
     * sélectionne une valeur
     */
    public void setSelected(T selected) {
        values.forEach((b, v) -> {
            if (v == selected) {
                b.setSelected(true);
            }
        });
    }
}
