import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;

import org.apache.commons.lang3.tuple.Pair;

import WavFile.WavFile;

public class Main {

	//Nie narzekaj na ten kod, on spełnia swoje zadanie jak najlepiej to tylko może ;)
	
	
	public static void main(String[] args) {
		args = new String[1];

		args[0] = "seq/KDF_piano.wav";
		// "seq/DWK_violin.wav"
		// "artificial/diff/1366Hz.wav"
		// "artificial/med/202Hz.wav"
		// "natural/viola/130Hz.wav"
		// "natural/viola/698Hz.wav"
		// "natural/flute/1779Hz.wav"
		// "artificial/diff/80Hz.wav"

		int WINDOW_WIDTH = 4096;

		try {

			File path = new File(args[0]);

			LinkedList<File> files = new LinkedList<File>();

			if (path.isDirectory()) {
				for (File f : path.listFiles()) {
					files.add(f);
				}
			} else
				files.add(path);

			for (File f : files) {
				
				double lastsinarg=0;
				

				// Open the wav file specified as the first argument
				WavFile wavFile = WavFile.openWavFile(f);
		
				WavFile outFile = WavFile.newWavFile(new File("synth-" + f.getName()), 1,
						(wavFile.getNumFrames() / WINDOW_WIDTH)*WINDOW_WIDTH, 16, wavFile.getSampleRate());

				// Display information about the wav file
				wavFile.display();

				int numChannels = wavFile.getNumChannels();

				int signal_counter = 0;
				double[] signal = new double[WINDOW_WIDTH];
				
				double[] all = new double[(int)wavFile.getNumFrames()]; 

				// Create a buffer of 100 frames
				double[] buffer = new double[WINDOW_WIDTH];

				int framesRead;

				do {
					framesRead = wavFile.readFrames(buffer, WINDOW_WIDTH);
					
					 for (int s = 0; s < framesRead * numChannels; s++) {
					 all[signal_counter * WINDOW_WIDTH + s] = buffer[s];
					
					 }
					 signal_counter++;

					if (framesRead == 0)
						break;
					if (framesRead == WINDOW_WIDTH)
						signal = buffer;
					else
						continue;

//					 PlotWave pw1 = new PlotWave();
//					 pw1.plot(new double[][]{signal}, "sygnał wejściowy 1",0);

					// filtr uśredniający

					int avg = 5;
					for (int k = 0; k < 5; ++k)
						for (int i = avg; i < signal.length - avg; ++i) {
							for (int j = i - avg; j < i + avg; ++j) {
								if (j != i)
									signal[i] += signal[j];
							}
							signal[i] /= (double) avg * 2 + 1;
						}
					double[] ssignal = new double[signal.length];
					for (int i = 0; i < signal.length; ++i) {
						ssignal[i] = signal[i];
					}
					avg = 3;
					for (int k = 0; k < 10; ++k)
						for (int i = avg; i < signal.length - avg; ++i) {
							for (int j = i - avg; j < i + avg; ++j) {
								if (j != i)
									ssignal[i] += ssignal[j];
							}
							ssignal[i] /= (double) avg * 2 + 1;
						}

					for (int i = avg; i < signal.length - avg; ++i) {
						signal[i] += ssignal[i];
					}

					double[][] d = new double[][] { signal };
					String hz = f.getName().replaceAll("[^\\d]", "");

					// PlotWave pw = new PlotWave();
					// pw.plot(new double[][]{signal}, "sygnał wejściowy 2", 0);

					CepstrumAnalysis ca = new CepstrumAnalysis(signal, wavFile);
					FundamentalFrequencyFinder.Tuple max;
					try {
					max = ca.process();
					} catch (Exception e) {
						continue;
					}
					System.out.println("peak:" + max.getRight() + "freq:"
							+ max.getLeft());
					// System.out.printf("max= %d | %d \t f= %.2f %.2f\n",(int)(wavFile.getSampleRate()
					// / Float.parseFloat(hz)),
					// max.getRight(), Float.parseFloat(hz) , max.getLeft() );

					// PlotWave plot = ca.plot();

//					System.in.read();

					// plot.close();
					// pw1.close();
					// pw.close();

					// Fill the buffer, one tone per channel
					double sini=(lastsinarg*(float)outFile.getSampleRate())/(2.0 * Math.PI * max.getLeft())+1;
					
					
					for (int s = 0; s < WINDOW_WIDTH; s++, sini+=1.0) {
						buffer[s] = Math.sin(2.0 * Math.PI * max.getLeft() * sini / (float) outFile.getSampleRate());
//						sini=sini%max.getRight();
						lastsinarg= 2.0 * Math.PI * max.getLeft() * sini / (float) outFile.getSampleRate();
					}

					// Write the buffer
					outFile.writeFrames(buffer, WINDOW_WIDTH);

				} while (framesRead != 0);

				
//				 PlotWave pw = new PlotWave();
//				 pw.plot(new double[][]{all}, "sygnał wejściowy ", 0);
				
				// Close the wavFile
				wavFile.close();

				// Close the wavFile
				outFile.close();

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}

}
