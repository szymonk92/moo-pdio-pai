/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import javax.swing.JTable;
import javax.swing.SwingWorker;

/**
 *
 * @author Lukasz
 */
public class DiffWorker extends SwingWorker<double[], Object>{

    BufferedImage in, out;
    JTable table;
    int column;
    
    public DiffWorker(BufferedImage in, BufferedImage out, JTable table, int column) {
        this.in = in;
        this.out = out;
        this.table = table;
        this.column = column;
    }

    @Override
    protected double[] doInBackground() throws Exception {
        double[] result = new double[3];
        setProgress(0);
        for(int i =1; i<4; i++){
            result[i-1]= sys.DiffCalculator.calculateError(in,out,i);
        }
        return result;
    }
    
     @Override
    protected void done() {
        try {
            double[] result = get();
             for(int i =0; i<3; i++){
                table.getModel().setValueAt(result[i],i, column);
             }
        } catch (InterruptedException ex) {
            //Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
           //Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (java.util.concurrent.CancellationException ex) {
        }
        setProgress(100);
    }
}
