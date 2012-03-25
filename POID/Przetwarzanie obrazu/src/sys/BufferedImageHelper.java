/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class BufferedImageHelper {

    private static List<ImageType> ImageTypes;

    public static List<ImageType> getImageTypes() {
        if(ImageTypes == null){
            ImageTypes = new ArrayList<ImageType>();
        }
        if(ImageTypes.isEmpty()){
            FillImageTypesList();
        }
        return ImageTypes;
    }

    private static void FillImageTypesList() {
        ImageTypes.add(new ImageType("CUSTOM", "Image type is not recognized so it must be a customized image.", 0));
        ImageTypes.add(new ImageType("INT_RGB", "Represents an image with 8-bit RGB color components packed into integer pixels. The image has a DirectColorModel without alpha.", 1));
        ImageTypes.add(new ImageType("INT_ARGB", "Represents an image with 8-bit RGBA color components packed into integer pixels. The image has a DirectColorModel with alpha.", 2));
        ImageTypes.add(new ImageType("INT_ARGB_PRE", "Represents an image with 8-bit RGBA color components packed into integer pixels. The image has a DirectColorModel without alpha.", 3));
        ImageTypes.add(new ImageType("INT_BGR", "Represents an image with 8-bit RGB color components, corresponding to a Windows- or Solaris- style BGR color model, with the colors Blue, Green, and Red packed into integer pixels.", 4));
        ImageTypes.add(new ImageType("3BYTE_BGR", "Represents an image with 8-bit RGB color components, corresponding to a Windows-style BGR color model) with the colors Blue, Green, and Red stored in 3 bytes.", 5));
        ImageTypes.add(new ImageType("4BYTE_ABGR", "Represents an image with 8-bit RGBA color components with the colors Blue, Green, and Red stored in 3 bytes and 1 byte of alpha.", 6));
        ImageTypes.add(new ImageType("4BYTE_ABGR_PRE", "Represents an image with 8-bit RGBA color components with the colors Blue, Green, and Red stored in 3 bytes and 1 byte of alpha.", 7));
        ImageTypes.add(new ImageType("USHORT_565_RGB", "Represents an image with 5-6-5 RGB color components (5-bits red, 6-bits green, 5-bits blue) with no alpha.", 8));
        ImageTypes.add(new ImageType("USHORT_555_RGB", "Represents an image with 5-5-5 RGB color components (5-bits red, 5-bits green, 5-bits blue) with no alpha.", 9));
        ImageTypes.add(new ImageType("BYTE_GRAY", "Represents a unsigned byte grayscale image, non-indexed. This image has a ComponentColorModel with a CS_GRAY ColorSpace.", 10));
        ImageTypes.add(new ImageType("USHORT_GRAY", "Represents an unsigned short grayscale image, non-indexed). This image has a ComponentColorModel with a CS_GRAY ColorSpace.", 11));
        ImageTypes.add(new ImageType("BYTE_BINARY", "Represents an opaque byte-packed 1, 2, or 4 bit image. The image has anIndexColorModel without alpha.", 12));
        ImageTypes.add(new ImageType("BYTE_INDEXED", "Represents an indexed byte image.", 13));
    }

    public static BufferedImage copy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage resize(BufferedImage originalImage, int height) {
        double scale = ((double) (height)) / originalImage.getHeight();
        int width = (int) (originalImage.getWidth() * scale);
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    static public double[] getHistogram(BufferedImage src, int channel) {
        double[] histogram = new double[256];
        for (int i = 0; i < 256; i++) {
            histogram[ i] = 0;
        }
        Raster raster = src.getRaster();
        if (raster.getNumBands() < channel) {
            return histogram;
        }
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                histogram[ raster.getSample(x, y, channel)]++;
            }
        }
        return histogram;
    }

    static public double[] getLuminanceHistogram(BufferedImage src) {

        double[] histogram = new double[256];
        for (int i = 0; i < 256; i++) {
            histogram[ i] = 0;
        }
        Raster raster = src.getRaster();
        if (raster.getNumBands() < 3) {
            return histogram;
        }
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                histogram[(raster.getSample(x, y, 0) + raster.getSample(x, y, 1) + raster.getSample(x, y, 2)) / 3]++;
            }
        }
        return histogram;
    }
}
