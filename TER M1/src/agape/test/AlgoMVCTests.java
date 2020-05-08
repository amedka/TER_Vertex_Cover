/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.test;

import agape.algos.MVC;
import agape.generators.RandGenerator;
import agape.io.Import;
import agape.tutorials.UndirectedGraphFactoryForStringInteger;
import agape.visu.Visualization;
import edu.uci.ics.jung.graph.Graph;

public class AlgoMVCTests {
	public static void main(String[] args) {

		System.out.println("----------------");
		System.out.println("UNDIRECTED GRAPH");
		System.out.println("----------------");

		UndirectedGraphFactoryForStringInteger factory = new UndirectedGraphFactoryForStringInteger(1, 1);
		RandGenerator<String, Integer> generator = new RandGenerator<String, Integer>();
		Graph<String, Integer> gu = RandGenerator.generateErdosRenyiGraph(factory, factory.vertexFactory,
				factory.edgeFactory, 29, 0.4);
		System.out.println("Generating an Erdos Renyi graph:");

		System.out.println(gu);

		MVC<String, Integer> mvc = new MVC<String, Integer>(factory);

		mvc.kVertexCoverBruteForce(gu, 4);
		System.out.println("result BruteForce ErdosRenyi k=4: " + mvc.getVertexCoverSolution());

		Graph<String, Integer> petersen = Import.readNet("src/agape/io/Petersen.net");
		System.out.println(petersen);

		mvc.kVertexCoverBruteForce(petersen, 3);
		System.out.println("result BruteForce petersen k=3: " + mvc.getVertexCoverSolution());

		Visualization.showGraph(petersen, mvc.getVertexCoverSolution());
	}
}