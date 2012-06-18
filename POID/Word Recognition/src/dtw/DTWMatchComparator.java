/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dtw;

import java.util.Comparator;

/**
 *
 * @author Lukasz
 */
public class DTWMatchComparator implements Comparator<DTWMatch> {

    @Override
    public int compare(DTWMatch o1, DTWMatch o2) {
        return Double.compare(o1.getWarpingDistance(), o2.getWarpingDistance());
    }
}
