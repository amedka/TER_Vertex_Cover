package Batterie;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.swing.JFrame;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Graphe{
	
	Graph myGraphe;
	Factory<UndirectedGraph<Integer,String>> grapheFactory;
	
	public Graph getGraphe() {
		return myGraphe;
	}
	
	//Erdos RenyiGenerator
	//pour un nombre de sommets et une probabilité donnés, génère un graphe aléatoire
	public Graphe(int nbSommets, double propability) {
			grapheFactory = 
	            new Factory<UndirectedGraph<Integer,String>>() {
	                public UndirectedGraph<Integer,String> create() {
	                	UndirectedGraph<Integer, String> g = new UndirectedSparseGraph<Integer, String>();
	                	return g;
	                }
	           	};
	        Factory<Integer> vertexFactory = 
	            new Factory<Integer>() {
	                int count;
	                public Integer create() {
	                    return count++;
            }};
            Factory<String> edgeFactory = 
	            new Factory<String>() {
			    	String s =  "E";
			    	int count;
	                public String create() {
	                    count++;
	                    return s+count;
            }};

        ErdosRenyiGenerator generator = new ErdosRenyiGenerator(grapheFactory, vertexFactory, edgeFactory, nbSommets, propability);
		myGraphe = generator.create();
	}

	//affiche le graphe dans une fenêtre à part
	public void visualization() {
		Layout<Integer, String> layout = new CircleLayout(myGraphe);
		layout.setSize(new Dimension(1000,1000));
		BasicVisualizationServer<Integer,String> vv =
		new BasicVisualizationServer<Integer,String>(layout);
		vv.setPreferredSize(new Dimension(1000,1000));
		
		//affichent nom sommet/arete si besoin
		//vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		//vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
		
		
		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}
	
	public Factory<UndirectedGraph<Integer,String>> getFactory(){
		return grapheFactory;
	}
}
