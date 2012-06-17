/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz
 */
public class DTW {

    protected double[][] unknown;
    protected int K;
    protected int unknowLenght;

    public void compute(DTWMatch templete) {

        double[][] templeteMcff = templete.getWord().getMcff();
        int templeteLenght = templeteMcff.length;
        K = 1;
        int[][] warpingPath = new int[unknowLenght + templeteLenght][2];	// max(unknowLenght, m) <= K < unknowLenght + m
        templete.setWarpingDistance(0.0);
        double accumulatedDistance;

        double[][] d = new double[unknowLenght][templeteLenght];	// local distances
        double[][] D = new double[unknowLenght][templeteLenght];	// global distances

        for (int i = 0; i < unknowLenght; i++) {
            for (int j = 0; j < templeteLenght; j++) {
                d[i][j] = distanceBetween(unknown[i], templeteMcff[j]);
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
        int minIndex;

        warpingPath[K - 1][0] = i;
        warpingPath[K - 1][1] = j;

        while ((i + j) != 0) {
            if (i == 0) {
                j -= 1;
            } else if (j == 0) {
                i -= 1;
            } else {	// i != 0 && j != 0
                double[] array = {D[i - 1][j], D[i][j - 1], D[i - 1][j - 1]};
                minIndex = this.getIndexOfMinimum(array);

                if (minIndex == 0) {
                    i -= 1;
                } else if (minIndex == 1) {
                    j -= 1;
                } else if (minIndex == 2) {
                    i -= 1;
                    j -= 1;
                }
            } // end else
            K++;
            warpingPath[K - 1][0] = i;
            warpingPath[K - 1][1] = j;
        } // end while
        templete.setWarpingDistance(accumulatedDistance / K);
        templete.setWarpingPath(this.reversePath(warpingPath));
        templete.setGlobalConstraints(checkGlobalConstraints(templete.getWarpingPath(), templeteLenght,unknowLenght));
        templete.setDistanceTabel(D);
    }

    private boolean checkGlobalConstraints(int[][] path, int I, int J) {
        for (int[] point : path) {
            if (point[0] == 0 && point[1] == 0) {
                return false;
            }
            if (point[0] < 2 * (point[1] - I) + J) {
               return false;
            }
            if (point[0] < 0.5 * (point[1] - 1) + 1) {
                return false;
            }
            if (point[0] > 2 * (point[1] - 1) + 1) {
                return false;
            }
            if (point[0] > 0.5 * (point[1] - I) + J) {
                 return false;
            }
        }
        return true;
    }

    protected int[][] reversePath(int[][] path) {
        int[][] newPath = new int[K][2];
        for (int i = 0; i < K; i++) {
            System.arraycopy(path[K - i - 1], 0, newPath[i], 0, 2);
        }
        return newPath;
    }

    protected double distanceBetween(double[] p1, double[] p2) {
        double distance = 0;
        for (int i = 0; i < p1.length; i++) {
            distance += (p1[i] - p2[i]) * (p1[i] - p2[i]);
        }
        return distance;
    }

    protected int getIndexOfMinimum(double[] array) {
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

    void setUnknown(double[][] unknown) {
        this.unknown = unknown;
        unknowLenght = unknown.length;
    }
}
