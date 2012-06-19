/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dtw;

/**
 *
 * @author Lukasz
 */
public class ItakuraConstraints implements IGlobalConstraints {

    @Override
    public boolean check(int i, int j, int I, int J) {
        if (j > 2 * i) {
            return false;
        }
        if (j > i / 2 + I - J / 2) {
            return false;
        }
        if (j < 2 * i + I - 2 * J) {
            return false;
        }
        if (j < i / 2) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Itakura parallelogram";
    }
}
