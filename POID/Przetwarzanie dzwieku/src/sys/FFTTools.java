package sys;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.geom.Point2D;

/**
 *
 * @author Lukasz &
 * @author Pawel
 *
 */
public class FFTTools {

    final static public int pow_2[];

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
    	
    	int input_size=x.length;
    	
        int p = 0;

        for (int i = 0; pow_2[i] < x.length; i++, ++p);

        if (pow_2[p] > x.length) {
            Complex[] tmp_x = new Complex[pow_2[p]];
            int i=0;
            for( i=0; i<x.length;++i)
            	tmp_x[i]=x[i];
            for( ;i<pow_2[p]; ++i)
            	tmp_x[i]=new Complex(0,0);
            
            x=tmp_x;
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
                Complex tmp = new Complex(x[i].re, x[i].im);
                x[i] = x[j];
                x[j] = tmp;
            }
        }
        
        if ( inverse == 1)
        for (j = 0; j < x.length; ++j) {
            x[j] = x[j].times(1.0f / (float) (input_size));
        }
        
        if(input_size < x.length){
        	
        	Complex[] output = new Complex[input_size];
        	
        	for (j = 0; j < input_size; ++j) {
        		output[j]=x[j];
        	}
        	
        	x=output;
        }

    }


}
