/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import sys.LerningSetProcesor;
import sys.ThreadProcesor;

/**
 *
 * @author Lukasz
 */
public class MainWindow extends javax.swing.JFrame implements PropertyChangeListener {

    public JPanel columnpanel;
    FilenameFilter pngfilter;
    int procesed;
    int green;
    int red;
    int orange;
    int tags;
    int regions;
    int positiveRegions;
    int found;
    int positiveFound;
    List<ViewPanel> panels;
    File wekaModel;
    File tmpDir;
    Thread processor;
    File learningDir;

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
        pngfilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                String[] namesplit = name.split("\\.");
                return namesplit[namesplit.length - 1].equalsIgnoreCase("png");
            }
        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openFileChooser = new javax.swing.JFileChooser();
        openFileChooser2 = new javax.swing.JFileChooser();
        openFileChooser3 = new javax.swing.JFileChooser();
        folderPanel = new javax.swing.JPanel();
        loadFolderButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        folderLabel = new javax.swing.JLabel();
        pngFileLabel = new javax.swing.JLabel();
        xmlFileLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        folderNameLabel = new javax.swing.JLabel();
        pngCountLabel = new javax.swing.JLabel();
        xmlFileCountLabel = new javax.swing.JLabel();
        networkPanel = new javax.swing.JPanel();
        loadWekaButton = new javax.swing.JButton();
        wekaModelLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        gridPanel = new javax.swing.JPanel();
        Green = new javax.swing.JLabel();
        Regions = new javax.swing.JLabel();
        Red = new javax.swing.JLabel();
        Tags = new javax.swing.JLabel();
        Orange = new javax.swing.JLabel();
        Found = new javax.swing.JLabel();
        jFileChooser1 = new javax.swing.JFileChooser();
        jScrollPane = new javax.swing.JScrollPane();
        tmpPanel = new javax.swing.JPanel();
        setTmpButton = new javax.swing.JButton();
        tmplLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        startLerningTestButton = new javax.swing.JButton();
        learningDirButton = new javax.swing.JButton();
        learningLabel = new javax.swing.JLabel();

        openFileChooser.setCurrentDirectory(new java.io.File("E:\\Politechnika\\stopien_II\\Obliczenia_inteligentne"));
        openFileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        openFileChooser3.setCurrentDirectory(new java.io.File("D:\\test"));
        openFileChooser3.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        folderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Folder i pliki"));
        folderPanel.setPreferredSize(new java.awt.Dimension(500, 70));

        loadFolderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open_dir.png"))); // NOI18N
        loadFolderButton.setText("Open");
        loadFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFolderButtonActionPerformed(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridLayout(3, 1));

        folderLabel.setText("Wybrany folder:");
        jPanel2.add(folderLabel);

        pngFileLabel.setText("Ilość plików PNG:");
        jPanel2.add(pngFileLabel);

        xmlFileLabel.setText("Ilość plików XML:");
        jPanel2.add(xmlFileLabel);

        jPanel3.setPreferredSize(new java.awt.Dimension(300, 100));
        jPanel3.setLayout(new java.awt.GridLayout(3, 1));

        folderNameLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanel3.add(folderNameLabel);

        pngCountLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        pngCountLabel.setMaximumSize(new java.awt.Dimension(19, 14));
        pngCountLabel.setMinimumSize(new java.awt.Dimension(19, 14));
        pngCountLabel.setPreferredSize(new java.awt.Dimension(19, 14));
        jPanel3.add(pngCountLabel);

        xmlFileCountLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanel3.add(xmlFileCountLabel);

        javax.swing.GroupLayout folderPanelLayout = new javax.swing.GroupLayout(folderPanel);
        folderPanel.setLayout(folderPanelLayout);
        folderPanelLayout.setHorizontalGroup(
            folderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(folderPanelLayout.createSequentialGroup()
                .addComponent(loadFolderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        folderPanelLayout.setVerticalGroup(
            folderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loadFolderButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(folderPanelLayout.createSequentialGroup()
                .addGroup(folderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 1, Short.MAX_VALUE))
        );

        networkPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Sieć/klasyfikator"));

        loadWekaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open_dir.png"))); // NOI18N
        loadWekaButton.setText("Open");
        loadWekaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadWekaButtonActionPerformed(evt);
            }
        });

        wekaModelLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout networkPanelLayout = new javax.swing.GroupLayout(networkPanel);
        networkPanel.setLayout(networkPanelLayout);
        networkPanelLayout.setHorizontalGroup(
            networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(networkPanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(loadWekaButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wekaModelLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        networkPanelLayout.setVerticalGroup(
            networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(wekaModelLabel)
            .addComponent(loadWekaButton)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Wyniki"));

        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/start.gif"))); // NOI18N
        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        gridPanel.setLayout(new java.awt.GridLayout(3, 2));

        Green.setText("Green:");
        gridPanel.add(Green);

        Regions.setText("Regions:");
        gridPanel.add(Regions);

        Red.setText("Red:");
        gridPanel.add(Red);

        Tags.setText("Tags:");
        gridPanel.add(Tags);

        Orange.setText("Orange:");
        gridPanel.add(Orange);

        Found.setText("Found:");
        gridPanel.add(Found);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addGap(33, 33, 33)
                        .addComponent(gridPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(123, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gridPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(112, 112, 112)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tmpPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Folder tymczasowy"));

        setTmpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open_dir.png"))); // NOI18N
        setTmpButton.setText("Open");
        setTmpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTmpButtonActionPerformed(evt);
            }
        });

        tmplLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout tmpPanelLayout = new javax.swing.GroupLayout(tmpPanel);
        tmpPanel.setLayout(tmpPanelLayout);
        tmpPanelLayout.setHorizontalGroup(
            tmpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tmpPanelLayout.createSequentialGroup()
                .addComponent(setTmpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tmplLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tmpPanelLayout.setVerticalGroup(
            tmpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tmplLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(setTmpButton)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Learning set"));

        startLerningTestButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/start.gif"))); // NOI18N
        startLerningTestButton.setText("Build");
        startLerningTestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startLerningTestButtonActionPerformed(evt);
            }
        });

        learningDirButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open_dir.png"))); // NOI18N
        learningDirButton.setText("Set folder");
        learningDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                learningDirButtonActionPerformed(evt);
            }
        });

        learningLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        learningLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startLerningTestButton)
                .addGap(18, 18, 18)
                .addComponent(learningDirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(219, Short.MAX_VALUE)
                    .addComponent(learningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(startLerningTestButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(learningDirButton))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(learningLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(networkPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tmpPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(folderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
                    .addComponent(jScrollPane)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(folderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(networkPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tmpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed

        procesed = 0;
        green = 0;
        red = 0;
        orange = 0;
        tags = 0;
        regions = 0;
        positiveRegions = 0;
        found = 0;
        positiveFound = 0;
        setLabals();
        if (processor != null && processor.isAlive()) {
            processor.interrupt();
        }
        processor = new ThreadProcesor(panels, this.folderNameLabel.getText(), wekaModel, tmpDir);
        processor.start();
    }//GEN-LAST:event_startButtonActionPerformed

    private void loadWekaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadWekaButtonActionPerformed
         int returnVal = this.openFileChooser2.showDialog(this, "Open file");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            wekaModel = this.openFileChooser2.getSelectedFile();
            this.wekaModelLabel.setText(wekaModel.getAbsolutePath());
        }
    }//GEN-LAST:event_loadWekaButtonActionPerformed

    private void startLerningTestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startLerningTestButtonActionPerformed

        if(learningDir!=null){
        LerningSetProcesor lerningSetProcesor = new LerningSetProcesor(panels, this.folderNameLabel.getText(), learningDir);
        lerningSetProcesor.start();
        }
    }//GEN-LAST:event_startLerningTestButtonActionPerformed

    private void loadFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFolderButtonActionPerformed
      int returnVal = this.openFileChooser.showDialog(this, "Open dir");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.openFileChooser.getSelectedFile();
            if (file.isDirectory()) {
                String folderPath = file.getAbsolutePath();
                this.folderNameLabel.setText(folderPath);
                panels = new ArrayList<ViewPanel>();
                this.columnpanel.removeAll();
                int xmlCount = 0;
                File[] pngFiles = file.listFiles(pngfilter);
                for (File pngFile : pngFiles) {
                    File xml = new File(pngFile.getAbsolutePath() + ".con.xml");
                    String xmlFile = null;
                    if (xml.exists()) {
                        xmlFile = xml.getName();
                        xmlCount++;
                    }
                    ViewPanel panel = new ViewPanel(folderPath, pngFile.getName(), xmlFile);
                    panel.changeSupport.addPropertyChangeListener(this);
                    panels.add(panel);
                    this.columnpanel.add(panel);

                }
                this.pngCountLabel.setText(String.valueOf(panels.size()));
                this.xmlFileCountLabel.setText(String.valueOf(xmlCount));
                this.columnpanel.revalidate();

            }
            //this.drawPanel.image=new filters.ExtractionFilter().processImage(ImageIO.read(file));
            //this.drawPanel.repaint();


        }
        this.openFileChooser.setSelectedFile(null);        // TODO add your handling code here:
    }//GEN-LAST:event_loadFolderButtonActionPerformed

    private void setTmpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTmpButtonActionPerformed
       
         int returnVal = this.openFileChooser3.showDialog(this, "Open file");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            tmpDir = this.openFileChooser3.getSelectedFile();
            this.tmplLabel.setText(tmpDir.getAbsolutePath());
        }
       
    }//GEN-LAST:event_setTmpButtonActionPerformed

    private void learningDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_learningDirButtonActionPerformed
        int returnVal = this.openFileChooser3.showDialog(this, "Open file");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            learningDir = this.openFileChooser3.getSelectedFile();
            this.learningLabel.setText(learningDir.getAbsolutePath());
        }
    }//GEN-LAST:event_learningDirButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Found;
    private javax.swing.JLabel Green;
    private javax.swing.JLabel Orange;
    private javax.swing.JLabel Red;
    private javax.swing.JLabel Regions;
    private javax.swing.JLabel Tags;
    private javax.swing.JLabel folderLabel;
    private javax.swing.JLabel folderNameLabel;
    private javax.swing.JPanel folderPanel;
    private javax.swing.JPanel gridPanel;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JButton learningDirButton;
    private javax.swing.JLabel learningLabel;
    private javax.swing.JButton loadFolderButton;
    private javax.swing.JButton loadWekaButton;
    private javax.swing.JPanel networkPanel;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JFileChooser openFileChooser2;
    private javax.swing.JFileChooser openFileChooser3;
    private javax.swing.JLabel pngCountLabel;
    private javax.swing.JLabel pngFileLabel;
    private javax.swing.JButton setTmpButton;
    private javax.swing.JButton startButton;
    private javax.swing.JButton startLerningTestButton;
    private javax.swing.JPanel tmpPanel;
    private javax.swing.JLabel tmplLabel;
    private javax.swing.JLabel wekaModelLabel;
    private javax.swing.JLabel xmlFileCountLabel;
    private javax.swing.JLabel xmlFileLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object value = evt.getNewValue();
        if("result".equals(propertyName)){
            if(value.equals(1)){
                green++;
            }
            if(value.equals(0)){
                red++;
            }
            if(value.equals(2)){
                orange++;
            }
        }
        if("tags".equals(propertyName)){
            tags+=(Integer)value;
        }
        if("regions".equals(propertyName)){
            regions+=(Integer)value;
        }
         if("found".equals(propertyName)){
            found+=(Integer)value;
        }
          if("regionsResult".equals(propertyName)){
            positiveRegions+=(Integer)value;
        }
           if("foundResult".equals(propertyName)){
            positiveFound+=(Integer)value;
        }
        setLabals();
    }
    
    private void setLabals(){
        this.Green.setText("Green:"+green);
        this.Red.setText("Red:"+red);
        this.Orange.setText("Orange:"+orange);
        this.Tags.setText("Tags:"+tags);
        this.Regions.setText("Regions:"+regions+ " z czego "+positiveRegions+"/"+tags);
        this.Found.setText("Found:"+found+ " z czego "+positiveFound+"/"+tags);
    }
}
