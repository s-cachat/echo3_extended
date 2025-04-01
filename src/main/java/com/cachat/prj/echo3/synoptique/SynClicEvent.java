package com.cachat.prj.echo3.synoptique;

/**
 *
 * @author scachat
 */
public class SynClicEvent {

    /**
     * l'identifiant unique de l'objet modifié
     */
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "SynClicEvent{" + "uid=" + uid + '}';
    }

}
