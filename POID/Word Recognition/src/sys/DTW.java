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

    protected double[][] seq1;
    protected double[][] seq2;
    protected int[][] warpingPath;
    protected int n;
    protected int m;
    protected int K;
    protected double warpingDistance;

    /**
     * Constructor
     *
     * @param query
     * @param templete
     */
    public DTW() {
    }

    public double compute(double[][] sample, double[][] templete) {
        seq1 = sample;
        seq2 = templete;

        n = seq1.length;
        m = seq2.length;
        K = 1;

        warpingPath = new int[n + m][2];	// max(n, m) <= K < n + m
        warpingDistance = 0.0;
        double accumulatedDistance;

        double[][] d = new double[n][m];	// local distances
        double[][] D = new double[n][m];	// global distances

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                d[i][j] = distanceBetween(seq1[i], seq2[j]);
            }
        }

        D[0][0] = d[0][0];

        for (int i = 1; i < n; i++) {
            D[i][0] = d[i][0] + D[i - 1][0];
        }

        for (int j = 1; j < m; j++) {
            D[0][j] = d[0][j] + D[0][j - 1];
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                accumulatedDistance = Math.min(Math.min(D[i - 1][j], D[i - 1][j - 1]), D[i][j - 1]);
                accumulatedDistance += d[i][j];
                D[i][j] = accumulatedDistance;
            }
        }
        accumulatedDistance = D[n - 1][m - 1];

        int i = n - 1;
        int j = m - 1;
        int minIndex = 1;

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
        warpingDistance = accumulatedDistance / K;
        this.reversePath(warpingPath);
        return warpingDistance;
    }

    /**
     * Changes the order of the warping path (increasing order)
     *
     * @param path	the warping path in reverse order
     */
    protected void reversePath(int[][] path) {
        int[][] newPath = new int[K][2];
        for (int i = 0; i < K; i++) {
            System.arraycopy(path[K - i - 1], 0, newPath[i], 0, 2);
        }
        warpingPath = newPath;
    }

    /**
     * Returns the warping distance
     *
     * @return
     */
    public double getDistance() {
        return warpingDistance;
    }

    /**
     * Computes a distance between two points
     *
     * @param p1	the point 1
     * @param p2	the point 2
     * @return	the distance between two points
     */
    protected double distanceBetween(double[] p1, double[] p2) {
        double distance = 0;
        for (int i = 0; i < p1.length; i++) {
            distance += (p1[i] - p2[i]) * (p1[i] - p2[i]);
        }
        return distance;
    }

    /**
     * Finds the index of the minimum element from the given array
     *
     * @param array	the array containing numeric values
     * @return	the min value among elements
     */
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
}
