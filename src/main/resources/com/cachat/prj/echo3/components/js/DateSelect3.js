console.log("loaded DateSelect3.js");
DateSelect3 = {};
DateSelect3 = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("DateSelect3", this);
    },
    componentType: "DateSelect3"
});
DateSelect3.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("DateSelect3", this);
        componentType : "DateSelect3";
        document.head.innerHTML = document.head.innerHTML
                + '<link rel="stylesheet" type="text/css" href="echo3extended/css/jquery-ui-datepicker.css" />'
                + '<link rel="stylesheet" type="text/css" href="echo3extended/css/jquery-ui-timepicker-addon.css" />';
    },
    _field: null,
    renderAdd: function (update, parentElement) {
        console.log(update);

        console.log("DateSelect3 renderAdd");

        this._field = document.createElement("input");
        this._field.id = this.component.renderId;

        var date = this.component.render("value");
        var withTime = this.component.render("withTime");
        var withNull = this.component.render("withNull");
        var step = this.component.render("step");
        console.log(this.component.renderId," date ", date, ", withTime ", withTime, ", withNull ", withNull);

        if (withTime) {
            $(this._field).datetimepicker();
        } else {
            $(this._field).datepicker();
        }

        this.component.addListener('change', function (e) {
            console.log("change ",e.v);
            e.source.peer.component.set("value", e.v);
        });

        const _this = this;
        this._field.onchange = function (e) {
            console.log("change ",e.target.value);
            _this.component.fireEvent({type: 'change', source: _this.component, v: e.target.value});
        };
        Echo.Sync.renderComponentDefaults(this.component, this._field);
        parentElement.appendChild(this._field);
        if (date){
            this._field.value=date;
        }
    },
    renderDisplay: function () {
        if (!this._field) {
            console.log("DateSelect3 renderDisplay create");
        }
    },
    renderDispose: function (update) {
        console.log("DateSelect3 renderDispose " + update);
        this._field = null;
    },
    renderUpdate: function (update) {
        console.log("DateSelect3 renderUpdate ",update);
        if (update.hasUpdatedProperties()) {
            const properties = update.getUpdatedPropertyNames();
            for (var property of properties) {
                console.log("DateSelect3 renderUpdate ",property, " = ", this.component.get(property));
                this._field[property] = this.component.get(property);
            }
        }
        var date = this.component.render("date");
        console.log("DateSelect3 renderUpdate date ",date);
        return true;
    }
});
