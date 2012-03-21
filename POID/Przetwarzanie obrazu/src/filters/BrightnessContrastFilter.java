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
public class BrightnessContrastFilter extends AbstractFilter {

    private int brightnessValue = 0;
    private double contrastValue = 1;

    public int getBrightnessValue() {
        return brightnessValue;
    }

    public double getContrastValue() {
        return contrastValue;
    }

    public BrightnessContrastFilter() {
        this.setName("Brightness/Contrast");
        this.setEditable(true);
    }

    public BrightnessContrastFilter(BrightnessContrastFilter filter) {
        super(filter);
        this.brightnessValue = filter.getBrightnessValue();
        this.contrastValue = filter.contrastValue;
    }

    public void setBrightnessValue(int value) {
        this.brightnessValue = value;
        this.changeSupport.firePropertyChange("brightnessValue", null, this.brightnessValue);
    }

    public void setContrastValue(double value) {
        this.contrastValue = value;
        this.changeSupport.firePropertyChange("contrastValue", null, this.contrastValue);
    }

    @Override
    public IFilter getCopy() {
        return new BrightnessContrastFilter(this);
    }

    @Override
    public JPanel getEditPanel() {
        return new BrightnessContrastFilterPanel(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        Color col;
        int RGBA;
        RGBA = image.getRGB(0, 0);
        col = new Color(RGBA, true);

        int avgR = col.getRed();
        int avgG = col.getGreen();
        int avgB = col.getBlue();

        for (int x = 1; x < image.getWidth(); x++) {
            for (int y = 1; y < image.getHeight(); y++) {
                RGBA = image.getRGB(x, y);
                col = new Color(RGBA, true);
                avgR += col.getRed();
                avgR = avgR / 2;
                avgG += col.getGreen();
                avgG = avgG / 2;
                avgB += col.getBlue();
                avgB = avgB / 2;
            }
        }
        int r, g, b;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGBA = image.getRGB(x, y);
                col = new Color(RGBA, true);
                r = (int) ((col.getRed() - avgR) * contrastValue + avgR + brightnessValue);
                g = (int) ((col.getGreen() - avgG) * contrastValue + avgG + brightnessValue);
                b = (int) ((col.getBlue() - avgB) * contrastValue + avgB + brightnessValue);
                image.setRGB(x, y, new Color(RGBHelper.calmp(r), RGBHelper.calmp(g), RGBHelper.calmp(b)).getRGB());
            }
        }

        return image;
    }



    @Override
    public String getIcon() {
        return "/images/brightness.png";
    }
}
