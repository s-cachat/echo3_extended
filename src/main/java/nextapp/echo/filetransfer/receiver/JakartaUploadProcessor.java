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
package nextapp.echo.filetransfer.receiver;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;

import nextapp.echo.filetransfer.model.Upload;
import nextapp.echo.filetransfer.model.UploadProcess;
import nextapp.echo.filetransfer.model.event.UploadProcessEvent;
import nextapp.echo.filetransfer.model.event.UploadProcessListener;

import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.core.ProgressListener;
import org.apache.commons.fileupload2.core.FileItemInputIterator;
import org.apache.commons.fileupload2.core.FileItemInput;
import org.apache.commons.fileupload2.core.FileUploadSizeException;
import org.apache.commons.io.FilenameUtils;

/**
 * {@link UploadProcessor} implementation that uses the Jakarta Commons
 * FileUpload library.
 * <p>
 * See http://jakarta.apache.org/commons/fileupload for details.
 */
public class JakartaUploadProcessor
        implements UploadProcessor {

    private static final Logger logger = Logger.getLogger("ApacheCommonUpload");
    private static final int DEFAULT_MEMORY_CACHE_THRESHOLD = 16 * 1024; // 16 KB
    private static final File DEFAULT_TEMP_LOCATION = new File(System.getProperty("java.io.tmpdir", "."));
    private static final int DEFAULT_UPLOAD_SIZE_LIMIT = 20 * 1024 * 1024; // 20 MB

    /**
     * Constant indicating that there is no size limit.
     */
    public static final short NO_SIZE_LIMIT = -1;

    /**
     * Lowest interval at which {@link UploadProcess#progress} should be
     * invoked.
     */
    private static final int PROGRESS_INTERVAL = 250;

    /**
     * Global bandwidth allocator.
     */
    private static final BandwidthAllocator allocator = new BandwidthAllocator(3 * 1024 * 1024);

    /**
     * Returns the collective bandwidth (in bytes per second) available for all
     * file transfers, for all users combined. A value of zero indicates
     * bandwidth is not throttled.
     *
     * @return the bandwidth
     */
    public static int getBandwidth() {
        if (allocator.isThrottling()) {
            return allocator.getBandwidth();
        } else {
            return 0;
        }
    }

    /**
     * Sets the collective bandwidth (in bytes per second) available for all
     * file transfers, for all users combined. A value of zero indicates
     * bandwidth is not throttled.
     *
     * @param newValue the new bandwidth setting
     */
    public static void setBandwidth(int newValue) {
        if (newValue < 0) {
            throw new IllegalArgumentException("Invalid bandwidth value.");
        } else if (newValue == 0) {
            allocator.setThrottling(false);
        } else {
            allocator.setThrottling(true);
            allocator.setBandwidth(newValue);
        }
    }

    /**
     * Stateful object used to process upload.
     */
    private class Instance
            implements ProgressListener {

        private boolean aborted = false;

        /**
         * The current {@link Upload} object being processed. Progress events
         * will be forwarded to this upload object.
         */
        private Upload currentUpload;

        /**
         * The next system time (in milliseconds) after which progress may be
         * reported to the component. This is used to avoid inundating the
         * component with events.
         */
        private long nextProgressTime = 0;

        /**
         * {@link BandwidthAllocator.Tracker} implementation.
         */
        private BandwidthAllocator.Tracker allocatorTracker = new BandwidthAllocator.Tracker() {

            /**
             * @see
             * nextapp.echo.filetransfer.receiver.BandwidthAllocator.Tracker#bytesTransferred(long)
             */
            public void bytesTransferred(long bytes) {
            }

            /**
             * @see
             * nextapp.echo.filetransfer.receiver.BandwidthAllocator.Tracker#isAborted()
             */
            public boolean isAborted() {
                return aborted;
            }
        };

        /**
         * The incoming {@link HttpServletRequest}.
         */
        private HttpServletRequest request;

        /**
         * The id of the {@link UploadProcess}
         */
        private String id;

        /**
         * The {@link UploadProcess} in which this processor is participating.
         */
        private UploadProcess uploadProcess = null;

        /**
         * Listener temporarily registered to {@link UploadProcess} to determine
         * if the operation has been canceled
         */
        private UploadProcessListener uploadProcessListener = new UploadProcessListener() {

            /**
             * @see
             * nextapp.echo.filetransfer.model.event.UploadProcessListener#uploadStart(
             * nextapp.echo.filetransfer.model.event.UploadProcessEvent)
             */
            public void uploadStart(UploadProcessEvent e) {
            }

            /**
             * @see
             * nextapp.echo.filetransfer.model.event.UploadProcessListener#uploadProgress(
             * nextapp.echo.filetransfer.model.event.UploadProcessEvent)
             */
            public void uploadProgress(UploadProcessEvent e) {
            }

            /**
             * @see
             * nextapp.echo.filetransfer.model.event.UploadProcessListener#uploadComplete(
             * nextapp.echo.filetransfer.model.event.UploadProcessEvent)
             */
            public void uploadComplete(UploadProcessEvent e) {
            }

            /**
             * @see
             * nextapp.echo.filetransfer.model.event.UploadProcessListener#uploadCancel(
             * nextapp.echo.filetransfer.model.event.UploadProcessEvent)
             */
            public void uploadCancel(UploadProcessEvent e) {
                aborted = true;
            }
        };

        /**
         * Creates a new upload processing instance.
         *
         * @param request the incoming {@link HttpServletRequest}
         * @param id the id of the {@link UploadProcess}
         */
        private Instance(HttpServletRequest request, String id) {
            super();
            this.request = request;
            this.id = id;
        }

        /**
         * Processes the file upload.
         */
        private void process() {
            logger.severe("UploadReceiverService : process()");
            DiskFileItemFactory itemFactory = DiskFileItemFactory.builder().get();
//            itemFactory.setRepository(getDiskCacheLocation());
//            itemFactory.setSizeThreshold(getMemoryCacheThreshold());

            String encoding = request.getCharacterEncoding();
            if (encoding == null) {
                encoding = "UTF-8";
            }
            logger.severe("UploadReceiverService : process/1");
            JakartaServletFileUpload sfu = new JakartaServletFileUpload(itemFactory);
            sfu.setHeaderCharset(Charset.forName(encoding));
            sfu.setProgressListener(this);
            if (getFileUploadSizeLimit() != NO_SIZE_LIMIT) {
                logger.severe("UploadReceiverService : process/size limit : " + getFileUploadSizeLimit());
                sfu.setSizeMax(getFileUploadSizeLimit());
            }

            uploadProcess = UploadProcessManager.get(request, id, true);
            uploadProcess.addProcessListener(uploadProcessListener);
            currentUpload = null;
            logger.severe("UploadReceiverService : process/2");
            try {
                FileItemInputIterator iter = sfu.getItemIterator(request);
                int uploadIndex = 0;
                logger.severe("UploadReceiverService : process/3");
                while (!aborted && iter.hasNext()) {
                    FileItemInput item = iter.next();
                    logger.severe("UploadReceiverService : process/4/" + item.getFieldName());
                    if (!item.isFormField()) {
                        logger.severe("UploadReceiverService : process/5");
                        currentUpload = uploadProcess.createUpload();
                        String fileName = FilenameUtils.getName(item.getName());
                        DiskFileItem item2 = itemFactory.fileItemBuilder()
                                .setFieldName(item.getFieldName())
                                .setFileName(item.getName())
                                .setContentType(item.getContentType())
                                .setFormField(false)
                                .get();

                        uploadProcess.configure(currentUpload, item.getContentType(), fileName);
                        logger.severe("UploadReceiverService : process/6");
                        uploadProcess.start(currentUpload);

                        if (currentUpload.getStatus() == Upload.STATUS_IN_PROGRESS) {
                            allocator.copy(allocatorTracker, item.getInputStream(), item2.getOutputStream());
                            logger.severe("UploadReceiverService : process/7/" + item2.getSize());
                            uploadProcess.complete(currentUpload, item2.getInputStream(), item2.getSize());
                        }
                        logger.severe("UploadReceiverService : process/8");
                        ++uploadIndex;
                    }
                }
                logger.severe("UploadReceiverService : process/9");
            } catch (FileUploadSizeException ex) {
                logger.log(Level.SEVERE, "Error", ex);
                if (currentUpload == null) {
                    /* If currentUpload is null, the SizeLimitExceededException was thrown
                     because the fileupload library detected that the content length specified
                     in the request's header is too large, before any upload objects could be created.
                     In this case, create a dummy Upload object to set the status on to inform the listeners.*/
                    uploadProcess.createUpload();
                }
                uploadProcess.setStatus(Upload.STATUS_ERROR_OVERSIZE);

            } catch (FileUploadException ex) {
                logger.log(Level.SEVERE, "Error", ex);
                uploadProcess.setStatus(Upload.STATUS_ERROR_IO);
            } catch (RuntimeException | Error ex) {
                logger.log(Level.SEVERE, "Error", ex);
                throw ex;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error", ex);
                uploadProcess.setStatus(Upload.STATUS_ERROR_IO);
            } finally {
                logger.severe("UploadReceiverService : end process");
                uploadProcess.removeProcessListener(uploadProcessListener);
            }
        }

        /**
         * @see org.apache.commons.fileupload.ProgressListener#update(long,
         * long, int)
         */
        public void update(long pBytesRead, long pContentLength, int pItems) {
            if (!uploadProcess.isInitialized()) {
                uploadProcess.init(pContentLength);
            }
            if (currentUpload != null && System.currentTimeMillis() > nextProgressTime) {
                uploadProcess.progress(currentUpload, pBytesRead);
                nextProgressTime = System.currentTimeMillis() + PROGRESS_INTERVAL;
            }
        }
    }

    /**
     * Returns the location where cached files should be stored to disk.
     *
     * @return the disk cache location
     */
    public File getDiskCacheLocation() {
        return DEFAULT_TEMP_LOCATION;
    }

    /**
     * Returns the maximum allowed file upload size, in bytes.
     *
     * @return the maximum allowed file upload size, in bytes
     */
    public long getFileUploadSizeLimit() {
        return DEFAULT_UPLOAD_SIZE_LIMIT;
    }

    /**
     * Returns the maximum file size that may be stored in memory. Files larger
     * than this size will be stored in the disk cache.
     *
     * @return the maximum file size that may be stored in memory
     */
    public int getMemoryCacheThreshold() {
        return DEFAULT_MEMORY_CACHE_THRESHOLD;
    }

    /**
     * @see
     * nextapp.echo.filetransfer.receiver.UploadProcessor#processUpload(HttpServletRequest,
     * String)
     */
    public void processUpload(HttpServletRequest request, String id) {
        Instance instance = new Instance(request, id);
        try {
            instance.process();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "UploadReceiverService : Jakarta error", t);
        }
    }
}
