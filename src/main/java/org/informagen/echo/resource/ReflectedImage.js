// Informagen =================================================================================
// Insure that the 'Informagen' namespace exists

if (!Core.get(window, ["Informagen"])) {
    Core.set(window, ["Informagen"], {});
}


// Informagen.ReflectedImage =======================================================================

Informagen.ReflectedImage = Core.extend(Informagen.StaticImage, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.ReflectedImage", this);
    },

    componentType :"Informagen.ReflectedImage",
    
    focusable : false

});


// Informagen.ReflectedImage.Peer ==================================================================

Informagen.ReflectedImage.Peer = Core.extend(Informagen.StaticImage.Peer, {

    $load : function() {
        Echo.Render.registerPeer("Informagen.ReflectedImage", this);
    },

    _reflection: null,
    _gap: 2,
    _reflectivity: 0.5,
    _reflectedHeight: 0.5,

    renderAdd: function(update, parentElement ) {
                
        this._divElement = document.createElement("div");
        this._divElement.id = this.component.renderId;

        parentElement.appendChild(this._divElement);

        this._imageReference = this.component.render("imageReference", null);
        this._gap = this.component.render("gap", 2);
        this._reflectivity = this.component.render("reflectivity", 0.5);
        this._reflectedHeight = this.component.render("reflectedHeight", 0.5);
        
        this.renderUpdate(update);
    },



    
    renderUpdate: function(update) {

        Core.Web.DOM.removeAllChildren(this._divElement);
        
        var updateProperty = null;
        
        updateProperty = update.getUpdatedProperty("imageReference");
        if (updateProperty) {
           this._imageReference = updateProperty.newValue;
        }

        updateProperty = update.getUpdatedProperty("gap");
        if (updateProperty) {
           this._gap = updateProperty.newValue;
        }

        updateProperty = update.getUpdatedProperty("reflectivity");
        if (updateProperty) {
           this._reflectivity = updateProperty.newValue;
        }

        updateProperty = update.getUpdatedProperty("reflectedHeight");
        if (updateProperty) {
           this._reflectedHeight = updateProperty.newValue;
        }
        
        if(this._imageReference) {
        
            // Create the 'img' element, add to component 'div'; use Echo to
            //  associate image reference to the 'img' src attribute
            this._imgElement = document.createElement("img");
            this._divElement.appendChild(this._imgElement);


            Echo.Sync.ImageReference.renderImg(this._imageReference, this._imgElement);

            // Render the reflection only after the image is loaded
            // It may have been cached and won't be registered by Core.Web.Image.monitor.
            // In that case, invoke the function to draw the reflection now.

            if (!this._imgElement.complete && 
                (Core.Web.Env.QUIRK_UNLOADED_IMAGE_HAS_SIZE || 
                (!this._imgElement.height && !this._imgElement.style.height)) ) {

                // Wait for the image to load
                var self = this;
                Core.Web.Image.monitor(this._divElement, function() {self._createReflection()});
                
            } else
                this._createReflection();

        }

        return false;
    },
    

    _createReflection: function() {
    
//         if (!this._imgElement.complete && 
//                 (Core.Web.Env.QUIRK_UNLOADED_IMAGE_HAS_SIZE || 
//                 (!this._imgElement.height && !this._imgElement.style.height)) )
//             return;
    
        // Image height & width for easy access
        var height = this._imgElement.height;
        var width = this._imgElement.width;
        
        // Reflection width is same as image width; Height is 0.0 to 1.0 fraction
        var reflectionWidth = width;
        var reflectionHeight = Math.floor(height * this._reflectedHeight);
        var divHeight = Math.floor(height + reflectionHeight + this._gap);
    
        // Define the 'div' dimensions to hold the image and it's reflection
        this._divElement.style.width = reflectionWidth + 'px';
        this._divElement.style.height = divHeight + 'px';

        // Create a canvas element, if the browser doesn't support the element expect a
        //   null value, however on MS IE you will get an element but it will not have
        //   the 'getContext' function.

        var canvas = document.createElement("canvas");

        
        if (canvas && (typeof(canvas.getContext) !== 'undefined')) {

            this._imgElement.style.cssText = 'vertical-align:bottom;';
    
            // Define the 'canvas' dimensions
            canvas.style.height = (reflectionHeight + this._gap) + 'px';
            canvas.style.width = reflectionWidth + 'px';
            canvas.height = reflectionHeight + this._gap;
            canvas.width = reflectionWidth;
            
            var context = canvas.getContext("2d");
            
            // Save state prior to translating coordindate system
            context.save();
            
            // Create the reflection for the original image but mathematical scaling
            //   into the canvas dimensions; 'gap' is amount of separation between the
            //   image and it's reflection
            context.translate(0, height);
            context.scale(1, -1);

            // An excecption may be thrown here because the browser must load 
            //   the image before it can create it's reflection. Schedule this
            //   function to run again in 50 milliseconds, keep rescheduling
            //   for slow network connections or large images.
            
            try {
                context.drawImage(this._imgElement, 0, -this._gap, width, height);
            } catch(exception) {
                canvas = null;
                var self = this;
                Core.Web.Scheduler.run(function() {self._createReflection()}, 50);
                return;
            }
            
            
            // Restore the coordinate system to its orginal state
            context.restore();
            
            // Create the opacity using an gradient, but varying the alpha
            context.globalCompositeOperation = "destination-out";
            var gradient = context.createLinearGradient(0, this._gap, 0, (reflectionHeight + this._gap));
    
            // Gradient from fully opaque to inverse specified opacity
            gradient.addColorStop(1, "rgba(255, 255, 255, 1.0)");
            gradient.addColorStop(0, "rgba(255, 255, 255, " + (1-this._reflectivity) + ")");
    
            context.fillStyle = gradient;
            context.rect(0, 0, reflectionWidth, (reflectionHeight + this._gap));
            context.fill();

            this._reflection = canvas;
            
        } else {
        
            // MicroSoft Internet Explorer specific code here; no canvas support yet
        
            // Create another 'img' element for the reflection
            this._reflection = document.createElement("img");
        
            // Set it's attributes to the original 'img'
		    this._reflection.src = this._imgElement.src;
            this._reflection.style.width = this._imgElement.width + 'px';
            this._reflection.style.height = this._imgElement.height + "px";
            this._reflection.style.display = 'block';
            
            // Apply a MicroSoft filter in order to create the reflection
            this._reflection.style.marginBottom = "-" + (height-reflectionHeight) + 'px';
            this._reflection.style.filter = 'flipv progid:DXImageTransform.Microsoft.Alpha(opacity=' + (this._reflectivity*100) + ', style=1, finishOpacity=0, startx=0, starty=0, finishx=0, finishy='+(this._reflectedHeight*100)+')';
            
        }


        // Build the final Echo component; Add the image and it's reflection to the DIV
        this._divElement.appendChild(this._reflection);

    },
    
    renderDispose: function(update) {
        this._divElement = null;
        this._imgElement = null;
        this._reflection = null;
        this._imageReference = null;
        this._gap = null;
        this._reflectivity = null;
        this._reflectedHeight = null;
    }
});
