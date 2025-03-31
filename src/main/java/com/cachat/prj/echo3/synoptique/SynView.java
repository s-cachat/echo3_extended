package com.cachat.prj.echo3.synoptique;

/**
 * le visuel d'un objet
 *
 * @author scachat
 */
public class SynView {

    /**
     * le content type
     */
    protected String contentType;
    /**
     * le contenu brut
     */
    protected byte[] content;

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
