/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import gui.SegmentationWindow;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 *
 * @author Lukasz
 */
public class QuadTreeProcessor extends SwingWorker<QuadTree, Object> {

    QuadTree tree;
    SegmentationWindow window;

    public QuadTreeProcessor(QuadTree tree) {
        this.tree = tree;
    }

    public QuadTreeProcessor(QuadTree quadTree, SegmentationWindow window) {
        this.tree = quadTree;
        this.window = window;
    }

    @Override
    protected QuadTree doInBackground() throws Exception {
        tree.process();
        return tree;
    }

    @Override
    protected void done() {
        if (window != null && !this.isCancelled()) {
            window.setQuadTree(tree);
        }
        setProgress(100);
    }
}
