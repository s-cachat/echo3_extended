/*
 */

package org.informagen.echo.app;

import org.informagen.echo.app.StaticImage;


// Echo - Components
import nextapp.echo.app.AwtImageReference;
import nextapp.echo.app.Component;
import nextapp.echo.app.HttpImageReference;
import nextapp.echo.app.ImageReference;
import nextapp.echo.app.ResourceImageReference;

// Java AWT
import java.awt.Image;
import java.awt.image.BufferedImage;

// Java IO
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

// Java ImageIO
import javax.imageio.ImageIO;


public class ReflectedImage extends StaticImage {


    public static final String PROPERTY_GAP = "gap";
    public static final String PROPERTY_REFECTIVITY = "reflectivity";
    public static final String PROPERTY_REFLECTED_HEIGHT = "reflectedHeight";

    private int gap = 2;
    private double reflectivity = 0.50;
    private double reflectedHeight = 0.50;
            
    /** Default constructor. */

    public ReflectedImage() {}
    
    /**
    * Create a new instance using the specified image reference.
    *
    * @param imageReference The image to associate with this component
    */

    public ReflectedImage(final ImageReference imageReference ) {
        super(imageReference);
    }
 
     /**
    * Return the value of {@link #PROPERTY_GAP} property.
    *
    * @return The amount of space between the image and its reflection
    */

    public int getGap() {
        return gap;
    }
    
    
    /**
    * Set the value of {@link #PROPERTY_GAP} property.
    *
    * @param gap The amount of space between the image and its reflection in pixels
    */

    public void setGap(int gap ) {
        Object oldValue = Integer.valueOf(this.gap);
        this.gap = gap;
        firePropertyChange(PROPERTY_GAP, oldValue, Integer.valueOf(gap));
    }

     /**
    * Return the value of {@link #PROPERTY_REFECTIVITY} property.
    *
    * @return The amount of reflectivity, lack of transparency,
    */

    public double getReflectivity() {
        return reflectivity;
    }
    
    
    /**
    * Set the value of {@link #PROPERTY_REFECTIVITY} property.
    *
    * @param reflectivity The amount of 
    */

    public void setReflectivity(double reflectivity ) {
        Object oldValue = Double.valueOf(this.reflectivity);
        this.reflectivity = reflectivity;
        firePropertyChange(PROPERTY_REFECTIVITY, oldValue, Double.valueOf(reflectivity));

    }
   
 
     /**
    * Return the value of {@link #PROPERTY_REFLECTED_HEIGHT} property.
    *
    * @return The amount of opacity, lack of transparency,
    */

    public double getReflectedHeight() {
        return reflectedHeight;
    }
    
    
    /**
    * Set the value of {@link #PROPERTY_REFLECTED_HEIGHT} property.
    *
    * @param opacity The amount of 
    */

    public void setReflectedHeight(double reflectedHeight ) {
        Object oldValue = Double.valueOf(this.reflectedHeight);
        this.reflectedHeight = reflectedHeight;
        firePropertyChange(PROPERTY_REFLECTED_HEIGHT, oldValue, Double.valueOf(reflectedHeight));

    }
   
}
