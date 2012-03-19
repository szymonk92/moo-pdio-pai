/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import gui.MainPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTabbedPane;

/**
 *
 * @author Lukasz
 */
public class ViewMenuItemListener implements ActionListener {

    private JTabbedPane tabPanel;
    private final IView view;
    
    public ViewMenuItemListener(JTabbedPane tabPanel, IView view){
        this.tabPanel = tabPanel;
        this.view = view;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedTabIndex = tabPanel.getSelectedIndex();
        if(selectedTabIndex!=-1){
            MainPanel panel = (MainPanel) tabPanel.getComponentAt(selectedTabIndex);
            if(panel!=null){
                if(!view.canByMultiple()){
                   for(IView viewOnList : panel.data.getViews()){
                       if(viewOnList.getClass().getName().equals(view.getClass().getName())){
                           return;
                       }
                   }
                }
                panel.data.getViews().add(view);
            }
        }
    }
    
}
