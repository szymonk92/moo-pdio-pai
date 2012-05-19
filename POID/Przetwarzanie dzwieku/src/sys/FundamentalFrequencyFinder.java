package sys;

import WavFile.WavFile;

public abstract class FundamentalFrequencyFinder {

    protected double[] signal;
    protected WavFile wavFile;
    public double[][] d;

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
