/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import java.util.Set;

import agape.algos.MIS;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class AlgoMISTutorial {
	public static void main(String[] args) {

		DirectedGraphFactoryForStringInteger factory = new DirectedGraphFactoryForStringInteger();

		DirectedSparseGraph<String, Integer> g = new DirectedSparseGraph<String, Integer>();
		g.addVertex("n1");
		g.addVertex("n2");
		g.addVertex("n3");
		g.addVertex("n4");
		g.addVertex("n5");

		g.addEdge(1, new Pair<String>("n1", "n2"));
		g.addEdge(2, new Pair<String>("n1", "n4"));
		g.addEdge(3, new Pair<String>("n2", "n3"));
		g.addEdge(4, new Pair<String>("n3", "n5"));
		g.addEdge(5, new Pair<String>("n5", "n2"));
		g.addEdge(6, new Pair<String>("n5", "n3"));

		System.out.println(g);

		Set<String> set = null;
		MIS<String, Integer> mis = new MIS<String, Integer>(factory, factory.vertexFactory, factory.edgeFactory);

		set = mis.maximalIndependentSetGreedy(g);
		System.out.println("Greedy result: " + set);

		set = mis.maximuRmIndependentSetFominGrandoniKratsch(g);
		System.out.println("FominGrandoniKratsch: " + set);

		set = mis.maximumIndependentSetMaximumDegree(g);
		System.out.println("Branching on Maximum Degree: " + set);

		set = mis.maximumIndependentSetMoonMoser(g);
		System.out.println("Moon Moser result: " + set);

		set = mis.maximumIndependentSetMoonMoserNonRecursive(g);
		System.out.println("Moon Moser (non recursive) result: " + set);

		Visualization.showGraph(g, set);
	}
}