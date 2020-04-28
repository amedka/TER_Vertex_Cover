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
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class provides methods for ab-separators problem.
 * @author V. Levorato
 * @author J.-F. Lalande
 * @author P. Berthome
 * @param <V> Vertices type
 * @param <E> Edges type
 */
public class Separators<V,E> extends Algorithms<V,E> {
    
      public Separators() { };
    
    /**
     * Returns the set of minimal ab-separator between vertices a and b in O(n^3) per separator.
     * The implementation is based on an algorithm of Berry, Bordat, Cogis
     * in ("Generating All the Minimal Separators of a Graph", 2000).
     * 
     * @param g graph
     * @param a vertex a
     * @param b vertex b
     * @return a set of minimal ab-separator.
     */
    public Set<Set<V>> getABSeparators(Graph<V,E> g, V a, V b)
        {

            Set<Set<V>> SS=new HashSet<Set<V>>();

            if(!g.getNeighbors(a).contains(b))
            {
                Set<V> Na=new HashSet<V>();
                Na.addAll(g.getNeighbors(a));
                Na.add(a);

                ArrayList<Set<V>> CC=Components.getAllConnectedComponent(g,Na);

                for(Set<V> C : CC)
                {
                    if(C.contains(b))
                    {
                        Set<V> Nc=Operations.getNeighbors(g, C);
                        if(!Nc.isEmpty())
                            SS.add(Nc);
                    }
                }

              Set<Set<V>> T=new HashSet<Set<V>>();
              Set<Set<V>> SdT=new HashSet<Set<V>>(SS);

              while(!SdT.isEmpty())
              {
                  Set<V> S=SdT.iterator().next();


                   for(V x : S)
                   {
                       Set<V> SNx=new HashSet<V>(S);
                       SNx.addAll(g.getNeighbors(x));

                       CC=Components.getAllConnectedComponent(g,SNx);


                           for(Set<V> C : CC)
                           {

                              Set<V> Nc=Operations.getNeighbors(g, C);
                              if(!Nc.isEmpty() && C.contains(b))
                                 SS.add(Nc);
                           }
                    }

                   T.add(S);

                   SdT=new HashSet<Set<V>>(SS);
                   SdT.removeAll(T);

                  }

            }

              return SS;
          }


    /**
     * Return all the minimal separators of G.
     * @param g graph
     * @return a set with all minimal separators of g.
     */
        public Set<Set<V>> getAllMinimalSeparators(Graph<V,E> g)
        {
              Set<Set<V>> SS=new HashSet<Set<V>>();

              for(V v : g.getVertices())
              {
                    Set<V> Nv=new HashSet<V>();
                    Nv.addAll(g.getNeighbors(v));
                    Nv.add(v);

                    ArrayList<Set<V>> CC=Components.getAllConnectedComponent(g,Nv);

                    for(Set<V> C : CC)
                    {
                        Set<V> Nc=Operations.getNeighbors(g, C);
                        if(!Nc.isEmpty())
                            SS.add(Nc);
                    }
              }

              Set<Set<V>> T=new HashSet<Set<V>>();
              Set<Set<V>> SdT=new HashSet<Set<V>>(SS);

              while(!SdT.isEmpty())
              {
                  Set<V> S=SdT.iterator().next();

                   for(V x : S)
                   {
                       Set<V> SNx=new HashSet<V>(S);
                       SNx.addAll(g.getNeighbors(x));


                       ArrayList<Set<V>> CC=Components.getAllConnectedComponent(g,SNx);


                       for(Set<V> C : CC)
                       {
                          Set<V> Nc=Operations.getNeighbors(g, C);
                          if(!Nc.isEmpty())
                                  SS.add(Nc);
                       }

                    }

                   T.add(S);

                   SdT=new HashSet<Set<V>>(SS);
                   SdT.removeAll(T);

              }      

              return SS;
          }
}
