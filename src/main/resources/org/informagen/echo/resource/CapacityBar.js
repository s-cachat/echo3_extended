// Informagen =================================================================================
// Insure that the 'Informagen' namespace exists

if (!Core.get(window, ["Informagen"])) {
    Core.set(window, ["Informagen"], {});
}


// Informagen.CapacityBar =======================================================================

Informagen.CapacityBar = Core.extend(Echo.Component, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.CapacityBar", this);
    },

    componentType :"Informagen.CapacityBar",
    
    focusable : false

});


// Informagen.CapacityBar.Peer ==================================================================

Informagen.CapacityBar.Peer = Core.extend(Echo.Render.ComponentSync, {

    $load : function() {
        Echo.Render.registerPeer("Informagen.CapacityBar", this);
    },


    renderAdd : function(update, parentElement) {
        this.divElement = document.createElement("div");
        this.divElement.id = this.component.renderId;
        parentElement.appendChild(this.divElement);
        this.renderUpdate(update);
    },

    renderUpdate : function(update) {
        var div = this.divElement;
        

        while (div.hasChildNodes()) {
            div.removeChild(div.firstChild);
        }
    
        var parameters = [];
        var string;

        // Obtain width, enforce mimimum and maximum
        parameters.width = Echo.Sync.Extent.toPixels(this.component.render("width", 460), true);
        parameters.width =  Math.max(parameters.width, 48); 

        // Obtain bar height; scale by 3/2 for reflection; enforce mimimum and maximum
        parameters.height = Echo.Sync.Extent.toPixels(this.component.render("height", 18), false);
        parameters.height *= 3/2;
		parameters.height = Math.max(parameters.height, 12);

        // Reflection reflectivity range:  0.001,solid to 1.0,transparent
        parameters.reflectivity = this.component.render("reflectivity", "0.75");
        parameters.reflectivity = Math.min(Math.max(parameters['reflectivity'], 0.001), 1.0); 

        parameters.showTicks = this.component.render("showTicks", true);

        string = this.component.render("tickSpacing", null);
        parameters.tickSpacing = eval(string);

        string = this.component.render("cornerRadius", "1.0");
        parameters.cornerRadius = eval(string);
        parameters.cornerRadius = Math.min(Math.max(parameters['cornerRadius'], 0.001), 1.0); 

        string = this.component.render("colors", "['#3765d9','#9ede7c','#9e42ee','#ec7612','#00aaaa','#cc0000','#aaaa00','#008000']");
        parameters.colors = eval(string);

        string = this.component.render("values", null);
        parameters.values = eval(string);


        // Create a canvas element, if the browser doesn't support the element expect a
        //   null value, however on MS IE you will get an element but it will not have
        //   the 'getContext' function.  Thanks, MS.  NOT!

        var canvas = document.createElement("canvas");
        
        if (canvas && (typeof(canvas.getContext) !== 'undefined')) {
        
            canvas.getContext("2d")
            canvas.height = parameters.height; 
            canvas.width = parameters.width;
            div.appendChild(canvas);
            
            this._createCapacityBar(canvas, parameters);
            
        } else {
            this._createStyledCapacityBar(parameters);
        }

        return false;
    },

    renderDispose : function(update) {
        delete this.divElement;
    },

    
	_createCapacityBar : function(canvas, parameters) {

        var radius; 
        var gradient;
        var color;
        
        // Values
        var total = 0;
        var valuesCount = 0;
        
        // Reflection vertical dimension
        var reflectionHeight = parseInt((canvas.height)/3);
            
        // Bar dimensions
        var barHeight = canvas.height - reflectionHeight;
        var barWidth = canvas.width;
        
        // Tick spacing in px, count
        var sectionWidth;
        var sectionCount;

    
        if(parameters.values) {
        
            valuesCount = parameters.values.length;
            
            // Generate a total of all values, only positive values allowed
            for(var i=0; i<valuesCount; i++) {
                total += Math.abs(parameters.values[i]);
            } 
        }
    
                    
        if(parameters.tickSpacing!=null && parameters.values != null && total > 0) {
            sectionWidth = barWidth * (parameters.tickSpacing/total);
        } else {
            sectionWidth = parameters.width/20; 
        }            
        
        sectionCount = Math.round((barWidth)/sectionWidth)-1;
        radius = parseInt(reflectionHeight * parameters.cornerRadius);

        try {
            
            var ctx = canvas.getContext("2d");
                        
            // Clear entire component, bar and reflection
            ctx.clearRect(0, 0, canvas.width, canvas.height); 
                        
            // Create a clipping path to round off the square corners
            ctx.beginPath(); 
            
            // Lower left bar, Upper left reflection
            ctx.moveTo(0, barHeight-radius); 
            ctx.quadraticCurveTo(0, barHeight, radius, barHeight); 
            ctx.quadraticCurveTo(0, barHeight, 0, barHeight+radius); 

            // Move to the right side of the bar & reflection
            ctx.lineTo(0, barHeight+reflectionHeight); 
            ctx.lineTo(barWidth, barHeight+reflectionHeight); 
            ctx.lineTo(barWidth, barHeight+radius);
            
            // Lower right bar, upper right reflection
            ctx.quadraticCurveTo(barWidth, barHeight, barWidth-radius, barHeight); 
            ctx.quadraticCurveTo(barWidth, barHeight, barWidth, barHeight-radius);
            
            // Move to upper right of bar, draw curve
            ctx.lineTo(barWidth, radius); 
            ctx.quadraticCurveTo(barWidth, 0, barWidth-radius, 0);
            
            // move to upper left of bar, draw curve and close path
            ctx.lineTo(radius, 0); 
            ctx.quadraticCurveTo(0, 0, 0, radius); 
            ctx.closePath();
            
            // Clip the bar and reflection to create the rounded ends
            ctx.clip();

            // Fill the bar with a light gray in case we have no data values
            //  or something happens
            
            ctx.fillStyle="rgba(204, 204, 204, 1.0)"; 
            ctx.fillRect(0, 0, canvas.width, canvas.height);
                            
            // If we have data values, draw in their colors, otherwise leave
            //   the bar with a light gray color
            
            if(parameters.values && total > 0) {
            
                var position = 0;
                
                // Draw the values in the capacity bar
                for(var i=0; i<valuesCount; i++) {
                
                    // Pick next available color
                    color = parameters.colors[i%(parameters.colors.length)];
                    
                    // Determine the segment width, no negative values
                    var width = barWidth*(Math.abs(parameters.values[i])/total); 
                    
                    // Color the segment
                    ctx.fillStyle = color; 
                    ctx.fillRect(position, 0, width, canvas.height);
                    position += width; 
                }
            }
            
            // Create the bar with a shadowed look
            //   Darker at the bottom, lighter at the top
            gradient = ctx.createLinearGradient(0, 0, 0, barHeight); 
            gradient.addColorStop(0.00,'rgba(255,255,255, 0.55)'); 
            gradient.addColorStop(0.05,'rgba(255,255,255, 0.50)'); 
            gradient.addColorStop(0.50,'rgba(128,128,128, 0.20)'); 
            gradient.addColorStop(0.95,'rgba(  0,  0,  0, 0.35)'); 
            gradient.addColorStop(1.00,'rgba(  0,  0,  0, 0.60)');
            ctx.fillStyle = gradient; 
            ctx.fillRect(0, 0, barWidth, barHeight); 
            
            // Draw tick marks
            if(parameters.showTicks) {

                ctx.lineWidth = 0.65; 
            
                gradient = ctx.createLinearGradient(0, 0.5, 0, canvas.height); 
                gradient.addColorStop(0.00, "rgba(255,255,255,1.0)"); 
                gradient.addColorStop(0.66, "rgba(255,255,255,0.7)"); 
                gradient.addColorStop(1.00, "rgba(255,255,255,0.2)");
                                    

                for(var i=1; i<=sectionCount; i++) {
                
                    ctx.beginPath(); 
                    ctx.moveTo(i*sectionWidth, 0.5); 
                    ctx.lineTo(i*sectionWidth, canvas.height); 
                    ctx.strokeStyle='rgba(0,0,0,0.75)'; 
                    ctx.stroke(); 
                    ctx.beginPath(); 
                    ctx.moveTo(i*sectionWidth + .5, 0.5); 
                    ctx.lineTo(i*sectionWidth + .5, canvas.height); 
                    ctx.strokeStyle=gradient; ctx.stroke();
                }
            }
            
            // Draw reflection, below the Capacity Bar
            gradient=ctx.createLinearGradient(0, barHeight, 0, canvas.height);
            gradient.addColorStop(0,  "rgba(0,0,0,1.0)"); 
            gradient.addColorStop(0.1,"rgba(0,0,0,0.5)"); 
            gradient.addColorStop(0.5,"rgba(0,0,0,0.0)"); 
            gradient.addColorStop(1,  "rgba(0,0,0,0.0)");
        
            ctx.fillStyle=gradient; 
            ctx.fillRect(0, barHeight, barWidth, canvas.height); 
            ctx.globalCompositeOperation="xor"; 
        
            // Set the refection reflectivity
            gradient=ctx.createLinearGradient(0, barHeight, 0, canvas.height); 
            gradient.addColorStop(1,"rgba(0, 0, 0, 1.0)"); 
            gradient.addColorStop(0,"rgba(0, 0, 0," + (1-parameters.reflectivity) + ")");
        
            ctx.fillStyle = gradient;
        
            ctx.fillRect(0, barHeight, barWidth, canvas.height); 
            ctx.restore();

        } catch(exception) {
            this.divElement.innerHTML = "CapacityBar - exception"; 
        }
        		
		return false;
	}, 
    
    
	_createStyledCapacityBar : function(parameters) {

        var color;
        
        // Values
        var total = 0;
        var valuesCount = 0;
        
        // Bar dimensions
        var barHeight = parameters.height;
        var barWidth = parameters.width;
        
    
        if(parameters.values) {
        
            valuesCount = parameters.values.length;
            
            // Generate a total of all values, only positive values allowed
            for(var i=0; i<valuesCount; i++) {
                total += Math.abs(parameters.values[i]);
            } 
        }
    
        var div = document.createElement("div");
        
        div.style.margin  = '0px'; 

        div.style.position = "relative";
        div.style.borderTop  = '3px solid #cccccc'; 
        div.style.borderRight  = '3px solid #888888'; 
        div.style.borderLeft  = '3px solid #cccccc'; 
        div.style.borderBottom  = '3px solid #888888'; 
        div.style.background  = '#cccccc'; 

        div.style.padding = '0px'; 
        div.style.height  = barHeight + 'px'; 
        div.style.width   = barWidth  + 'px'; 
                    
        if(parameters.values && total > 0) {
        
            var position = 0;
            
            // Draw the values in the capacity bar
            for(var i=0; i<valuesCount; i++) {
            
                var valueDiv = document.createElement("div");
                valueDiv.style.position = "absolute";
            
                // Pick next available color
                color = parameters.colors[i%(parameters.colors.length)];
                valueDiv.style.background = color;
                
                // Determine the segment width, no negative values
                var width = barWidth*(Math.abs(parameters.values[i])/total); 

                valueDiv.style.top = "0px";
                valueDiv.style.width =  (barWidth - position) +"px";
                valueDiv.style.height = barHeight +"px";
                valueDiv.style.left = position +"px";
                div.appendChild(valueDiv);
                
                position += width; 
            }
        }
        
        this.divElement.appendChild(div);

        		
		return false;
	}
    
    
    
    
});