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

//cette classe ne possï¿½de que des mï¿½thodes statiques
//permet de gï¿½nï¿½rer/rï¿½cuperer des fichiers contenant des graphes et des rï¿½sultats
//+ gï¿½nï¿½ration de courbes

public class GestionnaireDeFichiers {

	// gï¿½nï¿½re un ensemble de graphes avec la mï¿½thode ErdosRenyi
	// stock les graphes dans un fichier.txt
	public static void creerFichierErdosRenyi(String titreFichier, int nbGraphes, int nbSommets, double probability) {
		try {
			PrintWriter writer = new PrintWriter("mesGraphes/ErdosRenyi/" + titreFichier);
			for (int i = 0; i < nbGraphes; i++) {
				Graphe g = new Graphe(nbSommets, probability);
				writer.println(g.getGraphe().toString());

			}
			writer.close();
			System.out.println("Le fichier " + titreFichier + " a ete ecrit correctement.");
			System.out.println(nbGraphes + " graphes de taille " + nbSommets + " ont ete genere.");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// rï¿½cupere un ensemble de graphes ï¿½ partir d'un fichier.txt, les instancie et
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

	// ï¿½crit un fichier de rï¿½sultat
	public static void creerResultat(String titreFichier, ArrayList<Resultat> mesResultats) {
		try {
			Resultat resCourant;
			//c'est un résultat exacte
			if(mesResultats.get(0).getRep()<0) {
				PrintWriter writer = new PrintWriter("Resultats/Exact_" + titreFichier);
				writer.println("Algorithme utilise, Resultat, Temps d'execution (en millisecondes)");
				for (int i = 0; i < mesResultats.size(); i++) {
					resCourant = mesResultats.get(i);
					if(resCourant.getRep()==-2)
						writer.println(resCourant.getAlgo() + ", FALSE ," + resCourant.getTemps());
					else
						writer.println(resCourant.getAlgo() + ", TRUE ," + resCourant.getTemps());
				}

				writer.close();
				System.out.println("Les resultats ont ete ecrit correctement.");
				
			}
			//c'est un resultat d'approximation
			else {
				PrintWriter writer = new PrintWriter("Resultats/Approx_" + titreFichier);
				writer.println("Algorithme utilise, Resultat, Temps d'execution (en millisecondes)");
				for (int i = 0; i < mesResultats.size(); i++) {
					resCourant = mesResultats.get(i);
					writer.println(resCourant.getAlgo() + "," + resCourant.getRep() + "," + resCourant.getTemps());
				}

				writer.close();
				System.out.println("Les resultats ont ete ecrit correctement.");
				
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// affiche courbe
	public static void afficheCourbe(String chemin) {
		System.out.println("affichage des temps d'executions");
		// lire le csv et gï¿½nï¿½re une hashmap avec comme clï¿½ l'algo et comme valeur la
		// liste des temps d'ï¿½xec
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
