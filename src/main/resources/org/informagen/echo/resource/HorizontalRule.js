// Informagen =================================================================================
// Insure that the 'Informagen' namespace exists

if (!Core.get(window, ["Informagen"])) {
    Core.set(window, ["Informagen"], {});
}


// Informagen.HorizontalRule ==================================================================

Informagen.HorizontalRule = Core.extend(Echo.Component, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.HorizontalRule", this);
    },

    componentType :"Informagen.HorizontalRule"
});


// Informagen.HorizontalRule.Peer =============================================================

Informagen.HorizontalRule.Peer = Core.extend(Echo.Render.ComponentSync, {

    $load : function() {
            Echo.Render.registerPeer("Informagen.HorizontalRule", this);
    },
    
    _hrElement: null,

    renderAdd : function(update, parentElement) {
        // Create the HTML element; Assign a component ID; Add to the DOM
        this._hrElement = document.createElement("hr");
        this._hrElement.id = this.component.renderId;
        parentElement.appendChild(this._hrElement);
        
        // Use renderUpdate to handle properties
        this.renderUpdate(update);
    },

    renderUpdate : function(update) {

        // 'width' -------------------------------------
        
        var width = this.component.render("width");
        
        if (width) {
            
            if (Echo.Sync.Extent.isPercent(width)) 
                this._hrElement.style.width = width + "%";
            else 
                this._hrElement.style.width = Echo.Sync.Extent.toCssValue(width, true);
        } else
            this._hrElement.style.width = null;


        // 'height' -------------------------------------

        var height = this.component.render("height");

        if (height) 
            this._hrElement.style.height = Echo.Sync.Extent.toCssValue(height, false);
        else
            this._hrElement.style.height = null;

        // 'color' --------------------------------------
        //  Sets the 'background' style attribute; use 'renderClear' which handles null color

        var color = this.component.render("color");
        Echo.Sync.Color.renderClear(color,  this._hrElement, "backgroundColor");
        

        // 'border' -------------------------------------
        // Use 'renderClear' to handle null borders
        var border = this.component.render("border");
        Echo.Sync.Border.renderClear(border, this._hrElement);
                
        return false;
    },

    renderDispose : function(update) {
        delete this._hrElement;
    }



});