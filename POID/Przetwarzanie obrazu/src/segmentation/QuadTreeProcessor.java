/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.awt.image.BufferedImage;
import javax.swing.SwingWorker;

/**
 *
 * @author Lukasz
 */
public class QuadTreeProcessor extends SwingWorker<QuadTree, Object> {
    QuadTree tree;
    public QuadTreeProcessor(BufferedImage image){
      tree= new QuadTree(image);
    }
    @Override
    protected QuadTree doInBackground() throws Exception {
        tree.process();
        return tree;
    }
}
