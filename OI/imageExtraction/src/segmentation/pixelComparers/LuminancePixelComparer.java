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
public class LuminancePixelComparer extends AbstractPixelComparer {

    public LuminancePixelComparer(int value) {
        this.value = value;
    }

    @Override
    public double getCompareValue(Color pixelColor, Color areaColor, boolean grayScale) {
        int[] pixelA = RGBHelper.toRGBA(pixelColor.getRGB());
        int[] pixelB = RGBHelper.toRGBA(areaColor.getRGB());
        double valueA;
        double valueB;
        if (grayScale) {
            valueA = pixelA[0];
            valueB = pixelB[0];
        } else {
            valueA = 0.299 * pixelA[0] + 0.587 * pixelA[1] + 0.114 * pixelA[2];
            valueB = 0.299 * pixelB[0] + 0.587 * pixelB[1] + 0.114 * pixelB[2];
        }
        return Math.abs(valueA - valueB);
    }

}
