/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class JungGraphTutorial {
	public static void main(String[] args) {

		DirectedSparseGraph<String, Integer> g = new DirectedSparseGraph<String, Integer>();
		g.addVertex("n1");
		g.addVertex("n2");
		g.addVertex("n3");
		// Jung finds matching nodes even if pointers are different
		g.addEdge(1, new Pair<String>("n1", "n2"));
		System.out.println(g);
		// Jung adds automatically new nodes
		g.addEdge(2, new Pair<String>("n1", "n4"));
		System.out.println(g);
		g.removeVertex("n1");
		System.out.println(g);
	}
}