/*
 * Copyright University of Orleans - ENSI de Bourges. 
 * Source code under CeCILL license.
 */
package agape.tutorials;

import java.util.Iterator;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class FactoryProblem<V,E> {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        // Creating the graph and the corresponding factory
        UndirectedGraphFactoryForStringInteger undfactory = new UndirectedGraphFactoryForStringInteger();
        
        SparseGraph<String, Integer> gu = new SparseGraph<String, Integer>();
        gu.addVertex("v1");  gu.addVertex("v2");
        gu.addVertex("v3");  gu.addVertex("v4");
        gu.addVertex("v5");
        
        gu.addEdge(1, new Pair<String>("v1", "v2"));
        gu.addEdge(2, new Pair<String>("v1", "v4"));
        gu.addEdge(3, new Pair<String>("v2", "v3"));
        gu.addEdge(4, new Pair<String>("v3", "v5"));
        gu.addEdge(5, new Pair<String>("v5", "v2"));
        gu.addEdge(6, new Pair<String>("v5", "v3"));
        
        FactoryProblem<String, Integer> problem = new FactoryProblem<String, Integer>();
        try
        {
           problem.addRandomEdge(gu, undfactory.edgeFactory);
        }
        catch (IllegalArgumentException e)
        {
            System.err.println(e);
            System.out.println("Our factory is badly used because it tries to instantiate the edge number 1 " +
            		"which already exists in the graph.");
        }
        
        // Solution: we should not have created the graph manually !
        SparseGraph<String, Integer> gu2 = new SparseGraph<String, Integer>();
        String v[] = new String[5];
        for (int i=0; i<5; i++)
        {
            v[i] = undfactory.vertexFactory.create();
            gu2.addVertex(v[i]);
        }
        Integer e[] = new Integer[6];
        for (int i=0; i<6; i++)
            e[i] = undfactory.edgeFactory.create();
        gu2.addEdge(e[0], v[0], v[1]);
        gu2.addEdge(e[1], v[0], v[3]);
        gu2.addEdge(e[2], v[1], v[2]);
        gu2.addEdge(e[3], v[2], v[4]);
        gu2.addEdge(e[4], v[4], v[2]);
        gu2.addEdge(e[5], v[4], v[3]);
        
        System.out.println("gu2 before : " + gu2);
        problem.addRandomEdge(gu2, undfactory.edgeFactory);
        System.out.println("gu2 after  : " + gu2);
    }
    
    public void addRandomEdge(Graph<V, E> g, Factory<E> f)
    {
        // Choosing 2 vertices
        int choice = (int)(Math.random()*g.getVertexCount()) ;
        Iterator<V> it = g.getVertices().iterator();
        V v1 = null;
        for (int i = 0; i <= choice; i++)
            v1 = it.next();
       
        choice = (int)(Math.random()*g.getVertexCount()) ;
        it = g.getVertices().iterator();
        V v2 = null;
        for (int i = 0; i <= choice; i++)
            v2 = it.next();
               
        E edge = f.create();
        System.out.println("Trying to add Edge " + edge + " between " + v1 + " " + v2);
        g.addEdge(edge, v1, v2);
    }
}