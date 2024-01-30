package nextapp.echo.filetransfer.app.event;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.TaskQueueHandle;

/**
 * un upload listener qui appelle uploadComplete dans un thread de l'affichage
 *
 * @author scachat
 */
public class UploadAsyncListener implements UploadListener {

    private final ApplicationInstance app;
    private final UploadListener ul;

    public UploadAsyncListener(ApplicationInstance app, UploadListener ul) {
        this.app = app;
        this.ul = ul;
    }

    private class DoUploadComplete implements Runnable {

        private final UploadEvent e;
        private final TaskQueueHandle tq;

        public DoUploadComplete(UploadEvent e) {
            this.e = e;
            app.enqueueTask(tq = app.createTaskQueue(), this);
        }

        @Override
        public void run() {
            ul.uploadComplete(e);
            app.removeTaskQueue(tq);
        }

    }

    @Override
    public void uploadComplete(UploadEvent e) {
        new DoUploadComplete(e);
    }

    @Override
    public void uploadSend() {
        ul.uploadSend();
    }

}
