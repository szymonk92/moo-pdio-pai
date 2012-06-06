/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Lukasz
 */
public class ViewPanel extends javax.swing.JPanel {

    /**
     * Creates new form ViewPanel
     */
    public String image;
    public String xmlFile;
    public List<Rectangle> tags;
    public List<Rectangle> regions;
    public List<Rectangle> found;
    public String folder;
    public int foundResult;
    public int regionsResult;
    public PropertyChangeSupport changeSupport;
    public boolean imageSet;

    public ViewPanel() {
        initComponents();
        changeSupport = new PropertyChangeSupport(this);
    }

    public ViewPanel(String folder, String image, String xmlFile) {
        initComponents();
        imageSet = false;
        changeSupport = new PropertyChangeSupport(this);
        this.image = image;
        this.folder = folder;
        this.miniaturImagePanel.setImageName(image);

        this.xmlFile = xmlFile;
        if (xmlFile == null) {
            this.tagCountLabel.setText("Brak pliku xml.");
        }
        this.processingLabel.setVisible(false);
        this.progressBar.setVisible(false);
    }

    public void SetImage(BufferedImage minImage) {
        this.miniaturImagePanel.setImage(minImage);
        imageSet = true;
        this.revalidate();
    }

    public void Start() {
        setProcessName("Przetwarzanie");
        this.processingLabel.setVisible(true);
        this.progressBar.setValue(0);
        this.progressBar.setVisible(true);
        changeSupport.firePropertyChange("start", null, null);
        this.revalidate();
    }

    public void End() {
        this.processingLabel.setVisible(false);
        this.progressBar.setVisible(false);
        changeSupport.firePropertyChange("end", null, null);
        this.revalidate();
    }

    public void addProgress(int value) {
        int tmpValue = this.progressBar.getValue() + value;
        if (tmpValue > 100) {
            tmpValue = 100;
        }
        this.progressBar.setValue(tmpValue);
    }

    public void setProcessName(String name) {
        this.processingLabel.setText(name + "...");
    }

    public void setRegions(List<Rectangle> value) {
        regions = value;
        this.regionCountLabel.setText(String.valueOf(value.size()));
        if (xmlFile != null) {
            changeSupport.firePropertyChange("regions", null, value.size());
        }
        this.revalidate();
    }

    public void setTags(List<Rectangle> value) {
        tags = value;
        this.tagCountLabel.setText(String.valueOf(value.size()));
        if (xmlFile != null) {
            changeSupport.firePropertyChange("tags", null, value.size());
        }
        this.revalidate();
    }

    public void setFound(List<Rectangle> value) {
        found = value;
        this.foundCountLabel.setText(String.valueOf(value.size()));
        if (xmlFile != null) {
            changeSupport.firePropertyChange("found", null, value.size());
        }
        this.revalidate();
    }

    public void setRegionsResult(int value) {
        regionsResult = value;
        if (xmlFile != null) {
            changeSupport.firePropertyChange("regionsResult", null, value);
        }
        if (tags != null && !tags.isEmpty()) {
            this.regionResultLabel.setText(String.valueOf(value) + " z " + String.valueOf(tags.size()));
        }
        this.revalidate();
    }

    public void sendInfo() {
        changeSupport.firePropertyChange("regions", null, regions.size());
        changeSupport.firePropertyChange("tags", null, tags.size());
        changeSupport.firePropertyChange("regionsResult", null, regionsResult);
    }

    public void Clear() {
        this.setBackground(new Color(240,240,240));
        this.processingLabel.setVisible(false);
        this.progressBar.setVisible(false);
        found = null;
        this.foundCountLabel.setText("0");
        foundResult = 0;
        this.foundResultLabel.setText("");
    }

    public void setFoundResult(int value) {
        foundResult = value;
        if (xmlFile != null) {
            changeSupport.firePropertyChange("foundResult", null, value);
        }
        if (tags != null) {
            if (!tags.isEmpty()) {
                this.foundResultLabel.setText(String.valueOf(value) + " z " + String.valueOf(tags.size()));
            }
            if (value == tags.size() && value == found.size()) {
                changeSupport.firePropertyChange("result", null, 1);
                this.setBackground(Color.GREEN);
            } else if (value == 0) {
                changeSupport.firePropertyChange("result", null, 0);
                this.setBackground(Color.RED);
            } else {
                changeSupport.firePropertyChange("result", null, 2);
                this.setBackground(Color.ORANGE);
            }
        }
        this.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        miniaturImagePanel = new gui.MiniaturImagePanel();
        jPanel1 = new javax.swing.JPanel();
        tagLabel = new javax.swing.JLabel();
        tagCountLabel = new javax.swing.JLabel();
        emptyLabel = new javax.swing.JLabel();
        regionLabel = new javax.swing.JLabel();
        regionCountLabel = new javax.swing.JLabel();
        regionResultLabel = new javax.swing.JLabel();
        foundLabel = new javax.swing.JLabel();
        foundCountLabel = new javax.swing.JLabel();
        foundResultLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        processingLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        setMaximumSize(new java.awt.Dimension(32767, 80));
        setMinimumSize(new java.awt.Dimension(0, 80));
        setPreferredSize(new java.awt.Dimension(628, 80));

        miniaturImagePanel.setMaximumSize(new java.awt.Dimension(64, 64));
        miniaturImagePanel.setPreferredSize(new java.awt.Dimension(64, 64));

        javax.swing.GroupLayout miniaturImagePanelLayout = new javax.swing.GroupLayout(miniaturImagePanel);
        miniaturImagePanel.setLayout(miniaturImagePanelLayout);
        miniaturImagePanelLayout.setHorizontalGroup(
            miniaturImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );
        miniaturImagePanelLayout.setVerticalGroup(
            miniaturImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        jPanel1.setLayout(new java.awt.GridLayout(3, 3));

        tagLabel.setText("Ilość otagowanych znaków:");
        jPanel1.add(tagLabel);

        tagCountLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tagCountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tagCountLabelMouseClicked(evt);
            }
        });
        jPanel1.add(tagCountLabel);
        jPanel1.add(emptyLabel);

        regionLabel.setText("Ilość znalezionych rejonów:");
        jPanel1.add(regionLabel);

        regionCountLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        regionCountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regionCountLabelMouseClicked(evt);
            }
        });
        jPanel1.add(regionCountLabel);

        regionResultLabel.setBackground(java.awt.SystemColor.activeCaption);
        regionResultLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        regionResultLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                regionResultLabelMouseClicked(evt);
            }
        });
        jPanel1.add(regionResultLabel);

        foundLabel.setText("Ilość znalezionych znaków:");
        jPanel1.add(foundLabel);

        foundCountLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        foundCountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foundCountLabelMouseClicked(evt);
            }
        });
        jPanel1.add(foundCountLabel);

        foundResultLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        foundResultLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foundResultLabelMouseClicked(evt);
            }
        });
        jPanel1.add(foundResultLabel);

        jPanel2.setLayout(new java.awt.GridLayout(2, 1));

        processingLabel.setText("Przetwarzanie...");
        jPanel2.add(processingLabel);
        jPanel2.add(progressBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(miniaturImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(83, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(miniaturImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(5, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tagCountLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tagCountLabelMouseClicked
        try {
            PreviewWindow window = new PreviewWindow();
            window.setup(tags, ImageIO.read(new File(folder + File.separator + image)));
            window.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(ViewPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tagCountLabelMouseClicked

    private void regionCountLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regionCountLabelMouseClicked
        try {
            PreviewWindow window = new PreviewWindow();
            window.setup(regions, ImageIO.read(new File(folder + File.separator + image)));
            window.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(ViewPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_regionCountLabelMouseClicked

    private void foundCountLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foundCountLabelMouseClicked
        try {
            PreviewWindow window = new PreviewWindow();
            window.setup(found, ImageIO.read(new File(folder + File.separator + image)));
            window.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(ViewPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_foundCountLabelMouseClicked

    private void regionResultLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_regionResultLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_regionResultLabelMouseClicked

    private void foundResultLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foundResultLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_foundResultLabelMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel emptyLabel;
    private javax.swing.JLabel foundCountLabel;
    private javax.swing.JLabel foundLabel;
    private javax.swing.JLabel foundResultLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private gui.MiniaturImagePanel miniaturImagePanel;
    private javax.swing.JLabel processingLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel regionCountLabel;
    private javax.swing.JLabel regionLabel;
    private javax.swing.JLabel regionResultLabel;
    private javax.swing.JLabel tagCountLabel;
    private javax.swing.JLabel tagLabel;
    // End of variables declaration//GEN-END:variables
}
