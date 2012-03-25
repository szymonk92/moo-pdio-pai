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
public class ContrastFilter extends  AbstractFilter {
    private float value;

    public ContrastFilter() {
        this.value = 1;
        this.setName("Contrast");
        this.setEditable(true);
    }
    

    public ContrastFilter(float value) {
        this.value = value;
        this.setName("Contrast");
        this.setEditable(true);
    }
    
    public ContrastFilter(ContrastFilter filter) {
        this.value = filter.getValue();
        this.name= filter.getName();
    }

    
    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
        this.changeSupport.firePropertyChange("value", null, this.value);
    }

    @Override
    public JPanel getEditPanel() {
        return new ContrastFilterPanel(this);
    }
    
    
    

    @Override
    public IFilter getCopy() {
        return new ContrastFilter(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        Color col;
        int RGBA;
        RGBA = image.getRGB(0, 0);
        int r, g, b;
        float rt, gt, bt;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGBA = image.getRGB(x, y);
                col = new Color(RGBA, true);
                rt=col.getRed(); gt=col.getGreen(); bt=col.getBlue();
              // rt/=255.0f;gt/=255.0f;bt/=255.0f;
               // r = (int) ((((rt-0.5f)*value/100.0f)+0.5f)*255.0f);
                //g = (int) ((((gt-0.5f)*value/100.0f)+0.5f)*255.0f);
                //b = (int) ((((bt-0.5f)*value/100.0f)+0.5f)*255.0f);
                r = (int) (((rt-value)*Math.exp(value/50.0f))+value);
                g = (int) (((gt-value)*Math.exp(value/50.0f))+value);
                b = (int) (((bt-value)*Math.exp(value/50.0f))+value);
                image.setRGB(x, y, new Color(RGBHelper.calmp(r), RGBHelper.calmp(g), RGBHelper.calmp(b)).getRGB());
            }
        }
        return image;    
    }
    
}
