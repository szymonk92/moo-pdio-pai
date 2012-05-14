import java.io.File;

import WavFile.WavFile;

public class Main {

	public static void main(String[] args) {
		args = new String[1];
		args[0] = "artificial/easy/337Hz.wav";

		try {

			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(new File(args[0]));

			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();

			int signal_counter = 0;
			double[] signal = new double[(int) wavFile.getFramesRemaining()];

			// Create a buffer of 100 frames
			double[] buffer = new double[100 * numChannels];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			do {
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);

				// Loop through frames and look for minimum and maximum value
				for (int s = 0; s < framesRead * numChannels; s++) {
					signal[signal_counter * 100 + s] = buffer[s];
					if (buffer[s] > max)
						max = buffer[s];
					if (buffer[s] < min)
						min = buffer[s];
				}
				signal_counter++;

			} while (framesRead != 0);

			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			System.out.printf("Min: %f, Max: %f\n", min, max);

			Main m = new Main();
			PlotWave pw = new PlotWave();
//			pw.plot(signal, "sygnał wejściowy", wavFile.getSampleRate());

			// AMDF
			double[] d = new double[signal.length];
			for (int i = 1; i < signal.length; ++i) {
				for (int j = i; j < signal.length; ++j) {
					d[i] += Math.abs(signal[j] - signal[j - i]);
				}
			}
//			pw = new PlotWave();
//			pw.plot(d, "AMDF", wavFile.getSampleRate());

			// cepstrum analysis
			d = new double[signal.length];
			Complex[] csignal = new Complex[signal.length];
			for (int i = 0; i < signal.length; ++i)
				csignal[i] = new Complex(signal[i], 0);
			FFTTools.fft(csignal, 0);
			
			//cepstrum rzeczywiste i zespolone
			for (int i = 0; i < signal.length; ++i)
//				csignal[i] = csignal[i].log();
				csignal[i] = new Complex(Math.log(csignal[i].abs()), 0);
			FFTTools.fft(csignal,1);
			for (int i = 0; i < signal.length; ++i) {
				d[i]=csignal[i].re;
				if ( Double.isInfinite(d[i])) {
					d[i]=10;
				}
			}
			pw = new PlotWave();
			pw.plot(d, "Cepstrum", 0);
			

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}

}
