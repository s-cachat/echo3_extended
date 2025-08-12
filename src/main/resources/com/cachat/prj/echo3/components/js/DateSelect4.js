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
    _dateField: null,
    _timeField: null,
    withTime: false,
    setDate: function (date) {
        if (date) {
            var dt;
            console.log("in1 ", date);
            if (date.match(/\d\d?\/\d\d\/\d\d\d\d \d\d:\d\d(\/\d\d)?/)) {
                const words = date.split(/[\/ ]/);
                console.log(words);
                console.log(words[2] + "-" + words[1] + "-" + words[0] + "T" + words[3]);
                dt = new Date(words[2] + "-" + words[1] + "-" + words[0] + "T" + words[3]);
                console.log("date cnv 1 ", date, dt);
            } else {
                dt = new Date(date);
                console.log("date cnv 2 ", date, dt);
            }
            var varDate = new Intl.DateTimeFormat("fr-FR", {dateStyle: "short"}).format(dt);
            this._dateField.value = varDate;
            if (this.withTime) {
                var varTime = new Intl.DateTimeFormat("fr-FR", {timeStyle: "short"}).format(dt);
                console.log("Date ", varDate, "Time", varTime);
                this._timeField.value = varTime;
            } else {
                console.log("Date ", varDate);
            }
        }
    },
    fireEvent: function () {
        console.log(this._dateField._flatpickr.selectedDates);
        var dt = this._dateField._flatpickr.selectedDates[0];
        var value = new Intl.DateTimeFormat("fr-FR").format(dt);
        if (this.withTime) {
            value = value + " " + this._timeField.value;
        }
        console.log("fireEvent date=", value);
        this.component.fireEvent({type: 'change', source: this.component, v: value});
    },
    renderAdd: function (update, parentElement) {
        console.log(update);

        var date = this.component.render("value");
        var date2 = this.component.render("value2");
        this.withTime = this.component.render("withTime");
        var withNull = this.component.render("withNull");
        var locale = this.component.render("locale");
        var range = this.component.render("range");

        this._div = document.createElement("div");
        this._dateField = document.createElement("input");
        this._dateField.id = this.component.renderId;

        conf = {};
        if (locale !== undefined) {
            conf.locale = locale;
        }
        if (this.withTime) {
            this._timeField = document.createElement("input");
            this._timeField.id = this.component.renderId + "t";
            conf.dateFormat = "d/m/Y";
        } else if (range) {
            conf.dateFormat = "d/m/Y";
            conf.mode = "range";
        } else {
            conf.dateFormat = "d/m/Y";
        }
        if (date2 !== undefined) {
            if (date !== undefined) {
                conf.defaultDate = [date, date2];
            } else {
                conf.defaultDate = [date2];
            }
        } else {
            conf.defaultDate = date;
        }


        if (this._timeField !== null) {
            var t = document.createElement("table");
            parentElement.appendChild(t);
            var tr = document.createElement("tr");
            t.appendChild(tr);
            var td = document.createElement("td");
            tr.appendChild(td);
            td.appendChild(this._dateField);
            td.style.width = "60%";
            var th = document.createElement("th");
            tr.appendChild(th);
            th.appendChild(this._timeField);
            td.style.width = "40%";
            $(this._timeField).clockTimePicker({
                minimum: 0,
                alwaysSelectHoursFirst: true
            });
        } else {
            parentElement.appendChild(this._dateField);
        }
        flatpickr(this._dateField, conf);
        this.setDate(date);
        const _this = this;
        this.component.addListener('change', function (e) {
            console.log("change ", e.v);
            _this.setDate(e.v);
        });
        this._dateField.onchange = function (e) {
            _this.fireEvent();
        };
        if (this._timeField !== null) {
            this._timeField.onchange = function (e) {
                _this.fireEvent();
            };
        }
        Echo.Sync.renderComponentDefaults(this.component, this._dateField);
    },
    renderDisplay: function () {
    },
    renderDispose: function (update) {
        this._dateField = null;
        this._timeField = null;
    },
    renderUpdate: function (update) {
        if (update.hasUpdatedProperties()) {
            const properties = update.getUpdatedPropertyNames();
            for (var property of properties) {
                this._dateField[property] = this.component.get(property);
            }
        }
        var date = this.component.render("date");
        this.setDate(date);
        return true;
    }
});
