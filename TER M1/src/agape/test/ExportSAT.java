package agape.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import agape.io.Import;
import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @author V. Levorato
 */
public class ExportSAT {

	public static void main(String[] args) {

		String fname = "test.sat";
		Graph<String, Integer> g = Import.readNet("GenGraphs/ER/ERgraph_n5p0.4.net");

		FileWriter fw = null;
		try {

			fw = new FileWriter(fname, false);
		} catch (Exception e) {
			System.err.println("Error writing: " + e);
		}

		BufferedWriter bfw = new BufferedWriter(fw);
		PrintWriter output = new PrintWriter(bfw);

		output.println("c graph n=" + g.getVertexCount() + " m=" + g.getEdgeCount());
		output.println("c");
		output.println("p cnf " + g.getVertexCount() + " " + g.getEdgeCount() * 4);

		HashMap<String, Integer> index = new HashMap<String, Integer>();
		int i = 1;
		for (String v : g.getVertices()) {
			index.put(v, i);
			i++;
		}

		for (String v : g.getVertices()) {
			for (String nv : g.getNeighbors(v)) {
				output.println(index.get(v) + " -" + index.get(nv) + " 0");
				output.println("-" + index.get(v) + " " + index.get(nv) + " 0");
			}

		}

		output.flush();
		output.close();
	}
}
