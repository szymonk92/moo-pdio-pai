/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;
import sys.RGBHelper;

/**
 *
 * @author Lukasz
 */
public class DrawPanel extends javax.swing.JPanel {

    public BufferedImage image;
    public PropertyChangeSupport changeSupport;
    public boolean processing = false;
    private static Color transparent = new Color(1f,1f,1f,0.6f);
    /**
     * Creates new form DrawPanel
     */
    public DrawPanel() {
        initComponents();
        
        changeSupport = new PropertyChangeSupport(this);
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectNodeAt(e.getPoint());
                }
                else{
                      float[] hsv = RGBHelper.getHSV(image.getRGB(e.getPoint().x, e.getPoint().y));
                      //Messages.info("H:"+hsv[0]+",S:"+hsv[0]+",B:"+hsv[0]);
                }
            }
            

            private void selectNodeAt(Point p) {
                
                    repaint();
                
            }
        });
    }

    public DrawPanel(BufferedImage image) {
        this();
        this.image = image;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paints the background
        Graphics2D g2d = (Graphics2D) g;
        float width = this.getWidth();
        float height = this.getHeight();
        Point orgin = new Point();
        if (image != null) {
            float imgWidth = image.getWidth();
            float imgHeight = image.getHeight();
            float scale = Math.min(Math.min(width/imgWidth, height/imgHeight),1);
            g2d.scale(scale,scale);
            orgin = new Point((int)Math.max((width-imgWidth*scale), 0),(int)Math.max((height-imgHeight*scale), 0));
            g2d.drawImage(image, orgin.x, orgin.y, (int)imgWidth, (int)imgHeight, null);
        }
        if(!processing){
           
        }
        else{
            g2d.setColor(transparent);
            g2d.fillRect((image.getWidth()/2)-30, (image.getHeight()/2)-10, 100, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawString("Processing...", (image.getWidth()/2)-20, (image.getHeight()/2)+5);
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
