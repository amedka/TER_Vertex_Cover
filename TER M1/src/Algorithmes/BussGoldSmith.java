package Algorithmes;
import Batterie.*;
import agape.algos.MVC;

public class BussGoldSmith extends VertexCover{

	@Override
	public boolean algo(int n, Graphe g) {
		MVC mvc = new MVC(g.getFactory());
		return mvc.kVertexCoverBussGoldsmith(g.getGraphe(), n);
	}
	
}
