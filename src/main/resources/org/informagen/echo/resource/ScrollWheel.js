// Informagen =================================================================================
// Ensure that the 'Informagen' and 'Informagen.Sync' namespaces exists

if (!Core.get(window, ["Informagen", "Sync"])) {
    Core.set(window, ["Informagen", "Sync"], {});
}


// Informagen.ScrollWheel =====================================================================

//Informagen.ScrollWheel = Core.extend(Echo.Component, {

Informagen.ScrollWheel = Core.extend(Informagen.ButtonWheel, {

    $load: function() {
        Echo.ComponentFactory.registerType("Informagen.ScrollWheel", this);
    },

    componentType: "Informagen.ScrollWheel",

    focusable : false,
    
});

// Informagen.Sync.ScrollWheel ================================================================

//Informagen.Sync.ScrollWheel = Core.extend(Echo.Render.ComponentSync, {

Informagen.Sync.ScrollWheel = Core.extend(Informagen.Sync.ButtonWheel, {

    $load: function() {
        Echo.Render.registerPeer("Informagen.ScrollWheel", this);
    },


    // Class variables ----------------------------------------------------------
    
    _lastAng: null,
    _ignoreLastAngle: null,
    _defaultImageRef: "image/scrollWheel.gif",


    _wireComponent: function() {

        Informagen.Sync.ButtonWheel.prototype._wireComponent.call(this);      

        Core.Web.Event.add(this._divElement, "mousemove",  Core.method(this, this._onMouseMove), false);
        Core.Web.Event.add(this._divElement, "mouseout",   Core.method(this, this._onMouseOut), false);
    },


 
    _buildComponent: function() {

        Informagen.Sync.ButtonWheel.prototype._buildComponent.call(this);      

        // Style the feedback label so that it is over the 'Up' button
        this._feedbackDivElement.style.width = this._diameter + "px";
        this._feedbackDivElement.style.top = (this._diameter*.05) + "px";
        this._feedbackDivElement.style.fontSize = Math.floor(this._diameter*.15) + "px";

        // Scroll wheel has a non-zero inner radius
        this._innerRadius = this._diameter * 0.28;           
        
        // Accumulators for angle changes
        this._lastAng = 0;
        this._ignoreLastAngle = true;
        this._mouseWheelIn = false;
    },
    


    // Component (i.e. indirect) 'mouse' action handlers -------------------------------------------

	_onMouseOut: function (mouseEvent) {
        this._mouseWheelIn = false;
		this._ignoreLastAngle = true;
        this._feedbackDivElement.style.display="none";
	},


    // Private '_onmousemove' event handler which tracks the mouse movement in the inner wheel
    
	_onMouseMove: function(mouseEvent) {
	
        var location = Core.Web.DOM.getEventOffset(mouseEvent);
        var x = location.x;
        var y = location.y;
	
	    var angdiff = 0;
	    var ang = 0;
	    
		// Center the coordinates inside the component using cartesian coodinates, 
		//   ie positive y is up
		x = x - this._outerRadius;
		y = -(y - this._outerRadius);
		var r = Math.sqrt(x*x + y*y);

        // Only adjust mouse motion in the inner scrollwheel; If we are leaving the
        //  mouse wheel, notify via an action event so that the server can read the
        //  ScrollerModel is need be.
        
		if(r > this._innerRadius) {
		    if( this._mouseWheelIn)
                this.component.doAction("scrollExit", this._scrollerModel._value);
		    this._mouseWheelIn = false;
		    this._feedbackDivElement.style.display="none";
		    return;
        }
        
        this._mouseWheelIn = true;
        this._feedbackDivElement.style.display="";

		// Determine the angle (Top is 0 radians and clockwise is positive)
		
		// Horizontal line, 90 or 270; for others apply ATAN function
		if (y == 0) 
			ang = (x <= 0) ? Math.PI / -2 : Math.PI / 2;
		else 
			ang = Math.atan(x / y);
		
		// Angle from 90 to 180
		if (x >= 0 && y < 0) 
		    ang = ang + Math.PI;
	
	    // Angle from 180 to 270
		if (x < 0 && y < 0) 
		    ang = ang - Math.PI;

        // Actively scrolling, remember the last angle calculated
		if (this._ignoreLastAngle) {
			angdiff = 0;
			this._ignoreLastAngle = false;
		} else {
			angdiff = ang - this._lastAng;
			if (Math.abs(angdiff) > Math.PI) 
			    angdiff = 0;
		}
		
		this._lastAng = ang;

		// Magnify the radian change by 60 to yeild an integer
		var increment = angdiff * 60;
		
		if (angdiff > 0 && angdiff < 1) 
		    angdiff = 1;

        if(this._scrollerModel)
            this._scrollerModel.scrollWheel(increment);
	}


});

