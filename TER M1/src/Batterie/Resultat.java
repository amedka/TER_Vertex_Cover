package Batterie;
//objet généré lors de l'appel de la méthode run() d'un VertexCover
public class Resultat {
	
	private float tempsExec;
	private boolean reponse;
	private String nomAlgo;
	
	public Resultat() {
	}
	
	public Resultat(long l, boolean r, String s) {
		tempsExec=l;
		reponse=r;
		nomAlgo=s;
	}
	
	public float getTemps() {
		return tempsExec;
	}
	
	public boolean getRep() {
		return reponse;
	}
	
	public String getAlgo() {
		return nomAlgo;
	}
	
	public void setTemps(float l) {
		tempsExec=l;
	}
	
	public void setRep(boolean r) {
		reponse=r;
	}
	
	public void setAlgo(String s) {
		nomAlgo=s;
	}
	
	public String toString() {
		String res = "";
		res += "Réponse :"+reponse+"\n";
		res += "Temps d'éxécution : "+tempsExec+" millisecondes\n";
		return res;
	}
}
