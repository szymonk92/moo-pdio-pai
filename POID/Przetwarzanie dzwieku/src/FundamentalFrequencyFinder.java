import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import WavFile.WavFile;


public class FundamentalFrequencyFinder {
	
	public class Tuple extends Pair<Double,Integer> {
		
		double freq;
		int index;
		



		public Tuple(double freq, int index) {
			super();
			this.freq = freq;
			this.index = index;
		}

		@Override
		public Integer setValue(Integer value) {
			return null;
		}

		@Override
		public Double getLeft() {
			return freq;
		}

		@Override
		public Integer getRight() {
			return index;
		}
		
	}
	
	protected double[] signal;
	protected WavFile wavFile;
	protected double[][] d;
	
	
	
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
