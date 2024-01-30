package org.informagen.echo.app;

import nextapp.echo.app.text.Document;
import nextapp.echo.app.TextField;

import nextapp.echo.webcontainer.sync.component.TextComponentPeer;

public class SyncTextField extends TextField {
        
    public static final String PROPERTY_SYNC_DELAY = "syncDelay";

    private int delayInMillis = 0;

    // Constructors ----------------------------------------------------------------------------
    public SyncTextField() {
        super();
    }

    public SyncTextField(Document document, String text, int columns) {
        super(document, text, columns);
    }

    public SyncTextField(Document document) {
        super(document);
    }
    
    /**
     * Sets the delay before the text is synchronized between client and server.
     * 
     * @param delay delay in milliseconds
     */

    public void setSyncDelay(int delayInMillis) {
        int oldValue = this.delayInMillis;
        this.delayInMillis = delayInMillis;

        if(delayInMillis < 0)
            this.set(TextComponentPeer.PROPERTY_SYNC_MODE, Integer.valueOf(TextComponentPeer.SYNC_ON_ACTION));
        else {
            this.set(TextComponentPeer.PROPERTY_SYNC_MODE, Integer.valueOf(TextComponentPeer.SYNC_ON_CHANGE));
//            this.set(TextComponentPeer.PROPERTY_SYNC_INITIAL_DELAY, Integer.valueOf(delayInMillis));
//            this.set(TextComponentPeer.PROPERTY_SYNC_DELAY, Integer.valueOf(delayInMillis));
//            firePropertyChange(TextComponentPeer.PROPERTY_SYNC_DELAY, oldValue, delayInMillis);
        }
 }

    public int getSyncDelay() {
        return delayInMillis;
    }

}