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
        int RGBA,RGBAA;
        RGBA = image.getRGB(0, 0);
        int r, g, b;
        int [][] tmp = new int[image.getWidth()][3];
        int len=value/2;
        int sum = (len*2)*(len*2);
        
        for (int x = 0; x < image.getHeight(); ++x) {
            for (int y = 0; y < image.getWidth(); ++y) {
                RGBA = image.getRGB(y, x);
                r = RGBHelper.getRed(RGBA);
                g = RGBHelper.getGreen(RGBA);
                b = RGBHelper.getBlue(RGBA);
                int maxj = x + len >= image.getHeight() ? image.getHeight() : x + len,
                        maxi = y + len >= image.getWidth() ? image.getWidth() : y + len;
                for (int i = y - len < 0 ? 0 : y - len; i < maxi; ++i) {

                    if (y == 0 || i == y + len - 1) {
                        tmp[i][0] = tmp[i][1] = tmp[i][2] = 0;
                        for (int j = x - len < 0 ? 0 : x - len; j < maxj; ++j) {
                            RGBAA = image.getRGB(i, j);
                            tmp[i][0] += RGBHelper.getRed(RGBAA);
                            tmp[i][1] += RGBHelper.getGreen(RGBAA);
                            tmp[i][2] += RGBHelper.getBlue(RGBAA);
                        }
                    }
                    r += (i < 0 || i >= image.getWidth()) ? 0 : tmp[i][0];
                    g += (i < 0 || i >= image.getWidth()) ? 0 : tmp[i][1];
                    b += (i < 0 || i >= image.getWidth()) ? 0 : tmp[i][2];

                }
                r /= sum;
                g /= sum;
                b /= sum;
                

                image.setRGB(y, x, new Color(RGBHelper.calmp(r), RGBHelper.calmp(g), RGBHelper.calmp(b)).getRGB());
            }
        }
        return image;    

        
    
    }
    
    
} 
