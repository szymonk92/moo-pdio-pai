/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.image.BufferedImage;
import sys.AbstractFilter;
import sys.IFilter;
import sys.RGBHelper;

/**
 *
 * @author Lukasz
 */
public class InverseColor extends AbstractFilter {

    private short[] invertTable;

    public InverseColor() {
        this.setName("Inverse color");
        this.setDescription("Ten filtr odwrca kolory na obrazie.");
        invertTable = new short[256];
        for (int i = 0; i < 256; i++) {
            invertTable[i] = (short) (255 - i);
        }
    }

    public InverseColor(InverseColor filter) {
        super(filter);
        this.invertTable = filter.invertTable.clone();
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
            int[] RGBA;
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    RGBA = RGBHelper.toRGBA(image.getRGB(x, y));
                    image.setRGB(x, y, RGBHelper.toPixel(invertTable[RGBA[0]], invertTable[RGBA[1]], invertTable[RGBA[2]], 255)); //set the pixel to the altered colors
                }
            }
        return image;
    }


    @Override
    public IFilter getCopy() {
        return new InverseColor(this);
    }

    @Override
    public String getIcon() {
        return "/images/invert.png";
    }
}
