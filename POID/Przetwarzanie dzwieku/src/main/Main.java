package main;

import WavFile.WavFile;
import gui.MainWindow;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.cli.*;
import sys.*;

public class Main {

    private static FilenameExtensionOnly WavFilesExtension = new FilenameExtensionOnly("wav");
    private static Options options;

    public static void main(String[] args) {

        //args = new String[1];
        //args[0] = "-p ";

        // args[0] += "artificial/med/90Hz.wav";
        // args[0] += "artificial/diff/1366Hz.wav"
        // args[0] += "natural/viola/130Hz.wav";
        // args[0] += "natural/viola/698Hz.wav"
        // args[0] += "natural/flute/1779Hz.wav"
        // args[0] += "artificial/diff/80Hz.wav"

        //args[1] = "-c";
        //args[2] = "-IHateGUI";
        //args[3] = "-a";
        
        
        CommandLineParser parser = new PosixParser();
        options = new Options();
        options.addOption("g", "IHateGUI", false, "run with minimal GUI.");
        options.addOption("o", "orginal", false, "show orginal sound graph. (only with IHateGUI)");
        options.addOption("p", "path", true, "file or dir with wave files.");
        options.addOption("c", "cepstrum", false, "proces cepstrum");
        options.addOption("a", "amdf", false, "proces amdf.");
        options.addOption("h", "help", false, "help.");
        
        try {
            File path = null;
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Audio", options);
                System.exit(1);
            }
            if (line.hasOption("path")) {
                File tmpPath = new File(line.getOptionValue("path").trim());
                if (tmpPath.exists()) {
                    path = tmpPath;
                } else {
                    path = tmpPath;
                    System.err.println("Podana ścieżka jest błędna.");
                }
            }
            if (line.hasOption("IHateGUI")) {
                try {
                    List<File> files = new ArrayList<File>();
                    if (path != null && path.exists()) {
                        if (path.isDirectory()) {
                            files.addAll(Arrays.asList(path.listFiles(WavFilesExtension)));
                        } else if (WavFilesExtension.accept(path, path.getName())) {
                            files.add(path);
                        }
                    }

                    if (files.isEmpty()) {
                        System.out.println("Błędnie podany plik w argumentach.");
                    }
                    for (File f : files) {

                        // Open the wav file specified as the first argument
                        WavFile wavFile = WavFile.openWavFile(f);

                        // Display information about the wav file
                        wavFile.display();

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
                            //System.arraycopy(buffer, 0, signal, signal_counter * 100, framesRead * numChannels);
                            signal_counter++;

                        } while (framesRead != 0);

                        // Close the wavFile
                        wavFile.close();
                        if (line.hasOption("orginal")) {
                            double[][] d = new double[][]{signal};
                            PlotWave pw = new PlotWave();
                            pw.plot(d, "sygnał wejściowy", 0);
                        }
                        String hz = f.getName().replaceAll("[^\\d]", "");
                        float hzF = hz.isEmpty() ? 0 : Float.parseFloat(hz);
                        if (line.hasOption("cepstrum")) {
                            CepstrumAnalysis ca = new CepstrumAnalysis(signal, wavFile);
                            Tuple max = ca.process();

                            PlotWave plot = ca.plot();
                        }
                        if (line.hasOption("amdf")) {
                            AMDF a = new AMDF(signal, wavFile);
                            Tuple max = a.process();
                            System.out.printf("max= %d | %d \t f= %.2f %.2f\n", hzF != 0 ? (int) (wavFile.getSampleRate() / hzF) : 0,
                                    max.getRight(), hzF, max.getLeft());
                            PlotWave plot = a.plot();
                        }
                        System.in.read();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e);
                }
            } else {
                try {
                    // Set System L&F
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                } catch (UnsupportedLookAndFeelException e) {
                    // handle exception
                } catch (ClassNotFoundException e) {
                    // handle exception
                } catch (InstantiationException e) {
                    // handle exception
                } catch (IllegalAccessException e) {
                    // handle exception
                }
                final MainWindow window = new MainWindow();
                if (path != null) {
                    window.processPath(path, line.hasOption("cepstrum"), line.hasOption("amdf"));
                }
                java.awt.EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        window.setVisible(true);
                    }
                });
            }
        } catch (ParseException exp) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Audio", options);
            System.out.println("Unexpected exception:" + exp.getMessage());
        }



    }
}
