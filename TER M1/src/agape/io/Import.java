/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import mascoptLib.core.MascoptEdge;
import mascoptLib.core.MascoptGraph;
import mascoptLib.core.MascoptVertex;
import mascoptLib.io.reader.mgl.dom.MGLDOMReader;

/**
 * This class allows to import files into graph.
 *
 * @author V. Levorato
 */

public class Import {

	/**
	 * Transform a MGL file format (Mascopt) in JUNG graph.
	 *
	 * @param fname file path
	 * @return graph
	 */
	public static Graph<String, Integer> MGLtoJungGraph(String fname) {
		Graph<String, Integer> g = new UndirectedSparseGraph<String, Integer>();

		MGLDOMReader mglR = null;

		try {

			mglR = new MGLDOMReader(fname);
			mglR.parse();

		} catch (FileNotFoundException ex) {
			Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
		}

		MascoptGraph G = (MascoptGraph) mglR.getGraphs().next();

		for (MascoptVertex v : G.vertexSet())
			g.addVertex(v.toString());

		int k = 0;

		for (MascoptEdge e : G.edgeSet()) {
			g.addEdge(k, e.toArray()[0].toString(), e.toArray()[1].toString());
			k++;
		}

		return g;
	}

	/**
	 * Returns an undirected graph by reading a .net file (pajek format).
	 *
	 * @param fname file path
	 * @return graph
	 */
	public static Graph<String, Integer> readNet(String fname) {
		File f = new File(fname);
		BufferedReader lecteurAvecBuffer = null;
		String ligne;

		Graph<String, Integer> G = new UndirectedSparseGraph<String, Integer>();

		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException exc) {
			System.err.println("Error opening file .net\n");
		}

		ArrayList<String> V = new ArrayList<String>();

		try {

			ligne = lecteurAvecBuffer.readLine();

			ligne = lecteurAvecBuffer.readLine();

			// adding vertices
			while (!ligne.equals("*edgeslist")) {
				StringTokenizer tok = new StringTokenizer(ligne, "\"");

				tok.nextToken();
				String v = tok.nextToken();
				// v=v.replaceAll("\"","");
				V.add(v);
				G.addVertex(v);

				ligne = lecteurAvecBuffer.readLine();
			}

			// adding edges
			int ne = 0;
			ligne = lecteurAvecBuffer.readLine();
			while (ligne != null) {
				StringTokenizer tok = new StringTokenizer(ligne, " ");
				int s = Integer.valueOf(tok.nextToken()) - 1;
				while (tok.hasMoreTokens()) {
					int t = Integer.valueOf(tok.nextToken()) - 1;
					if (!V.isEmpty())
						G.addEdge(ne, V.get(s), V.get(t));
					else
						G.addEdge(ne, Integer.toString(s), Integer.toString(t));
					ne++;
				}

				ligne = lecteurAvecBuffer.readLine();
			}

			lecteurAvecBuffer.close();

		} catch (Exception e) {
			System.err.println("Error parsing file: " + e + "\n");
		}

		return G;
	}

	/**
	 * Returns an undirected graph by reading a .trg file TRG means Trace Graphe It
	 * is the format exported by System.out.println(g); For example:
	 * Vertices:v53,v50,v58 Edges:254[v58,v53] 255[v50,v58]
	 *
	 * @param fname file path
	 * @return graph
	 */
	public static Graph<String, Integer> readTRG(String fname) {
		File f = new File(fname);
		BufferedReader lecteurAvecBuffer = null;
		String ligneV;
		String ligneE;

		Graph<String, Integer> G = new UndirectedSparseGraph<String, Integer>();

		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException exc) {
			System.err.println("Error opening file .net\n");
		}

		try {

			ligneV = lecteurAvecBuffer.readLine();

			StringTokenizer tokenV = new StringTokenizer(ligneV, ":,");
			tokenV.nextToken(); // skip "Vertices"
			while (tokenV.hasMoreTokens()) {
				G.addVertex(new String(tokenV.nextToken()));
				// System.out.println("" + G);
			}

			ligneE = lecteurAvecBuffer.readLine();
			StringTokenizer tokenE = new StringTokenizer(ligneE, ": ");
			tokenE.nextToken(); // skip "Edges"
			while (tokenE.hasMoreTokens()) {
				String edge = tokenE.nextToken();
				StringTokenizer EdgeT = new StringTokenizer(edge, ",\\[\\]");
				G.addEdge(Integer.parseInt(EdgeT.nextToken()), EdgeT.nextToken(), EdgeT.nextToken());
				// System.out.println("" + G);
			}

			lecteurAvecBuffer.close();

		} catch (Exception e) {
			System.err.println("Error parsing file: " + e + "\n");
		}

		return G;
	}

	/**
	 * Returns a directed graph by reading a .net file (pajek format).
	 *
	 * @param fname file path
	 * @return graph
	 */
	public static Graph<String, Integer> readDNet(String fname) {
		File f = new File(fname);
		BufferedReader lecteurAvecBuffer = null;
		String ligne;

		Graph<String, Integer> G = new DirectedSparseGraph<String, Integer>();

		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException exc) {
			System.err.println("Error opening file .net\n");
		}

		ArrayList<String> V = new ArrayList<String>();

		try {

			ligne = lecteurAvecBuffer.readLine();

			ligne = lecteurAvecBuffer.readLine();

			// adding vertices
			while (!ligne.equals("*edgeslist")) {
				StringTokenizer tok = new StringTokenizer(ligne, "\"");

				tok.nextToken();
				String v = tok.nextToken();
				// v=v.replaceAll("\"","");
				V.add(v);
				G.addVertex(v);

				ligne = lecteurAvecBuffer.readLine();
			}

			// adding arcs
			int ne = 0;
			ligne = lecteurAvecBuffer.readLine();
			while (ligne != null) {
				StringTokenizer tok = new StringTokenizer(ligne, " ");
				int s = Integer.valueOf(tok.nextToken()) - 1;
				while (tok.hasMoreTokens()) {
					int t = Integer.valueOf(tok.nextToken()) - 1;
					if (!V.isEmpty())
						G.addEdge(ne, V.get(s), V.get(t));
					else
						G.addEdge(ne, Integer.toString(s), Integer.toString(t));
					ne++;
				}

				ligne = lecteurAvecBuffer.readLine();
			}

			lecteurAvecBuffer.close();

		} catch (Exception e) {
			System.err.println("Error parsing file: " + e + "\n");
			e.printStackTrace();
		}

		return G;
	}

	/**
	 * Returns a graph by reading a .tgf format (Mathematica format).
	 *
	 * @param fname file path
	 * @return graph
	 */
	public static Graph<String, Integer> readTGF(String fname) {
		File f = new File(fname);
		BufferedReader lecteurAvecBuffer = null;
		String ligne;

		Graph<String, Integer> G = new UndirectedSparseGraph<String, Integer>();

		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException exc) {
			System.err.println("Error opening file\n");
		}

		ArrayList<String> V = new ArrayList<String>();

		try {

			ligne = lecteurAvecBuffer.readLine();

			// adding vertices
			while (!ligne.equals("#")) {

				String v = ligne;
				V.add(v);
				G.addVertex(v);

				ligne = lecteurAvecBuffer.readLine();
			}

			// adding edges
			int ne = 0;
			ligne = lecteurAvecBuffer.readLine();
			while (ligne != null) {
				StringTokenizer tok = new StringTokenizer(ligne, " ");
				int s = Integer.valueOf(tok.nextToken());
				int t = Integer.valueOf(tok.nextToken());

				if (!V.isEmpty())
					G.addEdge(ne, V.get(s - 1), V.get(t - 1));
				else
					G.addEdge(ne, Integer.toString(s), Integer.toString(t));

				ne++;

				ligne = lecteurAvecBuffer.readLine();
			}

			lecteurAvecBuffer.close();

		} catch (Exception e) {
			System.err.println("Error reading: " + e + "\n");
		}

		return G;
	}

}
