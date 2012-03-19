/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import gui.NavigableImagePanel;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.jdesktop.observablecollections.ObservableList;

/**
 *
 * @author Lukasz
 */
public class ImageProcessor extends SwingWorker<BufferedImage, Object> {

    public ObservableList<IFilter> filters;
    public BufferedImage baseImage;
    public NavigableImagePanel imagePanel;

    @Override
    protected BufferedImage doInBackground() {
        if (filters == null || filters.isEmpty()) {
            return baseImage;
        }
        setProgress(0);
        int progressStep =  100 / filters.size();
        BufferedImage tmp = BufferedImageHelper.copy(baseImage);
        for (int i = 0; i < filters.size(); i++) {
            tmp = filters.get(i).processImage(tmp);
            int progressValue = this.getProgress() +  progressStep;
            if(progressValue>100) progressValue = 100;
            if(progressValue<0) progressValue = 0;
            setProgress(progressValue);
        }
        
        return tmp;
    }

    @Override
    protected void done() {
        if (imagePanel == null) {
            return;
        }
        try {
            imagePanel.setImage(get());
            imagePanel.repaint();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        setProgress(100);
    }
    
}
