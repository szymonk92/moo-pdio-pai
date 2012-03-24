/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import javax.swing.JPanel;

/**
 *
 * @author Lukasz
 */
public interface IView extends IPlugin{
    
   public JPanel getView(TabData data);
   public boolean canByMultiple();
   public boolean addAtTop();
    
}
