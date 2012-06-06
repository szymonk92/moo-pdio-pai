/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation.pixelComparers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Lukasz
 */
public abstract class AbstractPixelComparer implements IPixelComparer {

    protected String name;
    protected String description;
    protected PropertyChangeSupport changeSupport;
    protected BufferedImage image;
    protected int value;

    public AbstractPixelComparer() {
        changeSupport = new PropertyChangeSupport(this);
    }

    public AbstractPixelComparer(int value) {
        this();
        this.value = value;
    }

    public AbstractPixelComparer(AbstractPixelComparer comperer) {
        this(comperer.value);
        this.name = comperer.getName();
        this.description = comperer.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public PropertyChangeSupport getChangeSupport() {
        return changeSupport;
    }

    @Override
    public boolean Compare(Color pixelColor, Color areaColor, boolean grayScale) {
        return getCompareValue(pixelColor, areaColor, grayScale) <= this.value;
    }
}
