// Informagen =================================================================================
// Insure that the 'Informagen' namespace exists

if (!Core.get(window, ["Informagen"])) {
    Core.set(window, ["Informagen"], {});
}


// Informagen.StaticImage =======================================================================

Informagen.StaticImage = Core.extend(Echo.Component, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.StaticImage", this);
    },

    componentType :"Informagen.StaticImage",
    
    focusable : false

});


// Informagen.StaticImage.Peer ==================================================================

Informagen.StaticImage.Peer = Core.extend(Echo.Render.ComponentSync, {

    $load : function() {
        Echo.Render.registerPeer("Informagen.StaticImage", this);
    },
    

     $virtual: {
        _divElement: null,
        _imgElement: null,
        _imageReference: null
     },
    
    renderAdd: function(update, parentElement ) {
                
        this._divElement = document.createElement("div");
        this._divElement.id = this.component.renderId;

        parentElement.appendChild(this._divElement);
        
        this.renderUpdate(update);
    },
    
    renderUpdate: function(update) {
    
        Core.Web.DOM.removeAllChildren(this._divElement);
        
        var imageReference = this.component.render("imageReference", null);
        
        if(imageReference) {
            this._imgElement = document.createElement("img");
            Echo.Sync.ImageReference.renderImg(imageReference, this._imgElement);
            this._divElement.appendChild(this._imgElement);
        }
        
        return false;
    },
    
    
    renderDispose: function(update) {
        this._divElement = null;
        this._imgElement = null;
    }
    
});
