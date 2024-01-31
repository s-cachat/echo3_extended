
OpenLayers.Strategy.ClusterExplode = OpenLayers.Class(OpenLayers.Strategy, {
    distance: 44,
    iconDistance: 44,
    threshold: 2,
    features: null,
    clusters: null,
    clustering: !1,
    resolution: null,
    _lastId: 0,
    activate: function() {
        if (window.console)
//            console.log("ClusterExplode activate");
            var a = OpenLayers.Strategy.prototype.activate.call(this);
        if (a)
            this.layer.events.on({
                beforefeaturesadded: this.cacheFeatures,
                featureover: this._featureover,
                featureout: this._featureout,
                featuresremoved: this.clearCache,
                moveend: this.cluster,
                scope: this});
        return a;
    }, deactivate: function() {
        if (window.console)
//            console.log("ClusterExplode deactivate");
            var a = OpenLayers.Strategy.prototype.deactivate.call(this);
        if (a) {
            this.clearCache();
            this.layer.events.un({
                beforefeaturesadded: this.cacheFeatures,
                featureover: this._featureover,
                featureout: this._featureout,
                featuresremoved: this.clearCache,
                moveend: this.cluster,
                scope: this});
        }
        return a;
    }, cacheFeatures: function(a) {
        var b = !0;
        this.clustering || (this.clearCache(), this.features = a.features, this.cluster(), b = !1);
        return b;
    }, clearCache: function() {
        this.clustering || (this.features = null);
    }, cluster: function(a) {
//        console.log("cluster called with ");
//        console.log(a);
        if (this._clusterFeature !== null) {
            var evt = [];
            evt.feature = this._clusterFeature;
            this._featureout(evt);
        }
        var res;
        if ((!a || a.zoomChanged) && this.features && (res = this.layer.getResolution(), res != this.resolution || !this.clustersExist())) {
            this.resolution = res;
//            console.log("Clustering for resolution " + this.resolution);
            var _clusters = [];
            for (var b, c, d, e = 0; e < this.features.length; ++e) {
                var cur = this.features[e];
                cur.clusterHide = true;
                cur.clusterExplode = this;
                if (cur && (cur.geometry)) {
                    c = !1;
                    for (var f = _clusters.length - 1; 0 <= f; --f) {
                        var cluster = _clusters[f];
                        if (this.shouldCluster(cluster, cur)) {
                            this.addToCluster(cluster, cur);
                            c = !0;
                            break
                        }
                    }
                    c || _clusters.push(this.createCluster(cur));
                }
            }
            this.clustering = !0;
            this.layer.removeAllFeatures();
            this.clustering = !1;
            if (0 < _clusters.length) {
                if (1 < this.threshold) {
                    for (b = _clusters.slice(), _clusters = [], e = 0, d = b.length; e < d; ++e) {
                        var cur = b[e];
                        if (cur.attributes.count < this.threshold) {
                            Array.prototype.push.apply(_clusters, cur.cluster);
                            //console.log("ADD single");
                            cur.cluster[0].clusterHide=false;
                        } else {
                            var minLon, minLat, maxLon, maxLat;
                            for (var i = 0; i < cur.cluster.length; i++) {
                                var lon = cur.cluster[i].geometry.x;
                                var lat = cur.cluster[i].geometry.y;
                                cur.cluster[i].origGeometry = new OpenLayers.Geometry.Point(cur.cluster[i].geometry.x, cur.cluster[i].geometry.y);
                                if (i == 0) {
                                    minLon = lon;
                                    minLat = lat;
                                    maxLon = lon;
                                    maxLat = lat;
                                } else {
                                    minLon = Math.min(minLon, lon);
                                    minLat = Math.min(minLat, lat);
                                    maxLon = Math.max(maxLon, lon);
                                    maxLat = Math.max(maxLat, lat);
                                }
                            }
                            //console.log("ADD cluster");
                        cur.geometry.x = (minLon + maxLon) / 2;
                            cur.geometry.y = (minLat + maxLat) / 2;
                            _clusters.push(cur);
                        }
                    }
                }
                this.clustering = !0;
                this.layer.addFeatures(_clusters);
                this.clustering = !1;
            }
            this.clusters = _clusters;
        }
    },
    clustersExist: function() {
        var a = !1;
        if (this.clusters && 0 < this.clusters.length && this.clusters.length == this.layer.features.length)
            for (var a = !0, b = 0; b < this.clusters.length; ++b)
                if (this.clusters[b] != this.layer.features[b]) {
                    a = !1;
                    break
                }
        return a;
    },
    _tempDisplay: [],
    _tempLines: [],
    _explodeActive: false,
    _clusterFeature: null,
    _featureover: function(evt) {
        
        var cur = evt.feature;
//        console.log("cluster : featureOver");
//        console.log(cur);
        if ((this._clusterFeature === null) && cur.cluster) {
//            console.log("cluster over " + cur.cluster.fid + " size " + cur.cluster.length);
            this._explodeActive = true;
            var r1 = evt.feature.layer.map.getLonLatFromPixel(new OpenLayers.Pixel(0, 0));
            var r2 = evt.feature.layer.map.getLonLatFromPixel(new OpenLayers.Pixel(100, 100));
            var scale = Math.abs((r2.lat - r1.lat) / 100);//echelle pour passer des pixels aux coords. géo.
            var ref0 = this.iconDistance * scale;
            var ref = (this.iconDistance * cur.cluster.length / 2 / Math.PI) * scale;
            for (var i = 0; i < cur.cluster.length; i++) {
                dx = cur.cluster[i].origGeometry.x - cur.geometry.x;
                dy = cur.cluster[i].origGeometry.y - cur.geometry.y;
                cur.cluster[i].clusterAlpha = Math.atan2(dy, dx);
                ref = Math.max(ref, Math.sqrt(dx * dx + dy * dy));
            }
            ref += ref0 / 2;
            this._clusterFeature = new OpenLayers.Feature.Vector(OpenLayers.Geometry.Polygon.createRegularPolygon(cur.geometry, ref + ref0 / 2, 64, 0));
            this._clusterFeature.attributes.color = "ffffff";
            this._clusterFeature.attributes.width = 2;
            this._clusterFeature.attributes.isClusterBackground = true;
            this._clusterFeature.attributes.isTemp = true;
            this._tempLines.push(this._clusterFeature);

            cur.cluster.sort(function(a, b) {
                return b.clusterAlpha - a.clusterAlpha;
            });
            var alpha = cur.cluster[0].clusterAlpha;
            var deltaSum = 0;
            for (var i = 0; i < cur.cluster.length; i++) {
                deltaSum += (alpha - cur.cluster[i].clusterAlpha);
                cur.cluster[i].clusterAlpha = alpha;
                alpha -= 2 * Math.PI / cur.cluster.length;
            }
            deltaSum = deltaSum / cur.cluster.length;
            var alpha = cur.cluster[0].clusterAlpha;
            for (var i = 0; i < cur.cluster.length; i++) {
                cur.cluster[i].geometry.x = ref * Math.cos(cur.cluster[i].clusterAlpha - deltaSum) + cur.geometry.x;
                cur.cluster[i].geometry.y = ref * Math.sin(cur.cluster[i].clusterAlpha - deltaSum) + cur.geometry.y;
                cur.cluster[i].clusterHide = false;
                var v = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString([cur.cluster[i].geometry, cur.cluster[i].origGeometry]));
                v.attributes.color = "000000";
                v.attributes.width = 2;
                v.attributes.isTemp = true;
                this._tempLines.push(v);
            }
            this._tempDisplay = cur.cluster;
            this.clustering = !0;
            this.layer.addFeatures(this._tempLines);
            this.layer.addFeatures(this._tempDisplay);
            this.clustering = !1;
        } else {
            if (cur.clusterHide) {
//                console.log("cluster over SKIPPED " + (cur ? cur.fid : "?"));
                if (evt.preventDefault) {
                    evt.preventDefault();
                } else {
                    return false;
                }
            } else {
//                console.log("cluster over " + (cur ? cur.fid : "?"));
                if (cur){
                    this.layer.map.echoComponent._showBubble(cur);
                    console.log("EVENT ----------------------------------");
                    console.log(evt);
                }
            }
        }
    },
    _featureout: function(evt) {
        var cur = evt.feature;
        if (cur){
            this.layer.map.echoComponent._hideBubble();
        }else{
//            console.log("no hide");
        }
        if ((this._clusterFeature !== null) && cur.attributes.isClusterBackground) {
            this.clustering = !0;
            for (var i = 0; i < this._tempDisplay.length; i++) {
                this._tempDisplay[i].geometry.x = this._tempDisplay[i].origGeometry.x;
                this._tempDisplay[i].geometry.y = this._tempDisplay[i].origGeometry.y;
                this._tempDisplay[i].clusterHide = true;
            }
            this.layer.removeFeatures(this._tempDisplay);
            this.layer.removeFeatures(this._tempLines);
            this.clustering = !1;
            this._tempDisplay = [];
            this._tempLines = [];
            this._clusterFeature = null;
        }
    },
    shouldCluster: function(cluster, feature) {
        if (feature.geometry.CLASS_NAME !== "OpenLayers.Geometry.Point") {
//            console.log("not clustering 1 " + feature.fid + " / " + feature.geometry.CLASS_NAME);
            return false;
        }
        if (cluster.cluster[0].geometry.CLASS_NAME !== "OpenLayers.Geometry.Point") {
//            console.log("not clustering 2 " + feature.fid + " / " + feature.geometry.CLASS_NAME + " with " + cluster.cluster[0].geometry.CLASS_NAME);
            return false;
        }
        var c = feature.geometry.getBounds().getCenterLonLat(), d = cluster.geometry.getBounds().getCenterLonLat();
        var dlon = c.lon - d.lon;
        var dlat = c.lat - d.lat;
        var res = (dlon * dlon + dlat * dlat) <= this.distance * this.distance * this.resolution * this.resolution;
//        console.log("fid " + feature.fid + " cid=" + cluster.fid + " d=" + (Math.sqrt(Math.pow(c.lon - d.lon, 2) + Math.pow(c.lat - d.lat, 2)) / this.resolution) + " max=" + this.distance+" res="+res);
        return res;

//        return Math.sqrt(Math.pow(c.lon - d.lon, 2) + Math.pow(c.lat - d.lat, 2)) / this.resolution <= this.distance;
    },
    addToCluster: function(a, b) {
        a.cluster.push(b);
        a.attributes.count += 1;
    },
    createCluster: function(feature) {
        var pt = feature.geometry.getBounds().getCenterLonLat();
        //  if (feature.geometry.CLASS_NAME !== "OpenLayers.Geometry.Point") {
        //      console.log("createCluster =>");
        //     console.log(pt);
        //  }
        var clusterFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(pt.lon, pt.lat), {count: 1});
        clusterFeature.isTemp = true;
        clusterFeature.cluster = [feature];
        clusterFeature.fid = "Cluster." + (this._lastId++);
        clusterFeature.clusterExplode = this;
        clusterFeature.noFlyOver = true;
        return clusterFeature;
    },
    CLASS_NAME: "OpenLayers.Strategy.ClusterExplode"});
