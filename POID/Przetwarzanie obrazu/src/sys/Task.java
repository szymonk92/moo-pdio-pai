/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz
 */
public class Task {
    private String Name;
    private long startTime;
    private long endTime;
    private long operationTime;
    private boolean inProgess;

    public boolean isInProgess() {
        return inProgess;
    }
    
    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getName() {
        return Name;
    }

    public long getOperationTime() {
        return operationTime;
    }

    public Task(String Name, long startTime) {
        this.Name = Name;
        this.startTime = startTime;
        this.inProgess = true;
    }
    
    public void stop(long endTime){
        this.endTime = endTime;
        this.operationTime = endTime-this.startTime;
        this.inProgess = false;
    }
}
