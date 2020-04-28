package Batterie;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class Application {
	public static void main(String[] args) {
		
		//je crée une liste de deux graphes random
		ArrayList<Graphe> mesGraphes = new ArrayList<Graphe>();
		
		for(int i=0; i<7; i++) {
			mesGraphes.add(new Graphe(10,0.1));
		}
		/*
		Graphe random100 = new Graphe(100,0.1);
		Graphe random10 = new Graphe(10,0.5);
		random10.visualization();
		mesGraphes.add(random10);
		mesGraphes.add(random100);
		*/
		//je crée une liste d'algo a partir de deux implementations 
		ArrayList<VertexCover> mesAlgos = new ArrayList<VertexCover>();
		VertexCover v1 = new BruteForce();
		//VertexCover v2 = new VertexCover2();
		mesAlgos.add(v1);
		//mesAlgos.add(v2);
		
		//je génère les résultats pour n=10
		BatterieTest b = new BatterieTest(mesGraphes, mesAlgos);
		b.runBatterie(3);
		System.out.println("Temps d'exec moyen : "+b.moyenne()+" millisecondes");
		System.out.println("Temps d'exec total : "+b.tempsTotal()+" millisecondes");		
	}
}
