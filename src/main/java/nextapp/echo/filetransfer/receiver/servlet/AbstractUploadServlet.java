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

package nextapp.echo.filetransfer.receiver.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import nextapp.echo.filetransfer.receiver.UploadMonitor;
import nextapp.echo.filetransfer.receiver.UploadProcessor;
import nextapp.echo.filetransfer.receiver.UploadProcessorFactory;

/**
 * Abstract base class for servlets which will receive file uploads.
 * This servlet is intended for use with client-side applications, it is not
 * necessary to deploy it if using the server-side Java version of Echo.
 */
public abstract class AbstractUploadServlet extends HttpServlet {

    /**
     * @see jakarta.servlet.http.HttpServlet#doGet(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        String out = UploadMonitor.processRequest(request);
        
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Expires", "0");

        response.setContentType("text/xml");
        response.getWriter().write(out.toString());
    }

    /**
     * @see jakarta.servlet.http.HttpServlet#doPost(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        String id = request.getParameter("pid");
        if (!JakartaServletFileUpload.isMultipartContent(request)) {
            response.sendError(400, "Request must contain multipart content: \"" + id + "\".");
            return;
        }
        getUploadProcessor().processUpload(request, id);
    }
    
    public UploadProcessor getUploadProcessor() {
        return UploadProcessorFactory.getUploadProcessor();
    }
}
