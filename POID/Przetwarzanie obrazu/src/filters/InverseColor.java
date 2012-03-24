/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.JPanel;
import sys.AbstractFilter;
import sys.IFilter;
import sys.RGBHelper;

/**
 *
 * @author Lukasz
 */
public class InverseColor extends AbstractFilter {

    private boolean enabled;
    private short[] invertTable;

    public InverseColor() {
        this(true);
    }

    public InverseColor(boolean enabled) {
        this.setName("Inverse color");
        this.setDescription("Ten filtr odwrca kolory na obrazie.");
        this.setEditable(true);
        invertTable = new short[256];
        for (int i = 0; i < 256; i++) {
            invertTable[i] = (short) (255 - i);
        }
    }

    public InverseColor(InverseColor filter) {
        super(filter);
        this.enabled = filter.isEnabled();
        this.invertTable = filter.invertTable.clone();
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        if (enabled) {
            int[] RGBA;
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    RGBA = RGBHelper.toRGBA(image.getRGB(x, y));
                    image.setRGB(x, y, RGBHelper.toPixel(invertTable[RGBA[0]], invertTable[RGBA[1]], invertTable[RGBA[2]], 255)); //set the pixel to the altered colors
                }
            }
        }
        return image;
    }

    @Override
    public JPanel getEditPanel() {
        return new InverseColorPanel(this);
    }

    @Override
    public IFilter getCopy() {
        return new InverseColor(this);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.changeSupport.firePropertyChange("Enabled", null, this.enabled);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getIcon() {
        return "/images/invert.png";
    }
}
