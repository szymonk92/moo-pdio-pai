/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation.pixelComparers;

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
        if (grayScale) {
            return Math.abs(pixelA[0] - pixelB[0]);
        } else {
            return Math.max(Math.max(Math.abs(pixelA[0] - pixelB[0]), Math.abs(pixelA[1] - pixelB[1])), Math.abs(pixelA[2] - pixelB[2]));
        }
    }
}
