/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class DrawPanel extends javax.swing.JPanel {

    public BufferedImage image;
    public PropertyChangeSupport changeSupport;
    private static Color transparent = new Color(0f, 1f, 0f, 0.6f);

    /**
     * Creates new form DrawPanel
     */
    public DrawPanel() {
        initComponents();
        changeSupport = new PropertyChangeSupport(this);
    }

    public DrawPanel(BufferedImage image) {
        this();
        this.image = image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.revalidate();
        this.repaint();
    }

    public void setRectangleList(List<Rectangle> rectangles) {
        if (image != null) {
            Graphics g = this.image.getGraphics();
            g.setColor(transparent);
            for (Rectangle rect : rectangles) {
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
           
            this.revalidate();
            this.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paints the background
        Graphics2D g2d = (Graphics2D) g;
        float width = this.getWidth();
        float height = this.getHeight();
        Point orgin;
        if (image != null) {
            float imgWidth = image.getWidth();
            float imgHeight = image.getHeight();
            float scale = Math.min(Math.min(width / imgWidth, height / imgHeight), 1);
            g2d.scale(scale, scale);
            orgin = new Point((int) Math.max((width - imgWidth * scale), 0), (int) Math.max((height - imgHeight * scale), 0));
            g2d.drawImage(image, orgin.x, orgin.y, (int) imgWidth, (int) imgHeight, null);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(512, 512));
        setPreferredSize(new java.awt.Dimension(512, 512));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
