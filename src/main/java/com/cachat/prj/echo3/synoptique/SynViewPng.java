package com.cachat.prj.echo3.synoptique;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * un visuel png
 *
 * @author scachat
 */
public class SynViewPng extends SynView {

    /**
     * crée une image
     *
     * @param content une image png
     */
    public SynViewPng(byte[] content) {
        contentType = "image/png";
        this.content = content;
    }

    /**
     * crée une image à partir d'une resource (utilise le classloader de SynViewPng)
     *
     * @param resourceUrl l'url d'une resource image png
     * @return la vue
     * @throws IOException en cas d'erreur lors de la génération de l'image
     * encodée
     */
    public static SynViewPng buildFromResource(String resourceUrl) throws IOException {
        return new SynViewPng(SynViewPng.class.getResourceAsStream(resourceUrl).readAllBytes());
    }
    /**
     * crée une image à partir d'une resource (utilise le classloader de la classe donnée en paramètre)
     *
     * @param resourceUrl l'url d'une resource image png
     * @return la vue
     * @throws IOException en cas d'erreur lors de la génération de l'image
     * encodée
     */
    public static SynViewPng buildFromResource(Class clazz,String resourceUrl) throws IOException {
        return new SynViewPng(clazz.getResourceAsStream(resourceUrl).readAllBytes());
    }

    /**
     * crée une image
     *
     * @param content l'image
     * @throws IOException en cas d'erreur lors de la génération de l'image
     * encodée
     */
    public static SynViewPng buildFromImage(RenderedImage content) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(content, "png", bos);
            bos.flush();
            return new SynViewPng(bos.toByteArray());
        }
    }

}
