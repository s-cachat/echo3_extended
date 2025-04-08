package com.cachat.prj.echo3.synoptique;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

/**
 * le visuel d'un objet. Le visuel peut être partagé entre plusieurs synoptiques
 * ou utilisateurs.
 *
 * @author scachat
 */
@XmlSeeAlso({SynViewJpg.class, SynViewPng.class, SynViewSvg.class, SynViewBasic.class})
@XmlAccessorType(XmlAccessType.FIELD)
public class SynView {

    /**
     * l'uid de la vue
     */
    private String uid = SynManager.newUid();
    /**
     * le content type
     */
    protected String contentType;
    /**
     * le contenu brut
     */
    @XmlTransient
    protected byte[] content;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
