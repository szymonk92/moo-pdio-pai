/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import sys.AbstractFilter;
import sys.IFilter;
import sys.RGBHelper;

/**
 *
 * @author pawel
 */
public class MeanFilter extends AbstractFilter {
    /**
     * dimension of sibilings 
     */
    int value =3;
    
    public MeanFilter() {
        this.value = 3;
        this.setName("Mean Filter");
        this.setEditable(true);
        
    }
    public MeanFilter(MeanFilter filter) {
        this.value = filter.getValue();
        this.name = filter.getName();
        
    }
    public MeanFilter(int value) {
        this();
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.changeSupport.firePropertyChange("value", null, this.value);
    }

    
@Override
    public JPanel getEditPanel() {
        return new MeanFilterPanel(this);
    }
    
    @Override
    public IFilter getCopy() {
        return new MeanFilter(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
                Color col;
        int RGBA;
        RGBA = image.getRGB(0, 0);
        int r, g, b;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGBA = image.getRGB(x, y);
                col = new Color(RGBA, true);
                //tmp fake
                
                r=col.getRed();b=col.getBlue();g=col.getGreen();
                image.setRGB(x, y, new Color(RGBHelper.calmp(r), RGBHelper.calmp(g), RGBHelper.calmp(b)).getRGB());
            }
        }
        return image;    

        
    }
    
    
} 
