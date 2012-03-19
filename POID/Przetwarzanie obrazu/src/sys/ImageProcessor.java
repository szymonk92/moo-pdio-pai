/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author Lukasz
 */
public class ImageProcessor extends SwingWorker<BufferedImage, Object> {

    private TabData data;
    private boolean Added;

    public ImageProcessor(TabData data, boolean Added) {
        this.data = data;
        this.Added = Added;
    }

    @Override
    protected BufferedImage doInBackground() {
        if (data.getFilters() == null || data.getFilters().isEmpty()) {
            return BufferedImageHelper.copy(data.getBaseImage());
        }
        BufferedImage tmp;
        setProgress(0);
        if (Added) {
            tmp = BufferedImageHelper.copy(data.getFilteredImage());
            tmp = data.getFilters().get(data.getFilters().size() - 1).processImage(tmp);
            setProgress(100);
        } else {
            tmp = BufferedImageHelper.copy(data.getBaseImage());
            int progressStep = 100 / data.getFilters().size();

            for (int i = 0; i < data.getFilters().size(); i++) {
                tmp = data.getFilters().get(i).processImage(tmp);
                int progressValue = this.getProgress() + progressStep;
                if (progressValue > 100) {
                    progressValue = 100;
                }
                if (progressValue < 0) {
                    progressValue = 0;
                }
                setProgress(progressValue);
            }
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
