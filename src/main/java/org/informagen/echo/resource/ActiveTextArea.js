// Informagen =================================================================================
// Ensure that the 'Informagen' and 'Informagen.Sync' namespaces exists

if (!Core.get(window, ["Informagen", "Sync"])) {
    Core.set(window, ["Informagen", "Sync"], {});
}


// Informagen.ActiveTextArea ==================================================================

Informagen.ActiveTextArea = Core.extend(Echo.TextComponent, {

    $load : function() {
        Echo.ComponentFactory.registerType("Informagen.ActiveTextArea", this);
    },

    componentType : "Informagen.ActiveTextArea"
});


// Informagen.Sync.ActiveTextArea =============================================================
//  The name of this object is irrelevant; see '$load' function.  However, keeping this 
//    class as a member of the namespace 'Informagen.Sync' adds consistency.


Informagen.Sync.ActiveTextArea = Core.extend(Echo.Sync.TextComponent, {


    // Register this class as the 'Sync' class for the 'Informagen.ActiveTextArea' class

    $load: function() {
        Echo.Render.registerPeer("Informagen.ActiveTextArea", this);
    },


    // Invoke virtual superclass method which is invoke by most handlers
    //  and property change events. It's the only virtual method available
    //  from TextComponent.

    sanitizeInput : function(event) {
        Echo.Sync.TextComponent.prototype.sanitizeInput.call(this, event);      
        this._adjustStatusLine();
    },


    /***************************************************************************
     *   Member variables
     *   ----------------
     *      _overdraft:      An integer used to suggest to the user that they
     *                       have exceeded the suggested maximum number of characters.
     *                       NB: The component will NOT truncate the return text
     *                       string, that decision is left up to the applications
     *                       business logic.
     *
     *      _lengthSpan:     The HTML span which styles and displays the current
     *                       text character count.
     *
     *      _remainingSpan:  The HTML span which styles and displays a count
     *                       of the number of characters remaining before
     *                       'maximumLength' is reached.  If 'maximumLength'
     *                       is zero or negative, this span is hidden.
     *
     *      _overdrawnSpan:  The HTML span which style and display the current
     *                       text character count
     *
     *      _isMsgVisible:   Boolean used to control the visibility of the
     *                       status spans. Not implemented in this release
     *
     */
     
    _lengthSpan: null,
    _remainingSpan: null,
    _overdrawnSpan: null,
    
    _overdraft: 0,
    _isMsgVisible: true,

    /***************************************************************************
     * Override the TextComponent superclass method:  Sync.TextComponentSync#renderAdd 
     *  If not for the supercall's use of 'renderAddToParent' we could have invoked
     *  the superclass method instead of copying it's contents into this method.
     */

    renderAdd: function(update, parentElement) {
        
        // Instance the HTML TextArea according to supplied properties
        this.input = document.createElement("textarea");
        this.input.id = this.component.renderId;
        
        if (!this.component.render("editable", true)) {
            this.input.readOnly = true;
        }
        
        this._renderStyle(this.input);
        this.input.style.overflow = "auto";
        
        // Add event listeners
        this._addEventHandlers(this.input);
        
        // Set 'text' property, empty string otherwise
        if (this.component.get("text")) {
            this.input.value = this.component.get("text");
        } else {
            this.input.value = "";
        }

        // Set  'overdraft' property, no overdraft otherwise      
        if (this.component.get("overdraft")) {
            this._overdraft = this.component.get("overdraft");
        } else {
            this._overdraft = 0;
        }

                
        // Build the composite component        
        var textAreaDiv = document.createElement("div");
        textAreaDiv.appendChild(this.input);
        
        // Build status 'div', length, remaining and overdrawn 'span's
        
        var statusDiv = document.createElement("div");
        statusDiv.setAttribute("style", "margin-top:3px;");
        
        this._lengthSpan = document.createElement("span");
        this._lengthSpan.setAttribute("style", "font-size:x-small;");
        statusDiv.appendChild(this._lengthSpan);
        
        this._remainingSpan = document.createElement("span");
        this._remainingSpan.setAttribute("style", "padding-left:1em;font-size:x-small;");
        statusDiv.appendChild(this._remainingSpan);
        
        this._overdrawnSpan = document.createElement("span");
        this._overdrawnSpan.setAttribute("style", "padding-left:1em;font-size:x-small;color:#ff0000;font-weight:bold;display:none;");
        statusDiv.appendChild(this._overdrawnSpan);

        
        // Assemble the composite component; Stacked vertically
        this.container = document.createElement("div");
        this.container.appendChild(textAreaDiv);
        this.container.appendChild(statusDiv);

        parentElement.appendChild(this.container);
        
        // Initialize the status line
        this._adjustStatusLine();
    },
    
    /***************************************************************************
     * Private method to update the status HTML spans by overriding the
     *  superclass mehtod '_storeValue' with '_local_storeValue' as described
     *  above.
     */

    _adjustStatusLine: function() {
    
        // Convert member variables to local for easier coding and debugging
        var textArea = this.input;
        var overdraft = this._overdraft;
        var lengthSpan = this._lengthSpan;

        // Current text area character count
        var length = textArea.value.length;
    
        lengthSpan.innerHTML = "Length: " + length;
        lengthSpan.style.display = "";
    
        // No limit; Just show character count and quit
        if(overdraft <= 0)
            return;

        // Calculate and display the remaining character count
        var remainingSpan = this._remainingSpan;
        var overdrawnSpan = this._overdrawnSpan;
    
        var remaining = overdraft - length;
        var overdrawn = (remaining < 0) ? -remaining : 0
        remaining = (remaining > 0) ? remaining : 0;

        remainingSpan.innerHTML = "Remaining: " + remaining;
        remainingSpan.style.display = "";

        // Display the number of characters over limit
        if(overdrawn > 0) {
        
            overdrawnSpan.innerHTML = "Over: " + overdrawn;
            overdrawnSpan.style.display = "";
            
            Echo.Sync.Color.render(this.component.render("overdraftForegroundColor", "#000000"), this.input, "color");
            Echo.Sync.Color.render(this.component.render("overdraftBackgroundColor", "#ffffff"), this.input, "backgroundColor");
           
        } else {
            overdrawnSpan.style.display = "none";

            Echo.Sync.Color.render(this.component.render("foreground", "#000000"), this.input, "color");
            Echo.Sync.Color.render(this.component.render("background", "#ffffff"), this.input, "backgroundColor");
        }
    
    }


});
