package sys;

import WavFile.WavFile;
import java.util.*;

public class CepstrumAnalysis extends FundamentalFrequencyFinder {

    static double twopi = 8.0 * Math.atan(1.0);

    public CepstrumAnalysis(double[] signal, WavFile wavFile) {
        super(signal, wavFile);
        this.name = "Cepstrum";
        this.frameSize = 4096;
    }

    @Override
    public void process() {
        log = new StringBuilder();

        d = new double[2][signal.length];

        int frameCount = (int) Math.floor((double) wavFile.getNumFrames() / (double) frameSize);

        log.append("signal length: ").append(signal.length).append(newline);
        log.append("frameSize: ").append(frameSize).append(newline);
        log.append("frameCount: ").append(frameCount).append(newline);

        this.frequencies = new double[frameCount];
        Tuple[] results = new Tuple[frameCount];
        for (int f = 0; f < frameCount; f++) {
            int first = f * frameSize;
            if (first > signal.length) {
                first = signal.length - 1;
            }
            int last = (f + 1) * frameSize;
            if (last > signal.length) {
                last = signal.length;
            }
            results[f] = getF0(Arrays.copyOfRange(signal, first, last), first);
            log.append("Frame ").append(f+1).append(" frequency = ").append(df.format(results[f].freq)).append(" Hz ").append("at ").append(results[f].index).append(newline);
        }

        double sum = 0;
        int sum2 = 0;
        for (int i =0; i<results.length; i++) {
             this.frequencies[i] = results[i].freq;
             sum += results[i].freq;
             sum2 += results[i].index;
        }

        log.append("Average frequency = ").append(df.format(sum / frequencies.length)).append(" Hz ").append("at ").append(df.format(sum2 / frequencies.length)).append(newline);
    }

    public double[] avg(double[] x) {
        int avg = 5;
        for (int k = 0; k < 5; ++k) {
            for (int i = avg; i < x.length - avg; ++i) {
                for (int j = i - avg; j < i + avg; ++j) {
                    if (j != i) {
                        x[i] += x[j];
                    }
                }
                x[i] /= (double) avg * 2 + 1;
            }
        }
        double[] ssignal = new double[x.length];
        for (int i = 0; i < x.length; ++i) {
            ssignal[i] = x[i];
        }
        avg = 3;
        for (int k = 0; k < 10; ++k) {
            for (int i = avg; i < x.length - avg; ++i) {
                for (int j = i - avg; j < i + avg; ++j) {
                    if (j != i) {
                        ssignal[i] += ssignal[j];
                    }
                }
                ssignal[i] /= (double) avg * 2 + 1;
            }
        }

        for (int i = avg; i < x.length - avg; ++i) {
            x[i] += ssignal[i];
        }
        return x;
    }

    @Override
    public Tuple getF0(double[] x, int start) {

        x = avg(x);

        double arg = twopi / ((double) this.frameSize - 1.0);

        Complex[] csignal = new Complex[this.frameSize];

        for (int i = 0; i < this.frameSize; ++i) {
            csignal[i] = new Complex(x[i] * (0.54 - 0.46 * Math.cos(arg * (double) i)), 0);
        }

        FFTTools.fft(csignal, 0);

        // cepstrum rzeczywiste i zespolone
        for (int i = 0; i < csignal.length; ++i) {
            csignal[i] = new Complex(Math.log(Math.pow(csignal[i].abs(), 2) + 1), 0);
        }

        FFTTools.fft(csignal, 0);

        csignal = Arrays.copyOfRange(csignal, 0, this.frameSize / 2);

        double[] dd = new double[csignal.length];
        for (int i = 0; i < csignal.length; ++i) {
            int index = i + start;
            if (index < signal.length) {
                d[0][index] = Math.pow(csignal[i].abs(), 2);
                dd[i] = d[0][i];
            }
        }

        List<Integer> pperiod = new ArrayList<Integer>();

        // RANGE
        int range = 5;

        for (int i = range; i < dd.length - range; ++i) {
            int bigger = 0;
            // sprawdz czy jest to ,,dolina o zboczu wysokim na ,,range''
            // sprawdzamy wysokość, ale nie stromość zbocza - peaki są ostre
            for (int j = i - range; j < i + range; ++j) {
                if (dd[j] <= dd[i] && i != j) {
                    bigger++;
                }
            }
            // sprawdz czy zbocza sa tak wysokie jak to zalozylismy
            if (bigger == (range * 2) - 1) {
                pperiod.add(i);
            }
        }

        // odrzucanie wysokich ale peakow ale nie stromych
        // musza opadac w obu kierunkach - nisko
        for (ListIterator<Integer> iter = pperiod.listIterator(); iter.hasNext();) {
            int i = iter.next(), j = 0, k = 0;
            // szukamy najniższego wartosci na zboczu lewym
            while (i - j - 1 >= 0) {
                if ((dd[i - j - 1] <= dd[i - j])) {
                    ++j;
                } else {
                    break;
                }
            }
            // szukamy najnizszej wartosci na zboczu prawym
            while (((i + k + 1) < dd.length)) {
                if ((dd[i + k + 1] <= dd[i + k])) {
                    ++k;
                } else {
                    break;
                }
            }

            double maxmin = Math.max(dd[i - j], dd[i + k]);
            if (maxmin > dd[i] * 0.3) {
                iter.remove();
            }
        }

        // progowanie co do największego peaku
        int max_ind = Collections.max(pperiod, new MaxDataComparator(dd));

        for (ListIterator<Integer> it = pperiod.listIterator(); it.hasNext();) {
            Integer num = (Integer) it.next();
            if (dd[num] > dd[max_ind] * 0.4) {
                d[1][num + start] = dd[num];
            } else {
                it.remove();
            }
        }

        if (pperiod.size() > 3) {
            ListIterator<Integer> it;
            int prev, pprev;
            pprev = pperiod.get(0);
            prev = pperiod.get(1);
            LinkedList<Integer> rm = new LinkedList<Integer>();
            for (int i = 2; i < pperiod.size(); ++i) {
                int j = pperiod.get(i);
                //bo 1 nie jest liczba pierwsza, a dwa jest parzyste
                if ((dd[prev] < dd[j]) && (dd[prev] < dd[pprev]) && (Math.abs(dd[prev] - dd[j]) > (dd[j] * 0.03))) {
                    rm.add(prev);
                }
                pprev = prev;
                prev = j;

            }
            for (int i : rm) {
                for (it = pperiod.listIterator(); it.hasNext();) {
                    int j = it.next();
                    if (i == j) {
                        it.remove();
                        d[1][i + start] = 0;
                        break;
                    }
                }

            }
        }
        if (pperiod.isEmpty()) {
            return new Tuple(0.0,0);
        }
        int max_b, max_a;
        max_b = Collections.max(pperiod, new MaxDataComparator(dd));
        int a = 0, b = 0;
        while (pperiod.size() > 1) {
            for (ListIterator del = pperiod.listIterator(); del.hasNext();) {
                if ((Integer) del.next() == max_b) {
                    del.remove();
                    break;
                }
            }

            max_a = Collections.max(pperiod, new MaxDataComparator(dd));

            a = max_a;
            b = max_b;
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }

            for (ListIterator<Integer> it = pperiod.listIterator(); it.hasNext();) {
                Integer num = (Integer) it.next();
                if (num < a || num > b) {
                    it.remove();
                }
            }

            max_b = max_a;

        }

        max_ind = Math.abs(b - a);
        if (max_ind == 0 && pperiod.size() == 1) {
            max_ind = pperiod.get(0);
        }

        return new Tuple((double) wavFile.getSampleRate() / (double) max_ind,max_ind);
    }
}
