package Batterie;

import java.util.ArrayList;
import java.util.HashMap;

import Algorithmes.VertexCover;

//Une liste de graphes, une liste d'algos. Chaque algo est testï¿½ sur chaque graphe et gï¿½nï¿½re un objet Rï¿½sultat ï¿½ chaque test.

public class BatterieTest {
	private ArrayList<Graphe> mesGraphes = new ArrayList<Graphe>();
	private ArrayList<VertexCover> mesAlgos = new ArrayList<VertexCover>();
	private ArrayList<Resultat> mesResultats = new ArrayList<Resultat>();
	private HashMap<String, Float> tempsTotalParAlgo = new HashMap<>();
	private HashMap<String, Float> tempsMoyenParAlgo = new HashMap<>();

	public BatterieTest(ArrayList<Graphe> g, ArrayList<VertexCover> a) {
		this.mesGraphes = g;
		this.mesAlgos = a;
	}

	// applique tous les algos sur tous les graphes et gï¿½nï¿½re un objet Resultat ï¿½
	// chaque essaie
	// pour un n donnï¿½
	public void runBatterie(int n) {
		System.out.println("///// LANCEMENT DE LA BATTERIE POUR LA VALEUR : " + n + " /////");
		Resultat currentResultat;
		// pour obtenir les valeurs de chaque algos
		int nbAlgos = 0;
		float tempsTotalCourant = 0;

		String nomAlgoCourant = "";

		for (int i = 0; i < this.mesAlgos.size(); i++) {
			nomAlgoCourant = this.mesAlgos.get(i).getClass().getSimpleName();
			// System.out.println("Test de l'algo : "+nomAlgoCourant);
			for (int j = 0; j < this.mesGraphes.size(); j++) {
				// System.out.println("Test sur le graphe suivant : ");
				// System.out.println(mesGraphes.get(j).getGraphe().toString());
				currentResultat = this.mesAlgos.get(i).run(n, this.mesGraphes.get(j));
				currentResultat.setAlgo(nomAlgoCourant);
				this.mesResultats.add(currentResultat);
				tempsTotalCourant += currentResultat.getTemps();
				nbAlgos++;
			}
			this.tempsTotalParAlgo.put(nomAlgoCourant, tempsTotalCourant);
			this.tempsTotalParAlgo.put(nomAlgoCourant, tempsTotalCourant / nbAlgos);
			tempsTotalCourant = 0;
			nbAlgos = 0;
		}
	}
	
	//run batterie en appliquant la kernalisation sur les graphes au préalable
	public void runBatterieKernel(int n) {
		System.out.println("/// KERNALISATION DES GRAPHES ///");
		ArrayList<Integer> kernelList = new ArrayList<>();
		for(int i=0; i<mesGraphes.size();i++) {
			kernelList.add(mesGraphes.get(i).kerFusion(n));
		}
		System.out.println("///// LANCEMENT DE LA BATTERIE POUR LA VALEUR : " + n + " /////");
		Resultat currentResultat;
		// pour obtenir les valeurs de chaque algos
		int nbAlgos = 0;
		float tempsTotalCourant = 0;

		String nomAlgoCourant = "";

		for (int i = 0; i < this.mesAlgos.size(); i++) {
			nomAlgoCourant = this.mesAlgos.get(i).getClass().getSimpleName();
			// System.out.println("Test de l'algo : "+nomAlgoCourant);
			for (int j = 0; j < this.mesGraphes.size(); j++) {
				// System.out.println("Test sur le graphe suivant : ");
				// System.out.println(mesGraphes.get(j).getGraphe().toString());
				currentResultat = this.mesAlgos.get(i).run(kernelList.get(j), this.mesGraphes.get(j));
				currentResultat.setAlgo(nomAlgoCourant);
				this.mesResultats.add(currentResultat);
				tempsTotalCourant += currentResultat.getTemps();
				nbAlgos++;
			}
			this.tempsTotalParAlgo.put(nomAlgoCourant, tempsTotalCourant);
			this.tempsTotalParAlgo.put(nomAlgoCourant, tempsTotalCourant / nbAlgos);
			tempsTotalCourant = 0;
			nbAlgos = 0;
		}
		
	}

	// calcule le temps moyen total d'execution
	public float moyenneTotale() {
		float somme = 0;
		for (int i = 0; i < this.mesResultats.size(); i++) {
			somme += this.mesResultats.get(i).getTemps();
		}
		return somme / this.mesResultats.size();
	}

	// calcule le temps total d'execution
	public float tempsTotal() {
		float somme = 0;
		for (int i = 0; i < this.mesResultats.size(); i++) {
			somme += this.mesResultats.get(i).getTemps();
		}
		return somme;
	}

	public ArrayList<Graphe> getGraphes() {
		return this.mesGraphes;
	}

	public void setGraphes(ArrayList<Graphe> g) {
		this.mesGraphes = g;
	}

	public ArrayList<VertexCover> getAlgos() {
		return this.mesAlgos;
	}

	public void setAlgos(ArrayList<VertexCover> a) {
		this.mesAlgos = a;
	}

	public ArrayList<Resultat> getResultats() {
		return this.mesResultats;
	}

}
