package org.informagen.echo.app;

import org.informagen.echo.app.ActiveTextField;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class RegExTextField extends ActiveTextField {

    // Non-style properties
    public static final String PROPERTY_FILTER = "characterFilter";
    public static final String PROPERTY_REGEX = "regexPattern";

    private String characterFilter = null;
    private String regexPattern = null;
    
    private Pattern pattern;

    public RegExTextField() {
        this(".", ".");
    }

    public RegExTextField(String filter, String regex) {
        setFilter(filter);
        setRegEx(regex);
    }

    public boolean isValid() {
    
        String text = getText();
        
        // Empty field, defined as invalid
        if( isEmpty(text) )
            return false;
        
        // No RegEx pattern to test against, defined as valid
        if( isEmpty(regexPattern) || regexPattern.equals("."))
            return true;
        
        // Must use 'find' and not 'matches' so that the Java validation
        //   behavior is as close to the JavaScript valication behavior
        //   as possible. Otherwise, we'd have to go to the client.
        
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }


    /**
     *  A regular expression pattern used to filter input characters
     */
     
    public void setFilter(final String filter) {
        
        String oldValue = characterFilter;
        characterFilter = (filter == null) ? "." : filter;
        firePropertyChange(PROPERTY_FILTER, oldValue, characterFilter);
    }

    public String getFilter() {
        return characterFilter;
    }


    /**
     *  A regular expression pattern used to validate the current textfield contents
     */

    public void setRegEx(final String regex) {

        String oldValue = regexPattern;
        regexPattern = (regex == null) ? "." : regex;
        pattern = Pattern.compile(regex);

        firePropertyChange(PROPERTY_REGEX, oldValue, regexPattern);
    }   

    public String getRegEx() {
        return regexPattern;
    }



}