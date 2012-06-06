/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import javax.swing.SwingWorker;

/**
 *
 * @author Lukasz
 */
public class QuadTreeProcessor extends SwingWorker<QuadTree, Object> {

    QuadTree tree;

    public QuadTreeProcessor(QuadTree tree) {
        this.tree = tree;
    }

    @Override
    protected QuadTree doInBackground() throws Exception {
        tree.process();
        return tree;
    }

    @Override
    protected void done() {
       
        setProgress(100);
    }
}
