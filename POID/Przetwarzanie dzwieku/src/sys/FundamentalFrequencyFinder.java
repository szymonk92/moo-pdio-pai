package sys;

import WavFile.WavFile;

public abstract class FundamentalFrequencyFinder {

    protected double[] signal;
    protected WavFile wavFile;
    public double[][] d;
    public StringBuilder log;
    public static String newline = System.getProperty("line.separator");
    public FundamentalFrequencyFinder(double[] signal, WavFile wavFile) {
        super();
        this.signal = signal;
        this.wavFile = wavFile;
    }

    public PlotWave plot() {
        return null;
    }

    public Tuple process() {
        return null;
    }
}
