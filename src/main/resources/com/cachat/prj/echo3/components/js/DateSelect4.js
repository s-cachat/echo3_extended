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
    },
    _dateField: null,
    _date2Field: null,
    _timeField: null,
    _deleteButton: null,
    withTime: false,
    withNull: false,
    parseDate: function (date) {
        var dt;
        console.log("in1 ", date);
        if (date.match(/\d\d?\/\d\d\/\d\d\d\d \d\d:\d\d(\/\d\d)?/)) {
            const words = date.split(/[\/ ]/);
            console.log(words);
            console.log(words[2] + "-" + words[1] + "-" + words[0] + "T" + words[3]);
            dt = new Date(words[2] + "-" + words[1] + "-" + words[0] + "T" + words[3]);
            console.log("date cnv 1 ", date, dt);
        } else if (date.match(/\d\d?\/\d\d\/\d\d\d\d/)) {
            const words = date.split(/[\/ ]/);
            console.log(words);
            console.log(words[2] + "-" + words[1] + "-" + words[0]);
            dt = new Date(words[2] + "-" + words[1] + "-" + words[0]);
            console.log("date cnv 1 ", date, dt);
        } else {
            dt = new Date(date);
            console.log("date cnv 2 ", date, dt);
        }
        return dt;
    },
    setDate: function (date, date2) {
        if (date) {
            var dt = this.parseDate(date);

            try {
                var varDate = new Intl.DateTimeFormat("fr-FR", {dateStyle: "short"}).format(dt);
                this._dateField.value = varDate;
                if (this.withTime) {
                    var varTime = new Intl.DateTimeFormat("fr-FR", {timeStyle: "short"}).format(dt);
                    console.log("Date ", varDate, "Time", varTime);
                    this._timeField.value = varTime;
                } else {
                    console.log("Date ", varDate);
                }
            } catch (e) {
                console.log("Bad date ", dt, " : ", e);
            }
        } else if (this.withNull) {
            this._dateField.value = null;
            if (this.withTime) {
                this._timeField.value = null;
            }
        }
        if (this.range) {
            if (date2) {
                var dt = this.parseDate(date2);
                try {
                    var varDate = new Intl.DateTimeFormat("fr-FR", {dateStyle: "short"}).format(dt);
                    //this._dateField2.value = varDate;
                } catch (e) {
                    console.log("Bad date2 ", dt, " : ", e);
                }
            } else if (this.withNull) {
                //this._dateField2.value = null;
            }
        }
    },
    fireEvent: function () {
        console.log(this._dateField._flatpickr.selectedDates);
        var dt = this._dateField._flatpickr.selectedDates[0];
        var value;
        if (!dt) {
            // aucune date séléctionnée (i.e. modif de l'horaire uniquement)
            value = this._dateField.value;
        } else {
            value = new Intl.DateTimeFormat("fr-FR").format(dt);
        }
        if (this.withTime) {
            value = value + " " + this._timeField.value;
        }
        console.log("fireEvent date=", value);
        this.component.set("value", value);
        if (this.range) {
            dt = this._date2Field._flatpickr.selectedDates[0];
            var value2;
            if (!dt) {
                // aucune date séléctionnée (i.e. modif de l'horaire uniquement)
                value2 = this._date2Field.value;
            } else {
                value2 = new Intl.DateTimeFormat("fr-FR").format(dt);
            }
            this.component.set("value2", value2);
        }
        this.component.fireEvent({type: 'change', source: this.component, v: value});
    },
    renderAdd: function (update, parentElement) {
        console.log("renderAdd", update);

        var date = this.component.render("value");
        var date2 = this.component.render("value2");
        this.withTime = this.component.render("withTime");
        this.withNull = this.component.render("withNull");
        var locale = this.component.render("locale");
        this.range = this.component.render("range");

        this._div = document.createElement("div");
        this._dateField = document.createElement("input");
        this._dateField.id = this.component.renderId;

        conf = {};
        if (locale !== undefined) {
            conf.locale = locale;
        } else {
            conf.locale = "fr";
        }
        if (this.withTime) {
            this._timeField = document.createElement("input");
            this._timeField.id = this.component.renderId + "t";
        } else if (this.range) {
            conf.mode = "range";
        }
        conf.dateFormat = "d/m/Y";

        if (this.withNull) {
            this._deleteButton = document.createElement("button");
            this._deleteButton.id = this.component.renderId + "d";
            this._deleteButton.textContent = "X";
            this._deleteButton.style.color = "red";
            this._deleteButton.style.background = "transparent";
            this._deleteButton.style.border = "none";
            this._deleteButton.style.padding = "0";
            this._deleteButton.style.cursor = "pointer";
            this._deleteButton.style.fontWeight = "bold";
            this._deleteButton.style.textAlign = "center";
            const t = this;
            this._deleteButton.addEventListener("click", () => {
                t.setDate(null, null);
                t.fireEvent();
            }, false);
        }

        this.setWidth(this.component.render("width"));

        if (this._timeField != null) {
            var t = document.createElement("table");
            parentElement.appendChild(t);
            var tr = document.createElement("tr");
            t.appendChild(tr);
            var td = document.createElement("td");
            tr.appendChild(td);
            td.appendChild(this._dateField);
            td = document.createElement("td");
            tr.appendChild(td);
            td.appendChild(this._timeField);
            $(this._timeField).clockTimePicker({
                minimum: 0,
                alwaysSelectHoursFirst: true
            });
            if (this._deleteButton != null) {
                td = document.createElement("td");
                tr.appendChild(td);
                td.appendChild(this._deleteButton);
            }
        } else {
            parentElement.appendChild(this._dateField);
        }
        flatpickr(this._dateField, conf);
        const _this = this;
        this.component.addListener('change', function (e) {
            console.log("change ", e.v);
//            _this.setDate(e.v);
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
        this.setDate(date, date2);
    },
    renderDisplay: function () {
    },
    renderDispose: function (update) {
        this._dateField = null;
        this._timeField = null;
    },
    renderUpdate: function (update) {
        console.log("renderUpdate", update);
        if (update.hasUpdatedProperties()) {
            const properties = update.getUpdatedPropertyNames();
            var date = undefined;
            var date2 = undefined;
            for (var property of properties) {
                if ("value" === property) {
                    date = this.component.get(property);
                } else if ("value2" === property) {
                    date2 = this.component.get(property);
                }
            }
            if (date !== undefined || date2 !== undefined) {
                this.setDate(date, date2);
            }
        }
        return true;
    },
    setWidth: function (width) {
        console.log("DATE SELECT", "SET WIDTH", width);
        if (width == null) {
            this._dateField.style.width = "";
            if (this._timeField !== null) {
                this._timeField.style.width = "";
            }
            return;
        }
        if (this._timeField !== null) {
            if (typeof (width) === "number") {
                this._dateField.style.width = Echo.Sync.Extent.toCssValue(width * 0.5, true, true);
                this._timeField.style.width = Echo.Sync.Extent.toCssValue(width * 0.5, true, true);
            } else {
                if (Echo.Sync.Extent.isPercent(width)) {
                    this._dateField.style.width = Echo.Sync.Extent.toCssValue(width, true, true);
                    this._timeField.style.width = Echo.Sync.Extent.toCssValue(width, true, true);
                } else {
                    var pixels = Echo.Sync.Extent.toPixels(width, true);
                    if (pixels != null) {
                        this._dateField.style.width = Echo.Sync.Extent.toCssValue(pixels * 0.5, true, true);
                        this._timeField.style.width = Echo.Sync.Extent.toCssValue(pixels * 0.5, true, true);
                    } else {
                        console.log("DATE SELECT", "SET WIDTH", "Unable to parse width");
                    }
                }
            }
        } else {
            this._dateField.style.width = Echo.Sync.Extent.toCssValue(width, true, true);
        }
    }
});
