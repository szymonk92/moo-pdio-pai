/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import sys.AbstractFilter;
import sys.IFilter;

/**
 *
 * @author Lukasz
 */
public class BrightnessFilter extends AbstractFilter {

    private int value = 0;

    public int getValue() {
        return value;
    }
    private short[] changeTable;

    public BrightnessFilter() {
        this.setName("Brightness Filter");
        this.setEditable(true);
        generateTable();
    }

    public BrightnessFilter(BrightnessFilter filter) {
        super(filter);
        this.value = filter.getValue();
        this.changeTable = filter.changeTable.clone();
    }

    public void setValue(int value) {
        this.value = value;
        generateTable();
        this.changeSupport.firePropertyChange("Value", null, this.value);
    }

    private void generateTable() {
        changeTable = new short[256];
        for (int i = 0; i < 256; i++) {
            short v = (short) (i - value);
            if (v < 0) {
                v = 0;
            } else if (v > 255) {
                v = 255;
            }
            changeTable[i] = v;
        }
    }

    @Override
    public IFilter getCopy() {
        return new BrightnessFilter(this);
    }

    @Override
    public JPanel getEditPanel() {
        return new BrightnessFilterPanel(this);
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        if (value != 0) {
            Color col;
            int RGBA;
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    RGBA = image.getRGB(x, y);
                    col = new Color(RGBA, true);
                    image.setRGB(x, y, new Color(changeTable[col.getRed()], changeTable[col.getGreen()], changeTable[col.getBlue()]).getRGB());
                }
            }
        }
        return image;
    }
}
