/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import agape.algos.MVC;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class AlgoMVCTutorial {
	public static void main(String[] args) {

		System.out.println("----------------");
		System.out.println("UNDIRECTED GRAPH");
		System.out.println("----------------");

		SparseGraph<String, Integer> gu = new SparseGraph<String, Integer>();
		UndirectedGraphFactoryForStringInteger undfactory = new UndirectedGraphFactoryForStringInteger();

		gu.addVertex("n1");
		gu.addVertex("n2");
		gu.addVertex("n3");
		gu.addVertex("n4");
		gu.addVertex("n5");
		gu.addVertex("n6");
		gu.addVertex("n7");
		gu.addVertex("n8");

		gu.addEdge(1, new Pair<String>("n1", "n2"));
		gu.addEdge(2, new Pair<String>("n1", "n4"));
		gu.addEdge(3, new Pair<String>("n2", "n3"));
		gu.addEdge(5, new Pair<String>("n5", "n2"));
		gu.addEdge(7, new Pair<String>("n1", "n6"));
		gu.addEdge(9, new Pair<String>("n1", "n7"));
		gu.addEdge(11, new Pair<String>("n2", "n8"));

		System.out.println(gu);

		MVC<String, Integer> mvc = new MVC<String, Integer>(undfactory);

		mvc.twoApproximationCover(gu);
		System.out.println("result 2-approx: " + mvc.getVertexCoverSolution());

		mvc.greedyCoverMaxDegree(gu);
		System.out.println("result greedy: " + mvc.getVertexCoverSolution());

		mvc.kVertexCoverBruteForce(gu, 2);
		System.out.println("result BruteForce k=2: " + mvc.getVertexCoverSolution());

		mvc.kVertexCoverBruteForce(gu, 3);
		System.out.println("result BruteForce k=3: " + mvc.getVertexCoverSolution());

		mvc.kVertexCoverBruteForce(gu, 4);
		System.out.println("result BruteForce k=4: " + mvc.getVertexCoverSolution());

		mvc.kVertexCoverDegreeBranchingStrategy(gu, 2);
		System.out.println("result DegreeBranching k=2: " + mvc.getVertexCoverSolution());

		mvc.kVertexCoverBussGoldsmith(gu, 2);
		System.out.println("result Buss k=2: " + mvc.getVertexCoverSolution());

		mvc.kVertexCoverNiedermeier(gu, 2);
		System.out.println("result Niedermeier k=2: " + mvc.getVertexCoverSolution());

		Visualization.showGraph(gu, mvc.getVertexCoverSolution());
	}
}