package com.cachat.prj.echo3.list;

import java.io.IOException;
import java.io.OutputStream;
import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import nextapp.echo.app.StreamImageReference;

/**
 * genere une image a partir d'un rapport
 *
 * @author scachat
 */
public class ConcatenateReportImage extends StreamImageReference {

    private final JasperConcatenatedReportBuilder report;
    private String rid;
    private static long ridBase = System.currentTimeMillis();

    public ConcatenateReportImage(JasperConcatenatedReportBuilder report) {
        this.report = report;
        rid = "report_" + (ridBase++);
    }

    @Override
    public String getContentType() {
        return "image/png";
    }

    @Override
    public void render(OutputStream out) throws IOException {
        try {
            report.toPng(out);
        } catch (DRException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String getRenderId() {
        return rid;
    }
}
