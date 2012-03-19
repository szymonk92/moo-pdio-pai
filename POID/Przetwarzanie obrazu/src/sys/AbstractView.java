/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz
 */
public abstract class AbstractView implements IView {

    public TabData data;
    @Override
    public String getIcon() {
        return null;
    }
    
    @Override
    public void setTabData(TabData data){
        this.data = data;
    }
    
    @Override
    public boolean canByMultiple(){
        return false;
    }
    
    @Override
    public boolean addAtTop(){
        return false;
    }
    
}
