package com.cachat.prj.echo3.ng.menu;

import com.cachat.prj.echo3.base.BasicWindow;
import com.cachat.prj.echo3.interfaces.User;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * base pour un menu manager
 *
 * @author scachat
 */
public abstract class BaseMenuManager {

    /**
     * notre logger
     */
    public static final Logger logger = Logger.getLogger(BaseMenuManager.class.getSimpleName());
    /**
     * la racine
     */
    protected MenuRoot root;
    /**
     * la map des pages vers le menu correspondant
     */
    private final Map<Class<? extends BasicWindow>, com.cachat.prj.echo3.ng.menu.MenuItem> menuReverseCache = new HashMap<>();

    /**
     * indexe tous les éléments du menu
     *
     * @param x le point de départ de l'indexation
     */
    protected void makeIndex(MenuElement mel) {
        if (mel instanceof MenuItem mi) {
            if (mi.getNewPane() != null && !mi.getNewPane().isBlank()) {
                try {
                    Class<? extends BasicWindow> cla = (Class<? extends BasicWindow>) Class.forName(mi.getNewPane());
                    menuReverseCache.put(cla, mi);
                } catch (ClassNotFoundException ex) {
                    logger.log(Level.SEVERE, (String) "Impossible de trouver la BasicWindow " + mi.getNewPane(), ex);
                }
            }
        }
        if (mel instanceof SubMenu sm) {
            for (MenuElement me : sm.getChilds()) {
                makeIndex(me);
                me.setParent(sm);
            }
        }
    }

    /**
     * charge et indexe si nécessaire les menus, et renvoie la racine
     *
     * @param in le flux d'ou lire les menus
     * @return la racine
     */
    public MenuRoot getXmlRoot(Supplier<InputStream> inSupplier) {
        if (root == null) {
            try {
                JAXBContext ctx = JAXBContext.newInstance(MenuRoot.class.getPackage().getName());
                MenuRoot x = (MenuRoot) ctx.createUnmarshaller().unmarshal(inSupplier.get());
                makeIndex(x);
                return x;
            } catch (JAXBException ex) {
                Logger.getLogger(BaseMenuManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return root;
    }

    /**
     * donne le menu correspondant à la fenetre
     *
     * @param w la fenetre
     * @return le menu
     */
    public MenuItem getMenu(BasicWindow w) {
        return menuReverseCache.get(w.getClass());
    }

    /**
     * teste si un utilisateur peut avoir accès à un menu
     *
     * @param mel le menu
     * @param user l'utilisateur
     * @param superAdmin true si l'utilisateur est super admin
     * @return true si il a accès au menu
     */
    public abstract boolean canUse(MenuElement mel, User user, boolean superAdmin);

}
