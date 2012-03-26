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
public class NonLinearFilter extends AbstractFilter {
    
    int R;
    
    public NonLinearFilter() {
        this.name = "Nonlinear filter";
        this.setEditable(true);
        R = 1;
    }
    
    public NonLinearFilter(NonLinearFilter filter) {
        super(filter);
        this.R = filter.getR();
    }
    
    public int getR() {
        return R;
    }
    
    public void setR(int R) {
        this.R = R;
        this.changeSupport.firePropertyChange("R", null, this.R);
        
    }
    
    @Override
    public JPanel getEditPanel() {
        return new NonLinearFilterPanel(this);
    }
    
    @Override
    public IFilter getCopy() {
        return new NonLinearFilter(this);
    }
    
    @Override
    public BufferedImage processImage(BufferedImage image) {
        
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        
        int RGBA, RGBAA;
        int r, g, b;
        
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int r1 = 0, r2 = 0, b1 = 0, b2 = 0, g1 = 0, g2 = 0;
                int s1 = 0, s2 = 0; /* FIXME: Nie użyte zminnne */
                for (int i = 1; i <= R; ++i) {
                    RGBA = x + i - 1 >= image.getWidth() ? 0 : image.getRGB(x + i - 1, y);
                    RGBAA = x - i < 0 ? 0 : image.getRGB(x - i, y);
                    r1 += RGBHelper.getRed(RGBA);
                    g1 += RGBHelper.getGreen(RGBA);
                    b1 += RGBHelper.getBlue(RGBA);
                    r2 += RGBHelper.getRed(RGBAA);
                    g2 += RGBHelper.getGreen(RGBAA);
                    b2 += RGBHelper.getBlue(RGBAA);
                }
                
                r = (r1 - r2) / R;
                g = (g1 - g2) / R;
                b = (b1 - b2) / R;
                
                out.setRGB(x, y, RGBHelper.toPixel(r, g, b));
            }
        }
        return out;        
        
        
    }
}
