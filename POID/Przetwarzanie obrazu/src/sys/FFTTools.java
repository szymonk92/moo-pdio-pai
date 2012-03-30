/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz
 */
public class FFTTools {

    /**
     * Fast Fourier Transform
     */
    static public Complex[] simpleFFT(Complex[] in) {
        int N = in.length;


        if (N == 1) {
            return new Complex[]{new Complex(in[0].re(), in[0].im())};
        }

        int mask = 1;
        int bitsSet = 0;
        while (mask <= N) {
            if ((N & mask) > 0) {
                bitsSet++;
            }
            mask <<= 1;
        }

        if (bitsSet > 1) {
            throw new RuntimeException("Długość tablicy nie jest potęgą liczby 2");
        }


        Complex[] even = new Complex[N / 2];
        Complex[] odd = new Complex[N / 2];
        for (int i = 0; i < N / 2; i++) {
            even[i] = in[ 2 * i];
            odd[i] = in[ 2 * i + 1];
        }

        even = simpleFFT(even);
        odd = simpleFFT(odd);


        Complex[] out = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            float kth = -2.0f * (float) k * (float) Math.PI / (float) N;
            Complex wk = new Complex((float) Math.cos(kth), (float) Math.sin(kth));
            out[k] = even[k].plus(wk.times(odd[k]));
            out[k + N / 2] = even[k].minus(wk.times(odd[k]));
        }
        return out;
    }

    /**
     * Odwrotna Fast Fourier Transform
     */
    static public Complex[] inverseFFT(Complex[] in) {
        int N = in.length;
        float normalizationFactor = (float) (1.0 / ((double) N));
        Complex[] out = new Complex[N];

        for (int i = 0; i < N; i++) {
            out[i] = in[i].conjugate();
        }

        out = simpleFFT(out);

        for (int i = 0; i < N; i++) {
            out[i] = out[i].conjugate().times(normalizationFactor);
        }

        return out;
    }

    /**
     * Fast Fourier Transform dna tablicy dwuwymiarowej
     */
    static public Complex[][] simpleFFT(Complex[][] in) {
        int N = in.length;
        int M = in[0].length;

        Complex[][] out = new Complex[N][M];

        // rzędy
        for (int i = 0; i < N; i++) {
            out[i] = simpleFFT(in[i]);
        }

        // kolumny
        for (int i = 0; i < M; i++) {
            Complex[] tmp = new Complex[N];
            for (int j = 0; j < N; j++) {
                tmp[j] = out[j][i];
            }
            tmp = simpleFFT(tmp);
            for (int j = 0; j < N; j++) {
                out[j][i] = tmp[j];
            }
        }

        return out;
    }

    /**
     * Odwrotna Fast Fourier Transform dla tablicy
     */
    static public Complex[][] inverseFFT(Complex[][] in) {
        int N = in.length;
        int M = in[0].length;


        Complex[][] out = new Complex[N][M];

        // rzędy
        for (int i = 0; i < N; i++) {
            out[i] = inverseFFT(in[i]);
        }

        // kolumny
        for (int i = 0; i < M; i++) {
            Complex[] tmp = new Complex[N];
            for (int j = 0; j < N; j++) {
                tmp[j] = out[j][i];
            }
            tmp = inverseFFT(tmp);
            for (int j = 0; j < N; j++) {
                out[j][i] = tmp[j];
            }
        }

        return out;
    }
}
