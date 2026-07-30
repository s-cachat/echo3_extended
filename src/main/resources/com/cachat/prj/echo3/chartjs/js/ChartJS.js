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
    _chartjs: null,
    _click: null,
    renderAdd: function (update, parentElement) {
        try {
            this._div = document.createElement("div");
            this._div.id = this.component.renderId;
            Echo.Sync.renderComponentDefaults(this.component, this._div);
            Extended.renderPositionnable(this.component, this._div);
            parentElement.appendChild(this._div);
            this._canvas = document.createElement("canvas");
            this._canvas.id = this.component.renderId + "_canvas";
            this._div.appendChild(this._canvas);
        } catch (e) {
            if (console) {
                console.log("ChartJs error in renderAdd : ", e);
            }
        }
    },
    renderDisplay: function () {
        try {
            if (!this._chartjs) {
                var model=this.component.get("config");
                console.log("model",model);
                this._chartModel = JSON.parse(model).chartModel;
                this._chartjs = new Chart(this._canvas, this._chartModel);
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
            var _model = this.component.get("model");
            if (_model !== undefined) {
                var model = JSON.parse(_model).chartModel;
                console.log("update chart " + this.component.renderId, " new model ", model);
                this._chartjs.data = model.data;
                this._chartjs.update();
            } else {
                var _data = this.component.get("data");
                if (_data !== undefined) {
                    var data = JSON.parse(_data).chartData;
                    console.log("update chart " + this.component.renderId, " new data ", data);
                    this._chartjs.data = data;
                    this._chartjs.update();
                } else {
                    console.log("update chart " + this.component.renderId, " skipped");
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
