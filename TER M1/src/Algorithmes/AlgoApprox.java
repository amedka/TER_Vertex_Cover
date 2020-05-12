package Algorithmes;

import Batterie.Graphe;
import Batterie.Resultat;

public abstract class AlgoApprox extends VertexCover {

	public boolean algo(int n, Graphe g) {
		return false;
	}
	
	public Resultat run(int n, Graphe g) {
		Resultat r = new Resultat();
		long startTime = System.nanoTime();
		// long startTime = System.currentTimeMillis();
		int reponse = this.approx(n, g);
		long endTime = System.nanoTime();
		// long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		r.setRep(reponse);
		float dur = duration / 1000000F;
		r.setTemps(dur);
		// System.out.println(r);
		return r;
	}
	
	
}
