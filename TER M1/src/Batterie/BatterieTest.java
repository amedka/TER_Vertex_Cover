package Batterie;
import java.util.ArrayList;

//Une liste de graphes, une liste d'algos. Chaque algo est testé sur chaque graphe et génère un objet Résultat à chaque test.

public class BatterieTest {
	private ArrayList<Graphe> mesGraphes = new ArrayList<Graphe>();
	private ArrayList<VertexCover> mesAlgos = new ArrayList<VertexCover>();
	private ArrayList<Resultat> mesResultats = new ArrayList<Resultat>();
	
	public BatterieTest(ArrayList<Graphe> g, ArrayList<VertexCover> a) {
		mesGraphes=g;
		mesAlgos=a;
	}
	
	//applique tous les algos sur tous les graphes et génère un objet Resultat à chaque essaie
	//pour un n donné
	public void runBatterie(int n) {
		for(int i=0; i<mesAlgos.size();i++) {
			for(int j=0; j<mesGraphes.size();j++) {
				System.out.println("Test de l'algo : "+mesAlgos.get(i).getClass());
				System.out.println("Avec le graphe : ");
				System.out.println(mesGraphes.get(j));
				System.out.println("//lancement de l'algo VertexCover//");
				mesResultats.add(mesAlgos.get(i).run(n, mesGraphes.get(j)));
			}
		}
	}
	
	//calcule le temps moyen d'exec
	public float moyenne() {
		float somme = 0;
		for(int i=0; i<mesResultats.size();i++) {
			somme+=mesResultats.get(i).getTemps();
		}
		return somme/mesResultats.size();
	}
	
	public float tempsTotal() {
		float somme = 0;
		for(int i=0; i<mesResultats.size();i++) {
			somme+=mesResultats.get(i).getTemps();
		}
		return somme;
	}
	
	
	public ArrayList<Graphe> getGraphes(){
		return mesGraphes;
	}
	
	public void setGraphes(ArrayList<Graphe> g) {
		mesGraphes=g;
	}
	
	public ArrayList<VertexCover> getAlgos(){
		return mesAlgos;
	}
	
	public void setAlgos(ArrayList<VertexCover> a) {
		mesAlgos=a;
	}
	
	public ArrayList<Resultat> getResultats(){
		return mesResultats;
	}
	

}
