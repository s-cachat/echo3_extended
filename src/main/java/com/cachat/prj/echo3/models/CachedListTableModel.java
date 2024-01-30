package com.cachat.prj.echo3.models;

import com.cachat.prj.echo3.base.BaseApp;
import static com.cachat.prj.echo3.models.ListTableModel.logger;
import java.util.*;
import jakarta.persistence.*;
import com.cachat.util.*;
import java.util.logging.Level;

/**
 * le modele de la table de liste
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>
 * Copyright 2003 SST Informatique
 * @param <TypeObjet> le type d'équipement
 */
public abstract class CachedListTableModel<TypeObjet> extends ListTableModel<TypeObjet> {

    /**
     * le type des objets. null en mode cache total, TypeObjet en mode
     * progressif
     */
    protected final Class type;
    /**
     * le nom de la propriété id des objets en mode progressif, null en mode
     * total.
     */
    protected final String idProperty;
    /**
     * la liste des libelles
     */
    protected List<Object[]> libs = new ArrayList<>();

    /**
     * constructeur. Le mode est "cache total" : on calcule l'ensemble des
     * cellule après chaque requete
     *
     * @param app l'application
     */
    public CachedListTableModel(BaseApp app) {
        super(app);
        type = null;
        idProperty = null;
    }

    /**
     * Constructeur. Le mode est cache progressif. On calcule chaque ligne au
     * moment ou elle est demandée.Ceci implique que pour chaque calcul on
     * raffraichisse la valeur.
     *
     * @param app l'application
     * @param type le type des objets
     * @param idProperty le nom de la propriété id des objets
     */
    public CachedListTableModel(BaseApp app, Class<TypeObjet> type, String idProperty) {
        super(app);
        this.type = type;
        this.idProperty = idProperty;
    }

    /**
     * donne le nombre de lignes
     *
     * @return le nombre de lignes
     */
    @Override
    public int getRowCount() {
        return libs.size();
    }

    /**
     * met a jour la liste des objets.
     *
     * @param where le complement de clause where
     * @param arg les arguments correspondant aux ? du where
     */
    @Override
    public abstract void update(String where, List<Object> arg);

    /**
     * genere le cache. Doit etre appelle manuellement dans update, avant la
     * fermeture de l'entity manager
     */
    protected synchronized void updateCache() {

        if (type == null) {//mode total
//            logger.finest(String.format("CLTM on cree un cache de %d col par %d row depuis %s", getColumnCount(), data.size(),
//                    Debug.showWhere()));
            libs.clear();
            Class clazz = null;
            for (int j = 0; j < data.size(); j++) {

                Object o = data.get(j);
                if (o != null) {
                    if (clazz == null || !clazz.isAssignableFrom(o.getClass())) {
                        clazz = o.getClass();
                    }
                }
                Object[] cols = new Object[getColumnCount()];
                for (int i = 0; i < getColumnCount(); i++) {
                    cols[i] = computeValueAt(i, j);
                }
                libs.add(cols);
            }
            logger.finest(String.format("CLTM on a %d libelles, et une class %s", libs.size(), clazz == null ? null : clazz.getName()));
        } else {//mode progressif
            logger.finest(String.format("CLTM on vide le cache, il sera rempli ligne par ligne"));
            libs.clear();
            for (int j = 0; j < data.size(); j++) {
                libs.add(new Object[0]);
            }
        }
    }

    /**
     * genere le cache pour une ligne, si nécessaire. Méthode sans effet en mode
     * total
     */
    protected synchronized void updateCache(int row) {
        if (type == null) {
            return;
        }
        if (libs.get(row).length == 0) {
            logger.finest(String.format("CLTM on cree le cache de la ligne %d", row));
            try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())) {
                TypeObjet v = (TypeObjet) em.find(type, BeanTools.getRaw(data.get(row), idProperty));
                data.set(row, v);
                Object[] cols = new Object[getColumnCount()];
                for (int i = 0; i < getColumnCount(); i++) {
                    cols[i] = computeValueAt(i, row);
                }
                libs.set(row, cols);
            }
        }
    }

    /**
     * calcule la valeur d'une cellule pour le cache
     *
     * @param col la colonne;
     * @param row la ligne;
     */
    public abstract Object computeValueAt(int col, int row);

    /**
     * donne une valeur d'une cellule en utilisant le cache
     *
     * @param col la colonne;
     * @param row la ligne;
     */
    @Override
    public final Object getValueAt(int col, int row) {
        updateCache(row);
        return libs.get(row)[col];
    }

    /**
     * cree et execute une requete
     *
     * @param req la requete
     * @param arg les arguments correspondant aux ? du where
     * @return la query
     */
    @Override
    protected List<TypeObjet> executeQuery(String req, List<Object> arg) {
        EntityManager em = EntityManagerUtil.getEntityManager(app.getEntityManagerName());
        try {
            data = (List<TypeObjet>) createQuery(em, req, arg).getResultList();
            updateCache();

        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder();
            if (arg != null) {
                arg.stream().forEach((a) -> sb.append("\"").append(arg).append("\", "));
            }
            logger.log(Level.SEVERE, "IllegalArgument : \"{0}\", args={1}", new Object[]{req, sb});
            throw e;
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, null, e);
            throw e;
        } finally {
            em.close();
        }
        return data;
    }

    /**
     * cree et execute une requete
     *
     * @param req la requete
     * @param arg les arguments correspondant aux ? du where
     * @param maxItems le nombre max d'éléments a charger
     * @return la query
     */
    protected List<TypeObjet> executeQuery(String req, List<Object> arg, int maxItems) {
        try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())){
            data = (List<TypeObjet>) createQuery(em, req, arg).setMaxResults(maxItems).getResultList();
            updateCache();
        }
        return data;
    }

    /**
     * cree et execute une requete de décompte
     *
     * @param req la requete
     * @param arg les arguments correspondant aux ? du where
     * @param maxItems le nombre max d'éléments a charger
     * @return la query
     */
    protected int executeCountQuery(String req, List<Object> arg, int maxItems) {
        try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())){
            Number n = (Number) createQuery(em, req, arg).getSingleResult();
            return n == null ? 0 : n.intValue();
        }
    }

}
