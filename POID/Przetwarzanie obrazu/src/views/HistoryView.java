/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javax.swing.JPanel;
import sys.AbstractView;

/**
 *
 * @author Lukasz
 */
public class HistoryView extends AbstractView{

    @Override
    public JPanel getView() {
       return new HistoryPanel(this.data);
    }

    @Override
    public String getName() {
        return "Historia";
    }
    
    @Override
    public boolean addAtTop(){
        return true;
    }
    
}
