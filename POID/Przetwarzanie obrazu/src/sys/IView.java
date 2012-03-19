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
public interface IView {
    
   public JPanel getView();
   public String getName();
   public String getIcon();
   public void setTabData(TabData data);
   public boolean canByMultiple();
   public boolean addAtTop();
    
}
