package com.cachat.prj.echo3.synoptique;

import com.cachat.prj.echo3.base.BaseAppServlet;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * gestionnaire des vues
 *
 * @author scachat
 */
public class SynManager {

    private static final Logger logger = Logger.getLogger(SynManager.class.getSimpleName());
    /**
     * singleton
     */
    private static SynManager instance = new SynManager();

    private SynManager() {
    }

    public static SynManager getInstance() {
        return instance;
    }
    /**
     * generateur d'uid
     */
    private static long nextId = (long) (1 + (10000 * Math.random()));

    /**
     * genere un nouvel uid synoptique
     */
    public static synchronized String newUid() {
        return String.format("S_%d", nextId++);
    }

    /**
     * donne le synoptique (attaché à la session http en cours)
     *
     * @param uid l'uid du synoptique
     * @return le synoptique ou null
     */
    public Synoptique getSynoptique(String uid) {
        HttpSession s = BaseAppServlet.getSession();
        return getSynoptique(s, uid);
    }

    /**
     * donne le synoptique (attaché à la session http en cours)
     *
     * @param s la session http
     * @param uid l'uid du synoptique
     * @return le synoptique ou null
     */
    public Synoptique getSynoptique(HttpSession s, String uid) {
        if (s == null) {
            return null;
        }
        Map<String, Synoptique> synoptiques = (Map<String, Synoptique>) s.getAttribute("synoptiques_fabric");
        if (synoptiques == null) {
            return null;
        }
        return synoptiques.get(uid);
    }

    /**
     * enregistre un synoptique
     *
     * @param synoptique le synoptique
     */
    public void register(Synoptique synoptique) {
        HttpSession s = BaseAppServlet.getSession();
        if (s == null) {
            logger.log(Level.SEVERE, "Refus de l''enregistrement du synoptique {0} car il n''y a pas de session http", synoptique.getRenderId());
            return;
        }
        Map<String, Synoptique> synoptiques = (Map<String, Synoptique>) s.getAttribute("synoptiques_fabric");
        if (synoptiques == null) {
            synoptiques = new HashMap<>();
            s.setAttribute("synoptiques_fabric", synoptiques);
        }
        synoptiques.put("C."+synoptique.getRenderId(), synoptique);

    }

    /**
     * libere un synoptique
     *
     * @param synoptique le synoptique
     */
    public void unregister(Synoptique synoptique) {
        HttpSession s = BaseAppServlet.getSession();
        if (s == null) {
            return;
        }
        Map<String, Synoptique> synoptiques = (Map<String, Synoptique>) s.getAttribute("synoptiques_fabric");
        if (synoptiques == null) {
            return;
        }
        synoptiques.remove(synoptique.getRenderId());
    }

    /**
     * donne le contenu de la vue correspondante
     *
     * @param synoptiqueUid
     * @param objectUid
     * @return
     */
    public byte[] getViewContent(String synoptiqueUid, String objectUid) {
        Synoptique synoptique = getSynoptique(synoptiqueUid);
        if (synoptique != null) {
            SynObject object = synoptique.getObject(objectUid);
            if (object != null) {
                SynView view = object.getView();
                if (view != null) {
                    return view.getContent();
                }
            }
        }
        return null;
    }
}
