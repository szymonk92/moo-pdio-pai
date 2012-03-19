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
public class FilterMenuItemListener implements ActionListener {

    private JTabbedPane tabPanel;
    private final IFilter filter;
    
    public FilterMenuItemListener(JTabbedPane tabPanel, IFilter filter){
        this.tabPanel = tabPanel;
        this.filter = filter;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedTabIndex = tabPanel.getSelectedIndex();
        if(selectedTabIndex!=-1){
            MainPanel panel = (MainPanel) tabPanel.getComponentAt(selectedTabIndex);
            if(panel!=null){
                panel.data.getFilters().add(filter.getCopy());
            }
        }
    }
    
}
