/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.image.BufferedImage;
import sys.DTWMatch;

/**
 *
 * @author Lukasz
 */
public class DistanceImagePreviewWindow extends javax.swing.JFrame {

    BufferedImage image;

    /**
     * Creates new form DistanceImagePreviewWindow
     */
    public DistanceImagePreviewWindow(BufferedImage image) {
        initComponents();
        this.image = image;
        this.imagePanel.setImage(DTWMatch.resize(image,300));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePanel = new gui.ImagePanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 350));
        setPreferredSize(new java.awt.Dimension(500, 350));

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        getContentPane().add(imagePanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.ImagePanel imagePanel;
    // End of variables declaration//GEN-END:variables
}
