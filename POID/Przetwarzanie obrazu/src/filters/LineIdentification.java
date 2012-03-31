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
import sys.TabData;

/**
 *
 * @author pawel
 */
public class LineIdentification extends AbstractFilter {

    int[][][] predefined = {
        {
            {-1, 2, -1},
            {-1, 2, -1},
            {-1, 2, -1},},
        {
            {-1, -1, -1},
            {2, 2, 2},
            {-1, -1, -1}
        },
        {
            {-1, -1, 2},
            {-1, 2, -1},
            {2, -1, -1}
        },
        {
            {2, -1, -1},
            {-1, 2, -1},
            {-1, -1, 2}
        }
    };
    int[][] custom;
    int type;

    public LineIdentification() {
        this.name = "Line Identification";
        this.setEditable(true);
    }

    public LineIdentification(LineIdentification filter) {
        super(filter);
        this.custom = filter.getCustom();
    }

    public void setCustom(int[][] custom) {
        this.custom = custom;
    }

    public int[][] getCustom() {
        return custom;
    }

    public int[][][] getPredefined() {
        return predefined;
    }

    public void setType(int type) {
        this.type = type;
        this.changeSupport.firePropertyChange("type", null, this.type);
    }

    @Override
    public JPanel getEditPanel(TabData data) {
        return new LineIdentificationPanel(this);
    }

    @Override
    public IFilter getCopy() {
        return new LineIdentification(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {

        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int RGBA;
        int r, g, b;

        int[][] mask = type < 0 ? custom : predefined[type];
        int len = mask.length / 2;


        for (int x = 0; x < image.getHeight(); ++x) {
            for (int y = 0; y < image.getWidth(); ++y) {
                r = g = b = 0;
                int maxj = x + len >= image.getHeight() ? image.getHeight() - 1 : x + len,
                        maxi = y + len >= image.getWidth() ? image.getWidth() - 1 : y + len;
                for (int ii = 0, i = y - len < 0 ? 0 : y - len; i <= maxi; ++ii, ++i) {
                    for (int jj = 0, j = x - len < 0 ? 0 : x - len; j <= maxj; ++jj, ++j) {
                        RGBA = image.getRGB(i, j);
                        r += RGBHelper.getRed(RGBA) * mask[ii][jj];
                        g += RGBHelper.getGreen(RGBA) * mask[ii][jj];
                        b += RGBHelper.getBlue(RGBA) * mask[ii][jj];
                    }
                }
                out.setRGB(y, x, RGBHelper.toPixel(r, g, b));
            }
        }
        return out;
    }
}
