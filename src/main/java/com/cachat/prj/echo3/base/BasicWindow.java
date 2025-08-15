package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.ng.ContentPaneEx;
import com.cachat.util.StringUtil;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.WindowPane;

/**
 * Fenetre generique
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003 SST Informatique
 */
public abstract class BasicWindow extends WindowPane implements LocalisedItem {

    /**
     * Extent de largeur totale (100%)
     */
    public static final Extent FULL_WIDTH = new Extent(99, Extent.PERCENT);

    /**
     * Drapeau : débogage des propriètes
     */
    public static final boolean DEBUG = true;

    /**
     * Logger local
     */
    protected static final transient Logger logger = Logger.getLogger("com.cachat.prj.wir.gui");

    /**
     * L'application
     */
    protected final BaseApp app;

    /**
     * le container
     */
    protected ContentPaneEx contentPane;

    /**
     * Le titre principale
     */
    protected String titre1;

    /**
     * Le titre secondaire
     */
    protected String titre2;

    /**
     * Le domaine de la fenêtre
     */
    protected String domaine;

    /**
     * Le préfixe pour cette classe, utilisee pour les ressources, avec un point
     * final.
     */
    private String prefixe;

    /**
     * les ressources (avec préfixe supprime)
     */
    private ResourceBundle localResource = null;

    /**
     * Donne l'application associée
     *
     * @return l'application
     */
    public BaseApp getApp() {
        return app;
    }

    /**
     * Donne le préfixe des icônes
     *
     * @return le préfixe
     */
    public String getPrefixe() {
        return prefixe;
    }

    /**
     * Donne une ressource
     *
     * @param n le nom sans le préfixe qui sera ajoute
     * @return le message
     */
    @Override
    public final String getString(String n) {
        return app.getString(prefixe + n);
    }

    /**
     * Donne une ressource
     *
     * @param n le nom sans le préfixe qui sera ajoute
     * @return la chaine si elle est définie ou null sinon
     */
    public final String getString2(String n) {
        return app.getString2(prefixe + n);
    }

    /**
     * Donne une ressource
     *
     * @param n le nom sans le préfixe qui ne sera pas ajoute
     * @return la ressource
     */
    @Override
    public final String getBaseString(String n) {
        return app.getString(n);
    }

    @Override
    public Locale getLocale() {
        Locale l = app.getLocale();
        return app.getLocale();
    }

    /**
     * Donne les ressources locales
     *
     * @return les ressources
     */
    public synchronized ResourceBundle getLocalResource() {
        if (localResource == null) {
            localResource = new ResourceBundle() {
                @Override
                public String handleGetObject(String s) {
                    return BasicWindow.this.getString(s);
                }

                @Override
                public Enumeration<String> getKeys() {
                    return BasicWindow.this.app.getKeys();
                }
            };
        }
        return localResource;
    }

    /**
     * Donne les ressources globales
     *
     * @return les ressources
     */
    public synchronized ResourceBundle getBaseResource() {
        return app.getResource();
    }

    /**
     * Constructeur simplifié. La taille de la fenêtre est fixée à 800,600
     * arbitrairement (en fait elle n'est utilisée que pour les fenêtres
     * modales) Le domaine est défini à "" (il n'est pas utilisé dans les styles
     * actuels, mais il pourrait l'être pour certaines applications)
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre, utilisé pour récupérer les
     * chaines de caractères dont le titre (.title)
     */
    public BasicWindow(BaseApp app, String prefixe) {
        this(app, prefixe, "", new Extent(800), new Extent(600));
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associ?)
     * @param w la largeur de la fenetre en pixel
     * @param h la hauteur de la fenetre en pixel
     */
    public BasicWindow(BaseApp app, String prefixe, String domaine, int w, int h) {
        this(app, prefixe, domaine, new Extent(w), new Extent(h));
    }

    /**
     * Constructeur
     *
     * @param app l'application
     * @param prefixe le prefixe de la fenetre
     * @param domaine le code domaine de la fenetre (pour affichage du visuel
     * associ?)
     * @param w la largeur de la fenetre
     * @param h la hauteur de la fenetre
     */
    public BasicWindow(BaseApp app, String prefixe, String domaine, Extent w, Extent h) {
        super("", w, h);
        this.app = (BaseApp) (app == null ? ApplicationInstance.getActive() : app);
        this.prefixe = prefixe + ".";
        this.domaine = domaine;

        setTitle(getString("title"));
        setTitre1(getString("title1"));
        setTitre2(getString("title"));

        setStyleName("DefaultW");
        contentPane = new ContentPaneEx();
        super.add(contentPane);
    }

    /**
     * La fenêtre racine
     */
    protected Component parent;

    /**
     * Donne la fenêtre racine pour les popup
     *
     * @return la fenêtre racine
     */
    public Component getRootWindow() {
        return parent == null ? getParent() : parent;
    }

    /**
     * Fixe la fenêtre racine pour les popup
     *
     * @param parent la fenêtre racine
     */
    public void setParent(Component parent) {
        this.parent = parent;
    }

    /**
     * Donne le conteneur du contenu principal (supprime ce dernier de la
     * fenêtre définitivement)
     *
     * @return le conteneur du contenu
     */
    public Component getContentPane() {
        super.remove(contentPane);
        return contentPane;
    }

    /**
     * Ajoute un composant
     *
     * @param comp le composant
     */
    @Override
    public void add(Component comp) {
        contentPane.add(comp);
    }

    /**
     * Supprime un composant
     *
     * @param comp le composant
     */
    @Override
    public void remove(Component comp) {
        contentPane.remove(comp);
    }

    /**
     * Vide la fenêtre
     */
    @Override
    public void removeAll() {
        contentPane.removeAll();
    }

    /**
     * Get the value of titre1
     *
     * @return the value of titre1
     */
    public String getTitre1() {
        return titre1;
    }

    /**
     * Set the value of titre1
     *
     * @param titre1 new value of titre1
     */
    public void setTitre1(String titre1) {
        this.titre1 = titre1;
    }

    /**
     * Get the value of titre2
     *
     * @return the value of titre2
     */
    public String getTitre2() {
        return titre2;
    }

    /**
     * Set the value of titre2
     *
     * @param titre2 new value of titre2
     */
    public void setTitre2(String titre2) {
        this.titre2 = titre2;
    }

    @Override
    public void setTitle(String newValue) {
        super.setTitle(newValue);
        setTitre1(newValue);
        setTitre2(newValue);
    }

    /**
     * Fixe le titre
     *
     * @param newValue Le nouveau titre, à formatter
     * @param arg Les arguments pour le formattage
     */
    public void setTitle(String newValue, Object... arg) {
        try {
            String x = String.format(newValue, arg);
            setTitle(x);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Setting title to \"%s\" : error", t);
            super.setTitle("Translation error");
        }
    }

    /**
     * Get the value of domaine
     *
     * @return the value of domaine
     */
    public String getDomaine() {
        return domaine;
    }

    /**
     * Set the value of domaine
     *
     * @param domaine new value of domaine
     */
    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    /**
     * Affiche un popup modal avec un message d'erreur
     *
     * @param ex l'exception à afficher
     */
    public void popupError(Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getMessage()).append("\n\n");
        sb.append(StringUtil.valueOf(ex));
        MessagePane mp = new MessagePane(app, getString("errorTitle"), getDomaine(), sb.toString(), MessagePane.Icon.ERROR, new Extent(600,
                Extent.PX), new Extent(400, Extent.PX), true);

        app.addWindow(mp, null);
    }

    @Override
    public String toString() {
        return "BasicWindow{" + "titre1=" + titre1 + ", prefixe=" + prefixe + '}';
    }

}
