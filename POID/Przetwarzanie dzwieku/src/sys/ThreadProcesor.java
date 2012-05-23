/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys;

import gui.TabPanel;
import java.util.List;

/**
 *
 * @author Lukasz
 */
public class ThreadProcesor extends Thread {

    private List<TabPanel> panels;
    boolean cepstrum;
    boolean amdf;
    boolean setup;

    public ThreadProcesor(List<TabPanel> panels, boolean setup, boolean cepstrum, boolean amdf) {
        this.panels = panels;
        this.cepstrum = cepstrum;
        this.amdf = amdf;
        this.setup = setup;
    }

    @Override
    public void run() {
        if (panels != null && !panels.isEmpty()) {
            for (TabPanel panel : panels) {
                panel.setProcessing(true);
            }
            for (TabPanel panel : panels) {
                if(setup){
                    panel.basicSetUp();
                }
                if (cepstrum) {
                    panel.appendLog("----Cepstrum----");
                    CepstrumAnalysis ca = new CepstrumAnalysis(panel.signal, panel.wavFile);
                    ca.process();
                    panel.appendLog(ca.log.toString());
                    panel.addPlotData(new TabData("Cepstrum",TabData.generatePlotData(ca.d,0),ca.frameSize, ca.frequencies));
                }
                if (amdf) {
                    panel.appendLog("----AMDF----");
                    AMDF a = new AMDF(panel.signal, panel.wavFile);
                    a.process();
                    panel.appendLog(a.log.toString());
                    panel.addPlotData(new TabData("AMDF",TabData.generatePlotData(a.d,0),a.frameSize, a.frequencies));
                }
                panel.setProcessing(false);
            }
        }

    }
}
