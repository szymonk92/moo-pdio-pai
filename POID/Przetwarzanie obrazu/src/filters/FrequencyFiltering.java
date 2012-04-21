/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import com.lowagie.text.Image;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
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
    Point2D p1, p2;

    public class ImageFrame extends JFrame {

        BufferedImage img;

        private ImageFrame(String image) {
            super(image);
            
            this.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if ( e.getButton()  == 3) {
                    JFileChooser f= new JFileChooser();
                    f.showSaveDialog(null);
                    File file = f.getSelectedFile();
                    if ( file != null) 
                    try {
                        ImageIO.write(img,"png",file);
                    } catch (IOException ex) {
                        Logger.getLogger(FrequencyFiltering.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                    
                }

                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });
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

    public void setP1(Point2D p1) {
        this.p1 = p1;
    }

    public void setP2(Point2D p2) {
        this.p2 = p2;
    }

    public FrequencyFiltering() {
        super();
        name = "Frequency Filters";
        setEditable(true);
        filterNo = 6;
        params = new float[4];
        additionalImages = new boolean[2];


        mag = new ImageFrame("Magnitude image");
        mag.getContentPane().setLayout(new FlowLayout());
        mag.setMinimumSize(new Dimension(64, 64));
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
 

        int w = 0, h = 0;
        for (int i = 0; FFTTools.pow_2[i] < image.getWidth(); ++i) {
            ++w;
        }
        w = (int) Math.pow(2, w);
        for (int i = 0; FFTTools.pow_2[i] < image.getHeight(); ++i) {
            ++h;
        }
        h = (int) Math.pow(2, h);

        Complex[][][] input = new Complex[3][w][h];
        System.out.println("ColorModel:" + (raster.getNumBands() == 3 ? "RGB" : "Grayscale"));
        int test = 0;

        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                if (i < image.getWidth() && j < image.getHeight()) {
                    int[] RGB = RGBHelper.toRGBA(image.getRGB(i, j));
                    if (raster.getNumBands() == 3) {
                        if (filterNo < 5) {
                            input[0][i][j] = new Complex(0.299 * RGB[0] + 0.587 * RGB[1] + 0.114 * RGB[2], 0);
                        } else {
                            input[0][i][j] = new Complex((double) RGB[0], 0.0);
                            input[1][i][j] = new Complex((double) RGB[1], 0);
                            input[2][i][j] = new Complex((double) RGB[2], 0);
                        }

                    } else {
                        input[0][i][j] = new Complex((double) raster.getSample(i, j, 0), 0.0);
                    }
                } else {
                    input[0][i][j] = new Complex(0.0, 0.0);
                    if (filterNo > 4) {
                        input[1][i][j] = new Complex(0.0, 0.0);
                        input[2][i][j] = new Complex(0.0, 0.0);
                    }
                }
            }
        }

        System.out.println("IFreqFilter DIMENSION" + image.getWidth() + " " + image.getHeight());
        if (filterNo < 5|| filterNo==6) {
            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    input[0][i][j].re *= ((i + j) % 2 == 0 ? -1 : 1);
                }
            }
        }
        
        
        Complex[][][] transformedImage = new Complex[3][][];
        transformedImage[0] = FFTTools.fft2(input[0]);
        if (filterNo > 4 && raster.getNumBands() == 3  ) {
            transformedImage[1] = FFTTools.fft2(input[1]);
            transformedImage[2] = FFTTools.fft2(input[2]);
        }
        
        
        Complex[][] adImg=null;
        if (filterNo == 5) {
            adImg = new Complex[w][h];
                for (int i = 0; i < w; ++i) {
                    for (int j = 0; j < h; ++j) {
                        if (i < image.getWidth() && j < image.getHeight()) {
                            int[] RGB = RGBHelper.toRGBA(image.getRGB(i, j));
                            adImg[i][j] = new Complex(0.299 * RGB[0] + 0.587 * RGB[1] + 0.114 * RGB[2], 0);
                        } else {
                            adImg[i][j] = new Complex(0.0,0.0);
                        }
                    }
                }
                adImg = FFTTools.fft2(adImg);
        }
        

        //revert quarters to show image in proper way
//        FFTTools.revertQuarters(transformedImage);


        System.out.println("Filter no." + filterNo);

        //apply filter
        switch (filterNo) {
            case 0:
                transformedImage[0] = FFTTools.low_passFilter(transformedImage[0], params[0], params[1]);
                adImg=transformedImage[0];
                break;
            case 1:
                transformedImage[0] = FFTTools.high_passFilter(transformedImage[0], params[0], params[1]);
                adImg=transformedImage[0];
                break;
            case 2:
                transformedImage[0] = FFTTools.band_passFilter(transformedImage[0], params[0], params[1], params[2]);
                adImg=transformedImage[0];
                break;
            case 3:
                transformedImage[0] = FFTTools.band_stopFilter(transformedImage[0], params[0], params[1], params[2]);
                adImg=transformedImage[0];
                break;
            case 4:
//                System.out.println("bl:" + params[0] + " bh:" + params[1] + " ang1:" + params[2] + " ang2:" + params[3]
//                        + "p1(" + p1.getX() + "," + p1.getY() + ") p2(" + p2.getX() + "," + p2.getY() + ")");
                transformedImage[0] = FFTTools.edgeDetecionFilter(transformedImage[0], params[0], params[1], params[2], params[2], p1, p2);
                adImg=transformedImage[0];
                break;
            case 5:
                transformedImage[0] = FFTTools.spectreMod(transformedImage[0], params[0], params[1]);
                if ( raster.getNumBands() == 3 ) {
                    transformedImage[1] = FFTTools.spectreMod(transformedImage[1], params[0], params[1]);
                    transformedImage[2] = FFTTools.spectreMod(transformedImage[2], params[0], params[1]);
                }
                if (additionalImages[0] || additionalImages[1])
                    adImg=FFTTools.spectreMod(adImg, params[0], params[1]);
                break;

            default:
                adImg=transformedImage[0];
                break;
        }


        BufferedImage magImg, phaseImg;
        int[][] magnImgData, phaseImgData;
     


        if (additionalImages[1]) {
            magImg = new BufferedImage(input[0].length, input[0][0].length, BufferedImage.TYPE_BYTE_GRAY);
            magnImgData = FFTTools.magnitudeImage(adImg);
            WritableRaster ras = magImg.getRaster();
            for (int i = 0; i < magImg.getWidth(); ++i) {
                for (int j = 0; j < magImg.getHeight(); ++j) {
                    ras.setSample(i, j, 0, RGBHelper.calmp(magnImgData[i][j]));
                }
            }
            mag.setImage(magImg);
            mag.setPreferredSize(new Dimension(magImg.getWidth(), magImg.getHeight()));
            mag.pack();
            mag.setLocationRelativeTo(null);
            mag.setVisible(true);
        }
        if (additionalImages[0]) {
            phaseImg = new BufferedImage(input[0].length, input[0][0].length, BufferedImage.TYPE_BYTE_GRAY);
            phaseImgData = FFTTools.phaseImage(adImg);
            WritableRaster ras = phaseImg.getRaster();

            for (int i = 0; i < phaseImg.getWidth(); ++i) {
                for (int j = 0; j < phaseImg.getHeight(); ++j) {
                    ras.setSample(i, j, 0, RGBHelper.calmp(phaseImgData[i][j]));
                }
            }

            phase.setImage(phaseImg);
            phase.setPreferredSize(new Dimension(phaseImg.getWidth(), phaseImg.getHeight()));
            phase.pack();
            phase.setLocationRelativeTo(null);
            phase.setVisible(true);
        }


        //REVERT QUARTER SWAP
//        FFTTools.revertQuarters(transformedImage);
//        System.out.println("IFreqFilter REV, print");

        input[0] = FFTTools.ifft2(transformedImage[0]);
        if (filterNo > 4 && raster.getNumBands() == 3 ) {
            input[1] = FFTTools.ifft2(transformedImage[1]);
            input[2] = FFTTools.ifft2(transformedImage[2]);
        }

        if (filterNo < 5 || filterNo==6) {
            for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    input[0][i][j].re *= ((i + j) % 2 == 0 ? -1 : 1);
                }
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
                        outc[0]=(int) r;outc[1]=(int) g; outc[2]=(int) b;
                    if (filterNo < 4) {
                        float oldill=(float) (0.299f * RGB[0] + 0.587f * RGB[1] + 0.114f * RGB[2]);
                        float ill = input[0][i][j].re(); //from transformed data
                        outc[0] = (int) ((ill / oldill) * r);
                        outc[1] = (int) ((ill / oldill) * g);
                        outc[2] = (int) ((ill / oldill) * b);
//                        outc[0] = (int) (ill / (float) (aa + (bb * g) / r + (cc * b) / r));
//                        outc[1] = (int) (ill / (float) (bb + (aa * r) / g + (cc * b) / g));
//                        outc[2] = (int) (ill / (float) (cc + (aa * r) / b + (bb * g) / b));
                    } else if (filterNo==5) {
                        outc[0] = (int) input[0][i][j].re();
                        outc[1] = (int) input[1][i][j].re();
                        outc[2] = (int) input[2][i][j].re();
                    } else if ( filterNo == 4) {
                        outc[0] = (int) input[0][i][j].re();
                        outc[1] = (int) input[0][i][j].re();
                        outc[2] = (int) input[0][i][j].re();
                    }

                    out.setRGB(i, j, RGBHelper.toPixel(RGBHelper.calmp(outc[0]),
                            RGBHelper.calmp(outc[1]), RGBHelper.calmp(outc[2])));
                } else {
                    //raster
                    outraster.setSample(i, j, 0, RGBHelper.calmp(((int) (input[0][i][j].re()))));
                }
            }
        }


        return out;

    }
}
