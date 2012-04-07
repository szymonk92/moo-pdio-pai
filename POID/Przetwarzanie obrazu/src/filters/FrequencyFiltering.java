/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import com.lowagie.text.Image;
import java.awt.*;
import java.awt.image.*;
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
            g2d.drawImage(img, null, 0, 0);
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
        mag.setVisible(false);
        phase.setVisible(false);
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
        System.out.println("ColorModel:"+(raster.getNumBands() ==3?"RGB":"Grayscale"));
        
        
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                int[] RGB = RGBHelper.toRGBA(image.getRGB(i, j));
                if (raster.getNumBands() == 3) {
                    input[i][j] = new Complex(0.299 * RGB[0] + 0.587 * RGB[1] + 0.114 * RGB[2], 0);
                } else {
                    input[i][j] = new Complex((double)raster.getSample(i, j, 0), 0.0);
                }
            }
        }

        System.out.println("IFreqFilter DIMENSION"+image.getWidth()+" "+image.getHeight());
        
           for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                input[i][j].re*=( (i+j)%2 == 0 ? -1 : 1 );
                input[i][j].im*=( (i+j)%2 == 0 ? -1 : 1 );
            }
           }
        Complex[][] transformedImage = FFTTools.fft2(input);

        minMag = Complex.minMagnitude(transformedImage);
        maxMag = Complex.maxMagnitude(transformedImage);

        //revert quarters to show image in proper way
//        FFTTools.revertQuarters(transformedImage);

        BufferedImage magImg, phaseImg;
        int[][] magnImgData, phaseImgData;


        if (additionalImages[1]) {
            magImg = new BufferedImage(image.getWidth(), image.getHeight(),  BufferedImage.TYPE_BYTE_GRAY);
            magnImgData = FFTTools.magnitudeImage(transformedImage);
            WritableRaster ras = magImg.getRaster();
            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    ras.setSample(i, j, 0, RGBHelper.calmp(magnImgData[i][j]));
                }
            }
            mag.setImage(magImg);
            mag.setPreferredSize(new Dimension(magImg.getWidth(),magImg.getHeight()));
            mag.pack();
            mag.setLocationRelativeTo(null);
            mag.setVisible(true);
        }
        if (additionalImages[0]) {
            phaseImg = new BufferedImage(image.getWidth(), image.getHeight(),  BufferedImage.TYPE_BYTE_GRAY);
            phaseImgData = FFTTools.phaseImage(transformedImage);
            WritableRaster ras = phaseImg.getRaster();

            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    ras.setSample(i, j, 0, RGBHelper.calmp(phaseImgData[i][j]));
                }
            }

            phase.setImage(phaseImg);
            phase.setPreferredSize(new Dimension(phaseImg.getWidth(),phaseImg.getHeight()));
            phase.pack();
            phase.setLocationRelativeTo(null);
            phase.setVisible(true);
        }

        System.out.println("Filter no." + filterNo);
        //apply filter
        switch (filterNo) {
            case 0:
                transformedImage = FFTTools.low_passFilter(transformedImage, params[0],params[1]);
                break;
            case 1:
                transformedImage = FFTTools.high_passFilter(transformedImage, params[0],params[1]);
                break;
            case 2:
                transformedImage = FFTTools.band_passFilter(transformedImage, params[0], params[1], params[2]);
                break;
            case 3:
                transformedImage = FFTTools.band_stopFilter(transformedImage, params[0], params[1],params[2]);
                break;
            case 4:
                break;
            default:
                break;
        }   
                //REVERT QUARTER SWAP
//        FFTTools.revertQuarters(transformedImage);
        System.out.println("IFreqFilter REV, print");

        input = FFTTools.ifft2(transformedImage);
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                input[i][j].re*=( (i+j)%2 == 0 ? -1 : 1 );
                input[i][j].im*=( (i+j)%2 == 0 ? -1 : 1 );
            }
           }

        int[] outc = new int[4];
        final double aa = 0.299, bb = 0.587, cc = 0.114;
        WritableRaster outraster = out.getRaster();
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                if (raster.getNumBands() == 3) {
                    int[] RGB = RGBHelper.toRGBA(image.getRGB(i, j));
                    float r = RGB[0], g = RGB[1], b = RGB[2];
                    float ill = input[i][j].re(); //from transformed data
                    outc[0] = (int) (ill / (float) (aa + (bb * g) / r + (cc * b) / r));
                    outc[1] = (int) (ill / (float) (bb + (aa * r) / g + (cc * b) / g));
                    outc[2] = (int) (ill / (float) (cc + (aa * r) / b + (bb * g) / b));
                    out.setRGB(i, j, RGBHelper.toPixel(RGBHelper.calmp(outc[0]),
                            RGBHelper.calmp(outc[1]), outc[2]));
                } else {
                    //raster
                    outraster.setSample(i, j, 0,RGBHelper.calmp(((int)(input[i][j].re()))));
                }
            }
        }

        
        
        return out;

    }
}
