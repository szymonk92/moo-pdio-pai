
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;


public class Main {
	
	static int mFs, mFrameSize, mOverlap,
	mTriFilterNum=40,
	mMfccNum=20;
	static double hammingNormalize;
	
	
	
	public static void main(String[] args) {
//		fftTest();
		File input = null;
		
		args = new String[1];
		args[0]="seq/DWK_violin.wav";
		
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
		
		if (signal.length%256 > 0 ) {
			int p=0;
			while( signal.length> (256*p++));
			p--;
			signal = Arrays.copyOfRange(signal, 0, 256*p);
		}
		
		mFrameSize = (int) 512/*(20.0*mFs/1000)*/;
		mOverlap = (int) 256/*(17.5*mFs/1000)*/;
		
		
		
		double[][] frames = buffer2(signal);
		double[][] mfcc = new double[frames.length][mMfccNum];
		double[][] mfcc2 = null;
		
		MFCC mfcc_2 = new MFCC(mFs);
		
		
		System.out.println("mFrameSize:"+mFrameSize+"\nmOverlap:"+mOverlap+"\nframes"+frames.length);
		
		PlotWave pwv = new PlotWave();
		
		try {
			mfcc2=mfcc_2.process(signal);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for( int i=0; i<frames.length; ++i) {
			mfcc[i]= frame2mfcc(frames[i]);
			

		}
		
		for( int i=0; i<frames.length; ++i) {
			
//		pwv.plot(new double[][]{mfcc[i]}, "mfcc"+i, mFs);
//		delay(60);
		pwv.plot(new double[][]{mfcc2[i],mfcc[i]}, "mfcc"+i, mFs);
		delay(100);
		
		}
		
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
		
		for( int i=0; i<ssignal.length; ++i){
			ssignal[i]/=hammingNormalize*2;
		}
		double[] mag = Complex.getAbs(Complex.floatComplex2Complex(ssignal));
		
		double powerDb[] = new double[mag.length];
		
//		for( int i=0; i<mag.length; ++i){
//			powerDb[i]=20.0*Math.log(mag[i]+1.0d);
//		}
	
		return mag;
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
		double log10 = 10 * (1 / Math.log(10));
		double[] tbfCoef = new double[mTriFilterNum];
		
		for( int i=0; i<mTriFilterNum; ++i) {
			double[] cof = trimf(fftFreq,triFilterBank,i);
			double dot=0;
			for( int j=0; j<fftPowerDb.length; ++j) {
				dot+=fftPowerDb[j]*cof[j];
			}
			
			dot = dot < 1.0 ? 1.0: dot;
			dot=Math.log(dot+1.0);
//			dot*=log10;
			tbfCoef[i]=-dot;
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
        hammingNormalize=0;
        for (int i = 0; i < frameSize; ++i) {
            x[i] =  x[i] * ((1.0-a) - a * Math.cos(arg * (double) i));
            hammingNormalize+=((1.0-a) - a * Math.cos(arg * (double) i));
        }
	}
	
	
	public static double[][] buffer2(double[] signal) {
		
		
		int step = mFrameSize - mOverlap;
		int frameCount =(int)( (double)(signal.length - mOverlap)/(double)(step));
		double[][] frames = new double[frameCount][mFrameSize];
		for( int i =0; i< frameCount; ++i) {
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
	
	
	public static void fftTest() {
		double[] test=new double[]{1,2,3,4,5,6,7,8,9};
		DoubleFFT_1D fft = new DoubleFFT_1D(test.length);
		double[] test2= new double[test.length*2];
		for( int i=0; i<test.length; ++i)
			test2[i]=test[i];
		
		fft.realForwardFull(test2);
		
		test2 = Arrays.copyOfRange(test2, 0, test2.length/2); 
		
		test2 = Complex.getAbs(Complex.floatComplex2Complex(test2));
		for( int i=0; i<test2.length; ++i) {
			System.out.println(test2[i]);
		}
//		for( int i=0; i<test2.length/2; ++i) {
//			System.out.println(test2[2*i]+" "+test2[2*i+1]);
//		}
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
	
	
	
	public static void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
