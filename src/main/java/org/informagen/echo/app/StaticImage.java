/*
 */

package org.informagen.echo.app;

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


public class StaticImage extends Component {
        
    public static final String PROPERTY_IMAGEREFERENCE = "imageReference";

    private ImageReference imageReference = null;
    
    /** Default constructor. */

    public StaticImage() {}
    
    /**
    * Create a new instance using the specified image reference.
    *
    * @param imageReference The image to associate with this component
    */

    public StaticImage(final ImageReference imageReference ) {
        setImageReference(imageReference);
    }
    
    
    /**
    * Return the value of {@link #PROPERTY_IMAGEREFERENCE} property.
    *
    * @return The image reference currently in use
    */

    public ImageReference getImageReference() {
        return imageReference;
    }
    
    
    /**
    * Set the value of {@link #PROPERTY_IMAGEREFERENCE} property.
    *
    * @param imageReference The image reference to set
    */

    public void setImageReference(final ImageReference imageReference ) {
        Object oldValue = this.imageReference;
        this.imageReference = imageReference;
        firePropertyChange(PROPERTY_IMAGEREFERENCE, oldValue, imageReference);

    }


    /*
     *  Statics methods for loading images from various sources; file, Web and resource
     */

    public synchronized static ImageReference fromFile(File file) {
    
        Image image = null;
    
        try {
            InputStream is = new FileInputStream(file);
            image = ImageIO.read(new BufferedInputStream(is));
        } catch (IOException ioException) {
            return null;
        }
        
        return fromImage(image);
    }


    public synchronized static ImageReference fromURL(String urlString) {
        return new HttpImageReference(urlString);
    }

    public synchronized static ImageReference fromResource(String resourceLocation) {
        return new ResourceImageReference(resourceLocation);
    }

    public synchronized static ImageReference fromImage(Image image) {
        return (image != null) ? new AwtImageReference(image) : null;
    }

    
}
