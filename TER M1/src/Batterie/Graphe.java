package Batterie;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Graphe {

	Graph<Integer, String> myGraphe;
	Factory<UndirectedGraph<Integer, String>> grapheFactory;

	// constructeur utile pour instancier un Graphe � partir d'un fichier texte
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
	// pour un nombre de sommets et une probabilit� donn�s, g�n�re un graphe
	// al�atoire
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

	// affiche le graphe dans une fen�tre � part
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
}
