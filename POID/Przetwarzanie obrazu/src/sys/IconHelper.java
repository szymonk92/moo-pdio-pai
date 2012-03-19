/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import javax.swing.ImageIcon;

/**
 *
 * @author Lukasz
 */
public class IconHelper {
    
    private static ImageIcon defaultIcon = new ImageIcon("build/classes/images/filter_default.png");
    
    public static ImageIcon getDefaultIcon(){
        return defaultIcon;
    }
    
    
}
