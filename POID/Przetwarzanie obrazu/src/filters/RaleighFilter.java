/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import sys.AbstractFilter;
import sys.BufferedImageHelper;
import sys.IFilter;
import sys.RGBHelper;

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
        channel[0]=channel[1]=channel[2]=false;
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
    public JPanel getEditPanel() {
        return new RaleighFilterPanel(this);
    }

    @Override
    public IFilter getCopy() {
        return new RaleighFilter(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        int RGBA, dim = image.getWidth() * image.getHeight();
        double a2a = alpha * 2 * alpha,r,g,b;
        if (alpha == 0.0f) {
            alpha = 255.0f / (float) Math.sqrt(2 * Math.log(dim));
            a2a = alpha * 2 * alpha;
        }
        //System.out.println("" + alpha);
        double[] il = new double[256];
        int[] RGB;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                RGB = RGBHelper.toRGBA(image.getRGB(x, y));
                il[RGBHelper.calmp((int) (0.299 * RGB[0] + 0.587* RGB[1] + 0.114* RGB[2]))]++;
            }
        }
        double[] ilo = new double[il.length];

        for (int i = 0; i < il.length; ++i) {
            double s = 0, c = a2a;
            for (int j = 0; j <= i; ++j) {
                s += il[j];
            }
            c *= Math.log(dim / s);
            c = Math.sqrt(c);
            ilo[i] = (float) gmin + c;
        }
        out = new int[4];
        final double aa = 0.299, bb = 0.587, cc = 0.114;

        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                RGBA = image.getRGB(x, y);
                r = RGBHelper.getRed(RGBA);
                g = RGBHelper.getGreen(RGBA);
                b = RGBHelper.getBlue(RGBA);
                double ill = ilo[RGBHelper.calmp((int) ((aa * r) + (bb * g) + (cc * b)))];
                out[0] = (int) ((channel[0]==true) ? (ill / (aa + (bb * g) / r + (cc * b) / r)) : r);
                out[1] = (int) ((channel[1]==true) ? (ill / (bb + (aa * r) / g + (cc * b) / g)) : g);
                out[2] = (int) ((channel[2]==true) ? (ill / (cc + (aa * r) / b + (bb * g) / b)) : b);
                image.setRGB(x, y, RGBHelper.toPixel(out[0], out[1], out[2]));
            }
        }
        return image;
    }
}
