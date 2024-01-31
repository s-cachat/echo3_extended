
Extended = {
    renderPositionnable: function(component, element) {
        var v;
        v = component.render("top");
        if (v) {
            element.style.top = v;
        }
        v = component.render("left");
        if (v) {
            element.style.left = v;
        }
        v = component.render("right");
        if (v) {
            element.style.right = v;
        }
        v = component.render("bottom");
        if (v) {
            element.style.bottom = v;
        }
        v = component.render("height");
        if (v) {
            element.style.height = v;
        }
        v = component.render("width");
        if (v) {
            element.style.width = v;
        } else {
            element.style.width = "auto";
        }
        v = component.render("position");
        if (v) {
            switch (v) {
                case 1:
                    element.style.position = "static";
                    break;
                case 2:
                    element.style.position = "absolute";
                    break;
                case 3:
                    element.style.position = "relative";
                    break;
            }

        }
    }
};