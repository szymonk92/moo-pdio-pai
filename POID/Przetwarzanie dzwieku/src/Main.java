import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

		args[0] = "natural/viola/698Hz.wav"; 
		//"seq/DWK_violin.wav"
		//"artificial/diff/1366Hz.wav"
		//"artificial/med/202Hz.wav"
		//"natural/viola/130Hz.wav"

		
		
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

			
			WINDOW_WIDTH = (int) ((wavFile.getNumFrames()/zerocross)*8);
			int pow = 0;
			for (; FFTTools.pow_2[pow] < WINDOW_WIDTH; pow++);
			if ( FFTTools.pow_2[pow] > WINDOW_WIDTH )
				WINDOW_WIDTH=FFTTools.pow_2[pow];
			
			System.out.println(WINDOW_WIDTH+" "+(WINDOW_WIDTH*0.1));
			
			// Close the wavFile
			wavFile.close();

			double[][] d = new double[][]{signal};
			Main m = new Main();
			PlotWave pw = new PlotWave();
//			pw.plot(d, "sygnał wejściowy", 0);

			// AMDF
			d = new double[2][signal.length];

//			BoundedQueue mins = new BoundedQueue((int)(WINDOW_WIDTH+WINDOW_WIDTH*0.2),new MinDataComp(d[0]));
			
			for (int i = 1; i < signal.length; ++i) {
				for (int j = i; j < signal.length; ++j) {
					d[0][i] += Math.abs(signal[j] - signal[j - i]);
				}
			}
						
			double dd[] = d[0];
			LinkedList<Integer> pperiod = new LinkedList<Integer>();
			//RANGE
			
			int range =12;
			
			System.out.println("RANGE"+range);
			for (int i = range; i < signal.length-range; ++i) {
				int bigger=0;
				for (int j = i-range; j < i+range; ++j) {
					if ( dd[j] > dd[i] && i!=j)
						bigger++;
				}
				if ( bigger == (range*2)-1) {
//					System.out.println(i);
					pperiod.add(i);
					i+=(range*2)-1;
				}
			}
			
			HashMap<Integer, Integer> period = new HashMap<Integer,Integer>();
			int[] periodicity = new int[pperiod.size()];
			
			System.out.println("periods"+pperiod.size());
			
			for( int i=0; i< pperiod.size(); ++i) {
					for( int j=i+1; j< pperiod.size(); ++j) {
						
						int p = pperiod.get(j) - pperiod.get(i);
						if ( p < 2000) {
							Integer now = period.get(p);
							if ( now == null)
								period.put(p, 1);
							else 
								period.put(p, now+1);
						}
					}
			}
			
			
			List<Integer> sortedKeys=new ArrayList<Integer>(period.keySet());
			Collections.sort(sortedKeys);
			
			

			Integer prev = sortedKeys.get(0);
			for ( int j=1; j<sortedKeys.size(); ++j) {
				Integer i = sortedKeys.get(j);
				
				if (  Math.abs(i-prev)<= 5 ) {
					int new_key = (prev+i)/2, new_val = period.get(prev)+period.get(i);
					period.remove(i);
					period.remove(prev);
					period.put(new_key, new_val);
					
					sortedKeys=new ArrayList<Integer>(period.keySet());
					Collections.sort(sortedKeys);
					j=0;
					prev= sortedKeys.get(0);
				}
				else 
					prev= i;
			}
			
			sortedKeys=new ArrayList<Integer>(period.keySet());
			Collections.sort(sortedKeys);
			
			for ( int l=0; l<sortedKeys.size(); ++l) {
				Integer i = sortedKeys.get(l);
				for (int k=l+1; k<sortedKeys.size(); ++k) {
					Integer j = sortedKeys.get(k);
					for( int a=-2; a <= 2; ++a) {
						
					if ( ((j+a) % i) == 0 ) 
					{
						//j jest wielokrotnoscia k
						//przypisz ilosc wystapien j do k
						int new_val = period.get(j)+period.get(i);
						period.remove(j);
						period.put(i, new_val);
						
						sortedKeys=new ArrayList<Integer>(period.keySet());
						Collections.sort(sortedKeys);
						l=-1;
						k=sortedKeys.size();
						break;
					}
				}
				}	
			}
			
			
			
			sortedKeys=new ArrayList<Integer>(period.keySet());
			Collections.sort(sortedKeys);
			
			
			int max_val=0;
			int min_ind= 44100;
			
			for( Integer i : sortedKeys) {
				System.out.println(i+" "+period.get(i));
				if ( period.get(i)>max_val){
					max_val =period.get(i);
					min_ind=i;
				}
			}
						
			
//			for( int i=0; i< pperiod.size(); ++i) {
//				if( pperiod.get(i)>0)
//					System.out.println(pperiod.get(i)+":"+periodicity[i]);
//			}
//			for( ListIterator<Integer> it = pperiod.listIterator(); it.hasNext(); ) {
//				if ((Integer)it.next() < 0)
//					it.remove();
//			}
			
			
			
						
			
//			for (int i = (int)(WINDOW_WIDTH*0.1); i < (signal.length-signal.length*0.9); ++i) {
//				mins.add(i);
//			}
			
//			Object[] arr = mins.toArray();
//			HashMap<Integer, Double> map = new HashMap<Integer,Double>();
//			while ( mins.size() > 0) {
//				map.put(mins.peek(), d[0][mins.peek()]);
//				mins.poll();
//			}
			
//			Iterator it = map.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry<Integer, Double> e = (Map.Entry<Integer, Double>)it.next();
//				System.out.println( e.getKey() + " " + e.getValue());
//			}
			
//			Arrays.sort(arr, new Comparator<Object>() {
//				@Override
//				public int compare(Object o1, Object o2) {
//					
//					return ((Integer)o1).compareTo((Integer)o2);
//				}
//			});
//			min_ind=(Integer)arr[0];
//			for( int i=1; i<arr.length; ++i) {
//				if ( d[0][(Integer)arr[i]] < d[0][(Integer)arr[i-1]]) {
//					min_ind = (Integer)arr[i];
//				} else break;
//			}
			

			System.out.println("MIN:"+min_ind + "Freq ~= "+
				(1.0f/((double)min_ind/(double)wavFile.getSampleRate())) + "Hz");
			
			
			pw = new PlotWave();
			pw.plot(d, "AMDF", 0/*wavFile.getSampleRate()*/);
			
			
			double twopi = 8.0*Math.atan(1.0);            
			double arg = twopi/((double)WINDOW_WIDTH-1.0);
						
			// cepstrum analysis
			
			d = new double[1][WINDOW_WIDTH];
			Complex[] csignal = new Complex[WINDOW_WIDTH];
			for (int i = 0; i < WINDOW_WIDTH; ++i)
				//hammming window
				csignal[i] = new Complex(signal[i]*(0.54 - 0.46*Math.cos(arg*(double)i)), 0);
			
			FFTTools.fft(csignal, 0);
			
			//cepstrum rzeczywiste i zespolone
			for (int i = 0; i < csignal.length; ++i)
//				csignal[i] = csignal[i].log();
				csignal[i] = new Complex(10.0*Math.log10(csignal[i].abs()+1), 0);
			
//			
			FFTTools.fft(csignal,1);
			
			csignal = Arrays.copyOfRange(csignal, 0, WINDOW_WIDTH/2);
			
			for (int j=0; j<d.length; ++j)
			for (int i = 0; i < csignal.length; ++i) {
				d[j][i]=csignal[i].abs();
//				if ( Double.isInfinite(d[j][i])) {
//					d[j][i]=10;
//				}
			}
			
			BoundedQueue maxs = new BoundedQueue(1000,new MaxDataComp(d[0]));
			
			
			for (int i =(int)( WINDOW_WIDTH*0.05); i < d[0].length; ++i) {
				maxs.add(i);
			}
			
			int max_ind= 0;

//			HashMap<Integer, Double> map = new HashMap<Integer,Double>();

			while ( maxs.size() > 0) {
//				map.put(maxs.peek(), d[0][maxs.peek()]);
//				System.out.println( (maxs.peek()) + " " + d[0][max_ind=maxs.poll()]);
				max_ind=maxs.poll();
			}
			
//			for (Map.Entry<Integer, Double> entry : map.entrySet()) {
//				System.out.println( entry.getKey() + " " + entry.getValue());
//			}


			System.out.println("MAX:"+max_ind + "Freq ~= "+
				(((double)wavFile.getSampleRate()/(double)max_ind)) + "Hz");
			
			
			pw = new PlotWave();
//			pw.plot(d, "Cepstrum", 0);
			

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}

}
