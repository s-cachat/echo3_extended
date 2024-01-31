// Informagen =================================================================================
// Insure that the 'Informagen' namespace exists

if (!Core.get(window, ["Informagen"])) {
    Core.set(window, ["Informagen"], {});
}


// Informagen.SyncTextField ===================================================================

Informagen.SyncTextField = Core.extend(Echo.TextField, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.SyncTextField", this);
    },

    componentType : "Informagen.SyncTextField"

});


// Informagen.SyncTextField.Peer ==============================================================

Informagen.SyncTextField.Peer = Core.extend(Echo.Sync.TextField, {

    $load : function() {
        Echo.Render.registerPeer("Informagen.SyncTextField", this);
    },

    $construct : function() {
        Echo.Sync.TextField.call(this);
        
        // Here we apply a "JS-hack" in order to invoke a superclass private method...
        // This is a bad practice in general, but this way we can reuse lots of existing code.
        // Hopefully someday _processKeyUp will be a virtual method...
        
//          this._textFieldProcessKeyUp = this._processKeyUp;
//         this._processKeyUp = this.processKeyUp;
    },

    _syncDelay: 0,
    
    
    

    renderUpdate: function(update) {

        var updateProperty = null;

        // Check for 'syncDelay' non-style property changes
        
        updateProperty = update.getUpdatedProperty("syncDelay");
        if (updateProperty)
           this._syncDelay = updateProperty.newValue;
         
        // Invoke the superclass method for all other methods
        return Echo.Sync.TextField.prototype.renderUpdate.call(this, update);      
        
    },


    clientKeyDown : function(keyEvent) {

        // Invoke superclass method to handle valid keyEvents
        var status = Echo.Sync.TextField.prototype.clientKeyDown.call(this, keyEvent);
        
        // A keyCode of 13 (return) is handled in the previous line; Here start a timer
        //  which will send a action command to the server
        
        if (keyEvent.keyCode != 13) {
            
            if (this._syncDelay > 0) 
                this.scheduleSync(this._syncDelay);
            else
                this.executeSync();
        }
        
        return status;
    },


    scheduleSync : function(delay) {

        // Cancel pending sync before a new sync is scheduled, so only the most recent 
        //   sync is executed while someone is typing rapidly or if using a long delay
        
        this.cancelPendingSync();
        this.pendingSync = Core.Web.Scheduler.run(Core.method(this, this.executeSync), delay, false);
    },


    cancelPendingSync : function() {
        if (this.pendingSync !== undefined) {
                Core.Web.Scheduler.remove(this.pendingSync);
                delete this.pendingSync;
        }
    },

    // Invoke the ActionListener on the server-side
    executeSync : function() {
        this.component.doAction();
    }
});