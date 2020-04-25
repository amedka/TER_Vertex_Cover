//objet généré lors de l'appel de la méthode run() d'un VertexCover
public class Resultat {
	
	private long tempsExec;
	private boolean reponse;
	
	public Resultat() {
	}
	
	public Resultat(long l, boolean r) {
		tempsExec=l;
		reponse=r;
	}
	
	public long getTemps() {
		return tempsExec;
	}
	
	public boolean getRep() {
		return reponse;
	}
	
	public void setTemps(long l) {
		tempsExec=l;
	}
	
	public void setRep(boolean r) {
		reponse=r;
	}
	
	public String toString() {
		String res = "";
		res += "Réponse :"+reponse+"\n";
		res += "Temps d'éxécution : "+tempsExec+"\n";
		return res;
	}
}
