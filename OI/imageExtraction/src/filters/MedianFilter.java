/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import sys.RGBHelper;

/**
 *
 * @author pawel
 */
public class MedianFilter{

    int value;

    public MedianFilter() {
        this(3);
    }

    public MedianFilter(int value) {
        this.value = value;
    }


    public void setValue(int value) {
        this.value = value;
        
    }

    public int getValue() {
        return value;
    }
    
    public BufferedImage processImage(BufferedImage image) {

        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int RGBA;
        int r, g, b, ai, mid, mid2;
        int len = value / 2;
        int sum = ((len * 2) + 1) * ((len * 2) + 1);
        int[] red = new int[sum];
        int[] green = new int[sum];
        int[] blue = new int[sum];
        int[] c = new int[sum];
        if (sum % 2 == 0) { //parzyste, nie zachodzi
            mid = sum / 2;
            mid2 = (sum / 2) + 1;
        } else { //nieparzyste
            mid = sum / 2;
            mid2 = 0;
        }
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                ai = 0;

                for (int i = x - len; i <= x + len; ++i) {
                    for (int j = y - len; j <= y + len; ++j) {
                        RGBA = (i < 0 || j < 0
                                || i > image.getWidth() - 1 || j > image.getHeight() - 1)
                                ? 0 : image.getRGB(i, j);
                        red[ai] = RGBHelper.getRed(RGBA);
                        green[ai] = RGBHelper.getGreen(RGBA);
                        blue[ai] = RGBHelper.getBlue(RGBA);
                        c[ai] = RGBA;
                        ai++;
                    }
                }
                Arrays.sort(red);
                Arrays.sort(blue);
                Arrays.sort(green);

                if (sum % 2 == 0) { //parzyste, nie zachodzi
                    r = (red[mid] + red[mid2]) / 2;
                    g = (green[mid] + green[mid2]) / 2;
                    b = (blue[mid] + blue[mid2]) / 2;
                } else { //nieparzyste
                    r = red[mid];
                    g = green[mid];
                    b = blue[mid];
                }
                out.setRGB(x, y, RGBHelper.fastToPixel(r, g, b));
            }
        }
        return out;
    }
}