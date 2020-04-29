package Batterie;
import Algorithmes.*;
import java.util.ArrayList;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Application {
	
	public static void main(String[] args) {
		
		ArrayList<Graphe> mesGraphes;
		ArrayList<VertexCover> mesAlgos=new ArrayList<>();
		
		//crée un fichier "test1.txt" de 10 graphes de taille 200 avec 50% de chance que chaque arête existe
		//méthode de génération : ErdosRenyi
		GestionnaireDeFichiers.creerFichierErdosRenyi("test1.txt", 10, 300, 0.5);
		
		//récupere les graphes du fichier "test1.txt" et les ajoute dans une ArrayList
		mesGraphes = GestionnaireDeFichiers.recupererFichierGraphes("mesGraphes/ErdosRenyi/test1.txt");
		
		//test avec les algorithmes degreeBranchingStrategy et bussGoldMmith de la bibliotheque Agape 
		VertexCover bussGoldSmith = new BussGoldSmith();
		VertexCover degreeBranchingStrategy = new DegreeBranchingStrategy();
		mesAlgos.add(degreeBranchingStrategy);
		mesAlgos.add(bussGoldSmith);
		
		//création de la batterie de test
		BatterieTest b = new BatterieTest(mesGraphes, mesAlgos);
		//on test pour n=5
		b.runBatterie(5);
		
		//génère le fichier des résultats au format csv
		GestionnaireDeFichiers.creerResultat("test1.csv", b.getResultats());
		
		//récupère le fichier csv et affiche les courbes
		GestionnaireDeFichiers.afficheCourbe("test1.csv");

	}

}
