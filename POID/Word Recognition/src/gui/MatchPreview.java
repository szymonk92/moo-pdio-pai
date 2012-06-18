/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import dtw.DTWMatch;
import dtw.DistanceImageGenerator;

/**
 *
 * @author Lukasz
 */
public class MatchPreview extends javax.swing.JPanel {

    private static DecimalFormat df = new DecimalFormat("#.##");
    private DTWMatch match;
    private static DistanceImageGenerator distanceImageGenerator;

    /**
     * Creates new form MatchPreview
     */
    public MatchPreview(DTWMatch match) {
        initComponents();
        this.match = match;
        this.wordLabel.setText(match.getData().getWord());
        this.distnaceLabel.setText("nieznana");
    }

    public static DistanceImageGenerator getDistanceImageGenerator() {
        return distanceImageGenerator;
    }

    public static void setDistanceImageGenerator(DistanceImageGenerator distanceImageGenerator) {
        MatchPreview.distanceImageGenerator = distanceImageGenerator;
    }

    public void setWord(String word) {
        this.wordLabel.setText(word);
    }

    public void setDistance(double distace) {
        this.distnaceLabel.setText(df.format(distace));
    }

    public void setMiniaturImage(BufferedImage image) {
        this.miniaturImagePanel.setImage(resize(image, 100, 60));
    }

    public void setGlobalConstrains(boolean globalConstrains) {
        this.globalConstraintsLabel.setText(globalConstrains ? "tak" : "nie");
    }
   public void refershImage() {
       if (match.getImage() != null) {
            distanceImageGenerator.generate(match);
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

        wordLabel = new javax.swing.JLabel();
        distnaceLabel = new javax.swing.JLabel();
        miniaturImagePanel = new gui.ImagePanel();
        globalConstraintsLabel = new javax.swing.JLabel();

        wordLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        wordLabel.setText("wordLabel");

        distnaceLabel.setText("distnaceLabel");

        miniaturImagePanel.setPreferredSize(new java.awt.Dimension(100, 60));
        miniaturImagePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                miniaturImagePanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout miniaturImagePanelLayout = new javax.swing.GroupLayout(miniaturImagePanel);
        miniaturImagePanel.setLayout(miniaturImagePanelLayout);
        miniaturImagePanelLayout.setHorizontalGroup(
            miniaturImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 117, Short.MAX_VALUE)
        );
        miniaturImagePanelLayout.setVerticalGroup(
            miniaturImagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(miniaturImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(wordLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(distnaceLabel)
                        .addGap(51, 51, 51)
                        .addComponent(globalConstraintsLabel)))
                .addGap(0, 180, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(wordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(distnaceLabel)
                    .addComponent(globalConstraintsLabel))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(miniaturImagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void miniaturImagePanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_miniaturImagePanelMouseClicked
        if (match.getImage() == null) {
            distanceImageGenerator.generate(match);
            return;
        }
        DistanceImagePreviewWindow window = new DistanceImagePreviewWindow(match.getImage());
        window.setVisible(true);
    }//GEN-LAST:event_miniaturImagePanelMouseClicked
    public static BufferedImage resize(BufferedImage image, int width,
            int height) {
        if (image == null) {
            return null;
        }
        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel distnaceLabel;
    private javax.swing.JLabel globalConstraintsLabel;
    private gui.ImagePanel miniaturImagePanel;
    private javax.swing.JLabel wordLabel;
    // End of variables declaration//GEN-END:variables
}
