/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.beans.PropertyChangeSupport;

/**
 *
 * @author Lukasz
 */
public abstract class AbstractView implements IView {

    @Override
    public String getIcon() {
        return null;
    }
    
    @Override
    public boolean canByMultiple(){
        return false;
    }
    
    @Override
    public boolean addAtTop(){
        return false;
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
