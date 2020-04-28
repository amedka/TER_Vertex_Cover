/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.algos;

import agape.tools.Operations;
import com.google.common.collect.Sets;
import edu.uci.ics.jung.graph.Graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections15.Factory;

/**
 * This class implements algorithms for solving the Minimum Vertex Cover problem. 
 * The problem consists in finding the minimum set of vertices such that any edge
 * is incident at one of the vertex of the computed set.
 * !! WARNING !!
 * These algorithms work ONLY on undirected graphs.
 * @author V. Levorato
 * @author J.-F. Lalande
 * @author P. Berthome
 * @param <V> Vertices type
 * @param <E> Edges type
 */
public class MVC<V,E> extends Algorithms<V,E> {

	//used by branching algorithms
	private Set<V> VCbuf = null; //for vertex cover

	private Set<V> VCFinal;

	public MVC(Factory<Graph<V,E>> graphFactory) {
		this.graphFactory = graphFactory;
	};

	/**
	 * Returns the last computed vertex cover set, null if the called 
	 * Parameterized algorithms cannot find such a set.
	 * 
	 * @return a vertex cover (null if there is no solution)
	 */
	public Set <V> getVertexCoverSolution()
	{
		if (VCFinal.size() == 0)
			return null;
		return VCFinal;
	}

	private void initSolution()
	{
		VCFinal = new HashSet<V>();
		VCbuf = new HashSet<V>(); // used by branching algorithm (to be initialized once)
	}


	/**
	 * Finds a 2-approximation for a minimal vertex cover of the specified
	 * graph. The algorithm promises a cover that is at most double the size of
	 * a minimal cover. The algorithm takes O(|E|) time.
	 * @param g the graph for which vertex cover approximation has to be found
	 * @return a set of vertices which is a vertex cover for the specified graph.
	 */
	public Set<V> twoApproximationCover(Graph<V,E> g) {

		initSolution();

		Graph<V,E> Gp=Operations.copyGraph(g, this.graphFactory);

		while (Gp.getEdgeCount() > 0)
		{
			E e = Gp.getEdges().iterator().next();

			V u = Gp.getEndpoints(e).getFirst();
			V v = Gp.getEndpoints(e).getSecond();
			VCFinal.add(u);
			VCFinal.add(v);

			Gp.removeVertex(u);
			Gp.removeVertex(v);
		}

		return VCFinal;
	}


	/**
	 * Finds a greedy approximation for a minimal vertex cover of a specified
	 * graph. At each iteration, the algorithm picks the vertex with the highest
	 * degree and adds it to the cover, until all edges are covered.
	 * @param g the graph for which vertex cover approximation has to be found
	 * @return a set of vertices which is a vertex cover for the specified graph.
	 */
	public Set<V> greedyCoverMaxDegree(Graph<V,E> g) {

		initSolution();

		Graph<V,E> Gp=Operations.copyGraph(g, this.graphFactory);

		while (Gp.getEdgeCount() > 0)
		{
			V v = Operations.getMaxDegVertex(Gp);
			VCFinal.add(v);
			Gp.removeVertex(v);
		}

		return VCFinal;
	}



	/**
	 * Returns true if a vertex cover of size k exists in graph G. 
	 * The complexity of this method is O(C_k^n) (exhaustive search).
	 * This algorithm test exhaustively all the combinations of k vertices among n. 
	 * The principle is to test all the sets of size k: we build recursively these sets by choosing a new vertex
	 * to add in the set. When all the neighborhood of the set covers the graph, we stop if the size of the set is less 
	 * than k. 
	 * @param g graph
	 * @param k vertex cover size
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	public boolean kVertexCoverBruteForce(Graph<V,E> g, int k)
	{
		initSolution();
		Graph<V,E> g2 = Operations.copyGraph(g, this.graphFactory);
		return kVertexCoverBruteForce(g2, k, VCFinal, new HashSet<E>(), new HashSet<V>(g2.getVertices()));
	}

	protected boolean kVertexCoverBruteForce(Graph<V,E> G, int k, Set<V> VCcurrent, Set<E> viewed, Set<V> remaining)
	{
		if (k <= 0 && remaining.size() > 0)
		{
			//System.out.println("Cannot cover !");
			return false;
		}
		//        System.out.println("CALL: viewed edges=" + viewed  + " / remaining nodes="+remaining);
		//        System.out.println("VCBuf: " + VCbuf);
		//        System.out.println("VCcurrent: " + VCcurrent);
		//        System.out.println("k=" + k);
		//        System.out.println("--------------------------------------------"); 

		// There is no edges left: the current VCbuf is a solution
		if(viewed.size() == G.getEdgeCount())
		{
			// If the current solution VCbuf is better than the VCcurrent
			// we should replace the best solution
			if(VCbuf.size()<VCcurrent.size() || VCcurrent.isEmpty())
			{ VCcurrent.clear(); VCcurrent.addAll(VCbuf); }

			return true;
		}

		// Trying each edge not viewed in the solution
		boolean res = false;

		Iterator<V>itv = remaining.iterator();
		while (itv.hasNext())
		{
			V v_considered_in_the_solution1 = itv.next();
			VCbuf.add(v_considered_in_the_solution1);

			HashSet<E> viewed2 = new HashSet<E>(viewed);
			HashSet<V> remaining2 = new HashSet<V>(remaining);

			// Updating viewed edges and remaining edges
			// before the recursion
			remaining2.remove(v_considered_in_the_solution1);
			viewed2.addAll(G.getIncidentEdges(v_considered_in_the_solution1));

			// Backup of VCbuf before recursion
			Set<V> oldVCbuf=new HashSet<V>(VCbuf);

			res = kVertexCoverBruteForce(G, k-1, VCcurrent, viewed2, remaining2) || res;
			// Restore of the backup of VCbuf
			VCbuf=oldVCbuf;
			VCbuf.remove(v_considered_in_the_solution1);
		}

		return res;
	}



	/**
	 * Returns true if a vertex cover of size k exists in graph G.
	 * The comupted set contains a cover of size at most k at the end of the algorithm.
	 * This method uses Degree-Branching-Strategy (DBS) and has a time complexity in O(1.47^k).
	 * @param g graph
	 * @param k vertex cover size
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	public boolean kVertexCoverDegreeBranchingStrategy(Graph<V,E> g, int k)
	{
		initSolution();
		Graph<V,E> g2 = Operations.copyGraph(g, this.graphFactory);
		return kVertexCoverDegreeBranchingStrategy(g2, k, VCFinal);
	}

	protected boolean kVertexCoverDegreeBranchingStrategy(Graph<V,E> G, int k, Set<V> VCcurrent)
	{
		//if(tracker!=null) tracker.increase("VC"); //tracker operation


		int nbe=G.getEdgeCount();

		if(nbe==0)
		{
			if(VCbuf.size()<VCcurrent.size() || VCcurrent.isEmpty())
			{ VCcurrent.clear(); VCcurrent.addAll(VCbuf);}

			return true;
		}

		if(nbe>= k*G.getVertexCount()) return false;

		V vone=Operations.getDegVertex(G,1);
		if(vone!=null)
		{
			Set<V> Nvone=new HashSet<V>(G.getNeighbors(vone));
			Graph<V,E> Gp=graphFactory.create();
			for(V v : Nvone)
				Operations.subGraph(G,Gp,v);

			VCbuf.addAll(Nvone);

			Operations.removeAllVertices(G, Nvone);
			boolean b=kVertexCoverDegreeBranchingStrategy(G,k-Nvone.size(),VCcurrent);
			Operations.mergeGraph(G,Gp);
			VCbuf.removeAll(Nvone);
			return b;
		}

		V vtwo=Operations.getDegVertex(G,2);
		if(vtwo!=null)
		{
			Set<V> Nvtwo2=Operations.getNeighbors(G, vtwo, 2);
			Set<V> Nvtwo=new HashSet<V>(G.getNeighbors(vtwo));
			Graph<V,E> GNv2=graphFactory.create();
			Graph<V,E> GNv=graphFactory.create();

			Operations.subGraph(G,GNv2,vtwo);
			for(V v : Nvtwo2)
				Operations.subGraph(G,GNv2,v);

			VCbuf.add(vtwo);
			VCbuf.addAll(Nvtwo2);
			G.removeVertex(vtwo);
			Operations.removeAllVertices(G, Nvtwo2);

			boolean a=kVertexCoverDegreeBranchingStrategy(G,k-Nvtwo2.size()-1,VCcurrent);

			Operations.mergeGraph(G,GNv2);
			VCbuf.remove(vtwo);
			VCbuf.removeAll(Nvtwo2);

			for(V v : Nvtwo)
				Operations.subGraph(G,GNv,v);
			VCbuf.addAll(Nvtwo);

			Operations.removeAllVertices(G, Nvtwo);

			boolean b=kVertexCoverDegreeBranchingStrategy(G,k-Nvtwo.size(),VCcurrent);

			Operations.mergeGraph(G,GNv);
			VCbuf.removeAll(Nvtwo);

			return a || b;
		}

		V vmax=Operations.getMaxDegVertex(G);
		if(vmax!=null && G.degree(vmax)>=3)
		{
			Set<V> Nvmax=new HashSet<V>(G.getNeighbors(vmax));
			Graph<V,E> Gvmax=graphFactory.create();
			Graph<V,E> GNvmax=graphFactory.create();

			Operations.subGraph(G,Gvmax,vmax);

			VCbuf.add(vmax);
			G.removeVertex(vmax);

			boolean a=kVertexCoverDegreeBranchingStrategy(G,k-1,VCcurrent);

			Operations.mergeGraph(G,Gvmax);
			VCbuf.remove(vmax);

			for(V v : Nvmax)
				Operations.subGraph(G,GNvmax,v);
			VCbuf.addAll(Nvmax);

			Operations.removeAllVertices(G, Nvmax);

			boolean b=kVertexCoverDegreeBranchingStrategy(G,k-Nvmax.size(),VCcurrent);

			Operations.mergeGraph(G,GNvmax);
			VCbuf.removeAll(Nvmax);

			return a || b;
		}

		return kVertexCoverDegreeBranchingStrategy(G,k,VCcurrent);

	}

	/**
	 * Returns true if a vertex cover of size k exists in graph G. 
	 * This method reduces a graph instance in polynomial time in order to find
	 * a vertex cover by kernelization using Buss and Goldsmith reduction
	 * ("Nondeterminism within P", 1993).
	 * @param g graph
	 * @param k vertex cover size
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	public boolean kVertexCoverBussGoldsmith(Graph<V,E> g, int k) {
		initSolution();
		Graph<V,E> g2 = Operations.copyGraph(g, this.graphFactory);
		return kVertexCoverKernel(g2, k, VCFinal);
	}

	/**
	 * Returns true if a vertex cover of size k exists in graph G. 
	 * This method reduces a graph instance in polynomial time in order to find
	 * a vertex cover by kernelization using Buss and Goldsmith reduction
	 * ("Nondeterminism within P", 1993).
	 * @param g graph
	 * @param k vertex cover size
	 * @param VCfinal set which contains the vertex cover
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	protected boolean kVertexCoverKernel(Graph<V,E> G, int k, Set<V> VCfinal)
	{
		// if(tracker!=null) tracker.increase("VC"); //tracker operation

		int nbe=G.getEdgeCount();

		if(nbe==0)
		{
			if(VCbuf.size()<VCfinal.size() || VCfinal.isEmpty())
			{ VCfinal.clear(); VCfinal.addAll(VCbuf);}

			return true;
		}

		if(nbe>= k*G.getVertexCount()) return false;

		Set<V> K=new HashSet<V>();
		V vmin = Operations.getMinDegVertex(G);
		int degmin=G.degree(vmin);
		int degmax=Operations.getMaxDeg(G);

		Graph<V,E> Gp=G;

		//conditions to make buss kernelization
		if(degmin==0 || degmin==1 || degmax>k)
		{
			Gp=Operations.copyUndirectedSparseGraph(G);
			K=kVertexCoverKernelizationBussInternal(Gp,k);
			k=k-K.size();
			VCbuf.addAll(K);
		}

		V vmax=Operations.getMaxDegVertex(Gp);
		if(vmax==null)
			return kVertexCoverKernel(Gp,k,VCfinal);

		Set<V> oldVCbuf=new HashSet<V>(VCbuf);

		int degvmax=Gp.degree(vmax);


		VCbuf.add(vmax);
		//memorize which vertex is removed (with his neighbourhood)
		Graph<V,E> Gvmax=graphFactory.create();
		Operations.subGraph(Gp,Gvmax,vmax);

		Gp.removeVertex(vmax);

		boolean a=kVertexCoverKernel(Gp,k-1,VCfinal);

		//reinsert removed vertex
		Operations.mergeGraph(Gp,Gvmax);


		VCbuf=new HashSet<V>(oldVCbuf);
		Graph<V,E> GNvmax=graphFactory.create();
		if(degvmax>0)
		{
			Set<V> Nvmax=new HashSet<V>(Gp.getNeighbors(vmax));

			//memorize which vertex is removed (with his neighbourhood)

			for(V nv : Nvmax)
				Operations.subGraph(Gp,GNvmax, nv);

			VCbuf.addAll(Nvmax);
			Operations.removeAllVertices(Gp, Nvmax);
		}

		boolean b=kVertexCoverKernel(Gp,k-degvmax,VCfinal);

		//reinsert removed vertices
		Operations.mergeGraph(Gp,GNvmax);


		return a || b;
	}



	/**
	 * Returns true if a vertex cover of size k exists in graph G. 
	 * The computed set contains a cover of size at most k at the end of the algorithm.
	 * This method is an implementation of Niedermeier algorithm 
	 * "Invitation to Fixed Parameter Algorithms", 2006, pages 98-101
	 * which time complexity is O(1.33^k).
	 * @param g graph
	 * @param k vertex cover size
	 * @return true if G contains a vertex cover of size k, else returns false.
	 */
	public boolean kVertexCoverNiedermeier(Graph<V,E> g, int k)
	{ 
		initSolution();
		Graph<V,E> g2 = Operations.copyGraph(g, this.graphFactory);
		return kVertexCoverNiedermeier(g2, k, VCFinal);
	}

	protected boolean kVertexCoverNiedermeier(Graph<V,E> g, int k, Set<V> VCfinal)
	{
		// if(tracker!=null) tracker.increase("VC"); //tracker operation

		int nbe=g.getEdgeCount();

		if(nbe==0)
		{
			if(VCbuf.size()<VCfinal.size() || VCfinal.isEmpty())
			{ VCfinal.clear(); VCfinal.addAll(VCbuf);}

			return true;
		}

		if(nbe>= k*g.getVertexCount()) return false;


		if(Operations.getMinDeg(g)==0)
		{
			Graph<V,E> Gp=graphFactory.create();
			Set<V> vZeros=Operations.getAllMinDegVertex(g);
			for(V v : vZeros)
				Operations.subGraph(g,Gp,v);
			Operations.removeAllVertices(g, vZeros);
			boolean b=kVertexCoverNiedermeier(g, k, VCfinal);
			Operations.mergeGraph(g,Gp);
			return b;
		}


		//if a vertex of deg=1 exists
				V vone=Operations.getDegVertex(g, 1);
		if(vone!=null)
		{
			Graph<V,E> Gp=graphFactory.create();
			V Nvone=g.getNeighbors(vone).iterator().next();
			VCbuf.add(Nvone);
			Operations.subGraph(g,Gp,Nvone);
			g.removeVertex(Nvone);
			boolean b=kVertexCoverNiedermeier(g, k-1, VCfinal);
			Operations.mergeGraph(g,Gp);
			VCbuf.remove(Nvone);
			return b;
		}

		//if a vertex of deg>=5 exists
				V vfivemore=Operations.getMaxDegVertex(g);

				if(g.degree(vfivemore)>=5)
				{
					Set<V> Nvfivemore=new HashSet<V>(g.getNeighbors(vfivemore));

					Graph<V,E> Gf=graphFactory.create();
					Graph<V,E> GNf=graphFactory.create();

					//branching on x
					Operations.subGraph(g,Gf,vfivemore);
					VCbuf.add(vfivemore);
					g.removeVertex(vfivemore);
					boolean bx=kVertexCoverNiedermeier(g,k-1,VCfinal);


					//restore to current state of the tree process
					Operations.mergeGraph(g,Gf);
					VCbuf.remove(vfivemore);

					//branching on N(x)
					VCbuf.addAll(Nvfivemore);
					for(V v : Nvfivemore)
						Operations.subGraph(g,GNf,v);

					Operations.removeAllVertices(g,Nvfivemore);

					boolean bnx=kVertexCoverNiedermeier(g,k-Nvfivemore.size(),VCfinal);

					Operations.mergeGraph(g,GNf);
					VCbuf.removeAll(Nvfivemore);

					return bx || bnx;
				}

				//if the graph is regular
				if(Operations.isRegular(g))
				{
					V v=g.getVertices().iterator().next();

					Set<V> Nv=new HashSet<V>(g.getNeighbors(v));

					Graph<V,E> Gv=graphFactory.create();
					Graph<V,E> GNv=graphFactory.create();

					//branching on x
					Operations.subGraph(g,Gv,v);
					VCbuf.add(v);
					g.removeVertex(v);
					boolean bx=kVertexCoverNiedermeier(g,k-1,VCfinal);


					//restore to current state of the tree process
					Operations.mergeGraph(g,Gv);
					VCbuf.remove(v);

					//branching on N(x)
					VCbuf.addAll(Nv);
					for(V x : Nv)
						Operations.subGraph(g,GNv,x);

					Operations.removeAllVertices(g,Nv);


					boolean bnx=kVertexCoverNiedermeier(g,k-Nv.size(),VCfinal);

					Operations.mergeGraph(g,GNv);
					VCbuf.removeAll(Nv);

					return bx || bnx;
				}


				//if vertex with deg=2 exists
				V vtwo=null;
				vtwo=Operations.getDegVertex(g, 2);

				if(vtwo!=null)
				{
					Iterator<V> itrvtwo=g.getNeighbors(vtwo).iterator();
					V va=itrvtwo.next();
					V vb=itrvtwo.next();

					//if there is an edge between a and b
					if(Operations.isEdge(g, va, vb))
					{
						Graph<V,E> Gp=graphFactory.create();
						VCbuf.add(va);
						VCbuf.add(vb);
						Operations.subGraph(g,Gp,va);
						Operations.subGraph(g,Gp,vb);
						g.removeVertex(va);
						g.removeVertex(vb);
						boolean b=kVertexCoverNiedermeier(g,k-2,VCfinal);
						Operations.mergeGraph(g,Gp);
						VCbuf.remove(va);
						VCbuf.remove(vb);
						return b;
					}

					//if there is no edge between a and b but a and b both have degree two with
					//a common neighbor c different from x
					if(!Operations.isEdge(g, va, vb) && g.degree(va)==2 && g.degree(vb)==2)
					{
						Set<V> Nva=new HashSet<V>(g.getNeighbors(va));
						Set<V> Nvb=new HashSet<V>(g.getNeighbors(vb));
						Nva.remove(vtwo);   Nvb.remove(vtwo);
						if(Nva.equals(Nvb))
						{
							Graph<V,E> Gp=graphFactory.create();
							VCbuf.add(vtwo);
							VCbuf.add(Nva.iterator().next());
							Operations.subGraph(g,Gp,vtwo);
							Operations.subGraph(g,Gp,Nva.iterator().next());
							g.removeVertex(vtwo);
							g.removeVertex(Nva.iterator().next());
							boolean b=kVertexCoverNiedermeier(g,k-2,VCfinal);
							Operations.mergeGraph(g,Gp);
							VCbuf.remove(vtwo);
							VCbuf.remove(Nva.iterator().next());
							return b;
						}
					}

					//branching on N(x) and N(a)∪N(b)
					Set<V> Nx=new HashSet<V>(g.getNeighbors(vtwo));
					Set<V> Na=new HashSet<V>(g.getNeighbors(va));
					Set<V> Nb=new HashSet<V>(g.getNeighbors(vb));
					Set<V> NaNb=new HashSet<V>();
					Sets.union(Na, Nb).copyInto(NaNb);

					Graph<V,E> GNx=graphFactory.create();
					Graph<V,E> GNaNb=graphFactory.create();

					//branching on N(x)
					VCbuf.addAll(Nx);
					for(V v : Nx)
						Operations.subGraph(g,GNx,v);

					Operations.removeAllVertices(g,Nx);
					boolean bnx=kVertexCoverNiedermeier(g,k-Nx.size(),VCfinal);

					//restore to current state of the tree process
					VCbuf.removeAll(Nx);
					Operations.mergeGraph(g,GNx);

					//branching on N(a)∪N(b)
					VCbuf.addAll(NaNb);
					for(V v : NaNb)
						Operations.subGraph(g,GNaNb,v);

					Operations.removeAllVertices(g,NaNb);
					boolean bnanb=kVertexCoverNiedermeier(g,k-NaNb.size(),VCfinal);

					Operations.mergeGraph(g,GNaNb);
					VCbuf.removeAll(NaNb);

					return bnx || bnanb;
				}

				//if vertex with deg=3 exists
				V vthree=null;
				vthree=Operations.getDegVertex(g, 3);

				if(vthree!=null)
				{
					Iterator<V> itrvthree=g.getNeighbors(vthree).iterator();
					V va=itrvthree.next();
					V vb=itrvthree.next();
					V vc=itrvthree.next();

					//tests is there is a triangle with x
					V t=null;
					if(Operations.isEdge(g, va, vb)) t=vc; //triangle {x,a,b}
					else
						if(Operations.isEdge(g, va, vc)) t=vb; //triangle {x,a,c}
						else
							if(Operations.isEdge(g, vb, vc)) t=va; //triangle {x,b,c}

					//if x is part of a triangle
					if(t!=null)
					{
						//branching on N(x) and N(t)
						Set<V> Nx=new HashSet<V>(g.getNeighbors(vthree));
						Set<V> Nt=new HashSet<V>(g.getNeighbors(t));


						Graph<V,E> GNx=graphFactory.create();
						Graph<V,E> GNt=graphFactory.create();

						//branching on N(x)
						VCbuf.addAll(Nx);
						for(V v : Nx)
							Operations.subGraph(g,GNx,v);

						Operations.removeAllVertices(g,Nx);
						boolean bnx=kVertexCoverNiedermeier(g,k-Nx.size(),VCfinal);


						//restore to current state of the tree process
						VCbuf.removeAll(Nx);
						Operations.mergeGraph(g,GNx);
						//branching on N(t)
						VCbuf.addAll(Nt);
						for(V v : Nt)
							Operations.subGraph(g,GNt,v);
						Operations.removeAllVertices(g,Nt);
						boolean bnt=kVertexCoverNiedermeier(g,k-Nt.size(),VCfinal);
						Operations.mergeGraph(g,GNt);
						VCbuf.removeAll(Nt);

						return bnx || bnt;
					}


					//test if there is a cycle of length four with x
					Set<V> Na=new HashSet<V>(g.getNeighbors(va));
					Set<V> Nb=new HashSet<V>(g.getNeighbors(vb));
					Set<V> Nc=new HashSet<V>(g.getNeighbors(vc));

					Set<V> N=new HashSet<V>();
					if(!Sets.intersection(Na, Nb).isEmpty())
						Sets.intersection(Na, Nb).copyInto(N);
					else
						if(!Sets.intersection(Na, Nc).isEmpty())
							Sets.intersection(Na, Nc).copyInto(N);
						else
							if(!Sets.intersection(Nb, Nc).isEmpty())
								Sets.intersection(Nb, Nc).copyInto(N);

					if(!N.isEmpty())
					{
						V vd=N.iterator().next();
						//branching on N(x) and {x,d}
						Set<V> Nx=new HashSet<V>(g.getNeighbors(vthree));
						Set<V> xd=new HashSet<V>();
						xd.add(vthree);
						xd.add(vd);

						Graph<V,E> GNx=graphFactory.create();
						Graph<V,E> Gxd=graphFactory.create();

						//branching on N(x)
						VCbuf.addAll(Nx);
						for(V v : Nx)
							Operations.subGraph(g,GNx,v);
						Operations.removeAllVertices(g,Nx);
						boolean bnx=kVertexCoverNiedermeier(g,k-Nx.size(),VCfinal);


						//restore to current state of the tree process
						VCbuf.removeAll(Nx);
						Operations.mergeGraph(g,GNx);
						//branching on N(t)
						VCbuf.addAll(xd);
						for(V v : xd)
							Operations.subGraph(g,Gxd,v);
						Operations.removeAllVertices(g,xd);
						boolean bxd=kVertexCoverNiedermeier(g,k-xd.size(),VCfinal);
						Operations.mergeGraph(g,Gxd);
						VCbuf.removeAll(xd);

						return bnx || bxd;
					}

					//branching on N(x), N(a), and {a}∪N(b)∪N(c)
					Set<V> Nx=new HashSet<V>(g.getNeighbors(vthree));

					Graph<V,E> GNx=graphFactory.create();
					Graph<V,E> GNa=graphFactory.create();
					Graph<V,E> GaNbNc=graphFactory.create();

					//branching on N(x)
					VCbuf.addAll(Nx);
					for(V v : Nx)
						Operations.subGraph(g,GNx,v);
					Operations.removeAllVertices(g,Nx);
					boolean bnx=kVertexCoverNiedermeier(g,k-Nx.size(),VCfinal);


					//restore to current state of the tree process
					VCbuf.removeAll(Nx);
					Operations.mergeGraph(g,GNx);

					//branching on N(a)
					VCbuf.addAll(Na);
					for(V v : Na)
						Operations.subGraph(g,GNa,v);
					Operations.removeAllVertices(g,Na);
					boolean bna=kVertexCoverNiedermeier(g,k-Na.size(),VCfinal);
					Operations.mergeGraph(g,GNa);

					//restore to current state of the tree process
					VCbuf.removeAll(Na);
					//branching on {a}∪N(b)∪N(c)
					Set<V> aNbNc=new HashSet<V>();
					aNbNc.add(va);
					aNbNc.addAll(Nb);
					aNbNc.addAll(Nc);
					VCbuf.addAll(aNbNc);
					for(V v : aNbNc)
						Operations.subGraph(g,GaNbNc,v);
					Operations.removeAllVertices(g,aNbNc);
					boolean banbnc=kVertexCoverNiedermeier(g,k-aNbNc.size(),VCfinal);
					Operations.mergeGraph(g,GaNbNc);
					VCbuf.removeAll(aNbNc);

					return bnx || bna || banbnc ;

				}

				return kVertexCoverNiedermeier(g,k,VCfinal);

	}




	protected Set<V> kVertexCoverKernelizationBussInternal(Graph<V,E> g, int k)
	{
		Set<V> VC = new HashSet<V>();
		if(g.getVertexCount()==0)
			return VC;

		V x = Operations.getMinDegVertex(g);
		int degx=g.degree(x);


		if(degx==0)
		{ g.removeVertex(x); return kVertexCoverKernelizationBussInternal(g,k); }

		if(degx==1)
		{
			V y=g.getNeighbors(x).iterator().next();
			g.removeVertex(x); g.removeVertex(y);
			VC=kVertexCoverKernelizationBussInternal(g,k-1);
			VC.add(y);
		}

		x = Operations.getMaxDegVertex(g);
		degx=-1;
		if(x!=null)
			degx=g.degree(x);

		if(degx>k)
		{   g.removeVertex(x); VC=kVertexCoverKernelizationBussInternal(g,k-1); 
		VC.add(x);
		}

		return VC;
	}
}
