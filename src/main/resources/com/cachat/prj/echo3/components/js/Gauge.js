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

        this.gauge = new Gauge(this._field).setOptions(opts);
        this.gauge.maxValue = this.component.render("maxValue");
        this.gauge.setMinValue(this.component.render("minValue"));
        this.gauge.set(this.component.render("value"));
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
