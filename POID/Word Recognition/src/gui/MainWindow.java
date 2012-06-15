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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import javax.swing.JFileChooser;
import sys.*;

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
    SecureRandom random = new SecureRandom();
    int currentWord;
    boolean keyAvaible;
    File tmpFile;
    Recognizer recognizer;
    TargetDataLine targetDataLine;
    AudioFormat audioFormat;
    DataLine.Info info;
    WavCorrection corrector;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        recognizer = new Recognizer();
        try {
            tmpFile = File.createTempFile("record", ".wav");
            tmpFile.deleteOnExit();
        } catch (IOException ex) {
            Messages.fatalError("Nie można utworzyć pliku tymczasowego. " + ex.getMessage());
        }
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
                        corrector.rewriteWaveFile(tmpFile);
                        wordLabel.setText(recognizer.recognize(tmpFile).word);
                        playButton.setEnabled(true);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Messages.error(ex.getMessage());
        }
    }

    public void setUp() {
        this.jPanel2.setEnabled(true);
        currentWord = 0;
        setButtons();

    }

    public void setButtons() {
        this.recordInd.setEnabled(false);
        this.wordLabel.setEnabled(true);
    }

    public void NextWord() {
        if (currentWord == wordSet.size() - 1) {
            return;
        }
        currentWord = currentWord + 1;
        setButtons();
    }

    public void PreviousWord() {
        if (currentWord == 0) {
            return;
        }
        currentWord = currentWord - 1;
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

    private SimpleAudioRecorder recordFile() {
        if (targetDataLine == null) {
            return null;
        }
        try {
            targetDataLine.open(audioFormat);
        } catch (LineUnavailableException ex) {
            Messages.info(ex.getMessage());
        }
        return new SimpleAudioRecorder(targetDataLine, AudioFileFormat.Type.WAVE, tmpFile);
    }

    public List<ClassFile> processDir(File input) {
        List<ClassFile> result = new ArrayList<ClassFile>();
        if (input.isDirectory()) {
            for (File file : input.listFiles()) {
                if (file.isDirectory()) {
                    result.addAll(processDir(file));
                } else {
                    result.add(new ClassFile(input.getName(), file));
                }
            }
        }

        return result;
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
        computeButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        wordLabel = new javax.swing.JLabel();
        recordInd = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        playButton = new javax.swing.JButton();

        openDirChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tworzenie zbioru");
        setMaximumSize(new java.awt.Dimension(520, 380));
        setMinimumSize(new java.awt.Dimension(520, 380));
        setName("mainForm");
        setPreferredSize(new java.awt.Dimension(520, 380));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ścieżka bazy"));

        saveDirButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/open.png"))); // NOI18N
        saveDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDirButtonActionPerformed(evt);
            }
        });

        computeButton.setText("Compute");
        computeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                computeButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(saveDirButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(computeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveDirButton)
                    .addComponent(pathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(computeButton)
                    .addComponent(saveButton))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Rozpoznawanie"));
        jPanel2.setEnabled(false);

        wordLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        wordLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wordLabel.setText("słowo");
        wordLabel.setEnabled(false);
        wordLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        recordInd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/record.png"))); // NOI18N
        recordInd.setEnabled(false);

        jLabel1.setText("Naciśnij i przytrzymaj Spacje aby nagrać...");
        jLabel1.setEnabled(false);

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play.png"))); // NOI18N
        playButton.setEnabled(false);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(wordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(recordInd)
                .addGap(18, 18, 18)
                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(165, 165, 165))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(jLabel1)
                .addContainerGap(128, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(wordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recordInd, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(playButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDirButtonActionPerformed
        int returnVal = this.openDirChooser.showDialog(this, "Save path");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dir = this.openDirChooser.getSelectedFile();
            this.pathLabel.setText(dir.getAbsolutePath());

        }
    }//GEN-LAST:event_saveDirButtonActionPerformed

    private void computeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_computeButtonActionPerformed
        if (dir == null) {
            return;
        }
        boolean test = false;
        for (File file : dir.listFiles()) {
            if (file.getName().equals("recognizer.words")) {
                test = true;
                try {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    recognizer.words = (List<Word>) ois.readObject();
                    fis.close();
                    ois.close();
                    if (recognizer.words != null) {
                        System.out.println("Wczytano!");
                    } else {
                        System.out.println("Wczytano null!");
                    }

                } catch (FileNotFoundException e) {
                    System.out.println(e);
                } catch (IOException e) {
                    System.out.println(e);
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                }
                break;
            }

        }
        if (!test) {
            List<ClassFile> files = processDir(dir);
            MFCC mfcc = new MFCC();
            for (ClassFile file : files) {
                recognizer.words.add(new Word(file.getClassName(), mfcc.compute(file.getFile())));
            }
        }
        setUp();
        this.keyAvaible = true;
    }//GEN-LAST:event_computeButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (dir == null || recognizer.words.isEmpty()) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(dir + File.separator + "recognizer.words");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(recognizer.words);
            fos.close();
            oos.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

    }//GEN-LAST:event_saveButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        playSound(tmpFile);
    }//GEN-LAST:event_playButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton computeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JFileChooser openDirChooser;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel recordInd;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveDirButton;
    private javax.swing.JLabel wordLabel;
    // End of variables declaration//GEN-END:variables
}
