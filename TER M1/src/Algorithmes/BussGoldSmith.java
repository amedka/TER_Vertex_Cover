package Algorithmes;

import Batterie.Graphe;
import agape.algos.MVC;

public class BussGoldSmith extends 	AlgoExacte {

	@Override
	public boolean algo(int n, Graphe g) {
		MVC mvc = new MVC(g.getFactory());
		return mvc.kVertexCoverBussGoldsmith(g.getGraphe(), n);
	}

}
