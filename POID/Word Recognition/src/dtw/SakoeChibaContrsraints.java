/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dtw;

/**
 *
 * @author Lukasz
 */
public class SakoeChibaContrsraints implements IGlobalConstraints {

    private int r;

    public SakoeChibaContrsraints(int r) {
        this.r = r;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    @Override
    public boolean check(int i, int j, int I, int J) {
        return Math.abs((i * ((double) I / (double) J)) - j) <= r;
    }
     @Override
    public String toString() {
        return "Sakoe and Chiba";
    }
}
