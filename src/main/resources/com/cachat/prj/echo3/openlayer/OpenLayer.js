OpenLayer = {};
OpenLayer = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("OpenLayer", this);
    },
    componentType: "OpenLayer",
    doAction: function (info) {
        if (info) {
            this.peer.component.set("selectedItem", info);
        }
        var lonlat = this.peer._openlayer.getCenter();
        lonlat = new OpenLayers.LonLat(lonlat.lon, lonlat.lat).transform(new OpenLayers.Projection("EPSG:900913"), new OpenLayers.Projection("EPSG:4326"));
        this.peer.component.set("centerLat", lonlat.lat);
        this.peer.component.set("centerLon", lonlat.lon);
        this.peer.component.set("zoomLevel", this.peer._openlayer.getZoom());

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
OpenLayers.Strategy.AttributeCluster = OpenLayers.Class(OpenLayers.Strategy.Cluster, {
    /**
     * the attribute to use for comparison
     */
    attribute: null,
    /**
     * Method: shouldCluster
     * Determine whether to include a feature in a given cluster.
     *
     * Parameters:
     * cluster - {<OpenLayers.Feature.Vector>} A cluster.
     * feature - {<OpenLayers.Feature.Vector>} A feature.
     *
     * Returns:
     * {Boolean} The feature should be included in the cluster.
     */
    shouldCluster: function (cluster, feature) {
        var cc_attrval = cluster.cluster[0].attributes[this.attribute];
        var fc_attrval = feature.attributes[this.attribute];
        var superProto = OpenLayers.Strategy.Cluster.prototype;
        return cc_attrval === fc_attrval &&
                superProto.shouldCluster.apply(this, arguments);
    },
    CLASS_NAME: "OpenLayers.Strategy.AttributeCluster"
});
OpenLayer.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("OpenLayer", this);
        componentType : "OpenLayer";
    },
    _div: null,
    _bubbleDiv: null,
    _bubbleLastId: null,
    _bubbleComponent: null,
    _openlayer: null,
    _click: null,
    _reloadNow: 0,
    _groupIcons: true,
    renderAdd: function (update, parentElement) {
        console.log("Map renderAdd");
        this._div = document.createElement("div");
        this._div.id = this.component.renderId;
        Echo.Sync.renderComponentDefaults(this.component, this._div);
        Extended.renderPositionnable(this.component, this._div);
        parentElement.appendChild(this._div);
    },
    renderDisplay: function () {
        if (!this._openlayer) {
            console.log("Map renderDisplay create");
            this._openlayer = this._buildMap(this._div.id);
            var componentCount = this.component.getComponentCount();
            for (var i = 0; i < componentCount; i++) {
                var child = this.component.getComponent(i);
                this._renderAddChild(false, child);
            }
        } else {
//            console.log("Map renderDisplay");
        }
//        console.trace();

        this._openlayer.updateSize();
    },
    _buildMap: function (idDiv) {
        var lat = this.component.get("centerLat");
        var lon = this.component.get("centerLon");
        var zoom = this.component.get("zoomLevel");
        var maxZoom = this.component.get("maxZoom");
        this._groupIcons = this.component.get("groupIcons");
        if (this._groupIcons === undefined) {
            this._groupIcons = true;
        }
        var tileServer = this.component.get("tileServer");
        console.log("center on " + lat + " , " + lon + " zoom " + zoom + "tile server \"" + tileServer + "\", max Zoom " + maxZoom + " groupIcons " + this._groupIcons);

        OpenLayers.ImgPath = "ol/img/";
        var evl = {};
        var map = new OpenLayers.Map(idDiv, {
            numZoomLevels: maxZoom,
            units: 'm',
            projection: new OpenLayers.Projection("EPSG:900913"),
            theme: "ol/theme/default/style.css"
                    //  displayProjection: new OpenLayers.Projection("EPSG:4326")
        });
        var _context = {
            getColor: function (feature) {
                var color = '#aaaaaa';
                if (feature.attributes.color) {
                    // console.log("color for " + feature.attributes.id + " is " + feature.attributes.color);
                    color = "#" + feature.attributes.color;
                } else {
                    //console.log("no color for " + feature.attributes.id + " : " + mydump(feature.attributes))
                }
                return color;
            },
            getWidth: function (feature) {
                var width = 1;
                if (feature.attributes.width) {
                    //console.log("width for " + feature.attributes.id + " is " + feature.attributes.width);
                    width = parseInt(feature.attributes.width);
                } else {
                    // console.log("no width for " + feature.attributes.id + " : " + mydump(feature.attributes));
                }
                return width;
            },
            getIcon: function (feature) {
                var icon = null;
                if (feature.cluster) {
                    icon = OpenLayers.ImgPath + "cluster.png";
                } else if (feature.attributes.icon && feature.attributes.icon !== "null") {
//                    console.log("icon for " + feature.attributes.id + " is " + feature.attributes.icon);
                    icon = feature.attributes.icon;
                } else {
                    icon = OpenLayers.ImgPath + "pin.png";
//                    console.log("no icon for " + feature.attributes.id + " : " + mydump(feature.attributes));
                }
                return icon;
            },
            getLabel: function (feature) {
                var label = "";
                if (feature.attributes.label) {
                    label = feature.attributes.label;
                } else {
                    // console.log("no label for " + feature.attributes.id + " : " + mydump(feature.attributes));
                }
                return label;
            }
        };
        var styleMap = new OpenLayers.StyleMap({
            'default': new OpenLayers.Style({
                pointRadius: "14",
                fillColor: "${getColor}",
                fillOpacity: 0.7,
                strokeColor: "${getColor}",
                strokeWidth: "${getWidth}",
                externalGraphic: "${getIcon}",
                strokeOpacity: 1,
                graphicZIndex: 1,
                label: "${getLabel}",
                fontColor: "#000000",
                fontSize: "12px"
            }, {
                context: _context
            }),
            'select': new OpenLayers.Style({
                pointRadius: "14",
                fillColor: "${getColor}",
                fillOpacity: 0.7,
                strokeColor: "${getColor}",
                strokeWidth: "${getWidth}",
                externalGraphic: "${getIcon}",
                strokeOpacity: 1,
                graphicZIndex: 2
            }, {
                context: _context
            })
        });
        map.events.register("moveend", null, this._mapEvent);
        map.events.register("zoomend", null, this._mapEvent);
        map.events.register("featureclick", null, this._featureEvent);
        map.events.register("click", null, this._clickEvent);
        var ol_wms;
        if (tileServer) {
            ol_wms = new OpenLayers.Layer.OSM("openstreetmap", null, {
                projection: new OpenLayers.Projection("EPSG:900913"),
                numZoomLevels: maxZoom,
                url: "http://" + tileServer + "/${z}/${x}/${y}.png"
            });
        } else {
            ol_wms = new OpenLayers.Layer.OSM("openstreetmap", null, {
                projection: new OpenLayers.Projection("EPSG:900913"),
                numZoomLevels: maxZoom
            });
        }
        map.addLayers([ol_wms]);
        map.addControl(new OpenLayers.Control.LayerSwitcher());
        map.addControl(new OpenLayers.Control.Navigation());
        map.addControl(new OpenLayers.Control.PanZoomBar());
        map.addControl(new OpenLayers.Control.MousePosition());
        map.addControl(new OpenLayers.Control.ScaleLine());
        map.peer = this;
        if (this._groupIcons) {
            map._strategyCluster = new OpenLayers.Strategy.ClusterExplode();
        }
        map._styleMap = styleMap;
        map.echoComponent = this;

//        this._click = new OpenLayers.Control.Click();
//        this._click.olsync = this;
//        map.addControl(this._click);
//        this._click.activate();

        if ((zoom) && (lat) && (lon)) {
            var lonLat = new OpenLayers.LonLat(lon, lat).transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
            map.setCenter(lonLat, zoom);
        } else {
            console.log("§§§§§§§§§§§§§§§§§§§§§§§§§ no base center/zoom");
        }
        return map;
    },
    _featureEvent: function (e) {
        var fid = this.peer.component._getFidFromEvent(e);
        if (fid) {
            this.peer.component.doAction("feature_" + fid);
//            console.log("Map says: " + e.feature.id + " clicked on feature " + fid);
        } else {
            var lonlat = this.peer._openlayer.getLonLatFromPixel(e.xy);
            lonlat = new OpenLayers.LonLat(lonlat.lon, lonlat.lat).transform(new OpenLayers.Projection("EPSG:900913"), new OpenLayers.Projection("EPSG:4326"));
            this.peer.component.set("selectedLat", lonlat.lat);
            this.peer.component.set("selectedLon", lonlat.lon);
            this.peer.component.doAction("click");
        }
    },
    _mapEvent: function (e) {
        console.log("Map says: map event " + e + "my peer is " + this.peer);
        if (this.peer._openlayer) {
            var lonlat = this.peer._openlayer.getCenter();
            lonlat = new OpenLayers.LonLat(lonlat.lon, lonlat.lat).transform(new OpenLayers.Projection("EPSG:900913"), new OpenLayers.Projection("EPSG:4326"));
            this.peer.component.set("centerLat", lonlat.lat);
            this.peer.component.set("centerLon", lonlat.lon);
            this.peer.component.set("zoomLevel", this.peer._openlayer.getZoom());
            this.peer.component.doAction("map");
        }
    },
    _clickEvent: function (e) {
        var fid = this.peer.component._getFidFromEvent(e);
//        console.log("click trigger " + fid);
        var lonlat = this.peer._openlayer.getLonLatFromPixel(e.xy);
        if (lonlat) {
            lonlat = new OpenLayers.LonLat(lonlat.lon, lonlat.lat).transform(new OpenLayers.Projection("EPSG:900913"), new OpenLayers.Projection("EPSG:4326"));
            this.peer.component.set("selectedLat", lonlat.lat);
            this.peer.component.set("selectedLon", lonlat.lon);
            this.peer.component.doAction(fid ? "click_" + fid : null);
        } else {
            console.log('Impossible to get LonLat from mouse event!', e);
        }
    },
    _renderAddChild: function (update, child) {
        var layerName = child.render("layerLabel");
        if (child.componentType === "Layer") {
            var old = this._openlayer.getLayersByName(layerName);
            if (old.length > 0) {
                console.log("Map SKIP renderAddChild id=" + this.component.renderId + " name=" + layerName + " upd=" + update);
            } else {
                Echo.Render.renderComponentAdd(update, child, this._openlayer);
            }
        } else {
//TODO remettre
            console.log(update);
            console.log(child);
            child.isABubble = true;
            Echo.Render.renderComponentAdd(update, child, this._div);

            this._bubbleComponent = child;
            console.log("##### searching " + this._bubbleComponent.renderId);
            this._bubbleDiv = document.createElement("div");
            this._div.appendChild(this._bubbleDiv);
            var componentDiv = document.getElementById(this._bubbleComponent.renderId);
            this._div.removeChild(componentDiv);
            this._bubbleDiv.appendChild(componentDiv);
            this._bubbleDiv.isABubbleDiv = true;
            console.log(this._bubbleDiv);
            console.log("_renderAddChild non layer, BUBBLE id is " + child.renderId);
        }
    },
    renderDispose: function (update) {
        console.log("Map renderDispose " + update);
//        console.trace();
        this._div = null;
    },
    _showBubble: function (feature, mousePos) {
        if (feature.isTemp || !feature.fid) {
            return;
        }
        console.log("feature bubble id=" + feature.fid);
        console.log(feature);
        var xy;
        var center = feature.geometry.bounds.getCenterLonLat();
        if (center && ((mousePos === null) || feature.geometry.bounds.getWidth() > 0 || feature.geometry.bounds.getHeight() > 0)) {
            xy = feature.layer.map.getPixelFromLonLat(feature.geometry.bounds.getCenterLonLat());
            console.log("bubble at " + xy);
        } else {
            xy = feature.layer.map.getPixelFromLonLat(feature.geometry.bounds.getCenterLonLat());
            console.log("bubble at noloc ! " + xy);
        }

        console.log(this.component);
//TODO remettre C1
        if (xy) {
            if (this._bubbleDiv) {
                console.log("xy=" + xy);
                xy.x += 4;
                xy.y += 4;

                this._bubbleDiv.style.left = xy.x + "px";
                this._bubbleDiv.style.top = xy.y + "px";
                this._bubbleDiv.style.zIndex = 10000;
                this._bubbleDiv.style.position = "absolute";
                if (this._bubbleLastId === feature.fid) {
                    this._bubbleDiv.style.display = "block";
                }
                this._bubbleLastId = feature.fid;
            } else {
                console.log("*** no bubbleDiv !");
            }
            console.log("display bubbleDiv and call update for " + feature.fid);
            console.log(feature);
            this.component.set("bubble_id", feature.fid);
        }

    },
    _hideBubble: function () {
        if (this._bubbleDiv) {
            this._bubbleDiv.style.display = "none";
        }
        console.log("hide bubbleDiv");
        this._bubbleLastId = null;
        this.component.set("bubble_id", null);
        this.component.set("selectedItem", null);
    },
    _renderRemoveChild: function (update, child) {
        if (child.componentType === "Layer") {
            var layerName = child.render("layerLabel");
//            console.log("Map _renderRemoveChild " + layerName);
            var old = this._openlayer.getLayersByName(layerName);
            this._openlayer.removeLayer(old);
//            console.log("Map _renderRemoveChild done 1");
        } else {
//            console.log("_renderRemoveChild non layer");
//            console.log(update);
//            console.log(child);
            Echo.Render.renderComponentDispose(update, child);
        }
    },
    renderUpdate: function (update) {
        console.log("Map renderUpdate");
        console.log(update);
        var pu = update._propertyUpdates;
        if (pu) {
            var lat;
            var lon;
            var zoom;

            var lonlat = this._openlayer.getCenter();
            lonlat = new OpenLayers.LonLat(lonlat.lon, lonlat.lat).transform(new OpenLayers.Projection("EPSG:900913"), new OpenLayers.Projection("EPSG:4326"));
            var lat = lonlat.lat;
            var lon = lonlat.lon;
            var zoom = this._openlayer.getZoom();

            console.log("old pos : " + lat + " , " + lon + " , " + zoom);

            if (pu.centerLat) {
                lat = pu.centerLat.newValue;
            }
            if (pu.centerLon) {
                lon = pu.centerLon.newValue;
            }
            if (pu.zoomLevel) {
                zoom = pu.zoomLevel.newValue;
            }
            console.log("new pos : " + lat + " , " + lon + " , " + zoom);
            if ((zoom) && (lat) && (lon)) {
                var lonLat = new OpenLayers.LonLat(lon, lat).transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
                this._openlayer.setCenter(lonLat, zoom);
            }
        }
//        console.log("bubblecomponent");
//        console.log(this._bubbleComponent);
//        console.log("bubbleDiv");
//        console.log(this._bubbleDiv);
//TODO remettre
        var _reloadNow = this.component.render("reloadNow");
        if (_reloadNow && _reloadNow !== this._reloadNow) {
            if (this._openlayer.vectorLayer) {
                console.log("Server asked for reload, but we skip it");
                //this._openlayer.vectorLayer.refresh({force: true});
            } else {
                console.log("Server asked for reload, but theres is no vector layer");
            }
        }
        if (this._bubbleComponent && this._bubbleComponent.peer && this._bubbleComponent.peer !== null) {
            this._bubbleComponent.peer.renderUpdate(update);
            if (this._bubbleLastId && this._bubbleDiv) {
                this._bubbleDiv.style.display = "block";
            }
        }
//        console.log("Map renderUpdate DONE");
        return true;
    }
}
);