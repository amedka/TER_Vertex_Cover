package Batterie;
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

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

public class TestChart extends JFrame {

	HashMap<String, ArrayList<Float>> map;
    public TestChart(HashMap<String, ArrayList<Float>> m) {
    	map = m;
        initUI();
    }

    public void initUI() {

        XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        chartPanel.setBackground(Color.white);
        
        add(chartPanel);

        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public XYDataset createDataset() {

        XYSeriesCollection dataset = new XYSeriesCollection();
    	XYSeries currentSerie;
    	ArrayList<String> keys = new ArrayList<String>(map.keySet());
    	for(int i=0; i<keys.size();i++) {
    		currentSerie=new XYSeries(keys.get(i));
    		int cpt=1;
    		for(int j=0; j<map.get(keys.get(i)).size();j++) {
    			currentSerie.add(cpt, map.get(keys.get(i)).get(j));
    			cpt++;
    		}
            dataset.addSeries(currentSerie);
    	}
        return dataset;
    }

    public JFreeChart createChart(final XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Temps d'execution des differents algorithmes",
                "Numéro du graphe",
                "Millisecondes",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

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

        chart.setTitle(new TextTitle("Temps d'execution des differents algorithmes",
                        new Font("Serif", Font.BOLD, 18)
                )
        );

        return chart;
    }
}