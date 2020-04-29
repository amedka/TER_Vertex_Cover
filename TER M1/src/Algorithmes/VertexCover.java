package Algorithmes;
import Batterie.*;

public abstract class VertexCover {

	//à implémenter par les génies
	public abstract boolean algo(int n, Graphe g);
	
	//test de l'algo pour un graphe et un n donné
	//retourne un objet Resultat qui contient le temps d'exec et la réponse oui ou non.
	public Resultat run(int n, Graphe g) {
		Resultat r = new Resultat();
		long startTime = System.nanoTime();
		//long startTime = System.currentTimeMillis();
		boolean reponse = algo(n, g);
		long endTime = System.nanoTime();
		//long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		r.setRep(reponse);
		float dur = duration/1000000F;
		r.setTemps(dur);
		//System.out.println(r);
		return r;
	}
	
}
