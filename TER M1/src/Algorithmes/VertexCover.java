package Algorithmes;

import Batterie.Graphe;
import Batterie.Resultat;

public abstract class VertexCover {
	public abstract int approx(int n, Graphe g);
	public abstract boolean algo(int n, Graphe g);
	// test de l'algo pour un graphe et un n donn�
	// retourne un objet Resultat qui contient le temps d'exec et la r�ponse oui ou
	// non.

	public abstract Resultat run(int n, Graphe g);

}
