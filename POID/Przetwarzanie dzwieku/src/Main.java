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

	public static void main(String[] args) {
//		 args = new String[1];

//		 args[0] = "artificial/med/90Hz.wav";
		// "seq/DWK_violin.wav"
		// "artificial/diff/1366Hz.wav"
		// "artificial/med/202Hz.wav"
		// "natural/viola/130Hz.wav"
		// "natural/viola/698Hz.wav"
		// "natural/flute/1779Hz.wav"
		// "artificial/diff/80Hz.wav"

		int WINDOW_WIDTH = 0;

		try {

			File path = new File(args[0]);

			
			LinkedList<File> files = new LinkedList<File>();
			
			if (path.isDirectory()) {
				for ( File f : path.listFiles()) {
					files.add(f);
				}
			} else 
				files.add(path);
			

			for (File f : files) {

				// Open the wav file specified as the first argument
				WavFile wavFile = WavFile.openWavFile(f);

				// Display information about the wav file
//				wavFile.display();

				int numChannels = wavFile.getNumChannels();

				int signal_counter = 0;
				double[] signal = new double[(int) wavFile.getFramesRemaining()];

				// Create a buffer of 100 frames
				double[] buffer = new double[100 * numChannels];

				int framesRead;

				do {
					framesRead = wavFile.readFrames(buffer, 100);
					for (int s = 0; s < framesRead * numChannels; s++) {
						signal[signal_counter * 100 + s] = buffer[s];

					}
					signal_counter++;

				} while (framesRead != 0);

				// Close the wavFile
				wavFile.close();

				double[][] d = new double[][] { signal };
				Main m = new Main();
				PlotWave pw = new PlotWave();
				String hz = f.getName().replaceAll("[^\\d]", "");

//				pw.plot(d, "sygnał wejściowy", 0);

				CepstrumAnalysis ca = new CepstrumAnalysis(signal, wavFile);
				FundamentalFrequencyFinder.Tuple max = ca.process();
				System.out.printf("max= %d | %d \t f= %.2f %.2f\n",(int)(wavFile.getSampleRate() / Float.parseFloat(hz)),
						max.getRight(), Float.parseFloat(hz) , max.getLeft() );

				PlotWave plot = ca.plot();
				
				System.in.read();
				
				plot.close();
				

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}

}
