Layer = {};
Layer = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("Layer", this);
    },
    componentType: "Layer"
});

Layer.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("Layer", this);
        componentType : "Layer";
    },
    _layer: null,
    _parent: null,
    _oldLayerName: null,
    renderAdd: function (update, parentElement) {
        this._parent = parentElement;
        var layerType = this.component.render("layerType");
        var layerLabel = this.component.render("layerLabel");
        var layerName = this.component.render("layerName");
        var layerKey = this.component.render("layerKey");
        this._oldLayerName = layerName;
//        console.log("layer " + layerLabel + " renderAdd key " + layerKey);
        if (layerType === "BING") {
            console.log("building bing layer for " + layerName + " comp id is " + this.component.renderId);
            this._layer = new OpenLayers.Layer.Bing({
                name: layerLabel,
                key: layerKey,
                type: layerName
            });
        } else if (layerType === "WFS") {
            console.log("new wfs layer layerName=" + layerName + " layerKey=" + layerKey + " layerLabel=" + layerLabel);
            try {
                var _strategies = this.__strategyCluster ? [new OpenLayers.Strategy.Fixed(), parentElement._strategyCluster] : [new OpenLayers.Strategy.Fixed()]
                this._layer = new OpenLayers.Layer.Vector(layerLabel, {
                    name: layerLabel,
                    strategies: _strategies,
                    styleMap: parentElement._styleMap,
                    protocol: new OpenLayers.Protocol.WFS({
                        url: layerKey,
                        srsName: "EPSG:4326",
                        featureType: layerName
                    }),
                    _featureover: function (evt) {
                        //TODO remettre
                        if (evt.feature && !evt.feature.noFlyOver) {
                            if (!evt.feature.clusterExplode) {
                                console.log("MIS _featureover for " + evt.feature.fid);
                            } else if (evt.feature.clusterExplode._explodeActive && evt.feature.clusterHide) {
                                console.log("HID _featureover for " + evt.feature.fid + " explode=" + evt.feature.clusterExplode._explodeActive + " hide=" + evt.feature.clusterHide);
                            } else {//feature over an icon while exploding
                                console.log("WFS _featureover for " + evt.feature.fid + " explode=" + evt.feature.clusterExplode._explodeActive + " hide=" + evt.feature.clusterHide);
                            }
                            evt.feature.layer.map.echoComponent._showBubble(evt.feature);
                        }
                    },
                    _featureout: function (evt) {
                        if (!evt.feature.clusterExplode) {
                            console.log("MIS _featureout for " + evt.feature.fid);
                        } else if (evt.feature.clusterExplode._explodeActive && evt.feature.clusterHide) {
                            console.log("HID _featureout for " + evt.feature.fid);
                        } else {
                            console.log("WFS _featureout for " + evt.feature.fid + " explode=" + evt.feature.clusterExplode._explodeActive + " hide=" + evt.feature.clusterHide);
                        }
                        evt.feature.layer.map.echoComponent._hideBubble();
                    }
                });
                //TODO remettre
                this._layer.events.on({
                    featureover: this._layer._featureover,
                    featureout: this._layer._featureout,
                    scope: this._layer});
                this._layer.echoComponent = this;
                parentElement.vectorLayer = this._layer;
            } catch (e) {
                console.log(e);
                console.log(e.stack);
            }
        } else if (layerType === "KML") {
//            console.log("building KML layer for " + layerName + " comp id is " + this.component.renderId);
            this._layer = new OpenLayers.Layer.Vector(layerLabel, {
                name: layerLabel,
                strategies: [new OpenLayers.Strategy.Fixed()],
                projection: new OpenLayers.Projection("EPSG:4326"),
                protocol: new OpenLayers.Protocol.HTTP({
                    url: layerKey,
                    srsName: "EPSG:4326",
                    format: new OpenLayers.Format.KML({
                        extractStyles: true,
                        extractAttributes: true,
                        maxDepth: 2
                    })
                })
            });
        } else if (layerType === "WMS") {
            console.log("building WMS layer for " + layerName + " comp id is " + this.component.renderId);
            this._layer = new OpenLayers.Layer.WMS(layerLabel, layerKey, {
                strategies: [new OpenLayers.Strategy.Fixed()],
                layers: layerName,
                srsName: "EPSG:900913",
                isBaseLayer: false
            });
            this._layer.isBaseLayer = false;
            var visibility = (this.component.render("layerVisibility") === "true");
            this._layer.setVisibility(visibility);
        } else if (layerType === "OSM") {
            this._layer = new OpenLayers.Layer.OSM();
        } else {
            console.error("Layer type " + layerType + " not supported");
            return;
        }
        try {
//            console.log("will  call add for " + this._layer.name);

            parentElement.addLayers([this._layer]);
        } catch (e) {
            console.log(e);
        }
    },
    renderDispose: function (update) {
//        console.log("layer " + this._layer.name + " renderDispose");
        try {
            this._parent.removeLayer(this._layer);
        } catch (e) {
            //nop
        }
        this._layer = null;
    },
    renderUpdate: function (update) {
        console.log("layer " + this._layer.name + " renderUpdate");
        console.log(this._layer);
        var layerName = this.component.render("layerName");
        var layerLabel = this.component.render("layerLabel");
        var visibility = this.component.render("layerVisibility") === "true";
        if (this._layer instanceof OpenLayers.Layer.Vector) {

            if (this._oldLayerName === layerName) {
                console.log("Vect Modifying existing layer label=" + layerLabel + " visible=" + visibility);
                this._layer.setVisibility(visibility);
            } else {
                console.log("Vect Removing/readding existing layer label=" + layerLabel + " visible=" + visibility);
                try {
                    this._parent.removeLayer(this._layer);
                } catch (e) {
                    //nop
                }
                this._layer = null;
                this.renderAdd(update, this._parent);
                this._oldLayerName = layerName;
            }
        } else {
            if (this._oldLayerName === layerName) {
                console.log("Tile Modifying existing layer label=" + layerLabel + " visible=" + visibility);
                this._layer.setVisibility(visibility);
            } else {
                console.log("Tile Removing/readding existing layer label=" + layerLabel + " visible=" + visibility);
                try {
                    this._parent.removeLayer(this._layer);
                } catch (e) {
                    //nop
                }
                this._layer = null;
                this.renderAdd(update, this._parent);
                this._oldLayerName = layerName;
            }

        }
        return true;
    }
});