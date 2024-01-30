function mydump(arr, level) {
    var dumped_text = "";
    if (!level)
        level = 0;

    var level_padding = "";
    for (var j = 0; j < level + 1; j++)
        level_padding += "    ";

    if (typeof (arr) == 'object') {
        for (var item in arr) {
            var value = arr[item];

            if (typeof (value) == 'object') {
                dumped_text += level_padding + "'" + item + "' ...\n";
                dumped_text += mydump(value, level + 1);
            } else {
                dumped_text += level_padding + "'" + item + "' => \"" + value + "\"\n";
            }
        }
    } else {
        dumped_text = "===>" + arr + "<===(" + typeof (arr) + ")";
    }
    return dumped_text;
}
;


String.prototype.metric = function(pFontSize) {
    var lDiv = document.createElement('lDiv');

    document.body.appendChild(lDiv);
    lDiv.style.fontSize = pFontSize;
    lDiv.style.position = "absolute";
    lDiv.style.left = "-1000px";
    lDiv.style.top = "-1000px";

    lDiv.innerHTML = this;

    var lResult = {
        width: lDiv.clientWidth,
        height: lDiv.clientHeight
    };

    document.body.removeChild(lDiv);
    lDiv = null;
    if (console) console.log("fs=" + pFontSize + " txt=" + this + " w=" + lResult.width + " h=" + lResult.height);
    return lResult;
};

/**
 * MenuBar component: a menu bar containing "pull down" menus.
 */
Extended.MenuBar = Core.extend(Extras.MenuComponent, {
    $load: function() {
        Echo.ComponentFactory.registerType("Extended.MenuBar", this);
    },
    /** @see Echo.Component#componentType */
    componentType: "Extended.MenuBar"
});
Extended.Sync = new Object();
/**
 * Component rendering peer: MenuBarPane
 */
Extended.Sync.MenuBar = Core.extend(Extras.Sync.Menu, {
    $static: {
        /**
         * Default rendering values used when component does not specify a property value.
         */
        DEFAULTS: {
            itemInsets: "0px 12px",
            insets: "3px 0px"
        }
    },
    $load: function() {
        if (window.console)
            console.log("menu register");
        Echo.Render.registerPeer("Extended.MenuBar", this);
    },
    /**
     * The currently active menu item.
     * @type Extras.ItemModel
     */
    _activeItem: null,
    /**
     * The menu bar's main DIV element.
     * @type Element
     */
    _menuBarDiv: null,
    /**
     * The total height contribution of the menu bar's border, in pixels.
     * @type Number
     */
    _menuBarBorderHeight: null,
    /**
     * Mapping between model ids and menu item TD elements.
     * @type Object
     */
    itemElements: null,
    /**
     * Constructor.
     */
    $construct: function() {
        Extras.Sync.Menu.call(this);
        this.itemElements = {};
        if (window.console)
            console.log("menu construct, app=" + this.application);
    },
    /** @see Extras.Sync.Menu#activate */
    activate: function() {
        if (Extras.Sync.Menu.prototype.activate.call(this)) {
            this.addMenu(this);
        }
    },
    /**
     * Closes the menu.
     */
    close: function() {
        if (this._activeItem) {
            this._setItemHighlight(this._activeItem, false);
            this._activeItem = null;
        }
    },
    /**
     * Returns the menu item TD element which is a parent of the specified element.
     *
     * @param element an element which is a descendant of a TD element representing a menu item
     * @return the TD element
     * @type Element
     */
    _getItemElement: function(element) {
        if (element == null) {
            return null;
        }
        // Find TD element.
        while (!element.isMenuItem) {
            if (element == this.element) {
                return null;
            }
            element = element.parentNode;
        }
        return element;
    },
    /**
     * Determines a ItemModel id based on a menu item DOM element.
     *
     * @param element the DOM element
     * @return the ItemModel id
     * @type String
     */
    _getItemModel: function(element) {
        var itemModelId = null;
        element = this._getItemElement(element);
        if (element == null) {
            return null;
        }

        // Find item model id of clicked element.
        for (var x in this.itemElements) {
            if (this.itemElements[x] == element) {
                itemModelId = x;
                break;
            }
        }

        if (itemModelId == null) {
            return null;
        } else {
            return this.menuModel.findItem(itemModelId);
        }
    },
    /** @see Echo.Render.ComponnetSync#getPreferredSize */
    getPreferredSize: function() {
        if (window.console)
            console.log("menu getPreferredSize");
        this._menuBarDiv.style.height = "";
        var insets = Echo.Sync.Insets.toPixels(this.component.render("insets", Extras.Sync.MenuBarPane.DEFAULTS.insets));
        return {height: new Core.Web.Measure.Bounds(this.element).height + insets.top + insets.bottom};
    },
    /** @see Extras.Sync.Menu#getSubMenuPosition */
    getSubMenuPosition: function(menuModel) {
        var itemElement = this.itemElements[menuModel.id];
        if (!itemElement) {
            throw new Error("Invalid menu: " + menuModel);
        }

        var containerBounds = new Core.Web.Measure.Bounds(this.element);
        var itemBounds = new Core.Web.Measure.Bounds(itemElement);

        return {x: itemBounds.left, y: containerBounds.top + containerBounds.height};
    },
    /**
     * Processes a mouse click event.
     *
     * @param e the event
     */
    _processClick: function(e) {
        if (!this.client || !this.client.verifyInput(this.component)) {
            return true;
        }

        Core.Web.DOM.preventEventDefault(e);

        var itemModel = this._getItemModel(Core.Web.DOM.getEventTarget(e));
        if (itemModel) {
            if (itemModel instanceof Extras.OptionModel) {
                this.deactivate();
                this.processAction(itemModel);
            } else {
                this.activate(true);
                this._setActiveItem(itemModel, true);
            }
        } else {
            this.deactivate();
        }
    },
    /**
     * Processes a mouse rollover enter event.
     *
     * @param e the event
     */
    _processItemEnter: function(e) {
        this._processRollover(e, true);
    },
    /**
     * Processes a mouse rollover exit event.
     *
     * @param e the event
     */
    _processItemExit: function(e) {
        this._processRollover(e, false);
    },
    /**
     * Processes mouse rollover events.
     *
     * @param e the event
     * @param {Boolean} state the rollover state, true indicating the mouse is currently rolled over an item
     */
    _processRollover: function(e, state) {
        if (!this.client || !this.client.verifyInput(this.component) || Core.Web.dragInProgress) {
            return true;
        }

        var element = this._getItemElement(Core.Web.DOM.getEventTarget(e));
        if (!element) {
            return;
        }
        var itemModel = this._getItemModel(element);

        if (this.stateModel && !this.stateModel.isEnabled(itemModel.modelId)) {
            return;
        }

        if (this.activated) {
            if (state) {
                this._setActiveItem(itemModel, itemModel instanceof Extras.MenuModel);
            }
        } else {
            this._setItemHighlight(itemModel, state);
        }
    },
    /** @see Echo.Render.ComponentSync#renderDisplay */
    renderDisplay: function() {
        if (window.console)
            console.log("menu renderDisplay");
        Extras.Sync.Menu.prototype.renderDisplay.call(this);
        Core.Web.VirtualPosition.redraw(this.element);
        var bounds = new Core.Web.Measure.Bounds(this.element.parentNode);
console.log(bounds);
        var height = bounds.height - this._menuBarBorderHeight;
console.log(height);
        this._menuBarDiv.style.height = height <= 0 ? "" : height + "px";
    },
    /** @see Echo.Render.ComponentSync#renderDispose */
    renderDispose: function(update) {
        if (window.console)
            console.log("menu renderDispose");
        this._menuBarDiv = null;
        Core.Web.Event.removeAll(this.element);
        Extras.Sync.Menu.prototype.renderDispose.call(this, update);
    },
    /** @see Extras.Sync.Menu#renderMain */
    renderMain: function(update) {
        if (window.console)
            console.log("menu renderMain");
        this._menuBarDiv = document.createElement("div");
        this._menuBarDiv.id = this.component.renderId;
        this._menuBarDiv.style.cssText = "overflow:hidden;";
        this._menuBarDiv.style.top = "0px";
        this._menuBarDiv.style.bottom = "0px";
        this._menuBarDiv.style.left = "0px";
        this._menuBarDiv.style.right = "0px";
        this._menuBarDiv.style.position = "absolute";

        Echo.Sync.renderComponentDefaults(this.component, this._menuBarDiv);
        var border = this.component.render("border", Extras.Sync.Menu.DEFAULTS.border);
        var multisided = Echo.Sync.Border.isMultisided(border);
        this._menuBarBorderHeight = Echo.Sync.Border.getPixelSize(border, "top") + Echo.Sync.Border.getPixelSize(border, "bottom");
        Echo.Sync.Border.render(multisided ? border.top : border, this._menuBarDiv, "borderTop");
        Echo.Sync.Border.render(multisided ? border.bottom : border, this._menuBarDiv, "borderBottom");
        Echo.Sync.FillImage.render(this.component.render("backgroundImage"), this._menuBarDiv);


        if (this.menuModel == null || this.menuModel.items.length === 0) {
            this._menuBarDiv.innerHtml = "\u00a0";
        } else {
            var items = this.menuModel.items;
            var offset = 0;
            for (var i = 0; i < items.length; ++i) {
                var item = items[i];
                if (item instanceof Extras.OptionModel || item instanceof Extras.MenuModel) {
                    var menuBarItemDiv = this._createMenuBarItem(item.text, item.icon);
                    menuBarItemDiv.style.top = "0px";
                    menuBarItemDiv.style.bottom = "0px";
                    menuBarItemDiv.style.left = offset + "px";
                    offset += parseInt(menuBarItemDiv.style.width);
                    menuBarItemDiv.style.position = "absolute";
                    this._menuBarDiv.appendChild(menuBarItemDiv);
                    this.itemElements[item.id] = menuBarItemDiv;
                }
            }
            this._menuBarDiv.style.width = offset + "px";
            Core.Web.Event.add(this._menuBarDiv, "click", Core.method(this, this._processClick), false);
            Core.Web.Event.add(this._menuBarDiv, "mouseover", Core.method(this, this._processItemEnter), false);
            Core.Web.Event.add(this._menuBarDiv, "mouseout", Core.method(this, this._processItemExit), false);
        }

        Core.Web.Event.Selection.disable(this._menuBarDiv);

        return this._menuBarDiv;
    },
    _createMenuBarItem: function(text, icon) {
        var innerDiv;
        var outerDiv;
        var borderImage = this.component.render("borderImage");
        var idw = 0;
        var odw = 0;
        if (borderImage) {
            innerDiv = document.createElement("div");
            innerDiv.style.top = "0px";
            innerDiv.style.bottom = "0px";
            innerDiv.style.left = "0px";
            innerDiv.style.position = "absolute";
            outerDiv = Echo.Sync.FillImageBorder.renderContainer(borderImage, {absolute: true, child: innerDiv});
            outerDiv.style.height = "100%";
            var bi = Echo.Sync.Insets.toPixels(borderImage.contentInsets);
            odw += bi.left + bi.right;
        } else {
            innerDiv = document.createElement("div");
            outerDiv = innerDiv;
            Echo.Sync.Insets.render(Extras.Sync.MenuBarPane.DEFAULTS.itemInsets, innerDiv, "padding");
        }
        var backgroundImage = this.component.render("menuBarBackgroundImage");
        Echo.Sync.FillImage.render(backgroundImage, innerDiv);
        innerDiv.style.whiteSpace = "nowrap";
        outerDiv.style.cursor = "pointer";
        outerDiv.isMenuItem = true;
        if (icon) {

            var img = document.createElement("img");
            img.style.verticalAlign = "middle";
            img.src = icon;
            // FIXME no load listeners being set on images for auto-resizing yet.
            img.width = "20";
            img.style.bottom = "0px";
            img.style.left = "0px";
            img.style.position = "absolute";
            idw += parseInt(img.width);
            odw += parseInt(img.width);
            innerDiv.appendChild(img);
            if (text) {
                // FIXME Does not handle RTL.
                img.style.paddingRight = "1ex";
            }
            if (window.console)
                console.log("menu " + text + " img width=" + img.offsetWidth);
        }
        if (text) {
            var textSpan = document.createElement("span");
            textSpan.style.verticalAlign = "middle";
            textSpan.style.bottom = "0px";
            textSpan.style.left = idw + "px";
            textSpan.style.position = "absolute";
            textSpan.appendChild(document.createTextNode(text));
            innerDiv.appendChild(textSpan);
            var metric = text.metric(outerDiv.style.font.size ? outerDiv.style.font.size : "20px");
            idw += metric.width;
            odw += metric.width;
        }
        innerDiv.style.width = idw + "px";
        outerDiv.style.width = odw + "px";
        return outerDiv;
    },
    /**
     * Sets the active item.
     *
     * @param {Extras.ItemModel} itemModel the item
     * @param {Boolean} execute flag indicating whether the item should be executed
     */
    _setActiveItem: function(itemModel, execute) {
        if (this._activeItem == itemModel) {
            return;
        }

        if (this._activeItem) {
            this._setItemHighlight(this._activeItem, false);
            this._activeItem = null;
        }

        if (execute) {
            this.activateItem(itemModel);
        }

        if (itemModel) {
            this._activeItem = itemModel;
            this._setItemHighlight(this._activeItem, true);
        }
    },
    /**
     * Sets the highlight state of an item.
     *
     * @param {Extras.ItemModel} itemModel the item
     * @param {Boolean} state the highlight state
     */
    _setItemHighlight: function(itemModel, state) {
        var element = this.itemElements[itemModel.id];
//        if (state) {
//            Echo.Sync.FillImage.render(this.component.render("selectionBackgroundImage"), element);
//            Echo.Sync.Color.render(this.component.render("selectionBackground",
//                    Extras.Sync.Menu.DEFAULTS.selectionBackground), element, "backgroundColor");
//            Echo.Sync.Color.render(this.component.render("selectionForeground",
//                    Extras.Sync.Menu.DEFAULTS.selectionForeground), element, "color");
//        } else {
//            element.style.backgroundImage = "";
//            element.style.backgroundColor = "";
//            element.style.color = "";
//        }
    }
});
