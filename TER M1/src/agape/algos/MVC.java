/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.algos;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import com.google.common.collect.Sets;

import agape.tools.Operations;
import edu.uci.ics.jung.graph.Graph;

/**
 * This class implements algorithms for solving the Minimum Vertex Cover
 * problem. The problem consists in finding the minimum set of vertices such
 * that any edge is incident at one of the vertex of the computed set. !!
 * WARNING !! These algorithms work ONLY on undirected graphs.
 *
 * @author V. Levorato
 * @author J.-F. Lalande
 * @author P. Berthome
 * @param <V> Vertices type
 * @param <E> Edges type
 */
public class MVC<V, E> extends Algorithms<V, E> {

	// used by branching algorithms
	private Set<V> VCbuf = null; // for vertex cover

	private Set<V> VCFinal;

	public MVC(Factory<Graph<V, E>> graphFactory) {
		this.graphFactory = graphFactory;
	}

	/**
	 * Returns the last computed vertex cover set, null if the called Parameterized
	 * algorithms cannot find such a set.
	 *
	 * @return a vertex cover (null if there is no solution)
	 */
	public Set<V> getVertexCoverSolution() {
		if (this.VCFinal.size() == 0)
			return null;
		return this.VCFinal;
	}

	private void initSolution() {
		this.VCFinal = new HashSet<V>();
		this.VCbuf = new HashSet<V>(); // used by branching algorithm (to be initialized once)
	}

	/**
	 * Finds a 2-approximation for a minimal vertex cover of the specified graph.
	 * The algorithm promises a cover that is at most double the size of a minimal
	 * cover. The algorithm takes O(|E|) time.
	 *
	 * @param g the graph for which vertex cover approximation has to be found
	 * @return a set of vertices which is a vertex cover for the specified graph.
	 */
	public Set<V> twoApproximationCover(Graph<V, E> g) {

		this.initSolution();

		Graph<V, E> Gp = Operations.copyGraph(g, this.graphFactory);

		while (Gp.getEdgeCount() > 0) {
			E e = Gp.getEdges().iterator().next();

			V u = Gp.getEndpoints(e).getFirst();
			V v = Gp.getEndpoints(e).getSecond();
			this.VCFinal.add(u);
			this.VCFinal.add(v);

			Gp.removeVertex(u);
			Gp.removeVertex(v);
		}

		return this.VCFinal;
	}

	/**
	 * Finds a greedy approximation for a minimal vertex cover of a specified graph.
	 * At each iteration, the algorithm picks the vertex with the highest degree and
	 * adds it to the cover, until all edges are covered.
	 *
	 * @param g the graph for which vertex cover approximation has to be found
	 * @return a set of vertices which is a vertex cover for the specified graph.
	 */
	public Set<V> greedyCoverMaxDegree(Graph<V, E> g) {

		this.initSolution();

		Graph<V, E> Gp = Operations.copyGraph(g, this.graphFactory);

		while (Gp.getEdgeCount() > 0) {
			V v = Operations.getMaxDegVertex(Gp);
			this.VCFinal.add(v);
			Gp.removeVertex(v);
		}

		return this.VCFinal;
	}

	/**
	 * Returns true if a vertex cover of size k exists in graph G. The complexity of
	 * this method is O(C_k^n) (exhaustive search). This algorithm test exhaustively
	 * all the combinations of k vertices among n. The principle is to test all the
	 * sets of size k: we build recursively these sets by choosing a new vertex to
	 * add in the set. When all the neighborhood of the set covers the graph, we
	 * stop if the size of the set is less than k.
	 *
	 * @param g graph
	 * @param k vertex cover size
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	public boolean kVertexCoverBruteForce(Graph<V, E> g, int k) {
		this.initSolution();
		Graph<V, E> g2 = Operations.copyGraph(g, this.graphFactory);
		return this.kVertexCoverBruteForce(g2, k, this.VCFinal, new HashSet<E>(), new HashSet<V>(g2.getVertices()));
	}

	protected boolean kVertexCoverBruteForce(Graph<V, E> G, int k, Set<V> VCcurrent, Set<E> viewed, Set<V> remaining) {
		if (k <= 0 && remaining.size() > 0) {
			// System.out.println("Cannot cover !");
			return false;
		}
		// System.out.println("CALL: viewed edges=" + viewed + " / remaining
		// nodes="+remaining);
		// System.out.println("VCBuf: " + VCbuf);
		// System.out.println("VCcurrent: " + VCcurrent);
		// System.out.println("k=" + k);
		// System.out.println("--------------------------------------------");

		// There is no edges left: the current VCbuf is a solution
		if (viewed.size() == G.getEdgeCount()) {
			// If the current solution VCbuf is better than the VCcurrent
			// we should replace the best solution
			if (this.VCbuf.size() < VCcurrent.size() || VCcurrent.isEmpty()) {
				VCcurrent.clear();
				VCcurrent.addAll(this.VCbuf);
			}

			return true;
		}

		// Trying each edge not viewed in the solution
		boolean res = false;

		Iterator<V> itv = remaining.iterator();
		while (itv.hasNext()) {
			V v_considered_in_the_solution1 = itv.next();
			this.VCbuf.add(v_considered_in_the_solution1);

			HashSet<E> viewed2 = new HashSet<E>(viewed);
			HashSet<V> remaining2 = new HashSet<V>(remaining);

			// Updating viewed edges and remaining edges
			// before the recursion
			remaining2.remove(v_considered_in_the_solution1);
			viewed2.addAll(G.getIncidentEdges(v_considered_in_the_solution1));

			// Backup of VCbuf before recursion
			Set<V> oldVCbuf = new HashSet<V>(this.VCbuf);

			res = this.kVertexCoverBruteForce(G, k - 1, VCcurrent, viewed2, remaining2) || res;
			// Restore of the backup of VCbuf
			this.VCbuf = oldVCbuf;
			this.VCbuf.remove(v_considered_in_the_solution1);
		}

		return res;
	}

	/**
	 * Returns true if a vertex cover of size k exists in graph G. The comupted set
	 * contains a cover of size at most k at the end of the algorithm. This method
	 * uses Degree-Branching-Strategy (DBS) and has a time complexity in O(1.47^k).
	 *
	 * @param g graph
	 * @param k vertex cover size
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	public boolean kVertexCoverDegreeBranchingStrategy(Graph<V, E> g, int k) {
		this.initSolution();
		Graph<V, E> g2 = Operations.copyGraph(g, this.graphFactory);
		return this.kVertexCoverDegreeBranchingStrategy(g2, k, this.VCFinal);
	}

	protected boolean kVertexCoverDegreeBranchingStrategy(Graph<V, E> G, int k, Set<V> VCcurrent) {
		// if(tracker!=null) tracker.increase("VC"); //tracker operation

		int nbe = G.getEdgeCount();

		if (nbe == 0) {
			if (this.VCbuf.size() < VCcurrent.size() || VCcurrent.isEmpty()) {
				VCcurrent.clear();
				VCcurrent.addAll(this.VCbuf);
			}

			return true;
		}

		if (nbe >= k * G.getVertexCount())
			return false;

		V vone = Operations.getDegVertex(G, 1);
		if (vone != null) {
			Set<V> Nvone = new HashSet<V>(G.getNeighbors(vone));
			Graph<V, E> Gp = this.graphFactory.create();
			for (V v : Nvone)
				Operations.subGraph(G, Gp, v);

			this.VCbuf.addAll(Nvone);

			Operations.removeAllVertices(G, Nvone);
			boolean b = this.kVertexCoverDegreeBranchingStrategy(G, k - Nvone.size(), VCcurrent);
			Operations.mergeGraph(G, Gp);
			this.VCbuf.removeAll(Nvone);
			return b;
		}

		V vtwo = Operations.getDegVertex(G, 2);
		if (vtwo != null) {
			Set<V> Nvtwo2 = Operations.getNeighbors(G, vtwo, 2);
			Set<V> Nvtwo = new HashSet<V>(G.getNeighbors(vtwo));
			Graph<V, E> GNv2 = this.graphFactory.create();
			Graph<V, E> GNv = this.graphFactory.create();

			Operations.subGraph(G, GNv2, vtwo);
			for (V v : Nvtwo2)
				Operations.subGraph(G, GNv2, v);

			this.VCbuf.add(vtwo);
			this.VCbuf.addAll(Nvtwo2);
			G.removeVertex(vtwo);
			Operations.removeAllVertices(G, Nvtwo2);

			boolean a = this.kVertexCoverDegreeBranchingStrategy(G, k - Nvtwo2.size() - 1, VCcurrent);

			Operations.mergeGraph(G, GNv2);
			this.VCbuf.remove(vtwo);
			this.VCbuf.removeAll(Nvtwo2);

			for (V v : Nvtwo)
				Operations.subGraph(G, GNv, v);
			this.VCbuf.addAll(Nvtwo);

			Operations.removeAllVertices(G, Nvtwo);

			boolean b = this.kVertexCoverDegreeBranchingStrategy(G, k - Nvtwo.size(), VCcurrent);

			Operations.mergeGraph(G, GNv);
			this.VCbuf.removeAll(Nvtwo);

			return a || b;
		}

		V vmax = Operations.getMaxDegVertex(G);
		if (vmax != null && G.degree(vmax) >= 3) {
			Set<V> Nvmax = new HashSet<V>(G.getNeighbors(vmax));
			Graph<V, E> Gvmax = this.graphFactory.create();
			Graph<V, E> GNvmax = this.graphFactory.create();

			Operations.subGraph(G, Gvmax, vmax);

			this.VCbuf.add(vmax);
			G.removeVertex(vmax);

			boolean a = this.kVertexCoverDegreeBranchingStrategy(G, k - 1, VCcurrent);

			Operations.mergeGraph(G, Gvmax);
			this.VCbuf.remove(vmax);

			for (V v : Nvmax)
				Operations.subGraph(G, GNvmax, v);
			this.VCbuf.addAll(Nvmax);

			Operations.removeAllVertices(G, Nvmax);

			boolean b = this.kVertexCoverDegreeBranchingStrategy(G, k - Nvmax.size(), VCcurrent);

			Operations.mergeGraph(G, GNvmax);
			this.VCbuf.removeAll(Nvmax);

			return a || b;
		}

		return this.kVertexCoverDegreeBranchingStrategy(G, k, VCcurrent);

	}

	/**
	 * Returns true if a vertex cover of size k exists in graph G. This method
	 * reduces a graph instance in polynomial time in order to find a vertex cover
	 * by kernelization using Buss and Goldsmith reduction ("Nondeterminism within
	 * P", 1993).
	 *
	 * @param g graph
	 * @param k vertex cover size
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	public boolean kVertexCoverBussGoldsmith(Graph<V, E> g, int k) {
		this.initSolution();
		Graph<V, E> g2 = Operations.copyGraph(g, this.graphFactory);
		return this.kVertexCoverKernel(g2, k, this.VCFinal);
	}

	/**
	 * Returns true if a vertex cover of size k exists in graph G. This method
	 * reduces a graph instance in polynomial time in order to find a vertex cover
	 * by kernelization using Buss and Goldsmith reduction ("Nondeterminism within
	 * P", 1993).
	 *
	 * @param g       graph
	 * @param k       vertex cover size
	 * @param VCfinal set which contains the vertex cover
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	protected boolean kVertexCoverKernel(Graph<V, E> G, int k, Set<V> VCfinal) {
		// if(tracker!=null) tracker.increase("VC"); //tracker operation

		int nbe = G.getEdgeCount();

		if (nbe == 0) {
			if (this.VCbuf.size() < VCfinal.size() || VCfinal.isEmpty()) {
				VCfinal.clear();
				VCfinal.addAll(this.VCbuf);
			}

			return true;
		}

		if (nbe >= k * G.getVertexCount())
			return false;

		Set<V> K = new HashSet<V>();
		V vmin = Operations.getMinDegVertex(G);
		int degmin = G.degree(vmin);
		int degmax = Operations.getMaxDeg(G);

		Graph<V, E> Gp = G;

		// conditions to make buss kernelization
		if (degmin == 0 || degmin == 1 || degmax > k) {
			Gp = Operations.copyUndirectedSparseGraph(G);
			K = this.kVertexCoverKernelizationBussInternal(Gp, k);
			k = k - K.size();
			this.VCbuf.addAll(K);
		}

		V vmax = Operations.getMaxDegVertex(Gp);
		if (vmax == null)
			return this.kVertexCoverKernel(Gp, k, VCfinal);

		Set<V> oldVCbuf = new HashSet<V>(this.VCbuf);

		int degvmax = Gp.degree(vmax);

		this.VCbuf.add(vmax);
		// memorize which vertex is removed (with his neighbourhood)
		Graph<V, E> Gvmax = this.graphFactory.create();
		Operations.subGraph(Gp, Gvmax, vmax);

		Gp.removeVertex(vmax);

		boolean a = this.kVertexCoverKernel(Gp, k - 1, VCfinal);

		// reinsert removed vertex
		Operations.mergeGraph(Gp, Gvmax);

		this.VCbuf = new HashSet<V>(oldVCbuf);
		Graph<V, E> GNvmax = this.graphFactory.create();
		if (degvmax > 0) {
			Set<V> Nvmax = new HashSet<V>(Gp.getNeighbors(vmax));

			// memorize which vertex is removed (with his neighbourhood)

			for (V nv : Nvmax)
				Operations.subGraph(Gp, GNvmax, nv);

			this.VCbuf.addAll(Nvmax);
			Operations.removeAllVertices(Gp, Nvmax);
		}

		boolean b = this.kVertexCoverKernel(Gp, k - degvmax, VCfinal);

		// reinsert removed vertices
		Operations.mergeGraph(Gp, GNvmax);

		return a || b;
	}

	/**
	 * Returns true if a vertex cover of size k exists in graph G. The computed set
	 * contains a cover of size at most k at the end of the algorithm. This method
	 * is an implementation of Niedermeier algorithm "Invitation to Fixed Parameter
	 * Algorithms", 2006, pages 98-101 which time complexity is O(1.33^k).
	 *
	 * @param g graph
	 * @param k vertex cover size
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	public boolean kVertexCoverNiedermeier(Graph<V, E> g, int k) {
		this.initSolution();
		Graph<V, E> g2 = Operations.copyGraph(g, this.graphFactory);
		return this.kVertexCoverNiedermeier(g2, k, this.VCFinal);
	}

	protected boolean kVertexCoverNiedermeier(Graph<V, E> g, int k, Set<V> VCfinal) {
		// if(tracker!=null) tracker.increase("VC"); //tracker operation

		int nbe = g.getEdgeCount();

		if (nbe == 0) {
			if (this.VCbuf.size() < VCfinal.size() || VCfinal.isEmpty()) {
				VCfinal.clear();
				VCfinal.addAll(this.VCbuf);
			}

			return true;
		}

		if (nbe >= k * g.getVertexCount())
			return false;

		if (Operations.getMinDeg(g) == 0) {
			Graph<V, E> Gp = this.graphFactory.create();
			Set<V> vZeros = Operations.getAllMinDegVertex(g);
			for (V v : vZeros)
				Operations.subGraph(g, Gp, v);
			Operations.removeAllVertices(g, vZeros);
			boolean b = this.kVertexCoverNiedermeier(g, k, VCfinal);
			Operations.mergeGraph(g, Gp);
			return b;
		}

		// if a vertex of deg=1 exists
		V vone = Operations.getDegVertex(g, 1);
		if (vone != null) {
			Graph<V, E> Gp = this.graphFactory.create();
			V Nvone = g.getNeighbors(vone).iterator().next();
			this.VCbuf.add(Nvone);
			Operations.subGraph(g, Gp, Nvone);
			g.removeVertex(Nvone);
			boolean b = this.kVertexCoverNiedermeier(g, k - 1, VCfinal);
			Operations.mergeGraph(g, Gp);
			this.VCbuf.remove(Nvone);
			return b;
		}

		// if a vertex of deg>=5 exists
		V vfivemore = Operations.getMaxDegVertex(g);

		if (g.degree(vfivemore) >= 5) {
			Set<V> Nvfivemore = new HashSet<V>(g.getNeighbors(vfivemore));

			Graph<V, E> Gf = this.graphFactory.create();
			Graph<V, E> GNf = this.graphFactory.create();

			// branching on x
			Operations.subGraph(g, Gf, vfivemore);
			this.VCbuf.add(vfivemore);
			g.removeVertex(vfivemore);
			boolean bx = this.kVertexCoverNiedermeier(g, k - 1, VCfinal);

			// restore to current state of the tree process
			Operations.mergeGraph(g, Gf);
			this.VCbuf.remove(vfivemore);

			// branching on N(x)
			this.VCbuf.addAll(Nvfivemore);
			for (V v : Nvfivemore)
				Operations.subGraph(g, GNf, v);

			Operations.removeAllVertices(g, Nvfivemore);

			boolean bnx = this.kVertexCoverNiedermeier(g, k - Nvfivemore.size(), VCfinal);

			Operations.mergeGraph(g, GNf);
			this.VCbuf.removeAll(Nvfivemore);

			return bx || bnx;
		}

		// if the graph is regular
		if (Operations.isRegular(g)) {
			V v = g.getVertices().iterator().next();

			Set<V> Nv = new HashSet<V>(g.getNeighbors(v));

			Graph<V, E> Gv = this.graphFactory.create();
			Graph<V, E> GNv = this.graphFactory.create();

			// branching on x
			Operations.subGraph(g, Gv, v);
			this.VCbuf.add(v);
			g.removeVertex(v);
			boolean bx = this.kVertexCoverNiedermeier(g, k - 1, VCfinal);

			// restore to current state of the tree process
			Operations.mergeGraph(g, Gv);
			this.VCbuf.remove(v);

			// branching on N(x)
			this.VCbuf.addAll(Nv);
			for (V x : Nv)
				Operations.subGraph(g, GNv, x);

			Operations.removeAllVertices(g, Nv);

			boolean bnx = this.kVertexCoverNiedermeier(g, k - Nv.size(), VCfinal);

			Operations.mergeGraph(g, GNv);
			this.VCbuf.removeAll(Nv);

			return bx || bnx;
		}

		// if vertex with deg=2 exists
		V vtwo = null;
		vtwo = Operations.getDegVertex(g, 2);

		if (vtwo != null) {
			Iterator<V> itrvtwo = g.getNeighbors(vtwo).iterator();
			V va = itrvtwo.next();
			V vb = itrvtwo.next();

			// if there is an edge between a and b
			if (Operations.isEdge(g, va, vb)) {
				Graph<V, E> Gp = this.graphFactory.create();
				this.VCbuf.add(va);
				this.VCbuf.add(vb);
				Operations.subGraph(g, Gp, va);
				Operations.subGraph(g, Gp, vb);
				g.removeVertex(va);
				g.removeVertex(vb);
				boolean b = this.kVertexCoverNiedermeier(g, k - 2, VCfinal);
				Operations.mergeGraph(g, Gp);
				this.VCbuf.remove(va);
				this.VCbuf.remove(vb);
				return b;
			}

			// if there is no edge between a and b but a and b both have degree two with
			// a common neighbor c different from x
			if (!Operations.isEdge(g, va, vb) && g.degree(va) == 2 && g.degree(vb) == 2) {
				Set<V> Nva = new HashSet<V>(g.getNeighbors(va));
				Set<V> Nvb = new HashSet<V>(g.getNeighbors(vb));
				Nva.remove(vtwo);
				Nvb.remove(vtwo);
				if (Nva.equals(Nvb)) {
					Graph<V, E> Gp = this.graphFactory.create();
					this.VCbuf.add(vtwo);
					this.VCbuf.add(Nva.iterator().next());
					Operations.subGraph(g, Gp, vtwo);
					Operations.subGraph(g, Gp, Nva.iterator().next());
					g.removeVertex(vtwo);
					g.removeVertex(Nva.iterator().next());
					boolean b = this.kVertexCoverNiedermeier(g, k - 2, VCfinal);
					Operations.mergeGraph(g, Gp);
					this.VCbuf.remove(vtwo);
					this.VCbuf.remove(Nva.iterator().next());
					return b;
				}
			}

			// branching on N(x) and N(a)∪N(b)
			Set<V> Nx = new HashSet<V>(g.getNeighbors(vtwo));
			Set<V> Na = new HashSet<V>(g.getNeighbors(va));
			Set<V> Nb = new HashSet<V>(g.getNeighbors(vb));
			Set<V> NaNb = new HashSet<V>();
			Sets.union(Na, Nb).copyInto(NaNb);

			Graph<V, E> GNx = this.graphFactory.create();
			Graph<V, E> GNaNb = this.graphFactory.create();

			// branching on N(x)
			this.VCbuf.addAll(Nx);
			for (V v : Nx)
				Operations.subGraph(g, GNx, v);

			Operations.removeAllVertices(g, Nx);
			boolean bnx = this.kVertexCoverNiedermeier(g, k - Nx.size(), VCfinal);

			// restore to current state of the tree process
			this.VCbuf.removeAll(Nx);
			Operations.mergeGraph(g, GNx);

			// branching on N(a)∪N(b)
			this.VCbuf.addAll(NaNb);
			for (V v : NaNb)
				Operations.subGraph(g, GNaNb, v);

			Operations.removeAllVertices(g, NaNb);
			boolean bnanb = this.kVertexCoverNiedermeier(g, k - NaNb.size(), VCfinal);

			Operations.mergeGraph(g, GNaNb);
			this.VCbuf.removeAll(NaNb);

			return bnx || bnanb;
		}

		// if vertex with deg=3 exists
		V vthree = null;
		vthree = Operations.getDegVertex(g, 3);

		if (vthree != null) {
			Iterator<V> itrvthree = g.getNeighbors(vthree).iterator();
			V va = itrvthree.next();
			V vb = itrvthree.next();
			V vc = itrvthree.next();

			// tests is there is a triangle with x
			V t = null;
			if (Operations.isEdge(g, va, vb))
				t = vc; // triangle {x,a,b}
			else if (Operations.isEdge(g, va, vc))
				t = vb; // triangle {x,a,c}
			else if (Operations.isEdge(g, vb, vc))
				t = va; // triangle {x,b,c}

			// if x is part of a triangle
			if (t != null) {
				// branching on N(x) and N(t)
				Set<V> Nx = new HashSet<V>(g.getNeighbors(vthree));
				Set<V> Nt = new HashSet<V>(g.getNeighbors(t));

				Graph<V, E> GNx = this.graphFactory.create();
				Graph<V, E> GNt = this.graphFactory.create();

				// branching on N(x)
				this.VCbuf.addAll(Nx);
				for (V v : Nx)
					Operations.subGraph(g, GNx, v);

				Operations.removeAllVertices(g, Nx);
				boolean bnx = this.kVertexCoverNiedermeier(g, k - Nx.size(), VCfinal);

				// restore to current state of the tree process
				this.VCbuf.removeAll(Nx);
				Operations.mergeGraph(g, GNx);
				// branching on N(t)
				this.VCbuf.addAll(Nt);
				for (V v : Nt)
					Operations.subGraph(g, GNt, v);
				Operations.removeAllVertices(g, Nt);
				boolean bnt = this.kVertexCoverNiedermeier(g, k - Nt.size(), VCfinal);
				Operations.mergeGraph(g, GNt);
				this.VCbuf.removeAll(Nt);

				return bnx || bnt;
			}

			// test if there is a cycle of length four with x
			Set<V> Na = new HashSet<V>(g.getNeighbors(va));
			Set<V> Nb = new HashSet<V>(g.getNeighbors(vb));
			Set<V> Nc = new HashSet<V>(g.getNeighbors(vc));

			Set<V> N = new HashSet<V>();
			if (!Sets.intersection(Na, Nb).isEmpty())
				Sets.intersection(Na, Nb).copyInto(N);
			else if (!Sets.intersection(Na, Nc).isEmpty())
				Sets.intersection(Na, Nc).copyInto(N);
			else if (!Sets.intersection(Nb, Nc).isEmpty())
				Sets.intersection(Nb, Nc).copyInto(N);

			if (!N.isEmpty()) {
				V vd = N.iterator().next();
				// branching on N(x) and {x,d}
				Set<V> Nx = new HashSet<V>(g.getNeighbors(vthree));
				Set<V> xd = new HashSet<V>();
				xd.add(vthree);
				xd.add(vd);

				Graph<V, E> GNx = this.graphFactory.create();
				Graph<V, E> Gxd = this.graphFactory.create();

				// branching on N(x)
				this.VCbuf.addAll(Nx);
				for (V v : Nx)
					Operations.subGraph(g, GNx, v);
				Operations.removeAllVertices(g, Nx);
				boolean bnx = this.kVertexCoverNiedermeier(g, k - Nx.size(), VCfinal);

				// restore to current state of the tree process
				this.VCbuf.removeAll(Nx);
				Operations.mergeGraph(g, GNx);
				// branching on N(t)
				this.VCbuf.addAll(xd);
				for (V v : xd)
					Operations.subGraph(g, Gxd, v);
				Operations.removeAllVertices(g, xd);
				boolean bxd = this.kVertexCoverNiedermeier(g, k - xd.size(), VCfinal);
				Operations.mergeGraph(g, Gxd);
				this.VCbuf.removeAll(xd);

				return bnx || bxd;
			}

			// branching on N(x), N(a), and {a}∪N(b)∪N(c)
			Set<V> Nx = new HashSet<V>(g.getNeighbors(vthree));

			Graph<V, E> GNx = this.graphFactory.create();
			Graph<V, E> GNa = this.graphFactory.create();
			Graph<V, E> GaNbNc = this.graphFactory.create();

			// branching on N(x)
			this.VCbuf.addAll(Nx);
			for (V v : Nx)
				Operations.subGraph(g, GNx, v);
			Operations.removeAllVertices(g, Nx);
			boolean bnx = this.kVertexCoverNiedermeier(g, k - Nx.size(), VCfinal);

			// restore to current state of the tree process
			this.VCbuf.removeAll(Nx);
			Operations.mergeGraph(g, GNx);

			// branching on N(a)
			this.VCbuf.addAll(Na);
			for (V v : Na)
				Operations.subGraph(g, GNa, v);
			Operations.removeAllVertices(g, Na);
			boolean bna = this.kVertexCoverNiedermeier(g, k - Na.size(), VCfinal);
			Operations.mergeGraph(g, GNa);

			// restore to current state of the tree process
			this.VCbuf.removeAll(Na);
			// branching on {a}∪N(b)∪N(c)
			Set<V> aNbNc = new HashSet<V>();
			aNbNc.add(va);
			aNbNc.addAll(Nb);
			aNbNc.addAll(Nc);
			this.VCbuf.addAll(aNbNc);
			for (V v : aNbNc)
				Operations.subGraph(g, GaNbNc, v);
			Operations.removeAllVertices(g, aNbNc);
			boolean banbnc = this.kVertexCoverNiedermeier(g, k - aNbNc.size(), VCfinal);
			Operations.mergeGraph(g, GaNbNc);
			this.VCbuf.removeAll(aNbNc);

			return bnx || bna || banbnc;

		}

		return this.kVertexCoverNiedermeier(g, k, VCfinal);

	}

	protected Set<V> kVertexCoverKernelizationBussInternal(Graph<V, E> g, int k) {
		Set<V> VC = new HashSet<V>();
		if (g.getVertexCount() == 0)
			return VC;

		V x = Operations.getMinDegVertex(g);
		int degx = g.degree(x);

		if (degx == 0) {
			g.removeVertex(x);
			return this.kVertexCoverKernelizationBussInternal(g, k);
		}

		if (degx == 1) {
			V y = g.getNeighbors(x).iterator().next();
			g.removeVertex(x);
			g.removeVertex(y);
			VC = this.kVertexCoverKernelizationBussInternal(g, k - 1);
			VC.add(y);
		}

		x = Operations.getMaxDegVertex(g);
		degx = -1;
		if (x != null)
			degx = g.degree(x);

		if (degx > k) {
			g.removeVertex(x);
			VC = this.kVertexCoverKernelizationBussInternal(g, k - 1);
			VC.add(x);
		}

		return VC;
	}
}
