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
        if (distanceTabel == null) {
            return;
        }
        BufferedImage result = new BufferedImage(distanceTabel[0].length, distanceTabel.length, BufferedImage.TYPE_4BYTE_ABGR);
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < distanceTabel.length; i++) {
            for (int j = 0; j < distanceTabel[0].length; j++) {
                if (max < distanceTabel[i][j]) {
                    max = distanceTabel[i][j];
                }
                if (min > distanceTabel[i][j]) {
                    min = distanceTabel[i][j];
                }
            }
        }
        int jtmp = distanceTabel.length - 1;
        double down = max - min;
        Color color;

        for (int i = 0; i < distanceTabel[0].length; i++) {
            for (int j = 0; j < distanceTabel.length; j++) {
                int newj = jtmp - j;
                if (drawGlobalContstraints && dtw.getGlobalConstraints() != null && dtw.getGlobalConstraints().check(newj + 1, i + 1, distanceTabel[0].length, distanceTabel.length)) {
                    color = Color.GREEN;
                } else {
                    int value = 255 - (int) (((distanceTabel[newj][i] - min) / down) * 255);
                    color = new Color(value, value, value);
                }
                result.setRGB(i, j, color.getRGB());
            }
        }
        for (DTWPoint point : match.getWarpingPath()) {

            result.setRGB(point.getJ(), jtmp - point.getI(), Color.RED.getRGB());
        }
        match.setImage(result);
    }
}
