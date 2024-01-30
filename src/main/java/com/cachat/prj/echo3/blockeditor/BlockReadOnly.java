package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.util.StringUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import jakarta.validation.Validator;
import nextapp.echo.app.Color;

/**
 * un bloc affichant une propriete en lecture seule
 *
 * @author scachat
 */
public class BlockReadOnly extends BlockField<LabelEx> {

    /**
     * The format
     */
    private String format;
    /**
     * la locale pour le format
     */
    private Locale locale;
    /**
     * the properties to display
     */
    private String[] properties;
    /**
     * if true, the full line will be hidden if the value is null
     */
    private boolean hideIfNull;

    public BlockReadOnly(BlockField x) {
        super(x);
        editor = new LabelEx();
        format = ((BlockReadOnly) x).format;
        properties = ((BlockReadOnly) x).properties;
        hideIfNull = ((BlockReadOnly) x).hideIfNull;
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param format le format d'affichage
     * @param properties le nom des propriete
     * @param hideIfNull if true, the full line will be hidden if the value is
     * null
     */
    public BlockReadOnly(String format, boolean hideIfNull, LocalisedItem li, String... properties) {
        this(li, properties);
        this.format = format;
        this.hideIfNull = hideIfNull;
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param format le format d'affichage
     * @param properties le nom des propriete
     */
    public BlockReadOnly(String format, LocalisedItem li, String... properties) {
        this(li, properties);
        this.format = format;
        this.hideIfNull = false;
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property le nom de la propriete
     */
    public BlockReadOnly(LocalisedItem li, String property) {
        super(li, property);
        editor = new LabelEx();
        format = null;
        properties = null;
        hideIfNull = false;
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param property le nom de la propriete
     * @param hideIfNull if true, the full line will be hidden if the value is
     * null
     */
    public BlockReadOnly(LocalisedItem li, String property, boolean hideIfNull) {
        super(li, property);
        editor = new LabelEx();
        format = null;
        properties = null;
        this.hideIfNull = hideIfNull;
    }

    /**
     * Constructeur
     *
     * @param li pour l'I18N
     * @param properties le nom de la propriete
     */
    public BlockReadOnly(LocalisedItem li, String... properties) {
        super(li, StringUtil.toList(Arrays.asList(properties), ","));
        this.properties = properties;
        editor = new LabelEx();
        format = null;
        hideIfNull = false;
    }

    /**
     * copie l'objet sur l'interface
     */
    @Override
    public void copyObjectToUi() {
        Object current = getParent().getCurrent();
        copyObjectToUi(current);
    }

    /**
     * copie l'objet sur l'interface. Permet de spécifier l'instance de l'objet
     * a utiliser (Attention, ce n'est valable que pour un champs en lecture
     * seule)
     *
     * @param current l'instance
     */
    public void copyObjectToUi(Object current) {
        try {

            if (hideIfNull) {
                final Object val = BeanTools.getRaw(current, property);
                if (val == null || (val instanceof String && ((String) val).trim().length() == 0)) {
                    editor.setVisible(false);
                    label.setVisible(false);
                    error.setVisible(false);
                    return;
                } else {
                    editor.setVisible(true);
                    label.setVisible(true);
                    error.setVisible(true);
                }
            }
            if (format == null || properties == null) {
                Object arg = BeanTools.getRaw(current, property);
                if (arg != null && arg.getClass().isEnum()) {
                    editor.setText(localisedItem.getString(arg.getClass().getSimpleName() + "." + arg.toString()));
                } else if (arg != null && arg instanceof Collection) {
                    StringBuilder sb = new StringBuilder();
                    for (Iterator i = ((Collection) arg).iterator(); i.hasNext();) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        Object o = i.next();
                        if (o != null && o.getClass().isEnum()) {
                            sb.append(localisedItem.getString(arg.getClass().getSimpleName() + "." + arg.toString()));
                        } else if (format == null) {
                            sb.append(o);
                        } else {
                            sb.append(String.format(format, o));
                        }
                    }
                } else {
                    editor.setText(BeanTools.get(current, property));
                }
            } else {
                Object[] arg = new Object[properties.length];
                for (int i = 0; i < properties.length; i++) {
                    arg[i] = BeanTools.getRaw(current, properties[i]);
                    if (arg[i] != null && arg[i].getClass().isEnum()) {
                        arg[i] = localisedItem.getString(arg[i].getClass().getSimpleName() + "." + arg[i].toString());
                    }
                }
                try {
                    if (locale != null) {
                        editor.setText(String.format(locale, format, arg));
                    } else {
                        editor.setText(String.format(format, arg));
                    }
                } catch (Exception e) {
                    editor.setText(e.getMessage());
                    editor.setForeground(Color.RED);
                    logger.log(Level.SEVERE, "Format \"" + format + "\", args " + Arrays.toString(properties) + " = " + Arrays.toString(arg), e);
                }
            }
        } catch (NoSuchElementException e) {
            //usually, it means that the property doesn't exists
            logger.severe(e.getMessage());
            editor.setText("");
        }
    }

    @Override
    public boolean copyUiToObject(Validator validator, List<String> genericErrors
    ) {
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
    public boolean appendError(String pp, String msg
    ) {
        return false;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public BlockReadOnly setHideIsNull(boolean hideIfNull) {
        this.hideIfNull = hideIfNull;
        return this;
    }
}
