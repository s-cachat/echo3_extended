ChartJs = {};
ChartJs = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("ChartJs", this);
    },
    componentType: "ChartJs"
});
ChartJs.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("ChartJs", this);
        componentType : "ChartJs";
    },
    _div: null,
    _canvas: null,
    _chartjs: null,
    _click: null,
    renderAdd: function (update, parentElement) {
        try {
            v = this.component.render("withDiv");
            if (v) {
                this._div = document.createElement("div");
                this._div.id = this.component.renderId;
                Echo.Sync.renderComponentDefaults(this.component, this._div);
                Extended.renderPositionnable(this.component, this._div);
                parentElement.appendChild(this._div);
                this._canvas = document.createElement("canvas");
                this._canvas.id = this.component.renderId + "_canvas";
                this._div.appendChild(this._canvas);
                console.log("chartJs with div", v);
            } else {
                this._canvas = document.createElement("canvas");
                this._canvas.id = this.component.renderId;
                Echo.Sync.renderComponentDefaults(this.component, this._canvas);
                Extended.renderPositionnable(this.component, this._canvas);
                parentElement.appendChild(this._canvas);
                console.log("chartJs without div", v);
            }
        } catch (e) {
            if (console) {
                console.log("ChartJs error in renderAdd : ", e);
            }
        }
    },
    renderDisplay: function () {
        try {
            if (!this._chartjs) {
                var config = this.component.get("config");
                this._chartConfig = JSON.parse(config).chartModel;
                this._chartjs = new Chart(this._canvas, this._chartConfig);
            }
        } catch (e) {
            if (console) {
                console.log("ChartJs error in renderDisplay : ", e);
            }
        }
    },
    _featureEvent: function (e) {
        this.peer.component.doAction("feature");
    },
    renderDispose: function (update) {
        if (this._chartjs) {
            this._chartjs.destroy();
            this._chartjs = null;
        }
        this._canvas = null;
        this._div = null;
    },
    renderUpdate: function (update) {
        try {
            if (update.hasUpdatedProperties()) {
                const properties = update.getUpdatedPropertyNames();
                for (var property of properties) {
                    if ("config" === property) {
                        var config = JSON.parse(this.component.get("config")).chartModel;
                        console.log("update chart " + this.component.renderId, " new model with data : ", config);
                        this._chartjs.data = config.data;
                        this._chartjs.update();
                    } else if ("data" === property) {
                        var data = JSON.parse(this.component.get("data")).chartData;
                        console.log("update chart " + this.component.renderId, " new data ", data);
                        this._chartjs.data = data;
                        this._chartjs.update();
                    }
                }
                if (date !== undefined || date2 !== undefined) {
                    this.setDate(date, date2);
                }
            }

        } catch (e) {
            if (console) {
                console.log("ChartJs error in renderUpdate : " + e);
            }
        }
        return true;
    }
}
);
