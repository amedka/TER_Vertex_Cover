/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.algos;

import agape.tools.Components;
import agape.tools.Operations;
import com.google.common.collect.Sets;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.collections15.Factory;

/**
 * This class computes the maximum independ set of a graph (directed or undirected). 
 * This problem consists in finding the maximum set of vertices such that two vertices 
 * of the computed set are not neighbors in the original graph. Several algorithms have 
 * been implemented.
 * @author V. Levorato
 * @author J.-F. Lalande
 * @author P. Berthome
 * @param <V> Vertices type
 * @param <E> Edges type
 */
public class MIS<V,E> extends Algorithms<V,E> {
    
  
  public MIS(Factory<Graph<V,E>> graphFactory, 
              Factory<V> vertexFactory, Factory<E> edgeFactory) {
      this.graphFactory = graphFactory;
      this.vertexFactory = vertexFactory;
      this.edgeFactory = edgeFactory;
  };
      
  /**
   * This method finds a maximal independent set in a graph using a trivial
   * greedy algorithm. The complexity of this algorithm is polynomial (O(n+m)).
   * @param g Graph in which a maximal Independent set has to be found
   * @return a maximal independent set of G
   */

    public Set<V> maximalIndependentSetGreedy(Graph<V,E> g)
    {

       Graph<V,E> Gp=Operations.copyGraph(g, this.graphFactory);


        Set<V> S=new HashSet<V>();
        Set<V> M=new HashSet<V>();

        while(!Gp.getVertices().isEmpty())
        {
            for(V v : M)
                Gp.removeVertex(v);

            V x=Operations.getMinDegVertex(Gp);

            if(x!=null)
            {
                S.add(x);
                M.add(x);

                //Set<V> N=new HashSet<V>();
                if(Gp.getNeighbors(x)!=null)
                {
                        /*N.addAll(Gp.getNeighbors(x));
                        for(V y : N )
                            M.add( y);*/
                        M.addAll(Gp.getNeighbors(x));
                }
            }

        }


        return S;

    }


    /**
     * This method returns a maximum independent set of G using the naive brute
     * force algorithm that examines every vertex subset and checks whether it
     * is an independent set, which time complexity is O(n^2.2^n).
     * @param g Graph in which Maximum Independent sets have to be found
     * @return a maximum independent set of G
     */
    public Set<V> maximumIndependentSetBruteForce(Graph<V,E> g)
    {
        Set<V> S=new HashSet<V>();

        long ncomb=(long) Math.pow(2,g.getVertexCount());
        @SuppressWarnings("unchecked")
		V nodes[]=(V[]) g.getVertices().toArray();

        for(long i=0; i<ncomb ; i++)
        {
            Set<V> Sp=new HashSet<V>();
            String comb=Long.toBinaryString(i);
            for(int k=0; k<comb.length() ; k++)
                if(comb.charAt(k)=='1')
                    Sp.add(nodes[k]);

            if(isIndependentSet(g,Sp) && Sp.size()>S.size())
                S=Sp;
        }


        return S;
    }


    /**
     * This method returns a maximum independent set of G based on the Moon
     * and Moser work ("On cliques in graphs" 1965).
     * The algorithm solves the problem in time O(1.4423^n).
     * @param g Graph in which Maximum Independent sets have to be found
     * @return A maximum independent set of G
     */
    public Set<V> maximumIndependentSetMoonMoser(Graph<V,E> g)
    {
       Set<V> S = new HashSet<V>();
               
       return maximumIndependentSetMoonMoser(g,S);
    }
    
    protected Set<V> maximumIndependentSetMoonMoser(Graph<V,E> G, Set<V> S)
    {

        if(!G.getVertices().isEmpty())
        {
            V v=Operations.getMinDegVertex(G);

            Set<V> Nv=new HashSet<V>();
            Nv.addAll(G.getNeighbors(v));
            Nv.add(v);

            Set<V> Smax= new HashSet<V>();


            for(V u : Nv)
            {


                Graph<V, E> Gp=Operations.copyUndirectedSparseGraph(G);

                Set<V> Nu=new HashSet<V>();
                Nu.addAll(G.getNeighbors(u));
                Nu.add(u);

                Set<V> Stemp= new HashSet<V>();
                Set<V> Su = new HashSet<V>();
                Su.addAll(S);
                Su.add(u);

                for(V nu : Nu)
                {
                    Gp.removeVertex(nu);
                }


                Stemp=maximumIndependentSetMoonMoser(Gp,Su);
                if(Stemp.size()>Smax.size())
                    Smax=Stemp;
            }

            return Smax;
        }
        else
            return S;
    }

   /**
     * This method is the same as MaximumIndependentSetMM but in a non-recursive
     * version (slower).
     * @param g Graph in which Maximum Independent sets have to be found
     * @return A maximum independent set of G
     */
    public Set<V> maximumIndependentSetMoonMoserNonRecursive(Graph<V,E> g)
    {

        Stack<Graph<V,E>> stackG=new Stack<Graph<V, E>>();
        Stack<Set<V>> stackS=new Stack<Set<V>>();

        Set<V> Smax= new HashSet<V>();

        stackG.push(g);
        stackS.push(new HashSet<V>());

        while(!stackG.empty())
        {

            Graph<V,E> G=stackG.pop();
            Set<V> S=stackS.pop();

            if(!G.getVertices().isEmpty())
            {
                    V v=Operations.getMinDegVertex(G);

                    Set<V> Nv=new HashSet<V>();
                    Nv.addAll(G.getNeighbors(v));
                    Nv.add(v);

                    for(V u : Nv)
                    {

                        
                        Graph<V,E> Gp=Operations.copyUndirectedSparseGraph(G);

                        Set<V> Nu=new HashSet<V>();
                        Nu.addAll(G.getNeighbors(u));
                        Nu.add(u);

                        Set<V> Su = new HashSet<V>();
                        Su.addAll(S);
                        Su.add(u);

                        for(V nu : Nu)
                            Gp.removeVertex(nu);



                        stackG.push(Gp);
                        stackS.push(Su);

                    }
             }
             else
                 if(S.size()>Smax.size())
                            Smax=S;

        }

        return Smax;
    }


    /**
     * This method returns a maximum independent set of G by finding vertices with
     * max degrees.
     * @param g Graph in which Maximum Independent sets have to be found
     * @return A maximum independent set of G
     */
    public Set<V> maximumIndependentSetMaximumDegree(Graph<V,E> g)
    {
        Graph<V,E> G3 = Operations.copyGraph(g, this.graphFactory);
            
        int degmax=Operations.getMaxDeg(G3);
        Set<V> S=new HashSet<V>();

        if(degmax>=3)
        {
            V x=Operations.getMaxDegVertex(G3);
            Set<V> Nx=new HashSet<V>();
            Nx.addAll(G3.getNeighbors(x));
            Nx.add(x);

            Graph<V,E> Gp=graphFactory.create();

            // Backup the subgraph induced by x and its neighborhood
            for(V nx : Nx)
                Operations.subGraph(G3,Gp,nx);

             for(V nx : Nx)
                 G3.removeVertex(nx);

             S=maximumIndependentSetMaximumDegree(G3);

             Operations.mergeGraph(G3,Gp);

             S.add(x);

             Set<V> S_=new HashSet<V>();
             Graph<V,E> Gpp=graphFactory.create();
             Operations.subGraph(G3,Gpp,x);
             G3.removeVertex(x);
             S_=maximumIndependentSetMaximumDegree(G3);
             Operations.mergeGraph(G3,Gpp);

             if(S_.size()>S.size())
                 S=S_;
        }
        else {
            // In this case, degmax <= 2: the greedy heuristic provides 
            // the good result
            S=maximalIndependentSetGreedy(G3);
        }


        return S;

    }

     /**
     * This method returns a maximum independent set of G based on the Fomin and al.
     * work ("A Measure & Conquer Approach for the Analysis of Exact Algorithms" 2009).
     * The algorithm solves the problem in time O(1.2201^n).
     * @param g graph
     * @return a maximum independent set in G.
     */
      public Set<V> maximuRmIndependentSetFominGrandoniKratsch(Graph<V,E> g)
     {

         //if(tracker!=null)
           // tracker.increase("FGK"); //tracker operation

         //(1)
         int Vsize=g.getVertices().size();
         if(Vsize<=1)
             return new HashSet<V>(g.getVertices());

         //(2)
         Set<V> C=Components.getConnectedComponent(g);
         if(!C.isEmpty() && C.size()<Vsize)
         {
             Graph<V,E> GC=Operations.copyUndirectedSparseGraph(g);
             Graph<V,E> G_C=Operations.copyUndirectedSparseGraph(g);

             Set<V> toKeep=new HashSet<V>(g.getVertices());
             for(V c : toKeep)
                 if(!C.contains(c))
                     GC.removeVertex(c);

             for(V c : C)
                 G_C.removeVertex(c);
             
             Set<V> U=new HashSet<V>();
             U.addAll(maximuRmIndependentSetFominGrandoniKratsch(GC));
             U.addAll(maximuRmIndependentSetFominGrandoniKratsch(G_C));

             return U;
         }

         //(3)
         V v=getDominatedVertex(g);
         if(v!=null)
         {
             //if(tracker!=null) tracker.increase("DomV"); //tracker operation
             Graph<V,E> G_v=Operations.copyUndirectedSparseGraph(g);
             G_v.removeVertex(v);
             return maximuRmIndependentSetFominGrandoniKratsch(G_v);
         }

         //(4)
         boolean deg2=false;
         V x=null;
         Iterator<V> itr=g.getVertices().iterator();
         while(!deg2 && itr.hasNext())
         {
             x=itr.next();
             if(g.getNeighbors(x).size()==2)
                 deg2=true;
         }

         if(deg2)
         {
             //if(tracker!=null) tracker.increase("Fold"); //tracker operation
             Set<V> uF=new HashSet<V>();
             HashMap<V,Set<V>> vftable=new  HashMap<V, Set<V>>();
             Set<V> F=maximuRmIndependentSetFominGrandoniKratsch(getFoldingGraph(g, x, vftable,vertexFactory, edgeFactory));

             //unfolding
             boolean fold=false;
             for(V f : F)
                 if(vftable.containsKey(f))
                     { uF.addAll(vftable.get(f)); fold=true; }
                  else
                      uF.add(f);

             if(!fold)
                 uF.add(x);

                 return uF;
         }



         //(5)
         Set<V> Mdeg=Operations.getAllMaxDegVertex(g);
         V d=null;
         int ENv=Integer.MAX_VALUE;
         for(V y : Mdeg)
         {
             int ENy=Operations.getNbEdges(g,new HashSet<V>(g.getNeighbors(y)));
             if(ENy<ENv)
                 { ENv=ENy; d=y; }
         }

         //(6)
         Graph<V,E> G_v_Mv=Operations.copyUndirectedSparseGraph(g);
         Graph<V,E> G_Nv=Operations.copyUndirectedSparseGraph(g);


         G_v_Mv.removeVertex(d);
         Set<V> M=getMirrors(g, d);
         for(V m : M)
                G_v_Mv.removeVertex(m);

         Set<V> Nd=new HashSet<V>(g.getNeighbors(d));
         for(V nd : Nd)
             G_Nv.removeVertex(nd);
         G_Nv.removeVertex(d);

         Set<V> S1=maximuRmIndependentSetFominGrandoniKratsch(G_v_Mv);
         Set<V> S2=maximuRmIndependentSetFominGrandoniKratsch(G_Nv);

         S2.add(d);

         if(S1.size()>S2.size())
             { //if(tracker!=null) tracker.increase("M branch("+M.size()+")"); //tracker operation
             return S1;}
         else
             {  //if(tracker!=null) tracker.increase("Nv branch"); //tracker operation
             return S2; }
 
     }
      
      /**
     * This method tests if a set S of a Graph G is an independent set.
     * @param g graph
     * @param S set to test
     * @return true if S is an independent set, false if not.
     */
    public static<V,E> boolean isIndependentSet(Graph<V,E> g, Set<V> S)
    {
        boolean b=true;

        Iterator<V> itr=S.iterator();

        while(b && itr.hasNext())
        {
            V v=itr.next();
            Collection<V> N=g.getNeighbors(v);
            if(N!=null)
            {
                Set<V> Nv=new HashSet<V>(N);
                if(!Sets.intersection(S,Nv).isEmpty())
                    b=false;
            }
        }

        return b;
    }

   /**
 * This method tests if a set S of a Graph G is a maximal independent set.
 * @param g graph
 * @param S set to test
 * @return true if S is a maximal independent set, false if not.
 */
    public static<V,E> boolean isMaximalIndependentSet(Graph<V,E> g, Set<V> S)
    {
        boolean b=true;

        if(isIndependentSet(g,S))
        {
            Set<V> NS=new HashSet<V>(g.getVertices());
            NS.removeAll(S);
            Iterator<V> itr=NS.iterator();
            while(b && itr.hasNext())
            {
                V v=itr.next();
                if(Sets.intersection(new HashSet<V>(g.getNeighbors(v)),S).isEmpty())
                    b=false;
            }
        }
        else
            b=false;

        return b;
    }
    
    
   /**
     * Returns a Graph where v has been folded (Fomin, Grandoni and Kratsch, 2009).
     * The fold parameter permits to memorize folding operation and to unfold vertices
     * if needed.
     * @param G Graph where to fold v.
     * @param v vertex used to fold.
     * @param fold hashtable where keys are merged vertices and where values are these
     * same vertices unmerged.
     * @return a Graph where v has been folded.
     */
    protected static<V,E> Graph<V,E> getFoldingGraph(Graph<V,E> G, V v, HashMap<V,Set<V>> fold, Factory<V> vertexFactory, Factory<E> edgeFactory)
    {
        Graph<V,E> Gv=Operations.copyUndirectedSparseGraph(G);
        ArrayList<V> newv=new ArrayList<V>();

        // Step (1) and (2)
        Set<V> Nv=new HashSet<V>();
        Nv.addAll(G.getNeighbors(v));
        ArrayList<V> Nvlist=new ArrayList<V>(Nv);

        for(int i=0;i<Nvlist.size()-1;i++)
            for(int j=i+1;j<Nvlist.size();j++)
                {
                        V ui=Nvlist.get(i);
                        V uj=Nvlist.get(j);

                         if(!Operations.isEdge(G, ui, uj))
                         {
                             // Step (1)
                             V uij = vertexFactory.create();
                             
                             Gv.addVertex(uij);

                             //memorize folding operation
                             Set<V> vfolded=new HashSet<V>();
                             vfolded.add(ui);
                             vfolded.add(uj);
                             fold.put(uij, vfolded);


                             // Step(2)
                             for(V nui : G.getNeighbors(ui))
                               if(!Operations.isEdge(G,uij,nui))
                               { 
                                   E ei=edgeFactory.create();
                                   while(Gv.getEdges().contains(ei))
                                        ei = edgeFactory.create();
                                   Gv.addEdge(ei,uij,nui);
                               }

                             for(V nuj : G.getNeighbors(uj))
                               if(!Operations.isEdge(G,uij,nuj))
                                 { 
                                   E ei=edgeFactory.create();
                                   while(Gv.getEdges().contains(ei))
                                        ei = edgeFactory.create();
                                   Gv.addEdge(ei,uij,nuj);
                                 }


                             // for step (3)
                             newv.add(uij);
                         }

                }

        
       // Step(3)
       for(int i=0;i<newv.size()-1;i++)
            for(int j=i+1;j<newv.size();j++)
                if(!Operations.isEdge(G,newv.get(i),newv.get(j)))
                    { 
                        E ei= edgeFactory.create();
                        Gv.addEdge(ei,newv.get(i),newv.get(j));
                    }

       // Step(4)
       for(V x : Nv)
           Gv.removeVertex(x);

       Gv.removeVertex(v);

        return Gv;
    }

    /**This method returns the vertices mirrors of v in Graph G.
     * (Fomin, Grandoni and Kratsch, 2009).
     * @param G Graph
     * @param v vertex
     * @return mirrors of v
     */
    protected static<V,E> Set<V> getMirrors(Graph<V,E> G, V v)
    {
         Set<V> Mv=new  HashSet<V>();
         Set<V> U=new HashSet<V>();
         Set<V> Nv=new HashSet<V>();
         Nv.addAll(G.getNeighbors(v));

         //selecting possible vertices u
         U=Operations.getNeighbors(G,v,2);

         //test for each u if N(v)/N(u) is a clique
         //if true, u is a mirror of v
         for(V u : U)
         {
             Set<V> C=new HashSet<V>();
             Sets.difference(Nv, new HashSet<V>(G.getNeighbors(u))).copyInto(C);
             if(Operations.isClique(G,C))
                 Mv.add(u);
         }


         return Mv;
    }

    /**
     * If there is a vertex v and w in a Graph G such that N[w] ⊆ N[v], w dominates
     * v and v is returned.
     * @param G Graph where to find dominating nodes.
     * @return a dominating vertex or null if no dominating vertex is found.
     */
    protected static<V,E> V getDominatedVertex(Graph<V,E> G)
    {
        V v=null;
        HashMap<Set<V>,V> dom=new HashMap<Set<V>, V>();

        //list all neighborhoods
        ArrayList<Set<V>> N=new ArrayList<Set<V>>();
        for(V x : G.getVertices())
        {
            Set<V> Nx=new HashSet<V>();
            Nx.addAll(new HashSet<V>(G.getNeighbors(x)));
            Nx.add(x);
            dom.put(Nx, x);    
            N.add(Nx);
        }

        //sort getNeighborss by size (ascending)
        Operations.quickSortSet(N,0,N.size()-1);

        boolean vfound=false;
        int i=N.size()-1;

        while(i>0 && !vfound)
        {
            Set<V> Nv=N.get(i);
            int j=0;

            while(j<i && !vfound)
            {
                Set<V> Nw=N.get(j);
                if(Nv.containsAll(Nw))
                {
                    v=dom.get(Nv);
                    vfound=true;
                }

                j++;
            }

            i--;
        }

        return v;

    }

    
}
