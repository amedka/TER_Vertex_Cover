package Batterie;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Factory;

import Algorithmes.VertexCover;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Graphe {

	static int test = 0;
	Graph<Integer, String> myGraphe;
	Factory<UndirectedGraph<Integer, String>> grapheFactory;

	// constructeur utile pour instancier un Graphe ï¿½ partir d'un fichier texte
	public Graphe() {
		this.grapheFactory = new Factory<UndirectedGraph<Integer, String>>() {
			@Override
			public UndirectedGraph<Integer, String> create() {
				UndirectedGraph<Integer, String> g = new UndirectedSparseGraph<Integer, String>();
				return g;
			}
		};
	}

	public Graph<Integer, String> getGraphe() {
		return this.myGraphe;
	}

	public void setGraphe(Graph<Integer, String> g) {
		this.myGraphe = g;
	}

	public Factory<UndirectedGraph<Integer, String>> getFactory() {
		return this.grapheFactory;
	}

	// Erdos RenyiGenerator
	// pour un nombre de sommets et une probabilitï¿½ donnï¿½s, gï¿½nï¿½re un graphe
	// alï¿½atoire
	public Graphe(int nbSommets, double propability) {
		this.grapheFactory = new Factory<UndirectedGraph<Integer, String>>() {
			@Override
			public UndirectedGraph<Integer, String> create() {
				UndirectedGraph<Integer, String> g = new UndirectedSparseGraph<Integer, String>();
				return g;
			}
		};
		Factory<Integer> vertexFactory = new Factory<Integer>() {
			int count;

			@Override
			public Integer create() {
				return this.count++;
			}
		};
		Factory<String> edgeFactory = new Factory<String>() {
			String s = "E";
			int count;

			@Override
			public String create() {
				this.count++;
				return this.s + this.count;
			}
		};

		ErdosRenyiGenerator<Integer, String> generator = new ErdosRenyiGenerator<Integer, String>(this.grapheFactory,
				vertexFactory, edgeFactory, nbSommets, propability);
		this.myGraphe = generator.create();
	}

	// affiche le graphe dans une fenï¿½tre ï¿½ part
	public void visualization() {
		Layout<Integer, String> layout = new CircleLayout<Integer, String>(this.myGraphe);
		layout.setSize(new Dimension(1000, 1000));
		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(layout);
		vv.setPreferredSize(new Dimension(1000, 1000));

		// affichent nom sommet/arete si besoin
		// vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		// vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public String toString() {
		return this.getGraphe().toString();
	}
	

	
	// modifie le graphe : supprime les sommets isolés
	public void ker0() {
		for (Integer  value : this.myGraphe.getVertices()) {
			if (this.myGraphe.getNeighborCount(value)==0) {
				this.myGraphe.removeVertex(value);
			}
		}
	}
	
	
	//renvoie une instance de vertex cover (une graphe et un int) la plus petite possible aprÃ¨s avoir pris tout les sommet de taille > a k (ou k=0)
	public int ker1(int k) {
		int repeat = 0;
		while (repeat !=k && k>0){
			repeat = k;
			for (Integer  value : this.myGraphe.getVertices()) {
				if (myGraphe.getNeighborCount(value) > k) {
					k--;
					this.myGraphe.removeVertex(value);
				}
			}
		}
		return k;
	}
	
	//applique les deux algos de kernalisation et retourne k
	public int kerFusion(int k) {
		new Thread(new Runnable() {
			public void run() {

			    //do long running blocking bg stuff here
			    new Runnable() {
			        public void run() {
			    		ker0();
			        }   
			    };
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {

			    //do long running blocking bg stuff here
			    new Runnable() {
			        public void run() {
			    		test= ker1(k);
			        }   
			    };
			}
		}).start();
		return test;
	}
	
	
}
	
