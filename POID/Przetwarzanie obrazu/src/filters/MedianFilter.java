/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JPanel;
import sys.AbstractFilter;
import sys.IFilter;
import sys.RGBHelper;

/**
 *
 * @author pawel
 */
public class MedianFilter extends AbstractFilter{
    
    int value;

    public MedianFilter() {
        this.name="Median Filter";
        this.setEditable(true);
        this.value=3;
    }

    public MedianFilter(int value) {
        this();
        this.value = value;
    }

    public MedianFilter(MedianFilter filter) {
        this.name = filter.getName();
        this.value = filter.getValue();
    }
    
    
    

    public void setValue(int value) {
        this.value = value;
        this.changeSupport.firePropertyChange("value", null, this.value);
    }

    public int getValue() {
        return value;
    }
    
    

    @Override
    public JPanel getEditPanel() {
        return new MedianFilterPanel(this);
    }
    

    @Override
    public IFilter getCopy() {
        return new MedianFilter(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        
        BufferedImage out = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        
        int RGBA,RGBAA;
        int r, g, b,ai,mid,mid2;
        int len=value/2;
        int sum = ((len*2) + 1)*((len*2) + 1);
        int[] red=new int[sum];
        int[] green=new int[sum];
        int[] blue=new int[sum];
        int[] c=new int[sum];
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                ai=0;
                
                for( int i=x-len; i<=x+len; ++i) {
                    for ( int j=y-len;j<=y+len; ++j) {
                        RGBAA = (i<0 || j<0  ||
                                i > image.getWidth()-1 || j > image.getHeight()-1)?
                                0 :image.getRGB(i, j);
                        red[ai]=RGBHelper.getRed(RGBAA);
                        green[ai]=RGBHelper.getGreen(RGBAA);
                        blue[ai]=RGBHelper.getBlue(RGBAA);
                        c[ai]=RGBAA;
                        ai++;
                    }
                }
                Arrays.sort(red);Arrays.sort(blue);Arrays.sort(green);
                
                
                if (sum%2 == 0) { //parzyste, nie zachodzi
                    mid = sum / 2;
                    mid2 = (sum / 2) + 1;
                    r = (red[mid] + red[mid2]) / 2;
                    g = (green[mid] + green[mid2]) / 2;
                    b = (blue[mid] + blue[mid2]) / 2; 
                } else { //nieparzyste
                    mid=sum/2;
                    r=red[mid];
                    g=green[mid];
                    b=blue[mid];
                }

                
                
                out.setRGB(x, y, new Color(RGBHelper.calmp(r), RGBHelper.calmp(g), RGBHelper.calmp(b)).getRGB());
            }
        }
        return out;  
    }
    
}
