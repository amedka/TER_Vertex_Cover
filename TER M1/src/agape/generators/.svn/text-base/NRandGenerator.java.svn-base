/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.generators;

import java.util.ArrayList;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.Lattice2DGenerator;
import edu.uci.ics.jung.graph.Graph;

/**
 * This class allows to generate non-random graphs.
 * @author V. Levorato
 * @author P. Berthome, J.-F. Lalande
 */
public class NRandGenerator<V,E> {
    
    /**
     * This method generates grid2D graph. Generator from JUNG libray is used in this method.
     * @param graphFactory graph factory
     * @param vertexFactory vertex factory
     * @param edgeFactory edge factory
     * @param width  amount of colums
     * @param height amount of rows
     * @param torus if true, the method returns a torus.
     * @return grid2D graph
     */
    public static<V,E> Graph<V,E> generateGridGraph(Factory<Graph<V,E>> graphFactory, Factory<V> vertexFactory, Factory<E> edgeFactory, int width, int height, boolean torus)
    {
        return new Lattice2DGenerator<V,E>(graphFactory, vertexFactory, edgeFactory, height, width, torus).create();
    }
    
    
    /**
     * @param <V>
     * @param <E>
     * @param graphFactory graph factory
     * @param vertexFactory edge factory
     * @param edgeFactory
     * @param n number of vertices
     * @param k degree of the graph
     * @return a regular ring graph
     */
    public static<V,E> Graph<V,E> generateRegularRing(Factory<Graph<V,E>> graphFactory,
            Factory<V> vertexFactory,
            Factory<E> edgeFactory,
            int    n, int k) {

        Graph<V,E> G = graphFactory.create();
        ArrayList<V> vertices=new ArrayList<V>();
        for(int i=0;i<n;i++)
            vertices.add(vertexFactory.create());

        for(int j = 0 ; j <k/2; j++){
            for(int i=0;i<n;i++)
                G.addEdge(edgeFactory.create(),
                        vertices.get(i),vertices.get((i+1+j)%n));
        }

        return G;
    }


}
