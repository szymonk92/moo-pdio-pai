/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author Lukasz
 */
public class SimpleAudioRecorder extends Thread {

    private TargetDataLine targetDataLine;
    private AudioFileFormat.Type targetType;
    private AudioInputStream audioInputStream;
    private File outputFile;
    public boolean recording;

    public SimpleAudioRecorder(
            AudioFileFormat.Type targetType,
            File file) {
        AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, true);
        try {
            targetDataLine = AudioSystem.getTargetDataLine(format);

        } catch (LineUnavailableException ex) {
            Messages.error("Błąd: " + ex.getMessage());
        }
        audioInputStream = new AudioInputStream(targetDataLine);
        this.targetType = targetType;
        outputFile = file;
    }

    @Override
    public void start() {
        try {
            targetDataLine.open();
        } catch (LineUnavailableException ex) {
            Messages.error("Błąd: " + ex.getMessage());
        }
        targetDataLine.start();
        recording = true;
        super.start();
    }

    public void stopRecording() {
        targetDataLine.stop();
        recording = false;
    }

    @Override
    public void run() {
        try {
            AudioSystem.write(audioInputStream, targetType, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
