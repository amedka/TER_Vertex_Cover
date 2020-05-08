package Algorithmes;

import Batterie.Graphe;
import Batterie.Resultat;

public abstract class VertexCover {

	// � impl�menter par les g�nies
	public abstract boolean algo(int n, Graphe g);

	// test de l'algo pour un graphe et un n donn�
	// retourne un objet Resultat qui contient le temps d'exec et la r�ponse oui ou
	// non.
	public Resultat run(int n, Graphe g) {
		Resultat r = new Resultat();
		long startTime = System.nanoTime();
		// long startTime = System.currentTimeMillis();
		boolean reponse = this.algo(n, g);
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
