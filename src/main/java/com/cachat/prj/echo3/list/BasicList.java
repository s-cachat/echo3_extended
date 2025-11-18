package com.cachat.prj.echo3.list;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.BasicWindow;
import com.cachat.prj.echo3.base.EntityModifiedListener;
import com.cachat.prj.echo3.base.LineAction;
import com.cachat.prj.echo3.base.LineMenu;
import com.cachat.prj.echo3.base.Question;
import com.cachat.prj.echo3.base.tables.MyPageableSortableTable;
import com.cachat.prj.echo3.base.tables.MyPageableTableNavigation;
import com.cachat.prj.echo3.base.tables.MySortableTable;
import com.cachat.prj.echo3.components.GridOddAlignRight;
import com.cachat.prj.echo3.criteres.Crit;
import com.cachat.prj.echo3.criteres.CritContainer;
import com.cachat.prj.echo3.interfaces.Activable;
import com.cachat.prj.echo3.interfaces.Editable;
import com.cachat.prj.echo3.models.ListTableModel;
import com.cachat.prj.echo3.ng.*;
import com.cachat.prj.echo3.ng.able.Positionable;
import com.cachat.prj.echo3.ng.able.Scrollable;
import com.cachat.prj.echo3.ng.table.*;
import com.cachat.util.ACEntityManager;
import com.cachat.util.DateTimeUtil;
import com.cachat.util.EntityManagerUtil;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import nextapp.echo.app.*;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.GridLayoutData;
import nextapp.echo.app.table.DefaultTableCellRenderer;

/**
 * Fenetre de liste generique
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @param <TypeObjet> le type des objets listés
 * @license protected
 * <p>
 * Copyright 2003 SST Informatique
 */
public abstract class BasicList<TypeObjet extends Object> extends BasicWindow implements ActionListener, EntityModifiedListener<TypeObjet>, CritContainer {

    /**
     * hauteur par defaut d'un critere
     */
    protected static int CRIT_HEIGHT = 32;

    /**
     * liste des criteres
     */
    protected List<Crit> crits = new ArrayList<>();

    /**
     * map des criteres
     */
    protected Map<String, Crit> critm = new HashMap<>();

    /**
     * la grille contenant les criteres
     */
    protected transient Grid critf;

    /**
     * les types d'objets que l'on peut cr�er
     */
    private final Class[] types;

    /**
     * Le champs de sélection pour un nouvel objet
     */
    private SelectFieldEx nouvSelect;

    /**
     * Le bouton de creation pour un nouvel objet
     */
    private ButtonEx nouvButton;

    /**
     * zone principale
     */
    private ContainerEx mainArea;

    /**
     * zone légende
     */
    private ContainerEx legendArea;

    /**
     * la légende
     */
    protected Row legend;

    /**
     * module d'export
     */
    protected Row exportRow;

    /**
     * colonne principale
     */
    private Column mainCol;
    /**
     * hauteur de la zone de navigation
     */
    private int navAreaHeight = 0;
    /**
     * zone de criteres
     */
    private ContainerEx critArea;
    /**
     * ligne pour les composants des critères (V6)
     */
    private ContainerEx critContainer;
    /**
     * bouton pour fermer le critContainer
     */
    private ButtonEx critClose;
    /**
     * hauteur d'une ligne de critère
     */
    private int critRowHeight = 32;
    /**
     * hauteur la zone de critère réduite critRow (V6)
     */
    private int reducedCritRowHeight = 32;
    /**
     * libellé pour la résumé des critères actives (V6)
     */
    private Row critSummary;
    /**
     * hauteur de la zone de criteres complète
     */
    private int critAreaHeight = 0;
    /**
     * hauteur de la légende
     */
    private int legendAreaHeight = 24;
    /**
     * barre des boutons
     */
    protected Row buts;
    /**
     * zone de navigation
     */
    private ContainerEx navArea;
    /**
     * zone d'extension
     */
    protected ContainerEx extensionContainer;
    /**
     * largeur de la zone d'extension
     */
    private int extensionWidth = 0;
    /**
     * drapeau : passer une nouvelle instance a edite(Object o) pour la creation
     */
    private boolean newInstanceOnNew = true;
    /**
     * liste
     */
    protected TableEx list;
    /**
     * les donnees
     */
    protected ListTableModel listModel;
    /**
     * zone des boutons
     */
    private ContainerEx butArea;
    /**
     * le conteneur principal
     */
    private ContainerEx mainContainer;

    /**
     * numéro du rapport
     */
    private long reportNo = System.currentTimeMillis();
    /**
     * la ligne contenant le bouton de filtrage
     */
    protected Row critRow;
    /**
     * le libellé des critères
     */
    private LabelEx critLabel;
    /**
     * in V6 interface, make the criteria always visible (default behavior in
     * pre V6)
     */
    private boolean criteriaAlwaysVisible;

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associé)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param types les types geres par cet editeur
     */
    public BasicList(BaseApp app, String prefixe, String domaine, Extent w, Extent h, Class... types) {
        this(app, prefixe, domaine, w, h, false, types);
    }

    /**
     * Constructeur
     *
     * @param lateInit si true, initBasicList ne sera pas appelé depuis ce
     * constructeur, et il devra être appelé explicitement depuis le
     * constructeur de la classe fille
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associé)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param types les types geres par cet editeur
     * @deprecated
     */
    public BasicList(BaseApp app, boolean lateInit, String prefixe, String domaine, Extent w, Extent h, Class... types) {
        this(app, lateInit, prefixe, domaine, w, h, false, false, app.getInterfaceVersion() != BaseApp.IfaceVersion.WEB_V6, types);
    }

    /**
     * Constructeur
     *
     * @param beforeInit Une fonction a appeler après le constructeur, mais
     * avant init
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associé)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param types les types geres par cet editeur
     */
    public BasicList(BaseApp app, Runnable beforeInit, String prefixe, String domaine, Extent w, Extent h, Class... types) {
        this(app, beforeInit, prefixe, domaine, w, h, false, false, app.getInterfaceVersion() != BaseApp.IfaceVersion.WEB_V6, types);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param types les types geres par cet editeur
     * @param pageable si true, pagine al table
     */
    public BasicList(BaseApp app, String prefixe, Extent w, Extent h, boolean pageable, Class... types) {
        this(app, prefixe, "generique", w, h, pageable, false, types);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param types les types geres par cet editeur
     * @param pageable si true, pagine al table
     */
    public BasicList(BaseApp app, String prefixe, boolean pageable, Class... types) {
        this(app, prefixe, "generique", new Extent(800), new Extent(600), pageable, false, types);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associ?)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param types les types geres par cet editeur
     * @param pageable si true, pagine al table
     */
    public BasicList(BaseApp app, String prefixe, String domaine, Extent w, Extent h, boolean pageable, Class... types) {
        this(app, prefixe, domaine, w, h, pageable, false, types);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associé)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param types les types geres par cet editeur
     * @param extensible si true, la page est extensible
     * @param pageable si true, pagine la table
     */
    public BasicList(BaseApp app, String prefixe, String domaine, Extent w, Extent h, boolean pageable, boolean extensible,
            Class... types) {
        this(app, prefixe, domaine, w, h, pageable, extensible, app.getInterfaceVersion() != BaseApp.IfaceVersion.WEB_V6, types);
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associé)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param types les types geres par cet editeur
     * @param extensible si true, la page est extensible
     * @param pageable si true, pagine la table
     */
    public BasicList(BaseApp app, String prefixe, String domaine, Extent w, Extent h, boolean pageable, boolean extensible, boolean criteriaAlwaysVisible,
            Class... types) {
        this(app, false, prefixe, domaine, w, h, pageable, extensible, criteriaAlwaysVisible, types);
    }

    /**
     * Constructeur
     *
     * @param lateInit si true, initBasicList ne sera pas appelé depuis ce
     * constructeur, et il devra être appelé explicitement depuis le
     * constructeur de la classe fille
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associé)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param types les types geres par cet editeur
     * @param extensible si true, la page est extensible
     * @param pageable si true, pagine la table
     * @param criteriaAlwaysVisible si true, la zone de critère est toujours
     * visible
     * @Deprecated
     */
    public BasicList(BaseApp app, boolean lateInit, String prefixe, String domaine, Extent w, Extent h, boolean pageable, boolean extensible, boolean criteriaAlwaysVisible,
            Class... types) {
        super(app, prefixe, domaine, w, h);
        this.types = types;
        this.criteriaAlwaysVisible = criteriaAlwaysVisible;
        if (!lateInit) {
            initBasicList(pageable, extensible);
        }
    }

    /**
     * Constructeur
     *
     * @param beforeInit Une fonction a appeler après le constructeur, mais
     * avant init
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associé)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     * @param types les types geres par cet editeur
     * @param extensible si true, la page est extensible
     * @param pageable si true, pagine la table
     * @param criteriaAlwaysVisible si true, la zone de critère est toujours
     * visible
     */
    public BasicList(BaseApp app, Runnable beforeInit, String prefixe, String domaine, Extent w, Extent h, boolean pageable, boolean extensible, boolean criteriaAlwaysVisible,
            Class... types) {
        super(app, prefixe, domaine, w, h);
        this.types = types;
        this.criteriaAlwaysVisible = criteriaAlwaysVisible;
        beforeInit.run();
        initBasicList(pageable, extensible);
    }

    /**
     * Cette méthode initialise la liste. Elle est appellée à la fin du
     * constructeur de BasicList sauf si lateInit est true. Dans ce dernier cas,
     * elle doit être appelée depuis le constructeur de la classe fille.
     *
     * @param pageable si true, pagine la table
     * @param extensible si true, la page est extensible
     */
    protected final void initBasicList(boolean pageable, boolean extensible) {
        beginInit(pageable, extensible);
        if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6) {
            initV6();
        } else {
            initV5();
        }
        finishInit();
    }

    //<editor-fold desc="Initialisation" defaultstate="collapsed">
    /**
     * Commence l'initialisation de la liste/table
     *
     * @param pageable si true, pagine la table
     * @param extensible si true, la page est extensible
     */
    private void beginInit(boolean pageable, boolean extensible) {
        listModel = newTableModel();
        critArea = new ContainerEx();
        mainArea = new ContainerEx();

        mainArea.add(mainCol = new Column());
        if (extensible) {
            extensionContainer = new ContainerEx();
        }

        critArea.add(critf = newCritForm());
        buts = new Row();
        addCrits();
        if (this instanceof BasicListReportExtension) {
            exportRow = new Row();
            critf.add(exportRow);
            GridLayoutData gld = new GridLayoutData();
            gld.setColumnSpan(critf.getSize());
            exportRow.setLayoutData(gld);
            extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
            ButtonEx but;
            exportRow.add(but = new ButtonEx(getBaseString("report.export.pdf")));
            but.addActionListener((e) -> exportPdf());
            but.setStyleName("Button");
            exportRow.add(but = new ButtonEx(getBaseString("report.export.ods")));
            but.addActionListener((e) -> exportOds());
            but.setStyleName("Button");
            exportRow.add(but = new ButtonEx(getBaseString("report.export.xlsx")));
            but.addActionListener((e) -> exportXlsx());
            but.setStyleName("Button");
            exportRow.add(but = new ButtonEx(getBaseString("report.export.csv")));
            but.addActionListener((e) -> exportCsv());
            but.setStyleName("Button");
        }
        update(null);
        if (pageable) {
            setNavAreaHeight(40);
            PageableTableModel tm = (PageableTableModel) (listModel instanceof PageableTableModel ? listModel : new DefaultPageableSortableTableModel(listModel));
            tm.setRowsPerPage(25);
            if (tm instanceof SortableTableModel) {
                list = new MyPageableSortableTable(tm);
            } else {
                list = new TableEx(tm);
                list.setDefaultHeaderRenderer(new MyHeaderRenderer());
            }
            navArea.add(newMyPageableTableNavigation(list));
        } else {
            if (listModel instanceof SortableTableModel) {
                list = new MySortableTable((SortableTableModel) listModel);
            } else {
                list = new MySortableTable(new DefaultSortableTableModel(listModel));
            }
        }
        list.setStyleName("BasicList");
        list.setWidth(new Extent(100, Extent.PERCENT));
    }

    /**
     * Définit les objets et styles pour les versions d'interface inférieur à V6
     */
    protected void initV5() {
        list.setStyleName("DefaultT");
        list.setDefaultRenderer(Activable.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
                Row r = new Row();
                if (value != null) {
                    TypeObjet obj = (TypeObjet) value;
                    if (canEdit(obj)) {
                        r.add(new ActivableButton(obj));
                        r.add(new EditButton(obj));
                    } else if (canView(obj)) {
                        r.add(new ViewButton(obj));
                    }
                }
                return r;
            }
        });
        list.setDefaultRenderer(Editable.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
                Row r = new Row();
                if (value != null) {
                    TypeObjet obj = (TypeObjet) value;
                    if (canEdit(obj)) {
                        r.add(new DeleteButton(obj));
                        r.add(new EditButton(obj));
                    } else if (canView(obj)) {
                        r.add(new ViewButton(obj));
                    }
                }
                return r;
            }
        });
        list.setDefaultRenderer(Date.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, java.lang.Object value, int column, int row) {
                return new Label(value == null ? "-" : DateTimeUtil.getDescriptionDate((Date) value, getBaseResource()));
            }
        });
        list.setDefaultRenderer(Component.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, java.lang.Object value, int column, int row) {
                return (Component) value;
            }
        });
    }

    /**
     * Définit les objets et styles pour la version d'interface V6
     */
    protected void initV6() {
        if (!criteriaAlwaysVisible) {
            critContainer = new ContainerEx(0, 0, 12, null, null, reducedCritRowHeight);
            critSummary = new Row();
            critRow = new Row();
            ButtonEx filters = new ButtonEx(getBaseString("filters"));
            filters.setStyleName("Crit");
            filters.addActionListener(e -> showCrit());
            critRow.add(filters);
            critRow.add(new Strut(10, 5));
            critRow.add(critLabel = new LabelEx(getBaseString("app.activeFilters")));
            critRow.add(new Strut(5, 5));
            critRow.add(critSummary);
            updateCritSummary();
            critContainer.add(critRow);
            critClose = new ButtonEx(getBaseString("close"));
            critClose.setStyleName("Crit");
            critClose.setWidth(new Extent(48, Extent.PX));
            critClose.addActionListener(e -> hideCrit());
            critArea.setStyleName("Crit");
            critAreaHeight += critRowHeight;
            critArea.setHeight(critAreaHeight);
            critArea.remove(critf);
            Column col = new Column();
            col.add(critClose);
            col.add(critf);
            critArea.add(col);
            critArea.setStyleName("Crit");
        } else {
            critArea.setStyleName("CritFixed");
        }
        list.setInsets(new Insets(8, 0, 8, 0));

        list.setStyleName("DefaultT");
        list.setDefaultRenderer(Activable.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
                Row r = new Row();
                r.setInsets(new Insets(8));
                if (value != null) {
                    TypeObjet obj = (TypeObjet) value;
                    if (app.useDropDownForList()) {
                        DropDownMenuEx ddMenu = new DropDownMenuEx();
                        r.add(ddMenu);
                        if (canEdit(obj)) {
                            ddMenu.add(new ActivableMenu(ddMenu, obj, true));
                            ddMenu.add(new EditMenu(obj, true));
                        } else if (canView(obj)) {
                            ddMenu.add(new ViewMenu(obj));
                        }
                    } else if (canEdit(obj)) {
                        r.add(new ActivableButton(obj, false));
                        r.add(new EditButton(obj, false));
                    } else if (canView(obj)) {
                        r.add(new ViewButton(obj, false));
                    }
                }
                return r;
            }
        });
        list.setDefaultRenderer(Editable.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
                Row r = new Row();
                r.setInsets(new Insets(8));
                if (value != null) {
                    TypeObjet obj = (TypeObjet) value;
                    if (canEdit(obj)) {
                        r.add(new EditButton(obj, false));
                        DropDownMenuEx ddMenu = new DropDownMenuEx();
                        DeleteButton btn = new DeleteButton(obj);
                        MenuItem mi = new MenuItem();
                        mi.setIcon(btn.getIcon());
                        mi.setText(btn.getText());
                        mi.addActionListener(e -> btn.fireActionPerformed(new ActionEvent("", "")));
                        ddMenu.add(mi);
                        r.add(ddMenu);
                    } else if (canView(obj)) {
                        r.add(new ViewButton(obj));
                    }

                }
                return r;
            }
        });
        list.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, java.lang.Object value, int column, int row) {
                Row r = new Row();
                r.setInsets(new Insets(8));
                r.add(new Label(String.valueOf(value)));
                return r;
            }
        });
        list.setDefaultRenderer(Date.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, java.lang.Object value, int column, int row) {
                Row r = new Row();
                r.setInsets(new Insets(8));
                r.add(new Label(value == null ? "-" : DateTimeUtil.getDescriptionDate((Date) value, getBaseResource())));
                return r;
            }
        });
        list.setDefaultRenderer(Component.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(Table table, java.lang.Object value, int column, int row) {
                Row r = new Row();
                r.setInsets(new Insets(8));
                r.add((Component) value);
                return r;
            }
        });
    }

    /**
     * Finalise l'initialisation de la liste/table. En cas de surchargement,
     * penser a appeler super !
     */
    protected void finishInit() {
        mainCol.add(buts);
        mainCol.add(list);
        buts.setAlignment(Alignment.ALIGN_RIGHT);

        if (canEdit(null)) {
            if (types.length > 3) {
                String[] tl = new String[types.length + 1];
                tl[0] = "";
                for (int i = 0; i < types.length; i++) {
                    tl[i + 1] = getString("new." + types[i].getSimpleName());
                }
                nouvSelect = new SelectFieldEx(tl);
                nouvSelect.addActionListener((ActionEvent e) -> {
                    if (nouvSelect.getSelectedIndex() > 0) {
                        try {
                            TypeObjet o = (TypeObjet) (newInstanceOnNew ? BasicList.this.types[nouvSelect.getSelectedIndex() - 1].getConstructor().newInstance() : null);
                            nouvSelect.setSelectedIndex(0);
                            edite(o);
                        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                            logger.log(Level.SEVERE, "Erreur", ex);
                        }
                    }
                });
                buts.add(new LabelEx(getString("nouveau")));
                buts.add(nouvSelect);
            } else if (types.length > 0) {
                for (Class type : types) {
                    nouvButton = new ButtonEx(getString("new." + type.getSimpleName()));
                    nouvButton.setStyleName("GridButton");
                    nouvButton.addActionListener(new NouvListener(type));
                    buts.add(nouvButton);
                }
            }
        }
        updateSizes();
    }
    //</editor-fold>

    /**
     * Met à jour les tailles
     */
    protected final void updateSizes() {
        if (contentPane.isEmpty()) {
            mainContainer = new ContainerEx();
            add(mainContainer);
            mainContainer.setBounds(0, 0, 0, 0, null, null);
            mainContainer.setScrollBarPolicy(Scrollable.NEVER);

            if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6) {
                mainContainer.setRight(12);
                mainContainer.setBottom(6);
                if (!criteriaAlwaysVisible) {
                    if (!crits.isEmpty()) {
                        mainContainer.add(critContainer);
                    } else {
                        reducedCritRowHeight = 0;
                    }
                } else {
                    mainContainer.add(critArea);
                }
            } else {
                mainContainer.add(critArea);
            }
            if (navArea != null) {
                mainContainer.add(navArea);
            }

            if (legendArea != null) {
                mainContainer.add(legendArea);
            }

            mainContainer.add(mainArea);
            if (extensionContainer != null) {
                add(extensionContainer);
            }
            butArea = new ContainerEx();
            butArea.add(buts);
            mainContainer.add(butArea);
        }
        int bottom = legendArea == null ? 0 : legendAreaHeight;
        int butAreaHeight = buts.getComponentCount() == 0 ? 0 : 36;
        critArea.setBounds(0, 0, extensionWidth, null, null, critAreaHeight);

        int height = critAreaHeight;
        if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6 && !criteriaAlwaysVisible) {
            height = reducedCritRowHeight;
        }

        butArea.setBounds(null, height, extensionWidth, null, null, butAreaHeight);
        if (navArea != null) {
            navArea.setBounds(0, height + butAreaHeight, null, null, null, navAreaHeight);
        }
        mainArea.setBounds(0, height + navAreaHeight + butAreaHeight, extensionWidth, bottom, null, null);
        mainArea.setScrollBarPolicy(Scrollable.AUTO);
        if (extensionContainer != null) {
            extensionContainer.setBounds(null, 0, 0, bottom, extensionWidth, null);
        }
        if (legendArea != null) {
            legendArea.setBounds(0, null, 0, 0, null, bottom);
        }
    }

    /**
     * Ajoute un critère accessible a l'utilisateur
     *
     * @param crit le critère
     */
    @Override
    public void addCrit(Crit crit) {
        crit.getComponents().stream().forEach((c) -> critf.add(c));
        crits.add(crit);
        critm.put(crit.getProp(), crit);
        updateCritSummary();
    }

    /**
     * Donne un critère par son propriété
     *
     * @param prop le nom de la propriété
     * @return le critère
     */
    @Override
    public Crit getCrit(String prop) {
        return critm.get(prop);
    }

    /**
     * Fixe la hauteur de la zone de critères
     *
     * @param critAreaHeight la hauteur
     */
    public void setCritAreaHeight(int critAreaHeight) {
        this.critAreaHeight = critAreaHeight;
    }

    /**
     * Rallonge la zone de critère
     *
     * @param increment la hauteur a ajouter
     */
    @Override
    public void extendCritAreaHeight(int increment) {
        this.critAreaHeight += increment;
    }

    /**
     * Fixe la hauteur de la zone de navigation
     *
     * @param navAreaHeight la hauteur
     */
    public final void setNavAreaHeight(int navAreaHeight) {
        this.navAreaHeight = navAreaHeight;
        if (navArea == null) {
            navArea = new ContainerEx();
        }
    }

    /**
     * Fixe la largeur de la zone d'extension
     *
     * @param extensionWidth la largeur
     */
    public void setExtensionWidth(int extensionWidth) {
        this.extensionWidth = extensionWidth;
    }

    /**
     * Donne la ligne de légende. Au premier appel, la crée et reserve sa place
     *
     * @return la ligne de legende
     */
    public Row getLegend() {
        if (legend == null) {
            legendArea = new ContainerEx(legend = new Row());
            legendArea.setScrollBarPolicy(Scrollable.AUTO);
            mainContainer.add(legendArea);
            updateSizes();
        }
        return legend;
    }

    /**
     * Génère un conteneur pour les critères
     *
     * @return le conteneur, généralement de type Grid
     */
    protected Grid newCritForm() {
        Grid critForm = new GridOddAlignRight(2);
        critForm.setWidth(new Extent(100, Extent.PERCENT));
        return critForm;
    }

    /**
     * Ajoute les critères de séléction. Les critères doivent être ajoutes ici
     * pour etre pris en compte lors de la séléction initiale
     */
    public void addCrits() {
    }

    /**
     * Drapeau : passer une nouvelle instance à edite(Object o) pour la création
     *
     * @return la valeur de newInstanceOnNew
     */
    public boolean getNewInstanceOnNew() {
        return newInstanceOnNew;
    }

    /**
     * Drapeau : passer une nouvelle instance à edite(Object o) pour la création
     *
     * @param newInstanceOnNew la valeur de newInstanceOnNew
     */
    public void setNewInstanceOnNew(boolean newInstanceOnNew) {
        this.newInstanceOnNew = newInstanceOnNew;
    }

    /**
     * Écoute un changement de critère
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        update(null);
        if (app.getInterfaceVersion() == BaseApp.IfaceVersion.WEB_V6 && !criteriaAlwaysVisible) {
            hideCrit();
        }
    }

    /**
     * Crée une instance de TableModel adaptée au type de données
     *
     * @return le modèle
     */
    public abstract ListTableModel newTableModel();

    /**
     * Édite un objet
     *
     * @param o l'objet
     */
    public abstract void edite(TypeObjet o);

    /**
     * Affiche un objet (lecture seule). Par défaut, ne fait rien
     *
     * @param o l'objet
     */
    public void view(TypeObjet o) {
    }

    /**
     * Indique si un objet peut être édité (si on affiche le bouton de
     * modification et d'activation/désactivation/suppression). Par défaut
     * répond true
     *
     * @param obj l'objet (ou null pour savoir si on peut créer un nouvel objet)
     * @return true si l'objet peut être édité
     */
    public boolean canEdit(TypeObjet obj) {
        return true;
    }

    /**
     * Indique si un objet peut être vu en lecture seule (si on affiche le
     * bouton de visualisation). Par défaut répond false. La réponse est ignorée
     * si on peut éditer l'objet (dans ce cas on affiche le bouton d'édition).
     *
     * @param obj l'objet
     * @return true si l'objet peut être édité
     */
    public boolean canView(TypeObjet obj) {
        return false;
    }

    /**
     * Efface un objet
     *
     * @param o l'objet
     */
    public void efface(TypeObjet o) {
        Question.ask(app, this, "Confirmation", "Confirmez la supression", new Extent(200, Extent.PX), new Extent(100, Extent.PX), new Efface(o));
    }

    /**
     * Donne le préfixe des icônes
     *
     * @return le préfixe
     */
    public String getTypeIcon() {
        return "application_form";
    }

    /**
     * Met à jour la liste
     *
     * @param objet l'objet modifié (peut être null)
     */
    @Override
    public void update(TypeObjet objet) {
        StringBuilder sb = new StringBuilder();
        List<Object> arg = new ArrayList<>();
        String sep = "";
        for (Crit c : crits) {
            String s = c.updateWhere(arg);
            if (s != null) {
                sb.append(sep).append(s);
                sep = " and ";
            }
        }
        listModel.update(sb.toString(), arg);
        updateCritSummary();
    }

    /**
     * (V6) Met à jour la ligne de résumé de critères
     */
    protected void updateCritSummary() {
        if (critSummary == null) {
            return;
        }
        critSummary.removeAll();
        critLabel.setVisible(false);
        for (int i = 0, j = 0; i < crits.size(); i++) {
            String summary = crits.get(i).getSummary();
            if (summary == null) {
                continue;
            }
            if (j > 0) {
                critSummary.add(new LabelEx(", " + summary));
            } else {
                critSummary.add(new LabelEx(summary));
            }
            critLabel.setVisible(false);
            j++;
        }
    }
    private static Pattern qm = Pattern.compile("\\?");

    /**
     * Fabrique une requête
     *
     * @param em l'entity manager
     * @param req la requête sous forme texte
     * @param arg les arguments
     * @return la requête
     */
    protected Query createQuery(EntityManager em, String req, List<Object> arg) {
        StringBuffer sb = new StringBuffer();
        int n = 1;
        Matcher m = qm.matcher(req);
        while (m.find()) {
            m.appendReplacement(sb, ":arg" + (n++));
        }
        m.appendTail(sb);
        req = sb.toString();
        logger.severe(String.format("Requete \"%s\" avec les parametres \"%s\"", req, arg));

        Query q = em.createQuery(req);
        for (int i = 0; i < arg.size(); i++) {
            logger.log(Level.FINEST, "set param {0}{1} to {2}", new Object[]{i, 1, arg.get(i)});
            q.setParameter("arg" + (i + 1), arg.get(i));
        }
        return q;
    }

    /**
     * Affiche la zone de critères (V6)
     */
    private void showCrit() {
        if (critContainer != null) {
            critContainer.removeAll();
            critContainer.add(critArea);
            critContainer.setHeight(critAreaHeight);
        }
    }

    /**
     * Cache la zone de critères (V6)
     */
    private void hideCrit() {
        if (critContainer != null) {
            critContainer.removeAll();
            critContainer.add(critRow);
            critContainer.setHeight(reducedCritRowHeight);
        }
    }

    public int getLegendAreaHeight() {
        return legendAreaHeight;
    }

    public void setLegendAreaHeight(int legendAreaHeight) {
        this.legendAreaHeight = legendAreaHeight;
    }

    /**
     * donne la hauteur d'une ligne de critère
     *
     * @return la hauteur
     */
    public int getCritRowHeight() {
        return critRowHeight;
    }

    /**
     * fixe la hauteur d'une ligne de critère. Doit être appelé dans le
     * beforeInit.
     *
     * @param critRowHeight la hauteur
     */
    public void setCritRowHeight(int critRowHeight) {
        this.critRowHeight = critRowHeight;
    }

    //<editor-fold desc="Rapport" defaultstate="collapsed">
    /**
     * Génère le rapport à éxporter, si on implemente BasicListeReportExtension.
     *
     * @return le rapport
     */
    private JasperReportBuilder generate() {
        StringBuilder sb = new StringBuilder();
        List<Object> arg = new ArrayList<>();
        String sep = "";
        for (Crit c : crits) {
            String s = c.updateWhere(arg);
            if (s != null) {
                sb.append(sep).append(s);
                sep = " and ";
            }
        }
        return ((BasicListReportExtension) this).generate(sb, arg);
    }

    /**
     * Callback pour l'éxport en .pdf, si on implemente
     * BasicListeReportExtension.
     */
    private void exportPdf() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toPdf(out);
            app.sendDoc("application/pdf", "report_" + (reportNo++) + ".pdf", out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Callback pour l'éxport en .ods, si on implemente
     * BasicListeReportExtension.
     */
    private void exportOds() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toOds(out);
            app.sendDoc("application/vnd.oasis.opendocument.spreadsheet", "report_" + (reportNo++) + ".ods", out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Callback pour l'éxport en .docx, si on implemente
     * BasicListeReportExtension.
     */
    private void exportDocx() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toDocx(out);
            app.sendDoc("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "report_" + (reportNo++) + ".docx",
                    out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Callback pour l'éxport en .xls, si on implemente
     * BasicListeReportExtension.
     */
    private void exportXls() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toXls(out);
            app.sendDoc("application/vnd.ms-excel", "report_" + (reportNo++) + ".xls", out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Callback pour l'éxport en .xlsx, si on implemente
     * BasicListeReportExtension.
     */
    private void exportXlsx() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toXlsx(out);
            app.sendDoc("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "report_" + (reportNo++) + ".xlsx",
                    out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Callback pour l'éxport en .csv, si on implemente
     * BasicListeReportExtension.
     */
    private void exportCsv() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toCsv(out);
            app.sendDoc("text/csv", "report_" + (reportNo++) + ".csv",
                    out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Donne une nouvelle instance de PageableTableNavigation
     *
     * @param list La table associée
     * @return la nouvelle instance
     */
    protected MyPageableTableNavigation newMyPageableTableNavigation(TableEx list) {
        return new MyPageableTableNavigation(list);
    }

    //</editor-fold>
    //<editor-fold desc="Classes" defaultstate="collapsed">
    /**
     * le gestionnaire de bouton nouveau
     */
    public class NouvListener implements ActionListener {

        /**
         * la classe a creer
         */
        private final Class clazz;

        /**
         * Constructeur
         *
         * @param clazz la classe a creer
         */
        public NouvListener(Class clazz) {
            this.clazz = clazz;
        }

        /**
         * edite un nouvel objet
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                TypeObjet o = (TypeObjet) (newInstanceOnNew ? clazz.getConstructor().newInstance() : null);
                edite(o);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                logger.log(Level.SEVERE, "Erreur", ex);
            }
        }
    }

    /**
     * un bouton dont l'etat commande l'activation d'un Activable
     */
    protected class ActivableButton extends LineAction {

        /**
         * l'objet
         */
        protected TypeObjet o;
        /**
         * si true, affiche le texte du bouton
         */
        private final boolean showText;

        /**
         * Constructeur
         *
         * @param o l'objet concerne
         */
        public ActivableButton(TypeObjet o) {
            this(o, true);
        }

        /**
         * Constructeur
         *
         * @param showText si true, affiche le texte du bouton
         * @param o l'objet concerne
         */
        public ActivableButton(TypeObjet o, boolean showText) {
            super();
            this.app = BasicList.this.app;
            this.showText = showText;
            addAction((e) -> action());
            this.o = o;
            update();

        }

        @Override
        public void update() {
            this.iconImage = app.getStyles().getIcon(((Activable) o).getActif() ? "add" : "delete");
            if (showText) {
                this.label = app.getBaseString(((Activable) o).getActif() ? "desactiver" : "activer");
            } else {
                this.label = null;
                setToolTipText(app.getBaseString(((Activable) o).getActif() ? "desactiver" : "activer"));
            }
            super.update();
            setVisible(BasicList.this.canEdit(o));
        }

        /**
         * reagit
         *
         * @return true si l'action est effectuée
         */
        public boolean action() {
            try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())) {
                em.getTransaction().begin();
                o = (TypeObjet) EntityManagerUtil.refresh(em, o);
                ((Activable) o).setActif(!((Activable) o).getActif());
                em.merge(o);
                em.getTransaction().commit();
            }
            update();
            return true;
        }
    }

    /**
     * un bouton dont l'etat commande l'activation d'un Activable
     */
    protected class ActivableMenu extends LineMenu {

        /**
         * l'objet
         */
        protected TypeObjet o;
        /**
         * si true, affiche le texte du bouton
         */
        private final boolean showText;
        private final DropDownMenuEx menu;

        /**
         * Constructeur
         *
         * @param o l'objet concerne
         * @param menu le menu (dont la couleur de fond dépendra de l'activation
         * de l'objet)
         */
        public ActivableMenu(DropDownMenuEx menu, TypeObjet o) {
            this(menu, o, true);
        }

        /**
         * Constructeur
         *
         * @param showText si true, affiche le texte du bouton
         * @param obj l'objet concerne
         * @param menu le menu (dont la couleur de fond dépendra de l'activation
         * de l'objet)
         */
        private ActivableMenu(DropDownMenuEx menu, TypeObjet obj, boolean showText) {
            super();
            this.app = BasicList.this.app;
            this.showText = showText;
            this.menu = menu;
            addAction((e) -> action());
            this.o = obj;
            update();
        }

        @Override
        public void update() {
            final boolean actif = ((Activable) o).getActif();
            this.iconImage = app.getStyles().getIcon(actif ? "add" : "delete");
            if (showText) {
                this.label = app.getBaseString(actif ? "desactiver" : "activer");
            } else {
                this.label = "";
            }
            super.update();
            menu.setBackground(actif ? Color.GREEN : Color.RED);
            setVisible(BasicList.this.canEdit(o));
        }

        /**
         * reagit
         *
         * @return true si l'action est effectuée
         */
        public boolean action() {
            try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())) {
                em.getTransaction().begin();
                o = (TypeObjet) EntityManagerUtil.refresh(em, o);
                ((Activable) o).setActif(!((Activable) o).getActif());
                em.merge(o);
                em.getTransaction().commit();
            }
            update();
            return true;
        }
    }

    /**
     * un bouton lancant l'edition
     */
    protected class EditMenu extends LineMenu {

        /**
         * Constructeur
         *
         * @param o l'objet a editer
         */
        public EditMenu(TypeObjet o) {
            this(o, false);
        }

        /**
         * Constructeur
         *
         * @param showText si true, affiche le texte du bouton
         * @param o l'objet concerne
         */
        public EditMenu(TypeObjet o, boolean showText) {
            super();
            this.app = BasicList.this.app;
            this.label = showText ? getBaseString("edit") : null;
            this.iconImage = app.getStyles().getIcon("edit");
            this.addAction(e -> {
                edite(o);
                return true;
            });
            update();
        }

    }

    /**
     * un bouton lancant l'edition
     */
    protected class EditButton extends LineAction {

        /**
         * Constructeur
         *
         * @param o l'objet a editer
         */
        public EditButton(TypeObjet o) {
            this(o, false);
        }

        /**
         * Constructeur
         *
         * @param showText si true, affiche le texte du bouton
         * @param o l'objet concerne
         */
        public EditButton(TypeObjet o, boolean showText) {
            super();
            this.app = BasicList.this.app;
            this.label = showText ? getBaseString("edit") : null;
            this.iconImage = app.getStyles().getIcon("edit");
            this.addAction(e -> {
                edite(o);
                return true;
            });
            setToolTipText(getBaseString("edit"));
            update();
        }

    }

    /**
     * un bouton lancant la visualisation
     */
    protected class ViewButton extends LineAction {

        /**
         * Constructeur
         *
         * @param o l'objet concerne
         */
        public ViewButton(TypeObjet o) {
            this(o, true);
        }

        /**
         * Constructeur
         *
         * @param showText si true, affiche le texte du bouton
         * @param o l'objet concerne
         */
        public ViewButton(TypeObjet o, boolean showText) {
            super();
            this.app = BasicList.this.app;
            this.label = showText ? getBaseString("view") : null;
            this.iconImage = app.getStyles().getIcon("eye");
            this.addAction(e -> {
                view(o);
                return true;
            });
            setToolTipText(getBaseString("eye"));
            update();
        }

        @Override
        protected void update() {
            setText(null);
            setIcon(iconImage);
        }
    }

    /**
     * un bouton lancant la visualisation
     */
    protected class ViewMenu extends LineMenu {

        /**
         * Constructeur
         *
         * @param o l'objet a editer
         */
        public ViewMenu(TypeObjet o) {
            super();
            this.app = BasicList.this.app;
            this.label = getBaseString("view");
            this.iconImage = app.getStyles().getIcon("eye");
            this.addAction(e -> {
                view(o);
                return true;
            });
            update();
        }

        @Override
        protected void update() {
            setText(null);
            setIcon(iconImage);
        }
    }

    /**
     * un bouton effacant l'objet
     */
    protected class DeleteButton extends LineAction {

        /**
         * Constructeur
         *
         * @param o l'objet a effacer
         */
        public DeleteButton(TypeObjet o) {
            super();
            this.app = BasicList.this.app;
            this.iconImage = app.getStyles().getIcon("cross");
            this.label = app.getBaseString("delete");
            this.addAction(e -> {
                efface(o);
                return true;
            });
            setToolTipText(getBaseString("delete"));
            update();
        }

    }

    /**
     * un bouton effacant l'objet
     */
    protected class DeleteMenu extends LineMenu {

        /**
         * Constructeur
         *
         * @param o l'objet a effacer
         */
        public DeleteMenu(TypeObjet o) {
            super();
            this.app = BasicList.this.app;
            this.iconImage = app.getStyles().getIcon("cross");
            this.label = app.getBaseString("delete");
            this.addAction(e -> {
                efface(o);
                return true;
            });
            update();
        }

    }

    /**
     * reagit a la confirmation d'effacement
     */
    class Efface implements Question.Reponse {

        /**
         * objet a effacer
         */
        private final Object o;

        /**
         * constructeur
         */
        public Efface(Object o) {
            this.o = o;
        }

        /**
         * reponse
         */
        @Override
        public void reponse(boolean b) {
            if (b) {
                EntityManager em = EntityManagerUtil.getEntityManager(app.getEntityManagerName());
                em.getTransaction().begin();
                em.remove(EntityManagerUtil.refresh(em, o));
                em.getTransaction().commit();
                em.close();
                update(null);
            }
        }
    }

    class MyHeaderRenderer extends DefaultTableCellRenderer {

        public MyHeaderRenderer() {
        }

        @Override
        public Component getTableCellRendererComponent(Table table, Object value, int column, int row) {
            ContainerEx ce = new ContainerEx(0, 0, 0, 0, new LabelEx(String.valueOf(value)));
            ce.setPosition(Positionable.RELATIVE);
            ce.setStyleName("TableHeader");
            return ce;
        }
    }
    //</editor-fold>
}
