// Informagen =================================================================================
// Insure that the 'Informagen' namespace exists

if (!Core.get(window, ["Informagen"])) {
    Core.set(window, ["Informagen"], {});
}


// Informagen.ScrollArea ======================================================================

Informagen.ScrollArea = Core.extend(Echo.Component, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.ScrollArea", this);
    },

    componentType :"Informagen.ScrollArea"
});


// Informagen.ScrollArea.Peer =================================================================

Informagen.ScrollArea.Peer = Core.extend(Echo.Render.ComponentSync, {

    $load : function() {
            Echo.Render.registerPeer("Informagen.ScrollArea", this);
    },

    renderAdd : function(update, parentElement) {
        this.divElement = document.createElement("div");
        this.divElement.id = this.component.renderId;
        this.divElement.style.overflow = "auto";
        //this.divElement.style.overflowX = "auto";
        //this.divElement.style.overflowY = "auto";
        this.divElement.style.width = Echo.Sync.Extent.toCssValue(
                        this.component.render("width", "100%"), true, true);
        this.divElement.style.height = Echo.Sync.Extent.toCssValue(
                        this.component.render("height", "100%"), false, true);

        Echo.Sync.Color.renderFB(this.component, this.divElement);
        Echo.Sync.Font.render(this.component.render("font"), this.divElement);

        if (this.component.getComponentCount() == 1) {
            Echo.Render.renderComponentAdd(update,
                this.component.getComponent(0), 
                this.divElement);
        }

        parentElement.appendChild(this.divElement);
    },

    renderUpdate : function(update) {
        var divElement = this.divElement;
        var parentNode = divElement.parentNode;
        Echo.Render.renderComponentDispose(update, update.parent);
        parentNode.removeChild(divElement);
        this.renderAdd(update, parentNode);
        return true;
    },

    renderDispose : function(update) {
        delete this.divElement;
    }
});