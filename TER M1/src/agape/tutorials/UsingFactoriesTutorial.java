/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import org.apache.commons.collections15.Factory;

import agape.generators.RandGenerator;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class UsingFactoriesTutorial {
	public static void main(String[] args) {

		Factory<Integer> edgeFactory = new Factory<Integer>() {
			int c = 0;

			@Override
			public Integer create() {
				this.c++;
				return this.c;
			}
		};

		Factory<String> vertexFactory = new Factory<String>() {
			int c = 0;

			@Override
			public String create() {
				this.c++;
				return "v" + this.c;
			}
		};

		Factory<Graph<String, Integer>> graphFactory = new Factory<Graph<String, Integer>>() {
			@Override
			public Graph<String, Integer> create() {
				return new UndirectedSparseGraph<String, Integer>();
			}
		};

		Graph<String, Integer> g = RandGenerator.generateErdosRenyiGraph(graphFactory, vertexFactory, edgeFactory, 10,
				3);
		System.out.println(g);

		Visualization.showGraph(g);
	}
}