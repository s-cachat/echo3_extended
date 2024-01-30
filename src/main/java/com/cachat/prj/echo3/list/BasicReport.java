package com.cachat.prj.echo3.list;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.EntityModifiedListener;
import com.cachat.prj.echo3.criteres.Crit;
import com.cachat.prj.echo3.criteres.CritContainer;
import com.cachat.prj.echo3.ng.LabelEx;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import nextapp.echo.app.*;
import nextapp.echo.app.event.ActionListener;

/**
 * fenetre de rapport generique
 *
 * @author scachat
 * @param <TypeObjet> le type d'objet utilisé par le rapport
 */
public abstract class BasicReport<TypeObjet> extends AbstractBasicReport<TypeObjet> implements ActionListener, EntityModifiedListener<TypeObjet>, CritContainer {

    /**
     * si true, pas d'image preview
     */
    private boolean noPreview = false;

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
    public BasicReport(BaseApp app, String prefixe, String domaine, int w, int h) {
        this(app, prefixe, domaine, new Extent(w), new Extent(h));
    }

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
    public BasicReport(BaseApp app, String prefixe, String domaine, Extent w, Extent h) {
        super(app, prefixe, domaine, w, h);

    }

    @Override
    protected void exportPdf() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toPdf(out);
            app.sendDoc("application/pdf", "report_" + (reportNo++) + ".pdf", out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void exportOds() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toOds(out);
            app.sendDoc("application/vnd.oasis.opendocument.spreadsheet", "report_" + (reportNo++) + ".ods", out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void exportDocx() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toDocx(out);
            app.sendDoc("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "report_" + (reportNo++) + ".docx",
                    out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void exportXls() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toXls(out);
            app.sendDoc("application/vnd.ms-excel", "report_" + (reportNo++) + ".xls", out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void exportXlsx() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            generate().toXlsx(out);
            app.sendDoc("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "report_" + (reportNo++) + ".xlsx",
                    out.toByteArray());
        } catch (DRException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

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
        JasperReportBuilder x = generate(sb, arg);
        if (x != null) {
            try {
                maxPage = x.toJasperPrint().getPages().size();
                ((PageSelectModel) (pageSelect.getModel())).update();
            } catch (DRException ex) {
                Logger.getLogger(BasicReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            maxPage = 0;
        }
        return x;
    }

    /**
     * met a jour la liste
     *
     * @param objet l'objet modifié
     */
    @Override
    public void update(TypeObjet objet) {
        JasperReportBuilder report = generate();
        mainArea.removeAll();
        if (!noPreview) {
            mainArea.add(new LabelEx(new ReportImage(report, pageSelect.getSelectedIndex())));
        } else {
            mainArea.add(new LabelEx(" "));
        }
    }

    public boolean isNoPreview() {
        return noPreview;
    }

    public void setNoPreview(boolean noPreview) {
        this.noPreview = noPreview;
    }

    public abstract JasperReportBuilder generate(StringBuilder sb, List<Object> arg);

}
