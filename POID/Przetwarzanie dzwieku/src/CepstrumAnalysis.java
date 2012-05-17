import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.commons.lang3.tuple.Pair;

import WavFile.WavFile;

public class CepstrumAnalysis extends FundamentalFrequencyFinder {

	static public class DataComp implements Comparator<Integer> {

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

		public MaxDataComp(double[] d) {
			super(d);
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			return (d[o1] > d[o2]) ? 1 : -1;
		}

	}

	public CepstrumAnalysis(double[] signal, WavFile wavFile) {
		super(signal, wavFile);
	}

	@Override
	public Tuple process() {

		int WINDOW_WIDTH = (int) (wavFile.getSampleRate() / 8);
		int p = 0;
		for (; FFTTools.pow_2[p] < WINDOW_WIDTH; ++p)
			;
		if (FFTTools.pow_2[p] != WINDOW_WIDTH)
			WINDOW_WIDTH = FFTTools.pow_2[p];
		// System.out.println("Rozmiar okna:"+WINDOW_WIDTH);

		double twopi = 8.0 * Math.atan(1.0);
		double arg = twopi / ((double) WINDOW_WIDTH - 1.0);

		// cepstrum analysis
		d = new double[2][WINDOW_WIDTH];

		Complex[] csignal = new Complex[WINDOW_WIDTH];
		for (int i = 0; i < WINDOW_WIDTH; ++i)
			// hammming window
			csignal[i] = new Complex(signal[i]
					* (0.54 - 0.46 * Math.cos(arg * (double) i)), 0);

		FFTTools.fft(csignal, 0);

		// cepstrum rzeczywiste i zespolone
		for (int i = 0; i < csignal.length; ++i)
			// power spectrum
			// csignal[i] = new
			// Complex(10.0*Math.log10(Math.pow(csignal[i].abs(),2)+1), 0);
			// complex spectrum
			// csignal[i] = csignal[i].log();
			// real spectrum
			csignal[i] = new Complex(10.0 * Math.log10(csignal[i].abs() + 1), 0);

		FFTTools.fft(csignal, 0);

		csignal = Arrays.copyOfRange(csignal, 0, WINDOW_WIDTH / 2);

		d = new double[2][csignal.length];

		// for (int j=0; j<d.length; ++j)
		for (int i = 0; i < csignal.length; ++i) {
			// power cepstrum
			// d[0][i]=Math.pow(csignal[i].abs(),2);
			d[0][i] = csignal[i].abs();
		}

		double[] dd = d[0];

		LinkedList<Integer> pperiod = new LinkedList<Integer>();

		// RANGE
		int range = 5;

		// System.out.println("RANGE"+range);
		for (int i = range; i < dd.length - range; ++i) {
			int bigger = 0;
			// sprawdz czy jest to ,,dolina o zboczu wysokim na ,,range''
			// sprawdzamy wysokość, ale nie stromość zbocza - peaki są ostre
			for (int j = i - range; j < i + range; ++j) {
				if (dd[j] <= dd[i] && i != j)
					bigger++;
			}
			// sprawdz czy zbocza sa tak wysokie jak to zalozylismy
			if (bigger == (range * 2) - 1) {

				pperiod.add(i);

				// i+=range-1;
			}
		}

		int max_ind = 0;

		// odrzucanie wysokich ale peakow ale nie stromych
		// musza opadac w obu kierunkach - nisko
		for (ListIterator<Integer> iter = pperiod.listIterator(); iter.hasNext();) {
			int i = iter.next(), j = 0, k = 0;
			// szukamy najniższego wartosci na zboczu lewym
			while (i - j - 1 >= 0) {
				if ((dd[i - j - 1] <= dd[i - j]))
					++j;
				else
					break;
			}
			// szukamy najnizszej wartosci na zboczu prawym
			while (((i + k + 1) < dd.length)) {
				if ((dd[i + k + 1] <= dd[i + k]))
					++k;
				else
					break;
			}

			double maxmin = Math.max(dd[i - j], dd[i + k]);
			if (maxmin > dd[i] * 0.2) {
				iter.remove();
				// System.out.println(i+ "+"+j+ " " +
				// dd[i-j]+" "+dd[i]+" "+dd[i+k]);
			}
			// else d[1][i]=dd[i];

		}
		

		// progowanie co do największego peaku
		max_ind = Collections.max(pperiod, new MaxDataComp(dd));

		for (ListIterator<Integer> it = pperiod.listIterator(); it.hasNext();) {
			Integer num = (Integer) it.next();
			if (dd[num] > dd[max_ind] * 0.4) {
				d[1][num] = dd[num];
			} else
				it.remove();
		}
		
		
		
		if (pperiod.size() > 3) {
			ListIterator<Integer> it = pperiod.listIterator();
			int prev,pprev;
			pprev = pperiod.get(0);
			prev = pperiod.get(1);
			LinkedList<Integer> rm = new LinkedList<Integer>();
			for ( int i=2 ; i< pperiod.size() ; ++i ) {
				int j = pperiod.get(i);
//				System.out.println(pprev+":"+ dd[pprev]+">"+dd[prev]+":"+prev+"<"+dd[j]+":"+j);
				if ( (dd[prev] < dd[j]) && (dd[prev] < dd[pprev]) &&
						(Math.abs(dd[prev]-dd[j]) > (dd[j]*0.03)) ) {//bo 1 nie jest liczba pierwsza, a dwa jest parzyste
//					System.out.println("rm:"+prev);
					rm.add(prev);
				} 
				pprev=prev;
				prev=j;
				
			}
			for ( int i : rm) {
				for (it = pperiod.listIterator(); it.hasNext();) {
					int j = it.next();
					if ( i == j ){
						it.remove();
						d[1][i]=0;
						break;
					}
				}
				
			}
		}
		
		

		int max_b, max_a;
		// max_b= max_ind;
		max_b = Collections.max(pperiod, new MaxDataComp(dd));
		int a = 0, b = 0;
		while (pperiod.size() > 1) {
			for (ListIterator del = pperiod.listIterator(); del.hasNext();)
				if ((Integer) del.next() == max_b) {
					del.remove();
					break;
				}

			max_a = Collections.max(pperiod, new MaxDataComp(dd));

			a = max_a;
			b = max_b;
			if (a > b) {
				int tmp = a;
				a = b;
				b = tmp;
			}
			System.out.println(a + " " + b);

			for (ListIterator<Integer> it = pperiod.listIterator(); it.hasNext();) {
				Integer num = (Integer) it.next();
				if (num < a || num > b)
					it.remove();
			}

			max_b = max_a;

		}

		max_ind = Math.abs(b - a);
		if (max_ind == 0 && pperiod.size() == 1)
			max_ind = pperiod.get(0);
		else
			System.out.println(a + " " + b);

		return new Tuple(((double) wavFile.getSampleRate() / (double) max_ind),max_ind);

	}

	@Override
	public PlotWave plot() {
		PlotWave pw = new PlotWave();
		pw.plot(d, "Cepstrum", 0);
		return pw;
	}

}
