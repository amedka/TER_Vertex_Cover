/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import agape.generators.RandGenerator;
import agape.visu.Visualization;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;

public class RandomGeneratorTutorial {
	public static void main(String[] args) throws Exception {

		UndirectedGraphFactoryForStringInteger factory = new UndirectedGraphFactoryForStringInteger();

		Graph<String, Integer> g2 = RandGenerator.generateKleinbergSWGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 5, 5, 1, 2, 2);
		Visualization.showGraph(g2);

		Graph<String, Integer> g3 = RandGenerator.generateWattsStrogatzSWGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 24, 4, 0.5);
		Visualization.showGraph(g3, new CircleLayout<String, Integer>(g3));

		Graph<String, Integer> g4 = RandGenerator.generateErdosRenyiGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 10, 0.5);
		Visualization.showGraph(g4);

		Graph<String, Integer> g5 = RandGenerator.generateBarabasiAlbertGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 10, 5, 7);
		Visualization.showGraph(g5);

		Graph<String, Integer> g6 = RandGenerator.generateEppsteinGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 10, 25, 100);
		Visualization.showGraph(g6);

		Graph<String, Integer> g7 = RandGenerator.generateRandomRegularGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 10, 5);
		Visualization.showGraph(g7);
	}
}