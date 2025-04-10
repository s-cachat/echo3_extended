package com.cachat.prj.echo3.synoptique;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * un visuel svg
 *
 * @author scachat
 */
public class SynViewSvg extends SynView {

    /**
     * crée une image
     *
     * @param content une image png
     */
    public SynViewSvg(byte[] content) {
        contentType = "image/svg+xml";
        this.content = content;
    }

    /**
     * crée une image à partir d'une resource (utilise le classloader de SynViewPng)
     *
     * @param resourceUrl l'url d'une resource image svg
     * @return la vue
     * @throws IOException en cas d'erreur lors de la génération de l'image
     * encodée
     */
    public static SynViewSvg buildFromResource(String resourceUrl) throws IOException {
        return new SynViewSvg(SynViewSvg.class.getResourceAsStream(resourceUrl).readAllBytes());
    }
    /**
     * crée une image à partir d'une resource (utilise le classloader de la classe donnée en paramètre)
     *
     * @param resourceUrl l'url d'une resource image svg
     * @return la vue
     * @throws IOException en cas d'erreur lors de la génération de l'image
     * encodée
     */
    public static SynViewSvg buildFromResource(Class clazz,String resourceUrl) throws IOException {
        return new SynViewSvg(clazz.getResourceAsStream(resourceUrl).readAllBytes());
    }

    /**
     * crée une image
     *
     * @param content l'image
     * @throws IOException en cas d'erreur lors de la génération de l'image
     * encodée
     */
    public SynViewSvg(String content) throws IOException {
        contentType = "image/svg";
        this.content = content.getBytes(Charset.forName("UTF8"));
    }

}
