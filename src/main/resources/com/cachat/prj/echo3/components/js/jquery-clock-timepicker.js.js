console.log("loaded DateSelect4.js");
DateSelect4 = {};
DateSelect4 = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("DateSelect4", this);
    },
    componentType: "DateSelect4"
});
DateSelect4.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("DateSelect4", this);
        componentType : "DateSelect4";
        document.head.innerHTML = document.head.innerHTML
                + '<link rel="stylesheet" type="text/css" href="echo3extended/css/flatpicker.min.css" />';
        ;
    },
    _field: null,
    renderAdd: function (update, parentElement) {
        console.log(update);

        console.log("DateSelect4 renderAdd");

        this._field = document.createElement("input");
        this._field.id = this.component.renderId;

        var date = this.component.render("value");
        var date2 = this.component.render("value2");
        var withTime = this.component.render("withTime");
        var withNull = this.component.render("withNull");
        var locale = this.component.render("locale");
        var range = this.component.render("range");
        console.log(this.component.renderId, " date ", date, ", withTime ", withTime, ", withNull ", withNull);
        conf = {};
        if (locale !== undefined) {
            conf.locale = locale;
        }
        if (withTime) {
            conf.enableTime = true;
            conf.dateFormat = "d/m/Y H:i";
            conf.time_24hr=true;
        } else if (range) {
            conf.dateFormat = "d/m/Y";
            conf.mode = "range";
        } else {
            conf.dateFormat = "d/m/Y";
        }
        if (date2!==undefined){
            if (date!==undefined){
                conf.defaultDate=[date,date2];
            }else{
                conf.defaultDate=[date2];
            }
        }else{
            conf.defaultDate=date;
        }
            
        flatpickr(this._field, conf);

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
        if (date) {
            this._field.value = date;
        }
    },
    renderDisplay: function () {
        if (!this._field) {
            console.log("DateSelect4 renderDisplay create");
        }
    },
    renderDispose: function (update) {
        console.log("DateSelect4 renderDispose " + update);
        this._field = null;
    },
    renderUpdate: function (update) {
        console.log("DateSelect4 renderUpdate ", update);
        if (update.hasUpdatedProperties()) {
            const properties = update.getUpdatedPropertyNames();
            for (var property of properties) {
                console.log("DateSelect4 renderUpdate ", property, " = ", this.component.get(property));
                this._field[property] = this.component.get(property);
            }
        }
        var date = this.component.render("date");
        console.log("DateSelect4 renderUpdate date ", date);
        return true;
    }
});
