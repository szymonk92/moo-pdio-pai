import WavFile.WavFile;


public class FundamentalFrequencyFinder {
	
	protected double[] signal;
	protected WavFile wavFile;
	protected double[][] d;
	
	
	
	public FundamentalFrequencyFinder(double[] signal, WavFile wavFile) {
		super();
		this.signal = signal;
		this.wavFile = wavFile;
	}

	public void plot() {
		
	}
	
	public double process() {
		return -1;
	}

}
