ContainerEx = {};
ContainerEx = Core.extend(Echo.Component, {
    $load: function () {
        Echo.ComponentFactory.registerType("ContainerEx", this);
    },
    componentType: "ContainerEx"
});

ContainerEx.Sync = Core.extend(Echo.Render.ComponentSync, {
    $load: function () {
        Echo.Render.registerPeer("ContainerEx", this);
        componentType : "ContainerEx";
    },
    _div: null,
    _outerdiv: null,
    renderAdd: function (update, parentElement) {
        if (this.component.renderId === parentElement.id) {
            var e = new Error('dummy');
            var stack = e.stack.replace(/^[^\(]+?[\n$]/gm, '')
                    .replace(/^\s+at\s+/gm, '')
                    .replace(/^Object.<anonymous>\s*\(/gm, '{anonymous}()@')
                    .split('\n');
            if (console)
                console.log(stack);
        }
//        if (console) console.log("CE " + this.component.renderId + " renderAdd(update=" + update + ", parent=" + parentElement.id + ")");
        var borderImage = this.component.render("borderImage");
        if (borderImage) {
            this._div = document.createElement("div");
            this._div.style.top = "0px";
            this._div.style.bottom = "0px";
            this._div.style.left = "0px";
            this._div.style.right = "0px";
            this._div.style.position = "absolute";
            this._div.style.verticalAlign = "middle";
            this._div.id = this.component.renderId + "_inner";
            this._outerdiv = Echo.Sync.FillImageBorder.renderContainer(borderImage, {absolute: true, child: this._div});
        } else {
            this._div = document.createElement("div");
            this._outerdiv = this._div;
        }
        this._outerdiv.id = this.component.renderId;

        Echo.Sync.renderComponentDefaults(this.component, this._div);
        Extended.renderPositionnable(this.component, this._outerdiv);
        var background = this.component.render("background");
        var backgroundImage = this.component.render("backgroundImage");
        Echo.Sync.Color.renderFB(this.component, this._div);
        Echo.Sync.Border.render(this.component.render("border"), this._div);
        Echo.Sync.Font.render(this.component.render("font"), this._div);
        Echo.Sync.FillImage.render(backgroundImage, this._div);
        Echo.Sync.Insets.render(this.component.render("insets"), this._div, "padding");
        Echo.Sync.RoundedCorner.render(this.component.render("radius"), this._div);
        Echo.Sync.BoxShadow.render(this.component.render("boxShadow"), this._div);
        var textAlignment = this.component.render("textAlignment");
        if (textAlignment) {
            var horizontal = Echo.Sync.Alignment.getRenderedHorizontal(textAlignment);
            console.log("ContainerEx align global=" + textAlignment + " horizontal " + horizontal);
            if (!horizontal) {
                this._div.style.textAlign = "left";
            } else {
                this._div.style.textAlign = horizontal;
            }
        }
        var scrollBarPolicy = this.component.render("scrollBarPolicy");
        switch (scrollBarPolicy) {
            case 0:
                this._div.style.overflow = null;
                break;
            case 1:
                this._div.style.overflow = "visible";
                break;
            case 2:
                this._div.style.overflow = "scroll";
                break;
            case 4:
                this._div.style.overflow = "auto";
                break;
            case 8:
                this._div.style.overflow = "hidden";
                break;
            case 16:
                this._div.style.overflowX = "hidden";
                this._div.style.overflowY = "scroll";
                break;
            case 32:
                this._div.style.overflowX = "scroll";
                this._div.style.overflowY = "hidden";
                break;
        }

        var flexBasis = this.component.render("flexBasis");
        if (flexBasis) {
            this._div.style.flexBasis = flexBasis;
            this._div.style.display = "flex";
        }
        var flexGrow = this.component.render("flexGrow");
        if (flexGrow) {
            this._div.style.flexGrow = flexGrow;
            this._div.style.display = "flex";
        }
        var flexShrink = this.component.render("flexShrink");
        if (flexShrink) {
            this._div.style.flexShrink = flexShrink;
            this._div.style.display = "flex";
        }
        var flexWrap = this.component.render("flexWrap");
        if (flexWrap) {
            this._div.style.flexWrap = flexWrap;
            this._div.style.display = "flex";
        }
        var flexDirection = this.component.render("flexDirection");
        if (flexDirection) {
            if (flexDirection === "responsive") {
                responsiveWidth = this.component.render("responsiveWidth");
                if (responsiveWidth === undefined) {
                    responsiveWidth = 800;
                }
                flexDirection = window.innerWidth < responsiveWidth ? "column" : "row";
                console.log("flex responsive  : ", responsiveWidth, " / ", window.innerWidth, " => ", flexDirection);

            }
            this._div.style.flexDirection = flexDirection;
            this._div.style.display = "flex";
        }
        var justifyContent = this.component.render("justifyContent");
        if (justifyContent) {
            this._div.style.justifyContent = justifyContent;
        }
        var shadow = this.component.render("shadow");
        if (shadow) {
            this._div.style.boxShadow = shadow;
        }

        if (!background && !backgroundImage) {
            Echo.Sync.FillImage.render(this.client.getResourceUrl("Echo", "resource/Transparent.gif"), this._outerdiv);
        }
        this._childIdToElementMap = {};

        var componentCount = this.component.getComponentCount();

        for (var i = 0; i < componentCount; i++) {
            var child = this.component.getComponent(i);
            this._renderAddChild(update, child);
        }
        parentElement.appendChild(this._outerdiv);
    },
    _renderAddChild: function (update, child) {
//        if (console) console.log("CE " + this.component.renderId + " renderAddChild(update=" + update + ", child=" + child + ")");
        Echo.Render.renderComponentAdd(update, child, this._div);
    },
    renderDispose: function (update) {
        this._childIdToElementMap = null;
        this._div = null;
        this._outerdiv = null;
    },
    _renderRemoveChild: function (update, child) {
        var childDiv = this._childIdToElementMap[child.renderId];
//        if (console) console.log("CE " + this.component.renderId + " renderRemoveChild(" + update + "," + child.renderId + ",div=" + childDiv);
        if (!childDiv) {
            // Child never rendered.
            return;
        }
        childDiv.parentNode.removeChild(childDiv);
        delete this._childIdToElementMap[child.renderId];
        Echo.Render.renderComponentDispose(update, child);
    },
    renderUpdate: function (update) {
        if (console)
            console.log("CE " + this.component.renderId + " renderUpdate : ");
        if (console)
            console.log(update);
        var element = this._div;
        var containerElement = element.parentNode;
        for (var i = 0; i < this.component.children.length; ++i) {
            Echo.Render.renderComponentDispose(update, this.component.children[i]);
        }
        containerElement.removeChild(element);
        this.renderAdd(update, containerElement);
        return true;
    }
});
