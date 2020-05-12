package Batterie;

import java.util.ArrayList;

import Algorithmes.BussGoldSmith;
import Algorithmes.Constraint;
import Algorithmes.DegreeBranchingStrategy;
import Algorithmes.GreedyCoverMaxDegree;
import Algorithmes.VertexCover;

public class Application {

	public static void main(String[] args) {


		GestionnaireDeFichiers.creerFichierErdosRenyi("test1.txt", 10, 100, 0.5);
		
		ArrayList<Graphe> mesGraphes;
		mesGraphes = GestionnaireDeFichiers.recupererFichierGraphes("mesGraphes/ErdosRenyi/test1.txt");

		VertexCover bussGoldSmith = new BussGoldSmith();
		VertexCover degreeBranchingStrategy = new DegreeBranchingStrategy();
		VertexCover constraint = new Constraint();
		VertexCover greedyCoverMaxDegree = new GreedyCoverMaxDegree();
		
		ArrayList<VertexCover> mesAlgosExact = new ArrayList<>();
		mesAlgosExact.add(degreeBranchingStrategy);
		mesAlgosExact.add(bussGoldSmith);
		mesAlgosExact.add(constraint);
		
		ArrayList<VertexCover> mesAlgosApprox = new ArrayList<>();
		mesAlgosApprox.add(greedyCoverMaxDegree);

		// batterie destinee a des algos exacts
		BatterieTest b = new BatterieTest(mesGraphes, mesAlgosExact);
		b.runBatterieKernel(5);
		GestionnaireDeFichiers.creerResultat("test1.csv", b.getResultats());
		GestionnaireDeFichiers.afficheCourbe("Exact_test1.csv");
		
		//batterie destinee a des algos d'approx
		BatterieTest b2 = new BatterieTest(mesGraphes, mesAlgosApprox);
		b2.runBatterie(5);
		GestionnaireDeFichiers.creerResultat("test2.csv", b2.getResultats());
		GestionnaireDeFichiers.afficheCourbe("Approx_test2.csv");

	}

}
