Synoptique = {};
Synoptique = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("Synoptique", this);
        console.log("Synoptique load : ", this.toString(1));
    },
    componentType: "Synoptique"
});
/**
 * Date Property Translator Singleton.
 */
Synoptique.SerialEvent = Core.extend(Echo.Serial.PropertyTranslator, {

    $static: {
        /** @see Echo.Serial.PropertyTranslator#toProperty */
        toProperty: function (client, pElement) {
            return {};
        },

        /** @see Echo.Serial.PropertyTranslator#toXml */
        toXml: function (client, propertyElement, propertyValue) {
            if (propertyValue.uid) {
                propertyElement.setAttribute("uid", propertyValue.uid);
            }
            if (propertyValue.left) {
                propertyElement.setAttribute("left", propertyValue.left);
            }
            if (propertyValue.top) {
                propertyElement.setAttribute("top", propertyValue.top);
            }
            if (propertyValue.width) {
                propertyElement.setAttribute("width", propertyValue.width);
            }
            if (propertyValue.height) {
                propertyElement.setAttribute("height", propertyValue.height);
            }
            if (propertyValue.angle) {
                propertyElement.setAttribute("angle", propertyValue.angle);
            }
        }
    },

    $load: function () {
        Echo.Serial.addPropertyTranslator("SynModifiedEvent", this);
    }
});
/**
 * Date Property Translator Singleton.
 */
Synoptique.Action = Core.extend(Echo.Serial.PropertyTranslator, {

    $static: {
        /** @see Echo.Serial.PropertyTranslator#toProperty */
        toProperty: function (client, pElement) {
            var actions = {
                add: [],
                del: [],
                update: []
            };
            console.log(pElement);
            var eActions = pElement.childNodes[0].childNodes;
            for (eAction of pElement.childNodes[0].childNodes) {
                var action = {};
                for (const eVal of eAction.childNodes) {
                    switch (eVal.nodeName) {
                        case "angle":
                            action.angle = parseFloat(eVal.textContent);
                            break;
                        case "height":
                            action.height = parseFloat(eVal.textContent);
                            break;
                        case "left":
                            action.left = parseFloat(eVal.textContent);
                            break;
                        case "top":
                            action.top = parseFloat(eVal.textContent);
                            break;
                        case "width":
                            action.width = parseFloat(eVal.textContent);
                            break;
                        case "clickable":
                            action.clickable = "true" === eVal.textContent;
                            break;
                        case "movable":
                            action.moveable = "true" === eVal.textContent;
                            break;
                        case "resizeable":
                            action.resizeable = "true" === eVal.textContent;
                            break;
                        case "visible":
                            action.visible = "true" === eVal.textContent;
                            break;
                        case "uid":
                            action.uid = "true" === eVal.textContent;
                            break;
                        case "view":
                            action.view = {};
                            action.view.type = eVal.getAttribute("xsi:type");
                            for (const eView of eVal.childNodes) {
                                switch (eView.nodeName) {
                                    case "fill":
                                        action.view.fill = parseInt(eView.textContent);
                                        break;
                                    case "opacity":
                                        action.view.opacity = parseInt(eView.textContent);
                                        break;
                                    case "stroke":
                                        action.view.stroke = parseInt(eView.textContent);
                                        break;
                                    case "strokeWidth":
                                        action.view.strokeWidth = parseInt(eView.textContent);
                                        break;
                                    case "uid":
                                        action.view.uid = eView.textContent;
                                        break;
                                    case "contentType":
                                        action.view.contentType = eView.textContent;
                                        break;
                                    case "subType":
                                        action.view.subType = eView.textContent;
                                        break;
                                    default:
                                        console.log("unknown view property\"" + eView.nodeName + "\"");
                                }
                            }
                            break;
                        default:
                            console.log("unknown action property\"" + eVal.nodeName + "\"");
                    }
                }
                switch (eAction.nodeName) {
                    case "add":
                        actions.add.push(action);
                        break;
                    case "del":
                        actions.del.push(action);
                        break;
                    case "update":
                        actions.update.push(action);
                        break;
                    default:
                        console.log("unknown action \"" + eAction.nodeName + "\"");
                }
            }
            console.log("parsed actions", actions);
            return actions;
        },
        /** @see Echo.Serial.PropertyTranslator#toXml */
        toXml: function (client, propertyElement, propertyValue) {

        }
    },
    $load: function () {
        Echo.Serial.addPropertyTranslator("SynAction", this);
    }
});
Synoptique.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("Synoptique", this);
        componentType : "Synoptique";
    },
    _div: null,
    _canvas: null,
    _fabric: null,
    _content: {},
    renderAdd: function (update, parentElement) {
        console.log("render add update", update, ", action", this.component.get("action"));
        console.log("Synoptique renderAdd");
        this._div = document.createElement("div");
        this._div.id = this.component.renderId;
        Echo.Sync.renderComponentDefaults(this.component, this._div);
        Extended.renderPositionnable(this.component, this._div);
        parentElement.appendChild(this._div);
        this._canvas = document.createElement("canvas");
        this._canvas.id = this.component.renderId + "_canvas";
        this._div.appendChild(this._canvas);
    },
    _updateObj(action, obj) {
        if (obj !== undefined) {
            if (action.angle) {
                obj.angle = action.angle;
            }
            if (action.top) {
                obj.top = action.top;
            }
            if (action.left) {
                obj.left = action.left;
            }
            if (action.width) {
                obj.width = action.width;
            }
            if (action.height) {
                obj.height = action.height;
            }
        }
    },
    renderDisplay: function () {
        console.log("render display action", this.component.get("action"));
        if (!this._fabric) {
            console.log("Synoptique renderDisplay create this", this);
            this._canvas.width = this._div.offsetWidth;
            this._canvas.height = this._div.offsetHeight;
            this._fabric = new fabric.Canvas(this._canvas);
            var rect = new fabric.Rect({
                left: 100,
                top: 100,
                fill: 'red',
                width: 20,
                height: 20
            });
            rect.uid = "S99";
            this._fabric.add(rect);
            const _this = this;
            rect.on("modified", function (e) {
                _this._modifiedEvent(rect, e);
            });
            rect.on("mouseup", function (e) {
                _this._clicEvent(rect, e);
            });
        } else {
            console.log("Synoptique renderDisplay");
        }
        var actions = this.component.get("action");
        console.log("handle action ", actions);
        for (const action of actions.add) {
            var obj;
            if (action.view) {
                switch (action.view.type) {
                    case "RECT":
                        obj = new fabric.Rect();
                        break;
                }
            }
            if (obj !== undefined) {
                obj.uid = action.uid;
                this._fabric.add(obj);
                this._content[obj.uid] = obj;
                this.updateObj(action, obj);
                const _this = this;
                if (obj.movable || obj.resizeable) {
                    rect.on("modified", function (e) {
                        _this._modifiedEvent(rect, e);
                    });
                }
                if (obj.clickable) {
                    rect.on("mouseup", function (e) {
                        _this._clicEvent(rect, e);
                    });
                }
            }
        }
        for (const action of actions.update) {
            var obj = this._content[action.uid];
            if (obj !== undefined) {
                this.updateObj(action, obj);
            }
        }
        for (const action of actions.del) {
            var obj = this._content[action.uid];
            if (obj !== undefined) {
                this._fabric.remove(obj);
                this._content[action.uid] = undefined;
            }
        }
    },
    _modifiedEvent(source, event) {
        console.log("modified source", source, " event ", event);
        this.component.fireEvent({
            type: 'objectEdit',
            source: this.component,
            data: {
                className: "SynModifiedEvent",
                left: source.left,
                top: source.top,
                width: source.width,
                height: source.height,
                angle: source.angle,
                uid: source.uid
            }
        });
    },
    _clicEvent(source, event) {
        console.log("modified source", source, " event ", event);
        this.component.fireEvent({
            type: 'objectClic',
            source: this.component,
            data: {
                className: "SynModifiedEvent",
                left: source.left,
                top: source.top,
                width: source.width,
                height: source.height,
                angle: source.angle,
                uid: source.uid
            }
        });
    },
    renderDispose: function (update) {
        console.log("Synoptique renderDispose " + update);
        this._div = null;
    },
    renderUpdate: function (update) {
        console.log("Synoptique renderUpdate ", update, " action", this.component.get("action"));
        console.log(update);
        var pu = update._propertyUpdates;
        if (pu) {
            console.log("pu", pu);
        } else {

        }
        return true;
    }
}
);
