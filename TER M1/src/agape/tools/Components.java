/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections15.Factory;

import com.google.common.collect.Sets;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * This class provides methods for connected component computation.
 *
 * @author V. Levorato
 */
public class Components {

	// global maps for Tarjan algorithm
	private static HashMap indexmap;
	private static HashMap lowlinkmap;
	private static int index;
	private static ArrayList stackSC;
	private static ArrayList SCC;

	/**
	 * Same method as getConnectedComponent(Graph<V,E> G) with a set constraint
	 * representing nodes to be excluded.
	 *
	 * @param G             graph
	 * @param restrictNodes nodes to be excluded
	 * @return a connected component
	 */
	public static <V, E> Set<V> getConnectedComponent(Graph<V, E> G, Set<V> restrictNodes) {

		Set C = new HashSet();
		Stack<V> stack = new Stack();

		if (!G.getVertices().isEmpty()) {
			Iterator<V> itr = G.getVertices().iterator();
			V v = itr.next();

			while (restrictNodes.contains(v) && itr.hasNext())
				v = itr.next();

			if (!restrictNodes.contains(v)) {
				C.add(v);
				stack.add(v);
			}

			while (!stack.isEmpty()) {
				V x = stack.pop();
				Set nT = new HashSet(); // non-treated vertices
				Set Nx = new HashSet(G.getNeighbors(x));
				Nx.removeAll(restrictNodes);
				Sets.difference(Nx, C).copyInto(nT);
				stack.addAll(nT);
				C.addAll(nT);
			}
		}

		return C;

	}

	/**
	 * Return a connected component of G.
	 *
	 * @param G Graph in which component has to be found
	 * @return connected component of G
	 */
	public static <V, E> Set<V> getConnectedComponent(Graph<V, E> G) {

		Set C = new HashSet();
		Stack<V> stack = new Stack();

		if (!G.getVertices().isEmpty()) {
			V v = G.getVertices().iterator().next();

			C.add(v);
			stack.add(v);

			while (!stack.isEmpty()) {
				V x = stack.pop();
				Set nT = new HashSet(); // non-treated vertices
				Set Nx = new HashSet(G.getNeighbors(x));
				Sets.difference(Nx, C).copyInto(nT);
				stack.addAll(nT);
				C.addAll(nT);
			}
		}

		return C;

	}

	/**
	 * Same method as getAllConnectedComponent(Graph<V,E> G) with a set constraint
	 * representing nodes to be excluded.
	 *
	 * @param G             graph
	 * @param restrictNodes nodes to be excluded
	 * @return a list of all connected components
	 */
	public static <V, E> ArrayList<Set<V>> getAllConnectedComponent(Graph<V, E> G, Set restrictNodes) {

		ArrayList<Set<V>> CC = new ArrayList();
		Set<V> nodes = new HashSet(G.getVertices());
		nodes.removeAll(restrictNodes);

		while (!nodes.isEmpty()) {
			V vinit = null;
			Iterator<V> itr = nodes.iterator();
			boolean b = false;
			while (itr.hasNext() && !b) {
				V vtemp = itr.next();
				if (!restrictNodes.contains(vtemp)) {
					vinit = vtemp;
					b = true;
				}
			}

			if (vinit != null) {
				Set<V> C = new HashSet();
				Set<V> Ci = new HashSet();
				C.add(vinit);

				while (!Ci.equals(C)) {
					Ci = new HashSet(C);
					C.addAll(Operations.getNeighbors(G, Ci));
					C.removeAll(restrictNodes);
				}

				CC.add(C);
				nodes.removeAll(C);
			}

		}

		return CC;
	}

	/**
	 * Return all connected components of G.
	 *
	 * @param G Graph in which components have to be found
	 * @return list of all connected components of G.
	 */
	public static <V, E> ArrayList<Set<V>> getAllConnectedComponent(Graph<V, E> G) {
		ArrayList<Set<V>> CC = new ArrayList();
		ArrayList<Graph> memG = new ArrayList();
		while (!G.getVertices().isEmpty()) {
			try {
				Graph<V, E> Gp = G.getClass().newInstance();
				Set<V> C = Components.getConnectedComponent(G);
				CC.add(C);
				Operations.subGraph(G, Gp, C);
				memG.add(Gp);
				Operations.removeAllVertices(G, C);
			} catch (InstantiationException ex) {
				Logger.getLogger(Components.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				Logger.getLogger(Components.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		for (Graph GC : memG)
			Operations.mergeGraph(G, GC);

		return CC;
	}

	/**
	 * Returns ALL strongly connected components in a directed graph (Tarjan's
	 * algorithm).
	 *
	 * @param G graph
	 * @return a list of all strongly connected components
	 */
	public static <V, E> ArrayList<Set<V>> getAllStronglyConnectedComponent(Graph<V, E> G) {
		Components.indexmap = new HashMap<V, Integer>();
		Components.lowlinkmap = new HashMap<V, Integer>();
		Components.index = 0;
		Components.stackSC = new ArrayList<V>();
		Components.SCC = new ArrayList<Set<V>>();

		for (V v : G.getVertices()) {
			if (Components.indexmap.get(v) == null)
				Components.tarjan(v, G);
		}

		return Components.SCC;
	}

	private static <V, E> void tarjan(V v, Graph<V, E> G) {

		Components.indexmap.put(v, Components.index);
		Components.lowlinkmap.put(v, Components.index);
		Components.index++;
		Components.stackSC.add(0, v);
		for (E e : G.getOutEdges(v)) {
			V n = G.getDest(e);
			if (Components.indexmap.get(n) == null) {
				Components.tarjan(n, G);
				Components.lowlinkmap.put(v,
						Math.min((Integer) Components.lowlinkmap.get(v), (Integer) Components.lowlinkmap.get(n)));
			} else if (Components.stackSC.contains(n)) {
				Components.lowlinkmap.put(v,
						Math.min((Integer) Components.lowlinkmap.get(v), (Integer) Components.indexmap.get(n)));
			}
		}

		if ((Integer) Components.lowlinkmap.get(v) == (Integer) Components.indexmap.get(v)) {
			V n;
			Set<V> component = new HashSet<V>();
			do {
				n = (V) Components.stackSC.remove(0);
				component.add(n);
			} while (n != v);
			Components.SCC.add(component);
		}

	}

	/**
	 * Returns a graph which is the complement of G. A factory is needed to create
	 * the new edges of the complement graph.
	 *
	 * @param G        graph
	 * @param eFactory edge factory
	 * @return complement graph of G.
	 */
	public static <V, E> Graph<V, E> getComplementGraph(Graph<V, E> G, Factory<E> eFactory) {
		Graph<V, E> Gc = new UndirectedSparseGraph<V, E>();

		for (V v1 : G.getVertices())
			for (V v2 : G.getVertices())
				if (v1 != v2)
					if (G.findEdge(v1, v2) == null)
						Gc.addEdge(eFactory.create(), v1, v2);

		return Gc;
	}

}
