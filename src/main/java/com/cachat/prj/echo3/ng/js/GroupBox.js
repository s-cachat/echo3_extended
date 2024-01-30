GroupBox = {};
GroupBox = Core.extend(Echo.Component, {
    $load: function() {
        Echo.ComponentFactory.registerType("GroupBox", this);
    },
    componentType: "GroupBox"
});

GroupBox.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function() {
        Echo.Render.registerPeer("GroupBox", this);
        componentType : "GroupBox";
    },
    _fieldset: null,
    _legend: null,
    _legend_span: null,
    _child: null,
    renderAdd: function(update, parentElement) {
        this._fieldset = document.createElement("fieldset");
        this._fieldset.id = this.component.renderId;
        this._legend = document.createElement("legend");
        this._fieldset.appendChild(this._legend);
        this._legend_span = document.createElement("span");
        this._legend.appendChild(this._legend_span);
        var title = this.component.get("title");
        if (title) {
            this._legend_span.innerHTML = title;
        }
        Echo.Sync.renderComponentDefaults(this.component, this._fieldset);
        Extended.renderPositionnable(this.component, this._fieldset);
        var background = this.component.render("background");
        var backgroundImage = this.component.render("backgroundImage");
        Echo.Sync.FillImage.render(backgroundImage, this._fieldset);

        if (!background && !backgroundImage) {
            Echo.Sync.FillImage.render(this.client.getResourceUrl("Echo", "resource/Transparent.gif"), this._fieldset);
        }

        var componentCount = this.component.getComponentCount();

        if (componentCount > 0) {
            var child = this.component.getComponent(0);
            this._renderAddChild(update, child);
        }

        parentElement.appendChild(this._fieldset);
    },
    _renderAddChild: function(update, child) {
        Echo.Render.renderComponentAdd(update, child, this._fieldset);
        _child = child;
    },
    renderDispose: function(update) {
        this._fieldset = null;
    },
    _renderRemoveChild: function(update, child) {
        if (!_child) {
            // Child never rendered.
            return;
        }
        _child.parentNode.removeChild(_child);
        delete _child;
    },
    renderUpdate: function(update) {
        var element = this._fieldset;
        var containerElement = element.parentNode;
        Echo.Render.renderComponentDispose(update, update.parent);
        containerElement.removeChild(element);
        this.renderAdd(update, containerElement);
        return true;
    }
});