/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

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
        this(3);
    }
    
    public MeanFilter(MeanFilter filter) {
        super(filter);
        this.value = filter.getValue();
    }
    
    public MeanFilter(int value) {
        this.value = value;
        this.setName("Mean Filter");
        this.setEditable(true);
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
        int[] BaseRGBA, RGBA,RGBAA;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                BaseRGBA = RGBHelper.toRGBA(image.getRGB(x, y));
                RGBA = BaseRGBA.clone(); //klonowanie jest najszybsze
                int len=value/2;
                int sum = (len*2)*(len*2);
                for( int i=x-len; i<x+len; ++i) {
                    for ( int j=y-len;j<y+len; ++j) {
                        RGBAA = (i<0 || j<0  || i > image.getWidth()-1 || j > image.getHeight()-1)? BaseRGBA :RGBHelper.toRGBA(image.getRGB(i, j));
                        RGBA[0]+=RGBAA[0];
                        RGBA[1]+=RGBAA[1];
                        RGBA[2]+=RGBAA[2];
                    }
                }
                RGBA[0]/=sum;
                RGBA[1]/=sum;
                RGBA[2]/=sum;
                image.setRGB(x, y, RGBHelper.toPixel(RGBA[0], RGBA[1], RGBA[2], 255));
            }
        }
        return image;    

        
    
    }
    
    
} 
