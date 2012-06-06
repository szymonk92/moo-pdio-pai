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
public class EuclideanPixelComparer extends AbstractPixelComparer {

    public EuclideanPixelComparer(int value) {
        this.value = value;
    }

    @Override
    public double getCompareValue(Color pixelColor, Color areaColor, boolean grayScale) {
        int[] pixelA = RGBHelper.toRGBA(pixelColor.getRGB());
        int[] pixelB = RGBHelper.toRGBA(areaColor.getRGB());
        double pixelValue = Math.pow(pixelA[0] - pixelB[0], 2);
        if(!grayScale){
            pixelValue += Math.pow(pixelA[1] - pixelB[1], 2);
            pixelValue += Math.pow(pixelA[2] - pixelB[2], 2);
        }
        return Math.sqrt(pixelValue);
    }
}
