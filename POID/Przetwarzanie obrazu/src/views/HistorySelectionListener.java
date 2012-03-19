/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import gui.MainPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import views.HistoryPanel;

/**
 *
 * @author Lukasz
 */
public class HistorySelectionListener implements ListSelectionListener {

    HistoryPanel mainPanel;

    public HistorySelectionListener(HistoryPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

        int selected = this.mainPanel.historyTable.getSelectedRow();

        if (selected > 0 && selected < mainPanel.data.filters.size()) {
            this.mainPanel.upButton.setEnabled(true);
        } else {
            this.mainPanel.upButton.setEnabled(false);
        }
        if (selected >= 0 && selected < mainPanel.data.filters.size() - 1) {
            this.mainPanel.downButton.setEnabled(true);
        } else {
            this.mainPanel.downButton.setEnabled(false);
        }
        if (selected >= 0 && selected < mainPanel.data.filters.size()) {
            this.mainPanel.removeButton.setEnabled(true);
            if (this.mainPanel.data.filters.get(selected).isEditable()) {
                this.mainPanel.editButton.setEnabled(true);
            } else {
                this.mainPanel.editButton.setEnabled(false);
            }
        } else {
            this.mainPanel.removeButton.setEnabled(false);
            this.mainPanel.editButton.setEnabled(false);
        }
    }
}
