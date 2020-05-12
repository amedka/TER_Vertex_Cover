package Algorithmes;

import java.util.HashSet;
import java.util.Set;

import Batterie.Graphe;
import agape.tools.Operations;

public class GreedyCoverMaxDegree extends AlgoApprox {

	@Override
	public int approx(int n, Graphe g) {
		Set<Integer> cover = new HashSet<Integer>();
		while (g.getGraphe().getEdgeCount() > 0) {
			Integer v = Operations.getMaxDegVertex(g.getGraphe());
			cover.add(v);
			g.getGraphe().removeVertex(v);

		}
		
		return cover.size();
	}

}
