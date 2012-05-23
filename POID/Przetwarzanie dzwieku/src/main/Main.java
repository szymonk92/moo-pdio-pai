package main;

import gui.MainWindow;
import java.io.File;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.cli.*;
import sys.FilenameExtensionOnly;

public class Main {

    private static FilenameExtensionOnly WavFilesExtension = new FilenameExtensionOnly("wav");
    private static Options options;

    public static void main(String[] args) {

        CommandLineParser parser = new PosixParser();
        options = new Options();
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
        } catch (ParseException exp) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Audio", options);
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
}
