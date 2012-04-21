/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;
import segmentation.QuadNode;
import segmentation.QuadTree;
import segmentation.Region;

/**
 *
 * @author Lukasz
 */
public class SegmentationDrawPanel extends javax.swing.JPanel {

    public BufferedImage image;
    public QuadTree quadTree;
    private QuadNode selectedNode = null;
    private List<QuadNode> selectedNodeN;
    private List<Color> colors;
    private Random rand = new Random();
    boolean showGrid = true;
    boolean fillAll = true;
    boolean areaColor = false;
    boolean showSelected = false;
    boolean fillSelectedRegion = true;
    boolean inverseMask = false;
    public Region selectedRegion;
    public PropertyChangeSupport changeSupport;
    public int fillOption = 0;

    /**
     * Creates new form SegmentationDrawPanel
     */
    public SegmentationDrawPanel() {
        initComponents();
        changeSupport = new PropertyChangeSupport(this);
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectNodeAt(e.getPoint());
                }
            }

            private void selectNodeAt(Point p) {
                selectedNode = null;
                selectedNodeN = new ArrayList<QuadNode>();
                if (quadTree != null) {
                    for (int nodePosition : quadTree.QuadeNodeList.keySet()) {
                        QuadNode node = quadTree.QuadeNodeList.get(nodePosition);
                        if (node != null) {
                            int x = p.x - node.area.x;
                            int y = p.y - node.area.y;
                            if ((x >= 0 && x <= node.area.width) && (y >= 0 && y <= node.area.height)) {
                                selectedNode = node;
                                selectedRegion = node.region;
                                for (QuadNode neighbour : node.NeighboursList.values()) {
                                    selectedNodeN.add(neighbour);
                                }
                                changeSupport.firePropertyChange("selectedNode", null, selectedNode);
                            }

                        }
                    }
                    repaint();
                }
            }
        });
    }

    public SegmentationDrawPanel(BufferedImage image, QuadTree tree) {
        this();
        this.quadTree = tree;
        this.image = image;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paints the background
        if (image != null) {
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        }
        paintTree(g, quadTree);
    }

    public void paintTreeRegion(Graphics g, Region region, Color color, boolean grid, boolean fill) {
        for (QuadNode node : region.QuadeNodeList.values()) {
            if (grid && node.area.width > 1) {
                g.setColor(Color.black);
                g.drawRect(node.area.x, node.area.y, node.area.width, node.area.height);
            }
            if (fill) {
                g.setColor(color);
                if (grid && node.area.width > 1) {
                    g.fillRect(node.area.x + 1, node.area.y + 1, node.area.width - 1, node.area.height - 1);
                } else {
                    g.fillRect(node.area.x, node.area.y, node.area.width, node.area.height);
                }
            }
        }
    }

    public void paintTree(Graphics g, QuadTree tree) {
        if (tree != null) {
            if (colors == null) {
                colors = new ArrayList<Color>();
            }
            int counter = 0;
            if (tree.regions != null) {
                for (Region region : tree.regions) {
                    if (colors.size() < tree.regions.size()) {
                        colors.add(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
                    }
                    Color color =getFillColor(region,colors.get(counter) );
                    paintTreeRegion(g, region, color, showGrid, ((fillOption == 1 || (selectedRegion != null && (region.equals(selectedRegion) && fillOption == 2))||fillOption>2)&&color!=null));
                    counter++;
                }
            }
            if (selectedNode != null && showSelected) {
                g.setColor(Color.GREEN);
                if (showGrid && selectedNode.area.width > 1) {
                    g.fillRect(selectedNode.area.x + 1, selectedNode.area.y + 1, selectedNode.area.width - 1, selectedNode.area.height - 1);
                } else {
                    g.fillRect(selectedNode.area.x, selectedNode.area.y, selectedNode.area.width, selectedNode.area.height);
                }
                for (QuadNode testNode : selectedNodeN) {
                    g.setColor(Color.BLUE);
                    if (showGrid && testNode.area.width > 1) {
                        g.fillRect(testNode.area.x + 1, testNode.area.y + 1, testNode.area.width - 1, testNode.area.height - 1);
                    } else {
                        g.fillRect(testNode.area.x, testNode.area.y, testNode.area.width, testNode.area.height);
                    }
                }
            }
        }
    }

    private Color getFillColor(Region region, Color randomColor) {
        switch (fillOption) {
            case 0:
                return null;
            case 1:
                return areaColor ? region.avrageColor : randomColor;
            case 2:
                if(region.equals(selectedRegion)){
                    return areaColor ? region.avrageColor : randomColor;
                }
                else{
                    return null;
                }
            case 3:
                Color one3 = !inverseMask ? Color.WHITE:Color.BLACK;
                Color two3 = inverseMask ? Color.WHITE:Color.BLACK;
                return region.equals(selectedRegion)? one3: two3;
             
                
            case 4:
                Color one4 = !inverseMask ? null:Color.BLACK;
                Color two4 = inverseMask ? null:Color.BLACK;
                return region.equals(selectedRegion)? one4: two4;
            default:
                return randomColor;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(512, 512));
        setPreferredSize(new java.awt.Dimension(512, 512));
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
