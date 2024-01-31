// Informagen =================================================================================
// Ensure that the 'Informagen' and 'Informagen.Sync' namespaces exists

if (!Core.get(window, ["Informagen", "Sync"])) {
    Core.set(window, ["Informagen", "Sync"], {});
}


// Informagen.Spinner =========================================================================

Informagen.Spinner = Core.extend(Echo.Component, {

    $load: function() {
        Echo.ComponentFactory.registerType("Informagen.Spinner", this);
    },

    componentType: "Informagen.Spinner"
});



// Informagen.Sync.Spinner ====================================================================

Informagen.Sync.Spinner = Core.extend(Echo.Render.ComponentSync, {

    $load: function() {
        Echo.Render.registerPeer("Informagen.Spinner", this);
    },

    _addOffset: function(offset) {
        
        var value = parseInt(this._inputField.value);
        
        if(!isNaN(value)) {  
            value += offset;
            this._inputField.value = value;
            this.component.set("value", value);
        } else {
            this._inputField.value = 0;
            this.component.set("value", 0);
        }
    },

    _processDecrementClick: function(e) {
        this._addOffset(-1);
    },
    
    _processIncrementClick: function(e) {
        this._addOffset(+1);
    },
        
    _processTextChange: function(e) {
        this._addOffset(0);
    },


    // Required methods ------------------------------------------------
    
    renderAdd: function(update, parentElement) {
        
        // The '<' decrement element
        this._decrementElement = document.createElement("span");
        this._decrementElement.style.cursor = "pointer";
        this._decrementElement.appendChild(document.createTextNode("<"));

        // The input field
        this._inputField = document.createElement("input");
        this._inputField.type = "text";
        this._inputField.size = 4;
        this._inputField.style.textAlign = "right";

        // The '>' increment element
        this._incrementElement = document.createElement("span");
        this._incrementElement.style.cursor = "pointer";
        this._incrementElement.appendChild(document.createTextNode(">"));
        
        this._divElement = document.createElement("div");
        this._divElement.appendChild(this._decrementElement);
        this._divElement.appendChild(this._inputField);
        this._divElement.appendChild(this._incrementElement);

        parentElement.appendChild(this._divElement);
        
        // Assign JS event handlers to elements
        Core.Web.Event.add(this._decrementElement, "click", 
                Core.method(this, this._processDecrementClick), false);
                
        Core.Web.Event.add(this._incrementElement, "click",
                Core.method(this, this._processIncrementClick), false);
                
        Core.Web.Event.add(this._inputField, "change",
                Core.method(this, this._processTextChange), false); 
                
         this.renderUpdate(update);
    },

    renderUpdate: function(update) {
         this._inputField.value = this.component.get("value", 0);
    },
    
    renderDispose: function(update) {
        
        // Clean up handlers
        Core.Web.Event.removeAll(this._decrementElement);
        Core.Web.Event.removeAll(this._incrementElement);
        Core.Web.Event.removeAll(this._inputField);
        
        // Assist with Garbage Collection
        this._decrementElement = null;
        this._inputField = null;
        this._incrementElement = null;
        this._divElement = null;
    }
});