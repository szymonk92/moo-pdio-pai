/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dtw;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Lukasz
 */
public class DistanceImageGenerator {

    private DTW dtw;
    private boolean drawGlobalContstraints;

    public boolean isDrawGlobalContstraints() {
        return drawGlobalContstraints;
    }

    public void setDrawGlobalContstraints(boolean drawGlobalContstraints) {
        this.drawGlobalContstraints = drawGlobalContstraints;
    }

    public DTW getDtw() {
        return dtw;
    }

    public void setDtw(DTW dtw) {
        this.dtw = dtw;
    }

    public void generate(DTWMatch match) {
        double[][] distanceTabel = match.getDistanceTabel();
        if (distanceTabel == null || match.getWarpingPath() == null) {
            return;
        }
        BufferedImage result = new BufferedImage(distanceTabel.length, distanceTabel[0].length, BufferedImage.TYPE_4BYTE_ABGR);
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < distanceTabel.length; i++) {
            for (int j = 0; j < distanceTabel[0].length; j++) {
                if (match.getGlobalConstraints()[i][j]) {
                    if (max < distanceTabel[i][j]) {
                        max = distanceTabel[i][j];
                    }
                    if (min > distanceTabel[i][j]) {
                        min = distanceTabel[i][j];
                    }
                }
            }
        }
        double down = max - min;
        for (int i = 0; i < distanceTabel.length; i++) {
            for (int j = 0; j < distanceTabel[0].length; j++) {
                if (match.getGlobalConstraints()[i][j]) {
                    int value = 255 - (int) (((distanceTabel[i][j] - min) / down) * 255);
                    result.setRGB(i, j, new Color(value, value, value).getRGB());
                }

            }
        }
        for (DTWPoint point : match.getWarpingPath()) {
            result.setRGB(point.getI(), point.getJ(), Color.RED.getRGB());
        }
        match.setImage(result);
    }
}
