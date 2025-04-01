Synoptique = {};
Synoptique = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("Synoptique", this);
        console.log("Synoptique load : ", this.toString(1));
    },
    componentType: "Synoptique"
});
Synoptique.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("Synoptique", this);
        componentType : "Synoptique";
    },
    _div: null,
    _canvas: null,
    _fabric: null,
    _content: {},
    renderAdd: function (update, parentElement) {
        console.log("Synoptique renderAdd");
        this._div = document.createElement("div");
        this._div.id = this.component.renderId;
        Echo.Sync.renderComponentDefaults(this.component, this._div);
        Extended.renderPositionnable(this.component, this._div);
        parentElement.appendChild(this._div);
        this._canvas = document.createElement("canvas");
        this._canvas.id = this.component.renderId + "_canvas";
        this._div.appendChild(this._canvas);
    },
    renderDisplay: function () {
        if (!this._fabric) {
            console.log("Synoptique renderDisplay create this", this);
            this._canvas.width = this._div.offsetWidth;
            this._canvas.height = this._div.offsetHeight;
            this._fabric = new fabric.Canvas(this._canvas);
            var rect = new fabric.Rect({
                left: 100,
                top: 100,
                fill: 'red',
                width: 20,
                height: 20
            });
            rect.id = "S99";
            this._fabric.add(rect);
            const _this = this;
            rect.on("modified", function (e) {
                _this._modifiedEvent(rect, e);
            });
            rect.on("mouseup", function (e) {
                _this._clicEvent(rect, e);
            });
        } else {
            console.log("Synoptique renderDisplay");
        }
//        console.trace();
    },
    _modifiedEvent(source, event) {
        console.log("modified source", source," event ",event);
        this.component.fireEvent({
            type: 'objectEdit',
            source: this,
            left:source.left,
            top:source.top,
            width:source.width,
            height:source.height,
            angle:source.angle
        });
    },
    _clicEvent(source, event) {
        console.log("modified source", source," event ",event);
        this.component.fireEvent({
            type: 'objectClic', 
            source: this,
            uid: source.uid});
    },
    renderDispose: function (update) {
        console.log("Synoptique renderDispose " + update);
        this._div = null;
    },
    renderUpdate: function (update) {
        console.log("Synoptique renderUpdate ", update);
        console.log(update);
        var pu = update._propertyUpdates;
        if (pu) {
            console.log("pu", pu);
        } else {

        }
        return true;
    }
}
);
