package sys;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 
 * @author Lukasz &
 * @author Pawel
 */
public class Complex {


	public float re, im;

	public Complex(float real, float imag) {
		re = real;
		im = imag;
	}

	public Complex(double real, double imag) {
		re = (float) real;
		im = (float) imag;
	}

	public static Complex fromPolar(double r, double theta) {
		Complex ret = new Complex(0, 0);

		ret.im = (float) (r * Math.sin(theta));
		ret.re = (float) (r * Math.cos(theta));

		return ret;
	}

	public Complex log() {
		return new Complex(Math.log(this.abs()), this.phase());
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

	/**
	 * Liczba sprzężona
	 */
	public Complex conjugate() {
		return new Complex(re, -im);
	}

}
