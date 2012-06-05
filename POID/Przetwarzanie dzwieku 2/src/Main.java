package src;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;


public class Main {
	
	static int mFs, mFrameSize, mOverlap,
	mTriFilterNum=20,
	mMfccNum=12;
	
	
	public static void main(String[] args) {
		
		File input = null;
		
		if ( args.length==1) {
			try {
			input = new File(args[0]);
			} catch (NullPointerException e) {
				System.err.println("File not found");
				return;
			}
		} else {
			System.err.println("One arg : filepath");
			return;
		}
		
		
		
		double[] signal = readWavFile(input);
		
		mFrameSize = (int) (20.0*mFs/1000);
		mOverlap = (int) (17.5*mFs/1000);
		
		hammingWindow(signal, mFrameSize);
		
		
		
		
	}
	
	public static double[] fft(double[] signal) {
		int N = signal.length;
		
		double[] ssignal = new double[signal.length*2];
		for( int i=0; i<signal.length; ++i) {
			ssignal[i]=signal[i];
		}
		DoubleFFT_1D fft =new DoubleFFT_1D(signal.length);
		fft.realForwardFull(ssignal);
		
		ssignal = Arrays.copyOfRange(ssignal, 0, ssignal.length/2); 
		
		double[] mag = Complex.getAbs(Complex.floatComplex2Complex(ssignal));
		
		double powerDb[] = new double[mag.length];
		
		for( int i=0; i<mag.length; ++i){
			powerDb[i]=20.0d*Math.log(mag[i]+1.0d);
		}
	
		return powerDb;
	}
	
	public static double lin2melFreq(double linFreq) {
		return 1125.0d*Math.log(1.0d+linFreq/700.0d);
	}
	
	public static double  mel2linFreq(double melFreq) {
		return 700.0d*(Math.exp(melFreq/1125.0d)-1.0d);
	}     
	
	
	public static double[][] getTriFilterBank() {
		double[][] freq = new double[3][mTriFilterNum];
		double f[] = new double[mTriFilterNum+2];
		double fLow=0, fHigh=mFs/2;

		for( int i=0; i<mTriFilterNum+2; ++i) {
			   f[i]=mel2linFreq(lin2melFreq(fLow)+(i)*(lin2melFreq(fHigh)-lin2melFreq(fLow))/((double)mTriFilterNum+1.0d));	
		}
		 
		
		for( int i=0; i<mTriFilterNum; ++i) {
			freq[0][mTriFilterNum-i-1]=f[i];
			freq[1][mTriFilterNum-i-1]=f[i+1];
			freq[2][mTriFilterNum-i-1]=f[i+2];
			
		}
		return freq;

	}

	
	public static double[] trimf( double[] freq, double[][] filter, int k) {
		double[] ret = new double[freq.length];
		double a, b, c, x;
		for( int i=0; i<freq.length; ++i) {
			x=freq[i];
			a=filter[0][k];
			b=filter[1][k];
			c=filter[2][k];
			if ( x >= a && x<=b ) {
				ret[i]=(x-a)/(b-a);
			} else if ( x>=b && x<=c ) {
				ret[i]=(c-x)/(c-b);
			} else {
				ret[i]=0;
			}
//			ret[i]=Math.max(Math.min((x-a)/(b-a),(c-x)/(c-b)), 0);
		}
		return ret;
	}
	
	
	public static double[] frame2mfcc(double[] frame) {
		double[] mfcc = new double[mMfccNum];
		int frameSize=frame.length;
		
		hammingWindow(frame, frameSize);
		
		double[] fftFreq=null;
		double freqStep = (double)mFs/frameSize;
		fftFreq = new double[frameSize/2];
		for( int i =0; i< fftFreq.length; ++i) {
			fftFreq[i] = freqStep*i;
		}
		
		double fftPowerDb[] = fft(frame);
		double[][] triFilterBank = getTriFilterBank();
		
		double[] tbfCoef = new double[mTriFilterNum];
		
		for( int i=0; i<mTriFilterNum; ++i) {
			double[] cof = trimf(fftFreq,triFilterBank,i);
			double dot=0;
			for( int j=0; j<fftPowerDb.length; ++j) {
				dot+=fftPowerDb[j]*cof[j];
			}
			tbfCoef[i]=dot;
		}
		
		//DCT
		for( int n=1; n<=mMfccNum; ++n) {
			for( int i=0; i<mTriFilterNum; ++i) {
				mfcc[n-1]+=tbfCoef[i]*Math.cos(((double)n*Math.PI/mTriFilterNum)*((double)i+0.5));
			}
		}
		
		return mfcc;
		
	}
	
	
	public static double melScale(double arg) {
		return 700.0d*(Math.pow(10.0d,arg/2595.0d)-1.0d);
	}
		
	
	public static void hammingWindow(double[] x,int frameSize) {
		double twopi = 8.0 * Math.atan(1.0);
		final double a = 0.46;

        double arg = twopi / ((double) frameSize - 1.0);

        Complex[] csignal = new Complex[ frameSize];

        for (int i = 0; i < frameSize; ++i) {
            x[i] =  x[i] * ((1.0-a) - a * Math.cos(arg * (double) i));
        }
	}
	
	
	public static double[][] buffer2(double[] signal) {
		
		
		int step = mFrameSize - mOverlap;
		int frameCount =(int)( (double)(signal.length - mOverlap)/(double)(step));
		double[][] frames = new double[frameCount][mFrameSize];
		for( int i =0; i< frameCount; ++i) {
			int startIndex = i*step;
			for( int j=0; j<mFrameSize; ++j) {
				if (i*step+j > signal.length) {
					frames[i][j]=0;
				} else {
					frames[i][j]=signal[i*step+j];
				}
			}
		}
		
		return frames;
	}
	
	
	public static double[] readWavFile (File f) {
		double[] ret = null;
		try
		{
			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(f);

			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();
			
			// Create a buffer of 100 frames
			mFs = (int) wavFile.getSampleRate();
			double[] buffer = new double[100 * numChannels];
			
			int k=0;
			ret = new double[(int)wavFile.getNumFrames()];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			double bit16 = Math.pow(2, 16);

			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);

				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s++)
				{
					ret[k*100+s]=buffer[s]*bit16;
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
				}
				k++;
			}
			while (framesRead != 0);

			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			System.out.printf("name:"+f.getName()+"\nMin: %f, Max: %f\n", min, max);
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
		return ret;
	}
	
}
