/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import WavFile.WavFile;
import WavFile.WavFileException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Lukasz
 */
public class TabData {

    public String name;
    public XYSeriesCollection dataset;
    public double[] frequencies;
    public int frameSize;
    private static double pi2 = 2.0 * Math.PI;

    public TabData(String name, XYSeriesCollection dataset, int frameSize, double[] frequencies) {
        this.name = name;
        this.dataset = dataset;
        this.frequencies = frequencies;
        this.frameSize = frameSize;
    }

    static public XYSeriesCollection generatePlotData(double[][] signal, long samplerate) {
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
        return tmpDataset;
    }

    public WavFile generateWaveFile(WavFile wavFile, File file) {
        if (frequencies == null || frameSize == 0) {
            return null;
        }
        double lastsinarg = 0;
        double[] buffer = new double[frameSize];
        WavFile outFile = null;
        try {
            outFile = WavFile.newWavFile(file, 1, (wavFile.getNumFrames() / frameSize) * frameSize, 16, wavFile.getSampleRate());
        } catch (IOException ex) {
            Logger.getLogger(TabData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WavFileException ex) {
            Logger.getLogger(TabData.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (outFile != null) {
            for (Double freq : frequencies) {
                double sini = (lastsinarg * (float) outFile.getSampleRate()) / (pi2 * freq) + 1;
                for (int s = 0; s < frameSize; s++, sini += 1.0) {
                    buffer[s] = Math.sin(pi2 * freq * sini / (float) outFile.getSampleRate());
                    lastsinarg = pi2 * freq * sini / (float) outFile.getSampleRate();
                }

                if (outFile != null) {
                    try {
                        outFile.writeFrames(buffer, frameSize);
                    } catch (IOException ex) {
                        Logger.getLogger(TabData.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (WavFileException ex) {
                        Logger.getLogger(TabData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            try {
                outFile.close();
            } catch (IOException ex) {
                Logger.getLogger(TabData.class.getName()).log(Level.SEVERE, null, ex);
            }
            return outFile;
        }
        return null;
    }
}
