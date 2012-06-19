/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import dtw.DTWData;
import dtw.DTWMatch;
import dtw.ItakuraConstraints;
import dtw.SakoeChibaContrsraints;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import sys.*;

/**
 *
 * @author Lukasz
 */
public class MainWindow extends javax.swing.JFrame {

    public JPanel columnpanel;
    List<String> wordSet;
    SimpleAudioRecorder recorder;
    File recordedFile;
    boolean spaceBarPressed;
    File dir;
    SecureRandom random = new SecureRandom();
    int currentWord;
    boolean keyAvaible;
    File tmpFile;
    File betterTmpFile;
    Recognizer recognizer;
    WavCorrection corrector;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        JPanel borderlaoutpanel = new JPanel();
        this.jScrollPane.setViewportView(borderlaoutpanel);
        borderlaoutpanel.setLayout(new BorderLayout(0, 0));
        columnpanel = new JPanel();
        borderlaoutpanel.add(columnpanel, BorderLayout.NORTH);
        columnpanel.setLayout(new GridLayout(0, 1, 0, 1));
        columnpanel.setBackground(Color.gray);
        recognizer = new Recognizer();
        MatchPreview.setDistanceImageGenerator(recognizer.getDistanceImageGenerator());
        try {
            betterTmpFile= File.createTempFile("record", ".wav");
            betterTmpFile.deleteOnExit();
            tmpFile = File.createTempFile("record", ".wav");
            tmpFile.deleteOnExit();
        } catch (IOException ex) {
            Messages.fatalError("Nie można utworzyć pliku tymczasowego. " + ex.getMessage());
        }
        corrector = new WavCorrection(betterTmpFile);
      
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
                            delay(500);
                            recorder.start();              
                            recordInd.setEnabled(true);
                        }
                    }
                    if (spaceBarPressed && id == KeyEvent.KEY_RELEASED) {
                        spaceBarPressed = false;
                        delay(100);
                        if (recorder.recording) {
                            recorder.stopRecording();
                        }
                        recordInd.setEnabled(false);
                        delay(500);
                        corrector.rewriteWaveFile(tmpFile);
                        DTWMatch recognized = recognizer.recognize(betterTmpFile);
                        if (recognized != null) {
                            wordLabel.setText(recognized.getData().getWord());
                        } else {
                            wordLabel.setText("nie rozpoznano");
                        }
                        columnpanel.removeAll();
                        for (DTWMatch match : recognizer.getMatchList()) {
                            columnpanel.add(match.getView());
                        }
                        playButton.setEnabled(true);
                        playButton1.setEnabled(true);
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
        return new SimpleAudioRecorder(AudioFileFormat.Type.WAVE, tmpFile);
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

    private void refreshRecognized(){
        if (recognizer.processed) {
            DTWMatch recognized = recognizer.refresh();
            if (recognized != null) {
                wordLabel.setText(recognized.getData().getWord());
            } else {
                wordLabel.setText("nie rozpoznano");
            }
            columnpanel.removeAll();
            for (DTWMatch match : recognizer.getMatchList()) {
                columnpanel.add(match.getView());
            }
            for (DTWMatch match : recognizer.getMatchList()) {
                match.getView().refershImage();
            }
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
        computeButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        wordLabel = new javax.swing.JLabel();
        recordInd = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        playButton = new javax.swing.JButton();
        playButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        openDirChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rozpoznawanie słów");
        setMaximumSize(new java.awt.Dimension(472, 567));
        setMinimumSize(new java.awt.Dimension(472, 567));
        setName("mainForm");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Baza słów"));

        saveDirButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/open.png"))); // NOI18N
        saveDirButton.setText("Wczytaj");
        saveDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDirButtonActionPerformed(evt);
            }
        });

        computeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/process.png"))); // NOI18N
        computeButton.setText("Generuj/Ładuj");
        computeButton.setEnabled(false);
        computeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                computeButtonActionPerformed(evt);
            }
        });

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        saveButton.setText("Zapisz");
        saveButton.setEnabled(false);
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
                .addComponent(saveDirButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(computeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(computeButton)
                            .addComponent(saveButton)))
                    .addComponent(saveDirButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        playButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play.png"))); // NOI18N
        playButton1.setEnabled(false);
        playButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(wordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recordInd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(playButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(wordLabel)
                        .addComponent(playButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(recordInd)
                    .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jLabel1))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Porównania"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jPanel3.add(jScrollPane, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Ustawienia DTW"));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Brak", "Itakura parallelogram", "Sakoe and Chiba" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Globalne ograniczenia:");

        jCheckBox1.setText("rysuj ograniczenia");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jPanel6.setLayout(new java.awt.GridLayout(2, 2));

        jLabel2.setText("Wartość r:");
        jPanel6.add(jLabel2);

        jTextField1.setText("40");
        jTextField1.setEnabled(false);
        jPanel6.add(jTextField1);

        jLabel4.setText("Próg:");
        jPanel6.add(jLabel4);

        jTextField2.setText("600");
        jPanel6.add(jTextField2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(268, 268, 268))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCheckBox1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDirButtonActionPerformed
        int returnVal = this.openDirChooser.showDialog(this, "Save path");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dir = this.openDirChooser.getSelectedFile();
            this.pathLabel.setText(dir.getAbsolutePath());
            this.computeButton.setEnabled(true);
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
                    recognizer.setWords((List<DTWData>) ois.readObject());
                    fis.close();
                    ois.close();
                    if (recognizer.getMatchList() != null) {
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
            List<DTWData> words = new ArrayList<DTWData>();
            for (ClassFile file : files) {
                words.add(new DTWData(file.getClassName(), mfcc.compute(file.getFile()), file.getFile()));

            }
            recognizer.setWords(words);
        }

        setUp();
        this.keyAvaible = true;
        this.saveButton.setEnabled(true);
    }//GEN-LAST:event_computeButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (dir == null || recognizer.getMatchList().isEmpty()) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(dir + File.separator + "recognizer.words");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(recognizer.getWords());
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

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        int index = this.jComboBox1.getSelectedIndex();
        switch (index) {
            case 0:
                this.jTextField1.setEnabled(false);
                this.recognizer.getDtw().setGlobalConstraints(null);
                break;
            case 1:
                this.jTextField1.setEnabled(false);
                this.recognizer.getDtw().setGlobalConstraints(new ItakuraConstraints());
                break;
            case 2:
                this.jTextField1.setEnabled(true);
                int value = 20;
                try {
                    value = Integer.parseInt(this.jTextField1.getText());
                } catch (Exception e) {
                }
                this.jTextField1.setText(String.valueOf(value));
                this.recognizer.getDtw().setGlobalConstraints(new SakoeChibaContrsraints(value));
                break;
        }
        refreshRecognized();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        this.recognizer.getDistanceImageGenerator().setDrawGlobalContstraints(this.jCheckBox1.isSelected());
        for (DTWMatch match : recognizer.getMatchList()) {
            match.getView().refershImage();
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (this.jTextField1.isEnabled()) {
            int value = 20;
            try {
                value = Integer.parseInt(this.jTextField1.getText());
            } catch (Exception e) {
            }
            this.jTextField1.setText(String.valueOf(value));
            ((SakoeChibaContrsraints) this.recognizer.getDtw().getGlobalConstraints()).setR(value);
 
        }
        double value = 0;
        try {
            value = Double.parseDouble(this.jTextField2.getText());
        } catch (Exception e) {
        }
        this.jTextField2.setText(String.valueOf(value));
        this.recognizer.getDtw().setThresholdLevel(value);
        refreshRecognized();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void playButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButton1ActionPerformed
       playSound(betterTmpFile);
    }//GEN-LAST:event_playButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton computeButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JFileChooser openDirChooser;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JButton playButton;
    private javax.swing.JButton playButton1;
    private javax.swing.JLabel recordInd;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveDirButton;
    private javax.swing.JLabel wordLabel;
    // End of variables declaration//GEN-END:variables
}
