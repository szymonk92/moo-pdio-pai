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
public class HistogramView extends AbstractView{

    @Override
    public JPanel getView() {
       return new HistogramPanel(this.data);
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
}