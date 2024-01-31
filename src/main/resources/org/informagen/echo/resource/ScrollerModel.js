// Informagen =================================================================================
// Ensure that the 'Informagen' and 'Informagen.Sync' namespaces exists

if (!Core.get(window, ["Informagen"])) {
    Core.set(window, ["Informagen"], {});
}

// Informagen.ScrollerModel ===================================================================
//
// Acts as a model to the ScrollWheel controller.  I had intended for this to be a server-side
//   object which would have a client side peer so that the behavior of the various 'click'
//   fucntions could be subclassed and customized.
//
// Could not figure this out at this time without making it an Echo component, which did not
//  seem the correct way to implement this client side class.
//
// Implements a 'ScrollerModel' interface:
//       
//      clickTop()
//      clickBottom()
//      clickRight()
//      clickLeft()
//      scrollWheel(int increment)


Informagen.ScrollerModel = Core.extend({

    _minimum: 0,
    _maximum: 1,
    _value: 0,
    _sensitivity: 1,
    _feedback: null,

    $virtual: {

        clickTop: function() {
            this._value = this._minimum;
            this.updateValue();
        },
    
        clickBottom: function() {
            this._value = this._maximum;
            this.updateValue();
        },
    
        clickLeft: function() {
            this._value -= 1;
            this.updateValue();
        },
    
        clickRight: function() {
            this._value += 1;
            this.updateValue();
        },
    
        scrollWheel: function (increment) {
            if(this._sensitivity && this._sensitivity != 0)
                this._value += (increment/this._sensitivity) | 0;
            else
                this._value += increment;
                
            this.updateValue();
        },
    
        keepInRange: function() {
            this._value = Math.min(this._value, this._maximum);
            this._value = Math.max(this._value, this._minimum);
        },

        updateValue: function() {
            this.keepInRange();
            this.notifyListener();
        }

    },

    notifyListener: function() {
        if(this._feedback)
            this._feedback.nodeValue = this._value;
    },

    setValue: function(value) {
        this._value = value;
        this.updateValue();
    },
    
    getValue: function() {
        return this._value;
    },
    
    dispose: function() {
        this._minimum = null;
        this._maximum = null;
        this._value = null;
        this._sensitivity = null;
    }
    
});



