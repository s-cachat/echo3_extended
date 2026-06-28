console.log("loaded Gauge.js");
Gauge = {};
Gauge = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("Gauge", this);
    },
    componentType: "Gauge"
});
Gauge.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("Gauge", this);
        componentType : "Gauge";
    },
    _field: null,
    renderAdd: function (update, parentElement) {
        console.log(update);
        this._field = document.createElement("canvas");
        this._field.id = this.component.renderId;
        var opts = {
            angle: -0.2, // The span of the gauge arc
            lineWidth: 0.2, // The line thickness
            radiusScale: 1, // Relative radius
            pointer: {
                length: 0.4, // // Relative to gauge radius
                strokeWidth: 0.044, // The thickness
                color: '#000000' // Fill color
            },
            colorStart: '#6FADCF',
            colorStop: '#8FC0DA',
            strokeColor: '#E0E0E0',
            generateGradient: true,
            highDpiSupport: true, // High resolution support

        };
        this.gauge = new Gauge(this._field)
        try {
            console.log(this.gauge);
            this.gauge.setOptions(opts);
            this.gauge.maxValue = this.component.render("maxValue");
            this.gauge.minValue = this.component.render("minValue");
            this.gauge.set(this.component.render("value"));
        } catch (e) {
            console.log(e);
        }

        const _this = this;
        Echo.Sync.renderComponentDefaults(this.component, this._field);
        parentElement.appendChild(this._field);
    },
    renderDisplay: function () {
        if (!this._field) {
            console.log("Gauge renderDisplay create");
        }
    },
    renderDispose: function (update) {
        console.log("Gauge renderDispose " + update);
        this._field = null;
    },
    renderUpdate: function (update) {
        console.log("Gauge renderUpdate ", update);
        if (update.hasUpdatedProperties()) {
            const properties = update.getUpdatedPropertyNames();
            for (var property of properties) {
                console.log("Gauge renderUpdate ", property, " = ", this.component.get(property));
                this._field[property] = this.component.get(property);
            }
        }
        return true;
    }
});
