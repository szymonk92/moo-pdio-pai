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
public interface IPlugin {
    
    String getDescription();

    String getIcon();
    
    String getName();

    IPlugin getCopy();

    public PropertyChangeSupport getChangeSupport();
    
}
