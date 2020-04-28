/*
 * Copyright University of Orleans - ENSI de Bourges
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 */
package agape.algos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used for Maximum Acyclic Set algorithm (Razgon, 2007).
 * 
 * It implements equivalence classes of integers (and may be used to classify nodes in a graph) 
 * In the class, an equivalence class is called 'role' for a set of elements.
 * 
 * @author V. Levorato
 */

public class MarkMap<V> {


  private HashMap<Integer,Set<V>> mapISet;
  private HashMap<V,Integer> mapVInt;

  public  MarkMap() { mapISet=new HashMap<Integer, Set<V>>(); mapVInt=new HashMap<V, Integer>(); }

  public  MarkMap(MarkMap<V> m)
  {
      this();
      for(Integer k:m.getISetMap().keySet())
          this.put(k, new HashSet<V>(m.getISetMap().get(k)));

      for(V v:m.getVIntMap().keySet())
          this.put(v, m.getVIntMap().get(v));
      
  }


  public HashMap<Integer,Set<V>> getISetMap()
  { return mapISet; }

  public HashMap<V,Integer> getVIntMap()
  { return mapVInt; }

  private void put(int role, Set<V> S)
  {  mapISet.put(role, S); }

  private void put(V v, int role)
  { mapVInt.put(v, role); }

  public void addV(V v, int role)
  {
      if(mapISet.get(role)==null)
        mapISet.put(role, new HashSet<V>());

      mapISet.get(role).add(v);
      mapVInt.put(v, role);
  }

  public void initRole(int role)
  {
      mapISet.put(role, new HashSet<V>());
  }

  /**
   * 
   * @param role
   * @return the set of nodes associated to this role
   */
  public Set<V> getVertices(int role)
  { return mapISet.get(role); }


  /**
   * 
   * @param role1
   * @param role2
   * @return the union of the nodes associated to both roles role1 and role2
   */
  public Set<V> getVertices(int role1, int role2)
  {
      Set<V> S=new HashSet<V>();
      S.addAll(mapISet.get(role1));
      S.addAll(mapISet.get(role2));
      return S;
  }

  /**
   * Changes the role of a given node
   * @param v
   * @param role
   */
  public void changeRole(V v, int role)
  {
      int role_orig = mapVInt.get(v);

      if(role!=role_orig)
      {
          mapISet.get(role_orig).remove(v);
          mapISet.get(role).add(v);
          mapVInt.put(v, role);
      }
  }

  /**
   * Changes the role of a set of nodes
   * @param s
   * @param role
   */
  public void changeRole(Set<V> s, int role)
  {
      for(V v:s)
          changeRole(v, role);
  }

}
