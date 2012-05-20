/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import WavFile.WavFile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DateRange;
import sys.FundamentalFrequencyFinder;
import sys.PlotData;

/**
 *
 * @author Lukasz
 */
public class TabPanel extends javax.swing.JPanel {

    /**
     * Creates new form TabPanel
     */
    private static int SLIDER_DEFAULT_VALUE = 100;
    private NumberAxis domainAxis;
    public int lastValue;
    public double[] signal;
    public WavFile wavFile;
    List<PlotData> plots;
    private JFreeChart chart;

    public TabPanel(double[] signal, WavFile wavFile) {
        initComponents();
        this.processingLabel.setVisible(false);
        plots = new ArrayList<PlotData>();
        lastValue = SLIDER_DEFAULT_VALUE;
        this.signal = signal;
        this.wavFile = wavFile;
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                if (domainAxis != null) {
                    int value = slider.getValue();
                    double minimum = domainAxis.getRange().getLowerBound();
                    double maximum = domainAxis.getRange().getUpperBound();
                    double delta = (0.1f * (domainAxis.getRange().getLength()));
                    if (value < lastValue) { // left
                        minimum = minimum - delta;
                        maximum = maximum - delta;
                    } else { // right
                        minimum = minimum + delta;
                        maximum = maximum + delta;
                    }
                    DateRange range = new DateRange(minimum, maximum);
                    domainAxis.setRange(range);
                    lastValue = value;
                    if (lastValue == slider.getMinimum() || lastValue == slider.getMaximum()) {
                        slider.setValue(SLIDER_DEFAULT_VALUE);
                    }
                }
            }
        });
    }

    public void setProcessing(boolean b) {
        this.processingLabel.setVisible(b);
    }

    public void basicSetUp() {
        addPlotData(PlotData.generatePlotData(new double[][]{signal}, "Sygnał wejściowy", 0));
        if (!plots.isEmpty()) {
            plot(plots.get(0));
        }
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        wavFile.display(ps);
        try {
            appendLog(baos.toString("UTF-8")); // e.g. ISO-8859-1
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TabPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addPlotData(PlotData data) {
        plots.add(data);
        this.jComboBox.addItem(data.name);
        this.jComboBox.setSelectedIndex(this.jComboBox.getItemCount() - 1);
    }

    public void appendLog(String log) {
        this.jLogTextArea.append(log + FundamentalFrequencyFinder.newline);
    }

    public void plot(PlotData data) {
        this.chart =
                ChartFactory.createXYLineChart(
                data.name,
                "próbka",
                "wartość",
                data.dataset,
                PlotOrientation.VERTICAL,
                true, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();

        domainAxis = (NumberAxis) plot.getDomainAxis();
        plot.addRangeMarker(new ValueMarker(0, Color.BLACK, new BasicStroke(1)));

        ChartPanel chartPanel = new ChartPanel(chart);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createEtchedBorder());
        chartPanel.setBorder(border);

        chartPanel.addMouseWheelListener(addZoomWheel());

        plotPanel.removeAll();
        plotPanel.add(chartPanel);
        plotPanel.revalidate();
    }

    private MouseWheelListener addZoomWheel() {
        return new MouseWheelListener() {

            private void zoomChartAxis(ChartPanel chartP, boolean increase) {
                int width = chartP.getMaximumDrawWidth() - chartP.getMinimumDrawWidth();
                int height = chartP.getMaximumDrawHeight() - chartP.getMinimumDrawWidth();
                if (increase) {
                    chartP.zoomInDomain(width / 2, height / 2);
                } else {
                    chartP.zoomOutDomain(width / 2, height / 2);
                }
                lastValue = SLIDER_DEFAULT_VALUE;
                slider.setValue(lastValue);

            }

            public synchronized void decreaseZoom(JComponent chart, boolean saveAction) {
                ChartPanel ch = (ChartPanel) chart;
                zoomChartAxis(ch, false);
            }

            public synchronized void increaseZoom(JComponent chart, boolean saveAction) {
                ChartPanel ch = (ChartPanel) chart;
                zoomChartAxis(ch, true);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    return;
                }
                if (e.getWheelRotation() < 0) {
                    increaseZoom((ChartPanel) e.getComponent(), true);
                } else {
                    decreaseZoom((ChartPanel) e.getComponent(), true);
                }
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

        upPanel = new javax.swing.JPanel();
        jComboBox = new javax.swing.JComboBox();
        jLabel = new javax.swing.JLabel();
        processingLabel = new javax.swing.JLabel();
        plotPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        slider = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLogTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        upPanel.setPreferredSize(new java.awt.Dimension(30, 30));

        jComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxActionPerformed(evt);
            }
        });

        jLabel.setText("Graph:");

        processingLabel.setText("Processing...");

        javax.swing.GroupLayout upPanelLayout = new javax.swing.GroupLayout(upPanel);
        upPanel.setLayout(upPanelLayout);
        upPanelLayout.setHorizontalGroup(
            upPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(processingLabel)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        upPanelLayout.setVerticalGroup(
            upPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, upPanelLayout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addGroup(upPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel)
                    .addComponent(processingLabel)))
        );

        add(upPanel, java.awt.BorderLayout.NORTH);

        plotPanel.setLayout(new java.awt.BorderLayout());
        add(plotPanel, java.awt.BorderLayout.CENTER);

        jPanel1.setMinimumSize(new java.awt.Dimension(40, 100));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(slider, java.awt.BorderLayout.PAGE_START);

        jLogTextArea.setColumns(20);
        jLogTextArea.setEditable(false);
        jLogTextArea.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jLogTextArea.setRows(5);
        jScrollPane1.setViewportView(jLogTextArea);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxActionPerformed
        int index = this.jComboBox.getSelectedIndex();
        if (index >= 0 && index < plots.size()) {
            plot(plots.get(index));
        }
    }//GEN-LAST:event_jComboBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox;
    private javax.swing.JLabel jLabel;
    private javax.swing.JTextArea jLogTextArea;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel plotPanel;
    private javax.swing.JLabel processingLabel;
    private javax.swing.JSlider slider;
    private javax.swing.JPanel upPanel;
    // End of variables declaration//GEN-END:variables
}
