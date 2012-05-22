/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.util.Comparator;

/**
 *
 * @author Lukasz
 */
public class MaxDataComparator implements Comparator<Integer> {

    public double[] d;

    public MaxDataComparator(double[] d) {
        this.d = d;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        return (d[o1] > d[o2]) ? 1 : -1;
    }
}
