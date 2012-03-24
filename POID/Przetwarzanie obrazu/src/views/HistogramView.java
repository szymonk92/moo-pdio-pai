/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.beans.PropertyChangeSupport;
import javax.swing.JPanel;
import sys.AbstractView;
import sys.IPlugin;
import sys.TabData;

/**
 *
 * @author Lukasz
 */
public class HistogramView extends AbstractView{

    @Override
    public JPanel getView(TabData data) {
       return new HistogramPanel(data);
    }

    @Override
    public String getName() {
        return "Histogram";
    }
    
    @Override
    public boolean canByMultiple(){
        return true;
    }
    @Override
    public String getIcon() {
        return "/images/histogram.png";
    }


    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IPlugin getCopy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PropertyChangeSupport getChangeSupport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}