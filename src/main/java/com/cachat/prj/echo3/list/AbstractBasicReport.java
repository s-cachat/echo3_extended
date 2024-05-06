package com.cachat.prj.echo3.list;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.BasicWindow;
import com.cachat.prj.echo3.base.EntityModifiedListener;
import com.cachat.prj.echo3.criteres.Crit;
import com.cachat.prj.echo3.criteres.CritContainer;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.SelectFieldEx;
import com.cachat.prj.echo3.ng.able.Scrollable;
import java.util.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nextapp.echo.app.*;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.list.AbstractListModel;

/**
 * fenetre de rapport generique
 *
 * @author scachat
 * @param <TypeObjet> le type d'objet utilisé par le rapport
 */
public abstract class AbstractBasicReport<TypeObjet> extends BasicWindow implements ActionListener, EntityModifiedListener<TypeObjet>, CritContainer {

    /**
     * la grille contenant les criteres
     */
    protected final transient Grid critf;
    /**
     * liste des criteres
     */
    protected final List<Crit> crits = new ArrayList<>();
    /**
     * map des criteres
     */
    protected Map<String, Crit> critm = new HashMap<>();
    /**
     * zone principale
     */
    protected final ContainerEx mainArea;
    /**
     * zone de criteres
     */
    protected final ContainerEx critArea;
    /**
     * zone de boutons
     */
    protected final ContainerEx butArea;
    /**
     * les boutons
     */
    protected Row buts;
    /**
     * hauteur de la zone de criteres
     */
    protected int critAreaHeight = 0;
    protected final SelectFieldEx pageSelect;
    protected int maxPage;

    /**
     * Constructeur
     *
     * @param app l'instance
     * @param domaine le nom du domaine
     * @param prefixe le prefixe de la fenetre
     *         * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associ?)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     */
    public AbstractBasicReport(BaseApp app, String prefixe, String domaine, Extent w, Extent h) {
        super(app, prefixe, domaine, w, h);

        critArea = new ContainerEx();
        butArea = new ContainerEx();
        mainArea = new ContainerEx();

        critf = new Grid(2);
        critf.setWidth(new Extent(100, Extent.PERCENT));
        critArea.add(critf);

        butArea.add(buts = new Row());
        ButtonEx but;
        buts.add(but = new ButtonEx(getBaseString("report.export.pdf")));
        but.addActionListener((ActionEvent e) -> exportPdf());
        but.setStyleName("Button");
        buts.add(but = new ButtonEx(getBaseString("report.export.ods")));
        but.addActionListener((ActionEvent e) -> exportOds());
        but.setStyleName("Button");
        buts.add(but = new ButtonEx(getBaseString("report.export.xlsx")));
        but.addActionListener((ActionEvent e) -> exportXlsx());
        but.setStyleName("Button");
        buts.add(pageSelect = new SelectFieldEx(new PageSelectModel()));
        pageSelect.addActionListener((ActionEvent e) -> update(null));
        addCrits();
        update(null);
        updateSizes();
    }

    protected abstract void exportPdf();

    protected abstract void exportOds();

    protected abstract void exportDocx();

    protected abstract void exportXls();

    protected abstract void exportXlsx();

    /**
     * ajoute un critere accessible a l'utilisateur
     */
    @Override
    public void addCrit(Crit crit) {
        for (Component c : crit.getComponents()) {
            critf.add(c);
        }
        crits.add(crit);
        critm.put(crit.getProp(), crit);
    }

    /**
     * donne un critere
     */
    @Override
    public Crit getCrit(String prop) {
        return critm.get(prop);
    }

    /**
     * ecoute un changement de critere
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        update(null);
    }

    public void setCritAreaHeight(int critAreaHeight) {
        this.critAreaHeight = critAreaHeight;
    }

    /**
     * rallonge la zone de critere
     */
    @Override
    public void extendCritAreaHeight(int increment) {
        this.critAreaHeight += increment;
    }

    /**
     * met a jour les tailles
     */
    protected void updateSizes() {
        if (contentPane.isEmpty()) {
            add(critArea);
            add(mainArea);
            add(butArea);
        }
        critArea.setBounds(0, 0, 0, null, null, critAreaHeight);
        butArea.setBounds(0, critAreaHeight, 0, null, null, 32);
        mainArea.setBounds(0, critAreaHeight + 32, 0, 0, null, null);
        mainArea.setScrollBarPolicy(Scrollable.AUTO);
    }

    /**
     * ajoute les criteres de selection. Les criteres doivent etre ajoutes ici
     * pour etre pris en compte lors de la selection initiale
     */
    public void addCrits() {
    }

    protected class PageSelectModel extends AbstractListModel {

        @Override
        public Object get(int index) {
            return String.valueOf(index + 1);
        }

        @Override
        public int size() {
            return maxPage;
        }

        public void update() {
            fireContentsChanged(0, size());
        }
    }

    /**
     * met a jour la liste
     *
     * @param objet l'objet modifié
     */
    @Override
    public abstract void update(TypeObjet objet);
    protected static long reportNo = System.currentTimeMillis();
    private static Pattern qm = Pattern.compile("\\?");

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
}
