package Batterie;
import agape.algos.*;
public class BruteForce extends VertexCover{

	@Override
	public boolean algo(int n, Graphe g) {
		System.out.println("Calcul des choses");
		MVC test = new MVC(g.getFactory());
		return test.kVertexCoverBruteForce(g.getGraphe(), n);
	}
	
}
