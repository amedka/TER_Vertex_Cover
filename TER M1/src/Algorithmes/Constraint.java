package Algorithmes;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import Batterie.Graphe;

public class Constraint extends VertexCover {

	private Model model;

	private IntVar[] sommets;

	private IntVar total;

	@Override
	public boolean algo(int n, Graphe graph) {
		this.model = new Model();

		this.sommets = this.model.intVarArray("sommets", graph.getGraphe().getVertexCount(), 0, 1);

		this.buildConstraints(graph);

		this.total = this.model.intVar("total", 0, this.sommets.length);

		this.model.sum(this.sommets, "=", this.total).post();

		this.model.setObjective(Model.MINIMIZE, this.total);

		return this.model.getSolver().solve();
	}

	private void buildConstraints(Graphe graphe) {
		for (String ar : graphe.getGraphe().getEdges()) {
			Integer[] adjacents = new Integer[2];
			graphe.getGraphe().getIncidentVertices(ar).toArray(adjacents);
			this.model.arithm(this.sommets[adjacents[0]], "+", this.sommets[adjacents[1]], ">=", 1).post();
		}
	}
}
