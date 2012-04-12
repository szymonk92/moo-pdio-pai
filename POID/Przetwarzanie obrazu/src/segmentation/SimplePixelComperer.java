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
public class SimplePixelComperer extends AbstractPixelComperer{

    private int value;
    public SimplePixelComperer(int value){
        this.value = value;
    }
    @Override
    public boolean Comppere(int x, int y, Color areaColor) {
        int[] pixelA = RGBHelper.toRGBA(image.getRGB(x, y));
        int[] pixelB = RGBHelper.toRGBA(areaColor.getRGB());
        double pixelValue = Math.pow(pixelA[0]-pixelB[0],2);
        pixelValue += Math.pow(pixelA[1]-pixelB[1],2);
        pixelValue += Math.pow(pixelA[2]-pixelB[2],2);
        pixelValue/=3;
        return pixelValue<this.value;
    }

    @Override
    public IPixelComperer getCopy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
    
}
