
import java.util.*;
import java.awt.Dimension;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter.EdgeType;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


import org.apache.commons.collections15.Factory;




public class Kernel {
	
	private Graph<Integer, String> graphe;
	//private ArrayList<Integer> nbVoisins = new ArrayList<Integer>();
	
	
	
	public Kernel (Graph<Integer, String> g) {
		
		this.graphe=g;
		
	}

	
	// renvoie une instance de vertex cover sans les sommets isolées
	public VertexCover ker0(int k) {
		for (Integer  value : this.graphe.getVertices()) {
			if (graphe.getNeighborCount(value)==0) {
				this.graphe.removeVertex(value);
			}
		}
		
		return new VertexCover(k, this.graphe);
	}
	
	
	//renvoie une instance de vertex cover (une graphe et un int) la plus petite possible après avoir pris tout les sommet de taille > a k (ou k=0)
	public VertexCover ker1( int k) {
		int repeat = 0;
		while (repeat !=K && k>0){
			repeat = k!
			for (Integer  value : this.graphe.getVertices()) {
				if (graphe.getNeighborCount(value) >k) {
					k--;
					this.graphe.removeVertex(value);
				}
			}
		}
		
		return new VertexCover (k, this.graphe);
		
	}
	
	// un brutforce pour le vertex cover de taille K
	public boolean brutForce (int k, Graph<Integer, String> graph) {
		if (graph.getEdgeCount()==0) {return true;}
		if (k==0) {return false;}
		for (Integer  value : graph.getVertices()) {
			Graph<Integer, String> gtemp = graph;
			gtemp.removeVertex(value);
			if (brutForce(k-1, gtemp)){
				return true;
			}
		}
		
		return false;
	}
	
}
	

