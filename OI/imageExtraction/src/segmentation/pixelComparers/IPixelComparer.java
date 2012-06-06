/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation.pixelComparers;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Lukasz
 */
public interface IPixelComparer{
    public void setImage(BufferedImage image);
    public boolean Compare(Color pixelColor, Color areaColor, boolean grayScale);
    public double getCompareValue(Color pixelColor, Color areaColor, boolean grayScale);
}
