/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import gui.MainPanel;
import gui.ViewPanel;
import java.util.List;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;

/**
 *
 * @author Lukasz
 */
public class ViewsListener implements ObservableListListener {

    MainPanel mainPanel;
    
    public ViewsListener(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    @Override
    public void listElementsAdded(ObservableList list, int index, int length) {
        IView view = (IView) list.get(index);
        if (list.size() == 1) {
            mainPanel.setSplit();
        }
        if (view.addAtTop()) {
            this.mainPanel.columnpanel.add(new ViewPanel(view, this.mainPanel.data), 0);
        } else {
            this.mainPanel.columnpanel.add(new ViewPanel(view, this.mainPanel.data));
        }

        this.mainPanel.columnpanel.revalidate();
    }

    @Override
    public void listElementsRemoved(ObservableList list, int index, List oldElements) {

        if (list.size() == 0) {
            mainPanel.setSplit();
        }
        this.mainPanel.columnpanel.revalidate();
    }

    @Override
    public void listElementReplaced(ObservableList list, int index, Object oldElement) {
    }

    @Override
    public void listElementPropertyChanged(ObservableList list, int index) {
    }
}
