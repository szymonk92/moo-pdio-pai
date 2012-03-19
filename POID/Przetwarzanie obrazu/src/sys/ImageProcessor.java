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

    public TabData data;
    public ObservableList<IFilter> filters;
    public BufferedImage baseImage;
    public NavigableImagePanel imagePanel;

    @Override
    protected BufferedImage doInBackground() {
        if (data.getFilters() == null || data.getFilters().isEmpty()) {
            return BufferedImageHelper.copy(data.getBaseImage());
        }
        setProgress(0);
        int progressStep =  100 / data.getFilters().size();
        BufferedImage tmp = BufferedImageHelper.copy(data.getBaseImage());
        for (int i = 0; i < data.getFilters().size(); i++) {
            tmp = data.getFilters().get(i).processImage(tmp);
            int progressValue = this.getProgress() +  progressStep;
            if(progressValue>100) progressValue = 100;
            if(progressValue<0) progressValue = 0;
            setProgress(progressValue);
        }
        
        return tmp;
    }

    @Override
    protected void done() {
        try {
            data.setFilteredImage(get());
        } catch (InterruptedException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        setProgress(100);
    }
    
}
