package com.cachat.prj.echo3.components;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.AwtImageReference;
import nextapp.echo.app.Extent;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.StreamImageReference;
import nextapp.echo.app.WindowPane;

/**
 * un bouton qui affiche un qr code
 *
 * @author scachat
 */
public class QrButton extends ButtonEx2 {

    private final String url;
    private WindowPane parent;

    public QrButton(String url, WindowPane parent) {
        super(new ResourceImageReference("/com/cachat/prj/echo3/components/qr.png"));
        addActionListener(e -> showQr());
        this.url = url;
        this.parent = parent;
    }

    public void showQr() {
        BaseApp app = ((BaseApp) getApplicationInstance());
        app.addWindow(new QrWindows(app), parent);
    }

    class QrWindows extends WindowPane {

        public QrWindows(BaseApp app) {
            super("", new Extent(420), new Extent(420));
            setModal(true);
            ButtonEx b;

            StreamImageReference sir = new StreamImageReference() {
                @Override
                public String getContentType() {
                    return "image/png";
                }

                @Override
                public void render(OutputStream out) throws IOException {
                    try {
                        MatrixToImageWriter.writeToStream(new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400), "png", out);
                    } catch (WriterException ex) {
                        Logger.getLogger(QrButton.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public String getRenderId() {
                    return String.valueOf(url.hashCode());
                }
            };
            add(b = new ButtonEx(sir));
            b.addActionListener(e -> userClose());

        }
    }

}
