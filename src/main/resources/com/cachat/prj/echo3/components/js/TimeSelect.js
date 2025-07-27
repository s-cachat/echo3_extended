console.log("loaded TimeSelect.js");
TimeSelect = {};
TimeSelect = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("TimeSelect", this);
    },
    componentType: "TimeSelect"
});
TimeSelect.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("TimeSelect", this);
        componentType : "TimeSelect";
    },
    _field: null,
    renderAdd: function (update, parentElement) {
        this._field = document.createElement("input");
        this._field.id = this.component.renderId;

        var time = this.component.render("value");
        conf = {};


        this.component.addListener('change', function (e) {
            console.log("change ", e.v);
            e.source.peer.component.set("value", e.v);
        });

        const _this = this;
        this._field.onchange = function (e) {
            console.log("change ", e.target.value);
            _this.component.fireEvent({type: 'change', source: _this.component, v: e.target.value});
        };
        Echo.Sync.renderComponentDefaults(this.component, this._field);
        parentElement.appendChild(this._field);
        if (time) {
            this._field.value = time;
        }
        $(this._field).clockTimePicker({
            minimum: 0,
            alwaysSelectHoursFirst: true
        });

    },
    renderDisplay: function () {
        if (!this._field) {
            console.log("TimeSelect renderDisplay create");
        }
    },
    renderDispose: function (update) {
        console.log("TimeSelect renderDispose " + update);
        this._field = null;
    },
    renderUpdate: function (update) {
        console.log("TimeSelect renderUpdate ", update);
        if (update.hasUpdatedProperties()) {
            const properties = update.getUpdatedPropertyNames();
            for (var property of properties) {
                console.log("TimeSelect renderUpdate ", property, " = ", this.component.get(property));
                this._field[property] = this.component.get(property);
            }
        }
        var date = this.component.render("date");
        console.log("TimeSelect renderUpdate date ", date);
        return true;
    }
});
