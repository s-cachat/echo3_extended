Synoptique = {};
Synoptique = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("Synoptique", this);
    },
    componentType: "Synoptique",
    doAction: function (info) {
        this.fireEvent({type: "action", source: this, v: info});

        console.log("ACTION : " + info);
    },
    _getFidFromEvent: function (e) {
        var feature = null;
        if (e.feature) {
            feature = e.feature;
//            console.log("Got feature 1");
//            console.log(feature);
        } else {
            if (e.target) {
                var target = e.target;
                var fid = target.featureId;
//                console.log("Event from mouse : " + fid);
//                console.log(e);
                if (this.peer._openlayer && this.peer._openlayer.vectorLayer) {
                    feature = this.peer._openlayer.vectorLayer.getFeatureById(fid);
//                    console.log("Got feature 2");
//                    console.log(feature);
                } else {
                    console.log("no vectorLayer in");
                    console.log(this.peer);
                }
            } else {
//                console.log("Event from mouse : no target");
//                console.log(e);
            }
        }
        return feature ? feature.fid : null;
    }
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
    _click: null,
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
            console.log("Synoptique renderDisplay create");
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
            var myself = this;
            rect.on("modified", function (e) {
                console.log("rect clic ",myself,e);
                myself.fireEvent({type: "action", source: this, v: {
                    action: "clic",
                    id: rect.id
                }});
            });
            rect.on("mouseup", function (e) {
                console.log("rect mouseup ",myself,e);
                myself.fireEvent({type: "action", source: this, v: {
                    action: "modified",
                    id: rect.id
                }});
            });


        } else {
            console.log("Synoptique renderDisplay");
        }
//        console.trace();
    },
    _clic: function (src, e) {
        console.log("clic", src, e);
        this.fireEvent({type: "action", source: this, v: {
                action: "clic",
                id: src.id
            }});
    },
    _modified: function (src, e) {
        console.log("modified", src, e);
        this.fireEvent({type: "action", source: this, v: {
                action: "modified",
                id: src.id
            }});
    },    
    renderDispose: function (update) {
        console.log("Map renderDispose " + update);
        this._div = null;
    },
    renderUpdate: function (update) {
        console.log("Map renderUpdate ", update);
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
