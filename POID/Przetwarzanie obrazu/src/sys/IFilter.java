/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Lukasz
 */
public interface IFilter extends IPlugin{

    JPanel getEditPanel();
    
    @Override
    IFilter getCopy();
    
    boolean isEditable();

    BufferedImage processImage(BufferedImage image);

    void setDescription(String description);

    void setName(String name);
    
}
