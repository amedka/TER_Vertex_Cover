package Batterie;

import java.util.ArrayList;

import Algorithmes.BussGoldSmith;
import Algorithmes.Constraint;
import Algorithmes.DegreeBranchingStrategy;
import Algorithmes.VertexCover;

public class Application {

	public static void main(String[] args) {

		ArrayList<Graphe> mesGraphes;
		ArrayList<VertexCover> mesAlgos = new ArrayList<>();

		// cr�e un fichier "test1.txt" de 10 graphes de taille 200 avec 50% de chance
		// que chaque ar�te existe
		// m�thode de g�n�ration : ErdosRenyi
		GestionnaireDeFichiers.creerFichierErdosRenyi("test1.txt", 10, 300, 0.5);

		// r�cupere les graphes du fichier "test1.txt" et les ajoute dans une ArrayList
		mesGraphes = GestionnaireDeFichiers.recupererFichierGraphes("mesGraphes/ErdosRenyi/test1.txt");

		// test avec les algorithmes degreeBranchingStrategy et bussGoldMmith de la
		// bibliotheque Agape
		VertexCover bussGoldSmith = new BussGoldSmith();
		VertexCover degreeBranchingStrategy = new DegreeBranchingStrategy();
		VertexCover constraint = new Constraint();
		
		mesAlgos.add(degreeBranchingStrategy);
		mesAlgos.add(bussGoldSmith);
		mesAlgos.add(constraint);

		// cr�ation de la batterie de test
		BatterieTest b = new BatterieTest(mesGraphes, mesAlgos);
		// on test pour n=5
		b.runBatterie(5);

		// g�n�re le fichier des r�sultats au format csv
		GestionnaireDeFichiers.creerResultat("test1.csv", b.getResultats());

		// r�cup�re le fichier csv et affiche les courbes
		GestionnaireDeFichiers.afficheCourbe("test1.csv");

	}

}
