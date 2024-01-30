// Informagen =================================================================================
// Ensure that the 'Informagen' and 'Informagen.Sync' namespaces exist

if (!Core.get(window, ["Informagen", "Sync"])) {
    Core.set(window, ["Informagen", "Sync"], {});
}


// Informagen.NumericTextField ================================================================

Informagen.NumericTextField = Core.extend(Informagen.IntegerTextField, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.NumericTextField", this);
    },

    componentType : "Informagen.NumericTextField"
});


// Informagen.Sync.NumericTextField ===========================================================
//  The name of this object is irrelevant; see '$load' function.  However, keeping this 
//    class as a member of the namespace 'Informagen.Sync' adds consistency.



Informagen.Sync.NumericTextField = Core.extend(Informagen.Sync.IntegerTextField, {


    // Register this class as the 'Sync' class for the 'Informagen.NumericTextField' class

    $load: function() {
        Echo.Render.registerPeer("Informagen.NumericTextField", this);
    },

    $construct : function() {
        Informagen.Sync.IntegerTextField.call(this);

        this._filterRegExp = new RegExp("[-|+]|[0-9]|[\\.]");
        this._fromString = function(string) { return parseFloat(string); };
    }
    
        // To do: use real float RegEx pattern and float range

//     _minimumValue: -5e-324,
//     _maximumValue:  1.7976931348623157e+308
    
    
});
