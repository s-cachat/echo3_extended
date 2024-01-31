FlotChart = {};
FlotChart = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("FlotChart", this);
    },
    componentType: "FlotChart"
});
FlotChart.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("FlotChart", this);
        componentType : "FlotChart";
    },
    _div: null,
    _flotchart: null,
    _click: null,
    renderAdd: function (update, parentElement) {
        try {
            this._div = document.createElement("div");
            this._div.id = this.component.renderId;

            Echo.Sync.renderComponentDefaults(this.component, this._div);
            Extended.renderPositionnable(this.component, this._div);
            parentElement.appendChild(this._div);
        } catch (e) {
            if (console) {
                console.log("FlotChart error in renderAdd : " + e);
            }
        }
    },
    renderDisplay: function () {
        try {
            if (!this._flotchart) {
                this._flotchart = this._buildChart();
            }
        } catch (e) {
            if (console) {
                console.log("FlotChart error in renderDisplay : " + e);
            }
        }
    },
    _buildChart: function () {
        try {
            var chartModel = JSON.parse(this.component.get("model")).flotModel;
            var data = JSON.parse(this.component.get("data")).flotData.series;
            chartModel.series = undefined;
            if (chartModel.pie !== undefined) {
                chartModel.series = new Object();
                chartModel.series.pie = chartModel.pie;
            }
            var chart = $.plot(this._div, data, chartModel);
            return chart;
        } catch (e) {
            throw ("FlotChart error in _buildchart : " + e);
            return undefined;
        }
    },
    _featureEvent: function (e) {
        this.peer.component.doAction("feature");
    },
    renderDispose: function (update) {
        if (this._flotchart) {
            this._flotchart.shutdown();
            this._flotchart = null;
        }
        this._div = null;
    },
    renderUpdate: function (update) {
        try {
            var _data = this.component.get("data");
            var _model = this.component.get("model");
            console.log("update chart " + this.component.renderId);
            console.log(_data);
            console.log(_model);
            if (_data !== undefined) {
                var data = JSON.parse(_data).flotData.series;
                if (_model !== undefined) {
                    var chartModel = JSON.parse(_model).flotModel;
                    chartModel.series = undefined;
                    this._flotchart = $.plot(this._div, data, chartModel);
                } else {
                    this._flotchart.setData(data);
                }
            }
        } catch (e) {
            if (console) {
                console.log("FlotChart error in renderUpdate : " + e);
            }
        }
        return true;
    }
}
);
