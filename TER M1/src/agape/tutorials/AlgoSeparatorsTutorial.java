/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import java.util.HashSet;
import java.util.Set;

import agape.algos.Separators;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class AlgoSeparatorsTutorial {
	public static void main(String[] args) {

		Separators<String, Integer> sep = new Separators<String, Integer>();

		System.out.println("----------------");
		System.out.println("UNDIRECTED GRAPH");
		System.out.println("----------------");

		SparseGraph<String, Integer> gu = new SparseGraph<String, Integer>();

		gu.addVertex("n1");
		gu.addVertex("n2");
		gu.addVertex("n3");
		gu.addVertex("n4");
		gu.addVertex("n5");
		gu.addVertex("n6");

		gu.addEdge(1, new Pair<String>("n1", "n2"));
		gu.addEdge(2, new Pair<String>("n1", "n4"));
		gu.addEdge(3, new Pair<String>("n2", "n3"));
		gu.addEdge(4, new Pair<String>("n3", "n5"));
		gu.addEdge(5, new Pair<String>("n5", "n2"));
		gu.addEdge(6, new Pair<String>("n1", "n6"));
		gu.addEdge(7, new Pair<String>("n5", "n6"));

		System.out.println(gu);

		Set<Set<String>> set = sep.getABSeparators(gu, "n4", "n3");
		System.out.println("n4/n5: " + set);

		set = sep.getAllMinimalSeparators(gu);
		System.out.println("all minimum separators: " + set);

		set = sep.getABSeparators(gu, "n1", "n5");
		System.out.println("n1/n5: " + set);

		HashSet<String> toSeparate = new HashSet<String>();
		toSeparate.add("n1");
		toSeparate.add("n5");
		Visualization.showGraph(gu, set.iterator().next(), toSeparate);
	}
}