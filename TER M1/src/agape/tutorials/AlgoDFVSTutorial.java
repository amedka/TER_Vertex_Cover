/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import java.util.ArrayList;
import java.util.Set;

import agape.algos.MinDFVS;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class AlgoDFVSTutorial {
	public static void main(String[] args) {

		DirectedGraphFactoryForStringInteger factory = new DirectedGraphFactoryForStringInteger();
		DirectedSparseGraph<String, Integer> g = new DirectedSparseGraph<String, Integer>();

		g.addVertex("n1");
		g.addVertex("n2");
		g.addVertex("n3");
		g.addVertex("n4");
		g.addVertex("n5");
		g.addVertex("n6");
		g.addVertex("n7");
		g.addVertex("n8");

		g.addEdge(1, new Pair<String>("n1", "n2"));
		g.addEdge(2, new Pair<String>("n1", "n4"));
		g.addEdge(3, new Pair<String>("n2", "n3"));
		g.addEdge(4, new Pair<String>("n3", "n5"));
		g.addEdge(5, new Pair<String>("n5", "n2"));
		g.addEdge(6, new Pair<String>("n5", "n3"));
		g.addEdge(7, new Pair<String>("n4", "n6"));
		g.addEdge(8, new Pair<String>("n4", "n7"));
		g.addEdge(9, new Pair<String>("n8", "n1"));
		g.addEdge(10, new Pair<String>("n7", "n8"));
		g.addEdge(11, new Pair<String>("n3", "n1"));

		System.out.println(g);

		MinDFVS<String, Integer> dfvs = new MinDFVS<String, Integer>(factory, factory.edgeFactory);

		Set<ArrayList<String>> circuits = dfvs.enumAllCircuitsTarjan(g);
		System.out.println("Circuits: " + circuits);

		Set<String> subset = dfvs.maximumDirectedAcyclicSubset(g);
		System.out.println("Max directed acyclic subset: " + subset);

		Set<String> subset3 = dfvs.greedyMinFVS(g);
		System.out.println("Greedy directed acyclic subset:" + subset3);

		Set<String> subsetComplementary = dfvs.minimumFeedbackVertexSet(g);
		System.out.println("Min directed acyclic subset: " + subsetComplementary);

		Visualization.showGraph(g, subsetComplementary);
	}
}