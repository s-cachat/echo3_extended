// Informagen =================================================================================
// Insure that the 'Informagen' namespace exists

if (!Core.get(window, ["Informagen"])) {
    Core.set(window, ["Informagen"], {});
}


// Informagen.HtmlComponent =======================================================================

Informagen.HtmlComponent = Core.extend(Echo.Component, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.HtmlComponent", this);
    },

    componentType :"Informagen.HtmlComponent"
});


// Informagen.HtmlComponent.Peer ==================================================================

Informagen.HtmlComponent.Peer = Core.extend(Echo.Render.ComponentSync, {

    $load : function() {
        Echo.Render.registerPeer("Informagen.HtmlComponent", this);
    },

    renderAdd : function(update, parentElement) {
        this.divElement = document.createElement("div");
        this.divElement.id = this.component.renderId;
        parentElement.appendChild(this.divElement);
        this.renderUpdate(update);
    },

    renderUpdate : function(update) {
        this.divElement.innerHTML = this.component.render("html", "");
        Echo.Sync.Font.render(this.component.render("font"), this.divElement);
        Echo.Sync.Color.renderFB(this.component, this.divElement);
        return false;
    },

    renderDispose : function(update) {
        delete this.divElement;
    }
});