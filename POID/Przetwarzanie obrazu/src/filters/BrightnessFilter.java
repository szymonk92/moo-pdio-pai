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
 * @author Lukasz
 */
public class BrightnessFilter extends AbstractFilter {

    private int brightnessValue = 0;

    public int getBrightnessValue() {
        return brightnessValue;
    }



    public BrightnessFilter() {
        this.setName("Brightness");
        this.setEditable(true);
    }

    public BrightnessFilter(BrightnessFilter filter) {
        super(filter);
        this.brightnessValue = filter.getBrightnessValue();
    }

    public void setBrightnessValue(int value) {
        this.brightnessValue = value;
        this.changeSupport.firePropertyChange("brightnessValue", null, this.brightnessValue);
    }


    @Override
    public IFilter getCopy() {
        return new BrightnessFilter(this);
    }

    @Override
    public JPanel getEditPanel() {
        return new BrightnessFilterPanel(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        //Color col;
        int[] RGBA;
       
        //RGBA = image.getRGB(0, 0);
        //col = new Color(RGBA, true);

        int r, g, b;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGBA = RGBHelper.toRGBA(image.getRGB(x, y));
                //col = new Color(RGBA, true);
                float brightnessAdjust = ((float) brightnessValue / 255.0f) * 8.0f;
                RGBA[0] = (int) (RGBA[0]*brightnessAdjust );
                RGBA[1] = (int) (RGBA[1]*brightnessAdjust );
                RGBA[2] = (int) (RGBA[2]*brightnessAdjust );
                image.setRGB(x, y, RGBHelper.toPixel(RGBA[0], RGBA[1], RGBA[2], 255));
            }
        }

        return image;
    }



    @Override
    public String getIcon() {
        return "/images/brightness.png";
    }
}
