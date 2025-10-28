/*
 * (c) 2025 Stéphane Cachat stephane@cachat.com. No reuse or distribution allowed. Réutilisation ou redistribution interdite.
 */
package com.cachat.prj.echo3.interfaces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.ResourceImageReference;
import nextapp.echo.app.Style;
import nextapp.echo.app.StyleSheet;
import nextapp.echo.app.serial.SerialException;
import nextapp.echo.app.serial.StyleSheetLoader;

/**
 * implémentation par défaut d'une feuille de style
 *
 * @author scachat
 */
public abstract class AbstractStyles implements Styles {

    /**
     * notre logger
     */
    protected static final Logger logger = Logger.getLogger("com.cachat.echo3.style");

    /**
     * le cache des références aux icones
     */
    protected final Map<String, ImageReference> cacheIcon = new HashMap<>();
    /**
     * le cache des références aux images
     */
    protected final Map<String, ImageReference> cacheImage = new HashMap<>();
    /**
     * la feuille de style
     */
    protected StyleSheet defaultStyleSheet;

    /**
     * donne la feuille de style par défaut. Si un fichier de debug
     * /tmp/base.stylesheet existe, utilise ce fichier, sinon recherche dans le
     * classpath la ressource base.stylesheet dans le répertoire donné par
     * getStylePath().
     *
     * @return la feuille de style
     */
    @Override
    public StyleSheet getDefaultStyleSheet() {
        try {
            File f = new File("/tmp/base.stylesheet");
            if (f.exists()) {
                logger.log(Level.WARNING, "Loading debug stylesheet {0}", f);
                defaultStyleSheet = StyleSheetLoader.load(new FileInputStream(f), Thread.currentThread().getContextClassLoader());
            } else if (defaultStyleSheet == null) {
                defaultStyleSheet = StyleSheetLoader.load(getStylePath() + "base.stylesheet", Thread.currentThread().getContextClassLoader());
            }
            return new StyleSheet() {
                @Override
                public Iterator getStyleNames() {
                    return defaultStyleSheet.getStyleNames();
                }

                @Override
                public Iterator getComponentTypes(String styleName) {
                    return defaultStyleSheet.getComponentTypes(styleName);
                }

                @Override
                public Style getStyle(String styleName, Class componentClass, boolean searchSuperClasses) {
                    Class styleClass = componentClass;
                    Style style = defaultStyleSheet.getStyle(styleName, styleClass, false);
//                    while (style == null && styleClass != Component.class) {
//                        styleClass = styleClass.getSuperclass();
//                        style = defaultStyleSheet.getStyle(styleName, styleClass, false);
//                    }
                    if (style == null) {
                        logger.log(Level.SEVERE, "MISSING style \"{0}\" for \"{1}\"", new Object[]{styleName, componentClass.getName()});
                    }
                    return style;
                }

                @Override
                public Extent getExtentConstant(String name) {
                    return defaultStyleSheet.getExtentConstant(name);
                }

            };
        } catch (SerialException | FileNotFoundException ex) {
            logger.log(Level.SEVERE, "Erreur lors du chargement du style", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * donne l'icone. Cherche d'abord une icone dans le classpath avec
     * l'extension/format svg (utilise getIconPattern() en remplacant les 3
     * derniers caractères par svg), et si non trouvé retente avec
     * l'extension/format png.
     *
     * @param name le nom de l'icone
     * @return l'icone, ou l'icone debug si il n'existe pas
     */
    @Override
    public ImageReference getIcon(String name) {
        ImageReference ir = cacheIcon.get(name);
        if (ir == null) {
            String refPath = String.format(getIconPattern(), name);
            String path = refPath.substring(0, refPath.length() - 3) + "svg";
            if (getClass().getResourceAsStream(path) == null) {
                path = refPath.substring(0, refPath.length() - 3) + "png";
            }
            if (getClass().getResourceAsStream(path) == null) {
                logger.severe(String.format("MISSING ICON \"%s\"", name));
                if ("debug".equals(name)) {
                    return null;
                } else {
                    return getIcon("debug");
                }
            }
            ir = new ResourceImageReference(path);
            cacheIcon.put(name, ir);
        }
        return ir;
    }

    /**
     * donne l'image demandée
     *
     * @param name le nom de l'image
     * @return l'image, ou l'icone debug si elle n'existe pas
     */
    @Override
    public ImageReference getImage(String name) {
        ImageReference ir = cacheImage.get(name);
        if (ir == null) {
            String path = String.format("%s/%s", getImagePath(), name);
            if (getClass().getResourceAsStream(path) == null) {
                logger.severe(String.format("MISSING IMAGE \"%s\"", path));
                return getIcon("debug");
            }
            ir = new ResourceImageReference(path);
            cacheImage.put(name, ir);
        }
        return ir;
    }

    /**
     * donne l'image demandée
     *
     * @param name le nom de l'image
     * @return l'image, ou null si elle n'existe pas
     */
    @Override
    public ImageReference getImage2(String name) {
        if (getClass().getResourceAsStream(String.format("%s/%s", getImagePath(), name)) == null) {
            return null;
        } else {
            return getImage(name);
        }
    }

}
