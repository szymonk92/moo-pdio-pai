import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
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
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.chart.renderer.category.WaterfallBarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.DateRange;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class PlotWave {

	
	private static int SLIDER_DEFAULT_VALUE = 100;
	
	
	public int lastValue;
	public JFrame frame;
	public JPanel panel;
	public JSlider slider;
	
	
	public PlotWave() {
		frame = new JFrame();
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		slider =  new JSlider(0, 200, SLIDER_DEFAULT_VALUE);
		lastValue=SLIDER_DEFAULT_VALUE;
	}
	
	
	public void plot(double[][] signal,String  name,long samplerate) {
		
		frame.setTitle(name);

		XYSeries[] soundWave = new XYSeries[signal.length];
		for(int j=0; j<signal.length; ++j) {
			soundWave[j]= new XYSeries("sygnal"+j);
			for( int i=0; i<signal[0].length; ++i) {
				double index =  (samplerate ==0 ) ? i : 1000.0*(double)i/(double)samplerate;
				soundWave[j].add(index,signal[j][i]);
			}
		}
		
		
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		for( int j=0; j<signal.length; ++j)
		dataset.addSeries(soundWave[j]);
		
		
		
		
		JFreeChart chart = 
//				(samplerate ==0 )?
//				ChartFactory.createXYBarChart(
//				name,
//				"próbka",
//				false,
//				"wartość",
//				new XYBarDataset(dataset,0.0625),
//				PlotOrientation.VERTICAL,
//				true,false,false)
//				:
				
				ChartFactory.createXYLineChart(
				name,
				"próbka",
				"wartość",
				dataset,
				PlotOrientation.VERTICAL,
				true,false,false);
		
		
		XYPlot plot = (XYPlot)chart.getPlot();
		
		
		final NumberAxis domainAxis =(NumberAxis)plot.getDomainAxis();
		
		slider.addChangeListener(new ChangeListener() {

			@Override
		        public void stateChanged(ChangeEvent event) {
		            int value = slider.getValue();
		            double minimum = domainAxis.getRange().getLowerBound();
		            double maximum = domainAxis.getRange().getUpperBound();
		            double delta = (0.1f*(domainAxis.getRange().getLength()));
					if (value<lastValue) { // left
		                minimum = minimum - delta;
		                maximum = maximum - delta;
		            } else { // right
		                minimum = minimum + delta;
		                maximum = maximum + delta;
		            }
		            DateRange range = new DateRange(minimum,maximum);
		            domainAxis.setRange(range);
		            lastValue = value;
		            if ( lastValue == slider.getMinimum() || lastValue == slider.getMaximum())
		            	slider.setValue(SLIDER_DEFAULT_VALUE);
		            }

		});
	
		
		
		plot.addRangeMarker(new ValueMarker(0,Color.BLACK,new BasicStroke(1)));

		ChartPanel chartPanel = new ChartPanel(chart);	
		Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createEtchedBorder()
            );
            chartPanel.setBorder(border);
		
		chartPanel.addMouseWheelListener(addZoomWheel());
		
		panel.add(chartPanel);
		JPanel dashboard = new JPanel(new BorderLayout());
		dashboard.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));   
        dashboard.add(slider);
        panel.add(dashboard, BorderLayout.SOUTH);
		
				
		frame.getContentPane().add((JPanel)panel, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	
	private MouseWheelListener addZoomWheel() {
		return new MouseWheelListener() {
			  private void zoomChartAxis(ChartPanel chartP, boolean increase){              
			        int width = chartP.getMaximumDrawWidth() - chartP.getMinimumDrawWidth();
			        int height = chartP.getMaximumDrawHeight() - chartP.getMinimumDrawWidth();       
			        if(increase){
			        	chartP.zoomInDomain(width/2, height/2);
			        }else{
			        	chartP.zoomOutDomain(width/2, height/2);
			        }
			   lastValue=SLIDER_DEFAULT_VALUE;
			   slider.setValue(lastValue);
			   
			  }
		   
		    public synchronized void decreaseZoom(JComponent chart, boolean saveAction){
		        ChartPanel ch = (ChartPanel)chart;
		        zoomChartAxis(ch, false);
		    }  
		    
		   
		    public synchronized void increaseZoom(JComponent chart, boolean saveAction){
		        ChartPanel ch = (ChartPanel)chart;
		        zoomChartAxis(ch, true);
		    }
		    
		    @Override
		    public void mouseWheelMoved (MouseWheelEvent e) {
		        if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) return;
		        if (e.getWheelRotation()< 0) increaseZoom((ChartPanel)e.getComponent(), true);
		        else                          decreaseZoom((ChartPanel)e.getComponent(), true);
		    }
		};
	}
	
}
