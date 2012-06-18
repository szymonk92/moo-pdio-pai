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
        if (i < 2 * (j - I) + J) {
            return false;
        }
        if (i < 0.5 * (j - 1) + 1) {
            return false;
        }
        if (i > 2 * (j - 1) + 1) {
            return false;
        }
        if (i > 0.5 * (j - I) + J) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Itakura parallelogram";
    }
    
}
