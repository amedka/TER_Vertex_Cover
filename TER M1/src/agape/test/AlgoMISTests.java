/*
 * Copyright University of Orleans - ENSI de Bourges. 
 * Source code under CeCILL license.
 */
package agape.test;

import java.util.Set;

import agape.algos.MIS;
import agape.generators.RandGenerator;
import agape.tutorials.UndirectedGraphFactoryForStringInteger;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.Graph;

public class AlgoMISTests {
    public static void main(String[] args) {

        UndirectedGraphFactoryForStringInteger factory = new UndirectedGraphFactoryForStringInteger();

    	 Graph<String, Integer> g4 = RandGenerator.generateErdosRenyiGraph(
                 factory, factory.vertexFactory, factory.edgeFactory, 10, 0.5);
         Visualization.showGraph(g4);

        Set<String> set = null;
        MIS<String, Integer> mis = new MIS<String, Integer>(factory, factory.vertexFactory, factory.edgeFactory); 
        
        set = mis.maximalIndependentSetGreedy(g4);
        System.out.println("Greedy result: " + set);

        set = mis.maximuRmIndependentSetFominGrandoniKratsch(g4);
        System.out.println("FominGrandoniKratsch: " + set);

        set = mis.maximumIndependentSetMaximumDegree(g4);
        System.out.println("Branching on Maximum Degree: " + set);

        set = mis.maximumIndependentSetMoonMoser(g4);
        System.out.println("Moon Moser result: " + set);

        set = mis.maximumIndependentSetMoonMoserNonRecursive(g4);
        System.out.println("Moon Moser (non recursive) result: " + set);
        
        Visualization.showGraph(g4, set);
    }
}