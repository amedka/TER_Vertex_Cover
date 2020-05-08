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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import agape.tools.Operations;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.graph.Graph;
import tools.dataStructures.Pair;

/**
 * This class allows to generate random graphs.
 *
 * @author V. Levorato
 * @author P. Berthome, J.-F. Lalande
 */
public class RandGenerator<V, E> {

	/**
	 * @param <V>
	 * @param <E>
	 * @param graphFactory  graph factory
	 * @param vertexFactory vertex factory
	 * @param edgeFactory   edge factory
	 * @param n             number of vertices
	 * @param p             probability of connectedness (between 0.0 and 1.0)
	 * @return Erdös-Rényi random graph
	 * @see "Erdös, P. and Rényi, A., 1959. On random graphs. Publ Math Debrecen, 6(290-297), p.290-297."
	 */
	public static <V, E> Graph<V, E> generateErdosRenyiGraph(Factory<Graph<V, E>> graphFactory,
			Factory<V> vertexFactory, Factory<E> edgeFactory, int n, double p) {
		Graph<V, E> G = graphFactory.create();

		for (int i = 0; i < n; i++)
			G.addVertex(vertexFactory.create());

		@SuppressWarnings("unchecked")
		V[] varray = (V[]) G.getVertices().toArray();
		for (int i = 0; i < varray.length; i++)
			for (int j = i; j < varray.length; j++)
				if (Math.random() <= p) {
					if (varray[i] != varray[j])
						G.addEdge(edgeFactory.create(), varray[i], varray[j]);
				}

		return G;

	}

	/**
	 * This method generates Eppstein power law graph. Generator from JUNG library
	 * is used in this method.
	 *
	 * @param graphFactory  graph factory
	 * @param vertexFactory vertex factory
	 * @param edgeFactory   edge factory
	 * @param n             number of vertices
	 * @param e             number of edges
	 * @param i             the number of iterations to use: the larger the value
	 *                      the better the graph's degree distribution will
	 *                      approximate a power-law
	 * @return Eppstein power law graph
	 * @see "Eppstein, D. and Wang, J., 2002. A steady state model for graph power laws. In 2nd Int. Worksh. Web Dynamics. p. 8."
	 */
	public static <V, E> Graph<V, E> generateEppsteinGraph(Factory<Graph<V, E>> graphFactory, Factory<V> vertexFactory,
			Factory<E> edgeFactory, int n, int e, int i) {
		return new EppsteinPowerLawGenerator<V, E>(graphFactory, vertexFactory, edgeFactory, n, e, i).create();
	}

	/**
	 * This method generates Barabasi-Albert power law graph. Generator from JUNG
	 * library is used in this method.
	 *
	 * @param graphFactory  graph factory
	 * @param vertexFactory vertex factory
	 * @param edgeFactory   edge factory
	 * @param n             amount of nodes
	 * @param e             the number of edges that should be attached from the new
	 *                      vertex to pre-existing vertices at each time step
	 * @param steps         number of steps to iterate
	 * @see "﻿Barabasi, A.-L. and Albert, R., 1999. Emergence of scaling in random networks. Science, 286(5439), p.11."
	 * @return Barabasi-Albert power law graph with n+steps vertices and steps * e
	 *         edges.
	 * @throws Exception if too many edges are requested to add
	 */
	public static <V, E> Graph<V, E> generateBarabasiAlbertGraph(Factory<Graph<V, E>> graphFactory,
			Factory<V> vertexFactory, Factory<E> edgeFactory, int n, int e, int steps) throws Exception {
		if (e > n)
			throw new Exception("Impossible to attach the " + e + " new edges to " + n + " vertices.");

		Set<V> vset = new HashSet<V>();
		for (int i = 0; i < n; i++)
			vset.add(vertexFactory.create());

		BarabasiAlbertGenerator<V, E> BAgen = new BarabasiAlbertGenerator<V, E>(graphFactory, vertexFactory,
				edgeFactory, n, e, vset);

		BAgen.evolveGraph(steps);
		return BAgen.create();
	}

	/**
	 * @param <V>
	 * @param <E>
	 * @param graphFactory  graph factory
	 * @param vertexFactory vertex factory
	 * @param edgeFactory   edge factory
	 * @param width         the width of the grid (in number of vertices)
	 * @param height        the height of the grid (in number of vertices)
	 * @param p             Manhattan distance of the all nodes to connect to the
	 *                      neighbors of a node v
	 * @param r             A constant to manage the probability of connecting a
	 *                      node v to a long distance node
	 * @param q             The number of long distance neighbors of v
	 * @return Kleinberg small-world graph
	 * @see "﻿Kleinberg, J., 2000. The small-world phenomenon. In The thirty-second annual ACM symposium on Theory of computing. New York, New York, USA: ACM Press, pp. 163-170."
	 */
	public static <V, E> Graph<V, E> generateKleinbergSWGraph(Factory<Graph<V, E>> graphFactory,
			Factory<V> vertexFactory, Factory<E> edgeFactory, int width, int height, int p, int q, double r) {
		// V[][] map = new V[width][height];
		Graph<V, E> g = graphFactory.create();

		// Creating vertices
		ArrayList<ArrayList<V>> map = new ArrayList<ArrayList<V>>(width);
		for (int i = 0; i < width; i++) {
			map.add(new ArrayList<V>(height));
		}

		// Putting vertices in the 2D map
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				V v = vertexFactory.create();
				map.get(i).add(j, v);
				g.addVertex(v);
			}
		}

		// One node
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				V v = map.get(i).get(j);

				// Second node
				double sum_proba = 0.0;
				for (int k = 0; k < width; k++) {

					for (int l = 0; l < height; l++) {
						int manhattan_distance = Math.abs(i - k) + Math.abs(j - l);
						if ((i != k) || (j != l)) {
							// Grid + local neighborhood
							if (manhattan_distance <= p) {
								V v2 = map.get(k).get(l);
								E e = edgeFactory.create();
								g.addEdge(e, v, v2);
							} else
								sum_proba += Math.pow(manhattan_distance, -r);
						}
					}
				}

				// Adding long distance neighbors
				for (int q2 = 0; q2 < q; q2++) {
					double tirage = Math.random() * sum_proba;
					double sum_search = 0.0;
					boolean generated = false;
					for (int k = 0; k < width && !generated; k++) {
						for (int l = 0; l < height && !generated; l++) {
							int manhattan_distance = Math.abs(i - k) + Math.abs(j - l);
							if (manhattan_distance > p) {
								sum_search += Math.pow(manhattan_distance, -r);
								if (sum_search >= tirage) {
									V v2 = map.get(k).get(l);
									if (!Operations.isEdge(g, v, v2)) {
										E e = edgeFactory.create();
										g.addEdge(e, v, v2);
										generated = true;
									}
								}

							}
						}
					}
				}
			}
		}

		return g;
	}

	/**
	 * @param <V>
	 * @param <E>
	 * @param graphFactory  graph factory
	 * @param vertexFactory vertex factory
	 * @param edgeFactory   edge factory
	 * @param n             number of vertices
	 * @param k             mean degree of the graph
	 * @param p             probability of rewiring an edge
	 * @return Watts-Strogatz small-world graph
	 * @see "Watts, D.J. and Strogatz, S., 1998. Collective dynamics of 'small-world' networks. Nature, 393, p.440-442."
	 * @throws Exception when the probability is not in [0,1]
	 */
	public static <V, E> Graph<V, E> generateWattsStrogatzSWGraph(Factory<Graph<V, E>> graphFactory,
			Factory<V> vertexFactory, Factory<E> edgeFactory, int n, int k, double p) throws Exception {

		if ((p < 0) || (p > 1))
			throw new Exception("Probabilities must be between 0 and 1");

		Graph<V, E> G = NRandGenerator.generateRegularRing(graphFactory, vertexFactory, edgeFactory, n, k);

		Set<V> vertices = new HashSet<V>(G.getVertices());

		for (V v : vertices) {
			Set<V> Nv = new HashSet<V>(G.getNeighbors(v));
			for (V nv : Nv)
				if (Math.random() < p) {
					G.removeEdge(G.findEdge(v, nv));
					int j = 0, choose = (int) (Math.random() * n);
					V new_nv = null;
					Iterator<V> itr = G.getVertices().iterator();
					while (itr.hasNext() && j < choose) {
						new_nv = itr.next();
						j++;
					}

					if (!Operations.isEdge(G, v, new_nv) && new_nv != null)
						G.addEdge(edgeFactory.create(), v, new_nv);
				}
		}

		return G;
	}

	/**
	 * This method generates a random regular graph.
	 *
	 * @param graphFactory  graph factory
	 * @param vertexFactory vertex factory
	 * @param edgeFactory   edge factory
	 * @param n             number of vertices
	 * @param d             degree of the graph
	 * @return random regular graph
	 */
	public static <V, E> Graph<V, E> generateRandomRegularGraph(Factory<Graph<V, E>> graphFactory,
			Factory<V> vertexFactory, Factory<E> edgeFactory, int n, int d) {
		Graph<V, E> G;

		do {
			G = graphFactory.create();
			for (int i = 0; i < n; i++)
				G.addVertex(vertexFactory.create());

			// fill S (all anti-edges)
			Set<Pair<V, V>> S = new HashSet<Pair<V, V>>();
			for (V u : G.getVertices())
				for (V v : G.getVertices()) {
					if (u != v) {
						Pair<V, V> p1 = new Pair<V, V>(u, v);
						Pair<V, V> p2 = new Pair<V, V>(v, u);
						if (!S.contains(p1) && !S.contains(p2))
							S.add(p1);
					}
				}

			while (!S.isEmpty()) {
				// choose an anti-edge to transform it to an edge in G
				boolean stop = false;
				Pair<V, V> pchoosen = null;
				Iterator<Pair<V, V>> itr = S.iterator();
				while (!stop) {
					if (!itr.hasNext())
						itr = S.iterator();
					else {
						Pair<V, V> p = itr.next();
						double pp = ((double) (d - G.degree(p.getKey())) * (d - G.degree(p.getValue()))) / S.size();
						if (Math.random() < pp) {
							pchoosen = p;
							stop = true;
						}
					}
				}

				S.remove(pchoosen);
				G.addEdge(edgeFactory.create(), pchoosen.getKey(), pchoosen.getValue());

				// clean S
				Set<Pair<V, V>> toRemove = new HashSet<Pair<V, V>>();
				for (Pair<V, V> p : S) {
					int deg1 = G.degree(p.getKey());
					int deg2 = G.degree(p.getValue());
					if (deg1 > d - 1 || deg2 > d - 1)
						toRemove.add(p);
				}

				S.removeAll(toRemove);
			}

		} while (!Operations.isRegular(G, d));

		return G;
	}
}
