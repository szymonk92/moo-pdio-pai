package sys;

import WavFile.WavFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class AMDF extends FundamentalFrequencyFinder {

    int range = 8;

    public AMDF(double[] signal, WavFile wavFile) {
        super(signal, wavFile);
        this.frameSize = 4096;
        this.name = "AMDF";
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

    @Override
    public Tuple getF0(double[] x, int start) {
        List<LocalMinimum> pperiod = new ArrayList<LocalMinimum>();
        int n = x.length / 2;
        double[] thi = new double[n];
        for (int t = 0; t < n; t++) {
            for (int i = 0; i < n; i++) {
                thi[t] += Math.abs(x[i] - x[i + t]);
            }

            thi[t] = thi[t] / n;
        }
        double sum = 0;
        for (int t = 0; t < thi.length; t++) {
            sum += thi[t];
        }
        sum = sum / thi.length;
        for (int t = 0; t < thi.length; t++) {
            if (thi[t] > sum) {
                thi[t] = sum;
            }
            if (t + start < signal.length) {
                d[0][t + start] = thi[t];
            }
        }
        for (int t = range; t < (n - 1) - range; t++) {
            int bigger = 0;
            double max_depth = 0;
            for (int j = t - range; j < t + range; ++j) {
                if (t != j && thi[j] > thi[t]) {
                    bigger++;

                }
            }

            if (bigger == (range * 2) - 1) {
                boolean multi = false;
                for (LocalMinimum i : pperiod) {
                    if ((t % i.value) < 3) {
                        multi = true;
                        break;
                    }
                }
                if (!multi) {
                    LocalMinimum lc = new LocalMinimum(t, Math.max(thi[t + range - 1] - thi[t], thi[t - range + 1] - thi[t]));
                    pperiod.add(lc);
                    max_depth = Math.max(lc.depth, max_depth);
                }

                ListIterator it = pperiod.listIterator();
                while (it.hasNext()) {
                    LocalMinimum num = (LocalMinimum) it.next();
                    if (num.depth < max_depth * 0.8) {
                        it.remove();
                    }
                }
            }

        }
        int fT = Integer.MAX_VALUE;
        for (LocalMinimum lc : pperiod) {
            if (lc.value < fT) {
                fT = lc.value;
            }
        }

        if (fT < Integer.MAX_VALUE) {
            if (fT + start < signal.length) {
                d[1][fT + start] = thi[fT];
            }
            return new Tuple(wavFile.getSampleRate() / fT, fT);
        }

        return new Tuple(0.0,0);
    }

    public double getF0_basic(double[] x, int start) {
        int fT = 0;

        int n = x.length / 2;
        double[] thi = new double[n];
        for (int t = 0; t < n; t++) {
            for (int i = 0; i < n; i++) {
                thi[t] += Math.abs(x[i] - x[i + t]);
            }
            if (t + start < signal.length) {
                d[0][t + start] = thi[t];
            }
            thi[t] = thi[t] / n;
        }
        double peak = thi[0];
        for (int t = 1; t < (n - 1); t++) {
            if (((thi[t] - thi[t - 1]) < 0) && ((thi[t] - thi[t + 1]) < 0)) {
                if (thi[t] < (0.6 * peak)) {
                    if (fT == 0) {
                        fT = t;
                        if (t + start < signal.length) {
                            d[1][t + start] = 100;
                        }
                    }
                }
            }
            peak = Math.max(thi[t], peak);
        }

        if (fT > 0) {
            return (wavFile.getSampleRate() / fT);
        }

        return 0.0;
    }
}
