package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Lukasz
 */
public class ImagePreviewPanel extends JPanel {

    BufferedImage image;

    public ImagePreviewPanel() {
    }

    /**
     * Creates new form ImagePreviewPanel
     */
    public ImagePreviewPanel(BufferedImage image) {
        this.image = image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;
            int x = (this.getWidth() - image.getWidth()) / 2;
            int y = (this.getHeight() - image.getHeight()) / 2;
            g2d.drawImage(image, x, y, null);
        }
    }
}
