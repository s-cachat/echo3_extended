SlickGrid = {};
SlickGrid = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("SlickGrid", this);
    },
    doAction: function (message) {
        message.className = "SlickGrid.CellEdited";
        this.fireEvent({
            type: "cellUpdate",
            source: this,
            data: message
        });
    },
    componentType: "SlickGrid"
});
SlickGrid.Sync = Core.extend(Echo.Render.ComponentSync, {
    $static: {

        /**
         * Serialization peer for <code>CellEdited</code> instances.
         */
        CellEditedSerialPeer: Core.extend(Echo.Serial.PropertyTranslator, {

            $static: {

                /** @see Echo.Serial.PropertyTranslator#toXml */
                toXml: function (client, pElement, value) {
                    pElement.setAttribute("row", value.row);
                    pElement.setAttribute("col", value.col);
                    pElement.setAttribute("field", value.field);
                    pElement.setAttribute("value", value.value);
                }
            },

            $load: function () {
                Echo.Serial.addPropertyTranslator("SlickGrid.CellEdited", this);
            }
        })
    },

    $load: function () {
        Echo.Render.registerPeer("SlickGrid", this);
        componentType : "SlickGrid";
        console.log("loading slickgrid css");
        document.head.innerHTML = document.head.innerHTML +'<link rel="stylesheet" type="text/css" href="slickgrid/jquery-ui-1.11.3.custom.css" />';
        document.head.innerHTML = document.head.innerHTML +'<link rel="stylesheet" type="text/css" href="slickgrid/slick.grid.css" />';
        document.head.innerHTML = document.head.innerHTML +'<link rel="stylesheet" type="text/css" href="slickgrid/slickgridEcho3.css" />';

    },
    _div: null,
    _slickgrid: null,
    _data: null,
    _columns: null,
    _click: null,
    renderAdd: function (update, parentElement) {
        console.log("renderAdd",update,parentElement);
        try {
            this._div = document.createElement("div");
            this._div.id = this.component.renderId;

            Echo.Sync.renderComponentDefaults(this.component, this._div);
            Extended.renderPositionnable(this.component, this._div);
            parentElement.appendChild(this._div);
        } catch (e) {
            if (console) {
                console.log("SlickGrid error in renderAdd : " + e);
            }
        }
    },
    renderDisplay: function () {
        console.log("renderDisplay");

        try {
            if (!this._slickgrid) {
                this._slickgrid = this._buildGrid();
            }
        } catch (e) {
            if (console) {
                console.log("SlickGrid error in renderDisplay : " , e);
            }
        }
    },
    _buildGrid: function () {
        try {
            var gridModel = JSON.parse(this.component.get("model")).slickgridModel;
            var d = [];
            gridModel.data.forEach((x, i) => {
                x.data["id"] = x.id;
                d.push(x.data);
            });
            gridModel.columns.forEach((x, i) => {
                if (x.editorName) {
                    x.editor = eval(x.editorName);
                }
                if (x.formatterName) {
                    x.formatter = eval(x.formatterName);
                }
            });
            this._columns = gridModel.columns;
            var style = "";
            if (gridModel.options.backgroundColor) {
                style += ".slick-cell{background-color:" + gridModel.options.backgroundColor + ";}";
            }
            if ( gridModel.options.headerBackgroundColor) {
                style += ".slick-header-column{background:" + gridModel.options.headerBackgroundColor + ";}";
            }
            if ( gridModel.options.evenBackgroundColor) {
                style += ".grid-canvas .even{background:" + gridModel.options.evenBackgroundColor + ";}";
                console.log("even rows : " + gridModel.options.evenBackgroundColor)
            }
            if ( gridModel.options.oddBackgroundColor) {
                style += ".grid-canvas .odd{background:" + gridModel.options.oddBackgroundColor + ";}";
                console.log("odd rows : " + gridModel.options.oddBackgroundColor)
            }
            if (style.length > 0) {
                $("#slickGridCustomCss").remove();
                $("<style type='text/css' rel='stylesheet' id='slickGridCustomCss'>" + style + "</style>").appendTo($("head"));
            }
            var myself = this;
            this._data = d;
            var $div;
            if (this._div instanceof jQuery) {
                $div = this._div;
            } else {
                $div = $(this._div);
            }
            $div.css("position", "absolute");
            $div.css("top", "0px");
            $div.css("left", "0px");
            $div.css("right", "0px");
            $div.css("bottom", "0px");
            $div.css("padding-bottom", "30px");
            $div.css("padding-right", "20px");
            console.log("data", this._data);
            console.log("gridModel", gridModel);
            console.log("component", myself.component);
            this._slickgrid = new Slick.Grid(this._div, this._data, gridModel.columns, gridModel.options);
            this._slickgrid.setSelectionModel(new Slick.CellSelectionModel());
            this._slickgrid.onCellChange.subscribe(function (event, data) {
                var col = gridModel.columns[data.cell];
                console.log("onCellChange " + data.row + "," + data.cell + ": " + col.field + "=" + data.item[col.field]);
                var msg = {
                    row: data.row,
                    col: data.cell,
                    field: col.field,
                    value: data.item[col.field]
                };
                myself.component.doAction(msg);
            });
              this._slickgrid.resizeCanvas();
            return this._slickgrid;
        } catch (e) {
            throw ("SlickGrid error in _buildGrid : " + e);
            return undefined;
        }
    },
    _featureEvent: function (e) {
        this.peer.component.doAction("feature");
    },
    renderDispose: function (update) {
        if (this._slickgrid) {
            this._slickgrid = null;
        }
        this._div = null;
    },
    renderUpdate: function (update) {
        try {
            if (update._propertyUpdates.cellChange) {
                console.log("cellChange");
                console.log(update);
                var dx = JSON.parse(update._propertyUpdates.cellChange.newValue);

                console.log(dx);
                if (this._slickgrid !== undefined) {
                    if (dx.changes) {
                        dx.changes.forEach((x) => {
                            this._data[x.row][x.field] = x.value;
                            this._slickgrid.updateCell(x.row, this._slickgrid.getColumnIndex(x.field));
                        });
                    } else {
                        this._data[dx.row][dx.field] = dx.value;
                        this._slickgrid.updateCell(dx.row, this._slickgrid.getColumnIndex(dx.field));
                    }
                }
            } else {
                console.log("Other update", update);
            }
        } catch (e) {
            if (console) {
                console.log("SlickGrid error in renderUpdate : ");
                console.log(e);
            }
        }
        return true;
    }
}
);
