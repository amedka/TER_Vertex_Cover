/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.algos;

import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Factory;

/**
 * This class is used to set up factories for the different algorithms classes.
 * @author V. Levorato
 */
public class Algorithms<V,E> {
    
    protected Factory<V> vertexFactory;
    protected Factory<E> edgeFactory;
    protected Factory<Graph<V,E>> graphFactory;
    
    public  void setVertexFactoy(Factory<V> vf) { vertexFactory=vf; }
    public  void setEdgeFactoy(Factory<E> ef) { edgeFactory=ef; }
    public  void setGraphFactoy(Factory<Graph<V,E>> gf) { graphFactory=gf; }
    public  Factory<V> getVertexFactory() { return vertexFactory; }
    public  Factory<E> getEdgeFactory() { return edgeFactory; }
    public  Factory<Graph<V,E>> getGraphFactory() {return graphFactory; }
    
}
