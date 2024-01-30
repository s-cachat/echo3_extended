/*
 * This file is part of the Echo File Transfer Library.
 * Copyright (C) 2002-2009 NextApp, Inc.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */
package nextapp.echo.filetransfer.webcontainer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;

import nextapp.echo.filetransfer.app.AbstractUploadSelect;
import nextapp.echo.filetransfer.model.UploadProcess;
import nextapp.echo.filetransfer.receiver.UploadProcessManager;
import nextapp.echo.webcontainer.Connection;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.SynchronizePeerFactory;
import nextapp.echo.webcontainer.WebContainerServlet;


/**
 * Processes a file upload HTTP request from the client.
 */
public class UploadReceiverService extends BaseUploadService {

    private static final Logger logger = Logger.getLogger("ApacheCommonUpload");
    /**
     * Singleton instance.
     */
    public static final Service INSTANCE = new UploadReceiverService();

    static {
        WebContainerServlet.getServiceRegistry().add(INSTANCE);
    }

    /**
     * Default constructor. This is a singleton, use <code>INSTANCE</code>
     * static property.
     */
    private UploadReceiverService() {
        super();
    }

    /**
     * Flag indicating whether service has been installed.
     */
    private static boolean installed = false;

    /**
     * Installs this service, invoke before use.
     */
    public static void install() {
        if (!installed) {
            installed = true;
        }
    }

    /**
     * @see Service#getId()
     */
    public String getId() {
        return "EchoFileTransfer.UploadReceiver";
    }

    /**
     * @see Service#getVersion()
     */
    public int getVersion() {
        return DO_NOT_CACHE;
    }

    /**
     * @see
     * nextapp.echo.filetransfer.webcontainer.BaseUploadService#service(nextapp.echo.webcontainer.Connection,
     * java.lang.String)
     */
    public void service(Connection conn, String processId)
            throws IOException {
        HttpServletRequest request = conn.getRequest();
        if (!JakartaServletFileUpload.isMultipartContent(request)) {
            logger.severe("service : not multipart");
            serviceBadRequest(conn, "Request must contain multipart content.");
            return;
        }
        logger.severe("UploadReceiverService : Got multipart request");
        UploadProcess process = UploadProcessManager.get(conn.getRequest(), processId, true);
        String componentId = request.getParameter("cid");
        AbstractUploadSelect uploadSelect = (AbstractUploadSelect) conn.getUserInstance().getComponentByClientRenderId(componentId);
        uploadSelect.setUploadProcess(process);
        logger.log(Level.SEVERE, "UploadReceiverService : request is for component {0}", componentId);
        AbstractUploadSelectPeer uploadSelectPeer = (AbstractUploadSelectPeer) SynchronizePeerFactory.getPeerForComponent(
                uploadSelect.getClass());
        uploadSelectPeer.getUploadProcessor(uploadSelect).processUpload(request, processId);
        logger.severe("UploadReceiverService : Done with multipart request");
    }
}
