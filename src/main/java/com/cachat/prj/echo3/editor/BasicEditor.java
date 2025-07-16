package com.cachat.prj.echo3.editor;

import com.cachat.prj.echo3.models.AbstractRawListModel;
import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.BasicWindow;
import com.cachat.prj.echo3.components.DateTimeField;
import com.cachat.prj.echo3.models.EntityListModel;
import com.cachat.prj.echo3.base.EntityModifiedListener;
import com.cachat.prj.echo3.base.FullScreen;
import com.cachat.prj.echo3.models.EnumListModel;
import com.cachat.prj.echo3.models.MapListModel;
import com.cachat.prj.echo3.base.SelectPopup;
import com.cachat.prj.echo3.components.UploadDataControl;
import com.cachat.prj.echo3.components.UploadImageControl;
import com.cachat.util.EntityManagerUtil;
import java.util.logging.Logger;
import jakarta.persistence.EntityManager;
import java.util.Map;
import java.util.HashMap;
import jakarta.persistence.metamodel.Type;
import nextapp.echo.app.ImageReference;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nextapp.echo.app.AwtImageReference;
import nextapp.echo.app.CheckBox;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.PasswordField;
import nextapp.echo.app.SelectField;
import nextapp.echo.app.Table;
import nextapp.echo.app.TextArea;
import nextapp.echo.app.event.ChangeEvent;
import nextapp.echo.app.event.ChangeListener;
import nextapp.echo.app.layout.GridLayoutData;
import nextapp.echo.app.list.ListModel;
import nextapp.echo.app.text.StringDocument;
import nextapp.echo.app.text.TextComponent;
import com.cachat.prj.echo3.components.ButtonEx2;
import com.cachat.prj.echo3.components.DateSelect3;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.CheckBoxEx;
import com.cachat.prj.echo3.ng.LabelEx;
import com.cachat.prj.echo3.ng.SelectFieldEx;
import com.cachat.prj.echo3.ng.Strut;
import com.cachat.prj.echo3.ng.TableEx;
import com.cachat.prj.echo3.ng.TextFieldEx;
import com.cachat.util.ACEntityManager;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.list.AbstractListModel;
import nextapp.echo.app.table.AbstractTableModel;

/**
 * Fenetre d'edition d'objet
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003-2014 SST Informatique
 * @param <TypeObjet> le type de l'objet a éditer
 */
public abstract class BasicEditor<TypeObjet> extends BasicWindow implements FullScreen {

    /**
     * optionnel, la liste correspondante
     */
    private EntityModifiedListener<TypeObjet> list;

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param list la liste (a mettre a jour en fin d'edition)
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associ?)
     */
    public BasicEditor(BaseApp app, String prefixe, String domaine, Extent w, Extent h, EntityModifiedListener<TypeObjet> list) {
        this(app, prefixe, domaine, w, h);
        this.list = list;
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param list la liste (a mettre a jour en fin d'edition)
     * @deprecated
     */
    @Deprecated
    public BasicEditor(BaseApp app, String prefixe, Extent w, Extent h, EntityModifiedListener<TypeObjet> list) {
        this(app, prefixe, "generique", w, h, list);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @deprecated
     */
    @Deprecated
    public BasicEditor(BaseApp app, String prefixe, Extent w, Extent h) {
        this(app, prefixe, "generique", w, h);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associ?)
     */
    public BasicEditor(BaseApp app, String prefixe, String domaine, int w, int h) {
        this(app, prefixe, domaine, new Extent(w), new Extent(h));
    }

    /**
     * Constructeur. Fenêtre de taille fixe 800x600, sauf si non modale
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associ?)
     */
    public BasicEditor(BaseApp app, String prefixe, String domaine) {
        this(app, prefixe, domaine, new Extent(800), new Extent(600));
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associ?)
     */
    public BasicEditor(BaseApp app, String prefixe, String domaine, Extent w, Extent h) {
        super(app, prefixe, domaine, w, h);
        cform = new Column();
        errorMsg = new Label("");
        errorMsg.setStyleName("ErrorMsg");
        cform.add(errorMsg);
        form = initForm();
        cform.add(form);
        add(cform);
        contentPane.setInsets(new Insets(5, 5));
        endEdite();
    }
    /**
     *
     */
    protected Column cform;
    /**
     * le formulaire
     */
    private Component form;

    /**
     * reinitialise le formulaire
     */
    public void reinitForm() {
        cform.removeAll();
        cform.add(errorMsg);
        fields.clear();
        fieldsInv.clear();
        forceEnabled.clear();
        errorLabel.clear();
        cform.add(form = initForm());
    }
    /**
     * champs pour les erreurs globales
     */
    protected Label errorMsg;

    /**
     * initialise le formulaire
     *
     * @return le composant (generalement un Grid de 3 de large)
     */
    public abstract Component initForm();
    /**
     * map des controles/noms de propriete
     */
    private Map<Component, String> fields = new HashMap<>();
    /**
     * map des noms de propriete/controles
     */
    private Map<String, Component> fieldsInv = new HashMap<>();

    /**
     * donne le composant pour un champs (utiliser avec précaution)
     *
     * @param property le champ
     * @return le composant
     */
    public Component getFieldFor(String property) {
        return fieldsInv.get(property);
    }
    /**
     * map indiquant pour chaque controle, si il doit etre force a enabled meme
     * si la propriete n'est pas lisible
     */
    private Map<Component, Boolean> forceEnabled = new HashMap<>();
    /**
     * map des controles/champs erreur
     */
    private Map<String, Label> errorLabel = new HashMap<>();

    /**
     * ajoute un champs texte mot de passe.
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addPasswordField(Grid form, String prop, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        TextField tf = new PasswordField();
        tf.setStyleName("Grid");
        form.add(tf);
        lst.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        while (form.getComponentCount() % form.getSize() != 0) {
            form.add(new Strut(1, 1));
        }

        l = new Label(getString(prop + "_bis"));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        tf = new PasswordField();
        tf.setStyleName("Grid");
        form.add(tf);
        lst.add(tf);
        form.add(new Strut(1, 1));
        fields.put(tf, prop + "_bis");
        fieldsInv.put(prop + "_bis", tf);
        return lst;
    }

    /**
     * ajoute un champs texte.
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addTextField(Grid form, String prop, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        TextField tf = new TextField();
        tf.setStyleName("Grid");
        form.add(tf);
        lst.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;

    }

    /**
     * ajoute un champs d'upload d'image
     *
     * @param form le formulaire
     * @param prop la propriete (de type BufferedImage)
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @param maxWidth largeur maximale de l'image
     * @param maxHeight hauteur maximale de l'image
     *
     * @return
     */
    public List<Component> addUploadImage(Grid form, String prop, int maxWidth, int maxHeight, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        LabelEx err = new LabelEx();
        UploadImageControl tf = new UploadImageControl(this, err, maxWidth, maxHeight);
        tf.setStyleName("Grid");
        form.add(tf);
        lst.add(tf);

        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    /**
     * ajoute une table pour �diter des sous objets
     *
     * @param form le formulaire
     * @param prop la propriete (de type byte[])
     * @param propType type de l'objet représenté par chaque ligne
     * @param canAdd si true permet l'ajout d'un objet
     * @param canRemove si true permet la suppression d'un objet
     * @param cols les descripteurs de colonne
     * @return la table
     */
    public TableEditor addTableEditor(Grid form, String prop, Class propType, boolean canAdd, boolean canRemove,
            TEPropertyColumn... cols) {
        GridLayoutData gld = new GridLayoutData();
        gld.setColumnSpan(form.getSize());
        gld.setAlignment(Alignment.ALIGN_LEFT);
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        l.setLayoutData(gld);
        TableEditor te = new TableEditor(prop, propType, canAdd, canRemove, cols);
        form.add(te);
        te.setLayoutData(gld);
        fields.put(te, prop);
        fieldsInv.put(prop, te);
        return te;
    }

    /**
     * ajoute un champs d'upload de fichier generique
     *
     * @param form le formulaire
     * @param prop la propriete pour le contenu (de type byte[])
     * @param propContentType la propriete pour le content type du contenu (de
     * type String)
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return
     */
    public List<Component> addUploadData(Grid form, String prop, String propContentType, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        LabelEx err = new LabelEx();
        UploadDataControl tf = new UploadDataControl(this, err);
        tf.setStyleName("Grid");
        form.add(tf);
        lst.add(tf);

        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    /**
     * ajoute un champs texte area.
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addTextArea(Grid form, String prop, int cols, int rows, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        TextArea tf = new TextArea(new StringDocument(), "", cols, rows);
        tf.setStyleName("Grid");
        form.add(tf);
        lst.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;

    }

    /**
     * ajoute un champs texte area multilingue.
     *
     * @param form le formulaire
     * @param prop le nom de la propriete de type map de code langue (String)
     * vers l'objet correspondant
     * @param propClass le type d'objet contenu dans la liste prop
     * @param propText le nom de la propriete String a modifier de l'objet
     * contenu dans prop
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addMLTextArea(Grid form, BasicEditorMLSelect sel, String prop, Class propClass, String propText, int cols,
            int rows,
            boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(String.format("%s.%s", prop, propText)));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        Grid col = new Grid(1);
        LabelEx lref = new LabelEx();
        col.add(lref);
        lst.add(lref);
        TextArea tf = new TextArea(new StringDocument(), "", cols, rows);
        tf.setStyleName("Grid");
        col.add(tf);
        lst.add(tf);
        form.add(col);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        sel.getContent().add(new BasicEditorMLItem(tf, lref, propClass, prop, propText, this));
        errorLabel.put(prop + "." + propText, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    /**
     * ajoute un champs de choix base sur une propriete enum
     *
     * @param form le formulaire
     * @param name le nom du groupe de propriete multilingue
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public BasicEditorMLSelect addMLSelect(Grid form, String name) {
        Label l = new Label(getBaseString("langue"));
        l.setStyleName("Grid");
        form.add(l);
        BasicEditorMLSelect tf = new BasicEditorMLSelect(this);
        tf.setStyleName("Grid");
        form.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        fields.put(tf, name);
        fieldsInv.put(name, tf);
        errorLabel.put(name, err);
        return tf;
    }

    /**
     * ajoute un champs date/heure.
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addDateTimeField(Grid form, String prop, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        DateTimeField tf = new DateTimeField();
        tf.setStyleName("Grid");
        tf.setButtonStyleName("Button");
        form.add(tf);
        lst.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;

    }

    /**
     * ajoute un champs date/heure.
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addDateField(Grid form, String prop, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        DateSelect3 tf = new DateSelect3();
        tf.setStyleName("Grid");
        //tf.setButtonStyleName("Button");
        form.add(tf);
        lst.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;

    }

    /**
     * ajoute un champs case a cocher.
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addCheckBox(Grid form, String prop, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        CheckBox tf = new CheckBox();
        tf.setStyleName("Grid");
        tf.addChangeListener(new ChangeCallBack(prop, tf));
        form.add(tf);
        lst.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    /**
     *     * ajoute un champs case a cocher.
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addLabel(Grid form, String prop, String value, boolean force) {
        List<Component> lst = new ArrayList<>();
        LabelEx l = new LabelEx(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        LabelEx tf = new LabelEx(value);
        tf.setStyleName("Grid");
        form.add(tf);
        lst.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    /**
     * ajoute un champs de choix base sur une propriete enum
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param values la classe enum
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addEnumSelect(Grid form, String prop, Class values, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        SelectField tf = new SelectField(new EnumListModel(values, this, false));
        tf.setStyleName("Grid");
        tf.addActionListener(new ChangeCallBack(prop, tf));
        form.add(tf);
        lst.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    /**
     * ajoute un champs de choix base sur une liste d'entite
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param values la liste des valeurs possible
     * @param propLib le nom de la propriete des valeurs a utiliser comme
     * libelle
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addEntitySelect(Grid form, String prop, List<? extends Object> values, String propLib, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        SelectField tf = new SelectField(new EntityListModel(values, propLib));
        tf.setStyleName("Grid");
        tf.addActionListener(new ChangeCallBack(prop, tf));
        form.add(tf);
        lst.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    /**
     * ajoute un champs de choix base sur une liste d'entite
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param values la liste des valeurs possible
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addEntitySelect(Grid form, String prop, LinkedHashMap<? extends Object, String> values, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        SelectField tf = new SelectField(new MapListModel(values));
        tf.setStyleName("Grid");
        form.add(tf);
        lst.add(tf);
        tf.addActionListener(new ChangeCallBack(prop, tf));
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    private class ChangeCallBack implements ActionListener, ChangeListener {

        private final String prop;
        private final Component comp;

        private ChangeCallBack(String prop, Component comp) {
            this.prop = prop;
            this.comp = comp;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (comp instanceof SelectField) {
                ListModel lm = ((SelectField) comp).getModel();
                if (lm instanceof AbstractRawListModel) {
                    valueChanged(prop, ((AbstractRawListModel) lm).getRaw(((SelectField) comp).getSelectedIndex()));
                } else {
                    valueChanged(prop, ((SelectField) comp).getSelectedItem());
                }
            } else {
                valueChanged(prop, e.getActionCommand());
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            valueChanged(prop, null);
        }
    }

    /**
     * ajoute un champs de choix multiple base sur une liste d'entite
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param values la liste des valeurs possible
     * @param propLib le nom de la propriete des valeurs a utiliser comme
     * libelle
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addEntityMulti(Grid form, String prop, List<? extends Object> values, String propLib, boolean force) {
        return addEntityMulti(form, 1, prop, values, propLib, force);
    }

    /**
     * ajoute un champs de choix multiple base sur une liste d'entite
     *
     * @param form le formulaire
     * @param cols le nombre de colonnes pour la presentation
     * @param prop le nom de la propriete
     * @param values la liste des valeurs possible
     * @param propLib le nom de la propriete des valeurs a utiliser comme
     * libelle
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addEntityMulti(Grid form, int cols, String prop, List<? extends Object> values, String propLib,
            boolean force) {

        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        BasicEditorCheckBoxMulti tf = new BasicEditorCheckBoxMulti(values, propLib, this, cols);
        form.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    /**
     * ajoute un champs de choix multiple base sur une enum
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param values la liste des valeurs possible
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addEnumMulti(Grid form, String prop, Class values, boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        BasicEditorCheckBoxMulti tf = new BasicEditorCheckBoxMulti(values, this);
        form.add(tf);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(tf, prop);
        fieldsInv.put(prop, tf);
        errorLabel.put(prop, err);
        forceEnabled.put(tf, force);
        return lst;
    }

    /**
     * ajoute un bouton permettant de choisir la valeur d'un champs dans une
     * popup.
     *
     * @param form le formulaire
     * @param prop le nom de la propriete
     * @param force si true, le champs sera toujours actif meme si la propriete
     * ne peux etre ecrite
     * @return la liste des composants (pour eventuellement les cacher)
     */
    public List<Component> addSelectPopup(Grid form, String prop, String libelle, String icon, Class<? extends SelectPopup> clazz,
            boolean force) {
        List<Component> lst = new ArrayList<>();
        Label l = new Label(getString(prop));
        l.setStyleName("Grid");
        form.add(l);
        lst.add(l);
        SelectButton bu = new SelectButton(prop, libelle, icon, clazz);
        bu.setStyleName("Grid");
        form.add(bu);
        lst.add(bu);
        Label err = new Label();
        err.setStyleName("ErrorMsg");
        form.add(err);
        lst.add(err);
        fields.put(bu, prop);
        fieldsInv.put(prop, bu);
        errorLabel.put(prop, err);
        forceEnabled.put(bu, force);
        return lst;
    }

    public class SelectButton extends ButtonEx2 implements ActionListener {

        private String prop;
        private String libelleProperty;
        private String iconProperty;
        private Class<? extends SelectPopup> clazz;

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Constructor<? extends SelectPopup> cons = clazz.getConstructor(BaseApp.class, SelectButton.class);
                app.addWindow(cons.newInstance(app, this), BasicEditor.this);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
            update();
        }

        public SelectButton(String prop, String libelleProperty, String iconProperty,
                Class<? extends SelectPopup> clazz) {
            super();
            init(prop, libelleProperty, iconProperty, clazz);
        }

        public SelectButton(String text, ImageReference icon, String prop, String libelleProperty, String iconProperty,
                Class<? extends SelectPopup> clazz) {
            super(text);
            init(prop, libelleProperty, iconProperty, clazz);
        }

        public SelectButton(ImageReference icon, String prop, String libelleProperty, String iconProperty,
                Class<? extends SelectPopup> clazz) {
            super(icon);
            init(prop, libelleProperty, iconProperty, clazz);
        }

        public SelectButton(String text, String prop, String libelleProperty, String iconProperty,
                Class<? extends SelectPopup> clazz) {
            super(text);
            init(prop, libelleProperty, iconProperty, clazz);
        }

        private void init(String prop, String libelleProperty, String iconProperty,
                Class<? extends SelectPopup> clazz) {
            this.prop = prop;
            this.libelleProperty = libelleProperty;
            this.iconProperty = iconProperty;
            this.clazz = clazz;
            this.value = cur == null ? null : BeanTools.getRaw(cur, prop);
            update();
            addActionListener(this);
        }

        /**
         * met a jour l'apparence du bouton
         */
        public void update() {
            valueChanged(prop, value);
            if (value == null) {
                this.setText(getBaseString("choose"));
                this.setIcon(app.getStyles().getButtonIcon());
            } else {
                if (libelleProperty != null) {
                    setText((String) BeanTools.getRaw(value, libelleProperty));
                }
                if (iconProperty != null) {
                    setIcon(new AwtImageReference((BufferedImage) BeanTools.getRaw(value, iconProperty)));
                }
            }
        }
    }

    /**
     * callback appele quand une valeur change
     */
    public void valueChanged(String prop, Object value) {
    }

    /**
     * met a jour un label d'indication d'erreur
     */
    public void setErrorLabel(String property, String message) {
        Label s = errorLabel.get(property);
        if (s != null) {
            s.setText(message);
        } else {
            logger.log(Level.SEVERE, "Pas de label de message d''erreur pour {0}", property);
        }
    }
    /**
     * bouton ok;
     */
    private ButtonEx2 ok;
    /**
     * bouton annuler
     */
    private ButtonEx2 cancel;
    /**
     * la ligne de boutons
     */
    private Row butRow;

    /**
     * la ligne de boutons
     */
    /**
     * ajoute une zone de bouton
     */
    public Row addButtons(Component form) {
        if (butRow == null) {
            butRow = new Row();
            butRow.setStyleName("Buttons");
            form.add(butRow);
            if (form instanceof Grid) {
                GridLayoutData gld = new GridLayoutData();
                gld.setColumnSpan(((Grid) form).getSize());
                butRow.setLayoutData(gld);
            }
        } else {
            butRow.removeAll();
            form.add(butRow);
        }
        if (ok == null) {
            ok = new ButtonEx2(getBaseString("ok"), app.getStyles().getButtonIcon());
            ok.setStyleName("GridButton");
            ok.addActionListener((ActionEvent e) -> ok());
        }

        butRow.add(ok);

        if (cancel == null) {
            cancel = new ButtonEx2(getBaseString("cancel"), app.getStyles().getButtonIcon());
            cancel.setStyleName("GridButton");
            cancel.addActionListener((ActionEvent e) -> cancel());
        }

        butRow.add(cancel);

        return butRow;
    }

    /**
     * donne le prefixe des icones
     *
     * @return le prefixe
     */
    public String getTypeIcon() {
        return "application_form";
    }

    /**
     * edite l'objet. recharge une version propre depuis la base de donnée (si
     * on n'est pas en création)
     *
     * @param o l'objet
     *
     */
    public void edite(TypeObjet o) {
        edite(o, true);
    }

    /**
     * edite l'objet
     *
     * @param o l'objet
     * @param refresh si true, recharge une version propre depuis la base de
     * donnée (si on n'est pas en création). On peut le mettre a false par
     * exemple dans le cas de sous objet.
     */
    public void edite(TypeObjet o, boolean refresh) {
        try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())) {
            if (refresh) {
                cur = refresh(em, o);
                if (cur == null && o != null) {
                    logger.severe(String.format("ERROR : refresh of %s gave %s", o, cur));
                } else {
                    logger.finest(String.format("OK : refresh of %s gave %s", o, cur));
                }

            } else {
                cur = o;
            }

            reinitForm();
            for (Map.Entry<Component, String> x : fields.entrySet()) {
                Component c = x.getKey();
                Boolean b = forceEnabled.get(x.getKey());
                if (c instanceof BasicEditorMLSelect) {//pour celui la, on n'a vraiment pas besoin de la lecture
                    ((BasicEditorMLSelect) c).edite(o);
                } else if (c instanceof BasicEditorCheckBoxMulti) {//pour celui la, on n'a pas vraiment besoin de la lecture
                    logger.fine(String.format("CheckBoxMulti, get %s from %s is %s", x.getValue(), cur, BeanTools.getRaw(cur,
                            x.getValue())));
                    ((BasicEditorCheckBoxMulti) c).setData(
                            new ArrayList<>((Collection<Object>) BeanTools.getRaw(cur, x.getValue())));
                    c.setEnabled(true);
                } else if (BeanTools.canRead(cur, x.getValue())) {
                    boolean canWrite = (b != null && b) || BeanTools.canWrite(cur, x.getValue());
                    if (c instanceof PasswordField) {
                        ((TextComponent) c).setText("");
                    } else if (c instanceof TextComponent) {
                        ((TextComponent) c).setText(BeanTools.get(cur, x.getValue()));
                    } else if (c instanceof DateTimeField) {
                        ((DateTimeField) c).setDate((java.util.Date) BeanTools.getRaw(cur, x.getValue()));
                    } else if (c instanceof DateSelect3) {
                        ((DateSelect3) c).setSelectedDate((java.util.Date) BeanTools.getRaw(cur, x.getValue()));
                    } else if (c instanceof CheckBox) {
                        ((CheckBox) c).setSelected((Boolean) BeanTools.getRaw(cur, x.getValue()));
                    } else if (c instanceof UploadImageControl) {
                        ((UploadImageControl) c).setContent((BufferedImage) BeanTools.getRaw(cur, x.getValue()));
                    } else if (c instanceof UploadDataControl) {
                        ((UploadDataControl) c).setContent((byte[]) BeanTools.getRaw(cur, x.getValue()));
                    } else if (c instanceof SelectField) {
                        Object val = BeanTools.getRaw(cur, x.getValue());
                        SelectField sf = (SelectField) c;
                        AbstractRawListModel arl = (AbstractRawListModel) sf.getModel();
                        for (int i = 0; i
                                < arl.size(); i++) {
                            Object arv = arl.getRaw(i);
                            if ((arv == null && val == null) || (arv != null && arv.equals(val))) {
                                sf.setSelectedIndex(i);
                                break;

                            }
                        }
                    } else if (c instanceof TableEditor) {
                        ((TableEditor) c).updateData();
                        canWrite = true;
                    } else {
                    }
                    c.setEnabled(canWrite);
                }
            }
            startEdite();
        }
    }

    /**
     * enregistre
     */
    public final void ok() {
        save(true);
    }

    /**
     * enregistre
     *
     * @param close si true, et si il n'y a pas d'erreur, ferme la fenetre
     * @return true si l'enregistrement a �t� r�alis� sans erreur
     */
    public boolean save(boolean close) {
        logger.fine(String.format("save(%s)", cur));
        errorMsg.setText(null);
        errorLabel.values().forEach((l) -> l.setText(null));
        boolean gotError = false;
        StringBuilder errs = new StringBuilder();
        for (Map.Entry<Component, String> x : fields.entrySet()) {
            try {
                if (x.getKey() instanceof BasicEditorMLSelect) {
                    ((BasicEditorMLSelect) x.getKey()).save();
                } else if (x.getKey() instanceof BasicEditorCheckBoxMulti) {
                    BasicEditorCheckBoxMulti sf = (BasicEditorCheckBoxMulti) x.getKey();
                    ((Collection) BeanTools.getRaw(cur, x.getValue())).clear();
                    ((Collection) BeanTools.getRaw(cur, x.getValue())).addAll(sf.getData());
                    logger.warning(String.format("Setting property %s of %s to %s : %s", x.getValue(), cur, sf.getData(),
                            BeanTools.getRaw(cur, x.getValue())));
                } else if (BeanTools.canWriteBin(cur, x.getValue()) && UploadDataControl.class.isInstance(x.getKey())) {
                    UploadDataControl sf = (UploadDataControl) x.getKey();
                    BeanTools.setBin(cur, x.getValue(), sf.getContent());
                    logger.warning(String.format("Setting property %s of %s to %s ", x.getValue(), cur,
                            sf.getContent() == null ? null : "new data"));
                } else if (BeanTools.canWriteBin(cur, x.getValue()) && UploadImageControl.class.isInstance(x.getKey())) {
                    UploadImageControl sf = (UploadImageControl) x.getKey();
                    BeanTools.setRaw(cur, x.getValue(), sf.getContent());
                    logger.warning(String.format("Setting property %s of %s to %s ", x.getValue(), cur,
                            sf.getContent() == null ? null : "new data"));
                } else if (BeanTools.canWrite(cur, x.getValue())) {
                    if (x.getKey() instanceof PasswordField) {
                        if (x.getValue().endsWith("_bis")) {
                            //nop
                        } else {
                            String p1 = ((TextComponent) x.getKey()).getText();
                            String p2 = ((TextComponent) getField(x.getValue() + "_bis")).getText();
                            if (p1 != null && p1.length() > 0) {
                                if (!p1.equals(p2)) {
                                    errorLabel.get(x.getValue()).setText("Les mots de passe ne sont pas identiques");
                                    gotError = true;
                                } else {
                                    BeanTools.set(cur, x.getValue(), ((TextComponent) x.getKey()).getText());
                                    logger.warning(String.format("Setting property %s of %s to %s : %s", x.getValue(), cur,
                                            ((TextComponent) x.getKey()).getText(), BeanTools.getRaw(cur, x.getValue())));
                                }
                            }
                        }
                    } else if (x.getKey() instanceof TextComponent) {
                        BeanTools.set(cur, x.getValue(), ((TextComponent) x.getKey()).getText());
                        logger.warning(String.format("Setting property %s of %s to %s : %s", x.getValue(), cur,
                                ((TextComponent) x.getKey()).getText(), BeanTools.getRaw(cur, x.getValue())));
                    } else if (x.getKey() instanceof DateTimeField) {
                        BeanTools.setRaw(cur, x.getValue(), ((DateTimeField) x.getKey()).getDate());
                        //logger.warning(String.format("Setting property %s of %s to %s : %s",x.getValue(),cur,((DateTimeField)x.getValue()).getDate(),BeanTools.getRaw(cur,x.getValue())));
                    } else if (x.getKey() instanceof DateSelect3) {
                        BeanTools.setRaw(cur, x.getValue(), ((DateSelect3) x.getKey()).getSelectedDate());
                    } else if (x.getKey() instanceof CheckBox) {
                        BeanTools.setRaw(cur, x.getValue(), ((CheckBox) x.getKey()).isSelected());
                        logger.warning(String.format("Setting property %s of %s to %s : %s", x.getValue(), cur,
                                ((CheckBox) x.getKey()).isSelected(), BeanTools.getRaw(cur, x.getValue())));
                    } else if (x.getKey() instanceof SelectField) {
                        SelectField sf = (SelectField) x.getKey();
                        AbstractRawListModel arl = (AbstractRawListModel) sf.getModel();
                        BeanTools.setRaw(cur, x.getValue(), arl.getRaw(sf.getSelectedIndex()));
                        logger.warning(String.format("Setting property %s of %s to %s : %s", x.getValue(), cur, arl.getRaw(
                                sf.getSelectedIndex()), BeanTools.getRaw(cur, x.getValue())));
                    } else if (SelectButton.class.isInstance(x.getKey())) {
                        SelectButton sf = (SelectButton) x.getKey();
                        BeanTools.setRaw(cur, x.getValue(), sf.getValue());
                        logger.warning(String.format("Setting property %s of %s to %s : %s", x.getValue(), cur, sf.getValue(),
                                BeanTools.getRaw(
                                        cur, x.getValue())));

                    } else {
                        logger.fine(String.format("Skipping component %s for property %s", x.getKey(), x.getValue()));
                    }
                } else if (TableEditor.class.isInstance(x.getKey())) {
                    gotError |= ((TableEditor) x.getKey()).save();
                } else {
                    logger.warning(String.format("Skipping component %s for property %s (can't Write)", x.getKey(), x.getValue()));
                }
            } catch (Throwable e) {
                if (e instanceof InvocationTargetException) {
                    e = ((InvocationTargetException) e).getCause();
                }
                if (e instanceof ParseException) {
                    errorLabel.get(x.getValue()).setText(e.getMessage());
                } else {
                    errs.append(gotError ? "\n" : "");
                    errs.append("Erreur : ").append(e.getMessage());
                }
                gotError = true;
                logger.log(Level.SEVERE, "Erreur lors de la mise a jour de " + x.getValue(), e);
            }

        }

        gotError |= setOther();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<TypeObjet>> constraintViolations = validator.validate(cur);
        boolean localOk = false;
        if (constraintViolations.isEmpty() && !gotError) {
            saveObject(cur);
            localOk = true;
            if (close) {
                endEdite();
                TypeObjet item = cur;
                cur = null;
                if (list != null) {
                    list.update(item);
                }

                userClose();
            }
        } else {
            errorMsg.setText(null);
            for (ConstraintViolation cv : constraintViolations) {
                String pp = cv.getPropertyPath().toString();
                Label l = errorLabel.get(pp);
                if (l == null && pp.startsWith("test") && pp.length() > 4) {
                    l = errorLabel.get(pp.substring(4, 5).toLowerCase() + pp.substring(5));
                }
                if (l == null) {
                    errs.append(gotError ? "\n" : "");
                    String m = cv.getMessage();
                    if (m == null) {
                        errs.append("Erreur ").append(cv.getPropertyPath());
                    } else {
                        String msg = getString2(cv.getMessage());
                        errs.append(msg != null ? msg : cv.getMessage());
                    }
                    gotError = true;
                } else {
                    l.setText(cv.getMessage());
                }

            }
            errorMsg.setText(errs.length() > 0 ? errs.toString() : null);
            contentPane.setVerticalScroll(new Extent(0));
        }

        logger.fine(String.format("ok(%s) done", cur));
        return localOk;
    }

    public class TableEditor<T> extends Column {

        private List<T> data;
        private List<Integer> index = new ArrayList<>();
        private final boolean canRemove;
        private final List<TEPropertyColumn> columns = new ArrayList<>();
        private TableModelEditor model;
        private final TableEx table;
        private final Class<T> type;
        private final String property;
        protected final Map<TEPropertyColumn, Map<Integer, Component>> comps = new HashMap<>();

//        @Override
//        public int hashCode() {
//            int hash = 7;
//            hash = 83 * hash + Objects.hashCode(this.property);
//            return hash;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == null) {
//                return false;
//            }
//            if (getClass() != obj.getClass()) {
//                return false;
//            }
//            final TableEditor<?> other = (TableEditor<?>) obj;
//            if (!Objects.equals(this.property, other.property)) {
//                return false;
//            }
//            return true;
//        }
        public TableEditor(String property, Class<T> type, boolean canAdd, boolean canRemove, TEPropertyColumn... teps) {
            this.canRemove = canRemove;
            this.type = type;
            this.property = property;
            for (TEPropertyColumn tep : teps) {
                tep.setTableEditor(this);
                columns.add(tep);
                comps.put(tep, new HashMap<>());
            }
            table = new TableEx();
            table.setDefaultRenderer(Component.class, (Table table1, Object value, int column, int row) -> value instanceof Component ? (Component) value : new LabelEx(String.valueOf(value)));
            add(table);
            table.setStyleName("DefaultT");
            table.setWidth(FULL_WIDTH);
            updateData();
            if (canAdd) {
                Row row = new Row();
                ButtonEx2 badd = new ButtonEx2(getBaseString("add"));
                badd.setStyleName("GridButton");
                row.add(badd);
                add(row);
                badd.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            index.add(index.size());
                            data.add(TableEditor.this.type.getConstructor().newInstance());
                            model.fireTableDataChanged();
                        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                            logger.log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        }

        @Override
        public void setEnabled(boolean newValue) {
            super.setEnabled(newValue); //To change body of generated methods, choose Tools | Templates.
        }

        public TEPropertyColumn getColumn(int index) {
            return columns.get(index);
        }

        public T getData(int index) {
            return data.get(index);
        }

        public Component getComp(TEPropertyColumn tep, int index) {
            return (Component) ((Map<Integer, Component>) comps.get(tep)).get(index);
        }

        public final void updateData() {
            for (Map<Integer, Component> x : comps.values()) {
                x.clear();
            }
            if (cur == null) {
                data = null;
            } else {
                data = (List<T>) BeanTools.getRaw(cur, property);
                if (data != null) {
                    for (int i = index.size(); i < data.size(); i++) {
                        index.add(i);
                    }
                    if (model != null) {
                        model.fireTableDataChanged();
                    } else {
                        table.setModel(model = new TableModelEditor());
                    }
                }
            }

        }

        private boolean save() throws Exception {
            boolean err = false;
            for (int i = 0; i < data.size(); i++) {
                for (TEPropertyColumn te : columns) {
                    err |= te.save(i);
                }
            }
            return err;
        }

        private class TableModelEditor extends AbstractTableModel {

            @Override
            public String getColumnName(int column) {
                if (column < columns.size()) {
                    return getString(TableEditor.this.property + "." + columns.get(column).getProperty());
                } else {
                    return "";
                }
            }

            @Override
            public Class getColumnClass(int column) {
                return Component.class;
            }

            @Override
            public int getColumnCount() {
                return columns.size() + (canRemove ? 1 : 0);
            }

            @Override
            public int getRowCount() {
                return columns.isEmpty() ? 0 : (data == null ? 0 : data.size());
            }

            @Override
            public Object getValueAt(int column, int row) {
                int idx = index.indexOf(row);
                if (idx < 0) {
                    LabelEx err = new LabelEx("NOT FOUND");
                    err.setBackground(Color.RED);
                    return err;
                }
                if (column < columns.size()) {
                    TEPropertyColumn tep = columns.get(column);
                    Component comp = getComp(tep, idx);
                    if (comp == null) {
                        comp = tep.getEditor(idx);
                    }
                    comps.get(tep).put(idx, comp);
                    return comp;
                } else if (canRemove && column == columns.size()) {
                    Row r = new Row();
                    r.add(new RemoveButton(TableEditor.this, idx));
                    return r;
                } else {
                    return new LabelEx("ERR");
                }
            }
        }
    }

    public abstract class TEPropertyColumn<T> {

        private final String property;
        private final boolean readOnly;
        protected TableEditor te;

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TEPropertyColumn<T> other = (TEPropertyColumn<T>) obj;
            if ((this.property == null) ? (other.property != null) : !this.property.equals(other.property)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 17 * hash + (this.property != null ? this.property.hashCode() : 0);
            return hash;
        }

        public TEPropertyColumn(String property, boolean readOnly) {
            this.property = property;
            this.readOnly = readOnly;
        }

        void setTableEditor(TableEditor te) {
            this.te = te;
        }

        public String getProperty() {
            return property;
        }

        public boolean isReadOnly() {
            return readOnly;
        }

        /**
         * donne un composant initialis� permettant l'�dition de la propri�t� du
         * bean concern� par cette colonne.
         *
         * @param index l'index de l'objet sur lequel editer la propri�t�
         * "property"
         * @return le composant
         */
        public abstract Component getEditor(int index);

        /**
         * met a jour la propri�t� du bean concern� par cette colonne a partir
         * du composant
         *
         * @param index l'index de l'objet sur lequel editer la propriété
         * "property"
         * @return true si une erreur est détectée
         * @throws java.lang.Exception en cas d'erreur
         */
        public abstract boolean save(int index) throws Exception;
    }

    public class TETextPropertyColumn<T> extends TEPropertyColumn<T> {

        public TETextPropertyColumn(String property, boolean readOnly) {
            super(property, readOnly);
        }

        @Override
        public Component getEditor(int index) {
            if (isReadOnly()) {
                return new LabelEx(BeanTools.get(te.data.get(index), getProperty()));
            } else {
                TextFieldEx tex = new TextFieldEx(BeanTools.get(te.getData(index), getProperty()));
                tex.setWidth(FULL_WIDTH);
                return tex;
            }
        }

        @Override
        public boolean save(int index) throws Exception {
            if (!isReadOnly()) {
                Component comp = te.getComp(this, index);
                T bean = (T) te.getData(index);
                logger.log(Level.INFO, "TEText : saving {0} to {1}.{2}", new Object[]{comp, bean, getProperty()});
                try {
                    BeanTools.set(te.data.get(index), getProperty(), ((TextFieldEx) comp).getText());
                } catch (ParseException pe) {
                    ((TextFieldEx) comp).setToolTipText(pe.getMessage());
                    comp.setBackground(Color.RED);
                    return true;
                }
                ((TextFieldEx) comp).setToolTipText("");
                comp.setBackground(null);
                return false;
            } else {
                return false;
            }
        }
    }

    public class TEDateTimePropertyColumn<T> extends TEPropertyColumn<T> {

        public TEDateTimePropertyColumn(String property, boolean readOnly) {
            super(property, readOnly);
        }

        @Override
        public Component getEditor(int index) {
            if (isReadOnly()) {
                return new LabelEx(String.format("%1$td/%1$tm/%1$tY %1$tH:%1$tM:%1$tS", (java.util.Date) BeanTools.getRaw(te.data.get(index),
                        getProperty())));
            } else {
                DateTimeField dtf = new DateTimeField(true);
                dtf.setDate((Date) BeanTools.getRaw(te.data.get(index), getProperty()));
                dtf.setWidth(FULL_WIDTH);
                return dtf;
            }
        }

        @Override
        public boolean save(int index) throws Exception {
            if (!isReadOnly()) {
                Component comp = te.getComp(this, index);
                T bean = (T) te.getData(index);
                logger.log(Level.INFO, "TEText : saving {0} to {1}.{2}", new Object[]{comp, bean, getProperty()});
                try {
                    BeanTools.setRaw(te.data.get(index), getProperty(), ((DateTimeField) comp).getDate());
                } catch (ParseException pe) {
                    ((DateTimeField) comp).setToolTipText(pe.getMessage());
                    comp.setBackground(Color.RED);
                    return true;
                }
                ((DateTimeField) comp).setToolTipText("");
                comp.setBackground(null);
                return false;
            } else {
                return false;
            }
        }
    }

    public class TEEnumPropertyColumn<T> extends TEPropertyColumn<T> {

        private Enum[] values;
        private String labelProperty = null;

        public TEEnumPropertyColumn(String property, String labelProperty, boolean readOnly) {
            this(property, readOnly);
            this.labelProperty = labelProperty;
        }

        public TEEnumPropertyColumn(String property, boolean readOnly) {
            super(property, readOnly);
        }

        @Override
        public Component getEditor(int index) {
            T bean = (T) te.getData(index);
            Enum value = (Enum) BeanTools.getRaw(bean, getProperty());
            values = BeanTools.getEnumValues(bean, getProperty());
            return isReadOnly() ? new LabelEx(value == null ? "" : getString(value.toString())) : new EnumSelect(value);
        }

        @Override
        public boolean save(int index) throws Exception {
            if (!isReadOnly()) {
                Component comp = te.getComp(this, index);
                T bean = (T) te.getData(index);
                logger.log(Level.INFO, "TEEnum : saving {0} to {1}.{2}", new Object[]{comp, bean, getProperty()});
                BeanTools.setRaw(te.data.get(index), getProperty(), ((EnumSelect) comp).getValue());
                return false;
            } else {
                return false;
            }
        }

        protected class EnumSelect extends SelectFieldEx {

            public EnumSelect(Enum value) {
                super(new AbstractListModel() {
                    @Override
                    public Object get(int index) {
                        return labelProperty == null ? getString(values[index].toString()) : BeanTools.get(values[index], labelProperty);
                    }

                    @Override
                    public int size() {
                        return values.length;
                    }
                });
                setSelectedIndex(value == null ? -1 : value.ordinal());
            }

            public Enum getValue() {
                if (getSelectedIndex() < 0) {
                    return null;
                }
                return values[getSelectedIndex()];
            }
        }
    }

    public class TEBooleanPropertyColumn<T> extends TEPropertyColumn<T> {

        public TEBooleanPropertyColumn(String property, boolean readOnly) {
            super(property, readOnly);
        }

        @Override
        public Component getEditor(int index) {
            CheckBoxEx cb = new CheckBoxEx();
            cb.setSelected(((Boolean) BeanTools.getRaw(te.getData(index), getProperty())));
            cb.setEnabled(!isReadOnly());
            return cb;
        }

        @Override
        public boolean save(int index) throws Exception {
            if (!isReadOnly()) {
                T bean = (T) te.getData(index);
                Component comp = te.getComp(this, index);
                BeanTools.setRaw(bean, getProperty(), ((CheckBoxEx) comp).isSelected());
            }
            return false;
        }
    }

    public class TEOrderColumn<T> extends TEPropertyColumn<T> {

        public TEOrderColumn(String property, boolean readOnly) {
            super(property, readOnly);
        }

        @Override
        public Component getEditor(int index) {
            Row r = new Row();
            if (!isReadOnly()) {
                r.add(new MoveUpButton(te, index));
            }
            r.add(new LabelEx(String.valueOf(te.index.get(index))));
            if (!isReadOnly()) {
                r.add(new MoveDownButton(te, index));
            }
            return r;
        }

        @Override
        public boolean save(int index) throws Exception {
            if (!isReadOnly()) {
                BeanTools.setRaw(te.getData(index), getProperty(), te.index.get(index));
            }
            return false;
        }

        public class MoveUpButton extends ButtonEx {

            private final int index;
            private final TableEditor te;

            public MoveUpButton(TableEditor te, int index) {
                super(app.getStyles().getIcon("moveUp"));
                this.index = index;
                this.te = te;
                addActionListener((ActionEvent e) -> {
                    int a = (Integer) te.index.get(index);
                    if (a > 0) {
                        int j = te.index.indexOf(a - 1);
                        te.index.set(index, a - 1);
                        te.index.set(j, a);
                        te.model.fireTableRowsDeleted(a - 1, a);
                    }
                });

            }
        }

        public class MoveDownButton extends ButtonEx {

            private final int index;
            private final TableEditor te;

            public MoveDownButton(TableEditor te, int index) {
                super(app.getStyles().getIcon("moveDown"));
                this.index = index;
                this.te = te;
                addActionListener((ActionEvent e) -> {
                    int a = (Integer) te.index.get(index);
                    if (a > 0) {
                        int j = te.index.indexOf(a + 1);
                        te.index.set(index, a + 1);
                        te.index.set(j, a);
                        te.model.fireTableRowsDeleted(a, a + 1);
                    }
                });
            }
        }
    }

    public class RemoveButton<T> extends ButtonEx {

        private final int index;
        private final TableEditor te;

        public RemoveButton(TableEditor te, int index) {
            super(app.getStyles().getIcon("delete"));
            this.index = index;
            this.te = te;
            addActionListener((ActionEvent e) -> {
                int a = (Integer) te.index.get(index);
                te.data.remove(index);
                te.index.remove(index);
                for (int x = 0; x < te.index.size(); x++) {
                    int b = (Integer) te.index.get(x);
                    if (b > a) {
                        te.index.set(x, b - 1);
                    }
                }
                te.model.fireTableDataChanged();
            });
        }
    }

    /**
     * enregistre l'objet (par defaut le rend persistent
     *
     * @param cur l'objet a enregistrer
     */
    public void saveObject(TypeObjet cur) {
        EntityManager em = EntityManagerUtil.getEntityManager(app.getEntityManagerName());
        try {
            em.getTransaction().begin();
            Type<?> idType = em.getMetamodel().entity(cur.getClass()).getIdType();
            String idName = em.getMetamodel().entity(cur.getClass()).getId(idType.getJavaType()).getName();
            Object o = BeanTools.getRaw(cur, idName);
            if (o == null || (o instanceof Number && ((Number) o).longValue() <= 0)) {
                logger.log(Level.INFO, "Creating {0} id={1}", new Object[]{cur.getClass().getSimpleName(), o});
                em.persist(cur);
            } else {
                logger.log(Level.INFO, "Saving {0} id={1}", new Object[]{cur.getClass().getSimpleName(), o});
                em.merge(cur);
            }
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }

    }

    /**
     * fixe les valeurs de cur qui ne sont pas geres par des champs automatiques
     * retourne true si une erreur est détectée, false sinon.
     *
     * @return true en cas d'erreur
     */
    public boolean setOther() {
        return false;
    }

    /**
     * debut de l'edition
     */
    public void startEdite() {
    }

    /**
     * fin de l'edition. Est aussi appellee lors le l'intialisation de la
     * fenetre (qui part en mode hors edition, donc comme apres une edition)
     */
    public void endEdite() {
        logger.fine(String.format("endEdite(%s)", cur));
        for (Component c : fields.keySet()) {
            c.setEnabled(false);
            if (c instanceof TextComponent) {
                ((TextComponent) c).setText("");
            } else if (c instanceof CheckBox) {
                ((CheckBox) c).setSelected(false);
            } else if (c instanceof SelectField) {
                ((SelectField) c).setSelectedIndex(-1);
            } else if (c instanceof BasicEditorMLSelect) {
                ((BasicEditorMLSelect) c).endEdite();
            }

        }
        errorLabel.values().forEach((l) -> l.setText(null));

        logger.fine(String.format("X endEdite(%s)", cur));
    }

    /**
     * annule
     */
    public final void cancel() {
        logger.fine(String.format("cancel(%s)", cur));
        cur = null;
        endEdite();

        setVisible(false);
        app.removeWindow(this);
    }

    /**
     * rafraichi l'objet avant edition. Il s'agit d'un appel a refresh . Cette
     * methode peut etre surchargee si par exemple on veurt faire un
     * rechargement avec un fetch all properties
     */
    public TypeObjet refresh(EntityManager em, TypeObjet o) {
        TypeObjet n = (TypeObjet) EntityManagerUtil.refresh(em, o);
        if (n == null) {
            return o;
        } else {
            return n;
        }
    }
    /**
     * l'objet courant
     */
    protected TypeObjet cur = null;

    /**
     * donne le champ
     *
     * @param nom le nom
     * @return le champs ou null si indefini
     */
    public Component getField(
            String nom) {
        return fieldsInv.get(nom);
    }

    public void setReadOnly(boolean b) {
        for (Component comp : fields.keySet()) {
            comp.setEnabled(!b);
            ok.setVisible(!b);
        }
    }

    /**
     * fabrique un libelle pour l'objet o, avec la ou les proprietes demandées
     *
     * @param o l'objet
     * @param propLib la liste des propriétés, séparées par des virgules ou des
     * espaces
     * @return le libelle
     */
    public static String buildLib(Object o, String propLib) {
        try {
            StringBuilder sb = new StringBuilder();
            String sep = "";
            for (String prop : propLib.split("[, ]")) {
                sb.append(sep).append(BeanTools.get(o, prop));
                sep = " ";
            }
            return sb.toString();
        } catch (Throwable t) {
            return String.format("ERR lib for %s(%s)", o.getClass().getName(), propLib);
        }
    }

    /**
     * fixe le message d'erreur d'un champs
     */
    protected void setErrorMsg(String property, String message) {
        if (errorLabel.containsKey(property)) {
            errorLabel.get(property).setText(message);
        } else {
            throw new NoSuchElementException("Property " + property);
        }
    }

    public void setParent(EntityModifiedListener<TypeObjet> parent) {
        this.list = parent;
    }
}
