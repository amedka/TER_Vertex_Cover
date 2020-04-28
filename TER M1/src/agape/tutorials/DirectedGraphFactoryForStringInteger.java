/*
 * Copyright University of Orleans - ENSI de Bourges. 
 * Source code under CeCILL license.
 */
package agape.tutorials;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class DirectedGraphFactoryForStringInteger extends UndirectedGraphFactoryForStringInteger {
    
    public DirectedGraphFactoryForStringInteger(int i, int j) {
        super(i,j);
    }
    
    public DirectedGraphFactoryForStringInteger() {
        super();
    }

    public Graph<String, Integer> create() {
        return new DirectedSparseGraph<String, Integer>();
    }
}