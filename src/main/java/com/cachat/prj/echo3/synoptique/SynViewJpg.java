package com.cachat.prj.echo3.synoptique;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * un visuel jpg
 *
 * @author scachat
 */
public class SynViewJpg extends SynView {

    /**
     * crée une image
     *
     * @param content une image jpg
     */
    public SynViewJpg(byte[] content) {
        contentType = "image/jpg";
        this.content = content;
    }

    /**
     * crée une image à partir d'une resource (utilise le classloader de SynViewPng)
     *
     * @param resourceUrl l'url d'une resource image jpg
     * @return la vue
     * @throws IOException en cas d'erreur lors de la génération de l'image
     * encodée
     */
    public static SynViewJpg buildFromResource(String resourceUrl) throws IOException {
        return new SynViewJpg(SynViewJpg.class.getResourceAsStream(resourceUrl).readAllBytes());
    }
    /**
     * crée une image à partir d'une resource (utilise le classloader de la classe donnée en paramètre)
     *
     * @param resourceUrl l'url d'une resource image jpg
     * @return la vue
     * @throws IOException en cas d'erreur lors de la génération de l'image
     * encodée
     */
    public static SynViewJpg buildFromResource(Class clazz,String resourceUrl) throws IOException {
        System.err.println("A: "+clazz.getResource("/com/cachat/prj/trafxpark/gui/data/barriereOuverte.svg"));
        System.err.println("B: "+(resourceUrl));
        System.err.println("C: "+clazz.getResource(""));
        System.err.println("D: "+clazz.getResource(resourceUrl));
        return new SynViewJpg(clazz.getResourceAsStream(resourceUrl).readAllBytes());
    }

    /**
     * crée une image
     *
     * @param content l'image
     * @throws IOException en cas d'erreur lors de la génération de l'image
     * encodée
     */
    public static SynViewJpg buildFromImage(RenderedImage content) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(content, "jpg", bos);
            bos.flush();
            return new SynViewJpg(bos.toByteArray());
        }
    }

}
