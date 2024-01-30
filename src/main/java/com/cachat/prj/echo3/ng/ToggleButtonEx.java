package com.cachat.prj.echo3.ng;

import nextapp.echo.app.ImageReference;
import nextapp.echo.app.button.DefaultToggleButtonModel;

/**
 * a "Toggle button" with a selected state. styleName and selectedStyleName must
 * be defined !
 *
 * @author scachat
 */
public class ToggleButtonEx extends ButtonEx {

    /**
     * le style pour un bouton non pressé
     */
    private String unselectedStyleName;
    /**
     * le style pour un bouton pressé
     */
    private String selectedStyleName;
    /**
     * le groupe de bouton
     */
    private ButtonGroupEx buttonGroup;
    /**
     * l'état
     */
    private boolean selected = false;

    /**
     * Creates a button with no text or icon.
     */
    public ToggleButtonEx() {
        this(null, null, null, null);
    }

    /**
     * Creates a button with text.
     *
     * @param text the text to be displayed in the button
     */
    public ToggleButtonEx(String text) {
        this(text, null, null, null);
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon the icon to be displayed in the button
     */
    public ToggleButtonEx(ImageReference icon) {
        this(null, icon, null, null);
    }

    /**
     * Creates a button with text and an icon.
     *
     * @param text the text to be displayed in the button
     * @param icon the icon to be displayed in the button
     */
    public ToggleButtonEx(String text, ImageReference icon) {
        this(text, icon, null, null);
    }

    /**
     * Creates a button with text and an icon.
     *
     * @param selectedStyleName le style pour le bouton sélectionné
     * @param unSelectedStyleName le style pour le bouton non sélectionné
     * @param text the text to be displayed in the button
     */
    public ToggleButtonEx(String text, String unSelectedStyleName, String selectedStyleName) {
        this(text, null, unSelectedStyleName, selectedStyleName);
    }

    /**
     * Creates a button with text and an icon.
     *
     * @param selectedStyleName le style pour le bouton sélectionné
     * @param unSelectedStyleName le style pour le bouton non sélectionné
     * @param text the text to be displayed in the button
     * @param icon the icon to be displayed in the button
     */
    public ToggleButtonEx(String text, ImageReference icon, String unSelectedStyleName, String selectedStyleName) {
        super();
        setModel(new DefaultToggleButtonModel());
        setIcon(icon);
        setText(text);
        setUnselectedStyleName(unSelectedStyleName);
        setSelectedStyleName(selectedStyleName);
        addActionListener(e -> {
            if (buttonGroup != null) {
                setSelected(true);
            } else {
                setSelected(!isSelected());
            }
        });
        setStyleName(unSelectedStyleName);
    }

    public String getUnselectedStyleName() {
        return unselectedStyleName;
    }

    public void setUnselectedStyleName(String unselectedStyleName) {
        this.unselectedStyleName = unselectedStyleName;
    }

    public String getSelectedStyleName() {
        return selectedStyleName;
    }

    public void setSelectedStyleName(String selectedStyleName) {
        this.selectedStyleName = selectedStyleName;
    }

    /**
     * Retrieves the <code>ButtonGroup</code> to which this
     * <code>RadioButton</code> belongs. Only one radio button in a group may be
     * selected at a time.
     *
     * @return the button group
     */
    public ButtonGroupEx getGroup() {
        return buttonGroup;
    }

    /**
     * Sets the <code>ButtonGroup</code> to which this <code>RadioButton</code>
     * belongs. Only one radio button in a group may be selected at a time.
     *
     * @param newValue the new button group
     */
    public void setGroup(ButtonGroupEx newValue) {
        if (buttonGroup != null) {
            buttonGroup.removeButton(this);
        }
        if (buttonGroup != newValue && buttonGroup != null) {
            buttonGroup.addButton(this);
        }
        buttonGroup = newValue;

    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        setStyleName(selected ? selectedStyleName : unselectedStyleName);
    }

    public boolean isSelected() {
        return selected;
    }
}
