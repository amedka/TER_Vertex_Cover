package agape.test;

import java.util.Set;

import agape.algos.Coloring;
import agape.generators.RandGenerator;
import agape.io.Import;
import agape.tutorials.UndirectedGraphFactoryForStringInteger;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class DemoAgape {

	/**
	 * @param args
	 * @author P. Berthome, J.-F. Lalande
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Creating the graph and the corresponding factory
		UndirectedGraphFactoryForStringInteger undfactory = new UndirectedGraphFactoryForStringInteger();

		SparseGraph<String, Integer> gu = new SparseGraph<String, Integer>();
		gu.addVertex("v1");
		gu.addVertex("v2");
		gu.addVertex("v3");
		gu.addVertex("v4");

		gu.addEdge(1, new Pair<String>("v1", "v2"));
		gu.addEdge(2, new Pair<String>("v1", "v3"));
		gu.addEdge(3, new Pair<String>("v1", "v4"));
		gu.addEdge(4, new Pair<String>("v3", "v4"));

		// Visualization.showGraph(gu);

		UndirectedGraphFactoryForStringInteger factory = new UndirectedGraphFactoryForStringInteger();
		Graph<String, Integer> g4 = RandGenerator.generateErdosRenyiGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 10, 0.6);
		// System.out.println(g4);
		// Visualization.showGraph(g4);

		Coloring<String, Integer> coloring = new Coloring<String, Integer>(undfactory);
		// int col = coloring.chromaticNumberBjorklundHusfeldt(gu);
		// System.out.println(gu);
		// System.out.println("Chromatic number Simple graph: " + col);

		Set<Set<String>> couleurs = coloring.graphColoring(g4);
		System.out.println("Chromatic number Simple graph: " + couleurs.size());
		Visualization.showGraphSets(g4, couleurs);

		Graph<String, Integer> g = Import.readNet("test/Simone/graph.net");
		Visualization.showGraph(g);

	}

}
