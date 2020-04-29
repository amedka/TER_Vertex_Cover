package Batterie;
import Algorithmes.*;
import java.util.ArrayList;
import java.util.HashMap;

//Une liste de graphes, une liste d'algos. Chaque algo est testé sur chaque graphe et génère un objet Résultat à chaque test.

public class BatterieTest {
	private ArrayList<Graphe> mesGraphes = new ArrayList<Graphe>();
	private ArrayList<VertexCover> mesAlgos = new ArrayList<VertexCover>();
	private ArrayList<Resultat> mesResultats = new ArrayList<Resultat>();
	private HashMap<String, Float> tempsTotalParAlgo = new HashMap<>();
	private HashMap<String, Float> tempsMoyenParAlgo = new HashMap<>();
	
	public BatterieTest(ArrayList<Graphe> g, ArrayList<VertexCover> a) {
		mesGraphes=g;
		mesAlgos=a;
	}
	
	//applique tous les algos sur tous les graphes et génère un objet Resultat à chaque essaie
	//pour un n donné
	public void runBatterie(int n) {
		System.out.println("///// LANCEMENT DE LA BATTERIE POUR LA VALEUR : "+n+" /////");
		Resultat currentResultat;
		//pour obtenir les valeurs de chaque algos
		int nbAlgos=0;
		float tempsTotalCourant = 0;
		
		String nomAlgoCourant="";
		
		for(int i=0; i<mesAlgos.size();i++) {
			nomAlgoCourant=mesAlgos.get(i).getClass().getSimpleName();
			//System.out.println("Test de l'algo : "+nomAlgoCourant);
			for(int j=0; j<mesGraphes.size();j++) {
				//System.out.println("Test sur le graphe suivant : ");
				//System.out.println(mesGraphes.get(j).getGraphe().toString());
				currentResultat = mesAlgos.get(i).run(n, mesGraphes.get(j));
				currentResultat.setAlgo(nomAlgoCourant);
				mesResultats.add(currentResultat);
				tempsTotalCourant+=currentResultat.getTemps();
				nbAlgos++;
			}
			tempsTotalParAlgo.put(nomAlgoCourant, tempsTotalCourant);
			tempsTotalParAlgo.put(nomAlgoCourant, tempsTotalCourant/nbAlgos);
			tempsTotalCourant=0;
			nbAlgos=0;
		}
	}
	
	//calcule le temps moyen total d'execution
	public float moyenneTotale() {
		float somme = 0;
		for(int i=0; i<mesResultats.size();i++) {
			somme+=mesResultats.get(i).getTemps();
		}
		return somme/mesResultats.size();
	}
	
	//calcule le temps total d'execution
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
