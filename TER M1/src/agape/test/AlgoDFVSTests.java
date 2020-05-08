package agape.test;

import java.util.Set;

import agape.algos.MinDFVS;
import agape.tutorials.DirectedGraphFactoryForStringInteger;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class AlgoDFVSTests {
	public static void main(String[] args) {

		DirectedGraphFactoryForStringInteger factory = new DirectedGraphFactoryForStringInteger();
		DirectedSparseGraph<String, Integer> g = new DirectedSparseGraph<String, Integer>();

		g.addVertex("n1");
		g.addVertex("n2");
		g.addVertex("n3");

		g.addEdge(1, new Pair<String>("n1", "n2"));
		g.addEdge(2, new Pair<String>("n2", "n1"));
		g.addEdge(3, new Pair<String>("n2", "n3"));
		g.addEdge(4, new Pair<String>("n3", "n2"));
		g.addEdge(5, new Pair<String>("n1", "n3"));
		g.addEdge(6, new Pair<String>("n3", "n1"));

		AlgoDFVSTests.computeDFVS(g, factory);

		g.removeEdge(3);
		AlgoDFVSTests.computeDFVS(g, factory);

		g.removeEdge(4);
		AlgoDFVSTests.computeDFVS(g, factory);

		g.removeEdge(1);
		AlgoDFVSTests.computeDFVS(g, factory);

		g.removeEdge(5);
		AlgoDFVSTests.computeDFVS(g, factory);

		g.removeEdge(6);
		AlgoDFVSTests.computeDFVS(g, factory);

	}

	private static void computeDFVS(DirectedSparseGraph<String, Integer> g,
			DirectedGraphFactoryForStringInteger factory) {
		System.out.println(g);
		MinDFVS<String, Integer> dfvs = new MinDFVS<String, Integer>(factory, factory.edgeFactory);
		Set<String> subset = dfvs.maximumDirectedAcyclicSubset(g);
		System.out.println("Max directed acyclic subset: " + subset);
		Set<String> subsetComplementary = dfvs.minimumFeedbackVertexSet(g);
		System.out.println("Min directed acyclic subset: " + subsetComplementary);

		// Visualization.showGraph(g, subsetComplementary);
	}
}
