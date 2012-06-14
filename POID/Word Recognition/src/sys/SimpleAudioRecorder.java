/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Lukasz
 */
public class SimpleAudioRecorder extends Thread {

    private TargetDataLine m_line;
    private AudioFileFormat.Type m_targetType;
    private AudioInputStream m_audioInputStream;
    private File m_outputFile;
    public boolean recording;

    public SimpleAudioRecorder(TargetDataLine line,
            AudioFileFormat.Type targetType,
            File file) {
        m_line = line;
        m_audioInputStream = new AudioInputStream(line);
        m_targetType = targetType;
        m_outputFile = file;
    }

    @Override
    public void start() {
        m_line.start();
        recording = true;
        super.start();
    }

    public void stopRecording() {
        m_line.stop();
        m_line.close();
        recording = false;
    }

    @Override
    public void run() {
        try {
            AudioSystem.write(m_audioInputStream, m_targetType, m_outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
