/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.awt.image.BufferedImage;

/**
 *
 * @author Lukasz
 */
public class DiffCalculator {

    public static int MSE = 1;
    public static int PSNR = 2;
    public static int MAE = 3;

    public static double calculateError(BufferedImage in, BufferedImage out, int type) {
        if (in == null || out == null) {
            return -1;
        }
        double ratio = 0;
        if (in != null && out != null
                && in.getHeight() == out.getHeight() && in.getWidth() == out.getWidth()) {
            double sr = 0, sg = 0, sb = 0;
            if (type < 3) {
                for (int i = 0; i < in.getWidth(); ++i) {
                    for (int j = 0; j < in.getHeight(); ++j) {
                        int ini = in.getRGB(i, j),
                                outi = out.getRGB(i, j);
                        sr += (RGBHelper.getRed(ini) - RGBHelper.getRed(outi))
                                * (RGBHelper.getRed(ini) - RGBHelper.getRed(outi));
                        sg += (RGBHelper.getGreen(ini) - RGBHelper.getGreen(outi))
                                * (RGBHelper.getGreen(ini) - RGBHelper.getGreen(outi));
                        sb += (RGBHelper.getBlue(ini) - RGBHelper.getBlue(outi))
                                * (RGBHelper.getBlue(ini) - RGBHelper.getBlue(outi));
                    }
                }
                ratio = sr + sb + sg;
                //MSE
                ratio /= (double) (in.getHeight() * in.getWidth()*3);
                
                if (type == 1) {
                    return ratio;
                }
                //ratio== 0 dla PSNR oznacza dzielenie przez 0 dlatego zwracamy Double.POSITIVE_INFINITY
                if(ratio == 0){
                    return Double.POSITIVE_INFINITY;
                }
                //PSNR
                ratio = 10.0f * Math.log10((255.0f * 255.0f ) / ratio);
                return ratio;
            } else {
                for (int i = 0; i < in.getWidth(); ++i) {
                    for (int j = 0; j < in.getHeight(); ++j) {
                        int ini = in.getRGB(i, j),
                                outi = out.getRGB(i, j);
                        sr += Math.abs(RGBHelper.getRed(ini) - RGBHelper.getRed(outi));
                        sg += Math.abs(RGBHelper.getGreen(ini) - RGBHelper.getGreen(outi));
                        sb += Math.abs(RGBHelper.getBlue(ini) - RGBHelper.getBlue(outi));
                    }
                }
                return (double) (sr + sb + sg) / (double) (in.getHeight() * in.getWidth());
            }
        }
        return ratio;
    }
}
