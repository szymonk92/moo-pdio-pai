/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import sys.IPlugin;

/**
 *
 * @author Lukasz
 */
public interface IPixelComperer extends IPlugin{
    public void setImage(BufferedImage image);
    public boolean Comppere(int x, int y, Color areaColor);
    @Override
    IPixelComperer getCopy();
}
