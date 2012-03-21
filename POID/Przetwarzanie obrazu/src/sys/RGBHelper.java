/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz
 */
public class RGBHelper {

    static public int calmp(int value) {
        return Math.max(Math.min(value, 255), 0);
    }
}
