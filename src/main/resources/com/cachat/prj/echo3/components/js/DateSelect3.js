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
                + '<link rel="stylesheet" type="text/css" href="echo3extended/css/jquery.timepicker.css" />'
                + '<link rel="stylesheet" type="text/css" href="echo3extended/css/bootstrap-datepicker.standalone.css" />';
    },
    _container: null,
    renderAdd: function (update, parentElement) {
        console.log(update);
        
        console.log("DateSelect3 renderAdd");

        this._container = document.createElement("span");
        this._container.id = this.component.renderId;

        this._dateInput = document.createElement("input");
        this._container.appendChild(this._dateInput);
        this._dateInput.id = this.component.renderId + "_date";
        this._dateInput.timepicker({
            'showDuration': true,
            'timeFormat': 'g:ia'
        });

        this._timeInput = document.createElement("input");
        this._container.appendChild(this._timeInput);
        this._timeInput.id = this.component.renderId + "_date";
        this._timeInput.datepicker({
            'format': 'm/d/yyyy',
            'autoclose': true
        });

        this._container.datepair();

        this.component.addListener('change', function (e) {
            e.source.peer.component.set("value", e.v);
        });
        const enableInputEvents = this.component.get('enableInputEvent');
        if (enableInputEvents === true) {
            this.component.addListener('input', function (e) {
                e.source.peer.component.set("value", e.v);
            });
        }
        const _this = this;
        this._container.onchange = function (e) {
            _this.component.fireEvent({type: 'change', source: _this.component, v: e.target.valueAsNumber});
        };
        if (enableInputEvents === true) {
            this._container.oninput = function (e) {
                _this.component.fireEvent({type: 'input', source: _this.component, v: e.target.valueAsNumber});
            };
        }

        Echo.Sync.renderComponentDefaults(this.component, this._container);
        parentElement.appendChild(this._container);
    },
    renderDisplay: function () {
        if (!this._container) {
            console.log("DateSelect3 renderDisplay create");
        }
    },
    renderDispose: function (update) {
        console.log("DateSelect3 renderDispose " + update);
        this._container = null;
    },
    renderUpdate: function (update) {
        console.log("DateSelect3 renderUpdate");
        if (update.hasUpdatedProperties()) {
            const properties = update.getUpdatedPropertyNames();
            for (var property of properties) {
                this._container[property] = this.component.get(property);
            }
        }
        return true;
    }
});