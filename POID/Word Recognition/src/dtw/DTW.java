/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dtw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class DTW {

    private double[][] unknown;
    private int K;
    private int unknowLenght;
    private double thresholdLevel = 600;
    private IGlobalConstraints globalConstraints;

    public void compute(DTWMatch templete) {

        double[][] templeteMcff = templete.getData().getMfcc();
        int templeteLenght = templeteMcff.length;
        K = 1;
        List<DTWPoint> warpingPath = new ArrayList<DTWPoint>(unknowLenght + templeteLenght);
        templete.setWarpingDistance(0.0);
        double accumulatedDistance;

        double[][] d = new double[unknowLenght][templeteLenght];
        double[][] D = new double[unknowLenght][templeteLenght];
        boolean[][] gc = new boolean[unknowLenght][templeteLenght];

        for (int i = 0; i < unknowLenght; i++) {
            for (int j = 0; j < templeteLenght; j++) {
                d[i][j] = distanceBetween(unknown[i], templeteMcff[j]);
                if (globalConstraints != null) {
                    if (globalConstraints.check(i, j, templeteLenght, unknowLenght)) {
                        gc[i][j] = true;
                    }
                } else {
                    gc[i][j] = true;
                }

            }

        }

        D[0][0] = d[0][0];

        for (int i = 1; i < unknowLenght; i++) {
            D[i][0] = d[i][0] + D[i - 1][0];
        }

        for (int j = 1; j < templeteLenght; j++) {
            D[0][j] = d[0][j] + D[0][j - 1];
        }

        for (int i = 1; i < unknowLenght; i++) {
            for (int j = 1; j < templeteLenght; j++) {
                accumulatedDistance = Math.min(Math.min(D[i - 1][j], D[i - 1][j - 1]), D[i][j - 1]);
                accumulatedDistance += d[i][j];
                D[i][j] = accumulatedDistance;
            }
        }

        accumulatedDistance = D[unknowLenght - 1][templeteLenght - 1];

        int i = unknowLenght - 1;
        int j = templeteLenght - 1;
        int index;
        warpingPath.add(new DTWPoint(i, j));
        while ((i + j) != 0) {
            if (i == 0) {
                j -= 1;
            } else if (j == 0) {
                i -= 1;
            } else {
                double[] array = new double[3];
                array[0] = gc[i - 1][j] ? D[i - 1][j] : Double.MAX_VALUE;
                array[1] = gc[i][j - 1] ? D[i][j - 1] : Double.MAX_VALUE;
                array[2] = gc[i - 1][j - 1] ? D[i - 1][j - 1] : Double.MAX_VALUE;
                index = this.getIndexOfMinimum(array);
                switch (index) {
                    case 0:
                        i -= 1;
                        break;
                    case 1:
                        j -= 1;
                        break;
                    case 2:
                        i -= 1;
                        j -= 1;
                        break;
                }
                if (array[index] == Double.MAX_VALUE) {
                    accumulatedDistance = Double.MAX_VALUE;
                    break;
                }
            }
            K++;
            warpingPath.add(new DTWPoint(i, j));
        }
        if (accumulatedDistance != Double.MAX_VALUE) {
            templete.setWarpingDistance(accumulatedDistance / K);
            Collections.reverse(warpingPath);
            templete.setWarpingPath(warpingPath);
            if (thresholdLevel != 0 && templete.getWarpingDistance() > thresholdLevel) {
                templete.setThresholdLevel(false);
            } else {
                templete.setThresholdLevel(true);
            }
        } else {
            templete.setWarpingDistance(accumulatedDistance);
        }
        templete.setGlobalConstraints(gc);
        templete.setDistanceTabel(D);
    }

    private int getIndexOfMinimum(double[] array) {
        int index = 0;
        double val = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] < val) {
                val = array[i];
                index = i;
            }
        }
        return index;
    }

    private double distanceBetween(double[] p1, double[] p2) {
        double distance = 0;
        for (int i = 0; i < p1.length; i++) {
            distance += (p1[i] - p2[i]) * (p1[i] - p2[i]);
        }
        return distance;
    }

    public void setUnknown(double[][] unknown) {
        this.unknown = unknown;
        unknowLenght = unknown.length;
    }

    public IGlobalConstraints getGlobalConstraints() {
        return globalConstraints;
    }

    public void setGlobalConstraints(IGlobalConstraints globalConstraints) {
        this.globalConstraints = globalConstraints;
    }

    public double getThresholdLevel() {
        return thresholdLevel;
    }

    public void setThresholdLevel(double thresholdLevel) {
        this.thresholdLevel = thresholdLevel;
    }
}
