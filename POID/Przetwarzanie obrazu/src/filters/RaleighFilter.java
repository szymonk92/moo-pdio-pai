/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import sys.AbstractFilter;
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
    int gmin,pdim;

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
        int RGBA, r, g, b,dim=image.getWidth() * image.getHeight();
        double a2a = alpha*2*alpha;
        if (alpha == 0.0f) {
            alpha = 255.0f / (float) Math.sqrt(2 * Math.log(dim));
            a2a = alpha*2*alpha;
        }
        pdim=dim;
        
        out = new int[4];
        H = new int[4][256];

        double pixsum = image.getWidth() * image.getHeight();

        //compute all histograms
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                RGBA = image.getRGB(x, y);
                H[0][RGBHelper.getRed(RGBA)]++;
                H[1][RGBHelper.getGreen(RGBA)]++;
                H[2][RGBHelper.getBlue(RGBA)]++;
            }
        }

        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                RGBA = image.getRGB(x, y);
                r = RGBHelper.getRed(RGBA);
                g = RGBHelper.getGreen(RGBA);
                b = RGBHelper.getBlue(RGBA);
                for (int ch = 0; ch < 3; ++ch) {
                    if (channel[ch]) {
                        double s = 0;
                        int f = (ch == 0) ? r : ((ch == 1) ? g : b);
                        double c = a2a;
                        for (int i = 0; i <= f; i++) {
                            s += H[ch][i];
                        }
                        c *= Math.log(pixsum / s);
                        c = Math.sqrt(c);
                        out[ch] = gmin + ((int) c);
                    } else {
                        out[ch] = (ch == 0) ? r : ((ch == 1) ? g : b);
                    }
                }

                image.setRGB(x, y, RGBHelper.toPixel(out[0], out[1], out[2]));
            }
        }
        return image;
    }
}
