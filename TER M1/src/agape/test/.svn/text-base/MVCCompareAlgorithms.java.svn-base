/*
 * Copyright University of Orleans - ENSI de Bourges. 
 * Source code under CeCILL license.
 */
package agape.test;

import java.util.HashSet;

import agape.algos.MVC;
import agape.generators.RandGenerator;
import agape.io.Import;
import agape.tutorials.UndirectedGraphFactoryForStringInteger;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.Graph;

public class MVCCompareAlgorithms {
    public static void main(String[] args) {

        System.out.println("----------------");
        System.out.println("UNDIRECTED GRAPH");
        System.out.println("----------------");

        UndirectedGraphFactoryForStringInteger factory = new UndirectedGraphFactoryForStringInteger(1,1);
		RandGenerator<String, Integer> generator = new RandGenerator<String, Integer>();
		//Graph<String, Integer> gu =	generator.generateErdosRenyiGraph(factory, factory.vertexFactory, 
		//				factory.edgeFactory,8, 0.6)	;
		//Graph<String, Integer> gu =		 generator.generateRandomRegularGraph(factory, 
		//						factory.vertexFactory, factory.edgeFactory, 8, 3);
        
        Graph<String,Integer> gu = Import.readTRG("src/agape/test/MVC-test-rule5-2.trg");
        
		System.out.println("Generating randome ER:");

        MVC<String, Integer> mvc = new MVC<String, Integer>(factory);
        int size = 25;
        System.out.println("Computing a MVC of size " + size);
        
        boolean result = mvc.kVertexCoverNiedermeier(gu, size);
        System.out.println("Result Niedermeier: " + result);
        HashSet<String> result_backup = new HashSet<String>();
        if (result){
        	result_backup.addAll(mvc.getVertexCoverSolution());
        	System.out.println("Size of the MVC: " + mvc.getVertexCoverSolution().size());
        	System.out.println(mvc.getVertexCoverSolution());
        	Visualization.showGraph(gu,	mvc.getVertexCoverSolution());
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++");  
        System.out.println("+              BRUTE FORCE            +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++");      
        boolean result2 = mvc.kVertexCoverBruteForce(gu, size); // OK
        System.out.println("Result BruteForce: " + result2);
        HashSet<String> result2_backup = new HashSet<String>();
        if (result2){
        	result2_backup.addAll(mvc.getVertexCoverSolution());
        	System.out.println("Size of the MVC: " + mvc.getVertexCoverSolution().size());
        	
        	Visualization.showGraph(gu,	mvc.getVertexCoverSolution());
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++");  
        System.out.println("+           DEGREE BRANCHING          +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++");      
        boolean result4 = mvc.kVertexCoverDegreeBranchingStrategy(gu, size); // OK
        System.out.println("Result Degree Branching: " + result4);
        HashSet<String> result4_backup = new HashSet<String>();
        if (result4){
        	result4_backup.addAll(mvc.getVertexCoverSolution());
        	System.out.println("Size of the MVC: " + mvc.getVertexCoverSolution().size());
        	
        	Visualization.showGraph(gu,	mvc.getVertexCoverSolution());
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++");  
        System.out.println("+      let's see what Buss says       +");
        System.out.println("+++++++++++++++++++++++++++++++++++++++");
        boolean result3 = mvc.kVertexCoverBussGoldsmith(gu, size);

        System.out.println("Result BussGoldsmith: " + result3);
        HashSet<String> result3_backup = new HashSet<String>();
        if (result3){
        	result3_backup.addAll(mvc.getVertexCoverSolution());
        	System.out.println("Size of the MVC: " + mvc.getVertexCoverSolution().size());
        	Visualization.showGraph(gu,	mvc.getVertexCoverSolution());
        }
        
        System.out.println("=============================================");
        System.out.println("        Final results                        ");
        System.out.println("=============================================");

        
        System.out.print("Bruteforce:");
        System.out.println(result2_backup);
        
        System.out.print("Degree Branching:");
        System.out.println(result4_backup);
        
        System.out.print("Neidermeier:");
        System.out.println(result_backup);
        
        System.out.print("BussGoldsmith:");
        System.out.println(result3_backup);

        /*
           mvc.kVertexCoverBruteForce(gu, 4);

        System.out.println("result BruteForce ErdosRenyi k=4: " + mvc.getVertexCoverSolution());

        
    	 Graph<String,Integer> petersen = Import.readNet("src/agape/io/Petersen.net");
		 System.out.println(petersen);
				
		 mvc.kVertexCoverBruteForce(petersen, 3);
		 System.out.println("result BruteForce petersen k=3: " + mvc.getVertexCoverSolution());
		*/
    }
}