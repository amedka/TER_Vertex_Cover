/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import java.io.IOException;

import agape.io.Export;
import agape.io.Import;
import edu.uci.ics.jung.graph.Graph;

/**
 * @author jf
 */
public class IOTutorial {
	public static void main(String[] args) {
		Graph<String, Integer> g = Import.readDNet("src/agape/tutorials/IOTutorial.net");
		System.out.println(g);
		try {
			Export.writeGV("src/agape/tutorials/IOTutorial", g);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}