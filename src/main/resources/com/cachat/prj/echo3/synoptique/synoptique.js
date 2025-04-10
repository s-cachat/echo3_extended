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
                            action.movable = "true" === eVal.textContent;
                            break;
                        case "resizeable":
                            action.resizeable = "true" === eVal.textContent;
                            break;
                        case "visible":
                            action.visible = "true" === eVal.textContent;
                            break;
                        case "uid":
                            action.uid = eVal.textContent;
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
                                        action.view.strokeWidth = parseFloat(eView.textContent);
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
                                    case "text":
                                        action.view.text = eView.textContent;
                                        break;
                                    case "fontSize":
                                        action.fontSize = parseFloat(eVal.textContent);
                                        break;
                                    case "underline":
                                        action.underline = "true" === eVal.textContent;
                                        break;
                                    case "linethrough":
                                        action.linethrough = "true" === eVal.textContent;
                                        break;
                                    case "overline":
                                        action.overline = "true" === eVal.textContent;
                                        break;
                                    case "fontStyle":
                                        action.view.fontStyle = eView.textContent;
                                        break;
                                    case "fontFamily":
                                        action.view.fontFamily = eView.textContent;
                                        break;
                                    case "textAlign":
                                        action.view.textAlign = eView.textContent;
                                        break;
                                    case "textBackgroundColor":
                                        action.view.textBackgroundColor = parseInt(eView.textContent);
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
            var setCoord = false;
            if (action.angle) {
                obj.angle = action.angle;
                setCoord = true;
            }
            if (action.top) {
                obj.top = action.top;
                setCoord = true;
            }
            if (action.left) {
                obj.left = action.left;
                setCoord = true;
            }
            console.log("set position of ", obj.uid, " to ", obj.left, "x", obj.top);
            if (obj.hasRadius) {
                if (action.width) {
                    obj.radius = action.width / 2;
                    setCoord = true;
                }
                if (action.height && (action.width === undefined || action.height < action.width)) {
                    obj.radius = action.height / 2;
                    setCoord = true;
                }
                console.log("set radius of ", obj.uid, " to ", obj.radius);
            } else {
                if (action.width) {
                    obj.width = action.width;
                    setCoord = true;
                }
                if (action.height) {
                    obj.height = action.height;
                    setCoord = true;
                }
                console.log("set size of ", obj.uid, " to ", obj.width, "x", obj.height);
            }
            if (setCoord) {
                console.log("setCoord for ", obj.uid);
                obj.setCoords();
            }
            if (action.view.fill) {
                obj.fill = "#" + action.view.fill.toString(16).padStart(6, '0');
            }
            if (action.view.stroke) {
                obj.stroke = "#" + action.view.stroke.toString(16).padStart(6, '0');
            }
            if (action.view.strokeWidth) {
                obj.strokeWidth = action.view.strokeWidth;
            }
            var hasControl = undefined;
            var selectable = undefined;

            if (action.movable !== undefined) {
                if (action.movable) {
                    console.log("object ", obj.uid, " is movable");
                    obj.lockMovementX = false;
                    obj.lockMovementY = false;
                    obj.lockRotation = false;
                    hasControl = true;
                } else {
                    console.log("object ", obj.uid, " is not movable :", action.movable);
                    obj.lockMovementX = true;
                    obj.lockMovementY = true;
                    obj.lockRotation = true;
                    hasControl = false;
                }
            }
            if (action.resizeable !== undefined) {
                if (action.resizeable) {
                    console.log("object ", obj.uid, " is resizeable");
                    obj.lockScalingX = false;
                    obj.lockScalingY = false;
                    hasControl = true;
                } else {
                    console.log("object ", obj.uid, " is not resizeable :", action.resizeable);
                    obj.lockScalingX = true;
                    obj.lockScalingY = true;
                    if (hasControl === undefined) {
                        hasControl = false;
                    }
                }
            }
            obj.hasBorders = hasControl;
            obj.hasControls = hasControl;
            selectable = hasControl;
            const _this = this;

            if (hasControl !== undefined) {
                if (hasControl) {
                    console.log("object ", obj.uid, " has control");
                    var handler = function (e) {
                        _this._modifiedEvent(obj, e);
                    };
                    obj.modifiedHandler = handler;
                    obj.on("modified", handler);

                } else {
                    console.log("object ", obj.uid, " hasn't control : ", hasControl);
                    if (obj.modifiedHandler) {
                        obj.off("modified", obj.modifiedHandler);
                        obj.modifiedHandler = undefined;
                    }

                }
            }
            if (action.clickable !== undefined) {
                if (action.clickable) {
                    console.log("object ", obj.uid, " is clickable");
                    var handler = function (e) {
                        _this._clicEvent(obj, e);
                    };
                    obj.clickHandler = handler;
                    obj.on("mouseup", handler);
                    selectable = true;
                } else {
                    console.log("object ", obj.uid, " is not clickable ", action.clickable);
                    if (obj.clickHandler) {
                        obj.off("mouseup", obj.clickHandler);
                        obj.clickHandler = undefined;
                    }
                    if (selectable === undefined) {
                        selectable = false;
                    }
                }
            }
            if (selectable !== undefined) {
                console.log("object ", obj.uid, " is ", (selectable ? "" : "not"), " selectable");
                obj.set('selectable', selectable);
            }
            if (obj.isText) {
                if (action.text) {
                    obj.set('text', text);
                }
                //TODO change content, style
            }
        }
    },
    renderDisplay: function () {
        console.log("render display action", this.component.get("action"));
        const _this = this;
        if (!this._fabric) {
            console.log("Synoptique renderDisplay create this", this);
            this._canvas.width = this._div.offsetWidth;
            this._canvas.height = this._div.offsetHeight;
            this._fabric = new fabric.Canvas(this._canvas);

            try {
                this._fabric.on('mouse:wheel', function (opt) {
                    var delta = opt.e.deltaY;
                    var zoom = canvas.getZoom();
                    zoom *= 0.999 ** delta;
                    if (zoom > 20)
                        zoom = 20;
                    if (zoom < 0.01)
                        zoom = 0.01;
                    canvas.setZoom(zoom);
                    opt.e.preventDefault();
                    opt.e.stopPropagation();
                });
            } catch (e) {
                console.log(e);
            }
            var rect = new fabric.Rect({
                left: 100,
                top: 100,
                fill: '#FFBB22',
                width: 20,
                height: 20
            });
            rect.uid = "S99";
            this._fabric.add(rect);

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
                    case "synViewText":
                    case "synViewBasic":
                        switch (action.view.subType) {
                            case "CIRCLE":
                                obj = new fabric.Circle();
                                obj.hasRadius = true;
                                break;
                            case "ELLIPSE":
                                obj = new fabric.Ellipse();
                                break;
                            case "POLYGON":
                                obj = new fabric.Polygon();
                                break;
                            case "POLYLINE":
                                obj = new fabric.Polyline();
                                break;
                            case "RECT":
                                obj = new fabric.Rect();
                                break;
                            case "TEXT":
                                obj = new fabric.Text("");
                                obj.isText = true;
                                break;
                            default:
                                console.log("unsupported view synViewBasic subtype ", action.view.type);
                        }
                        if (obj !== undefined) {
                            console.log("object ", action.uid, " is ", action.view.subType, " = ", obj);
                            this._objectPostCreate(action, obj);
                        }
                        break;
                    case "synViewSvg":
                        var url = "synView/" + this.component.renderId + "/" + action.view.uid + "/view.svg";
                        console.log("create image view with url ", action.view.type);
                        fabric.Image.fromURL(url, {"left": 0, "top": 0, "scaleX": 1, "scaleY": 1})
                                .then(function (nobj) {
                            _this._objectPostCreate(action, nobj);
                            _this._fabric.renderAll();
                        });
                        break;
                    case "synViewJpg":
                        var url = "synView/" + this.component.renderId + "/" + action.view.uid + "/view.jpg";
                        console.log("create image view with url ", action.view.type, " action:", action, " fabric:", _this._fabric);

                        var URL = "https://www.tutorialspoint.com/images/trending_categories.png?v2";

                        fabric.Image.fromURL(URL, function (oImg) {
                            _this._fabric.add(oImg);
                            _this._fabric.renderAll();
                        }, {"left": 120, "top": 120, "scaleX": 0.25, "scaleY": 0.25});


                        fabric.Image.fromURL(url, function (nobj) {
                            console.log("synViewJpg loaded, postcreating ", url);
                            _this._objectPostCreate(action, nobj);
                            _this._fabric.renderAll();
                        }, {"left": 0, "top": 0, "scaleX": 1, "scaleY": 1});
                        break;
                    case "synViewPng":
                        var url = "synView/" + this.component.renderId + "/" + action.view.uid + "/view.png";
                        console.log("create image view with url ", action.view.type);
                        fabric.Image.fromURL(url, function (nobj) {
                            _this._objectPostCreate(action, nobj);
                            _this._fabric.renderAll();
                        }, {"left": 0, "top": 0, "scaleX": 1, "scaleY": 1});
                        break;
                    default:
                        console.log("unsupported view type ", action.view.type);
                }
            }
        }
        for (const action of actions.update) {
            var obj = this._content[action.uid];
            if (obj !== undefined) {
                this._updateObj(action, obj);
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
    _objectPostCreate(action, obj) {
        console.log("postcreating uid:", action.uid, " action:", action, " obj:", obj, " fabric:", this._fabric);
        obj.uid = action.uid;
        this._fabric.add(obj);
        this._content[obj.uid] = obj;
        this._updateObj(action, obj);
    },
    _modifiedEvent(source, event) {
        var mEvent = {
            className: "SynModifiedEvent",
            left: source.left,
            top: source.top,
            width: source.width,
            height: source.height,
            angle: source.angle,
            uid: source.uid
        };
        console.log("modified source", source, " mEvent", mEvent, " event ", event);
        this.component.fireEvent({
            type: 'objectEdit',
            source: this.component,
            data: mEvent
        });
    },
    _clicEvent(source, event) {
        var cEvent = {
            className: "SynModifiedEvent",
            left: source.left,
            top: source.top,
            width: source.width,
            height: source.height,
            angle: source.angle,
            uid: source.uid
        };
        console.log("click source", source, " mEvent", cEvent, " event ", event);
        this.component.fireEvent({
            type: 'objectClic',
            source: this.component,
            data: cEvent
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
