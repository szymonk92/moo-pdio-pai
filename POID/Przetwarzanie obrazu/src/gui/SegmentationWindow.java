/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import segmentation.*;
import sys.Messages;

/**
 *
 * @author Lukasz
 */
public class SegmentationWindow extends javax.swing.JFrame implements PropertyChangeListener {
    
    BufferedImage image;
    private QuadTree quadTree;
    private QuadTreeProcessor processor;

    /**
     * Creates new form SegmentationWindow
     */
    public SegmentationWindow() {
        initComponents();
    }
    
    SegmentationWindow(BufferedImage image) {
        this();
        this.image = image;
        quadTree = new QuadTree(image);
        this.segmentationDrawPanel.image = image;
        this.segmentationDrawPanel.quadTree = quadTree;
        this.segmentationDrawPanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        this.segmentationDrawPanel.revalidate();
        this.segmentationDrawPanel.changeSupport.addPropertyChangeListener(this);
        this.depthLimitSlider.setMaximum(quadTree.maxDepth);
        this.depthLimitSlider.setValue(quadTree.maxDepth);
        this.valueLabel.setText(String.valueOf(this.jSlider1.getValue()));
    }
    
    public void setQuadTree(QuadTree tree) {
        quadTree = tree;
        int count = 1;
        this.regionsComboBox.removeAllItems();
        for (Region region : quadTree.regions) {
            this.regionsComboBox.addItem("Region " + count);
            count++;
        }
        if(!quadTree.regions.isEmpty()){
        this.regionsComboBox.setSelectedIndex(0);
        }
        this.segmentationDrawPanel.quadTree = quadTree;
        this.segmentationDrawPanel.repaint();
        if (processor != null) {
            processor = null;
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

        saveFileChooser = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        exitButton = new javax.swing.JButton();
        processButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        gridCheckBox = new javax.swing.JCheckBox();
        colorFillCheckBox = new javax.swing.JCheckBox();
        showSelectedCheckBox = new javax.swing.JCheckBox();
        fillComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        pixelCompererPanel = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        valueLabel = new javax.swing.JLabel();
        comparerTypeComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        masksComboBox = new javax.swing.JComboBox();
        formatComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        regionsComboBox = new javax.swing.JComboBox();
        inverseCheckBox = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        depthLimitSlider = new javax.swing.JSlider();
        forceSplitCheckBox = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        minPixelTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        segmentationDrawPanel = new gui.SegmentationDrawPanel();

        saveFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        saveFileChooser.setDialogTitle("Save mask");
        saveFileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMaximumSize(new java.awt.Dimension(300, 32767));
        jPanel1.setMinimumSize(new java.awt.Dimension(300, 200));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 200));

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        processButton.setText("Process");
        processButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processButtonActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Draw options"));

        gridCheckBox.setSelected(true);
        gridCheckBox.setText("Show grid");
        gridCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridCheckBoxActionPerformed(evt);
            }
        });

        colorFillCheckBox.setText("Area color");
        colorFillCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorFillCheckBoxActionPerformed(evt);
            }
        });

        showSelectedCheckBox.setText("Show selected");
        showSelectedCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showSelectedCheckBoxActionPerformed(evt);
            }
        });

        fillComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No fill", "Fill all", "Fill only selected", "Mask fill", "Transparent mask fill" }));
        fillComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("Fill options:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fillComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(gridCheckBox)
                        .addGap(10, 10, 10)
                        .addComponent(colorFillCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(showSelectedCheckBox)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gridCheckBox)
                    .addComponent(colorFillCheckBox)
                    .addComponent(showSelectedCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fillComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pixelCompererPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Pixel/Region Comparer"));

        jSlider1.setMajorTickSpacing(10);
        jSlider1.setMaximum(255);
        jSlider1.setMinorTickSpacing(1);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        comparerTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Simple", "Luminance" }));

        jLabel4.setText("Type:");

        javax.swing.GroupLayout pixelCompererPanelLayout = new javax.swing.GroupLayout(pixelCompererPanel);
        pixelCompererPanel.setLayout(pixelCompererPanelLayout);
        pixelCompererPanelLayout.setHorizontalGroup(
            pixelCompererPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pixelCompererPanelLayout.createSequentialGroup()
                .addGroup(pixelCompererPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pixelCompererPanelLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comparerTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pixelCompererPanelLayout.setVerticalGroup(
            pixelCompererPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pixelCompererPanelLayout.createSequentialGroup()
                .addGroup(pixelCompererPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comparerTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pixelCompererPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(valueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Export"));

        jButton1.setText("Export");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        masksComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Selected" }));

        formatComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PNG", "BMP", "JPG" }));

        jLabel1.setText("Format:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(masksComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(masksComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(formatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1)
                .addComponent(jButton1))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Masks"));

        regionsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regionsComboBoxActionPerformed(evt);
            }
        });

        inverseCheckBox.setText("Inverse");
        inverseCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inverseCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(regionsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(inverseCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(regionsComboBox)
                .addComponent(inverseCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Split options"));

        depthLimitSlider.setMajorTickSpacing(1);
        depthLimitSlider.setMaximum(8);
        depthLimitSlider.setMinorTickSpacing(1);
        depthLimitSlider.setPaintLabels(true);
        depthLimitSlider.setPaintTicks(true);
        depthLimitSlider.setSnapToTicks(true);
        depthLimitSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                depthLimitSliderStateChanged(evt);
            }
        });

        forceSplitCheckBox.setText("Force split");
        forceSplitCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forceSplitCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(depthLimitSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forceSplitCheckBox)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(depthLimitSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(forceSplitCheckBox)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Mearg options"));

        minPixelTextField.setText("0");

        jLabel2.setText("Minimum pixel count in region:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minPixelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(minPixelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(processButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exitButton)
                .addGap(33, 33, 33))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pixelCompererPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(pixelCompererPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitButton)
                    .addComponent(processButton))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);

        segmentationDrawPanel.setPreferredSize(new java.awt.Dimension(530, 530));
        jScrollPane1.setViewportView(segmentationDrawPanel);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void processButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processButtonActionPerformed
        this.segmentationDrawPanel.quadTree = null;
        int comparerValue = this.jSlider1.getValue();
        IPixelComparer comparer =new SimplePixelComparer(comparerValue);
        if(this.comparerTypeComboBox.getSelectedIndex()==1){
            comparer = new LuminancePixelComparer(comparerValue);
        }
        quadTree.setPixelComperer(comparer);
        try {
            int value = Integer.parseInt(this.minPixelTextField.getText());
            quadTree.minimumPixelRegion = Math.max(0, Math.min(value, image.getHeight() * image.getWidth()));
        } catch (Exception ex) {
            quadTree.minimumPixelRegion = 0;
        }
        this.minPixelTextField.setText(Integer.toString(quadTree.minimumPixelRegion));
        processor = new QuadTreeProcessor(quadTree, this);
        processor.execute();
    }//GEN-LAST:event_processButtonActionPerformed
    
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_exitButtonActionPerformed
    
    private void gridCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridCheckBoxActionPerformed
        this.segmentationDrawPanel.showGrid = this.gridCheckBox.isSelected();
        this.segmentationDrawPanel.repaint();
    }//GEN-LAST:event_gridCheckBoxActionPerformed
    
    private void colorFillCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorFillCheckBoxActionPerformed
        this.segmentationDrawPanel.areaColor = this.colorFillCheckBox.isSelected();
        this.segmentationDrawPanel.repaint();
    }//GEN-LAST:event_colorFillCheckBoxActionPerformed
    
    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        this.valueLabel.setText(String.valueOf(this.jSlider1.getValue()));
    }//GEN-LAST:event_jSlider1StateChanged
    
    private void depthLimitSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_depthLimitSliderStateChanged
        quadTree.depthLimit = this.depthLimitSlider.getValue();
    }//GEN-LAST:event_depthLimitSliderStateChanged
    
    private void showSelectedCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showSelectedCheckBoxActionPerformed
        this.segmentationDrawPanel.showSelected = this.showSelectedCheckBox.isSelected();
        this.segmentationDrawPanel.repaint();
    }//GEN-LAST:event_showSelectedCheckBoxActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int returnVal = this.saveFileChooser.showDialog(this, "Save file");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.saveFileChooser.getSelectedFile().getAbsoluteFile();
            if(!file.isDirectory()){
                file = file.getParentFile();
            }
            File dir = new File(file.getPath()+File.separator+"Masks");
            dir.mkdir();
            String format = (String) this.formatComboBox.getSelectedItem();
            this.saveFileChooser.setSelectedFile(null);            
            int index = this.regionsComboBox.getSelectedIndex();
            List<Region> regions = new ArrayList<Region>();
            if (this.masksComboBox.getSelectedIndex() == 1) {
                regions.add(quadTree.regions.get(index));
            }
            if (this.masksComboBox.getSelectedIndex() == 0) {
                regions.addAll(quadTree.regions);
            }
            int count = 1;
            for (Region region : regions) {
                BufferedImage mask = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
                Graphics g = mask.getGraphics();
                Graphics2D graphics = (Graphics2D) g;
                Color one = !this.inverseCheckBox.isSelected() ? Color.WHITE : Color.BLACK;
                Color two = this.inverseCheckBox.isSelected() ? Color.WHITE : Color.BLACK;
                for (int i = 0; i < quadTree.regions.size(); i++) {
                    Region current = quadTree.regions.get(i);
                    segmentationDrawPanel.paintTreeRegion(graphics, current, current.equals(region) ? one : two, false, true);
                }
                try {
                    ImageIO.write(mask, format, new File(dir.getPath()+File.separator+"Mask"+count+"."+format));
                } catch (IOException ex) {
                    Messages.error("Błąd zapisu pliku. " + ex.getMessage());
                    break;
                }
                count++;
            }
        }
        
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void forceSplitCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forceSplitCheckBoxActionPerformed
        this.quadTree.forceSplit = this.forceSplitCheckBox.isSelected();
    }//GEN-LAST:event_forceSplitCheckBoxActionPerformed
    
    private void fillComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillComboBoxActionPerformed
        this.segmentationDrawPanel.fillOption = this.fillComboBox.getSelectedIndex();
        this.segmentationDrawPanel.repaint();
    }//GEN-LAST:event_fillComboBoxActionPerformed
    
    private void inverseCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inverseCheckBoxActionPerformed
        this.segmentationDrawPanel.inverseMask = this.inverseCheckBox.isSelected();
        this.segmentationDrawPanel.repaint();
    }//GEN-LAST:event_inverseCheckBoxActionPerformed
    
    private void regionsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regionsComboBoxActionPerformed
        int index = regionsComboBox.getSelectedIndex();
        if (!this.quadTree.regions.isEmpty() && index >= 0 && index < this.quadTree.regions.size()) {
            this.segmentationDrawPanel.selectedRegion = this.quadTree.regions.get(regionsComboBox.getSelectedIndex());
            this.segmentationDrawPanel.repaint();
        }
    }//GEN-LAST:event_regionsComboBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox colorFillCheckBox;
    private javax.swing.JComboBox comparerTypeComboBox;
    private javax.swing.JSlider depthLimitSlider;
    private javax.swing.JButton exitButton;
    private javax.swing.JComboBox fillComboBox;
    private javax.swing.JCheckBox forceSplitCheckBox;
    private javax.swing.JComboBox formatComboBox;
    private javax.swing.JCheckBox gridCheckBox;
    private javax.swing.JCheckBox inverseCheckBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JComboBox masksComboBox;
    private javax.swing.JTextField minPixelTextField;
    private javax.swing.JPanel pixelCompererPanel;
    private javax.swing.JButton processButton;
    private javax.swing.JComboBox regionsComboBox;
    private javax.swing.JFileChooser saveFileChooser;
    private gui.SegmentationDrawPanel segmentationDrawPanel;
    private javax.swing.JCheckBox showSelectedCheckBox;
    private javax.swing.JLabel valueLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("selectedNode".equals(evt.getPropertyName())) {
            QuadNode node = (QuadNode) evt.getNewValue();
            int count = 0;
            for (Region region : quadTree.regions) {
                if (region.equals(node.region)) {
                    this.regionsComboBox.setSelectedIndex(count);
                    this.regionsComboBox.revalidate();
                    break;
                }
                count++;
            }
        }
    }
}
