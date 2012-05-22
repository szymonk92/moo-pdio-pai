/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Lukasz
 */
public class PlotData {

    public String name;
    public XYSeriesCollection dataset;

    public PlotData(String name, XYSeriesCollection dataset) {
        this.name = name;
        this.dataset = dataset;
    }

    static public PlotData generatePlotData(double[][] signal, String name, long samplerate) {
        XYSeries[] soundWave = new XYSeries[signal.length];
        for (int j = 0; j < signal.length; ++j) {
            soundWave[j] = new XYSeries("sygnał" + j);
            for (int i = 0; i < signal[0].length; ++i) {
                double index = (samplerate == 0) ? i : 1000.0 * (double) i / (double) samplerate;
                soundWave[j].add(index, signal[j][i]);
            }
        }
        XYSeriesCollection tmpDataset = new XYSeriesCollection();
        for (int j = 0; j < signal.length; ++j) {
            tmpDataset.addSeries(soundWave[j]);
        }
        return new PlotData(name, tmpDataset);
    }
}
