package Algorithmes;
import Batterie.*;

import agape.algos.*;
public class BruteForce extends VertexCover{

	@Override
	public boolean algo(int n, Graphe g) {
		MVC mvc = new MVC(g.getFactory());
		return mvc.kVertexCoverBruteForce(g.getGraphe(), n);
	}
	
}
