/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.Color;
import sys.RGBHelper;

/**
 *
 * @author Lukasz
 */
public class SimplePixelComparer extends AbstractPixelComparer {

    public SimplePixelComparer(int value) {
        this.value = value;
    }

    @Override
    public double getCompareValue(Color pixelColor, Color areaColor, boolean grayScale) {
        int[] pixelA = RGBHelper.toRGBA(pixelColor.getRGB());
        int[] pixelB = RGBHelper.toRGBA(areaColor.getRGB());
        return Math.max(Math.max(pixelA[0] - pixelB[0],pixelA[1] - pixelB[1]), pixelA[2] - pixelB[2]);
    }

    @Override
    public IPixelComparer getCopy() {
        return null;
    }
}
