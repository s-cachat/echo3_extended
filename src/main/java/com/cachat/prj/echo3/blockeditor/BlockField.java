package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.LabelEx;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Component;

/**
 * un champs. Le type est le composant utilisé pour l'edition
 *
 * @author scachat
 * @param <T> le type de composant utilisé pour l'édition
 */
public abstract class BlockField<T extends Component> implements BlockInterface {

    protected static final transient Logger logger = Logger.getLogger("BlockEditor");
    protected LabelEx label;
    protected LabelEx error;
    protected T editor;
    protected String property;
    protected List<Component> components;
    public BlockContainer parent;
    protected final LocalisedItem localisedItem;

    /**
     * Constructeur par recopie pour le cloneage
     *
     * @param x l'original
     */
    public BlockField(BlockField x) {
        this.label = new LabelEx(x.label.getText(), x.label.getIcon());
        this.label.setStyleName(x.label.getStyleName());
        this.error = new LabelEx(x.error.getText(), x.error.getIcon());
        this.error.setStyleName(x.error.getStyleName());
        this.property = x.property;
        this.localisedItem = x.localisedItem;
    }

    /**
     * Constructeur
     *
     * @param li used for label generation
     * @param property the property name and the label code tor I18N. If they
     * are not the same, parameter must be the property name, the | sign and the
     * label code.
     */
    public BlockField(LocalisedItem li, String property) {
        String[] x = property.split("\\|");
        if (x.length > 1) {
            this.label = new LabelEx(li.getString(x[1]));
            this.property = x[0];
        } else {
            this.label = new LabelEx(li.getString(property));
            this.property = property;
        }
        label.setStyleName("Grid");
        label.setTextAlignment(Alignment.ALIGN_RIGHT);
        error = new LabelEx();
        error.setStyleName("ErrorMsg");
        this.localisedItem = li;
    }

    public LocalisedItem getLocalisedItem() {
        return localisedItem;
    }

    /**
     * return ui components list (label, editor and error field)
     *
     * @return the component list with 3 items
     */
    public List<Component> getComponents() {
        if (components == null) {
            components = Arrays.asList((Component) label, (Component) editor, (Component) error);
        }
        return components;
    }

    public BlockContainer getParent() {
        return parent;
    }

    @Override
    public void setParent(BlockContainer parent) {
        this.parent = parent;
    }

    @Override
    public void setVisible(boolean visible) {
        label.setVisible(visible);
        error.setVisible(visible);
        editor.setVisible(visible);
    }

    @Override
    public void setEnabled(boolean enabled) {
        label.setEnabled(enabled);
        error.setEnabled(enabled);
        editor.setEnabled(enabled);
    }

    @Override
    public Object clone() {
        try {
            return getClass().getConstructor(BlockField.class).newInstance(this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("while cloning an instance of "+getClass().getName(),ex);
        }
    }

    /**
     * valide une propriété
     *
     * @param validator le validateur
     * @param current l'objet
     * @param property la propriété
     * @return true si erreur
     */
    protected boolean validateProperty(Validator validator, Object current, String... property) {
        if (validator != null) {
            Set<ConstraintViolation<Object>> vp = new HashSet<>();
            for (String p : property) {
                Object o = getParent().getCurrent();
                String pe[] = p.split("\\.");
                if (pe.length > 1) {
                    List<String> path = new ArrayList(Arrays.asList(pe));
                    if (path.isEmpty()) {
                        throw new RuntimeException("Invalid property : \"" + p + "\"");
                    }
                    while (path.size() > 1) {
                        o = BeanTools.getRaw(o, path.remove(0));
                    }
                    p = path.get(0);
                }
                vp.addAll(validator.validateProperty(o, p));
            }

            if (!vp.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                vp.forEach((cv) -> {
                    sb.append(cv.getMessage());
                    logger.log(Level.SEVERE, "Erreur lors de la validation de " + property + ": " + cv.getMessage());
                });
                error.setText(sb.toString());
                return true;
            } else {
                error.setText((String) null);
                return false;
            }
        } else {
            return true;
        }
    }

    public void setErrorMessage(String msg) {
        error.setText(msg);
    }

    public String getProperty() {
        return property;
    }

    /**
     * change le libelle
     *
     * @param label le libelle
     */
    public void setLabelText(String label) {
        this.label.setText(label);
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
        if (property.equals(pp)) {
            String v = error.getText();
            if (v != null && v.length() > 0) {
                return true;
            } else {
                error.setText(msg);
                return true;
            }

        } else {
            return false;
        }
    }

    /**
     * gère une erreur, soit en trouvant le message a afficher a côté du champ,
     * soit en la mettant dans la liste des erreurs globales Les erreurs
     * dérivant de ParseException, ou de InvalidException sont affichées à côté
     * du champ. Pour les erreurs de type InvalidException, on cherche d'abord
     * message avec la clé error.[code], ou si il n'y a pas de code, un message
     * avec la clé error.[simple class name]
     *
     * @param e l'erreur.
     * @param genericErrors la liste des erreurs globales.
     */
    protected void handleError(Throwable e, List<String> genericErrors) {
        if (e.getCause() != null && e.getCause() instanceof InvocationTargetException) {
            e = e.getCause().getCause();
        }
        if (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getCause();
        }
        if (e instanceof ParseException) {
            error.setText(e.getMessage());
        } else if (e instanceof InvalidException) {
            String code = ((InvalidException) e).getCode();
            error.setText(localisedItem.getBaseString("error." + (code == null ? e.getClass().getSimpleName() : code)));
        } else {
            genericErrors.add(e.getMessage());
            error.setText((String) null);
        }
        logger.log(Level.SEVERE, "Erreur lors de la mise a jour de " + property, e);
    }
}
