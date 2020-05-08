/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import agape.algos.Coloring;
import agape.generators.RandGenerator;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class AlgoColoringTutorial {
	public static void main(String[] args) {

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

		Coloring<String, Integer> coloring = new Coloring<String, Integer>(undfactory);
		int col = coloring.chromaticNumberBjorklundHusfeldt(gu);
		System.out.println(gu);
		System.out.println("Chromatic number Simple graph: " + col);

		UndirectedGraphFactoryForStringInteger factory = new UndirectedGraphFactoryForStringInteger();

		Graph<String, Integer> g4 = RandGenerator.generateErdosRenyiGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 7, 0.8);
		System.out.println(g4);

		Visualization.showGraph(g4);

		int col4 = coloring.chromaticNumberBjorklundHusfeldt(g4);
		System.out.println("Chromatic number A. Bjorklund and T. Husfeldt: " + col4);
		int col4check = coloring.chromaticNumberBodlaenderKratsch(g4);
		System.out.println("Chromatic number H. Bodlaender and D. Kratsch: " + col4check);

	}
}