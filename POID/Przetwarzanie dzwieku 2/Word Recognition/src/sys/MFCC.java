/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import java.io.File;
import java.util.Arrays;

/**
 *
 * @author Paweł
 */
public class MFCC {

    int mFs;
    int mFrameSize;
    int mOverlap;
    static int mTriFilterNum = 20;
    static int mMfccNum = 12;
    WavFile wavFile;

    public double[][] compute(File input) {
        double[] signal = readWavFile(input);
        
        mFs = (int) wavFile.getSampleRate();
        mFrameSize = (int) (20.0 * mFs / 1000);
        mOverlap = (int) (17.5 * mFs / 1000);
        if(mFrameSize<=mOverlap ||signal.length<=mOverlap){
            return null;
        }
        double[][] frames = buffer(signal);
        double[][] mfcc = new double[frames.length][mMfccNum];
        for (int i = 0; i < frames.length; ++i) {
            mfcc[i] = frame2mfcc(frames[i]);
        }
        return mfcc;
    }

    public  double[][] buffer(double[] signal) {
        int step = mFrameSize - mOverlap;
        int frameCount = (int) ((double) (signal.length - mOverlap) / (double) (step));
        double[][] frames = new double[frameCount][mFrameSize];
        for (int i = 0; i < frameCount; ++i) {
            for (int j = 0; j < mFrameSize; ++j) {
                if (i * step + j > signal.length) {
                    frames[i][j] = 0;
                } else {
                    frames[i][j] = signal[i * step + j];
                }
            }
        }
        return frames;
    }

    public static double[] fft(double[] signal) {
        double[] ssignal = new double[signal.length * 2];
        for (int i = 0; i < signal.length; ++i) {
            ssignal[i] = signal[i];
        }
        DoubleFFT_1D fft = new DoubleFFT_1D(signal.length);
        fft.realForwardFull(ssignal);

        ssignal = Arrays.copyOfRange(ssignal, 0, ssignal.length / 2);

        double[] mag = Complex.getAbs(Complex.floatComplex2Complex(ssignal));

        double powerDb[] = new double[mag.length];

        for (int i = 0; i < mag.length; ++i) {
            powerDb[i] = 20.0d * Math.log(mag[i] + 1.0d);
        }
        return powerDb;
    }

    public static double lin2melFreq(double linFreq) {
        return 1125.0d * Math.log(1.0d + linFreq / 700.0d);
    }

    public static double mel2linFreq(double melFreq) {
        return 700.0d * (Math.exp(melFreq / 1125.0d) - 1.0d);
    }

    public double[][] getTriFilterBank() {
        double[][] freq = new double[3][mTriFilterNum];
        double f[] = new double[mTriFilterNum + 2];
        double fLow = 0, fHigh = mFs / 2;
        for (int i = 0; i < mTriFilterNum + 2; ++i) {
            f[i] = mel2linFreq(lin2melFreq(fLow) + (i) * (lin2melFreq(fHigh) - lin2melFreq(fLow)) / ((double) mTriFilterNum + 1.0d));
        }
        for (int i = 0; i < mTriFilterNum; ++i) {
            freq[0][mTriFilterNum - i - 1] = f[i];
            freq[1][mTriFilterNum - i - 1] = f[i + 1];
            freq[2][mTriFilterNum - i - 1] = f[i + 2];
        }
        return freq;
    }

    public  double[] trimf(double[] freq, double[][] filter, int k) {
        double[] ret = new double[freq.length];
        double a, b, c, x;
        for (int i = 0; i < freq.length; ++i) {
            x = freq[i];
            a = filter[0][k];
            b = filter[1][k];
            c = filter[2][k];
            if (x >= a && x <= b) {
                ret[i] = (x - a) / (b - a);
            } else if (x >= b && x <= c) {
                ret[i] = (c - x) / (c - b);
            } else {
                ret[i] = 0;
            }
//			ret[i]=Math.max(Math.min((x-a)/(b-a),(c-x)/(c-b)), 0);
        }
        return ret;
    }

    public  double[] frame2mfcc(double[] frame) {
        double[] mfcc = new double[mMfccNum];
        int frameSize = frame.length;

        hammingWindow(frame, frameSize);

        double freqStep = (double) mFs / frameSize;
        double[] fftFreq = new double[frameSize / 2];
        for (int i = 0; i < fftFreq.length; ++i) {
            fftFreq[i] = freqStep * i;
        }

        double fftPowerDb[] = fft(frame);
        double[][] triFilterBank = getTriFilterBank();

        double[] tbfCoef = new double[mTriFilterNum];

        for (int i = 0; i < mTriFilterNum; ++i) {
            double[] cof = trimf(fftFreq, triFilterBank, i);
            double dot = 0;
            for (int j = 0; j < fftPowerDb.length; ++j) {
                dot += fftPowerDb[j] * cof[j];
            }
            tbfCoef[i] = dot;
        }

        //DCT
        for (int n = 1; n <= mMfccNum; ++n) {
            for (int i = 0; i < mTriFilterNum; ++i) {
                mfcc[n - 1] += tbfCoef[i] * Math.cos(((double) n * Math.PI / mTriFilterNum) * ((double) i + 0.5));
            }
        }

        return mfcc;

    }

    public static double melScale(double arg) {
        return 700.0d * (Math.pow(10.0d, arg / 2595.0d) - 1.0d);
    }

    public static void hammingWindow(double[] x, int frameSize) {
        double twopi = 8.0 * Math.atan(1.0);
        final double a = 0.46;

        double arg = twopi / ((double) frameSize - 1.0);

        for (int i = 0; i < frameSize; ++i) {
            x[i] = x[i] * ((1.0 - a) - a * Math.cos(arg * (double) i));
        }
    }

    public  double[] readWavFile(File f) {
        double[] ret = null;
        try {
            wavFile = WavFile.openWavFile(f);
            int numChannels = wavFile.getNumChannels();
            double[] buffer = new double[100 * numChannels];
            ret = new double[(int) wavFile.getNumFrames() * numChannels];
            int framesRead;
            int k = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            double bit16 = Math.pow(2, 16);
            do {
                framesRead = wavFile.readFrames(buffer, 100);
                for (int s = 0; s < framesRead * numChannels; s++) {
                    ret[k * 100 + s] = buffer[s] * bit16;
                    if (buffer[s] > max) {
                        max = buffer[s];
                    }
                    if (buffer[s] < min) {
                        min = buffer[s];
                    }
                }
                k++;
            } while (framesRead != 0);
            wavFile.close();
        } catch (Exception e) {
            Messages.fatalError("MFCC Błąd: " + e.toString());
        }
        return ret;
    }
}
