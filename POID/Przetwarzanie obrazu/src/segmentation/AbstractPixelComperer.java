/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Lukasz
 */
public abstract class AbstractPixelComperer implements IPixelComperer {
    protected String name;
    protected String description;
    protected PropertyChangeSupport changeSupport;
    protected BufferedImage image;
    
    public AbstractPixelComperer() {
        changeSupport = new PropertyChangeSupport(this);
    }

    public AbstractPixelComperer(AbstractPixelComperer comperer) {
        this();
        this.name = comperer.getName();
        this.description = comperer.getDescription();
    }
    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public void setImage(BufferedImage image) {
       this.image = image;
    }

   @Override
    public String getIcon() {
        return null;
    }

    @Override
    public PropertyChangeSupport getChangeSupport() {
        return changeSupport;
    }

    

   
    
    
}
