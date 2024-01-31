// Informagen =================================================================================
// Ensure that the 'Informagen' and 'Informagen.Sync' namespaces exists

if (!Core.get(window, ["Informagen", "Sync"])) {
    Core.set(window, ["Informagen", "Sync"], {});
}


// Informagen.ButtonWheel =====================================================================

Informagen.ButtonWheel = Core.extend(Echo.Component, {

    $load: function() {
        Echo.ComponentFactory.registerType("Informagen.ButtonWheel", this);
    },

    componentType: "Informagen.ButtonWheel",

    focusable : false,

    
    // Override superclass Echo.Component 'doAction' in order to pass an actionCommand  
    //   based on which part of the scrollwheel was affected. Change 'actionCommand'
    //   then invoke superclass method
    
    doAction: function(actionCommand, value) {
        this.set("value", value);
        this.set("actionCommand", actionCommand);
        this.fireEvent({type: "action", source: this, actionCommand: this.get("actionCommand")});

        // Invoking the superclass method did not seem to work. 2-OCT-2009        
//         Echo.Component.prototype.doAction.call(this);      
    }
    
});

// Informagen.Sync.ButtonWheel ================================================================

Informagen.Sync.ButtonWheel = Core.extend(Echo.Render.ComponentSync, {

    $load: function() {
        Echo.Render.registerPeer("Informagen.ButtonWheel", this);
    },


    // Class variables ----------------------------------------------------------

     $virtual: {
    
        // HTML/DOM elements
        _divElement: null,            // Component container
        _imgElement: null,            // Background template
    
        // Component variables    
        _diameter: null,              // scrollwheel diameter
        _innerRadius: 0,              // radius of inner/outer wheel boundary
        _outerRadius: null,           // scrollwheel radius
        _mouseWheelIn: false,

        _defaultImageRef: "image/buttonWheel.gif",
        _scrollerModel: null,
        
        // Virtual Methods
        _wireComponent: null,
        _buildComponent: null,
        _onClick: null,
        _onMouseMove: null,
     },

    _feedbackDivElement: null,    // Feedback indicator



    renderAdd: function(update, parentElement ) {
                
        // Build component
        this._divElement = document.createElement("div");
        this._divElement.id = this.component.renderId;
        this._divElement.style.position = "relative";
       
        // Build the foundation to the component
        parentElement.appendChild(this._divElement);
        
        // Create a ScrollModel; would rather this be passed in from the
        //   the server but I don't know how, yet.
        this._scrollerModel = new Informagen.ScrollerModel();

        // ScrollWheel property: diameter
        this._diameter = this.component.render("diameter", 150);

        // ScrollModel properties:

        this._scrollerModel._minimum = this.component.render("minimum", 0);
        this._scrollerModel._maximum = this.component.render("maximum", 100);
        this._scrollerModel._sensitivity = this.component.render("sensitivity", 15);


        this._buildComponent();
        this._wireComponent();
    },
    
    renderUpdate: function(update) {

        var updateProperty = null;
        
        updateProperty = update.getUpdatedProperty("diameter");
        if (updateProperty) {
            this._diameter = updateProperty.newValue;
            // Only remove the 'img' child node; all others remain
            Core.Web.DOM.removeNode(this._imgElement);
            this._buildComponent();
        } 

        updateProperty = update.getUpdatedProperty("value");
        if (updateProperty) {
            this._scrollerModel.setValue(updateProperty.newValue);
        }


        // Update Scroller Model properties here
        this._scrollerModel._maximum = this.component.render("maximum", this._scrollerModel._maximum);
        this._scrollerModel._minimum = this.component.render("minimum", this._scrollerModel._minimum);
        this._scrollerModel._sensitivity = this.component.render("sensitivity", this._scrollerModel._sensitivity);

        return false;
    },    
 
     
    renderDispose: function(update) {
    
        // Remove all the event handlers on the scrollwheel image
        Core.Web.Event.removeAll(this._divElement);

        this._divElement = null;
        this._imgElement = null;
		this._feedbackDivElement = null;
        
		this._scrollerModel.dispose();
		this._scrollerModel = null;
    },

    _wireComponent: function() {
        
        // Register mouse event handlers for the ButtonWheel component
        Core.Web.Event.add(this._divElement, "click", Core.method(this, this._onClick), false);
        Core.Web.Event.add(this._divElement, "mousewheel", Core.method(this, this._onMouseWheel), false);
        Core.Web.Event.add(this._divElement, "mousemove", Core.method(this, this._onMouseMove), false);
    },


 
    _buildComponent: function() {

        // Set the size of the enclosing 'div'
        this._divElement.style.width = this._diameter + 'px';
        this._divElement.style.height = this._diameter + 'px';
        
        // Build the scroll wheel 'img' element
        this._imgElement = document.createElement("img");

        var scrollWheelImg = this.client.getResourceUrl("Informagen", this._defaultImageRef);
        Echo.Sync.ImageReference.renderImg(scrollWheelImg, this._imgElement);
        
        // Size the background image to fit the component DIV
        this._imgElement.style.width = this._diameter + 'px';
        this._imgElement.style.height = this._diameter + 'px';
        this._imgElement.style.cursor = "pointer";

        // Create the feedback element
        this._feedbackDivElement = document.createElement("div");
        this._divElement.appendChild(this._feedbackDivElement);

        // Feedback position and style; Center over the component
        this._feedbackDivElement.style.position = "absolute";
        this._feedbackDivElement.style.display = "none";
        this._feedbackDivElement.style.width = this._diameter + "px";
        this._feedbackDivElement.style.top = (this._diameter*.30) + "px";
        this._feedbackDivElement.style.left = "0px";
        this._feedbackDivElement.style.fontSize = Math.floor(this._diameter*.35) + "px";
        this._feedbackDivElement.style.textAlign = "center";
        this._feedbackDivElement.style.fontWeight = "bold";
        this._feedbackDivElement.style.color = "black";

        // Set opacity filter for IE, everybody else uses CSS
        if(Core.Web.Env.PROPRIETARY_IE_OPACITY_FILTER_REQUIRED)
            this._feedbackDivElement.style.filter = "alpha(opacity=30)";
        else
            this._feedbackDivElement.style.opacity = "0.3";

        // Build scroller feedback element contents
        var textNode = document.createTextNode("");
        this._feedbackDivElement.appendChild(textNode);

        // Assign feedback to scroller model
        this._scrollerModel._feedback = textNode;
        this._scrollerModel.setValue(this.component.render("value", 0));

        // Outer radius is half the diameter
        this._outerRadius = Math.floor(this._diameter/2);
        
        // No inner radius for the button wheel
        this._innerRadius = 0;           
        
        // Add the scroll wheel image to the component 'div' element
        this._divElement.appendChild(this._imgElement);

        return false;
    },
    


    // Private '_onmouseclick' event handler which will dispatch to diagonaly oriented quadrants
    
    _onClick: function(mouseEvent) {
        
        var location = Core.Web.DOM.getEventOffset(mouseEvent);
        var x = location.x;
        var y = location.y;
		    
	    var quadrant, r;
	    
		// Center the coordinates inside the component using cartesian coodinates, 
		//   ie positive y is up
		x = x - this._outerRadius;
		y = -(y - this._outerRadius);
		r = Math.sqrt(x*x + y*y);
				
		if(r < this._innerRadius)
		    return;
		
		// Determine the clicked quadrant, split on diagonals, not horizontal/vertical axes
		// 0 = not defined, 1 = top, 2 = right, 3 = bottom, 4 = left
		
		var quadrant = 0; 
		
		if (Math.abs(x) >= Math.abs(y)) {
			quadrant = (x >= 0) ? 2 : 4;   	// Right or Left
		} else {
			quadrant = (y >= 0) ? 1 : 3;    // Top or Bottom
		}
		
		// Dispatch to the actual 'MouseClick' action handler
		if (quadrant == 1) {
		    if(this._scrollerModel)
                this._scrollerModel.clickTop();
            this.component.doAction("clickTop", this._scrollerModel._value);
        }
		    
		if (quadrant == 2) {
		    if(this._scrollerModel)
                this._scrollerModel.clickRight();
            this.component.doAction("clickRight", this._scrollerModel._value);
        }
		    
		if (quadrant == 3) {
		    if(this._scrollerModel)
                this._scrollerModel.clickBottom();
            this.component.doAction("clickBottom", this._scrollerModel._value);
        }
		    
		if (quadrant == 4) {
		    if(this._scrollerModel)
                this._scrollerModel.clickLeft();
            this.component.doAction("clickLeft", this._scrollerModel._value);
        }
	},


    // Support for mouse wheels; only process magnitude change, not direction

	_onMouseWheel: function (mouseEvent) {
	
        this._mouseWheelIn = true;

		this._feedbackDivElement.style.display="";
	    
	    if(mouseEvent.wheelDelta) {
	        var increment = mouseEvent.wheelDelta;
            this._scrollerModel.scrollWheel(increment);
        }
        
        Core.Web.DOM.preventEventDefault(mouseEvent);
	},

	_onMouseMove: function(mouseEvent) {
        if( this._mouseWheelIn)
            this.component.doAction("scrollExit", this._scrollerModel._value);
        this._mouseWheelIn = false;
        
        this._feedbackDivElement.style.display="none";
	}

});
