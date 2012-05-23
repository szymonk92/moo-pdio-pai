package sys;

import WavFile.WavFile;
import java.text.DecimalFormat;

public abstract class FundamentalFrequencyFinder {

    protected double[] signal;
    protected WavFile wavFile;
    public double[][] d;
    public double[] frequencies;
    public StringBuilder log;
    public static DecimalFormat df = new DecimalFormat("#.##");
    int frameSize;
    public static String newline = System.getProperty("line.separator");
    public String name;
    public FundamentalFrequencyFinder(double[] signal, WavFile wavFile) {
        this.signal = signal;
        this.wavFile = wavFile;
        this.name = "No name";
    }

    public PlotWave plot() {
        PlotWave pw = new PlotWave();
        pw.plot(d, this.name, 0);
        return pw;
    }

    public abstract void process();
    
    public abstract double getF0(double[] x, int start);
}
