/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

/**
 *
 * @author Lukasz
 */
public class Complex {

    /**
     * część rzeczywista
     */
    private final float re;
    /**
     * cześć urojona
     */
    private final float im;

    public Complex(float real, float imag) {
        re = real;
        im = imag;
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

    /**
     * Zamień ćwiartki miejscami
     */
    static public void revertQuarters(Complex[][] data) {
        int halfHeight = data.length / 2;
        int halfWidth = data[0].length / 2;

        for (int i = 0; i < halfHeight; i++) {
            for (int j = 0; j < halfWidth / 2; j++) {
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
}
