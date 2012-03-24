/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.beans.PropertyChangeListener;
import java.util.List;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;

/**
 *
 * @author Lukasz
 */
public class FiltersListener implements ObservableListListener {

    ImageProcessor task;
    TabData data;
    int replaceCounter;
    PropertyChangeListener taskPropertyChangeListener;

    public PropertyChangeListener getTaskPropertyChangeListener() {
        return taskPropertyChangeListener;
    }

    public void setTaskPropertyChangeListener(PropertyChangeListener taskPropertyChangeListener) {
        this.taskPropertyChangeListener = taskPropertyChangeListener;
    }
   
    public int getReplaceCounter() {
        return replaceCounter;
    }

    public void setReplaceCounter(int replaceCounter) {
        this.replaceCounter = replaceCounter;
    }

    public FiltersListener(TabData data, PropertyChangeListener taskPropertyChangeListener) {
        this.data = data;
        this.taskPropertyChangeListener = taskPropertyChangeListener;
    }

    @Override
    public void listElementsAdded(ObservableList list, int index, int length) {
        if(task!=null && !task.isDone()) task.cancel(true);
        task = new ImageProcessor(data, true);
        task.addPropertyChangeListener(taskPropertyChangeListener);
        task.execute();
    }

    @Override
    public void listElementsRemoved(ObservableList list, int index, List oldElements) {
        if(task!=null && !task.isDone()) task.cancel(true);
        task = new ImageProcessor(data, false);
        task.addPropertyChangeListener(taskPropertyChangeListener);
        task.execute();
    }

    @Override
    public void listElementReplaced(ObservableList list, int index, Object oldElement) {
        if (replaceCounter > 1) {
            replaceCounter--;
            return;
        }
        replaceCounter--;
        if(task!=null && !task.isDone()) task.cancel(true);
        task = new ImageProcessor(data, false);
        task.addPropertyChangeListener(taskPropertyChangeListener);
        task.execute();
    }

    @Override
    public void listElementPropertyChanged(ObservableList list, int index) {
        
        if(task!=null && !task.isDone()) task.cancel(true);
        task = new ImageProcessor(data, false);
        task.addPropertyChangeListener(taskPropertyChangeListener);
        task.execute();
    }

    @Override
    protected void finalize() throws Throwable {
        task = null;
        super.finalize();
    }
}