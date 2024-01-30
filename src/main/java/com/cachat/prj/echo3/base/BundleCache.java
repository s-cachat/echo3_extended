package com.cachat.prj.echo3.base;

import static com.cachat.prj.echo3.base.BaseApp.logger;
import java.lang.module.ModuleDescriptor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Cache pour les bundles. Pour charger les resources locales, cette classe doit
 * être surchargée par une classe du projet.
 *
 * @author scachat
 */
public abstract class BundleCache {

    /**
     * le cache des bundles
     */
    private final Map<Locale, ResourceBundle> resCache = new HashMap<>();

    /**
     * donne une ressource pour une langue donnée
     *
     * @param loc la locale demandée
     * @return les ressources
     */
    public ResourceBundle getResource(Locale loc) {
        ResourceBundle rb = resCache.get(loc);
        if (rb != null) {
            return resCache.get(loc);
        } else {
            synchronized (resCache) {
                rb = resCache.get(loc);//on le refait de façon synchrone
                if (rb == null) {
                    if (loc != null && "**".equals(loc.getLanguage())) {
                        rb = new ResourceBundle() {
                            @Override
                            protected Object handleGetObject(String key) {
                                return key;
                            }

                            @Override
                            public Enumeration<String> getKeys() {
                                return (new Vector<String>()).elements();
                            }
                        };
                    } else 
                        try {
                        final String resourceName = "/com/cachat/prj/echo3/editor";
                        rb = new MyResourceBundle(getRawResource(resourceName, loc));
                    } catch (MissingResourceException e) {
                        logger.log(Level.SEVERE, "", e);
                        java.lang.Module myClassModule = BundleCache.class.getModule();
                        final ModuleDescriptor descriptor = myClassModule.getDescriptor();
                        if (descriptor != null) {
                            logger.info("BundleCache module name : " + descriptor.toNameAndVersion());
                            descriptor.provides().forEach(p -> logger.info("BundleCache module provides : " + p.service()));
                            descriptor.requires().forEach(p -> logger.info("BundleCache module requires : " + p.name()));
                            descriptor.uses().forEach(p -> logger.info("BundleCache module uses : " + p));
                        } else {
                            logger.info("BundleCache module with null descriptor : " + myClassModule.getName());
                        }
                        rb = new ResourceBundle() {
                            @Override
                            protected Object handleGetObject(String key) {
                                return '<'+key+'>';
                            }

                            @Override
                            public Enumeration<String> getKeys() {
                                return (new Vector<String>()).elements();
                            }
                        };
                    }
                    resCache.put(loc, rb);
                }
                return rb;
            }

        }
    }
/**
 * méthode proxy pour charger des resources locales au module. 
 * ResourceBundle.getBundle(resourceName, loc)
 * @param resourceName le nom de la resource
 * @param loc la locale
 * @return la resource
 */
    public abstract ResourceBundle getRawResource(final String resourceName, Locale loc) ;

    /**
     * ResourceBundle permissif
     */
    protected static class MyResourceBundle extends ResourceBundle {

        /**
         * Constructeur
         *
         * @param bundle le bundle a encapsuler
         */
        public MyResourceBundle(ResourceBundle bundle) {
            logger.log(Level.SEVERE, "LOCALE : App.MyResourceBundle({0})", bundle.getLocale());
            myRes = bundle;
        }
        /**
         * les resources sous jacentes
         */
        private ResourceBundle myRes;

        /**
         * gere l'obtention d'une cle
         */
        @Override
        public String handleGetObject(String s) {
            try {
                return myRes.getString(s);
            } catch (MissingResourceException e) {
                return s;
            }
        }

        /**
         * donne la locale de cette ressource
         */
        @Override
        public Locale getLocale() {
            return myRes.getLocale();//loc;//don't use underlying locale (error on default)
        }

        /**
         * donne toutes les cl�s
         *
         * @return
         */
        @Override
        public Enumeration<String> getKeys() {
            return myRes.getKeys();
        }
    };

}
