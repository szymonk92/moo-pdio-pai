/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import com.lowagie.text.Image;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sys.*;

/**
 *
 * @author pawel
 */
public class FrequencyFiltering extends AbstractFilter {

    int filterNo;
    float[] params;
    boolean[] additionalImages;
    float minMag, maxMag;

    public class ImageFrame extends JFrame {

        BufferedImage img;

        private ImageFrame(String image) {
            super(image);
        }

        public void setImage(BufferedImage i) {
            img = i;
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(img, null, img.getWidth(), img.getHeight());
        }
    };
    ImageFrame mag, phase;

    public FrequencyFiltering() {
        super();
        name = "Frequency Filters";
        setEditable(true);
        filterNo = 6;
        params = new float[4];
        additionalImages = new boolean[2];


        mag = new ImageFrame("Magnitude image");
        mag.getContentPane().setLayout(new FlowLayout());
        phase = new ImageFrame("Phase image");
        phase.getContentPane().setLayout(new FlowLayout());

    }

    public void refresh() {
        this.changeSupport.fireIndexedPropertyChange("generic", 0, null, this);
    }

    public float getMaxMag() {
        return maxMag;
    }

    public float getMinMag() {
        return minMag;
    }

    /**
     * @desc Set what image of transformed image show (0-magnitude, 1-phase)
     *
     * @param no
     */
    public void setAdditionalImage(int no, boolean val) {
        additionalImages[no] = val;
    }

    public void setParams(float[] params) {
        this.params = params;
    }

    /**
     * @desc set filter to use
     *
     * @param filter
     */
    public void setFilter(int filter) {
        filterNo = filter;
    }

    public FrequencyFiltering(FrequencyFiltering f) {
        this();
    }

    @Override
    public JPanel getEditPanel(TabData data) {
        return new FrequencyFilteringPanel(this);
    }

    @Override
    public IFilter getCopy() {
        return new FrequencyFiltering(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        WritableRaster raster = image.getRaster();
        Complex[][] input = new Complex[out.getWidth()][out.getHeight()];
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                int[] RGB = RGBHelper.toRGBA(image.getRGB(i, j));
//                if (raster.getNumBands() == 3) {
//                    input[i][j] = new Complex(0.299 * RGB[0] + 0.587 * RGB[1] + 0.114 * RGB[2], 0);
//                } else {
                    input[i][j] = new Complex(raster.getSample(i, j, 0), 0);
//                }
            }
        }

        System.out.println("IFreqFilter DIMENSION"+image.getWidth()+" "+image.getHeight());
        
        
        Complex[][] transformedImage = FFTTools.fft2(input);

//        minMag = Complex.minMagnitude(transformedImage);
//        maxMag = Complex.maxMagnitude(transformedImage);

        //revert quarters to show image in proper way
//        FFTTools.revertQuarters(transformedImage);

        BufferedImage magImg, phaseImg;
        int[][] magnImgData, phaseImgData;


        if (additionalImages[0]) {
            magImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            magnImgData = FFTTools.magnitudeImage(transformedImage);
//                    Complex.getAbs(transformedImage);
            WritableRaster ras = magImg.getRaster();
            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    ras.setSample(i, j, 0, RGBHelper.calmp(magnImgData[i][j]));
                }
            }
            mag.setImage(magImg);
            mag.setSize(image.getWidth(), image.getHeight());
            mag.setVisible(true);
        }
        if (additionalImages[1]) {
            phaseImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            phaseImgData = FFTTools.magnitudeImage(transformedImage);
//                    Complex.getPhase(transformedImage);
            WritableRaster ras = phaseImg.getRaster();

            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    ras.setSample(i, j, 0, RGBHelper.calmp(phaseImgData[i][j]));
                }
            }

            phase.setImage(phaseImg);
            phase.setSize(image.getWidth(), image.getHeight());
            phase.setVisible(true);
        }

        //REVERT QUARTER SWAP
//        FFTTools.revertQuarters(transformedImage);
        System.out.println("Filter no." + filterNo);
        //apply filter
        switch (filterNo) {
            case 0:
                transformedImage = FFTTools.low_passFilter(transformedImage, 0, params[0]);
                break;
            case 1:
                transformedImage = FFTTools.high_passFilter(transformedImage, 0, params[0]);
                break;
            case 2:
                transformedImage = FFTTools.band_passFilter(transformedImage, 0, params[0], params[1]);
                break;
            case 3:
                transformedImage = FFTTools.band_stopFilter(transformedImage, 0, params[0], params[1]);
                break;
            case 4:
                break;
            default:
                break;
        }

        input = FFTTools.ifft2(transformedImage);

        int[] outc = new int[4];
        final double aa = 0.299, bb = 0.587, cc = 0.114;
        WritableRaster outraster = out.getRaster();
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
//                if (raster.getNumBands() == 3) {
//                    int[] RGB = RGBHelper.toRGBA(image.getRGB(i, j));
//                    float r = RGB[0], g = RGB[1], b = RGB[2];
//                    float ill = input[i][j].re(); //from transformed data
//                    outc[0] = (int) (ill / (float) (aa + (bb * g) / r + (cc * b) / r));
//                    outc[1] = (int) (ill / (float) (bb + (aa * r) / g + (cc * b) / g));
//                    outc[2] = (int) (ill / (float) (cc + (aa * r) / b + (bb * g) / b));
//                    out.setRGB(i, j, RGBHelper.toPixel(outc[0], outc[1], outc[2]));
//                } else {
//                    //raster
                    outraster.setSample(i, j, 0, input[i][j].re());
//                }
            }
        }


        return out;

    }
}
