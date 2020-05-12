package Batterie;

//objet g�n�r� lors de l'appel de la m�thode run() d'un VertexCover
public class Resultat {

	private float tempsExec;
	private int reponse;
	private String nomAlgo;

	public Resultat() {
	}

	public Resultat(long l, int r, String s) {
		this.tempsExec = l;
		this.reponse = r;
		this.nomAlgo = s;
	}

	public float getTemps() {
		return this.tempsExec;
	}

	public int getRep() {
		return this.reponse;
	}

	public String getAlgo() {
		return this.nomAlgo;
	}

	public void setTemps(float l) {
		this.tempsExec = l;
	}

	public void setRep(int r) {
		this.reponse = r;
	}

	public void setAlgo(String s) {
		this.nomAlgo = s;
	}

	@Override
	public String toString() {
		String res = "";
		res += "Reponse :" + this.reponse + "\n";
		res += "Temps d'execution : " + this.tempsExec + " millisecondes\n";
		return res;
	}
}
