package Algorithmes;

import Batterie.Graphe;
import agape.algos.MVC;

public class BruteForce extends VertexCover {

	@Override
	public boolean algo(int n, Graphe g) {
		MVC mvc = new MVC(g.getFactory());
		return mvc.kVertexCoverBruteForce(g.getGraphe(), n);
	}

}
