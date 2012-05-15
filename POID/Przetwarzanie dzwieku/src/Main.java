import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;

import org.apache.commons.lang3.tuple.Pair;

import WavFile.WavFile;

public class Main {
	
	static public class DataComp implements  Comparator<Integer> {

		public double[] d;
		
		public DataComp(double[] d) {
			this.d = d;
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			return 0;
		}
	}
	
	static public class MinDataComp extends DataComp {
		

		public MinDataComp(double[] d) {super(d);}

		@Override
		public int compare(Integer o1, Integer o2) {
			return ( d[o1] < d[o2] ) ? 
					1
					: -1 ;
		}
		
	}
	
	static public class MaxDataComp extends DataComp {
		
		public MaxDataComp(double[] d) {super(d);}

		@Override
		public int compare(Integer o1, Integer o2) {
			return (d[o1] > d[o2] ) ? 1 : -1 ;
		}
		
	}
	
	static public class BoundedQueue extends PriorityQueue<Integer> {		
		
		public final int capacity;
		
		public BoundedQueue(int capacity, DataComp c) {
			super(capacity, c); 
			this.capacity = capacity;
		}
		@Override
		public boolean add(Integer e) {
			
			
//			for ( Integer i : this ) {
//					if ( Math.abs((e%i)-i)<3 )
//						return false;
//			}
			super.add(e);
			
			if ( this.size() > capacity)
				this.poll();
			
			
			return true;
		}
		
	}

	public static void main(String[] args) {
		args = new String[1];

		args[0] ="artificial/diff/1366Hz.wav"; 
		//"seq/DWK_violin.wav"
		//"artificial/diff/1366Hz.wav"
		//"artificial/med/202Hz.wav"
		//"natural/viola/130Hz.wav"
		//"natural/flute/1779Hz.wav"
		//"artificial/diff/80Hz.wav"


		
		int WINDOW_WIDTH = 0;


		try {
			
			

			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(new File(args[0]));

			// Display information about the wav file
			wavFile.display();

			int numChannels = wavFile.getNumChannels();

			int signal_counter = 0;
			double[] signal = new double[(int) wavFile.getFramesRemaining()];

			// Create a buffer of 100 frames
			double[] buffer = new double[100 * numChannels];

			int framesRead;
			
			int zerocross=0;
			double lastVal =0; 

			do {
				framesRead = wavFile.readFrames(buffer, 100);
				lastVal = buffer[0];
				for (int s = 0; s < framesRead * numChannels; s++) {
					signal[signal_counter * 100 + s] = buffer[s];
					if ( lastVal > 0 && buffer[s]< 0) {
						zerocross++;
					}
					lastVal=buffer[s];
				}
				signal_counter++;

			} while (framesRead != 0);

			
			// Close the wavFile
			wavFile.close();

			double[][] d = new double[][]{signal};
			Main m = new Main();
			PlotWave pw = new PlotWave();
			pw.plot(d, "sygnał wejściowy", 0);

			
			
			
			
			////
			//AMDF
			////
			
			
			
			
			
			d = new double[2][signal.length];

			
			for (int i = 1; i < signal.length; ++i) {
				for (int j = i; j < signal.length; ++j) {
					d[0][i] += Math.abs(signal[j] - signal[j - i]);
				}
			}
						
			double dd[] = d[0];
			LinkedList<Integer> pperiod = new LinkedList<Integer>();
			
			//RANGE
			int range =8;
			
			System.out.println("RANGE"+range);
			for (int i = range; i < wavFile.getNumFrames()/4 ; ++i) {
				int bigger=0;
				//sprawdz czy jest to ,,dolina o zboczu wysokim na ,,range''
				for (int j = i-range; j < i+range; ++j) {
					if ( dd[j] > dd[i] && i!=j)
						bigger++;
				}
				//sprawdz czy zbocza sa tak wysokie jak to zalozylismy
				if ( bigger == (range*2)-1) {
					boolean multi=false;
					//sprawdz czy i nie jest wielokrotnoscia ktorejs liczby juz bedacych na liscie
						for (int k=0; k<pperiod.size() && !multi ; ++k) {
							Integer j = pperiod.get(k);
							if (i < j) continue;
							
							for( int a=-3; a <= 3; ++a) {
								if ( ((i+a) % j) == 0 ) {
									multi=true;
									break;
								}
							}
						}
						
					if ( !multi ) {
						pperiod.add(i);
					}
					
					//usun ,,płytkie minima''
					int  max_depth=0;
					for (int k=0; k<pperiod.size(); ++k) {
						Integer j = pperiod.get(k);
						max_depth = (int) Math.max(dd[j+range-1]-dd[j], max_depth);
					}
					ListIterator it = pperiod.listIterator();
					while (it.hasNext()) {
						Integer num = (Integer)it.next();
						if ( dd[num-range-1]-dd[num] < ((double)max_depth)*0.8  ) {
							it.remove();
						}
					}
					i+=range-1;
				}
			}
			ListIterator it = pperiod.listIterator();
			while (it.hasNext()) {
				Integer num = (Integer)it.next();
				System.out.println(num+" "+dd[num]);
			}
			int min_ind = 0;
			min_ind = Collections.min(pperiod);
			

			System.out.println("MIN:"+min_ind + "Freq ~= "+
				(1.0f/((double)min_ind/(double)wavFile.getSampleRate())) + "Hz");
			
			
			pw = new PlotWave();
			pw.plot(d, "AMDF", 0);
			
			
			
			
			
			
			
			////
			//CEPSTRUM ANALYSIS
			////
			
			
			WINDOW_WIDTH = (int) (wavFile.getSampleRate()/4);
			 int p = 0;
			 for (; FFTTools.pow_2[p] < WINDOW_WIDTH; ++p);
			 if ( FFTTools.pow_2[p] != WINDOW_WIDTH )
				 WINDOW_WIDTH=FFTTools.pow_2[p];
			 
		        
			double twopi = 8.0*Math.atan(1.0);            
			double arg = twopi/((double)WINDOW_WIDTH-1.0);
						
			// cepstrum analysis
			d = new double[2][WINDOW_WIDTH];
			
			Complex[] csignal = new Complex[WINDOW_WIDTH];
			for (int i = 0; i < WINDOW_WIDTH; ++i)
				//hammming window
				csignal[i] = new Complex(signal[i]*(0.54 - 0.46*Math.cos(arg*(double)i)), 0);
			
			FFTTools.fft(csignal, 0);
			
			//cepstrum rzeczywiste i zespolone
			for (int i = 0; i < csignal.length; ++i)
				csignal[i] = new Complex(10.0*Math.log10(Math.pow(csignal[i].abs(),2)+1), 0);

			
			FFTTools.fft(csignal,0);
			
			csignal = Arrays.copyOfRange(csignal, 0, WINDOW_WIDTH/2);
			
//			for (int j=0; j<d.length; ++j)
			for (int i = 0; i < csignal.length; ++i) {
				d[0][i]=Math.pow(csignal[i].abs(),2);
			}
			
		
			dd = d[0];
			pperiod = new LinkedList<Integer>();
			
			//RANGE
			range =5;
			
			System.out.println("RANGE"+range);
			for (int i = range; i < dd.length-range ; ++i) {
				int bigger=0;
				//sprawdz czy jest to ,,dolina o zboczu wysokim na ,,range''
				for (int j = i-range; j < i+range; ++j) {
					if ( dd[j] < dd[i] && i!=j)
						bigger++;
				}
				//sprawdz czy zbocza sa tak wysokie jak to zalozylismy
				if ( bigger == (range*2)-1) {
					
						pperiod.add(i);
		
					i+=range-1;
				}
			}
			
			int max_ind = 0;
			
			max_ind = Collections.max(pperiod,new MaxDataComp(dd));
			it = pperiod.listIterator();
			while (it.hasNext()) {
				Integer num = (Integer)it.next();
				if ( dd[num] > dd[max_ind]*0.1) {
					System.out.println(num+" "+dd[num]);
					d[1][num]=dd[num];
				}
				else
					it.remove();
			}
			
			int max_b, max_a;
			max_a= max_ind;
			do {
				max_b=max_a;
				pperiod.remove((Object)max_b);
				max_a=Collections.max(pperiod,new MaxDataComp(dd));
			
				System.out.println(max_a+" "+max_b);
			
				it = pperiod.listIterator();
				while (it.hasNext()) {
					Integer num = (Integer)it.next();
					if ( num < max_a || num > max_b )
						it.remove();
					else {
						System.out.println(num);
					}
				}
			
			
			} while ( pperiod.size() > 2); 
	
			max_ind = Math.abs( max_b-max_a );

			System.out.println("MAX:"+max_ind + "Freq ~= "+
				(((double)wavFile.getSampleRate()/(double)max_ind)) + "Hz");
			
			
			pw = new PlotWave();
			pw.plot(d, "Cepstrum", 0);
			

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}

}
