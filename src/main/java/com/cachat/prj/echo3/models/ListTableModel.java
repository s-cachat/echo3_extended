package com.cachat.prj.echo3.models;

import com.cachat.prj.echo3.base.BaseApp;
import java.util.*;
import jakarta.persistence.*;
import com.cachat.util.*;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nextapp.echo.app.table.AbstractTableModel;

/**
 * le modele de la table de liste
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * 
 * Copyright 2003 SST Informatique
 */
public abstract class ListTableModel<TypeObjet> extends AbstractTableModel {

    /**
     * l'application
     */
    protected BaseApp app;

    public ListTableModel(BaseApp app) {
        this.app = app;
    }
    /**
     * Logger local
     */
    protected static final transient Logger logger = Logger.getLogger("com.cachat.prj.wir.gui");
//   /**
//    * donne l'objet a la ligne demandee
//    */
//   public Object getItem(int row){
//      return data.get(row);
//   }

    /**
     * donne le nombre de lignes
     */
    @Override
    public int getRowCount() {
        return data == null ? 0 : data.size();
    }
    /**
     * la liste des objets
     */
    protected List<TypeObjet> data;

    /**
     * met a jour la liste des objets.
     *
     * @param where le complement de clause where
     * @param arg les arguments correspondant aux ? du where
     */
    public abstract void update(String where, List<Object> arg);

    /**
     * cree et execute une requete
     *
     * @param req la requete
     * @param arg les arguments correspondant aux ? du where
     * @return la query
     */
    protected List<TypeObjet> executeQuery(String req, List<Object> arg) {
        EntityManager em = EntityManagerUtil.getEntityManager(app.getEntityManagerName());
        List<TypeObjet> d = (List<TypeObjet>) createQuery(em, req, arg).getResultList();
        em.close();
        return d;
    }
    private static Pattern qm = Pattern.compile("\\?");

    /**
     * cree une requete
     *
     * @param req la requete
     * @param arg les arguments correspondant aux ? du where
     * @param em l'entitymanager
     * @return la query
     */
    protected Query createQuery(EntityManager em, String req, List<Object> arg) {
        logger.fine(String.format("Requete \"%s\" avec les parametres \"%s\"", req, arg));
        StringBuffer sb = new StringBuffer();
        int n = 1;
        Matcher m = qm.matcher(req);
        while (m.find()) {
            m.appendReplacement(sb, ":arg" + (n++));
        }
        m.appendTail(sb);
        req = sb.toString();
        Query q = em.createQuery(req);
        for (int i = 0; i < arg.size(); i++) {
            logger.log(Level.WARNING, "set param {0}{1} to {2}", new Object[]{i, 1, arg.get(i)});
            q.setParameter("arg" + (i + 1), arg.get(i));
        }
        return q;
    }
}
