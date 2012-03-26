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
 * @author Lukasz
 */
public class BrightnessFilter extends AbstractFilter {

    private int brightnessValue = 0;
    private int[] lut;

    public int getBrightnessValue() {
        return brightnessValue;
    }

    public BrightnessFilter() {
        this.setName("Brightness");
        this.setEditable(true);
        this.lut = new int[256];
        generateLut();
    }

    public BrightnessFilter(BrightnessFilter filter) {
        super(filter);
        this.brightnessValue = filter.getBrightnessValue();
        this.lut = filter.lut.clone();
    }

    public void setBrightnessValue(int value) {
        this.brightnessValue = value;
        generateLut();
        this.changeSupport.firePropertyChange("brightnessValue", null, this.brightnessValue);
    }

    private void generateLut() {
        for (int i = 0; i < 256; i++) {
            lut[i] = RGBHelper.calmp(i + brightnessValue);
            //float brightnessAdjust = ((float) brightnessValue / 255.0f) * 8.0f;
        }
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
        int[] RGBA;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGBA = RGBHelper.toRGBA(image.getRGB(x, y));
                image.setRGB(x, y, RGBHelper.fastToPixel(lut[RGBA[0]], lut[RGBA[1]], lut[RGBA[2]]));
            }
        }
        return image;
    }

    @Override
    public String getIcon() {
        return "/images/brightness.png";
    }
}
