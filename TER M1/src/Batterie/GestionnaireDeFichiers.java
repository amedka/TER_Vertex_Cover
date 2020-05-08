package Batterie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.opencsv.CSVReader;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

//cette classe ne poss�de que des m�thodes statiques
//permet de g�n�rer/r�cuperer des fichiers contenant des graphes et des r�sultats
//+ g�n�ration de courbes

public class GestionnaireDeFichiers {

	// g�n�re un ensemble de graphes avec la m�thode ErdosRenyi
	// stock les graphes dans un fichier.txt
	public static void creerFichierErdosRenyi(String titreFichier, int nbGraphes, int nbSommets, double probability) {
		try {
			PrintWriter writer = new PrintWriter("mesGraphes/ErdosRenyi/" + titreFichier);
			for (int i = 0; i < nbGraphes; i++) {
				Graphe g = new Graphe(nbSommets, probability);
				writer.println(g.getGraphe().toString());

			}
			writer.close();
			System.out.println("Le fichier " + titreFichier + " a �t� cr�e correctement.");
			System.out.println(nbGraphes + " graphes de taille " + nbSommets + " ont �t� g�n�r�s.");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// r�cupere un ensemble de graphes � partir d'un fichier.txt, les instancie et
	// les retourne
	public static ArrayList<Graphe> recupererFichierGraphes(String chemin) {
		BufferedReader lecteurAvecBuffer = null;
		String ligne;
		ArrayList<Graphe> mesGraphes = new ArrayList<>();
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(chemin));
		} catch (FileNotFoundException exc) {
			System.out.println("Erreur d'ouverture");
		}
		try {
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				Graph<Integer, String> jungGraphe = new UndirectedSparseGraph<Integer, String>();
				String vertices = ligne.substring(9, ligne.length());
				String[] verticesArray = vertices.split(",");
				for (int i = 0; i < verticesArray.length; i++) {
					jungGraphe.addVertex(Integer.parseInt(verticesArray[i]));
				}

				ligne = lecteurAvecBuffer.readLine();
				String edges = ligne.substring(6, ligne.length());
				String[] edgesArray = edges.split(" ");
				for (int i = 0; i < edgesArray.length; i++) {
					String[] currentString = edgesArray[i].substring(1, edgesArray[i].length() - 1).split("\\[");
					String title = currentString[0];
					String s1 = currentString[1].split(",")[0];
					String s2 = currentString[1].split(",")[1];
					jungGraphe.addEdge(title, Integer.parseInt(s1), Integer.parseInt(s2));
				}
				Graphe g = new Graphe();
				g.setGraphe(jungGraphe);
				mesGraphes.add(g);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			lecteurAvecBuffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mesGraphes;

	}

	// �crit un fichier de r�sultat
	public static void creerResultat(String titreFichier, ArrayList<Resultat> mesResultats) {
		try {
			Resultat resCourant;
			PrintWriter writer = new PrintWriter("Resultats/" + titreFichier);
			writer.println("Algorithme utilis�, R�sultat, Temps d'�x�cution (en millisecondes)");
			for (int i = 0; i < mesResultats.size(); i++) {
				resCourant = mesResultats.get(i);
				writer.println(resCourant.getAlgo() + "," + resCourant.getRep() + "," + resCourant.getTemps());
			}

			writer.close();
			System.out.println("Les r�sultats ont �t� cr�� correctement.");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// affiche courbe
	public static void afficheCourbe(String chemin) {
		System.out.println("affichage des temps d'�x�cutions");
		// lire le csv et g�n�re une hashmap avec comme cl� l'algo et comme valeur la
		// liste des temps d'�xec
		CSVReader reader;
		try {
			HashMap<String, ArrayList<Float>> map = new HashMap<>();
			reader = new CSVReader(new FileReader("Resultats/" + chemin));

			String[] nextLine;
			reader.readNext();
			while ((nextLine = reader.readNext()) != null) {
				if (map.containsKey(nextLine[0])) {
					map.get(nextLine[0]).add(Float.parseFloat(nextLine[2]));
				} else {
					ArrayList<Float> a = new ArrayList<Float>();
					a.add(Float.parseFloat(nextLine[2]));
					map.put(nextLine[0], a);
				}
			}
			// affichage des courbes

			TestChart ex = new TestChart(map);
			ex.setVisible(true);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
