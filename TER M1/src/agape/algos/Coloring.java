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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import agape.tools.Operations;

import com.google.common.collect.Sets;

import edu.uci.ics.jung.graph.Graph;

/**
 * The goal of this class is to compute the minimum number of colors to properly color an undirected graph.
 * @author V. Levorato
 * @author J.-F. Lalande
 * @author P. Berthome
 * @param <V> Vertices type
 * @param <E> Edges type
 */
public class Coloring<V,E> extends Algorithms<V,E> {
    
    //some constants
    final double alpha=0.19903; // for chromatic number
    
    /**
     * Constructor of the Coloring algorithms.
     * Needs a factory in order to create new graphs.
     * @param factory
     */
    public Coloring(Factory<Graph<V,E>> factory) {
        graphFactory = factory;
    };
    
    /**
     * @param Ginit the graph
     * @return a coloring assignment using a greedy algorithm that removes iteratively greedy MISs.
     */
    public Set<Set<V>> greedyGraphColoring(Graph<V,E> Ginit)
    {
        Graph<V, E> G = Operations.copyGraph(Ginit, this.graphFactory);
        Set<Set<V>> sol = new HashSet<Set<V>>();
        
        MIS<V, E> mis = new MIS<V, E>(this.graphFactory, this.vertexFactory, this.edgeFactory);
        
        while (G.getVertexCount() != 0)
        {
            Set<V> misSet = mis.maximalIndependentSetGreedy(G);
            sol.add(misSet);
            Operations.removeAllVertices(G, misSet);
        }

        return sol;
    
    }
    
    /**
     * Returns the chromatic number of a graph G. This method is based on the
     * Bodlaender and Kratsch work ("An exact algorithm for graph coloring
     * with polynomial memory" 2006). The algorithm solves the problem in PSPACE
     * and in time O(5.283^n).
     * This algorithm is limited to graphs having no more than 63 vertices. 
     * @param Ginit graph
     * @return chromatic number of G
     */
    public Set<Set<V>> graphColoring(Graph<V,E> Ginit)
    {
        Graph<V, E> G = Operations.copyGraph(Ginit, this.graphFactory);

        return graphColoringInternal(G);
    }

    /**
     * Returns the cpartition of the vertices into color classes of a graph G. 
     * This method is based on the
     * Bodlaender and Kratsch work ("An exact algorithm for graph coloring
     * with polynomial memory" 2006). The algorithm solves the problem in PSPACE
     * and in time O(5.283^n).
     * This algorithm is limited to graphs having no more than 63 vertices. 
     * @param G graph
     * @return the partition of the vertices into color classes.
     * @author P. Berthome, J.-F. Lalande, V. Levorato
     */
    protected Set<Set<V>> graphColoringInternal(Graph<V,E> G)
    {
      double n=G.getVertexCount();
      int best=(int) n;
      Set<Set<V>> bestSol = new HashSet<Set<V>>();
      long ncomb=(long) Math.pow(2,best);
      ArrayList<V> nodesArray = new ArrayList<V>();
      nodesArray.addAll(G.getVertices());

      for(long i=1; i<ncomb ; i++)
      {
          Set<V> S=new HashSet<V>();
          String comb=Long.toBinaryString(i);
          for(int k=0; k<comb.length() ; k++)
              if(comb.charAt(k)=='1')
                  S.add(nodesArray.get(comb.length() - k -1));
        
          if(MIS.isMaximalIndependentSet(G, S) && S.size()>=alpha*n)
          {
              Graph<V,E> GS=graphFactory.create();
              Operations.subGraph(G,GS,S);
              Operations.removeAllVertices(G, S);
              Set<Set<V>> sol = graphColoringInternal(G);
              Operations.mergeGraph(G,GS);
              best=Math.min(best, sol.size() +1);
              if (best == sol.size() +1) {
                  bestSol = sol;
                  bestSol.add(S);
              }
          }

          if( (n-alpha*n)/2 <= S.size() && S.size() <= (n+alpha*n)/2 )
          {
              Graph<V,E> GS=graphFactory.create();
              Set<V> V_S=new HashSet<V>();
              Sets.difference(new HashSet<V>(G.getVertices()), S).copyInto(V_S);
              Operations.subGraph(G,GS,V_S);
              Operations.removeAllVertices(G, V_S);
              Set<Set<V>> sol = graphColoringInternal(G);
              int cnGS = sol.size();
              Operations.mergeGraph(G,GS);

              Graph<V,E> G_S=graphFactory.create();
              Operations.subGraph(G,G_S,S);
              Operations.removeAllVertices(G, S);
              Set<Set<V>> sol2=graphColoringInternal(G);
              int cnG_S = sol2.size();
              Operations.mergeGraph(G,G_S);

              best=Math.min(best,cnGS + cnG_S);
              
              if(best == cnG_S + cnGS)
                  {
                  bestSol = sol;
                  sol.addAll(sol2);
                  }
          }
          
      }
      
      // If the graph is complete, no best solution have been found and best = n
      // The solution has to be constructed
      if(best==n){
          for(V v:G.getVertices()){
              Set<V> toAdd = new HashSet<V>();
              toAdd.add(v);
              bestSol.add(toAdd);
          }
      }

      return bestSol;
    }
    
    /**
     * Returns the chromatic number of a graph G. This method is based on the
     * Bodlaender and Kratsch work ("An exact algorithm for graph coloring
     * with polynomial memory" 2006). The algorithm solves the problem in PSPACE
     * and in time O(5.283^n).
     * This algorithm is limited to graphs having no more than 63 vertices. 
     * @param Ginit graph
     * @return chromatic number of G
     */
    public int chromaticNumberBodlaenderKratsch(Graph<V,E> Ginit)
    {
        Graph<V, E> G = Operations.copyGraph(Ginit, this.graphFactory);

        return chromaticNumberInternal(G);
    }

    /**
     * Returns the chromatic number of a graph G. This method is based on the
     * Bodlaender and Kratsch work ("An exact algorithm for graph coloring
     * with polynomial memory" 2006). The algorithm solves the problem in PSPACE
     * and in time O(5.283^n).
     * This algorithm is limited to graphs having no more than 63 vertices. 
     * @param G graph
     * @return chromatic number of G
     * @author P. Berthome, J.-F. Lalande, V. Levorato
     */
    protected int chromaticNumberInternal(Graph<V,E> G)
    {
        int n = G.getVertexCount();
        int best = n;

        long ncomb=(long) Math.pow(2,best);
        ArrayList<V> nodesArray = new ArrayList<V>();
        nodesArray.addAll(G.getVertices());

        for(long i=1; i<ncomb ; i++)
        {
            Set<V> S=new HashSet<V>();
            String comb=Long.toBinaryString(i);
            for(int k=0; k<comb.length() ; k++)
                if(comb.charAt(k)=='1')
                    S.add(nodesArray.get(comb.length() - k-1));
            
            if(MIS.isMaximalIndependentSet(G, S) && S.size()>=alpha*n)
            {
                Graph<V,E> GS=graphFactory.create();
                Operations.subGraph(G,GS,S);
                Operations.removeAllVertices(G, S);
                int cnG_S=chromaticNumberInternal(G);
                Operations.mergeGraph(G,GS);
                best=Math.min(best, 1+cnG_S);
            }

            if( (n-alpha*n)/2 <= S.size() && S.size() <= (n+alpha*n)/2 )
            {
                Graph<V,E> GS=graphFactory.create();
                
                Set<V> V_S=new HashSet<V>();
                Sets.difference(new HashSet<V>(G.getVertices()), S).copyInto(V_S);
                Operations.subGraph(G,GS,V_S);
                Operations.removeAllVertices(G, V_S);
                int cnGS=chromaticNumberInternal(G);
                Operations.mergeGraph(G,GS);

                Graph<V,E> G_S=graphFactory.create();
                Operations.subGraph(G,G_S,S);
                Operations.removeAllVertices(G, S);
                int cnG_S=chromaticNumberInternal(G);
                Operations.mergeGraph(G,G_S);

                best=Math.min(best,cnGS + cnG_S);
            }
        }

        return best;
      }
    
    private Map<String, Integer> S_0x ;
    
    protected void computeAi(Graph<V,E> g){
        S_0x = new HashMap<String, Integer>();

        int n = g.getVertexCount();
        Collection<V> Vset = g.getVertices();
        ArrayList<V> Varray = new ArrayList<V>(Vset);

        long N = (long) Math.pow(2.0D, n);

        String s = longToString(N-1, n);
        S_0x.put(s, 0);

        for(long i = N-2; i>=0; i--) {
            s = longToString(i, n);
            if( ! S_0x.containsKey(s)) {
                computeSx(g, s, Varray);

            }
        }
    }

    protected int computeSx(Graph<V,E> g, String X, ArrayList<V>     Varray) {
    	
    	// W = "0000000000000000000"
    	StringBuffer sbW = new StringBuffer(X.length());
    	for(int i = 0; i< X.length();i++)
    	{
    		sbW.replace(i, i+1, "0");
    	}
    	
    	return computeSWx(g, X, new String(sbW), Varray);
    }
    
    
    protected int computeSWx(Graph<V,E> g, String X, String W, ArrayList<V>     Varray) {

    	// W = "00000000000000000000"
    	// This is the value we need to compute for X
    	boolean W_full_0 = true;
    	for(int i = 0; i< W.length();i++) {
    		W_full_0 = W_full_0 && W.charAt(i)=='0';
    	}

    	// If W = "0000000000000000" we check if we have already computed the value
    	if (W_full_0)
    	{
    		Integer res = S_0x.get(X);
    		if (res != null)
    			return res;
    	}
    	
    	 Set<V> setW = new HashSet<V>();
    	 
    	 // sb = X union W
    	 boolean full_1 = true;
    	 //int W_number_of_1 = 0;
    	 for(int i = 0; i< W.length();i++)
    	 {
    		full_1 = full_1 && (W.charAt(i)=='1' || X.charAt(i)=='1');
    		if (W.charAt(i)=='1')
    			{
    			//	W_number_of_1 += 1;
    				setW.add(Varray.get(i));
    			}
    	 }
    	
    	 // X union W = "1111" = Varray = vertices set
    	 // W is the complementary set of X. We can compute S_W[X] by
    	 // checking if W is a MIS in G.
    	 if (full_1)
    	 {
    		 // if W represents a MIS in G, S_W[X] = 1 else 0
    		 if(MIS.isIndependentSet(g, setW)) return 1;
    		 else return 0;
    	 }
    	 
    	 // X union W does not cover the vertices
    	 // we have to find one missing vertex and introduce into the formula
    	 
    	 int missing_vertex = 0;
    	 while (! (W.charAt(missing_vertex)=='0' && X.charAt(missing_vertex)=='0'))
    		 missing_vertex++;
    	 
    	 StringBuffer SBX2 = new StringBuffer(X);
    	 SBX2.replace(missing_vertex, missing_vertex+1, "1");
    	 String X2 = new String(SBX2);
    	 int SwXv = computeSWx(g, X2, W, Varray);
    	 
    	 StringBuffer SBW2 = new StringBuffer(W);
    	 SBW2.replace(missing_vertex, missing_vertex+1, "1");
    	 String W2 = new String(SBW2);
    	 int SwvX = computeSWx(g, X, W2, Varray);
    	 
    	 if (W_full_0)
    		 S_0x.put(X, new Integer(SwXv + SwvX));
    	 
    	 return SwXv + SwvX;
}



    protected boolean isKColorable(int k) {

        long c_k_S = 0;

        for(String s : S_0x.keySet()) {
            int ai = S_0x.get(s);
            long ai_pow_k = (long) Math.pow(ai, k);

            // Calcul de (-1) ** (taille de s)  avec taille de s = nombre de 1 dans s
            int t = 0;
            for(int i = 0 ; i < s.length(); i++)
                if(s.charAt(i)=='1') t++;

            if (t%2==0)
                c_k_S += ai_pow_k;
            else
                c_k_S -= ai_pow_k;
        }

        return (c_k_S>0);
    }

    /**
     * Returns the chromatic number of a graph G. The complexity of this algorithm is O(n^2.2^n) in time 
     * and O(n.2^n) in space.
     * This method is based on the inclusion-exclusion algorithm proposed in 
     * A. Bjorklund and T. Husfeldt, “Inclusion--Exclusion Algorithms for Counting Set Partitions,” 
     * 47th Annual IEEE Symposium on Foundations of Computer Science (FOCS’06), pp. 575–582, 2006.
     *  
     * @param G graph
     * @return chromatic number of G
     * @author P. Berthome, J.-F. Lalande
     */
    public int chromaticNumberBjorklundHusfeldt(Graph<V,E> g) {
    	
    	computeAi(g);

    	// Lemma 3
    	int k = 1;
    	while(! isKColorable(k)) k++;
    	return k;
    }

    private static String longToString(long N, int k){
        StringBuffer sb = new StringBuffer();
        long R = N;
        for(int i = 0; i < k; i++){
            if(R%2 ==0) sb.append('0');
            else sb.append('1');

            R/=2;
        }
        return sb.toString();
    }
    
    
}
