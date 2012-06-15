/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import javax.swing.JFileChooser;
import sys.Messages;
import sys.SimpleAudioRecorder;
import sys.WavCorrection;

/**
 *
 * @author Lukasz
 */
public class MainWindow extends javax.swing.JFrame {

    List<String> wordSet;
    SimpleAudioRecorder recorder;
    File recordedFile;
    boolean spaceBarPressed;
    File dir;
    List<File> recordedFiles;
    SecureRandom random = new SecureRandom();
    int currentWord;
    boolean keyAvaible;
    TargetDataLine targetDataLine;
    AudioFormat audioFormat;
    DataLine.Info info;
    WavCorrection corrector;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        corrector = new WavCorrection();
        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
        info = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            Messages.error("Błąd: " + e.getMessage());
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (!keyAvaible) {
                    return false;
                }
                int keyCode = e.getKeyCode();
                int id = e.getID();
                if (keyCode == KeyEvent.VK_SPACE) {
                    if (!spaceBarPressed && id == KeyEvent.KEY_PRESSED) {
                        recorder = recordFile();
                        if (recorder != null) {
                            spaceBarPressed = true;
                            recorder.start();
                            delay(150);
                            recordInd.setEnabled(true);
                        }
                    }
                    if (spaceBarPressed && id == KeyEvent.KEY_RELEASED) {
                        spaceBarPressed = false;
                        if (recorder.recording) {
                            recorder.stopRecording();
                        }
                        recordInd.setEnabled(false);
                        delay(150);
                        corrector.rewriteWaveFile(recordedFiles.get(currentWord));
                        setButtonsRecorded();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setUp() {
        this.pathLabel.setText(dir.getAbsolutePath());
        this.jPanel2.setEnabled(true);
        loadWordSet();
        recordedFiles = new ArrayList<File>();
        currentWord = 0;
        setButtons();
        this.keyAvaible = true;
        this.recordLabel.setEnabled(true);
    }

    public void restart() {
        recordedFiles = new ArrayList<File>();
        currentWord = 0;
        setButtons();
        this.keyAvaible = true;
        this.recordLabel.setEnabled(true);
    }

    public void setButtons() {
        this.wordLabel.setText(wordSet.get(currentWord));
        this.recordInd.setEnabled(false);
        this.wordLabel.setEnabled(true);
        this.nextButton.setEnabled(recordedFiles.size() > currentWord);
        this.previousButton.setEnabled(currentWord != 0);
        this.playButton.setEnabled(!recordedFiles.isEmpty() && recordedFiles.size() > currentWord);
        this.newSetButton.setEnabled(!recordedFiles.isEmpty() && recordedFiles.size() == wordSet.size());
    }

    public void setButtonsRecorded() {
        this.previousButton.setEnabled(currentWord != 0);
        this.nextButton.setEnabled(currentWord != wordSet.size() - 1);
        this.playButton.setEnabled(true);
        this.newSetButton.setEnabled(recordedFiles.size() == wordSet.size());
    }

    public void nextWord() {
        if (currentWord == wordSet.size() - 1) {
            return;
        }
        currentWord = currentWord + 1;
        setButtons();
    }

    public void previousWord() {
        if (currentWord == 0) {
            return;
        }
        currentWord = currentWord - 1;
        this.wordLabel.setText(wordSet.get(currentWord));
        setButtons();
    }

    public void playSound(final File soundFile) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile.getAbsoluteFile());
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    Messages.error(e.getMessage());
                }
            }
        }).start();
    }

    private void loadWordSet() {
        try {
            String sConfigFile = "config/words";
            InputStream fstream = MainWindow.class.getClassLoader().getResourceAsStream(sConfigFile);
            if (fstream == null) {
                Messages.error("Błąd! Nie znaleziono pliku z konfiguracją.");
                System.exit(-1);
            }
            wordSet = new ArrayList<String>();
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                wordSet.add(strLine);
            }
            in.close();
        } catch (Exception e) {
            Messages.fatalError("Błąd: " + e.getMessage());
        }
    }

    public String randomFileName() {
        return new BigInteger(130, random).toString(16);
    }

    private SimpleAudioRecorder recordFile() {
        if (targetDataLine == null) {
            return null;
        }
        File savedir = new File(dir.getAbsolutePath() + File.separator + wordSet.get(currentWord));
        savedir.mkdir();
        File outputFile = null;
        if (!recordedFiles.isEmpty() && recordedFiles.size() > currentWord) {
            outputFile = recordedFiles.get(currentWord);
        } else {
            do {
                outputFile = new File(dir.getAbsolutePath() + File.separator + wordSet.get(currentWord) + File.separator + "record" + randomFileName() + ".wav");
            } while (outputFile.exists());
            recordedFiles.add(outputFile);
        }
        try {
            targetDataLine.open(audioFormat);
        } catch (LineUnavailableException ex) {
            Messages.info(ex.getMessage());
        }
        return new SimpleAudioRecorder(targetDataLine, AudioFileFormat.Type.WAVE, outputFile);
    }

    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Messages.error(ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openDirChooser = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        pathLabel = new javax.swing.JLabel();
        saveDirButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        wordLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        previousButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        recordInd = new javax.swing.JLabel();
        newSetButton = new javax.swing.JButton();
        recordLabel = new javax.swing.JLabel();

        openDirChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tworzenie zbioru");
        setMaximumSize(new java.awt.Dimension(520, 380));
        setMinimumSize(new java.awt.Dimension(520, 380));
        setName("mainForm");
        setPreferredSize(new java.awt.Dimension(520, 380));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ścieżka zapisu"));

        saveDirButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/open.png"))); // NOI18N
        saveDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDirButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveDirButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveDirButton)
                    .addComponent(pathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Nagrywanie"));
        jPanel2.setEnabled(false);

        wordLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        wordLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wordLabel.setText("słowo");
        wordLabel.setEnabled(false);
        wordLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel3.setLayout(new java.awt.GridLayout(1, 3, 60, 0));

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/previous.png"))); // NOI18N
        previousButton.setEnabled(false);
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });
        jPanel3.add(previousButton);

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play.png"))); // NOI18N
        playButton.setText("Play");
        playButton.setEnabled(false);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        jPanel3.add(playButton);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/next.png"))); // NOI18N
        nextButton.setActionCommand("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        jPanel3.add(nextButton);

        recordInd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/record.png"))); // NOI18N
        recordInd.setEnabled(false);

        newSetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/new.png"))); // NOI18N
        newSetButton.setText("Nowy set");
        newSetButton.setEnabled(false);
        newSetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSetButtonActionPerformed(evt);
            }
        });

        recordLabel.setText("Naciśnij i przytrzymaj Spacje aby nagrać...");
        recordLabel.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
            .addComponent(wordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(recordInd)
                        .addGap(82, 82, 82))
                    .addComponent(recordLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(newSetButton)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(wordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(recordInd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(recordLabel))
                    .addComponent(newSetButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDirButtonActionPerformed
        int returnVal = this.openDirChooser.showDialog(this, "Save path");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dir = this.openDirChooser.getSelectedFile();
            setUp();
        }
    }//GEN-LAST:event_saveDirButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        playSound(recordedFiles.get(this.currentWord));
    }//GEN-LAST:event_playButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        nextWord();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        previousWord();
    }//GEN-LAST:event_previousButtonActionPerformed

    private void newSetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSetButtonActionPerformed
        restart();
    }//GEN-LAST:event_newSetButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton newSetButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JFileChooser openDirChooser;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JButton playButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JLabel recordInd;
    private javax.swing.JLabel recordLabel;
    private javax.swing.JButton saveDirButton;
    private javax.swing.JLabel wordLabel;
    // End of variables declaration//GEN-END:variables
}
