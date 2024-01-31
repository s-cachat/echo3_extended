QuillEditor = {};
QuillEditor = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("QuillEditor", this);
    },
    doAction: function (message) {
        message.className = "QuillEditor.CellEdited";
        this.fireEvent({
            type: "textUpdate",
            source: this,
            data: message
        });
    },
    componentType: "QuillEditor"
});
QuillEditor.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("QuillEditor", this);
        componentType : "QuillEditor";
        console.log("loading quilleditor css");
        document.head.innerHTML = document.head.innerHTML +'<link rel="stylesheet" type="text/css" href="quilleditor/quill.snow.css" />';
    },
    _div: null,
    _quilleditor: null,
    _data: null,
    _click: null,
    renderAdd: function (update, parentElement) {
        try {
            this._div = document.createElement("div");
            this._div.id = this.component.renderId;

            this._div_tb = document.createElement("div");
            this._div_tb.id = this.component.renderId + "_toolbar";
            this._div.append(this._div_tb);
            this._div_c = document.createElement("div");
            this._div_c.id = this.component.renderId + "_container";
            this._div.append(this._div_c);

            this._div_tb.innerHTML =
                    '    <span class="ql-formats">' +
                    '      <select class="ql-font"></select>' +
                    '      <select class="ql-size"></select>' +
                    '    </span>' +
                    '    <span class="ql-formats">' +
                    '      <button class="ql-bold"></button>' +
                    '      <button class="ql-italic"></button>' +
                    '      <button class="ql-underline"></button>' +
                    '      <button class="ql-strike"></button>' +
                    '    </span>' +
                    '    <span class="ql-formats">' +
                    '      <select class="ql-color"></select>' +
                    '      <select class="ql-background"></select>' +
                    '    </span>' +
                    '    <span class="ql-formats">' +
                    '      <button class="ql-list" value="ordered"></button>' +
                    '      <button class="ql-list" value="bullet"></button>' +
                    '      <button class="ql-indent" value="-1"></button>' +
                    '      <button class="ql-indent" value="+1"></button>' +
                    '    </span>' +
                    '    <span class="ql-formats">' +
                    '      <select class="ql-align"></select>' +
                    '    </span>' +
                    '    <span class="ql-formats">' +
                    '      <button class="ql-link"></button>' +
                    '    </span>' +
                    '    <span class="ql-formats">' +
                    '      <button class="ql-clean"></button>' +
                    '    </span>';

            Echo.Sync.renderComponentDefaults(this.component, this._div);
            Extended.renderPositionnable(this.component, this._div);

            parentElement.appendChild(this._div);
        } catch (e) {
            if (console) {
                console.log("QuillEditor error in renderAdd : " + e);
            }
        }
    },
    renderDisplay: function () {

        try {
            var comp = this.component;
            this._div_c.style.height = this._div.height - 24;
            if (!this._quilleditor) {
                this._quilleditor = new Quill(this._div_c, {
                    modules: {
                        toolbar: this._div_tb
                    },
                    placeholder: '',
                    theme: 'snow'
                });
                var quill = this._quilleditor;
                this._quilleditor.on('text-change', function (delta, oldDelta, source) {
                    comp.set("text", quill.root.innerHTML, true);
                    console.log("quillDisplay text change : ", quill.root.innerHTML);
                });
            }
            var text = comp.get("text");
            if (text) {
                this._quilleditor.root.innerHTML = text;
            }
            console.log("quillDisplay text in  : ", text);
            console.log("quillDisplay text out : ", this._quilleditor.root.innerHTML);

        } catch (e) {
            if (console) {
                console.log("QuillEditor error in renderDisplay : ", e);
            }
        }
    },
    _featureEvent: function (e) {
        this.peer.component.doAction("feature");
    },
    renderDispose: function (update) {
        if (this._quilleditor) {
            this._quilleditor = null;
        }
        this._div = null;
    },
    renderUpdate: function (update) {
        try {
            if (update._propertyUpdates.textChange) {
                console.log("textChange");
                console.log(update);
                //TODO
            } else {
                console.log("Other update", update);
            }
        } catch (e) {
            if (console) {
                console.log("QuillEditor error in renderUpdate : ");
                console.log(e);
            }
        }
        return true;
    }
}
);
