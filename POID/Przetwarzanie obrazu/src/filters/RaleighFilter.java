/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.JPanel;
import sys.*;

/**
 *
 * @author pawel
 */
public class RaleighFilter extends AbstractFilter {

    float alpha;
    /**
     * 0-r; 1=g; 2=b,3=grey
     */
    boolean[] channel = new boolean[4];
    int[][] H;
    int[] out;
    int gmin, pdim;

    public void setGmin(int gmin) {
        this.gmin = gmin;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setChannel(int channel, boolean value) {
        this.channel[channel] = value;
    }

    public boolean[] getChannel() {
        return channel;
    }

    public float getAlpha() {
        return alpha;
    }

    public int getGmin() {
        return gmin;
    }

    public RaleighFilter() {
        this.name = "Raleigh Filter";
        this.setEditable(true);
        channel[0] = channel[1] = channel[2] = false;
    }

    public void refresh() {
        this.changeSupport.firePropertyChange("gmin", null, this.gmin);
    }

    public RaleighFilter(RaleighFilter filter) {
        super(filter);
        this.gmin = filter.getGmin();
        this.channel = filter.getChannel();
        this.alpha = filter.getAlpha();
        this.H = filter.H;
        this.pdim = filter.pdim;
    }

    @Override
    public JPanel getEditPanel(TabData data) {
        return new RaleighFilterPanel(this, data);
    }

    @Override
    public IFilter getCopy() {
        return new RaleighFilter(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        int dim = image.getWidth() * image.getHeight();
        double a2a, r, g, b;
        WritableRaster raster = image.getRaster();
        if (alpha == 0.0f) {
            alpha = 255.0f / (float) Math.sqrt(2 * Math.log(dim));
        }
        a2a = alpha * 2 * alpha;
        //System.out.println("" + alpha);
        double[][] histograms = new double[4][];
        double[][] histogramSum = new double[4][];
        double[][] histogramIlo = new double[4][];
        if (raster.getNumBands() == 3 && !channel[3]) {
            for (int i = 0; i < raster.getNumBands(); i++) {
                histograms[i] = BufferedImageHelper.getHistogram(image, i);
                histogramIlo[i] = new double[histograms[i].length];
                histogramSum[i] = new double[histograms[i].length];
                for (int j = 0; j < histograms[i].length; j++) {
                    if (j == 0) {
                        histogramSum[i][j] = histograms[i][j];
                    } else {
                        histogramSum[i][j] += histogramSum[i][j - 1] + histograms[i][j];
                    }
                    histogramIlo[i][j] = RGBHelper.calmp(gmin + Math.sqrt(a2a * Math.log(((double) dim) / histogramSum[i][j])));
                }
            }
        }
        histograms[3] = BufferedImageHelper.getLuminanceHistogram(image);
        histogramSum[3] = new double[histograms[3].length];
        histogramIlo[3] = new double[histograms[3].length];
        for (int j = 0; j < histograms[3].length; j++) {
            if (j == 0) {
                histogramSum[3][j] = histograms[3][j];
            } else {
                histogramSum[3][j] += histogramSum[3][j - 1] + histograms[3][j];
            }
            histogramIlo[3][j] = RGBHelper.calmp(gmin + Math.sqrt(a2a * Math.log(((double) dim) / histogramSum[3][j])));
        }

        out = new int[4];
        final double aa = 0.299, bb = 0.587, cc = 0.114;
        int RGBA;
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                if (raster.getNumBands() == 3) {
                    RGBA = image.getRGB(x, y);
                    r = RGBHelper.getRed(RGBA);
                    g = RGBHelper.getGreen(RGBA);
                    b = RGBHelper.getBlue(RGBA);
                    if (channel[3]) {
                        double ill = histogramIlo[3][RGBHelper.calmp((int) ((aa * r) + (bb * g) + (cc * b)))];
                        out[0] = (int) ((channel[0] == true) ? (ill / (aa + (bb * g) / r + (cc * b) / r)) : r);
                        out[1] = (int) ((channel[1] == true) ? (ill / (bb + (aa * r) / g + (cc * b) / g)) : g);
                        out[2] = (int) ((channel[2] == true) ? (ill / (cc + (aa * r) / b + (bb * g) / b)) : b);
                    } else {
                        out[0] = (int) (channel[0] ? histogramIlo[0][(int) (r)] : r);
                        out[1] = (int) (channel[1] ? histogramIlo[1][(int) (g)] : g);
                        out[2] = (int) (channel[2] ? histogramIlo[2][(int) (b)] : b);
                    }
                    image.setRGB(x, y, RGBHelper.toPixel(out[0], out[1], out[2]));
                }
                if (raster.getNumBands() == 1) {
                    raster.setSample(x, y, 0, histogramIlo[3][raster.getSample(x, y, 0)]);
                }
            }
        }
        return image;
    }
}
