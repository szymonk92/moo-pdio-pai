/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz
 */
public class RGBHelper {

    static public int calmp(int value) {
        return Math.max(Math.min(value, 255), 0);
    }

    public static int toPixel(int r, int g, int b, int a) {
        return ((calmp(a) & 0xFF) << 24) | ((calmp(r) & 0xFF) << 16) | ((calmp(g) & 0xFF) << 8) | ((calmp(b) & 0xFF));
    }
    
    public static int fastToPixel(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
    }
    
    public static int[] toRGBA(int pixel) {
        int[] result = new int[4];
        result[0] = (pixel >> 16) & 0xff;//red
        result[1] = (pixel >> 8) & 0xff;//green
        result[2] = (pixel) & 0xff;//blue
        result[3] = (pixel >> 24) & 0xff;//alpha
        return result;
    }
    
    public static int getRed(int pixel) {
        return  (pixel >> 16) & 0xff;//red
    }
    public static int getGreen(int pixel) {
        return  (pixel >> 8) & 0xff;//green
    }
    public static int getBlue(int pixel) {
        return (pixel) & 0xff;//blue
    }
    

    public static double[] hsv2rgb(double hue, double sat, double val) {
        double red = 0, grn = 0, blu = 0;
        double i, f, p, q, t;
        double result[] = new double[3];

        if (val == 0) {
            red = 0;
            grn = 0;
            blu = 0;
        } else {
            hue /= 60;
            i = Math.floor(hue);
            f = hue - i;
            p = val * (1 - sat);
            q = val * (1 - (sat * f));
            t = val * (1 - (sat * (1 - f)));
            if (i == 0) {
                red = val;
                grn = t;
                blu = p;
            } else if (i == 1) {
                red = q;
                grn = val;
                blu = p;
            } else if (i == 2) {
                red = p;
                grn = val;
                blu = t;
            } else if (i == 3) {
                red = p;
                grn = q;
                blu = val;
            } else if (i == 4) {
                red = t;
                grn = p;
                blu = val;
            } else if (i == 5) {
                red = val;
                grn = p;
                blu = q;
            }
        }
        result[0] = red;
        result[1] = grn;
        result[2] = blu;
        return result;
    }

    public static double[] rgb2hsv(double red, double grn, double blu) {
        double hue, sat, val;
        double x, f, i;
        double result[] = new double[3];

        x = Math.min(Math.min(red, grn), blu);
        val = Math.max(Math.max(red, grn), blu);
        if (x == val) {
            hue = 0;
            sat = 0;
        } else {
            f = (red == x) ? grn - blu : ((grn == x) ? blu - red : red - grn);
            i = (red == x) ? 3 : ((grn == x) ? 5 : 1);
            hue = ((i - f / (val - x)) * 60) % 360;
            sat = ((val - x) / val);
        }
        result[0] = hue;
        result[1] = sat;
        result[2] = val;
        return result;
    }

    public static int toPixel(int r, int g, int b) {
        return toPixel(r,g,b,255);
    }
     public static int fastToPixel(int r, int g, int b) {
        return fastToPixel(r,g,b,255);
    }
}
