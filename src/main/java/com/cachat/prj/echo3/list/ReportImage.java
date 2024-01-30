package com.cachat.prj.echo3.list;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.jasper.builder.export.JasperImageExporterBuilder;
import net.sf.dynamicreports.jasper.constant.ImageType;
import net.sf.dynamicreports.report.exception.DRException;
import nextapp.echo.app.StreamImageReference;

/**
 * genere une image a partir d'un rapport
 *
 * @author scachat
 */
public class ReportImage extends StreamImageReference {

    private final JasperReportBuilder report;
    private String rid;
    private static long ridBase = System.currentTimeMillis();
    private final int page;

    public ReportImage(JasperReportBuilder report, int page) {
        this.report = report;
        rid = "report_" + (ridBase++);
        this.page = page;
    }

    @Override
    public String getContentType() {
        return "image/png";
    }

    @Override
    public void render(OutputStream out) throws IOException {
        try {
            JasperImageExporterBuilder x = Exporters.imageExporter(out, ImageType.PNG);
            x.setStartPageIndex(page);
            x.setEndPageIndex(page);
            if (report != null) {
                report.toImage(out, ImageType.PNG);
            } else {
                BufferedImage bi = new BufferedImage(80, 60, BufferedImage.TYPE_INT_RGB);
                ImageIO.write(bi, "PNG", out);
            }
        } catch (IllegalArgumentException e) {
            BufferedImage x = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            x.getGraphics().drawString(e.getMessage(), 10, 10);
            ImageIO.write(x, "PNG", out);
        } catch (DRException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String getRenderId() {
        return rid;
    }
}
