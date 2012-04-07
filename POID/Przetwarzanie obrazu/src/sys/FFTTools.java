/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz &
 * @author Pawel
 *
 */
public class FFTTools {

    final static private int pow_2[];

    static {
        pow_2 = new int[31];
        pow_2[0] = 1;
        for (int i = 1; i < 31; ++i) {
            pow_2[i] = pow_2[i - 1] * 2;
        }
    }

    /**
     * Fast Fourier Transform (decimation in frequency)
     *
     * @param inverse 1=inverse, 0=forward
     */
    static public void fft(Complex[] x, int inverse) {
        int p = 0;

        for (int i = 0; pow_2[i] < x.length; i++, ++p);
//    System.out.println(p+" "+x.length);
        if (pow_2[p] > x.length) {
            p--;
        }

        int Bp, Np, Npp, P, b, n, BaseE, BaseO;
        Bp = 1;
        Np = 1 << p;
        Complex e, o;

        for (P = 0; P < p; ++P) {
            Npp = Np >> 1;
            BaseE = 0;
            for (b = 0; b < Bp; ++b) {
                BaseO = BaseE + Npp;
                for (n = 0; n < Npp; ++n) {
                    double kth = (inverse == 1 ? -1.0 : 1.0) * -2.0f
                            * (double) n * Math.PI / (double) Np;
                    Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
//                System.out.print(p+" "+x.length+" "+(BaseE+n)+" "+(BaseO+n)+"\n");
                    e = x[BaseE + n].plus(x[BaseO + n]);
                    o = (x[BaseE + n].minus(x[BaseO + n])).times(wk);
                    x[BaseE + n] = e;
                    x[BaseO + n] = o;
                }
                BaseE += Np;
            }
            Bp = Bp << 1;
            Np = Np >> 1;
        }

        int bits = 0, i, j, k;

        for (i = 0; i < 31; i++) {
            if (pow_2[i] == x.length) {
                bits = i;
            }
        }

        for (i = 0; i < x.length; ++i) {
            j = 0;
            for (k = 0; k < bits; ++k) {
                if ((i & pow_2[k]) != 0) {
                    j += pow_2[bits - k - 1];
                }
            }

            if (j > i) {
                /**
                 * Only make "up" swaps
                 */
                Complex tmp = new Complex(x[i].re(), x[i].im());
                x[i] = x[j];
                x[j] = tmp;
            }
        }

    }

    /**
     * Fast Fourier Transform dna tablicy dwuwymiarowej
     */
    static public Complex[][] fft2(Complex[][] in) {
        int N = in.length;
        int M = in[0].length;

        Complex[][] out = new Complex[N][M];

        // rzędy
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; ++j) {
                out[i][j] = in[i][j];
            }
            fft(out[i], 0);
        }

        // kolumny
        for (int i = 0; i < M; i++) {
            Complex[] tmp = new Complex[N];
            for (int j = 0; j < N; j++) {
                tmp[j] = out[j][i];
            }
            fft(tmp, 0);
            for (int j = 0; j < N; j++) {
                out[j][i] = tmp[j];
            }
        }

        return out;
    }

    static public void ifft(Complex[] in) {
        fft(in, 1);
        for (int j = 0; j < in.length; ++j) {
            in[j] = in[j].times(1.0f / (float) (in.length));
        }

    }

    /**
     * Odwrotna Fast Fourier Transform dla tablicy
     */
    static public Complex[][] ifft2(Complex[][] in) {
        int N = in.length;
        int M = in[0].length;

        Complex[][] out = new Complex[N][M];

        // rzędy
        for (int i = 0; i < N; i++) {
            out[i] = in[i];
            ifft(out[i]);
        }

        // kolumny
        for (int i = 0; i < M; i++) {
            Complex[] tmp = new Complex[N];
            for (int j = 0; j < N; j++) {
                tmp[j] = out[j][i];
            }
            ifft(tmp);
            for (int j = 0; j < N; j++) {
                out[j][i] = tmp[j];
            }
        }

        return out;
    }

    /**
     *
     * @param signal input signal
     * @param cutoff - fraction of the highest magnitude/phase o cutt-off
     * @param outType 0 -magnitude , 1- phase
     * @return
     */
    static public Complex[][] low_passFilter(Complex[][] signal, double cutoff, double power) {
        Complex[][] out = new Complex[signal.length][signal[0].length];
        
        int ydim = signal.length, xdim = signal[0].length;
        cutoff=Math.sqrt(ydim*ydim + xdim*xdim)*cutoff;

        for (int v = 0; v < ydim; ++v) {
            for (int u = 0; u < xdim; ++u) {
                int dv=(v<ydim/2)? v : v-ydim;
                int du=(u<xdim/2)? u : u-xdim;
                float dist = (float) Math.sqrt(dv*dv+du*du);
                if (power > 0) {
                    out[v][u] = new Complex(signal[v][u].re(), signal[v][u].im());
                    double filter = 1.0d/(1.0d+Math.pow(dist/cutoff,2*power));
//                    double filter = Math.exp(-1*dist*dist/(2.0d*cutoff*cutoff));
                    out[v][u].re*=1-filter;
                    out[v][u].im*=1-filter;
                } else {
                    if (dist < cutoff) {
                        out[v][u] = new Complex(0.0, 0.0);
                    } else {
                        out[v][u] = new Complex(signal[v][u].re(), signal[v][u].im());
                    }
                }
            }
        }
        System.out.println("lowpass passed, cutoff"+cutoff);

        return out;
    }

    static public Complex[][] high_passFilter(Complex[][] signal, double cutoff,double power) {
        Complex[][] out = new Complex[signal.length][signal[0].length];
        
       int ydim = signal.length, xdim = signal[0].length;
        cutoff=Math.sqrt(ydim*ydim + xdim*xdim)*cutoff;

        for (int v = 0; v < ydim; ++v) {
            for (int u = 0; u < xdim; ++u) {
                int dv=(v<ydim/2)? v : v-ydim;
                int du=(u<xdim/2)? u : u-xdim;
                float dist = (float) Math.sqrt(dv*dv+du*du);
                if (power > 0) {
                    out[v][u] = new Complex(signal[v][u].re(), signal[v][u].im());
                    double filter = 1.0d/(1.0d+Math.pow(dist/cutoff,2*power));
//                    double filter = Math.exp(-1*dist*dist/(2.0d*cutoff*cutoff));
                    out[v][u].re*=filter;
                    out[v][u].im*=filter;
                } else {
                    if (dist > cutoff) {
                        out[v][u] = new Complex(0.0, 0.0);
                    } else {
                        out[v][u] = new Complex(signal[v][u].re(), signal[v][u].im());
                    }
                }
            }
        }
        System.out.println("highpass passed, cutoff"+cutoff);
        return out;
    }

    static public Complex[][] band_passFilter(Complex[][] signal, double bl, double bh, double power) {
    Complex[][] out = new Complex[signal.length][signal[0].length];
        
       int ydim = signal.length, xdim = signal[0].length;
        bl=Math.sqrt(ydim*ydim + xdim*xdim)*bl;
        bh=Math.sqrt(ydim*ydim + xdim*xdim)*bh;
        
        for (int v = 0; v < ydim; ++v) {
            for (int u = 0; u < xdim; ++u) {
                int dv=(v<ydim/2)? v : v-ydim;
                int du=(u<xdim/2)? u : u-xdim;
                float dist = (float) Math.sqrt(dv*dv+du*du);
                if (power > 0) {
                    out[v][u] = new Complex(signal[v][u].re(), signal[v][u].im());
                    double filterlow = 10.f - 1.0d/(1.0d+Math.pow(dist/bl,2*power));
                    double filterhigh = 1.0d/(1.0d+Math.pow(dist/bh,2*power));
//                    double filter = Math.exp(-1*dist*dist/(2.0d*cutoff*cutoff));
                    out[v][u].re*=filterlow*filterhigh;
                    out[v][u].im*=filterlow*filterhigh;
                } else {
                    if (dist > bl && dist < bh) {
                        out[v][u] = new Complex(signal[v][u].re(), signal[v][u].im());
                    } else {
                        out[v][u] = new Complex(0.0, 0.0);
                    }
                }
            }
        }
        return out;
    }

    static public Complex[][] band_stopFilter(Complex[][] signal, double bl, double bh, double power) {
    Complex[][] out = new Complex[signal.length][signal[0].length];
        
       int ydim = signal.length, xdim = signal[0].length;
        bl=Math.sqrt((ydim/2)*(ydim/2) + (xdim/2)*(xdim/2))*bl;
        bh=Math.sqrt((ydim/2)*(ydim/2) + (xdim/2)*(xdim/2))*bh;
        double width=bh-bl;
        
        for (int v = 0; v < ydim; ++v) {
            for (int u = 0; u < xdim; ++u) {
                int dv=(v<ydim/2)? v : v-ydim;
                int du=(u<xdim/2)? u : u-xdim;
                float dist = (float) Math.sqrt(dv*dv+du*du);
                if (power > 0) {
                    out[v][u] = new Complex(signal[v][u].re(), signal[v][u].im());
                    double filterlow = 10.f - 1.0d/(1.0d+Math.pow(dist/bh,2*power));
                    double filterhigh = 1.0d/(1.0d+Math.pow(dist/bl,2*power));
//                    double filter = Math.exp(-1*dist*dist/(2.0d*cutoff*cutoff));
                    out[v][u].re*=filterlow*filterhigh;
                    out[v][u].im*=filterlow*filterhigh;
                } else {
                    if (dist > bl && dist < bh ) {
                        out[v][u] = new Complex(0.0, 0.0);
                    } else {
                        out[v][u] = new Complex(signal[v][u].re(), signal[v][u].im());
                    }
                }
            }
        }
        return out;
    }

    /**
     * @desc http://homepages.inf.ed.ac.uk/rbf/HIPR2/pixlog.htm
     *
     * @param signal input signal
     * @param outType 0 -magnitude , 1- phase
     * @return output int array ready to display
     */
    public static int[][] logScale(Complex[][] signal, int outType) {
        int[][] out = new int[signal.length][signal[0].length];
        float c = 0.0f;
        for (int i = 0; i < signal.length; ++i) {
            for (int j = 0; j < signal[i].length; ++j) {
                c = Math.max(c,
                        (outType == 0 ? signal[i][j].abs() : signal[i][j].phase()));
            }
        }

        c = (255.0f) / (float) Math.log10(1 + c);

        for (int i = 0; i < signal.length; ++i) {
            for (int j = 0; j < signal[i].length; ++j) {
                out[i][j] = (int) (c
                        * Math.log10((outType == 0 ? signal[i][j].abs() : signal[i][j].phase())));
            }
        }

        return out;
    }

    public static int[][] magnitudeImage(Complex[][] signal) {
        return logScale(signal, 0);
    }

    public static int[][] phaseImage(Complex[][] signal) {
        return logScale(signal, 1);
    }

    /**
     * Zamień ćwiartki miejscami
     */
    public static void revertQuarters(Complex[][] data) {
        int halfWidth = data.length / 2;
        int halfHeight = data[0].length / 2;

        
            for (int i = 0; i < halfWidth; i++) {
                for (int j = 0; j < halfHeight; j++) {
                // drua z czwartą
                Complex tmp = data[i][j];
                data[i][j] = data[ halfHeight + i][ halfWidth + j];
                data[ halfHeight + i][ halfWidth + j] = tmp;

                // pierwsza z trzecią
                tmp = data[ i][ halfWidth + j];
                data[ i][ halfWidth + j] = data[ halfHeight + i][ j];
                data[ halfHeight + i][ j] = tmp;
            }
        }
    }

    public static void fft2dTest() {
        Complex[][] t = new Complex[][]{
            {new Complex(1, 0), new Complex(1, 0)},
            {new Complex(1, 0), new Complex(1, 0)},
            {new Complex(1, 0), new Complex(1, 0)},
            {new Complex(1, 0), new Complex(1, 0)},
            {new Complex(0, 0), new Complex(0, 0)},
            {new Complex(0, 0), new Complex(0, 0)},
            {new Complex(0, 0), new Complex(0, 0)},
            {new Complex(0, 0), new Complex(0, 0)}
        };

        t = FFTTools.fft2(t);
        System.out.println("FWD");
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                System.out.print("(" + t[i][j].re() + "," + t[i][j].im() + ")\n");
            }
        }
        t = FFTTools.ifft2(t);
        System.out.println("REV");
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                System.out.print("(" + t[i][j].re() + "," + t[i][j].im() + ")\n");
            }
        }
    }
}
