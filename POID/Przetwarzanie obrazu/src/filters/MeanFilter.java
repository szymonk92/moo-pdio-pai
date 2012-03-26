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
public class MeanFilter extends AbstractFilter {

    /**
     * dimension of sibilings
     */
    int value = 3;

    public MeanFilter() {
        this(3);
    }

    public MeanFilter(MeanFilter filter) {
        super(filter);
        this.value = filter.getValue();
    }

    public MeanFilter(int value) {
        this.value = value;
        this.setName("Mean Filter");
        this.setEditable(true);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.changeSupport.firePropertyChange("value", null, this.value);
    }

    @Override
    public JPanel getEditPanel() {
        return new MeanFilterPanel(this);
    }

    @Override
    public IFilter getCopy() {
        return new MeanFilter(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {

        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int RGBA;
        int r, g, b;
        int[][] tmp = new int[image.getWidth()][3];
        int len = value / 2;
        int sum = ((len * 2) + 1) * ((len * 2) + 1);

        for (int x = 0; x < image.getHeight(); ++x) {
            for (int y = 0; y < image.getWidth(); ++y) {
                r = g = b = 0;
                int maxj = x + len >= image.getHeight() ? image.getHeight() - 1 : x + len,
                        maxi = y + len >= image.getWidth() ? image.getWidth() - 1 : y + len;
                for (int i = y - len < 0 ? 0 : y - len; i <= maxi; ++i) {

                    if (y == 0 || i == maxi) {
                        tmp[i][0] = tmp[i][1] = tmp[i][2] = 0;
                        for (int j = x - len < 0 ? 0 : x - len; j <= maxj; ++j) {
                            RGBA = image.getRGB(i, j);
                            tmp[i][0] += RGBHelper.getRed(RGBA);
                            tmp[i][1] += RGBHelper.getGreen(RGBA);
                            tmp[i][2] += RGBHelper.getBlue(RGBA);
                        }
                    }
                    r += (i < 0 || i >= image.getWidth()) ? 0 : tmp[i][0];
                    g += (i < 0 || i >= image.getWidth()) ? 0 : tmp[i][1];
                    b += (i < 0 || i >= image.getWidth()) ? 0 : tmp[i][2];

                }
                r /= sum;
                g /= sum;
                b /= sum;
                out.setRGB(y, x, RGBHelper.toPixel(r, g, b));
            }
        }
        return out;
    }
}