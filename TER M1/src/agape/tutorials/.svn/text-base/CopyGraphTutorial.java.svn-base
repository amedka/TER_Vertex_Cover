/*
 * Copyright University of Orleans - ENSI de Bourges. 
 * Source code under CeCILL license.
 */
package agape.tutorials;

import agape.tools.Operations;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class CopyGraphTutorial {

    public static void main(String[] args) {
        System.out.println("----------------");
        System.out.println("UNDIRECTED GRAPH");
        System.out.println("----------------");
        SparseGraph<String, Integer> gu = new SparseGraph<String, Integer>();
        UndirectedGraphFactoryForStringInteger undfactory = new UndirectedGraphFactoryForStringInteger();
        
        gu.addVertex("n1");  gu.addVertex("n2");
        gu.addVertex("n3");  gu.addVertex("n4");
        gu.addVertex("n5");
        
        gu.addEdge(1, new Pair<String>("n1", "n2"));
        gu.addEdge(2, new Pair<String>("n1", "n4"));
        gu.addEdge(3, new Pair<String>("n2", "n3"));
        gu.addEdge(4, new Pair<String>("n3", "n5"));
        gu.addEdge(5, new Pair<String>("n5", "n2"));
        gu.addEdge(6, new Pair<String>("n5", "n3")); // useless
        
        /* This is an explicit call to the copyUndirectedSparseGraph method
         * (we know that the graph is undirected)
         */
        System.out.println(gu);
        UndirectedSparseGraph<String, Integer> gu2 = 
        		(UndirectedSparseGraph<String, Integer>) Operations.copyUndirectedSparseGraph(gu);
        System.out.println(gu2);
        /* This is a general call to the copyGraph method that uses a factory
         * (we do not want to know if the graph is direted or not)
         */
        UndirectedSparseGraph<String, Integer> gu3 = 
        		(UndirectedSparseGraph<String, Integer>) Operations.copyGraph(gu, undfactory);
        System.out.println(gu3);

    }
}