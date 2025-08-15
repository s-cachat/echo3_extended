package com.cachat.prj.echo3.blockeditor;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.EntityModifiedListener;
import com.cachat.prj.echo3.components.ButtonEx2;
import com.cachat.prj.echo3.base.BasicWindow;
import com.cachat.prj.echo3.base.FullScreen;
import com.cachat.prj.echo3.components.DirectHtml;
import com.cachat.util.EntityManagerUtil;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.Strut;
import com.cachat.util.ACEntityManager;
import static com.cachat.util.EntityManagerUtil.saveOrCreateEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Panel;
import static nextapp.echo.app.Position.STATIC;
import nextapp.echo.app.Row;
import nextapp.echo.app.layout.GridLayoutData;

/**
 * Editor d'objets composé de composants en blocs
 *
 * @author scachat
 * @param <T> le type d'objet à modifier
 */
public abstract class BlockEditor<T> extends BasicWindow implements FullScreen {

    /**
     * La fenêtre à prévenir des modifications (typiquemement la liste qui devra
     * être raffraichie avec la valeur modifiée)
     */
    protected EntityModifiedListener list;

    /**
     * Le colonne principale
     */
    protected Column cform;

    /**
     * Le champs pour les erreurs globales
     */
    protected DirectHtml errorMsg;

    /**
     * Le formulaire principal
     */
    protected BlockContainer form;

    /**
     * L'objet courant
     */
    public T current;

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le domaine de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param list la liste (a mettre a jour en fin d'edition)
     */
    public BlockEditor(BaseApp app, String prefixe, String domaine, Extent w, Extent h, EntityModifiedListener list) {
        this(app, prefixe, domaine, w, h);
        this.list = list;
    }

    /**
     * Constructeur. Taille fixe sauf si non modale
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le domaine de la fenetre
     */
    public BlockEditor(BaseApp app, String prefixe, String domaine) {
        this(app, prefixe, domaine, new Extent(800), new Extent(600));
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le domaine de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     */
    public BlockEditor(BaseApp app, String prefixe, String domaine, int w, int h) {
        this(app, prefixe, domaine, new Extent(w), new Extent(h));
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le domaine de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     */
    public BlockEditor(BaseApp app, String prefixe, String domaine, Extent w, Extent h) {
        super(app, prefixe, domaine, w, h);
        cform = new Column();
        errorMsg = new DirectHtml("");
        errorMsg.setStyleName("ErrorMsg");
        add(new ContainerEx(0, 0, 0, 0, cform));
        reinitForm();
        if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6) {
            cform.setInsets(new Insets(6, 6, 12, 6));
            contentPane.setInsets(new Insets(-6, 0, 0, 0));
        } else {
            contentPane.setInsets(new Insets(5, 5));
        }
    }

    /**
     * Initialise le formulaire
     *
     * @return le composant (generalement un Grid de 3 de large)
     */
    public abstract BlockContainer initForm();

    /**
     * Reinitialise le formulaire
     */
    public final void reinitForm() {
        cform.removeAll();
        Panel p = new Panel();
        p.setStyleName("ErrorMsg");
        cform.add(p);
        p.add(errorMsg);
        form = initForm();
        cform.add(form.getComponent());
    }

    protected class BlockButton implements BlockBase<Component>, BlockInterface {

        /**
         * bouton ok;
         */
        protected final ButtonEx2 ok;

        /**
         * bouton annuler
         */
        protected final ButtonEx2 cancel;

        /**
         * la ligne de boutons
         */
        protected final Row butRow;

        /**
         * la colonne de boutons
         */
        protected final Column butCol;

        /**
         * découpage de la ligne de boutons en trois colonnes
         */
        protected final Grid butGrid;

        /**
         * les lignes pour chaque colonne de boutons
         */
        protected final Row leftRow, centerRow, rightRow;

        /**
         * le conteneur de boutons
         */
        protected final ContainerEx butContainer;

        /**
         * la ligne de boutons
         */
        /**
         * ajoute une zone de bouton
         */
        public BlockButton() {
            if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6) {
                butContainer = new ContainerEx(0, null, 0, 0, null, null);
                butContainer.setPosition(STATIC);
                butContainer.setStyleName("GridButton");

                butGrid = new Grid(3);
                butGrid.setStyleName("GridButton");
                GridLayoutData gld = new GridLayoutData(1, 1);

                leftRow = new Row();
                leftRow.setAlignment(Alignment.ALIGN_LEFT);
                leftRow.setLayoutData(gld);
                butGrid.add(leftRow);

                centerRow = new Row();
                centerRow.setAlignment(Alignment.ALIGN_CENTER);
                centerRow.setLayoutData(gld);
                butGrid.add(centerRow);

                rightRow = new Row();
                rightRow.setAlignment(Alignment.ALIGN_RIGHT);
                rightRow.setLayoutData(gld);
                butGrid.add(rightRow);

                butContainer.add(butGrid);
                butGrid.setColumnWidth(0, new Extent(20, Extent.PERCENT));
                butGrid.setColumnWidth(1, new Extent(60, Extent.PERCENT));
                butGrid.setColumnWidth(2, new Extent(20, Extent.PERCENT));

                ok = new ButtonEx2(getBaseString("ok"));
                ok.setStyleName("GridButton");
                ok.addActionListener((e) -> ok());
                addButtonRight(ok);

                cancel = new ButtonEx2(getBaseString("cancel"));
                cancel.setStyleName("GridButton");
                cancel.addActionListener((e) -> cancel());
                addButtonLeft(cancel);

                // Variables < V6
                butRow = leftRow;
                butCol = null;
            } else {
                // Initialisation pour versions inferieur à V6
                butCol = new Column();
                butRow = new Row();
                butCol.add(new Strut(5, 5));
                butCol.add(butRow);
                butRow.setStyleName("Buttons");
                ok = new ButtonEx2(getBaseString("ok"), app.getStyles().getButtonIcon());
                ok.setStyleName("GridButton");
                ok.addActionListener((e) -> ok());
                butRow.add(ok);
                cancel = new ButtonEx2(getBaseString("cancel"), app.getStyles().getButtonIcon());
                cancel.setStyleName("GridButton");
                cancel.addActionListener((e) -> cancel());
                addButton(cancel);

                // Variables V6
                butContainer = null;
                butGrid = null;
                leftRow = null;
                centerRow = null;
                rightRow = null;
            }
        }

        @Override
        public Component getComponent() {
            if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6) {
                return butContainer;
            } else {
                return butCol;
            }
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
            butRow.setVisible(visible);
        }

        @Override
        public void setEnabled(boolean enabled) {
            ok.setEnabled(enabled);
            cancel.setEnabled(enabled);
        }

        /**
         * change uniquement la possibilité d'appuyer sur ok
         */
        public void setOkEnabled(boolean enabled) {
            ok.setEnabled(enabled);
        }

        @Override
        public Object clone() {
            return new BlockButton();
        }

        /**
         * add another button
         *
         * @param b the button
         */
        public void addButton(Button b) {
            if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6) {
                if (centerRow == null) {
                    butContainer.add(b);
                } else {
                    centerRow.add(new Strut(6, 5));
                    centerRow.add(b);
                }
            } else {
                butRow.add(new Strut(5, 5));
                butRow.add(b);
            }
        }

        /**
         * (V6) Ajoute un bouton sur la partie gauche de la ligne de boutons
         *
         * @param b le bouton
         */
        protected void addButtonLeft(Button b) {
            if (leftRow == null) {
                butContainer.add(b);
            } else {
                leftRow.add(b);
                leftRow.add(new Strut(6, 5));
            }
        }

        /**
         * (V6) Ajoute un bouton sur la partie droite de la ligne de boutons
         *
         * @param b le bouton
         */
        protected void addButtonRight(Button b) {
            if (rightRow == null) {
                butContainer.add(b);
            } else {
                rightRow.add(new Strut(6, 5));
                rightRow.add(b);
            }
        }

    }

    /**
     * donne le prefixe des icones
     */
    public String getTypeIcon() {
        return "application_form";
    }

    /**
     * edite l'objet. recharge une version propre depuis la base de donnÃ©e (si
     * on n'est pas en crÃ©ation)
     *
     * @param o l'objet
     *
     */
    public void edite(T o) {
        edite(o, true);
    }

    /**
     * edite l'objet
     *
     * @param o l'objet
     * @param refresh si true, recharge une version propre depuis la base de
     * donnÃ©e (si on n'est pas en création). On peut le mettre a false par
     * exemple dans le cas de sous objet.
     */
    public void edite(T o, boolean refresh) {
        try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName());) {
            if (refresh) {
                current = refresh(em, o);
                if (current == null && o != null) {
                    logger.severe(String.format("ERROR : refresh of %s gave %s", o, current));
                } else {
                    logger.info(String.format("OK : refresh of %s gave %s", o, current));
                }

            } else {
                current = o;
            }

            reinitForm();
            form.copyObjectToUi();
        }
    }

    /**
     * donne l'objet en cours d'édition
     *
     * @return l'objet en cours d'édition
     */
    public T getCurrent() {
        return current;
    }

    /**
     * enregistre
     */
    public final void ok() {
        save(true);
    }

    /**
     * valide les données, mais n'enregistre pas
     *
     * @return true si les données sont valides
     */
    public boolean validateWithoutSaving() {
        List<String> genericErrors = new ArrayList<>();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        boolean gotError = form.copyUiToObject(validator, genericErrors);
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(current);
        return (constraintViolations.isEmpty() && !gotError);
    }

    /**
     * enregistre
     *
     * @param close si true, et si il n'y a pas d'erreur, ferme la fenetre
     * @return true si l'enregistrement a été réalisé sans erreur
     */
    public boolean save(boolean close) {
        logger.fine(String.format("save(%s)", current));
        errorMsg.setText("");
        List<String> genericErrors = new ArrayList<>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        boolean gotError = form.copyUiToObject(validator, genericErrors);
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(current);
        boolean localOk = false;
        if (constraintViolations.isEmpty() && !gotError) {
            saveObject(current);
            localOk = true;
            if (close) {
                T item = current;
                current = null;
                if (list != null) {
                    list.update(item);
                }
                userClose();
            }
        } else {
            errorMsg.setText("");
            constraintViolations.stream().forEach((cv) -> {
                String msg = getApplicationSpecificMessage(cv);
                if (msg != null) {
                    genericErrors.add(msg);
                } else {
                    String pp = cv.getPropertyPath().toString();

                    String m = cv.getMessage();
                    if (m == null || m.isBlank()) {
                        genericErrors.add(String.format("Erreur \"%s\"", cv.getPropertyPath()));
                    } else {

                        if (!form.appendError(pp, m)) {
                            genericErrors.add(m);
                        }
                        logger.info("Form error : " + pp + " => " + m);
                    }

                }
            });
            if (genericErrors.isEmpty()) {
                errorMsg.setText("");
            } else {
                StringBuilder sb = new StringBuilder("<ul>");
                genericErrors.stream().forEach((s) -> sb.append("<li>").append(s).append("</li>"));
                genericErrors.stream().forEach((s) -> logger.info("Form generic error : => " + s));
                sb.append("</ul>");
                errorMsg.setText(sb.toString());
                contentPane.setVerticalScroll(new Extent(0));
            }

        }

        logger.fine(String.format("ok(%s) done", current));
        return localOk;
    }

    /**
     * donne le message spécifique pour la violation de contrainte. Doit
     * retourner null si pas de message spécifique. La validation de contrainte
     * est faite avant l'enregistrement
     *
     * @param cv la violation
     * @return le message ou null
     */
    public String getApplicationSpecificMessage(ConstraintViolation cv) {
        return null;
    }

    /**
     * enregistre l'objet (par defaut le rend persistent)
     *
     * @param cur l'objet a enregistrer
     */
    public void saveObject(T cur) {
        try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())) {
            saveOrCreateEntity(em, cur);
        }
    }

    /**
     * annule
     */
    public void cancel() {
        logger.fine(String.format("cancel(%s)", current));
        current = null;
        userClose();
    }

    /**
     * rafraichi l'objet avant edition. Il s'agit d'un appel a refresh . Cette
     * methode peut etre surchargee si par exemple on veurt faire un
     * rechargement avec un fetch all properties
     */
    public T refresh(EntityManager em, T o) {
        T n = (T) EntityManagerUtil.refresh(em, o);
        if (n == null) {
            return o;
        } else {
            return n;
        }
    }
}
