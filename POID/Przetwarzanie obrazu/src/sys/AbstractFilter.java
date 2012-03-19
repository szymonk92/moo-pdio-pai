/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.beans.PropertyChangeSupport;
import javax.swing.JPanel;

/**
 *
 * @author Lukasz
 */
public abstract class AbstractFilter implements IFilter {

    protected String name;
    protected String description;
    protected boolean editable;
    protected PropertyChangeSupport changeSupport;
    
    public AbstractFilter(){
        changeSupport = new PropertyChangeSupport(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    protected void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    @Override
    public JPanel getEditPanel(){
        return null;
    }
    
    @Override
    public String getIcon(){
        return null;
    }
    
    @Override
    public PropertyChangeSupport getChangeSupport(){
        return changeSupport;
    }
}
