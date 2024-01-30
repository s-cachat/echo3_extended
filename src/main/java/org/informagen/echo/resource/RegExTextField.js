// Informagen =================================================================================
// Ensure that the 'Informagen' and 'Informagen.Sync' namespaces exist

if (!Core.get(window, ["Informagen", "Sync"])) {
    Core.set(window, ["Informagen", "Sync"], {});
}


// Informagen.RegExTextField ==================================================================

Informagen.RegExTextField = Core.extend(Echo.TextComponent, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.RegExTextField", this);
    },

    componentType : "Informagen.RegExTextField"
});


// Informagen.Sync.RegExTextField ===========================================================
//  The name of this object is irrelevant; see '$load' function.  However, keeping this 
//    class as a member of the namespace 'Informagen.Sync' adds consistency.


Informagen.Sync.RegExTextField = Core.extend(Informagen.Sync.ActiveTextField, {

    // Register this class as the 'Sync' class for the 'Informagen.RegExTextField' class

    $load: function() {
        Echo.Render.registerPeer("Informagen.RegExTextField", this);
    },

    _filterRegExp: new RegExp("."),
    _patternRegExp: new RegExp("."),
    
    renderAdd : function(update, parentElement) {

        var property = this.component.render("characterFilter");
        if(property != null)
            this._filterRegExp = new RegExp(property);
            
        property = this.component.render("regexPattern");
        if(property != null)
            this._patternRegExp = new RegExp(property);

        Informagen.Sync.ActiveTextField.prototype.renderAdd.call(this, update, parentElement);      
        this._adjustStatus();
    },


    renderUpdate: function(update) {

        var updateProperty = null;

        // Check for 'RegExTextfield' non-style property changes
        
        updateProperty = update.getUpdatedProperty("characterFilter");
        if (updateProperty) {
           this._filterRegExp = new RegExp(updateProperty.newValue);
        }
         
        updateProperty = update.getUpdatedProperty("regexPattern");
        if (updateProperty) {
            this._patternRegExp = new RegExp(updateProperty.newValue);
        }

        // Invoke the superclass method for all other methods
        return Informagen.Sync.ActiveTextField.prototype.renderUpdate.call(this, update);      
        
    },



    /***************************************************************************
     * Private method to update the status of the icon and message elements
     */

    _adjustStatus: function() {

        var value = this._adjustStatusPrecondition();

        // Empty input, use empty icon space holder
        if(value.length == 0) 
            return;

        // Test against the RegEx pattern; display valid or invalid icon
        if( this._patternRegExp.test(value)) 
            this._statusValid();
        else
            this._statusInvalid();

    }

});
