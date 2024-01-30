package org.informagen.echo.app;

public enum ActiveTextFieldIcon {

    // Names of ActiveTextField built-in icons
    //  All icons must be 13x13 pixels
    
    EMPTY("empty"), 
    GOOD("good"), 
    ERROR("error"), 
    WARNING("warning"), 
    RIGHT_ARROW("rightArrow"), 
    LEFT_ARROW("leftArrow");
    
    private String name;
 
    // Constructor
    private ActiveTextFieldIcon(String name) { this.name = name; } 
 
    String getName() { return name; }
    
};


