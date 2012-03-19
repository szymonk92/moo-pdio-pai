/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import sys.IView;
import sys.IconHelper;
import sys.TabData;

/**
 *
 * @author Lukasz
 */
public class ViewPanel extends javax.swing.JPanel {

    boolean state = true;
    IView view;
    TabData data;
    Dimension maxd = new Dimension(200, 200);

    /**
     * Creates new form ViewPanel
     */
    public ViewPanel(IView view, TabData data) {
        this.data = data;
        this.view = view;
        view.setTabData(data);
        initComponents();
        if (view.getIcon() == null) {
            titleLabel.setIcon(IconHelper.getDefaultIcon());
        } else {
            titleLabel.setIcon(new ImageIcon(getClass().getResource(view.getIcon())));
        }
        this.titleLabel.setText(view.getName());
        this.contentPanel.add(view.getView());

        setSize(maxd);
        setPreferredSize(maxd);
        setMaximumSize(maxd);
        setMinimumSize(maxd);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setPreferredSize(new java.awt.Dimension(400, 200));
        setLayout(new java.awt.BorderLayout());

        menuPanel.setBackground(new java.awt.Color(204, 204, 204));
        menuPanel.setMaximumSize(new java.awt.Dimension(32767, 22));
        menuPanel.setMinimumSize(new java.awt.Dimension(100, 22));
        menuPanel.setPreferredSize(new java.awt.Dimension(400, 22));

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        titleLabel.setText("Title");
        titleLabel.setMaximumSize(new java.awt.Dimension(100, 22));
        titleLabel.setMinimumSize(new java.awt.Dimension(100, 22));
        titleLabel.setPreferredSize(new java.awt.Dimension(100, 22));

        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/window_app_list_close.png"))); // NOI18N
        closeButton.setEnabled(false);
        closeButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        closeButton.setMaximumSize(new java.awt.Dimension(22, 22));
        closeButton.setMinimumSize(new java.awt.Dimension(22, 22));
        closeButton.setOpaque(false);
        closeButton.setPreferredSize(new java.awt.Dimension(22, 22));
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                MouseExited(evt);
            }
        });
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addGap(31, 31, 31)
                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(closeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(menuPanel, java.awt.BorderLayout.PAGE_START);

        contentPanel.setMaximumSize(new java.awt.Dimension(32767, 250));
        contentPanel.setMinimumSize(new java.awt.Dimension(100, 250));
        contentPanel.setPreferredSize(new java.awt.Dimension(222, 250));
        contentPanel.setLayout(new java.awt.BorderLayout());
        add(contentPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MouseEntered
        evt.getComponent().setEnabled(true);

    }//GEN-LAST:event_MouseEntered

    private void MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MouseExited
        evt.getComponent().setEnabled(false);
    }//GEN-LAST:event_MouseExited

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.getParent().remove(this);
        this.data.getViews().remove(view);
    }//GEN-LAST:event_closeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
