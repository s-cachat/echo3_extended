DirectHtml = {};
DirectHtml = Core.extend(Echo.Component, {
    $load: function() {
        Echo.ComponentFactory.registerType("DirectHtml", this);
    },
    componentType: "DirectHtml"
});

DirectHtml.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function() {
        Echo.Render.registerPeer("DirectHtml", this);
        componentType : "DirectHtml";
    },
    _div: null,
    renderAdd: function(update, parentElement) {
        try {
            this._div = document.createElement("div");
            Echo.Sync.renderComponentDefaults(this.component, this._div);
            Extended.renderPositionnable(this.component, this._div);
            parentElement.appendChild(this._div);
            var text = this.component.render("text");
            if (text) {
                this._div.innerHTML = text;
            }
        } catch (e) {
            if (console) console.log(e);
        }

    },
    renderDispose: function(update) {
        this._div = null;
    },
    renderUpdate: function(update) {
        try {
            var element = this._div;
            var containerElement = element.parentNode;
            Echo.Render.renderComponentDispose(update, update.parent);
            containerElement.removeChild(element);
            this.renderAdd(update, containerElement);
            return true;
        } catch (e) {
            if (console) console.log(e);
        }
    }
});