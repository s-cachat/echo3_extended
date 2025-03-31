package com.cachat.prj.echo3.synoptique;

import java.util.ArrayList;
import java.util.List;

/**
 * une action a faire sur le synoptique
 *
 * @author scachat
 */
public class SynAction {

    /**
     * objet a ajouter
     */
    private List<SynObject> add;
    /**
     * objet a modifier
     */
    private List<SynObject> update;
    /**
     * objet a supprimer
     */
    private List<String> del;

    public List<SynObject> getAdd() {
        if (add == null) {
            add = new ArrayList<>();
        }
        return add;
    }

    public List<SynObject> getUpdate() {
        if (update == null) {
            update = new ArrayList<>();
        }
        return update;
    }

    public List<String> getDel() {
        if (del == null) {
            del = new ArrayList<>();
        }
        return del;
    }

}
