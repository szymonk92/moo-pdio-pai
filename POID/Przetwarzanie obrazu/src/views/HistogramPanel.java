/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import sys.BufferedImageHelper;
import sys.TabData;

/**
 *
 * @author Lukasz
 */
public class HistogramPanel extends javax.swing.JPanel {

    private TabData data;
    /**
     * Creates new form HistogramPanel
     */
    public HistogramPanel(TabData data) {
        this.data = data;
        initComponents();
        double[] value = BufferedImageHelper.getHistogram(this.data.getFilteredImage(), 1);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < 256; i++) {
            dataset.addValue(value[i], "", Integer.toString(i));
        }
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createBarChart(null, null, null, dataset, orientation, show, toolTips, urls);
        this.add(new ChartPanel(chart));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
