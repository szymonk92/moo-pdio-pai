import java.io.File;
import java.io.FileNotFoundException;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;


public class Main {
	
	
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
		
		
		int frameSize = 4096;
		
		double[] signal = readWavFile(input);
		
		hammingWindow(signal, frameSize);
		
		
		double[] window = new double[frameSize*2];
		for ( int i=0; i<frameSize; ++i) {
			window[i]=signal[i];
		}
		DoubleFFT_1D fft =new DoubleFFT_1D(frameSize);
		fft.realForwardFull(window);
		//mmfc
		Complex[] fftcomplex = Complex.floatComplex2Complex(window);
		double[] spectrum = Complex.getAbs(fftcomplex);
		
		
	}
	
	
	public static double melScale(double arg) {
		return 700.0d*(Math.pow(10.0d,arg/2595.0d)-1.0d);
	}
	
	
	public static void filter(double[] arr) {
		
		
	}
	
	
	public static void hammingWindow(double[] x,int frameSize) {
		double twopi = 8.0 * Math.atan(1.0);

        double arg = twopi / ((double) frameSize - 1.0);

        Complex[] csignal = new Complex[ frameSize];

        for (int i = 0; i < frameSize; ++i) {
            csignal[i] = new Complex(x[i] * (0.54 - 0.46 * Math.cos(arg * (double) i)), 0);
        }
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
			
			double[] buffer = new double[100 * numChannels];
			
			int k=0;
			ret = new double[(int)wavFile.getNumFrames()];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);

				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * numChannels ; s++)
				{
					ret[k*100+s]=buffer[s];
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
				}
				k++;
			}
			while (framesRead != 0);

			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			System.out.printf("Min: %f, Max: %f\n", min, max);
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
		return ret;
	}
	
}
