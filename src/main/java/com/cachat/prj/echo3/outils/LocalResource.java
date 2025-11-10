package com.cachat.prj.echo3.outils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 * Provides functionality for obtaining text and binary resource files. Same as
 * echo3 resource, but using a base class to find file in J11
 */
public class LocalResource {

    /**
     * notre logger
     */
    private static Logger logger = Logger.getLogger(LocalResource.class.getSimpleName());

    /**
     * Buffer size for use in streaming an <code>InputStream</code> to an
     * <code>OutputStream</code>.
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * A RuntimeException exception that will be thrown in the event that
     * problems are encountered obtaining a resource.
     */
    public static class ResourceException extends RuntimeException {

        /**
         * Serial Version UID.
         */
        private static final long serialVersionUID = 20070101L;

        /**
         * Creates a resource exception.
         *
         * @param message a description of the error
         * @param cause the causal exception
         */
        private ResourceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Retrieves the specified resource as a <code>String</code>.
     *
     * @param ref reference class to select correct classloader/module
     * @param resourceName the name of the resource to be retrieved
     * @return the specified resource as a <code>String</code>
     */
    public static String getResourceAsString(Class ref, String resourceName) {
        return getResource(ref, resourceName).toString();
    }

    /**
     * Retrieves the specified resource as an array of <code>byte</code>s.
     *
     * @param ref reference class to select correct classloader/module
     * @param resourceName the name of the resource to be retrieved
     * @return the specified resource as an array of <code>byte</code>s
     */
    public static byte[] getResourceAsByteArray(Class ref, String resourceName) {
        return getResource(ref, resourceName).toByteArray();
    }

    /**
     * An internal method used to retrieve a resource as a
     * <code>ByteArrayOutputStream</code>.
     *
     * @param ref reference class to select correct classloader/module
     * @param resourceName the name of the resource to be retrieved
     * @return a <code>ByteArrayOutputStream</code> of the content of the
     * resource
     */
    private static ByteArrayOutputStream getResource(Class ref, String resourceName) {
        InputStream in = null;
        byte[] buffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream out = null;
        int bytesRead = 0;

        try {
            final Module module = ref.getModule();
            logger.info("Will try to load " + resourceName + " from module " + module.getName());
            in = module.getResourceAsStream(resourceName);
            if (in == null && resourceName.startsWith("/")) {
                logger.info("Resource does not exist: \"" + resourceName + "\" trying to remove /");
                in = module.getResourceAsStream(resourceName.substring(1));
            }
            if (in == null) {
                throw new ResourceException("Resource does not exist: \"" + resourceName + "\" when loaded with classloader from " + ref.getName() + " .", null);
            }
            out = new ByteArrayOutputStream();
            do {
                bytesRead = in.read(buffer);
                if (bytesRead > 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } while (bytesRead > 0);
        } catch (IOException ex) {
            throw new ResourceException("Cannot get resource: \"" + resourceName + "\".", ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }

        return out;
    }

    /**
     * Creates a new <code>JavaScript</code> service from the specified resource
     * in the <code>CLASSPATH</code>.
     *
     * @param ref la classe de référence
     * @param id the <code>Service</code> id
     * @param resourceName the <code>CLASSPATH</code> resource name containing
     * the JavaScript content
     * @return the created <code>JavaScriptService</code>
     */
    public static JavaScriptService forJsResource(Class ref, String id, String resourceName) {
        String content = getResourceAsString(ref, resourceName);
        return new JavaScriptService(id, content, resourceName);
    }

    public static JavaScriptService forJsResources(Class ref, String id, String[] resourceNames) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < resourceNames.length; ++i) {
            out.append(getResourceAsString(ref, resourceNames[i]));
            out.append("\n\n");
        }
        return new JavaScriptService(id, out.toString(), Arrays.toString(resourceNames));
    }

    /**
     * Non-instantiable class.
     */
    private LocalResource() {
    }
}
