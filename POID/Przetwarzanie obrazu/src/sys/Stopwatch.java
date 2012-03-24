/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class Stopwatch {

    private boolean running = false;
    List<Task> tasks;
    protected PropertyChangeSupport changeSupport;
    public long pixelCount;

    public long getPixelCount() {
        return pixelCount;
    }
    
    public PropertyChangeSupport getChangeSupport() {
        return changeSupport;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Stopwatch(long pixelCount) {
        tasks = new ArrayList<Task>();
        changeSupport = new PropertyChangeSupport(this);
        this.pixelCount = pixelCount;
    }

    public void start(String task) {
        if (running) {
            return;
        }
        tasks.add(new Task(task, System.currentTimeMillis()));
        changeSupport.firePropertyChange("Tasks", null, tasks);
        running = true;
    }

    public void stop() {
        if (!running || tasks == null || tasks.isEmpty()) {
            return;
        }
        tasks.get(tasks.size() - 1).stop(System.currentTimeMillis());
        changeSupport.firePropertyChange("Tasks", null, tasks);
        running = false;
    }

    public long getElapsedTime() {
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }
        if (running) {
            return System.currentTimeMillis() - tasks.get(tasks.size() - 1).getStartTime();
        } else {
            return tasks.get(tasks.size() - 1).getEndTime() - tasks.get(tasks.size() - 1).getStartTime();
        }
    }

    public void reset() {
        tasks = new ArrayList<Task>();
        running = false;
    }
}
