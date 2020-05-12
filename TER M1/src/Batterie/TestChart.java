package Batterie;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class TestChart extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -1875964637529997015L;
	HashMap<String, ArrayList<Float>> map;

	public TestChart(HashMap<String, ArrayList<Float>> m) {
		this.map = m;
		this.initUI();
	}

	public void initUI() {

		XYDataset dataset = this.createDataset();
		JFreeChart chart = this.createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		chartPanel.setBackground(Color.white);

		this.add(chartPanel);

		this.pack();
		this.setTitle("Line chart");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public XYDataset createDataset() {

		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries currentSerie;
		ArrayList<String> keys = new ArrayList<String>(this.map.keySet());
		for (int i = 0; i < keys.size(); i++) {
			currentSerie = new XYSeries(keys.get(i));
			int cpt = 1;
			for (int j = 0; j < this.map.get(keys.get(i)).size(); j++) {
				currentSerie.add(cpt, this.map.get(keys.get(i)).get(j));
				cpt++;
			}
			dataset.addSeries(currentSerie);
		}
		return dataset;
	}

	public JFreeChart createChart(final XYDataset dataset) {

		JFreeChart chart = ChartFactory.createXYLineChart("Temps d'execution des differents algorithmes",
				"Numéro du graphe", "Millisecondes", dataset, PlotOrientation.VERTICAL, true, true, false);

		XYPlot plot = chart.getXYPlot();

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesPaint(1, Color.BLUE);
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);

		chart.getLegend().setFrame(BlockBorder.NONE);

		chart.setTitle(new TextTitle("Temps d'execution des differents algorithmes", new Font("Serif", Font.BOLD, 18)));

		return chart;
	}
}