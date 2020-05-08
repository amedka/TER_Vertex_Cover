/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.algos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.collections15.Factory;

import com.google.common.collect.Sets;

import agape.tools.Components;
import agape.tools.Operations;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * This class is dedicated to the minimum directed feedback vertex set. This
 * problem consists in finding the minimum number of vertices to delete in order
 * to get a directed acyclic graph from a directed graph.
 *
 * @author V. Levorato
 * @author J.-F. Lalande
 * @author P. Berthome
 * @param <V> Vertices type
 * @param <E> Edges type
 */
public class MinDFVS<V, E> extends Algorithms<V, E> {

	// used by MAS algorithm
	final int UM = 10; // UnMarked
	final int LM = 11; // LeftMarked
	final int RM = 12; // RightMarked
	final int WLM = 13; // WeakLeftMarked
	final int WRM = 14; // WeakRightMarked
	final int LMD = 15; // LeftMarkedDisconnected
	final int RMD = 16; // RightMarkedDisconnected

	// used by Tarjan circuits enumeration method
	private Set<V> Tmarked = new HashSet<V>();
	private Stack<V> Mstack = new Stack<V>();
	private Stack<V> Pstack = new Stack<V>();
	private HashMap<V, Integer> Tnodes = new HashMap<V, Integer>();

	public MinDFVS(Factory<Graph<V, E>> graphFactory, Factory<E> edgeFactory) {
		this.graphFactory = graphFactory;
		this.edgeFactory = edgeFactory;

	}

	/**
	 * Enumerate all the elementary circuits of a directed graph. The method is
	 * based on R. Tarjan paper "Enumeration of the elementary circuits of a
	 * directed graph" (1973)
	 *
	 * @param G graph
	 * @return return a set of lists where a list corresponds to a path.
	 */
	public Set<ArrayList<V>> enumAllCircuitsTarjan(Graph<V, E> G) {
		Set<ArrayList<V>> cycles = new HashSet<ArrayList<V>>();
		this.Tmarked = new HashSet<V>();
		this.Mstack = new Stack<V>();
		this.Pstack = new Stack<V>();
		this.Tnodes = new HashMap<V, Integer>();

		Iterator<V> itr = G.getVertices().iterator();
		for (int i = 1; i <= G.getVertexCount(); i++)
			this.Tnodes.put(itr.next(), i);

		for (V v : G.getVertices()) {
			this.BackTrack(v, v, G, cycles);
			while (!this.Mstack.isEmpty()) {
				V u = this.Mstack.pop();
				this.Tmarked.remove(u);
			}
		}

		return cycles;
	}

	private boolean BackTrack(V v, V source, Graph<V, E> G, Set<ArrayList<V>> cycles) {
		boolean f = false;

		this.Pstack.push(v);
		this.Tmarked.add(v);
		this.Mstack.push(v);

		for (V w : G.getSuccessors(v)) {
			if (this.Tnodes.get(w) >= this.Tnodes.get(source)) {
				if (w == source) {
					ArrayList<V> path = new ArrayList<V>(this.Pstack);
					cycles.add(path);
					f = true;
				} else {
					if (!this.Tmarked.contains(w))
						f = this.BackTrack(w, source, G, cycles) || f;
				}
			}
		}
		if (f) {
			while (this.Mstack.peek() != v) {
				V u = this.Mstack.pop();
				this.Tmarked.remove(u);
			}

			this.Mstack.remove(v);
			this.Tmarked.remove(v);
		}

		this.Pstack.remove(v);

		return f;
	}

	private MarkMap<V> initMarkedMap() {
		MarkMap<V> map = new MarkMap<V>();
		map.initRole(this.UM);
		map.initRole(this.LM);
		map.initRole(this.RM);
		map.initRole(this.WLM);
		map.initRole(this.WRM);
		map.initRole(this.LMD);
		map.initRole(this.RMD);

		return map;
	}

	/**
	 * This method gives an approximation of the FVS problem using Big Degree
	 * heuristic.
	 *
	 * @param G graph
	 * @return approximative size of a FVS in G
	 */
	public Set<V> greedyMinFVS(Graph<V, E> G) {
		Set<V> aset = new HashSet<V>();
		Graph<V, E> Gp = Operations.copyGraph(G, this.graphFactory);
		while (!Operations.isAcyclic(Gp)) {
			int degmax = 0;
			V toremove = null;
			for (V v : Gp.getVertices()) {
				if (Gp.getNeighborCount(v) > degmax) {
					degmax = Gp.getNeighborCount(v);
					toremove = v;
				}
			}

			aset.add(toremove);
			Gp.removeVertex(toremove);
		}

		return aset;

	}

	/**
	 * Return the Maximum Directed Acyclic Subset of G. This method is based on the
	 * Razgon work ("Computing Minimum Directed Feedback Vertex Set in O*(1.9977^n)"
	 * 2007). The algorithm solves the problem in time O(1.9977^n). The complement
	 * of the solution gives a directed FVS. It uses kernelization techniques
	 * presented in "A 4k2 kernel for feedback vertex set" by Thomassé 2010.
	 *
	 * @param Ginit graph
	 * @return a Maximum Directed Acyclic Subset of G.
	 */
	public Set<V> maximumDirectedAcyclicSubset(Graph<V, E> Ginit) {
		Graph<V, E> G = Operations.copyGraph(Ginit, this.graphFactory);

		MarkMap<V> R = this.initMarkedMap();
		for (V v : G.getVertices())
			R.addV(v, this.UM);

		Set<V> MAS = new HashSet<V>(G.getVertices()), MAStemp = new HashSet<V>();

		while (!MAS.equals(MAStemp)) {
			MAS = new HashSet<V>(MAStemp);
			this.KernelizationMAS(G, MAStemp);
		}

		MAS.addAll(this.MASAlgorithm(G, R));

		return MAS;

	}

	/**
	 * Computes the minimum directed acyclic subset of the graph G. Uses the
	 * maximumDirectedAcyclicSubset method.
	 *
	 * @param G graph
	 * @return the minimum directed acyclic subset of G
	 * @see agape.algos.MinDFVS#maximumDirectedAcyclicSubset
	 */
	public Set<V> minimumFeedbackVertexSet(Graph<V, E> G) {
		Set<V> res = new HashSet<V>();
		res.addAll(G.getVertices());
		res.removeAll(this.maximumDirectedAcyclicSubset(G));
		return res;
	}

	// used by MAS method
	private void CompressGraph(Graph<V, E> G, V v) {
		Set<V> loopvertices = new HashSet<V>();

		for (V u : G.getPredecessors(v))
			for (V w : G.getSuccessors(v))
				if (u == w)
					loopvertices.add(u);
				else {
					if (!G.isSuccessor(u, w))
						G.addEdge(this.edgeFactory.create(), u, w);
				}

		Operations.removeAllVertices(G, loopvertices);
		G.removeVertex(v);

	}

	// used by MAS method
	private void KernelizationMAS(Graph<V, E> G, Set<V> S) {

		Set<V> toRemove = new HashSet<V>();

		// Self-loop
		for (V v : G.getVertices()) {
			if (G.findEdge(v, v) != null)
				toRemove.add(v);
		}

		Operations.removeAllVertices(G, toRemove);

		// System.out.println("Vertices to remove (self-loop) " + toRemove);

		toRemove.clear();

		// Dummy nodes
		for (V v : G.getVertices()) {
			if (G.getPredecessorCount(v) == 0 || G.getSuccessorCount(v) == 0) {
				toRemove.add(v);
				S.add(v);
			}
		}
		// System.out.println("Vertices to remove (dummy) " + toRemove);
		Operations.removeAllVertices(G, toRemove);

		// T. Chaining Nodes
		boolean cfound = false;

		do {
			Pair<V> p = null;
			V r = null;
			cfound = false;

			Iterator<V> itr = G.getVertices().iterator();
			while (itr.hasNext() && !cfound) {
				V v = itr.next();
				if (G.getPredecessorCount(v) == 1 && G.getSuccessorCount(v) == 1) {

					V y = G.getPredecessors(v).iterator().next();
					V z = G.getSuccessors(v).iterator().next();

					if (y != z) {
						// System.out.println("Found chain " + r + " in chain " + y + "->" + r + "->"+
						// z);
						p = new Pair<V>(y, z);
						r = v;
						S.add(v);
						cfound = true;
					}
				}
			}

			if (cfound) {
				G.removeVertex(r);
				// System.out.println("Removing vertex " + r);
				if (G.findEdge(p.getFirst(), p.getSecond()) == null)
					G.addEdge(this.edgeFactory.create(), p.getFirst(), p.getSecond());
			}
		} while (cfound);

	}

	private Set<V> MASAlgorithm(Graph<V, E> G, MarkMap<V> R) {

		if (G.getEdgeCount() == 0)
			return new HashSet<V>(G.getVertices());

		Set<V> MAS = new HashSet<V>();

		// balancing operations
		Set<V> VL = R.getVertices(this.LM, this.LMD), VR = R.getVertices(this.RM, this.RMD);

		if (VR.size() - VL.size() > 3) {
			int nToChangeRole = VR.size() - (VL.size() + 3);
			Set<V> sRM = R.getVertices(this.RM);
			Set<V> sRMD = R.getVertices(this.RMD);
			Set<V> sWRM = R.getVertices(this.WRM);
			int i = 0;
			while (i < nToChangeRole) // change n vertices of VR to WRM role.
			{
				if (!sRM.isEmpty() && !sRMD.isEmpty()) {
					V v = null;
					if (Math.random() < 0.5) {
						v = sRM.iterator().next();
						sRM.remove(v);
					} else {
						v = sRMD.iterator().next();
						sRMD.remove(v);
					}
					sWRM.add(v);
					i++;
				} else {
					if (sRM.isEmpty() && sRMD.isEmpty()) {
						i = nToChangeRole;
					} else {
						if (sRM.isEmpty()) {
							V v = sRMD.iterator().next();
							sWRM.add(v);
							sRMD.remove(v);
							i++;
						} else if (sRMD.isEmpty()) {
							V v = sRM.iterator().next();
							sWRM.add(v);
							sRM.remove(v);
							i++;
						}
					}

				}

			}

			// updating marked vertices
			R.changeRole(sRM, this.RM);
			R.changeRole(sRMD, this.RMD);
			R.changeRole(sWRM, this.WRM);

		}

		if (VL.size() - VR.size() > 3) {
			int nToChangeRole = VL.size() - (VR.size() + 3);
			Set<V> sLM = R.getVertices(this.LM);
			Set<V> sLMD = R.getVertices(this.LMD);
			Set<V> sWLM = R.getVertices(this.WLM);
			int i = 0;
			while (i < nToChangeRole) // change n vertices to of VL to WLM role.
			{
				if (!sLM.isEmpty() && !sLMD.isEmpty()) {
					V v = null;
					if (Math.random() < 0.5) {
						v = sLM.iterator().next();
						sLM.remove(v);
					} else {
						v = sLMD.iterator().next();
						sLMD.remove(v);
					}
					sWLM.add(v);
					i++;
				} else {
					if (sLM.isEmpty() && sLMD.isEmpty()) {
						i = nToChangeRole;
					} else {
						if (sLM.isEmpty()) {
							V v = sLMD.iterator().next();
							sWLM.add(v);
							sLMD.remove(v);
							i++;
						} else if (sLMD.isEmpty()) {
							V v = sLM.iterator().next();
							sWLM.add(v);
							sLM.remove(v);
							i++;
						}
					}

				}

			}

			// updating marked vertices
			R.changeRole(sLM, this.LM);
			R.changeRole(sLMD, this.LMD);
			R.changeRole(sWLM, this.WLM);

		}

		// C1 rule
		if (G.getVertexCount() <= 3) {
			// System.out.println("C1 rule !");

			if (G.getVertexCount() == 2) {
				Iterator<V> itrv = G.getVertices().iterator();
				V u = itrv.next(), v = itrv.next();
				if (G.isPredecessor(u, v) && G.isSuccessor(u, v))
					MAS.add(u);
				else
					MAS.addAll(G.getVertices());
			}

			if (G.getVertexCount() == 3) {
				ArrayList<Set<V>> SCC = Components.getAllStronglyConnectedComponent(G);

				/*
				 * BUGGY !!!!!!!! if (SCC.size() == 1 && SCC.get(0).equals(G.getVertices())) {
				 * Iterator<V> itrv = G.getVertices().iterator(); MAS.add(itrv.next());
				 * MAS.add(itrv.next()); }
				 *
				 *
				 * if (SCC.size() == 1 && SCC.get(0).size() == 1) MAS.addAll(G.getVertices());
				 * END OF BUGGY
				 */

				if (SCC.size() == 1) {
					if (G.getEdgeCount() == 6)
						MAS.add(SCC.get(0).iterator().next());
					else {
						Iterator<V> it = G.getVertices().iterator();
						V v1 = it.next();
						V v2 = it.next();
						V v3 = it.next();
						if (G.degree(v1) <= G.degree(v2) && G.degree(v3) <= G.degree(v2)) // v2 max arity
						{
							MAS.add(v1);
							MAS.add(v3);
						} else if (G.degree(v2) <= G.degree(v1) && G.degree(v3) <= G.degree(v1)) // v1 max arity)
						{
							MAS.add(v2);
							MAS.add(v3);
						} else {
							MAS.add(v1);
							MAS.add(v2);
						}
					}
				}

				// 2 CC
				// Adding the isolated node + one of the 2 other connected nodes
				// (the SCC of size 2 has two vertices)
				if (SCC.size() == 2)
					if (SCC.get(0).size() == 1) {
						MAS.addAll(SCC.get(0));
						MAS.add(SCC.get(1).iterator().next());
					} else {
						MAS.addAll(SCC.get(1));
						MAS.add(SCC.get(0).iterator().next());
					}

				// 3 isolated nodes (3 CC)
				if (SCC.size() == 3)
					MAS.addAll(G.getVertices());
			}

			return MAS;

		}

		// C2 rule
		boolean cycle2 = false;
		Iterator<E> itre = G.getEdges().iterator();
		V x = null, y = null;
		while (!cycle2 && itre.hasNext()) {
			E edge = itre.next();
			x = G.getEndpoints(edge).getFirst();
			y = G.getEndpoints(edge).getSecond();
			if (G.getSuccessors(x).contains(y) && G.getSuccessors(y).contains(x))
				cycle2 = true;
		}

		if (cycle2) {
			// System.out.println("C2 rule !");
			V v = x;
			Set<V> MAS1 = new HashSet<V>();
			MAS1.add(v);

			Graph<V, E> Gcv = Operations.copyDirectedSparseGraph(G);
			this.CompressGraph(Gcv, v);

			MarkMap<V> Rp = new MarkMap<V>(R);
			if (!R.getVertices(this.UM).contains(v)) {
				if (R.getVertices(this.LM).contains(v) || R.getVertices(this.LMD).contains(v)
						|| R.getVertices(this.WLM).contains(v))
					for (V nv : G.getPredecessors(v))
						if (R.getVertices(this.UM).contains(nv))
							Rp.changeRole(nv, this.WLM);

				if (R.getVertices(this.RM).contains(v) || R.getVertices(this.RMD).contains(v)
						|| R.getVertices(this.WRM).contains(v))
					for (V nv : G.getSuccessors(v))
						if (R.getVertices(this.UM).contains(nv))
							Rp.changeRole(nv, this.WRM);
			}

			MAS1.addAll(this.MASAlgorithm(Gcv, Rp));

			Graph<V, E> Gv = this.graphFactory.create();
			Operations.subGraph(G, Gv, v);
			G.removeVertex(v);
			Set<V> MAS2 = this.MASAlgorithm(G, R);
			Operations.mergeGraph(G, Gv);

			if (MAS1.size() > MAS2.size())
				return MAS1;
			else
				return MAS2;

		}

		// C3 rule
		ArrayList<Set<V>> CC = Components.getAllStronglyConnectedComponent(G);

		if (CC.size() > 1) {
			// System.out.println("C3 rule !");

			Set<V> VLMDRMD = new HashSet<V>();
			VLMDRMD.addAll(R.getVertices(this.LMD));
			VLMDRMD.addAll(R.getVertices(this.RMD));

			Iterator<Set<V>> itr = CC.iterator();
			Set<V> C1 = null, C2 = null;
			V v1 = null, v2 = null;
			while (itr.hasNext() && v1 == null && v2 == null) {
				Set<V> S = itr.next();
				if (!Sets.intersection(S, VLMDRMD).isEmpty()) {
					if (v1 == null) {
						for (V v : S)
							if (VLMDRMD.contains(v)) {
								v1 = v;
								C1 = S;
								break;
							}
					} else {
						for (V v : S)
							if (VLMDRMD.contains(v)) {
								v2 = v;
								C2 = S;
								break;
							}
					}
				}
			}

			if (v1 == null) {
				itr = CC.iterator();
				while (itr.hasNext() && v1 == null) {
					Set<V> C = itr.next();
					if (!C.contains(v2)) {
						v1 = C.iterator().next();
						C1 = C;
					}
				}
			}

			if (v2 == null) {
				itr = CC.iterator();
				while (itr.hasNext() && v2 == null) {
					Set<V> C = itr.next();
					if (!C.contains(v1)) {
						v2 = C.iterator().next();
						C2 = C;
					}
				}
			}

			Set<V> T1 = new HashSet<V>();

			Graph<V, E> Gcv1v2 = Operations.copyDirectedSparseGraph(G);
			Graph<V, E> Gv1v2 = this.graphFactory.create();

			Set<V> v1v2 = new HashSet<V>();
			v1v2.add(v1);
			v1v2.add(v2);

			for (V v : v1v2)
				this.CompressGraph(Gcv1v2, v);

			T1.addAll(v1v2);
			T1.addAll(this.MASAlgorithm(Gcv1v2, R));

			Operations.subGraph(G, Gv1v2, v1v2);
			G.removeVertex(v1);
			G.removeVertex(v2);
			Set<V> T2 = this.MASAlgorithm(G, R);
			Operations.mergeGraph(G, Gv1v2);

			Set<V> A = new HashSet<V>(), B = new HashSet<V>(), C = new HashSet<V>(), D = new HashSet<V>();

			Sets.intersection(T1, C1).copyInto(A);
			Sets.intersection(T2, C1).copyInto(B);
			Sets.intersection(T1, C2).copyInto(C);
			Sets.intersection(T2, C2).copyInto(D);

			Set<V> toReturn = new HashSet<V>();

			if (A.size() > B.size())
				toReturn.addAll(A);
			else
				toReturn.addAll(B);

			if (C.size() > D.size())
				toReturn.addAll(C);
			else
				toReturn.addAll(D);

			C1.addAll(C2);
			T1.removeAll(C1);

			toReturn.addAll(T1);

			return toReturn;
		}

		// C4 rule
		if (VL.isEmpty() || VR.isEmpty()) {
			// System.out.println("C4 rule !");
			V v = null;
			Iterator<V> itr = G.getVertices().iterator();
			while (itr.hasNext() && v == null) {
				V w = itr.next();
				if (G.getPredecessors(w).size() <= 3 || G.getSuccessors(w).size() <= 3)
					if (R.getVertices(this.UM).contains(w) || R.getVertices(this.WLM).contains(w)
							|| R.getVertices(this.WRM).contains(w))
						v = w;
			}

			// C41 rule
			if (v != null) {
				ArrayList<V> l = new ArrayList<V>();
				if (G.getPredecessors(v).size() <= 3)
					l = new ArrayList<V>(G.getPredecessors(v));
				else
					l = new ArrayList<V>(G.getSuccessors(v));

				// full branching
				ArrayList<Set<V>> MASlist = new ArrayList<Set<V>>();

				Graph<V, E> Gp = Operations.copyDirectedSparseGraph(G);
				this.CompressGraph(Gp, v);
				MAS.add(v);
				MAS.addAll(this.MASAlgorithm(Gp, R));
				MASlist.add(new HashSet<V>(MAS));

				for (int i = 0; i < l.size(); i++) {
					MAS = new HashSet<V>();
					Gp = Operations.copyDirectedSparseGraph(G);
					Gp.removeVertex(v);
					MAS.add(l.get(i));
					for (int j = 0; j < i; j++)
						Gp.removeVertex(l.get(j));

					this.CompressGraph(Gp, l.get(i));
					MAS.addAll(this.MASAlgorithm(Gp, R));
					MASlist.add(new HashSet<V>(MAS));
				}

				MAS.clear();
				for (Set<V> s : MASlist)
					if (s.size() > MAS.size())
						MAS = s;

				return MAS;
			} else // C42 rule
			{
				v = null;
				if (!R.getVertices(this.UM).isEmpty())
					v = R.getVertices(this.UM).iterator().next();

				if (v == null && !R.getVertices(this.WLM).isEmpty())
					v = R.getVertices(this.WLM).iterator().next();

				if (v == null && !R.getVertices(this.WRM).isEmpty())
					v = R.getVertices(this.WRM).iterator().next();

				if (v == null)
					v = G.getVertices().iterator().next();

				Set<V> MAS1 = new HashSet<V>();
				MAS1.add(v);
				Graph<V, E> Gcv = Operations.copyDirectedSparseGraph(G);
				this.CompressGraph(Gcv, v);
				MarkMap<V> Rp = new MarkMap<V>(R);
				if (!VL.contains(v) && !VR.contains(v)) {
					for (V h : Gcv.getVertices())
						Rp.changeRole(h, this.UM);

					Set<V> U = new HashSet<V>();
					Iterator<V> itru = Gcv.getPredecessors(v).iterator();
					for (int i = 0; i < 4; i++)
						U.add(itru.next());

					Set<V> W = new HashSet<V>();
					Iterator<V> itrw = Gcv.getSuccessors(v).iterator();
					for (int i = 0; i < 4; i++)
						W.add(itrw.next());

					Rp.changeRole(U, this.LM);
					Rp.changeRole(W, this.RM);

					Set<V> UU = new HashSet<V>();
					Sets.difference(new HashSet<V>(Gcv.getPredecessors(v)), U).copyInto(UU);

					Set<V> WW = new HashSet<V>();
					Sets.difference(new HashSet<V>(Gcv.getSuccessors(v)), W).copyInto(WW);

					if (!UU.isEmpty())
						Rp.changeRole(UU, this.WLM);
					if (!WW.isEmpty())
						Rp.changeRole(WW, this.WRM);
				}

				MAS1.addAll(this.MASAlgorithm(Gcv, Rp));

				Graph<V, E> Gv = this.graphFactory.create();
				Operations.subGraph(G, Gv, v);
				G.removeVertex(v);
				Set<V> MAS2 = this.MASAlgorithm(G, R);
				Operations.mergeGraph(G, Gv);

				if (MAS1.size() > MAS2.size())
					return MAS1;
				else
					return MAS2;
			}
		}

		// C5 rule
		// System.out.println("C5 rule !");
		int xM = 0, xMD = 0, WxM = 0;
		Set<V> Vx = null;
		if (VL.size() <= VR.size()) {
			xM = this.LM;
			xMD = this.LMD;
			WxM = this.WLM;
			Vx = VL;
		} else {
			xM = this.RM;
			xMD = this.RMD;
			WxM = this.WRM;
			Vx = VR;
		}

		Set<V> U = new HashSet<V>();
		for (V h : Vx) {
			if (xM == this.LM) {
				for (V p : G.getPredecessors(h))
					if (R.getVertices(this.UM).contains(p)) {
						U.add(p);
						break;
					}

			} else {
				for (V p : G.getSuccessors(h))
					if (R.getVertices(this.UM).contains(p)) {
						U.add(p);
						break;
					}
			}

			if (!U.isEmpty())
				break;
		}

		// C51 rule
		if (R.getVertices(xMD).containsAll(Vx) || U.isEmpty()) {

			V v = Vx.iterator().next();
			Set<V> MAS1 = new HashSet<V>();
			MAS1.add(v);
			Graph<V, E> Gcv = Operations.copyDirectedSparseGraph(G);
			this.CompressGraph(Gcv, v);
			MAS1.addAll(this.MASAlgorithm(Gcv, R));

			Graph<V, E> Gv = this.graphFactory.create();
			Operations.subGraph(G, Gv, v);
			G.removeVertex(v);
			Set<V> MAS2 = this.MASAlgorithm(G, R);
			Operations.mergeGraph(G, Gv);

			if (MAS1.size() > MAS2.size())
				return MAS1;
			else
				return MAS2;
		}

		// C52 rule
		Set<V> xmarked = new HashSet<V>();
		xmarked.addAll(Vx);
		xmarked.addAll(R.getVertices(WxM));
		xmarked.removeAll(R.getVertices(xMD));
		U = new HashSet<V>();
		V v = null;
		for (V h : xmarked) {
			U.clear();
			if (xM == this.LM) {
				for (V p : G.getPredecessors(h))
					if (R.getVertices(this.UM).contains(p))
						U.add(p);
			} else {
				for (V p : G.getSuccessors(h))
					if (R.getVertices(this.UM).contains(p))
						U.add(p);
			}

			if (U.size() >= 4) {
				v = h;
				break;
			}
		}

		if (v != null) {
			Set<V> Uv = new HashSet<V>();
			Iterator<V> itru = U.iterator();
			for (int i = 0; i < 4; i++)
				Uv.add(itru.next());

			Set<V> MAS1 = new HashSet<V>();
			MAS1.add(v);
			Graph<V, E> Gcv = Operations.copyDirectedSparseGraph(G);
			this.CompressGraph(Gcv, v);
			MarkMap<V> Rp = new MarkMap<V>(R);
			Rp.changeRole(Uv, xM);
			U.removeAll(Uv);
			Rp.changeRole(U, WxM);
			MAS1.addAll(this.MASAlgorithm(Gcv, Rp));

			Graph<V, E> Gv = this.graphFactory.create();
			Operations.subGraph(G, Gv, v);
			G.removeVertex(v);
			Set<V> MAS2 = this.MASAlgorithm(G, R);
			Operations.mergeGraph(G, Gv);

			if (MAS1.size() > MAS2.size())
				return MAS1;
			else
				return MAS2;
		}

		// C53 rule
		v = null;
		int max = 0;
		Set<V> X = null;
		for (V h : xmarked) {
			U.clear();
			if (xM == this.LM) {
				for (V p : G.getPredecessors(h))
					if (R.getVertices(this.UM).contains(p))
						U.add(p);
			} else {
				for (V p : G.getSuccessors(h))
					if (R.getVertices(this.UM).contains(p))
						U.add(p);
			}

			if (U.size() > max) {
				v = h;
				max = U.size();
				X = new HashSet<V>(U);
			}
		}

		long ncomb = (long) Math.pow(2, X.size());
		V[] tabX = (V[]) X.toArray();

		Set<V> TYfinal = new HashSet<V>();

		for (long i = 0; i < ncomb; i++) {
			Set<V> Y = new HashSet<V>();
			String comb = Long.toBinaryString(i);
			for (int k = 0; k < comb.length(); k++)
				if (comb.charAt(k) == '1')
					Y.add(tabX[k]);

			Set<V> X_Y = new HashSet<V>(X);
			X_Y.removeAll(Y);
			Graph<V, E> GY = Operations.copyDirectedSparseGraph(G);
			Operations.removeAllVertices(GY, X_Y);
			for (V h : Y)
				this.CompressGraph(GY, h);

			MarkMap<V> Rp = new MarkMap<V>(R);
			if (Y.isEmpty())
				Rp.changeRole(v, xMD);
			Set<V> TY = new HashSet<V>();
			TY.addAll(this.MASAlgorithm(GY, Rp));
			TY.addAll(Y);

			if (TY.size() > TYfinal.size())
				TYfinal = new HashSet<V>(TY);
		}

		return TYfinal;
	}
}
