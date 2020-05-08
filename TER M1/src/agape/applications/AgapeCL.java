/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.applications;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import agape.algos.Algorithms;
import agape.algos.Coloring;
import agape.algos.MIS;
import agape.algos.MVC;
import agape.algos.MinDFVS;
import agape.algos.Separators;
import agape.io.Import;
import agape.tools.Operations;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * Agape Commande Line interface to exponential and parametrized algorithms.
 *
 * @author V. Levorato
 */
public class AgapeCL {

	static Set S = new HashSet();

	static final int MISBF = 100;
	static final int MISMM = 101;
	static final int MISDegMax = 102;
	static final int MISFGK = 103;
	static final int MVCBF = 104;
	static final int MVCBG = 105;
	static final int MVCN = 106;
	static final int MVCDBS = 107;
	static final int DFVS = 108;
	static final int CN = 109;
	static final int SEP = 110;

	public static ArrayList<File> listFiles(String directoryPath) {
		ArrayList<File> files = new ArrayList();
		File directoryToScan = new File(directoryPath);
		files = new ArrayList(Arrays.asList(directoryToScan.listFiles()));
		return files;
	}

	public static double launchAlgorithm(Graph<String, Integer> G, int algo, boolean directed) {

		final int ei = G.getEdgeCount();
		final int vi = G.getVertexCount();

		Factory<Integer> edgeFactory = new Factory<Integer>() {
			int c = ei;

			@Override
			public Integer create() {
				this.c++;
				return this.c;
			}
		};

		Factory<String> vertexFactory = new Factory<String>() {
			int c = vi;

			@Override
			public String create() {
				this.c++;
				return "v" + this.c;
			}
		};

		Factory<Graph<String, Integer>> graphFactory = new Factory<Graph<String, Integer>>() {
			@Override
			public Graph<String, Integer> create() {
				return new UndirectedSparseGraph<String, Integer>();
			}
		};

		Factory<Graph<String, Integer>> DgraphFactory = new Factory<Graph<String, Integer>>() {
			@Override
			public Graph<String, Integer> create() {
				return new DirectedSparseGraph<String, Integer>();
			}
		};

		Algorithms<String, Integer> alg;

		double t = 0.0;

		int k = 0;

		switch (algo) {
		case MISBF:
			alg = new MIS(graphFactory, vertexFactory, edgeFactory);
			t = System.currentTimeMillis();
			AgapeCL.S = ((MIS) alg).maximumIndependentSetBruteForce(G);
			t = System.currentTimeMillis() - t;
			break;

		case MISMM:
			alg = new MIS(graphFactory, vertexFactory, edgeFactory);
			t = System.currentTimeMillis();
			AgapeCL.S = ((MIS) alg).maximumIndependentSetMoonMoser(G);
			t = System.currentTimeMillis() - t;
			break;

		case MISDegMax:
			alg = new MIS(graphFactory, vertexFactory, edgeFactory);
			t = System.currentTimeMillis();
			AgapeCL.S = ((MIS) alg).maximumIndependentSetMaximumDegree(G);
			t = System.currentTimeMillis() - t;
			break;

		case MISFGK:
			alg = new MIS(graphFactory, vertexFactory, edgeFactory);
			t = System.currentTimeMillis();
			AgapeCL.S = ((MIS) alg).maximuRmIndependentSetFominGrandoniKratsch(G);
			t = System.currentTimeMillis() - t;
			break;

		case MVCBF:
			alg = new MVC(graphFactory);
			k = ((MVC) alg).greedyCoverMaxDegree(G).size();
			t = System.currentTimeMillis();
			((MVC) alg).kVertexCoverBruteForce(G, k);
			AgapeCL.S = ((MVC) alg).getVertexCoverSolution();
			t = System.currentTimeMillis() - t;
			break;

		case MVCBG:
			alg = new MVC(graphFactory);
			k = ((MVC) alg).greedyCoverMaxDegree(G).size();
			t = System.currentTimeMillis();
			((MVC) alg).kVertexCoverBussGoldsmith(G, k);
			AgapeCL.S = ((MVC) alg).getVertexCoverSolution();
			t = System.currentTimeMillis() - t;
			break;

		case MVCDBS:
			alg = new MVC(graphFactory);
			k = ((MVC) alg).greedyCoverMaxDegree(G).size();
			t = System.currentTimeMillis();
			((MVC) alg).kVertexCoverDegreeBranchingStrategy(G, k);
			AgapeCL.S = ((MVC) alg).getVertexCoverSolution();
			t = System.currentTimeMillis() - t;
			break;

		case MVCN:
			alg = new MVC(graphFactory);
			k = ((MVC) alg).greedyCoverMaxDegree(G).size();
			t = System.currentTimeMillis();
			((MVC) alg).kVertexCoverNiedermeier(G, k);
			AgapeCL.S = ((MVC) alg).getVertexCoverSolution();
			t = System.currentTimeMillis() - t;
			break;

		case DFVS:
			alg = new MinDFVS(DgraphFactory, edgeFactory);
			Graph<String, Integer> Gp = null;
			Gp = Operations.copyDirectedSparseGraph(G);

			t = System.currentTimeMillis();
			AgapeCL.S = ((MinDFVS) alg).maximumDirectedAcyclicSubset(Gp);
			t = System.currentTimeMillis() - t;
			Set<String> FVS = new HashSet(G.getVertices());
			FVS.removeAll(AgapeCL.S);
			AgapeCL.S = FVS;
			break;

		case CN:
			alg = new Coloring(graphFactory);
			t = System.currentTimeMillis();
			AgapeCL.S = ((Coloring) alg).graphColoring(G);
			t = System.currentTimeMillis() - t;
			break;

		case SEP:
			alg = new Separators();
			t = System.currentTimeMillis();
			AgapeCL.S = ((Separators) alg).getAllMinimalSeparators(G);
			t = System.currentTimeMillis() - t;
			break;
		}

		return t;
	}

	/*
	 * Use: arguments: graphfilepath|graphdirectorypath algorithm [D] (for directed
	 * graph)
	 */

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.print("Usage: \n" + "java -jar AGAPE.jar graphfilepath|graphdirectorypath algorithm \n\n"
					+ "graphfilepath|graphdirectorypath:    use mygraph.net to apply an algorithm on a simple graph, use mydirectory/ to apply algorithm on a series of graph.\n"
					+ "algorithm:   specify algorithm (CN, MISBF, MISMM, MISDegMax, MISFGK, MVCBF, MVCBG, MVCDBS, MVCN, DFVS, SEP).\n"
					+ "");

			return;
		}

		String folder = args[0];

		int algo = 0;
		if (args[1].equals("CN"))
			algo = AgapeCL.CN;
		if (args[1].equals("SEP"))
			algo = AgapeCL.SEP;
		if (args[1].equals("MISBF"))
			algo = AgapeCL.MISBF;
		if (args[1].equals("MISMM"))
			algo = AgapeCL.MISMM;
		if (args[1].equals("MISDegMax"))
			algo = AgapeCL.MISDegMax;
		if (args[1].equals("MISFGK"))
			algo = AgapeCL.MISFGK;
		if (args[1].equals("MVCBF"))
			algo = AgapeCL.MVCBF;
		if (args[1].equals("MVCBG"))
			algo = AgapeCL.MVCBG;
		if (args[1].equals("MVCDBS"))
			algo = AgapeCL.MVCDBS;
		if (args[1].equals("MVCN"))
			algo = AgapeCL.MVCN;
		if (args[1].equals("DFVS"))
			algo = AgapeCL.DFVS;

		if (folder.contains(".net")) {
			Graph<String, Integer> G = null;
			if (algo == AgapeCL.DFVS) {
				System.out.println("Reading a directed graph...");
				G = Import.readDNet(folder);
			} else {
				System.out.println("Reading an undirected graph...");
				G = Import.readNet(folder);
			}

			System.out.println("Read from " + folder + ": ");

			System.out.println("V:" + G.getVertexCount() + " E:" + G.getEdgeCount());
			AgapeCL.Bench(G, algo);

			System.out.println();
		} else {
			ArrayList<File> graphs = AgapeCL.listFiles(folder);
			for (File f : graphs) {
				if (f.getName().contains(".net")) {
					Graph<String, Integer> G = null;
					if (algo == AgapeCL.DFVS)
						G = Import.readDNet(f.getPath());
					else
						G = Import.readNet(f.getPath());

					System.out.println(f.getName());
					System.out.println("V:" + G.getVertexCount() + " E:" + G.getEdgeCount());
					AgapeCL.Bench(G, algo);

					System.out.println();
				}
			}
		}

	}

	public static void Bench(Graph<String, Integer> G, int algo) {

		double t = 0;

		if (algo == AgapeCL.DFVS)
			t = AgapeCL.launchAlgorithm(G, algo, true);
		else
			t = AgapeCL.launchAlgorithm(G, algo, false);

		System.out.println(AgapeCL.printTime(t));
		System.out.println("Size: " + AgapeCL.S.size());
		System.out.println(AgapeCL.S);
	}

	/**
	 * @deprecated you must send the factories to the constructor of alogirhtms.
	 */
	@Deprecated
	public static void initFactories(Algorithms<String, Integer> alg, Factory<Graph<String, Integer>> gf,
			Factory<String> vf, Factory<Integer> ef) {
		alg.setEdgeFactoy(ef);
		alg.setVertexFactoy(vf);
		alg.setGraphFactoy(gf);
	}

	public static String printTime(double t) {

		String r = "";

		if (t < 1000)
			r = t + " ms";
		else {
			double tt = t / 1000;
			if (tt < 60)
				r += tt + " s";
			else {
				tt = tt / 60;
				if (tt < 60)
					r += tt + " mins";
				else {
					tt = tt / 60;
					if (tt < 24)
						r += tt + " h";
					else {
						tt = tt / 24;
						r += tt + " days";
					}
				}
			}
		}

		return r;
	}

}
