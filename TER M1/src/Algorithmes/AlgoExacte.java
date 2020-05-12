package Algorithmes;

import Batterie.Graphe;
import Batterie.Resultat;

public abstract class AlgoExacte extends VertexCover {
	public int approx(int n, Graphe g)
	{
		return -1;
	}
	public Resultat run(int n, Graphe g) {
		Resultat r = new Resultat();
		long startTime = System.nanoTime();
		// long startTime = System.currentTimeMillis();
		boolean b = this.algo(n, g);
		long endTime = System.nanoTime();
		// long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		if(b)
			r.setRep(-1);
		else
			r.setRep(-2);
		float dur = duration / 1000000F;
		r.setTemps(dur);
		// System.out.println(r);
		return r;
	}
}
