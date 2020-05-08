/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import agape.generators.NRandGenerator;
import agape.visu.Visualization;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;

public class NRandomGeneratorTutorial {
	public static void main(String[] args) {

		DirectedGraphFactoryForStringInteger factory = new DirectedGraphFactoryForStringInteger();

		Graph<String, Integer> g = NRandGenerator.generateRegularRing(factory, factory.vertexFactory,
				factory.edgeFactory, 20, 6);
		Visualization.showGraph(g, new CircleLayout<String, Integer>(g));

		Graph<String, Integer> g2 = NRandGenerator.generateGridGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 3, 5, false);
		Visualization.showGraph(g2);

	}
}