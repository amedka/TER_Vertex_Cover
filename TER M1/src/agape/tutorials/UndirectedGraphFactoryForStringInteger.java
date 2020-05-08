/*
 * Copyright University of Orleans - ENSI de Bourges.
 * Source code under CeCILL license.
 */
package agape.tutorials;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class UndirectedGraphFactoryForStringInteger implements Factory<Graph<String, Integer>> {

	public UndirectedGraphFactoryForStringInteger() {
		super();
	}

	public UndirectedGraphFactoryForStringInteger(int incV, int incE) {
		this();
		this.incV = incV;
		this.incE = incE;
	}

	public VertexFactoryForString vertexFactory = new UndirectedGraphFactoryForStringInteger.VertexFactoryForString();
	public EdgeFactoryForinteger edgeFactory = new UndirectedGraphFactoryForStringInteger.EdgeFactoryForinteger();
	public int incV = +1;
	public int incE = -1;

	@Override
	public Graph<String, Integer> create() {
		return new UndirectedSparseGraph<String, Integer>();
	}

	public class VertexFactoryForString implements Factory<String> {
		int c = 0;

		@Override
		public String create() {
			this.c = this.c + UndirectedGraphFactoryForStringInteger.this.incV;
			return "v" + this.c;
		}
	}

	public class EdgeFactoryForinteger implements Factory<Integer> {
		int c = 0;

		@Override
		public Integer create() {
			this.c = this.c + UndirectedGraphFactoryForStringInteger.this.incE;
			return this.c;
		}
	}
}