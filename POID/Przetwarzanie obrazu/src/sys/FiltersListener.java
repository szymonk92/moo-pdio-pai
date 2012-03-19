/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import gui.MainPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;

/**
 *
 * @author Lukasz
 */
public class FiltersListener implements ObservableListListener, PropertyChangeListener {
    ImageProcessor task;
    MainPanel mainPanel;
    public FiltersListener(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }



    @Override
    public void listElementsAdded(ObservableList list, int index, int length) {
       task = new ImageProcessor(mainPanel.data, true);
        task.addPropertyChangeListener(this);
        task.execute();
    }

    @Override
    public void listElementsRemoved(ObservableList list, int index, List oldElements) {
        task = new ImageProcessor(mainPanel.data, false);
        task.addPropertyChangeListener(this);
        task.execute();
    }

    @Override
    public void listElementReplaced(ObservableList list, int index, Object oldElement) {
        task = new ImageProcessor(mainPanel.data, false);
        task.addPropertyChangeListener(this);
        task.execute();
    }

    @Override
    public void listElementPropertyChanged(ObservableList list, int index) {
        task = new ImageProcessor(mainPanel.data, false);
        task.addPropertyChangeListener(this);
        task.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            mainPanel.progressBar.setValue(progress);
            if (progress >= 100) {
                mainPanel.progressBar.setValue(0);
            }
        }
    }
    
}
