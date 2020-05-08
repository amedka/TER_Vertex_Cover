/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * This class allows to export graph into files.
 *
 * @author V. Levorato
 */

public class Export {

	/**
	 * Write a .net file (pajek format) corresponding to the graph G (undirected
	 * version).
	 *
	 * @param G     undirected graph.
	 * @param fname name of the file
	 */
	public static <V, E> void writeNet(Graph<V, E> G, String fname) {
		FileWriter fw = null;
		try {

			fw = new FileWriter(fname, false);
		} catch (Exception e) {
			System.err.println("Error writing: " + e);
		}

		BufferedWriter bfw = new BufferedWriter(fw);
		PrintWriter output = new PrintWriter(bfw);

		HashMap<V, Integer> posv = new HashMap<V, Integer>();
		Set<Pair<V>> done = new HashSet<Pair<V>>();

		output.println("*Vertices " + G.getVertexCount());
		int i = 1;
		for (V v : G.getVertices()) {
			output.println(i + " \"" + v.toString() + "\"");
			posv.put(v, i);
			i++;
		}

		output.println("*edgeslist");
		for (V v : G.getVertices()) {
			boolean first = true;
			for (V nv : G.getNeighbors(v)) {
				Pair<V> p1 = new Pair<V>(v, nv);
				Pair<V> p2 = new Pair<V>(nv, v);
				if (!done.contains(p1) && !done.contains(p2))
					if (first) {
						output.print(posv.get(v) + " ");
						output.print(posv.get(nv) + " ");
						done.add(p1);
						done.add(p2);
						first = false;
					} else {
						output.print(posv.get(nv) + " ");
						done.add(p1);
						done.add(p2);
					}
			}

			if (!first)
				output.println();
		}

		output.flush();
		output.close();
	}

	/**
	 * Write a .net file (pajek format) corresponding to the graph G (directed
	 * version).
	 *
	 * @param G     directed graph.
	 * @param fname name of the file
	 */
	public static <V, E> void writeDNet(Graph<V, E> G, String fname) {
		FileWriter fw = null;
		try {

			fw = new FileWriter(fname, false);
		} catch (Exception e) {
			System.err.println("Error writing: " + e);
		}

		BufferedWriter bfw = new BufferedWriter(fw);
		PrintWriter output = new PrintWriter(bfw);

		HashMap<V, Integer> posv = new HashMap<V, Integer>();
		Set<Pair<V>> done = new HashSet<Pair<V>>();

		output.println("*Vertices " + G.getVertexCount());
		int i = 1;
		for (V v : G.getVertices()) {
			output.println(i + " \"" + v.toString() + "\"");
			posv.put(v, i);
			i++;
		}

		output.println("*edgeslist");
		for (V v : G.getVertices()) {
			boolean first = true;
			for (V nv : G.getSuccessors(v)) {
				Pair<V> p = new Pair<V>(v, nv);
				if (!done.contains(p))
					if (first) {
						output.print(posv.get(v) + " ");
						output.print(posv.get(nv) + " ");
						done.add(p);
						first = false;
					} else {
						output.print(posv.get(nv) + " ");
						done.add(p);
					}
			}

			if (!first)
				output.println();
		}

		output.flush();
		output.close();
	}

	/**
	 * Write a .gv file (GraphViz format) corresponding to the graph G.
	 *
	 * @param fname name of the file
	 * @param G     graph
	 */
	public static <V, E> void writeGV(String fname, Graph<V, E> G) throws IOException {
		PrintWriter pfile;
		pfile = new PrintWriter(new BufferedWriter(new FileWriter(fname + ".gv")));

		pfile.println("digraph G {");
		pfile.println("size=\"100,20\"; ratio = auto;");
		pfile.println("node [style=filled];");

		for (E e : G.getEdges()) {
			pfile.println("\"" + G.getSource(e) + "\" -> \"" + G.getDest(e) + "\" [color=\"0.649 0.701 0.701\"];");
		}

		for (V x : G.getVertices()) {
			pfile.print("\"" + x + "\" ");
			pfile.print("[color=grey];\n");
		}

		pfile.println("}");
		pfile.flush();
		pfile.close();

	}

}
