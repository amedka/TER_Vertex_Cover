package agape.test;

import java.util.ArrayList;

import org.apache.commons.collections15.Factory;

import agape.algos.Algorithms;
import agape.algos.Separators;
import agape.tools.Operations;
import edu.uci.ics.jung.graph.Graph;
import tools.dataStructures.Pair;

/**
 * This class contains some unfinished methods.
 *
 * @author V. Levorato
 */
public class Tests<V, E> {

	public static final int FGK = 1;
	public static final int MINSEP = 2;

	public static <V, E> Graph<V, E> generateHarderGraph(Graph<V, E> G, Factory<Graph<V, E>> gf, Factory<V> vf,
			Factory<E> ef, int method) {

		Algorithms<V, E> alg;

		ArrayList<Pair<V, V>> edges = new ArrayList<Pair<V, V>>();
		ArrayList<Pair<V, V>> antiedges = new ArrayList<Pair<V, V>>();

		V[] varray = (V[]) G.getVertices().toArray();
		for (int i = 0; i < varray.length; i++)
			for (int j = i; j < varray.length; j++)
				if (varray[i] != varray[j]) {
					if (Operations.isEdge(G, varray[i], varray[j]))
						edges.add(new Pair(varray[i], varray[j]));
					else
						antiedges.add(new Pair(varray[i], varray[j]));
				}

		int maxcall = 0;
		Graph<V, E> Gp = gf.create(), Gpp = gf.create(), Gmax = Operations.copyUndirectedSparseGraph(G);

		if (method == Tests.MINSEP) {

			alg = new Separators<V, E>();
			alg.setGraphFactoy(gf);
			alg.setEdgeFactoy(ef);
			alg.setVertexFactoy(vf);

			int absep = ((Separators) alg).getAllMinimalSeparators(G).size();

			if (absep > maxcall) {
				maxcall = absep;
			}

			Gpp = Operations.copyUndirectedSparseGraph(G);

			for (Pair<V, V> pedge : edges) {
				Gp = Operations.copyUndirectedSparseGraph(G);
				E edge = Gp.findEdge(pedge.getKey(), pedge.getValue());

				Gp.removeEdge(edge);

				for (Pair<V, V> paedge : antiedges) {
					Gpp = Operations.copyUndirectedSparseGraph(Gp);

					Gpp.addEdge(ef.create(), paedge.getKey(), paedge.getValue());

					int nsep = 0;

					absep = ((Separators) alg).getAllMinimalSeparators(Gpp).size();

					if (absep > nsep) {
						nsep = absep;
					}

					if (nsep > maxcall) {
						maxcall = nsep;
						Gmax = Operations.copyUndirectedSparseGraph(Gpp);
						System.out.println(maxcall);
					}

				}

			}

		}

//         if(method==FGK)
//         {
//
//             alg.MaximumIndependentSetFGK(G);
//
//             maxcall=alg.getTracker().get("FGK");
//
//             for(Pair<V,V> pedge : edges)
//             {
//                 Gp=Operations.copyGraph(G);
//                 E edge=Gp.findEdge(pedge.fst, pedge.snd);
//                 Gp.removeEdge(edge);
//                 for(Pair<V,V> paedge : antiedges)
//                 {
//                     Gpp=Operations.copyGraph(Gp);
//                     Gpp.addEdge(ef.create(), paedge.fst,paedge.snd);
//                     alg.initTracker();
//                     alg.MaximumIndependentSetFGK(Gpp);
//                     if(alg.getTracker().get("FGK")>maxcall)
//                         { maxcall=alg.getTracker().get("FGK"); Gmax=Tools.copyGraph(Gpp); System.out.println(maxcall); }
//                 }
//
//             }
//
//
//         }

		return Gmax;

	}

}
