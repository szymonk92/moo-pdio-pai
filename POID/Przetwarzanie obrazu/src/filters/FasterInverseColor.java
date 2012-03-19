/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import sys.AbstractFilter;
import sys.IFilter;

/**
 *
 * @author Lukasz
 */
public class FasterInverseColor extends AbstractFilter {

    private short[] invertTable;

    public FasterInverseColor() {
        this.setName("Faster Inverse color");
        this.setDescription("Ten filtr odwrca kolory na obrazie.");
        this.setEditable(false);
        invertTable = new short[256];
        for (int i = 0; i < 256; i++) {
            invertTable[i] = (short) (255 - i);
        }
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        final BufferedImage dst = new BufferedImage(w, h, image.getType());

        final BufferedImageOp invertOp = new LookupOp(new ShortLookupTable(0, invertTable), null);
        return invertOp.filter(image, dst);
    }

    @Override
    public IFilter getCopy() {
        return new FasterInverseColor();
    }
}
