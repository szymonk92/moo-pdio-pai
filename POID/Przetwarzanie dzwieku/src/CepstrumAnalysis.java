import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;


import WavFile.WavFile;


public class CepstrumAnalysis extends FundamentalFrequencyFinder {
	
	
	
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
	
	static public class MaxDataComp extends DataComp {
		
		public MaxDataComp(double[] d) {super(d);}

		@Override
		public int compare(Integer o1, Integer o2) {
			return (d[o1] > d[o2] ) ? 1 : -1 ;
		}
		
	}
	

	public CepstrumAnalysis(double[] signal, WavFile wavFile) {
		super(signal, wavFile);
	}
	
	@Override
	public double process(){
		
		int WINDOW_WIDTH = (int) (wavFile.getSampleRate()/8);
		 int p = 0;
		 for (; FFTTools.pow_2[p] < WINDOW_WIDTH; ++p);
		 if ( FFTTools.pow_2[p] != WINDOW_WIDTH )
			 WINDOW_WIDTH=FFTTools.pow_2[p];
		 System.out.println("Rozmiar okna:"+WINDOW_WIDTH);
	        
		double twopi = 8.0*Math.atan(1.0);            
		double arg = twopi/((double)WINDOW_WIDTH-1.0);
					
		// cepstrum analysis
		d = new double[3][WINDOW_WIDTH];
		
		Complex[] csignal = new Complex[WINDOW_WIDTH];
		for (int i = 0; i < WINDOW_WIDTH; ++i)
			//hammming window
			csignal[i] = new Complex(signal[i]*(0.54 - 0.46*Math.cos(arg*(double)i)), 0);
		
		FFTTools.fft(csignal, 0);
		
		//cepstrum rzeczywiste i zespolone
		for (int i = 0; i < csignal.length; ++i)
			//power spectrum
			//csignal[i] = new Complex(10.0*Math.log10(Math.pow(csignal[i].abs(),2)+1), 0);
			//complex spectrum
			//csignal[i] = csignal[i].log();
			//real spectrum
			csignal[i] = new Complex(10.0*Math.log10(csignal[i].abs()+1), 0);
		
		
		FFTTools.fft(csignal,0);
		
		csignal = Arrays.copyOfRange(csignal, 0, WINDOW_WIDTH/2);
		
//		for (int j=0; j<d.length; ++j)
		for (int i = 0; i < csignal.length; ++i) {
			//power cepstrum
			//d[0][i]=Math.pow(csignal[i].abs(),2);
			d[0][i]=csignal[i].abs();
		}
		
	
		double[] dd = d[0];
		LinkedList<Integer> pperiod = new LinkedList<Integer>();
		
		//RANGE
		int range =5;
		
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
		ListIterator<Integer> it = pperiod.listIterator();
		while (it.hasNext()) {
			Integer num = (Integer)it.next();
			if ( dd[num] > dd[max_ind]*0.33) {
//				System.out.println(num+" "+dd[num]);
				d[1][num]=dd[num];
			}
			else
				it.remove();
		}
		
		int max_b, max_a;
		max_b= max_ind;
		int a,b;
		do {
			for  (ListIterator del=pperiod.listIterator(); del.hasNext();)
				if ((Integer)del.next() == max_b) {
					del.remove();
					
					break;
				}

			max_a=Collections.max(pperiod,new MaxDataComp(dd));
			
				
			
			System.out.println(max_a+" "+max_b);
		
			it = pperiod.listIterator();
			a=max_a; b=max_b;
			if ( a > b) {
				int tmp = a;
				a=b;
				b=tmp;
			}

			while (it.hasNext()) {
				Integer num = (Integer)it.next();
				if ( num < a || num > b )
					it.remove();
				else {
//					System.out.println(num);
				}
			}
			
			System.out.println(pperiod.size());
			max_b=max_a;
		
		} while ( pperiod.size() > 2); 

		max_ind = Math.abs( b-a );

		System.out.println("MAX:"+max_ind + "Freq ~= "+
			(((double)wavFile.getSampleRate()/(double)max_ind)) + "Hz");
		

		return (((double)wavFile.getSampleRate()/(double)max_ind));
	}
	
	@Override
	public void plot() {
		new PlotWave().plot(d, "Cepstrum", 0);
	}

	
	
}
