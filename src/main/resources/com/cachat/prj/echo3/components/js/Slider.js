Slider = {};
Slider = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("Slider", this);
    },
    componentType: "Slider"
});
Slider.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("Slider", this);
        componentType : "Slider";
    },
    _slider: null,
    renderAdd: function (update, parentElement) {
        console.log("Slider renderAdd");

        this._slider = document.createElement("input");
        this._slider.id = this.component.renderId;
        this._slider.type = "range";
        if (this.component.get('max') !== undefined) {
            this._slider.max = this.component.get('max');
        }
        if (this.component.get('min') !== undefined) {
            this._slider.min = this.component.get('min');
        }
        if (this.component.get('step') !== undefined) {
            this._slider.step = this.component.get('step');
        }
        if (this.component.get('value') !== undefined) {
            this._slider.value = this.component.get('value');
        }
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
        this._slider.onchange = function (e) {
            _this.component.fireEvent({type: 'change', source: _this.component, v: e.target.valueAsNumber});
        };
        if (enableInputEvents === true) {
            this._slider.oninput = function (e) {
                _this.component.fireEvent({type: 'input', source: _this.component, v: e.target.valueAsNumber});
            };
        }

        Echo.Sync.renderComponentDefaults(this.component, this._slider);
        parentElement.appendChild(this._slider);
    },
    renderDisplay: function () {
        if (!this._slider) {
            console.log("Slider renderDisplay create");
        }
    },
    renderDispose: function (update) {
        console.log("Slider renderDispose " + update);
        this._slider = null;
    },
    renderUpdate: function (update) {
        console.log("Slider renderUpdate");
        if (update.hasUpdatedProperties()) {
            const properties = update.getUpdatedPropertyNames();
            for (var property of properties) {
                this._slider[property] = this.component.get(property);
            }
        }
        return true;
    }
});