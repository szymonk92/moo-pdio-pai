package sys;

import WavFile.WavFile;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;


public class AMDF extends FundamentalFrequencyFinder {

	public AMDF(double[] signal, WavFile wavFile) {
		super(signal, wavFile);
	}	
	
	
	@Override
	public PlotWave plot() {
		PlotWave pw = new PlotWave();
		pw.plot(d, "AMDF", 0);
		return pw;
	}
	
	@Override
	public Tuple process() {
		
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
                
//		ListIterator it = pperiod.listIterator();
//		while (it.hasNext()) {
//			Integer num = (Integer)it.next();
//			System.out.println(num+" "+dd[num]);
//		}
                
		int min_ind = Collections.min(pperiod);
		
		System.out.println("MIN:"+min_ind + "Freq ~= "+
			(1.0f/((double)min_ind/(double)wavFile.getSampleRate())) + "Hz");
		
		return new Tuple((1.0f/((double)min_ind/(double)wavFile.getSampleRate())),min_ind);
	}

}
