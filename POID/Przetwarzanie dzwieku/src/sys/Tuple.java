/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Lukasz
 */
public class Tuple extends Pair<Double, Integer> {

    double freq;
    int index;

    public Tuple(double freq, int index) {
        super();
        this.freq = freq;
        this.index = index;
    }

    @Override
    public Integer setValue(Integer value) {
        return null;
    }

    @Override
    public Double getLeft() {
        return freq;
    }

    @Override
    public Integer getRight() {
        return index;
    }
}
