package sys;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
            
            ret.im = (float) (r * Math.sin(theta));
            ret.re = (float) (r * Math.cos(theta));

            
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

    
    static public double[] getAbs(Complex[] in) {
        double[] out = new double[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i].abs();
        }
        return out;
    }

   
    static public double[] getPhase(Complex[] in) {
        double[] out = new double[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i].phase();
        }
        return out;
    }

    static public Complex[] floatComplex2Complex(double[] arr) {
    	Complex[] ret = new Complex[arr.length/2];
    	for(int i=0, j=0; i<arr.length/2; ++i) {
    		ret[i] = new Complex(arr[2*i], arr[2*i+1]);
    	}
    	return ret;
    }
    
    public static float[] Complex2floatComplex(Complex[] arr) {
    	float[] ret = new float[arr.length*2];
    	for( int i=0; i<arr.length; ++i) {
    		ret[i*2]=arr[i].re;
    		ret[i*2+1]=arr[i].im;
    	}
    	return ret;
    }


    
}
