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
import sys.TabData;

/**
 *
 * @author pawel
 */
public class ContrastFilter extends AbstractFilter {

    private float value;
    private int[] lut;

    public ContrastFilter() {
        this(1);
    }

    public ContrastFilter(float value) {
        this.value = value;
        this.setName("Contrast");
        this.setEditable(true);
        this.lut = new int[256];
        generateLut();
    }

    public ContrastFilter(ContrastFilter filter) {
        super(filter);
        this.value = filter.getValue();
        this.lut = filter.lut.clone();
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        generateLut();
        this.changeSupport.firePropertyChange("value", null, this.value);
    }

    private void generateLut() {
        for (int i = 0; i < 256; i++) {
            //lut[i] = RGBHelper.calmp((int) (((i - value) * Math.exp(value / 50.0f)) + value));
            lut[i] = RGBHelper.calmp((int) (((i - 127) * value) + 127));
        }
    }

    @Override
    public JPanel getEditPanel(TabData data) {
        return new ContrastFilterPanel(this);
    }

    @Override
    public IFilter getCopy() {
        return new ContrastFilter(this);
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
}
