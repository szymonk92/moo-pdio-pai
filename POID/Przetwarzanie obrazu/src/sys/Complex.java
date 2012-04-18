/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz
 *      &
 * @author Pawel
 */
public class Complex {

    /**
     * część rzeczywista
     */
    public float re;
    /**
     * cześć urojona
     */
    public float im;

    public Complex(float real, float imag) {
        re = real;
        im = imag;
    }
        public Complex(double real, double imag) {
        re = (float) real;
        im = (float) imag;
    }
        
        public static Complex fromPolar(double r, double theta) {
            Complex ret = new Complex(0,0);
            
            ret.re = (float) (r * Math.sin(theta));
            ret.im = (float) (r * Math.cos(theta));

            
            return ret;
        }

   
    public Complex(float real) {
        this(real, 0.0f);
    }

   
    public Complex() {
        this(0.0f, 0.0f);
    }

  
    public float abs() {
        return (float) Math.hypot(re, im);
    }

   
    public float phase() {
        return (float) Math.atan2(im, re);
    }

    
    public Complex plus(Complex b) {
        Complex a = this;
        float real = a.re + b.re;
        float imag = a.im + b.im;
        return new Complex(real, imag);
    }

   
    public Complex minus(Complex b) {
        Complex a = this;
        float real = a.re - b.re;
        float imag = a.im - b.im;
        return new Complex(real, imag);
    }

    
    public Complex times(Complex b) {
        Complex a = this;
        float real = a.re * b.re - a.im * b.im;
        float imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    
    public Complex times(float alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    
    public float re() {
        return re;
    }

    
    public float im() {
        return im;
    }

    /**
     * Liczba sprzężona
     */
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    
    static public float[] getAbs(Complex[] in) {
        float[] out = new float[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i].abs();
        }
        return out;
    }

   
    static public float[] getPhase(Complex[] in) {
        float[] out = new float[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i].phase();
        }
        return out;
    }

  
    static public float[][] getAbs(Complex[][] in) {
        float[][] out = new float[in.length][];
        for (int i = 0; i < in.length; i++) {
            out[i] = getAbs(in[i]);
        }
        return out;
    }

   
    static public float[][] getPhase(Complex[][] in) {
        float[][] out = new float[in.length][];
        for (int i = 0; i < in.length; i++) {
            out[i] = getPhase(in[i]);
        }
        return out;
    }

    static public float maxMagnitude(Complex[] signal) {
        float max=Float.MIN_VALUE;
        for( int i=0; i<signal.length; ++i) {
            max = Math.max(max,signal[i].abs());
        }
        return max;
    }
    static public float minMagnitude(Complex[] signal) {
        float min=Float.MAX_VALUE;
        for( int i=0; i<signal.length; ++i) {
            min = Math.min(min,signal[i].abs());
        }
        return min;
    }
    
    static public float maxMagnitude(Complex[][] signal) {
        float max=Float.MIN_VALUE;
        for( int i=0; i<signal.length; ++i) {
                max = Math.max(max,maxMagnitude(signal[i]));
            }
        return max;
    }
    
    static public float minMagnitude(Complex[][] signal) {
        float min=Float.MAX_VALUE;
        for( int i=0; i<signal.length; ++i) {
            min = Math.min(min,minMagnitude(signal[i]));
        }
        return min;
    }
    
}
